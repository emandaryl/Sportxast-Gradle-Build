package com.sportxast.SportXast.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.sportxast.SportXast.R;
import com.sportxast.SportXast.activities2_0.Highlight_Activity;
import com.sportxast.SportXast.commons.Constants;
import com.sportxast.SportXast.commons.TestConnection;

import java.io.File;
import java.util.Arrays;

public class FacebookFragment extends DialogFragment {

	private static final String TAG = "FacebookFragment";
	private UiLifecycleHelper uiHelper;
	private Button btnLoginFb;
	private TextView txtvwPermission;
	private ProgressBar progressChecking;
	private EditText edtStatus;
	private ImageView imgvwThumb;
	private LinearLayout linSectionStatus;
	private LinearLayout linFacebookLogin;
	private Button btnPost;
	private String message;
	
	private String[] shareInfos;
	/*
	 * For sharing on Facebook, EXPLODE important Strings.
	 * shareInfos array contains
	 * 
	 * index 0 = shareMsg = The caption/description for the video to be uploaded.
	 * index 1 = coverImageThumb = Image file for thumbnail.
	 * index 2 = videoLocalPath = Path to the video stored on the phone.
	 * index 3 = mp4url = Url of the video.
	 * index 4 = mediaId
	 * Pass it to setTag()
	 */
	
	public static FacebookFragment newInstance(String message) {
		FacebookFragment fragment = new FacebookFragment();
		
		Bundle bundle = new Bundle();
		bundle.putString(Constants.KEY_MESSAGE, message);
		
		fragment.setArguments(bundle);
		
		return fragment;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		message = getArguments().getString(Constants.KEY_MESSAGE);
		Log.i(TAG, "message from the heavens: " + message);

		shareInfos = message.split(Constants.SEPARATOR);
		
		uiHelper = new UiLifecycleHelper(getActivity(), sessionCallback);
		uiHelper.onCreate(savedInstanceState);

//		Log.i(TAG, "shareInfo size: " + shareInfos.length);
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		
		uiHelper.onSaveInstanceState(outState);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		uiHelper.onResume();
		
		Session session = Session.getActiveSession();
		if(session != null) {
//			Log.i(TAG, "onResume session active");
		 	onSessionStateChange(session, session.getState(), null);	
		}
	}
	
	@Override
	public void onPause() {
		super.onPause();
		
		uiHelper.onPause();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		
		uiHelper.onDestroy();
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		
		Log.i(TAG, "Activity ResultCode: " + resultCode + " RequestCode: " + requestCode);
		uiHelper.onActivityResult(requestCode, resultCode, data);
		Session.getActiveSession().onActivityResult(getActivity(), requestCode, resultCode, data);
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.facebook_layout, container, false);
		
		btnLoginFb = (Button) view.findViewById(R.id.btn_facebook_login);
		txtvwPermission = (TextView) view.findViewById(R.id.txtvw_facebook_permission);
		progressChecking = (ProgressBar) view.findViewById(R.id.progress_checking);
		
		edtStatus = (EditText) view.findViewById(R.id.edt_status);
		imgvwThumb = (ImageView) view.findViewById(R.id.imgvw_thumbnail);
		btnPost = (Button) view.findViewById(R.id.btn_post);
		linSectionStatus = (LinearLayout) view.findViewById(R.id.lin_section_status);
		linFacebookLogin = (LinearLayout) view.findViewById(R.id.lin_facebook_login);
		
		btnPost.setOnClickListener(onclickPostToFb);
		btnLoginFb.setOnClickListener(onclickLogin);
		
//		getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

		getDialog().setTitle("Facebook");
		return view;
	}
	
	private OnClickListener onclickLogin = new OnClickListener() {
		
		@Override
		public void onClick(View view) {
			/*
			 * Open session with email as the initial
			 * permission.
			 */
			Session.openActiveSession(getActivity(), true, Arrays.asList("email"), sessionCallback);
			
		}
	};
	
	private OnClickListener onclickPostToFb = new OnClickListener() {
		
		@Override
		public void onClick(View view) {
			File file = new File(shareInfos[2]);
//			Log.i(TAG, "Video file path: " + shareInfos[2]);
			
			Highlight_Activity highlight = (Highlight_Activity) getActivity();
			/*
			 * params 0 = Video file to be uploaded.
			 * params 1 = Url of the video.
			 * params 2 = Message/caption for the video.
			 * params 3 = mediaId
			 */
			if(TestConnection.checkInternetConnection(getActivity())) {
				highlight.uploadVideoToFb(file, shareInfos[3], shareInfos[0], shareInfos[4]);
				
				/*
				 * Suicide
				 */
				getActivity().getSupportFragmentManager().beginTransaction().remove(FacebookFragment.this).commit();
			} else {
				/*
				 * No Internet.
				 */
				Toast.makeText(getActivity(), getString(R.string._No_internet_connection), Toast.LENGTH_LONG).show();
			}
			
		}
	};

	private void onSessionStateChange(final Session session, SessionState sessionState, Exception exception) {
		progressChecking.setVisibility(View.GONE);
		if(sessionState.isOpened()) {
			Log.i(TAG, "facebook logged in.");
			showMeLogInWidgets();
			/*
			 * Get logged in user's info.
			 */     
			Request.newMeRequest(session, new Request.GraphUserCallback() {
				
				@Override
				public void onCompleted(GraphUser user, Response response) {
					if(user != null) {
						Log.i(TAG, "facebook user is " + user.getName());
	
						edtStatus.setText(shareInfos[0]);
						
						AQuery query = new AQuery(getActivity());
						query.id(imgvwThumb).image(shareInfos[1], false, true);
					}
				}
			}).executeAsync();
			
		} else {
			Log.i(TAG, "facebook logged out.");
			showMeLogOutWidgets();
		}
	}
	
	private void showMeLogInWidgets() {
		btnLoginFb.setVisibility(View.GONE);
		txtvwPermission.setVisibility(View.GONE);
		linFacebookLogin.setVisibility(View.GONE);
		linSectionStatus.setVisibility(View.VISIBLE);
		edtStatus.setVisibility(View.VISIBLE);
		imgvwThumb.setVisibility(View.VISIBLE);
		btnPost.setVisibility(View.VISIBLE);
	}
	
	private void showMeLogOutWidgets() {
		btnLoginFb.setVisibility(View.VISIBLE);
		txtvwPermission.setVisibility(View.GONE);
		linSectionStatus.setVisibility(View.GONE);
//		edtStatus.setVisibility(View.GONE);
//		imgvwThumb.setVisibility(View.GONE);
		btnPost.setVisibility(View.GONE);
	}
	
	private Session.StatusCallback sessionCallback = new Session.StatusCallback() {
		
		@Override
		public void call(Session session, SessionState sessionState, Exception exception) {	
			onSessionStateChange(session, sessionState, exception);
		}
	};
}
