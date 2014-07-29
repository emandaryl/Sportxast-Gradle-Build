package com.sportxast.SportXast.tasks;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.io.InputStream;

public class StreamImageTask  extends AsyncTask<String, Void, Bitmap> { 
	
	ProgressBar progressLoader;
	ImageView bmImage;

	@Override
	protected void onPreExecute() {
		super.onPreExecute();   
		this.progressLoader.setVisibility(View.VISIBLE); 
		//( (ProgressBar) findViewById(R.id.pbLoading_avatar) ).setVisibility(View.VISIBLE); 
	}
	 
    public StreamImageTask(ImageView bmImage, ProgressBar progressLoader) {
        this.bmImage = bmImage;
        this.progressLoader = progressLoader;
    }
 
    protected Bitmap doInBackground(String... urls) {
        String urldisplay = urls[0];
        Bitmap mIcon11 = null;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            mIcon11 = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return mIcon11;
    }

    protected void onPostExecute(Bitmap result) {
    	//imgvw_avatar_profile.setImageBitmap(result);
    	bmImage.setImageBitmap(result);
    	this.progressLoader.setVisibility(View.GONE); 
    	//( (ProgressBar) findViewById(R.id.pbLoading_avatar) ).setVisibility(View.GONE);
    }
}
 