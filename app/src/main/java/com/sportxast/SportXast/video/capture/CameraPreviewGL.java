package com.sportxast.SportXast.video.capture;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.hardware.Camera;
import android.opengl.GLSurfaceView;
import android.os.Messenger;
import android.util.Log;
import android.view.SurfaceHolder;

import java.io.IOException;

public class CameraPreviewGL  extends GLSurfaceView implements Camera.PreviewCallback, SurfaceHolder.Callback {
    private static final String TAG = "CameraPreview";

    private final Camera camera;
    private SurfaceHolder mHolder;
    int mX, mY;
    private int pixelFormat;
    private TextureRenderer mRenderer;
    //private AsyncNV21Decoder nv21Decoder;
    private byte[] mData;
    private int[] mDataRGB8888;
    private int counter = 0;
    Messenger mService = null;
    boolean mIsBound;
	private CaptureListener vcListner;

    public CameraPreviewGL(Context context, Camera camera, CaptureListener vcListner) {
        super(context);
        this.camera = camera;
        this.vcListner = vcListner;
        super.getHolder().addCallback(this);
        // required for API <= 11
        super.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        final ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        final ConfigurationInfo configurationInfo = activityManager.getDeviceConfigurationInfo();
        final boolean supportsEs2 = configurationInfo.reqGlEsVersion >= 0x20000;
        if (supportsEs2)
        {
            mRenderer = new TextureRenderer(context);
            this.setEGLContextClientVersion(2);
            this.setRenderer(mRenderer);
            this.setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
        }
        
    }

    public void surfaceCreated(SurfaceHolder holder) {
        Log.d(TAG, "surfaceCreated()");
        // now that we have the surface, we can start the preview
        try {
            this.camera.setPreviewDisplay(holder);
            this.camera.startPreview();
        } catch (IOException e) {
            Log.wtf(TAG, "Failed to start camera preview", e);
        }
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        // we will release the camera preview in our activity before this
        // happens
        Log.d(TAG, "surfaceDestroyed()");
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
        // our activity runs with screenOrientation="landscape" so we don't
        // care about surface changes
        Log.d(TAG, "surfaceChanged()");
        if( this.camera != null ) {
            try {
                this.camera.setPreviewDisplay(holder);
                this.camera.startPreview();

	    		Camera.Parameters parameters = this.camera.getParameters();
	
	            pixelFormat = parameters.getPreviewFormat();
	            mX = vcListner.getCameraPreviewFrame().getWidth();
	            mY = vcListner.getCameraPreviewFrame().getHeight();
	            mX = (mX/4) * 4;
	            mY = (mY/4) * 4;
	            parameters.setPreviewSize( mX, mY);
	            this.camera.setParameters(parameters);
	            parameters = this.camera.getParameters();
	            Camera.Size size = parameters.getPreviewSize();
	            mX = size.width;
	            mY = size.height;
	            //nv21Decoder = new AsyncNV21Decoder(mX, mY);
	            //nv21Decoder.start();
	            mData = new byte[mX * mY * 3 / 2];
	            this.camera.addCallbackBuffer(mData);
	            this.camera.setPreviewCallback(this);
	            mDataRGB8888 = new int[mX * mY];
	            vcListner.startRecording();
	        } catch (IOException e) {
	            Log.wtf(TAG, "Failed to start camera preview", e);
	        }
        }
    }

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
        // System.arraycopy(data, 0, mData, 0, data.length);
        Camera.Parameters parameters = this.camera.getParameters();
        Camera.Size s = parameters.getPreviewSize();
        //if( nv21Decoder != null ) nv21Decoder.processBuffer( data );
        mRenderer.drawFrame(s.width, s.height, data);
        this.requestRender();

/*
		 System.arraycopy(data, 0, mData , 0, s.width * s.height * 3 / 2);


		 Bitmap bmp = getBitmapFromNV21(mData, s.width, s.height );
		 mRenderer.loadTexture(s.width, s.height, bmp);
		 mGLSurfaceView.requestRender();
*/
        this.camera.addCallbackBuffer(mData);

    }
}