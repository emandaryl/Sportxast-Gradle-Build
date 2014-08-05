package com.sportxast.SportXast.activities2_0;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import com.sportxast.SportXast.R;
import com.sportxast.SportXast.commons.GlobalVariablesHolder;

public class VideoFullScreenActivity extends Activity {

	private VideoView videoView;

    private int isAutoPlay;

    private VideoView videovw_expand;

    private int FCallingActivityID;
	/** Called when the activity is first created. */
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.video_fullscreen);
        // TODO Auto-generated method stub

        videovw_expand = (VideoView) findViewById(R.id.videovw_expand);
        RelativeLayout layout_menubar = (RelativeLayout) findViewById(R.id.layout_menubar);
        layout_menubar.setVisibility(View.GONE);
        String path = getIntent().getExtras().getString("mediaUrl");//

        this.isAutoPlay = getIntent().getExtras().getInt("isAutoPlay", 0);//


        FCallingActivityID = getIntent().getExtras().getInt("callingActivityID", -1);

        if( GlobalVariablesHolder.firstTimeUseOfVideoCapture ){
            ((RelativeLayout) findViewById(R.id.tooltip_frame)).setVisibility(View.VISIBLE);
        }else{
            ((RelativeLayout) findViewById(R.id.tooltip_frame)).setVisibility(View.GONE);
        }

		/*
		if(FCallingActivityID == Constants.requestCode_Tutorial_Activity){
			((RelativeLayout) findViewById(R.id.tooltip_frame)).setVisibility(View.VISIBLE);
		}else{
			((RelativeLayout) findViewById(R.id.tooltip_frame)).setVisibility(View.GONE);
		}
		*/

        MediaController mc = new MediaController(this);
        videovw_expand.setMediaController(mc);
        videovw_expand.setVideoURI(Uri.parse(path));
        videovw_expand.requestFocus();
        videovw_expand.start();
        videovw_expand.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                //######################################################
                Intent returnIntent = new Intent();

                returnIntent.putExtra("isAutoPlay", isAutoPlay);
                setResult(RESULT_OK, returnIntent);
                //######################################################
                finish();
            }
        });

    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();

        GlobalVariablesHolder.firstTimeUseOfVideoCapture = false;
    }


	public void onDone(View v)
    {
		finish();
	}
}
