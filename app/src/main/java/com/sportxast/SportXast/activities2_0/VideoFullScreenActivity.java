package com.sportxast.SportXast.activities2_0;

import android.app.Activity;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.MediaController;
import android.widget.VideoView;

import com.sportxast.SportXast.R;

public class VideoFullScreenActivity extends Activity {

	VideoView videoView;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.video_fullscreen);
		// TODO Auto-generated method stub

		videoView = (VideoView) findViewById(R.id.videovw_expand);
		String path = getIntent().getExtras().getString("mediaUrl");//
		
		//Uri uri = Uri.parse(path);
		videoView.setVideoPath(path);
		  
		final MediaController mc = new MediaController(this);
		mc.setAnchorView(videoView);
		mc.setMediaPlayer(videoView);
		videoView.setMediaController(mc);
		//videoView.setVideoURI(uri);
		videoView.start();
		videoView.requestFocus();
		videoView.setOnPreparedListener(new OnPreparedListener() {

			@Override
			public void onPrepared(MediaPlayer mp) {
				// TODO Auto-generated method stub
				
				mp.setOnBufferingUpdateListener(new OnBufferingUpdateListener() {

					@Override
					public void onBufferingUpdate(MediaPlayer mp,
							int percent) {
						// TODO Auto-generated method stub
						
					
						if(percent==100){
							findViewById(R.id.txtvw_menutitle).setVisibility(View.GONE);
							findViewById(R.id.progress_refresh).setVisibility(View.GONE);
							mc.show(0);
						}
						
					}
					
					
				});
				
			
				
			}
			
			
			
		});
		
	
	}

	
	public void onDone(View v){
		finish();
	}
}
