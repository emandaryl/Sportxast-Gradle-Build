package com.sportxast.SportXast.activities2_0;

import android.app.Activity;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import com.sportxast.SportXast.R;

public class VideoPreviewActivity extends Activity {

	VideoView videoView;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.video_fullscreen);
		// TODO Auto-generated method stub

		videoView = (VideoView) findViewById(R.id.videovw_expand);
		RelativeLayout layout_menubar = (RelativeLayout) findViewById(R.id.layout_menubar);
		layout_menubar.setVisibility(View.GONE);
		String path = getIntent().getExtras().getString("mediaUrl");//
		
		MediaController mc = new MediaController(this);
        videoView.setMediaController(mc);
        videoView.setVideoURI(Uri.parse(path));
        videoView.requestFocus();
        videoView.start();
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                finish();
            }
        });
	
	}

	public void onDone(View v){
		finish();
	}
}
