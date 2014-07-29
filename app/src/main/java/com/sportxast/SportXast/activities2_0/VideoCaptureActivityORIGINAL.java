package com.sportxast.SportXast.activities2_0;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.drawable.AnimationDrawable;
import android.hardware.Camera;
import android.media.AudioManager;
import android.media.CamcorderProfile;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.coremedia.iso.IsoFile;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sportxast.SportXast.Global_Data;
import com.sportxast.SportXast.R;
import com.sportxast.SportXast.Utils;
import com.sportxast.SportXast.commons.CommonFunctions_1;
import com.sportxast.SportXast.commons.Constants;
import com.sportxast.SportXast.commons.Dialog;
import com.sportxast.SportXast.commons.DialogSettings;
import com.sportxast.SportXast.commons.GlobalVariablesHolder;
import com.sportxast.SportXast.interfaces.EditTextImeBackListener;
import com.sportxast.SportXast.localdatabase.Keys;
import com.sportxast.SportXast.models.EditTextBackEvent;
import com.sportxast.SportXast.models.EventParcel;
import com.sportxast.SportXast.models._EventLists.EventLists;
import com.sportxast.SportXast.models._MediaStorage;
import com.sportxast.SportXast.tasks.ProcessingTask;
import com.sportxast.SportXast.thirdparty_class.Async_HttpClient;
import com.sportxast.SportXast.video.capture.CameraPreview;
import com.sportxast.SportXast.video.capture.CaptureListener;
import com.sportxast.SportXast.video.capture.CustomVideoView;
import com.sportxast.SportXast.video.capture.TrimVideoUtils;
import com.sportxast.SportXast.video.capture.UIFragment;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class VideoCaptureActivityORIGINAL extends Activity implements CaptureListener { 
	
	private static final String TAG = "VideoCaptureActivity";
	private CaptureListener vcListner; 
	UIFragment uiFragment = null;	
    private Camera devCamera;

    RelativeLayout header; 
    RelativeLayout uicontainer;
    TableLayout footer;
   
    private FrameLayout cameraPreviewFrame;
    private SurfaceView FCameraPreview; 
    private MediaRecorder FMediaRecorder; 
    
    File file;
    int mX;
    int mY;
     
    boolean mIsBound;
    boolean canClickCapture = true;

	private ImageView snapshot;
	
	private String FEventId = "";
	 
	private ImageView closeBtnCapture;
	private int defaultOrientation;
	protected String FMediaId; 
	protected String shareText;
	protected String shareUrl;
	private File renameTo;
	private boolean ui_is_showing = false;
	
	ImageButton btnAddFav;
	private ImageButton btnAddComment;
	ImageButton btnAddTag;
	ImageButton btnShare;
	private ImageButton btnDelete;
	
	//private DatabaseHelper DB;
	
	private int oldStreamVolume;
	private CustomVideoView mVideoView;
	 
	private boolean canPlayPreview = false;
	private String previewPath = "";
	private ImageView previewPlay;
	protected String imagePath;
	protected String videoPath;
	private String currentLocalFilename;
	private boolean recording = false;
	private TextView waitText; 
	
	/** Tooltip resources **/
	//private RelativeLayout tooltip_frame;
	private RelativeLayout tooltip_cont1; 
	private RelativeLayout portraitview_cover;
	  
	//private boolean FFromTutorial; 
	private int FCallingActivityID; 
	private ImageButton captureButton;
	 
	public void gotoProfile_Activity(){
		Intent intent = new Intent(VideoCaptureActivityORIGINAL.this,Profile_Activity.class);
		intent.putExtra("userId", 		 GlobalVariablesHolder.X_USER_ID);
		intent.putExtra("userDisplayname", GlobalVariablesHolder.X_USER_NAME);
		startActivity(intent);
		
		//runThreadUploader();
		//####################################################################
		releaseResources(); 
    	finish();
	}
	
	 
	public void gotoCreateEventPage(){ 
		Intent intent = new Intent(VideoCaptureActivityORIGINAL.this, Create_Activity.class); 
		//intent.putExtra("eventId",		eventId);
		//intent.putExtra("isOpen", 		eventIsOpen);  
		startActivity(intent);

		//runThreadUploader();
		//####################################################################
		releaseResources(); 
    	finish();
	}
	  
	public void gotoHighlight_Activity(){ 
		// TODO Auto-generated method stub 
		Intent intent = new Intent(VideoCaptureActivityORIGINAL.this, Highlight_Activity.class); 
		intent.putExtra("eventParcel", FChosenEvent);
		intent.putExtra("numberOfVideosRecorded", FNumberOfVideosRecorded);
		intent.putExtra("callingActivityID", Constants.requestCode_VideoCapture_Activity);   
		startActivity(intent);
		  
		//runThreadUploader();
		//####################################################################
		releaseResources(); 
    	finish();
		
	} 
	
	/** Coordinates **/
	private Double FCorLatitude;
	private Double FCorLongitude;   
	private String FEventLatitude;
	private String FEventLongitude;
	private String FEventFirstTeam;
	private String FEventSecondTeam;
	private String FEventName;
	 
	public void supplyChosenEvent( EventLists latestEvent ){ 
		this.FChosenEvent 		= CommonFunctions_1.parseToEventParcel(latestEvent);  
		this.FEventId 			= FChosenEvent.eventId; 
		this.FEventLatitude 	= FChosenEvent.eventLatitude; 
		this.FEventLongitude	= FChosenEvent.eventLongitude;
		this.FEventFirstTeam	= FChosenEvent.eventFirstTeam; 
    	this.FEventSecondTeam	= FChosenEvent.eventSecondTeam; 
    	this.FEventName			= FChosenEvent.eventName;  
    	
		supplyLabels();
	}
	
	private Global_Data FGlobal_Data; 

	@Override
	protected void onResume() {
		super.onResume();  
		//GlobalVariablesHolder.currentActivityContext = VideoCaptureActivity.this;
		
		 
	}
	
    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setMute();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        //global_Data = (Global_Data) getApplicationContext();
        super.setContentView(R.layout.layout_video_capture);
         
        FGlobal_Data = (Global_Data) getApplicationContext();
        
        this.FCorLatitude = GlobalVariablesHolder.user_Latitude;
		this.FCorLongitude= GlobalVariablesHolder.user_Longitude;  
        
        try {
        	FEventId = getIntent().getExtras().getString("eventId");
			} 
        catch (Exception e) {
			FEventId = "";
		} 
          
        FCallingActivityID 	= getIntent().getExtras().getInt("callingActivityID", -1); 
          
       // DB = new DatabaseHelper(this);
        this.cameraPreviewFrame = (FrameLayout) findViewById(R.id.camera_preview);
        this.uicontainer 	    = (RelativeLayout) findViewById(R.id.uicontainer);
        
        //################################################################# 
        this.FEventLatitude 	= "0";
    	this.FEventLongitude	= "0";
    	this.FEventFirstTeam	= "";
    	this.FEventSecondTeam	= "";
    	this.FEventName			= "";
    	
    	
    	if( GlobalVariablesHolder.FLatestEvent != null){
        	
        	if(GlobalVariablesHolder.alreadyCheckedIntoAnEvent){ 
				supplyChosenEvent( GlobalVariablesHolder.FLatestEvent );
				}
        	else
				{ 
				//showCheckIntoDialog(1, GlobalVariablesHolder.FLatestEvent.eventName);
				}   
    	}
    	
    	
    	if( FCallingActivityID == Constants.requestCode_Highlight_Activity ){ 
    		FChosenEvent = getIntent().getExtras().getParcelable("eventParcel"); 
    		FEventId = FChosenEvent.eventId;  
    		this.FEventLatitude 	= FChosenEvent.eventLatitude; 
    		this.FEventLongitude	= FChosenEvent.eventLongitude;
    		this.FEventFirstTeam	= FChosenEvent.eventFirstTeam; 
        	this.FEventSecondTeam	= FChosenEvent.eventSecondTeam; 
        	this.FEventName			= FChosenEvent.eventName;   
        	
    	}else if(FCallingActivityID == Constants.requestCode_Tutorial_Activity){ 
    		//beginVideoCaptureMode(); 
    	}else if(FCallingActivityID == Constants.requestCode_SportX2_Main){ 
    		//beginVideoCaptureMode(); 
    	}else if(FCallingActivityID == Constants.requestCode_Create_Activity){ 
    		FChosenEvent = getIntent().getExtras().getParcelable("eventParcel"); 
    		FEventId = FChosenEvent.eventId;  
    		this.FEventLatitude 	= FChosenEvent.eventLatitude; 
    		this.FEventLongitude	= FChosenEvent.eventLongitude;
    		this.FEventFirstTeam	= FChosenEvent.eventFirstTeam; 
        	this.FEventSecondTeam	= FChosenEvent.eventSecondTeam; 
        	this.FEventName			= FChosenEvent.eventName;  
    		//beginVideoCaptureMode();  
    	}else if(FCallingActivityID == Constants.requestCode_Menu_Activity){
    		 
    	}
        
        initializeResources();   
        
        videoCaptureModeIsOn = false;
        beginVideoCaptureMode(); 
        FNumberOfVideosRecorded = 0;
        //########################################################  
        hideKeyboard();  
        //########################################################### 
    }
     
	public void showCheckIntoDialog( final int promptMessageType, String eventName ){ 
		
		DialogSettings dialog = new DialogSettings( VideoCaptureActivityORIGINAL.this, promptMessageType, eventName); 
		Dialog.showSettingsDialog( dialog, VideoCaptureActivityORIGINAL.this ); 
		/*
		dialog.setOnDismissListener(new DialogInterface.OnDismissListener() { 
			@Override
			public void onDismiss(DialogInterface dialog) {
				if (Dialog.getDialogSettingsResult() == 0) { // NO CHANGES,  JUST DISMISS 
				} else if (Dialog.getDialogSettingsResult() == 1) {  
				} else if (Dialog.getDialogSettingsResult() == 11) {
				} else if (Dialog.getDialogSettingsResult() == 2) {
				}
				// TODO Auto-generated method stub
			}
		});  
		*/
		
	}
	 
	private EventParcel FChosenEvent;
    private TextView  event_title;
	
    private TextView actionbar_menutitle;
    
    private void supplyLabels(){
    	if(event_title == null)
    	 event_title = (TextView) findViewById(R.id.event_title); 
    	 
    	String eventTitle = ""; 
    	if(FEventFirstTeam.trim().length() > 0){
    		eventTitle = FEventFirstTeam +"\n"+ FEventName;  
    	}else{
    		eventTitle = FEventName; 
    	}
    	
    	event_title.setText( eventTitle );   
    	 
    	if(actionbar_menutitle == null)
    		actionbar_menutitle = (TextView) findViewById(R.id.actionbar_menutitle); 
       	 
    	if(eventTitle.trim().length() <= 0)
    		eventTitle = "Capture";
    	
    	actionbar_menutitle.setText(eventTitle);
    	 /*
         System.out.println("--------------------global_Data.getNewEvent------------------------------");
         System.out.println("EventId: " + FEventId);
         System.out.println(global_Data.getNewEvent().eventId);
         System.out.println(global_Data.getNewEvent().eventSportId);
         System.out.println(global_Data.getNewEvent().eventName);       
         System.out.println("-----------------END global_Data.getNewEvent ---------------------------------");
         */  
    }
    private LinearLayout captureButton_cont; 
    private AnimationDrawable rocketAnimation;
    
    private ImageView menuBtn;
    
    private RelativeLayout layout_commentDelete_field_cont; 
    
    private Button imgbtn_back;
    
    private void initializeResources(){
 
    	this.imgbtn_back = (Button) findViewById(R.id.imgbtn_back);
    	this.imgbtn_back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//runThreadUploader();
				//####################################################################
				releaseResources(); 
		    	finish();
			}
		});
    	
    	this.portraitview_cover = (RelativeLayout) findViewById(R.id.portraitview_cover); 
        this.header 		= (RelativeLayout) findViewById(R.id.header);
        this.footer 		= (TableLayout) findViewById(R.id.footer);
        this.snapshot		= (ImageView) findViewById(R.id.snapshot); 
//########################################################################## 
       // this.tooltip_frame 	= (RelativeLayout) findViewById(R.id.tooltip_frame);
        this.tooltip_cont1 	= (RelativeLayout) findViewById(R.id.tooltip_cont1); 
//########################################################################## 
        
        this.captureButton_cont = (LinearLayout) findViewById(R.id.captureButton_cont); 
        this.captureButton 	= (ImageButton) captureButton_cont.findViewById(R.id.captureButton); 
        captureButton.setBackgroundResource(R.drawable.logo_anim);
        rocketAnimation 	= (AnimationDrawable) captureButton.getBackground();
        rocketAnimation.start();
         
    	final View activityRootView =  ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0);
		activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
		@Override
		public void onGlobalLayout() {
			   
		    Rect r = new Rect();
		    //r will be populated with the coordinates of your view that area still visible.
		    activityRootView.getWindowVisibleDisplayFrame(r);

		    int heightDiff = activityRootView.getRootView().getHeight() - (r.bottom - r.top);
		    if (heightDiff > 100) { // if more than 100 pixels, its probably a keyboard...
		    	//Toast.makeText(getApplicationContext(), "keybARDS height: "+heightDiff, Toast.LENGTH_LONG).show();
		    	/*
		    	if( edittext_comment != null ){
		    		edittext_comment.setFocusableInTouchMode(true);
		    		edittext_comment.setCursorVisible(true); 
		    		edittext_comment.requestFocus();
		    	}    */
		    }else{
		    	//hidden = true;  
		    	//showCommentSectionPanel(false);
		    	//Toast.makeText(getApplicationContext(), "wagtang keybARDS height: "+heightDiff, Toast.LENGTH_LONG).show();
		    }
		 
		 }
		}); 
        
    	layout_commentDelete_field_cont = (RelativeLayout) findViewById(R.id.layout_commentDelete_field_cont);	
    	layout_commentDelete_field_cont.setVisibility(View.GONE);
    	  
        this.btnAddFav  	= (ImageButton) findViewById(R.id.btn_add_fav);
        this.btnAddComment	= (ImageButton) findViewById(R.id.btn_add_comment);
        
        //final RelativeLayout commentView = (RelativeLayout) findViewById(R.id.comment_field_layout); 
        btnAddComment.setOnClickListener(new OnClickListener() {
        	@Override
        	public void onClick(View v) {
        		
        		if(FLatestHighlightMediaId.length() <= 0)
        			return;
        		
        		showCommentSectionPanel( true, FLatestHighlightMediaId );   
        		/*
        		Log.e("commentView", commentView.toString());
        		uicontainer.setVisibility(View.GONE);
        		updateCurrentCapture();
        		Utils.addComment(VideoCaptureActivity.this, FCurrentCapture, commentView, vcListner);
        		*/ 
        	}
        });
        
        this.btnAddTag 		= (ImageButton) findViewById(R.id.btn_add_tag);
        this.btnShare 		= (ImageButton) findViewById(R.id.btn_share);
        
        this.btnDelete 		= (ImageButton) findViewById(R.id.btn_delete); 
        btnDelete.setOnClickListener(new OnClickListener() {
        	@Override
        	public void onClick(View v) { 
        		if(FLatestHighlightMediaId.length() <= 0)
        			return;
        		
        		showDeleteSectionPanel( true, FLatestHighlightMediaId ); 
        		/*
        		updateCurrentCapture();
        		Utils.deleteCapture(VideoCaptureActivity.this, FCurrentCapture, vcListner); 
        		*/ 
        	}
        });
         
        this.waitText 		= (TextView) findViewById(R.id.waitText);
        
        this.menuBtn		= (ImageView) findViewById(R.id.menuBtn); 
        menuBtn.setOnClickListener(new OnClickListener() {
        	@SuppressLint("ResourceAsColor")
			@Override
        	public void onClick(View v) {
        		// TODO Auto-generated method stub 
        		//portraitview_cover.setAlpha(100);
        		portraitview_cover.setBackgroundColor(R.color.opaque_back);
        		portraitview_cover.setVisibility(View.VISIBLE); 
        		((RelativeLayout) findViewById(R.id.tooltip_cont12)).setBackgroundResource(R.drawable.sx_rotate_right); 
        		//DialogOrientation.show();
        	}
        });
        
        this.previewPlay 	= (ImageView)findViewById(R.id.previewPlay);
        mVideoView  		= (CustomVideoView)findViewById(R.id.smallPreview);
//        mVideoView.setMediaController(new MediaController(this));  
        mVideoView.setOnCompletionListener(new OnCompletionListener() {
			@Override
			public void onCompletion(MediaPlayer mp) {
				// TODO Auto-generated method stub
				mVideoView.setVisibility(View.GONE); 
			}
		});
          
        supplyLabels();
        
        // we'll enable this button once the camera is ready
        vcListner = (CaptureListener) VideoCaptureActivityORIGINAL.this; 
        captureButton_cont.setVisibility(View.GONE);
      //captureButton.setVisibility(View.GONE);
        captureButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				// TODO Auto-generated method stub
				if (canClickCapture) { 
					showTooltip( false ); 
					FCurrentCapture = new _MediaStorage();
					FCurrentCapture.mediaEventID = FEventId;
					FCurrentCapture.mediaDateShortFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
					//FCurrentCapture.userLatitude = global_Data.getCoordinate().latitude + "";
					//FCurrentCapture.userLongitude= global_Data.getCoordinate().longitude + "";
					FCurrentCapture.userLatitude = "" + FCorLatitude;
					FCurrentCapture.userLongitude= "" + FCorLongitude;
					capture();
				}
				
				canClickCapture = false;
			}
		});
        
        
        btnAddFav.setOnClickListener(new OnClickListener() {
        	@Override
        	public void onClick(View v) {
        		btnAddFav.setImageResource(R.drawable.bbtn_favorite_on);
        		btnAddFav.setEnabled(false);
        		updateCurrentCapture();
        		Utils.addToFavorites(VideoCaptureActivityORIGINAL.this, FCurrentCapture, vcListner);
        	}
        });
        
        btnShare.setOnClickListener(new OnClickListener() {
        	@Override
        	public void onClick(View v) {
        		
        		/*
        		btnShare.setImageResource(R.drawable.bbtn_share_on);
        		btnShare.setEnabled(false);
        		updateCurrentCapture();
        		Utils.shareMedia(VideoCaptureActivity.this, FCurrentCapture, vcListner);
        		*/ 
        	}
        });
         
        
        btnAddTag.setOnClickListener(new OnClickListener() {
        	@Override
        	public void onClick(View v) { 
        		/*
        		updateCurrentCapture();
        		Utils.addTag(VideoCaptureActivity.this, FCurrentCapture, vcListner);
        		*/
        	}
        });        
        
        this.snapshot.setOnClickListener(new OnClickListener() {
        	@Override
        	public void onClick(View v) {
        		startPreview(true);
        	}
        });        
//        closeBtnCapture.setOnClickListener(new OnClickListener() {
//        	@Override
//        	public void onClick(View v) {
//        		// TODO Auto-generated method stub
//        		dialog.dismiss();
//        	}
//        });
    } 
    
    private EditTextBackEvent edittext_comment; 
	public void showCommentSectionPanel(boolean showPanel, final String parentId ){ 
		if(showPanel == false){ 
			if( layout_commentDelete_field_cont != null ){
				layout_commentDelete_field_cont.removeAllViews();
				layout_commentDelete_field_cont.setVisibility(View.GONE);
			}
			edittext_comment = null; 
			return;
		} 
		   
		LayoutInflater infalInflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);  
		RelativeLayout lyt1 = (RelativeLayout) infalInflater.inflate( R.layout.layout_comment_field, null);
		 
		edittext_comment = (EditTextBackEvent ) lyt1.findViewById(R.id.edittext_comment); 
		edittext_comment.setOnEditTextImeBackListener(new EditTextImeBackListener() { 
			@Override
			public void onImeBack(EditTextBackEvent ctrl, String text,
					int keyCode, KeyEvent event) {
				/*keyCode = 4, KeyEvent { action=ACTION_UP, keyCode=KEYCODE_BACK, scanCode=158, metaState=0, flags=0x8, repeatCount=0, eventTime=151940006, downTime=151939934, deviceId=1, displayId=0, source=0x101 }*/
				//TODO Auto-generated method stub
				
				if( ( keyCode == 4 ) && (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) ){
					showCommentSectionPanel(false, parentId); 
					} 
				
				String hey = text;
				String ho = hey; 
			}
		});
		
		View view_outside = ( View ) lyt1.findViewById(R.id.view_outside); 
		view_outside.setOnClickListener(new View.OnClickListener() { 
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub 
				clearScreenFromCommentPanel();
				//hideSoftKeyboard( edittext_comment );
				//showCommentSectionPanel(false, parentId, itemPosition); 
			}
		});
		 
		Button btn_send = ( Button ) lyt1.findViewById(R.id.btn_send); 
		btn_send.setOnClickListener(new View.OnClickListener() { 
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				sendHighlightComment( parentId, edittext_comment.getText().toString() );
			}
		});
		
		layout_commentDelete_field_cont.addView(lyt1, new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)); 
		layout_commentDelete_field_cont.setVisibility(View.VISIBLE);  
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput( edittext_comment, InputMethodManager.SHOW_IMPLICIT );
		 
		edittext_comment.setFocusableInTouchMode(true);
		edittext_comment.setCursorVisible(true); 
		edittext_comment.requestFocus(); 
		
		//Toast.makeText(getApplicationContext(), "shown", Toast.LENGTH_LONG).show();
	}

	private void sendHighlightComment( String parentId, final String strComment ){ 
		// TODO Auto-generated method stub
		 
		 RequestParams requestParams = new RequestParams(); 
		 requestParams.put("parentId",	parentId);
		 requestParams.put("type",		"media");
		 requestParams.put("comment", 	strComment);
		 Async_HttpClient async_HttpClient = new Async_HttpClient(VideoCaptureActivityORIGINAL.this);
		 async_HttpClient.POST("AddComment", requestParams, new JsonHttpResponseHandler(){
	 		@Override
	 		public void onSuccess(JSONObject response) {
	 			// TODO Auto-generated method stub
	 			super.onSuccess(response);
	 			
	 			//Toast.makeText(getApplicationContext(), "COMMENT ADDED MAH NIGGA.", Toast.LENGTH_SHORT).show();
	 			Toast.makeText(getApplicationContext(), "Comment successfully added.", Toast.LENGTH_SHORT).show();
	 			Log.e("comment", "Success comment"+response.toString());
	 			 
	 			clearScreenFromCommentPanel();
	 			/* 
	 			Comments comments 		= new Comments();
	 			comments.commentBody 	= strComment;
	 			comments.commentUserId 	= GlobalVariablesHolder.X_USER_ID;
	 			comments.commentUserName= GlobalVariablesHolder.X_USER_NAME;
	 			
	 			FArrMediaList.get( itemPosition ).comments.add(0, comments);
	 			clearScreenFromCommentPanel();
	 			reloadListView(FArrMediaList); 
	 			*/
	 		}
	 		
	 		@Override
	 		public void onFailure(int arg0, Header[] arg1, byte[] arg2,
	 				Throwable arg3) {
	 			// TODO Auto-generated method stub
	 			super.onFailure(arg0, arg1, arg2, arg3); 
	 			/*
	 			 btn_send.setEnabled(true);
	 			 Toast.makeText(context, "fail to add comment. try again.", Toast.LENGTH_SHORT).show();
	 			 commentView.setVisibility(View.GONE); 
	 			 */
	 		}
	 	});
	}
	private void clearScreenFromCommentPanel(){
		hideSoftKeyboard( edittext_comment );
		showCommentSectionPanel(false, ""); 
	}
	 
	private void hideSoftKeyboard( EditText editTextWithKeyboard ){
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(editTextWithKeyboard.getWindowToken(), 0);
	}
	
	
    public void showDeleteSectionPanel( boolean showPanel, final String mediaId  ){ 
		if(showPanel == false){ 
			if( layout_commentDelete_field_cont != null ){
				layout_commentDelete_field_cont.removeAllViews();
				layout_commentDelete_field_cont.setVisibility(View.GONE);
			}
			//edittext_comment = null; 
			return;
		} 
		   
		LayoutInflater infalInflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);  
		RelativeLayout lyt1 = (RelativeLayout) infalInflater.inflate( R.layout.layout_delete_field, null);
		    
		Button btn_delete = ( Button ) lyt1.findViewById(R.id.btn_delete); 
		btn_delete.setOnClickListener(new View.OnClickListener() { 
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub 
				deleteHighlight(mediaId);
			}
		});
		
		Button btn_cancel = ( Button ) lyt1.findViewById(R.id.btn_cancel); 
		btn_cancel.setOnClickListener(new View.OnClickListener() { 
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				//sendHighlightComment( itemPosition, parentId, edittext_comment.getText().toString() );
				showDeleteSectionPanel(false, ""); 
			}
		});
		
		View view_outside = ( View ) lyt1.findViewById(R.id.view_outside); 
		view_outside.setOnClickListener(new View.OnClickListener() { 
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub 
				showDeleteSectionPanel(false, ""); 
			}
		});
		  
		layout_commentDelete_field_cont.addView(lyt1, new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)); 
		layout_commentDelete_field_cont.setVisibility(View.VISIBLE);    
	}
     
   	private void deleteHighlight( String mediaId ){  
   		//  TODO Auto-generated method stub 
   			RequestParams params = new RequestParams();
   			params.put("mediaId", mediaId);
   			
   			Async_HttpClient async_HttpClient =  new Async_HttpClient(VideoCaptureActivityORIGINAL.this);
   			async_HttpClient.GET("DeleteMedia", params, new JsonHttpResponseHandler(){
   				
   				@Override
   				public void onSuccess(int statusCode,
   						JSONObject response) {
   					//TODO Auto-generated method stub
   					super.onSuccess(statusCode, response);
   				  
   					Toast.makeText(getApplicationContext(), "Deleted successfully.", Toast.LENGTH_SHORT).show(); 
   					removeHighlightDataFromSharedPref();
   					//FArrMediaList.remove(itemPosition); 
   					showDeleteSectionPanel(false, ""); 
   					//reloadListView(FArrMediaList);
   				}
   				@Override
   				public void onFailure(int statusCode,
   						Throwable e, JSONObject errorResponse) {
   					// TODO Auto-generated method stub
   					super.onFailure(statusCode, e, errorResponse); 
   					Toast.makeText(getApplicationContext(), "Failed to delete media.", Toast.LENGTH_SHORT).show();
   				}
   				 
   			}); 
   			//FArrMediaLists.remove(section);
   		//	 notifyDataSetChanged(); 
   	}
   	 
   	private void removeHighlightDataFromSharedPref(){ 
   	   SharedPreferences sharedpreferences = getApplicationContext().getSharedPreferences("com.sportxast.SportXast.highlights", getApplicationContext().MODE_PRIVATE); 
	   if (sharedpreferences.contains( Constants.sharePrefKey_uploadedHighlights ) )
			{   
		   //#####################################################################################################################
		   //#####################################################################################################################
		   	String highlightDataInString = sharedpreferences.getString( Constants.sharePrefKey_uploadedHighlights , "");   
		   	
		   	if( highlightDataInString.length() >= 2 ){
			   	if(highlightDataInString.substring(0, 2).equals("||")){
			   		highlightDataInString = highlightDataInString.substring(2).trim();
			   	}
		   	}
		   	  
		    if( highlightDataInString.length() > 0 ){
		    	String[] arrHighlightsData = highlightDataInString.split("\\|\\|");
		    	 
		    	String newHighlightDataInString = ""; 
		    	for (int i = 0; i < arrHighlightsData.length; i++) {
		    		
		    		if(  i == (arrHighlightsData.length - 1) ){//if last entry, then ignore
		    			break;
		    		}
		    		 
		    		newHighlightDataInString = newHighlightDataInString +"||"+ arrHighlightsData[i].toString(); 
				}
		    	 
		    	//################################################################################## 
		    	if( newHighlightDataInString.length() >= 2 ){
			    	if( newHighlightDataInString.substring(0, 2).equals("||") ){
			    		newHighlightDataInString = newHighlightDataInString.substring(2);
			    	}
		    	}
		    	  
		    	if(newHighlightDataInString.length() > 0){
		    	
			    	Editor editor = sharedpreferences.edit();
			    	editor.putString(Constants.sharePrefKey_uploadedHighlights, newHighlightDataInString); 
			    	editor.commit();  
		    	}else{
		    		sharedpreferences.edit().remove( Constants.sharePrefKey_uploadedHighlights ).commit();  
		    	}
		    	  
		    	showUi(false);
		    	 
		    	}
		      
		    	 
			}
	//#########################################################################	   
	
   	}  
     
    private void showTooltip( boolean showTooltip ){  
    	//Toast.makeText(getApplicationContext(), "wala na", Toast.LENGTH_LONG).show();
    	if( GlobalVariablesHolder.firstTimeUseOfVideoCapture ){
    		 
    		if(tooltip_cont1 == null)
    			return;
    		
    		if(showTooltip){
    			tooltip_cont1.setVisibility(View.VISIBLE);
    		} else 	{
    			tooltip_cont1.setVisibility(View.GONE); 	 
    		}
    		  
    	}else{
    		tooltip_cont1.setVisibility(View.GONE); 
    	}
    	 
    }
       

    private void releaseResources() {
        this.releaseMediaRecorder();
        this.releaseCamera();
    }
      
    /* 
    private FrameLayout cameraPreviewFrame;
    private SurfaceView FCameraPreview; 
    private MediaRecorder FMediaRecorder;  
    */
   
    private void releaseMediaRecorder() {
        if (this.FMediaRecorder != null) {
            this.FMediaRecorder.reset(); // clear configuration (optional here)
            this.FMediaRecorder.release();
            this.FMediaRecorder = null;
            recording = false;
        }
    }

    private void releaseCamera() {
        if (this.devCamera != null) {
           // this.camera.lock(); // unnecessary in API >= 14
            this.devCamera.stopPreview();
            this.devCamera.release();
            this.devCamera = null;
            this.cameraPreviewFrame.removeView(this.FCameraPreview);
        }
        
        //atan
        this.cameraPreviewFrame.setVisibility(View.GONE);
    }
    
    @Override
    public void onPause() {
    	portraitview_cover.setVisibility(View.GONE);
    	/*
    	if (DialogOrientation.isShowing()) { 
    		//DialogOrientation.dismiss();
    	}
    	*/
        this.releaseResources();
        super.onPause();
    }
    
    @Override
    protected void onDestroy() {
    	this.releaseResources();
    	restoreSounds();
    	super.onDestroy();
    	 
    	if(devCamera!=null){
    		devCamera.stopPreview();
    		devCamera.setPreviewCallback(null);

    		devCamera.release();
    		devCamera = null;
        }   
    }
    
    @Override
    protected void onStart() {
    	// TODO Auto-generated method stub
    	super.onStart();  
    }
 
    
    public void startRecording() {
    	int orientation = getDeviceDefaultOrientation();
    	
    	if (this.recording) {
    		return;
    	}
    	
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
	    	Log.d(TAG, "startRecording()");
	        // we need to unlock the camera so that mediaRecorder can use it
	        try {
	        this.devCamera.unlock(); // unnecessary in API >= 14
	        } catch (Exception e) {
	        }
	        
	        // now we can initialize the media recorder and set it up with our
	        // camera
	        this.FMediaRecorder = new MediaRecorder();
	        this.FMediaRecorder.setCamera(this.devCamera);
	        this.FMediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
	        this.FMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
	        
	        /*
	        if(CamcorderProfile.hasProfile(CamcorderProfile.QUALITY_480P)){
	        	 this.FMediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_480P));
	        	// Log.e("BBBBBBBQUALITY_480P", "QUALITY_480P");
	        	// Toast.makeText(	getApplicationContext(), "QUALITY_480P", Toast.LENGTH_LONG).show();
		            
	        }else if(CamcorderProfile.hasProfile(CamcorderProfile.QUALITY_720P)){
	        	 this.FMediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_720P));
	        	// Log.e("BBBBBBBQUALITY_720P", "QUALITY_720P");
	        	// Toast.makeText(	getApplicationContext(), "QUALITY_720P", Toast.LENGTH_LONG).show(); 
	        }else{
	        	this.FMediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_LOW));
	        	
	        	//this.FMediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH));
	        	// Log.e("BBBBBBBQUALITY_HIGH", "QUALITY_HIGH");
	        	// Toast.makeText(	getApplicationContext(), "QUALITY_HIGH", Toast.LENGTH_LONG).show(); 
	        }
	        */
	        
	        try {
	        	this.FMediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_480P));
	        	}
	        	catch (Exception e) {
	        	e.printStackTrace();
	        	this.FMediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_LOW));
	     	}
	        
	        this.FMediaRecorder.setOutputFile(this.initFile().getAbsolutePath());
	        this.FMediaRecorder.setPreviewDisplay( this.FCameraPreview.getHolder().getSurface() );
	          
	        try {
	            this.FMediaRecorder.prepare();
	            // start the actual recording
	            // throws IllegalStateException if not prepared
	            this.FMediaRecorder.start();
	            recording  = true;
	            // enable the stop button by indicating that we are recording
	            
	        	} catch (Exception e) {
	        	e.printStackTrace();
	            recording = false;
	            captureButton_cont.setVisibility(View.GONE);
	            //captureButton.setVisibility(View.GONE);
	            this.releaseMediaRecorder();
	            
	        	}
        }
    }
    
    // gets called by the button press
    public void stopRecording() {
    	
    	Log.d(TAG, "stopRecording()");
        assert this.FMediaRecorder != null;
        try {
            this.FMediaRecorder.stop();
            // we are no longer recording
            if (uiIsShowing()) {
            	showUi(false);
            	//hideUi();
            }
        }catch (RuntimeException e){
            // the recording did not succeed
            Log.w(TAG, "Failed to record", e);
            if (this.file != null && this.file.exists() && this.file.delete()) {
                Log.d(TAG, "Deleted " + this.file.getAbsolutePath());
            }
            return;
        } finally {
            this.releaseMediaRecorder();
        	}
        
        recording = false;
        if (this.file == null || !this.file.exists()) {
            Log.w(TAG, "File does not exist after stop: " + this.file.getAbsolutePath());
        }
        
    }
    /*
    private void hideUi() {
		uicontainer.setVisibility(View.GONE);
	    ui_is_showing = false;
	}
*/ 
    private void showUi(boolean setVisible) { 
		if(setVisible == true){ 
			uicontainer.setVisibility(View.VISIBLE);
		    ui_is_showing = true;
		    btnAddFav.setImageResource(R.drawable.btn_fav_2);
		    btnAddFav.setEnabled(true);
		    btnAddComment.setImageResource(R.drawable.btn_comment_2);
		    btnAddComment.setEnabled(true);
		    btnAddTag.setImageResource(R.drawable.btn_tag_2);
		    btnAddTag.setEnabled(true);
		    btnShare.setImageResource(R.drawable.btn_share_2);
		    btnShare.setEnabled(true);
		    btnDelete.setImageResource(R.drawable.btn_delete_2);
		    btnDelete.setEnabled(true);
		}
		else{ 
			uicontainer.setVisibility(View.GONE);
		    ui_is_showing = false;
		}
    	
	}
    
	private boolean uiIsShowing() {
		return this.ui_is_showing;
	}
	/*
	public void captureORIGINAL() {
        Log.d(TAG, "Capture");
        assert this.FMediaRecorder != null;
        try {
            startSaveService();
        } catch (Exception e) {
            // the recording did not succeed
            Log.w(TAG, "Failed to capture", e);
            if (uiIsShowing()) {
            	showUi(false);
            	//hideUi();
            } 
            this.releaseMediaRecorder();
        } 
    }
	*/
	public void capture() {
        Log.d(TAG, "Capture");
        assert this.FMediaRecorder != null;
        try {
            //startSaveService();
        	new startSaveServiceTask("").execute();  
        } catch (Exception e) {
            // the recording did not succeed
            Log.w(TAG, "Failed to capture", e);
            if (uiIsShowing()) {
            	showUi(false);
            	//hideUi();
            } 
            this.releaseMediaRecorder();
        } 
    }
	 
	private class startSaveServiceTask extends AsyncTask<String, Integer, Bitmap> { 
		private String localVideoFilePath;
		  
		@Override
		protected void onPreExecute() {
			super.onPreExecute();  
			stopRecording();
			captureButton_cont.setVisibility(View.GONE);
			//captureButton.setVisibility(View.GONE);
			Log.e("========= STOP REC", file.getAbsolutePath());
	        renameTo        = new File(TrimVideoUtils.getLocalTmpPath( getApplicationContext() ), "tmp_rec_tmp.mp4");
	        file.renameTo(renameTo);
	        startRecording();
	        Log.e("========= Renamed", renameTo.getAbsolutePath()); 
	        captureButton_cont.setVisibility(View.GONE); 
		}
		 
		private startSaveServiceTask(String localVideoFilePath ) { 
			this.localVideoFilePath = localVideoFilePath; 
	    }
		 
		@Override
		protected Bitmap doInBackground(String... params) { 
			//do your work
			  
	    	Bitmap snapshot = null;
	    	
	    	if (! renameTo.exists()) {
	    		return null;
	    	}
	    	
	    	double sampleLength = 8;
	    	
	    	try {
	    	    //sampleLength = Double.parseDouble(global_Data.getAppSetting().settings._MOVIE_DURATION);
	    		sampleLength = Double.parseDouble( Constants._MOVIE_DURATION );
	    	} catch (Exception e) {
	    		
	    	}
	    	
	    	try { 
	    		
	    		/*
			    @SuppressWarnings("resource")
				IsoFile isoFile = new IsoFile(renameTo.getAbsolutePath());
					
		        double length = (double)
		                isoFile.getMovieBox().getMovieHeaderBox().getDuration() /
		                isoFile.getMovieBox().getMovieHeaderBox().getTimescale();
		        */
		        
		        
		        double length = 3.500;
		        
			    /*      
		        isoFile.close();
				double startTime = length - sampleLength ;
				if (startTime < 0) startTime = 0;
	//			snapshot = TrimVideoUtils.getVideoFrame(renameTo.getAbsolutePath(),(long) startTime);
				snapshot = TrimVideoUtils.getVideoFrame(renameTo.getAbsolutePath(),(long) length);
				
				*/
		        
		        //ANOTHER WAY
		        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
				  
		        mediaMetadataRetriever.setDataSource(renameTo.getAbsolutePath());
		        //Bitmap bmFrame = mediaMetadataRetriever.getFrameAtTime(5000000); //unit in microsecond
		        snapshot = mediaMetadataRetriever.getFrameAtTime( (long) length );
			  
				/*  
		        Drawable myDrawable = getResources().getDrawable(R.drawable.ic_twitter_logo);
		        snapshot   = ((BitmapDrawable) myDrawable).getBitmap();
				*/   
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    	
			return snapshot; 
		} 
		
		@Override
		protected void onPostExecute(final Bitmap bitmapSnapshot) {
			super.onPostExecute(bitmapSnapshot);    
			 
			Log.e("SNAPSHOT", "SET");
	    	
	    	if (bitmapSnapshot == null) {
	    		showUi(false);
	    		//hideUi();
	    		return;
	    	}
	    	/*
	    	runOnUiThread(new Runnable() {
				@Override
				public void run() {
					
	            	showUi(true);
			    	snapshot.setImageBitmap( bitmapSnapshot ); 
			    	   
				}
			});
	    	   
	    	*/
	    	showUi(true);
	    	snapshot.setImageBitmap( bitmapSnapshot ); 
	    	 
	    	saveSnapshot( bitmapSnapshot );  
		} 
	}
	
    private void startSaveServiceORIGINAL() { 
	try { 
		stopRecording();
		captureButton_cont.setVisibility(View.GONE);
		//captureButton.setVisibility(View.GONE);
		Log.e("========= STOP REC", this.file.getAbsolutePath());
        renameTo        = new File(TrimVideoUtils.getLocalTmpPath(this), "tmp_rec_tmp.mp4");
        this.file.renameTo(renameTo);
        startRecording();
        Log.e("========= Renamed", renameTo.getAbsolutePath());
        
        new AsyncTask<Void, Void, Bitmap>() {
            @Override
            protected Bitmap doInBackground(Void... params) {
            	
            	try {
            		runOnUiThread(new Runnable() {
					
						@Override
						public void run() {
							// TODO Auto-generated method stub
							captureButton_cont.setVisibility(View.GONE);
	                		//captureButton.setVisibility(View.GONE);
							}
						
						});
                		} 
            		catch (Exception e) {
            			//exception error message
            		}
            	
            	return getVideoFrame();
            }

            @Override
            protected void onPostExecute(Bitmap obj) {
            	setSnapshot(obj);
            	 
            	/*
            	if( GlobalVariablesHolder.firstTimeUseOfVideoCapture ){
            		//startTimerOnVideoProcessingComplete();
            		 
            		startPreview(true); 
            	} 
            	*/
            	 
            }
            
        }.execute();
        
        } catch (Exception e) { 
            // TODO: handle exception 
        	canClickCapture = true;
        	showUi(false);
        	//hideUi();
            e.printStackTrace(); 
        } 
    }
  
    //Global_Data global_Data;
	private Bitmap getVideoFrame() { 
    	Bitmap snapshot = null; 
    	if (! renameTo.exists()) {
    		return null;
    	}
    	
    	double sampleLength = 8;
    	
    	try {
    	    //sampleLength = Double.parseDouble(global_Data.getAppSetting().settings._MOVIE_DURATION);
    		sampleLength = Double.parseDouble( Constants._MOVIE_DURATION );
    	} catch (Exception e) {
    		
    	}
    	
    	try {
	    IsoFile isoFile;
			isoFile = new IsoFile(renameTo.getAbsolutePath());
			
        double length = (double)
                isoFile.getMovieBox().getMovieHeaderBox().getDuration() /
                isoFile.getMovieBox().getMovieHeaderBox().getTimescale();
        isoFile.close();
		double startTime = length - sampleLength ;
		if (startTime < 0) startTime = 0;
//		snapshot = TrimVideoUtils.getVideoFrame(renameTo.getAbsolutePath(),(long) startTime);
		snapshot = TrimVideoUtils.getVideoFrame(renameTo.getAbsolutePath(),(long) length);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return snapshot;
    }
    
	private void saveSnapshot(final Bitmap snapshot) {
    	currentLocalFilename = Utils.getCurrentLocalFilename();
    	new AsyncTask<Object, Object, Object>() {
    		
    		@Override
    		protected void onPreExecute() {
    			super.onPreExecute();  
    			captureButton_cont.setVisibility(View.VISIBLE); 
    		}
    	  
            @Override
            protected Object doInBackground(Object... params) {
            	
            	try {
        	        FileOutputStream fos = new FileOutputStream(TrimVideoUtils.getLocalImagesPath(VideoCaptureActivityORIGINAL.this) + "/" + currentLocalFilename + ".jpg");
        	       
        	        snapshot.compress(Bitmap.CompressFormat.PNG, 90, fos); 
        	        
        	        fos.close();
        			canClickCapture = true;
        			System.out.println("CLICK CAPTURE" + canClickCapture);
        			
            		} catch (Exception e) {
            			e.printStackTrace();
            		}
            	
				return params;  	
            }
   
            @Override
            protected void onPostExecute(Object obj) {
                new ProcessingTask(VideoCaptureActivityORIGINAL.this, vcListner, currentLocalFilename).execute();
            }
            
        }.execute();			
	}
	 
    private File initFile() {
        this.file = new File(TrimVideoUtils.getLocalTmpPath(this), "tmp_rec.mp4");
        return this.file;
    }
    
    public void setSnapshot(final Bitmap snp) {
    	
    	Log.e("SNAPSHOT", "SET");
    	
    	if (snp == null) {
    		showUi(false);
    		//hideUi();
    		return;
    	}
    	 
        //ORIGINALLY THIS IS HERE
    	runOnUiThread(new Runnable() {
			@Override
			public void run() {
				
            	showUi(true);
		    	snapshot.setImageBitmap(snp); 
		    	  
		    	/*
		    	captureButton_cont.setVisibility(View.VISIBLE); 
		    	Log.e("SNAPSHOT", "SET ON UI THREAD");
		    	FNumberOfVideosRecorded = FNumberOfVideosRecorded + 1;
		    	*/
		    	
			}
		});
    	 
    	
    	
    	saveSnapshot(snp); 
    }
    
    public int getDeviceDefaultOrientation() {
        int o = getScreenOrientation();
        if ( o == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE || o == ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE) 
          return Configuration.ORIENTATION_LANDSCAPE;
        else 
          return Configuration.ORIENTATION_PORTRAIT;
    }

    /*
	@Override
	public void videoProcessCompleteORIGINAL(String dir, String VideoImageBasename) { 
	 
		Log.i("VideocaptureActivity", "*************************videoProcessComplete*************************");
		Log.i("VideocaptureActivity", "Dir: " + dir);
		Log.i("VideocaptureActivity", "Basename: " + VideoImageBasename);
//		new AmazonProcessingTask(this, vcListner, dir, basename, "video").execute();
		Log.i("VideocaptureActivity", "************************* END videoProcessComplete*************************");
	  
		canPlayPreview 	= true;
		previewPath 	= TrimVideoUtils.getLocalVideoPath(this) + "/" + VideoImageBasename + ".mp4"; 
		DatabaseHelper DB = new DatabaseHelper(this);
		updateCurrentCapture();
		FCurrentCapture.imageFileName = VideoImageBasename + ".jpg";
		FCurrentCapture.videoFileName = VideoImageBasename + ".mp4";
		FCurrentCapture.modified = "1";
		FCurrentCapture = DB.createUpdateCapture(FCurrentCapture);
		runOnUiThread( new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				previewPlay.setVisibility(View.VISIBLE);  
				Toast.makeText(getApplicationContext(), "PWEDE NAKA MUSAVE BAI", Toast.LENGTH_LONG).show();
			}
		}); 
	}
	*/
	 
    @Override
	public void videoProcessComplete(String dir, String VideoImageBasename) {  
    	
		canPlayPreview 			  = true;
		previewPath 			  = TrimVideoUtils.getLocalVideoPath(this) + "/" + VideoImageBasename + ".mp4";  
		String localImageFilePath = TrimVideoUtils.getLocalImagesPath(this) + "/" + VideoImageBasename + ".jpg"; 
		
		/* 
		DatabaseHelper DB = new DatabaseHelper(this);
		updateCurrentCapture();
		FCurrentCapture.imageFileName = VideoImageBasename + ".jpg";
		FCurrentCapture.videoFileName = VideoImageBasename + ".mp4";
		FCurrentCapture.modified = "1";
		FCurrentCapture = DB.createUpdateCapture(FCurrentCapture);
		runOnUiThread( new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				previewPlay.setVisibility(View.VISIBLE);  
				Toast.makeText(getApplicationContext(), "PWEDE NAKA MUSAVE BAI", Toast.LENGTH_LONG).show();
			}
		});  
		*/  
		new videoProcessCompleteTask( previewPath, localImageFilePath, VideoImageBasename + ".mp4", VideoImageBasename + ".jpg" ).execute();  
	}
    
    
	private class videoProcessCompleteTask extends AsyncTask<String, Integer, Void> { 
		private String localVideoFilePath;
		private String localImageFilePath;
		private String localVideoFileName;
		private String localImageFileName;
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();   
		}
		 
		private videoProcessCompleteTask(String localVideoFilePath, String localImageFilePath, String localVideoFileName, String localImageFileName) { 
			this.localVideoFilePath = localVideoFilePath;
		 	this.localImageFilePath = localImageFilePath; 
		 	this.localVideoFileName = localVideoFileName;
		 	this.localImageFileName = localImageFileName;
	    }
		 
		@Override
		protected Void doInBackground(String... params) { 
			//nothing to do here
			return null; 
		} 
		
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);   
			//captureButton_cont.setVisibility(View.VISIBLE);  
			previewPlay.setVisibility(View.VISIBLE);  
			 
			getNextMediaId( FEventId, this.localVideoFilePath, this.localImageFilePath, this.localVideoFileName, this.localImageFileName);  
			
			
			
			//new getNextMediaIdTask( FEventId, this.localVideoFilePath, this.localImageFilePath, this.localVideoFileName, this.localImageFileName).execute(); 
			
		} 
	}
      
	private String FLatestHighlightMediaId;


	private class getNextMediaIdTask extends AsyncTask<String, Integer, Void> { 
		private String timeStamp;
		private String eventID;
		private String localVideoFilePath;
		private String localImageFilePath;
		private String localVideoFileName;
		private String localImageFileName; 
		private String latitude_; 
		private String longitude_;
		
		//private boolean hey;
		@Override
		protected void onPreExecute() {
			super.onPreExecute();   
		} 

		public getNextMediaIdTask( final String eventID, final String localVideoFilePath, final String localImageFilePath, final String localVideoFileName, final String localImageFileName ) {
	        this.timeStamp 			= CommonFunctions_1.getCurrentTimeStamp();
			this.eventID 			= eventID;
			this.localVideoFilePath	= localVideoFilePath;
			this.localImageFilePath	= localImageFilePath;
			this.localVideoFileName	= localVideoFileName;
			this.localImageFileName	= localImageFileName; 
			this.latitude_			= String.valueOf( GlobalVariablesHolder.user_Latitude ); 
			this.longitude_ 		= String.valueOf( GlobalVariablesHolder.user_Longitude ); 
		}

		@Override
		protected Void doInBackground(String... params) { 
		 
			RequestParams requestParams = new RequestParams();   
			requestParams.put("_ts", 			timeStamp);
			requestParams.put("eventId", 		eventID );
			requestParams.put("videoLocalPath", localVideoFilePath );
			requestParams.put("imageLocalPath",	localImageFilePath );
			requestParams.put("latitude", 		latitude_ );
			requestParams.put("longitude", 		longitude_ );
		          
			
			
			Async_HttpClient async_HttpClient = new Async_HttpClient( VideoCaptureActivityORIGINAL.this ); 
			async_HttpClient.GET("GetNextMediaId", requestParams,	
				new JsonHttpResponseHandler() { 
					@Override
					public void onStart() {
						// TODO Auto-generated method stub
						super.onStart();
						// Log.v("onStart", "onStart"); 
					}
		
					@Override
					public void onSuccess(final JSONObject response) {
						/** SAMPLE response: {
									  "twitterCardUrl":"http:\/\/goo.gl\/60Haxl",
									  "shareUrl":"http:\/\/goo.gl\/3xRekv",
									  "mediaId":"14003",
									  "shareText":"Watch this Ping Pong highlight from Ayala Center in Cebu captured by SportXast",
									  "added":"new media added",
									  "imagePath":"db\/event_media\/2014\/07\/09\/14003.jpg",
									  "videoPath":"db\/event_media\/2014\/07\/09\/14003.mp4"
									  } **/
						// TODO Auto-generated method stub
						super.onSuccess(response);
						Toast.makeText(getApplicationContext(), "HUMANAG UPLOAD SA getNextMediaId", Toast.LENGTH_LONG).show();
						
						captureButton_cont.setVisibility(View.VISIBLE); 
				    	Log.e("SNAPSHOT", "SET ON UI THREAD");
				    	
				    	FNumberOfVideosRecorded = FNumberOfVideosRecorded + 1;
						if(response != null){
						try {   
							FLatestHighlightMediaId =  response.getString("mediaId") ; 
							FGlobal_Data.setVideoPath( response.getString("videoPath") ); 
				    		//######################################################################## 
				    		//String eventId, String coverImage, String largeImageUrl, String videoLocalPath, String imageLocalPath
							FGlobal_Data.setNewlyUploadedHighlightsData2( response, eventID, localImageFileName, "", localVideoFilePath, localImageFilePath, localVideoFileName );
				    		//########################################################################
							} catch (Exception e) {
								e.printStackTrace();
								}
						 
							if( GlobalVariablesHolder.firstTimeUseOfVideoCapture ){
			            		//startTimerOnVideoProcessingComplete(); 
			            		startPreview(true); 
			            		} 
						    
							
						} else {
							FLatestHighlightMediaId = ""; 
							/*
							capture.inQueue = "0";
						    DB.createUpdateCapture(capture);
						    */ 
						} 
					}
		
					@Override
					public void onFinish() {
						// TODO Auto-generated method stub
						super.onFinish();
						// Log.v("onFinish", "onFinish"); 
					}
		
					@Override
					public void onFailure(String responseBody, Throwable error) {
						// TODO Auto-generated method stub
						super.onFailure(responseBody, error);
						Log.v("onFailure", "onFailure :" + responseBody + " : " + error);
						 
					}
				});  
			
				return null;
			} 

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);   
		}
	}
	
	private void getNextMediaId( final String eventID, final String localVideoFilePath, final String localImageFilePath, final String localVideoFileName, final String localImageFileName ) { 	      
		//final DatabaseHelper DB = new DatabaseHelper( VideoCaptureActivity.this ); 
		final String timeStamp 			= CommonFunctions_1.getCurrentTimeStamp();
		//final String eventID 			= currentCapture.mediaEventID;
		//final String localVideoFilePath = TrimVideoUtils.getLocalVideoPath( VideoCaptureActivity.this ) + "/" + currentCapture.videoFileName; 
		//final String localImageFilePath = TrimVideoUtils.getLocalImagesPath( VideoCaptureActivity.this ) + "/" + currentCapture.imageFileName;
		final String latitude_ 			= String.valueOf( GlobalVariablesHolder.user_Latitude ); 
		final String longitude_ 		= String.valueOf( GlobalVariablesHolder.user_Longitude ); 
		  
		RequestParams requestParams = new RequestParams();   
		requestParams.put("_ts", 			timeStamp );
		requestParams.put("eventId", 		eventID );
		requestParams.put("videoLocalPath", localVideoFilePath );
		requestParams.put("imageLocalPath",	localImageFilePath );
		requestParams.put("latitude", 		latitude_ );
		requestParams.put("longitude", 		longitude_ );
	          
		String reqs = requestParams.toString();
		
		Async_HttpClient async_HttpClient = new Async_HttpClient( VideoCaptureActivityORIGINAL.this ); 
		async_HttpClient.GET("GetNextMediaId", requestParams,	
			new JsonHttpResponseHandler() { 
				@Override
				public void onStart() {
					// TODO Auto-generated method stub
					super.onStart();
					// Log.v("onStart", "onStart"); 
				}
	
				@Override
				public void onSuccess(final JSONObject response) {
					/** SAMPLE response: {
								  "twitterCardUrl":"http:\/\/goo.gl\/60Haxl",
								  "shareUrl":"http:\/\/goo.gl\/3xRekv",
								  "mediaId":"14003",
								  "shareText":"Watch this Ping Pong highlight from Ayala Center in Cebu captured by SportXast",
								  "added":"new media added",
								  "imagePath":"db\/event_media\/2014\/07\/09\/14003.jpg",
								  "videoPath":"db\/event_media\/2014\/07\/09\/14003.mp4"
								  } **/
					// TODO Auto-generated method stub
					super.onSuccess(response);
					//Toast.makeText(getApplicationContext(), "HUMANAG UPLOAD SA getNextMediaId", Toast.LENGTH_LONG).show();
					
					captureButton_cont.setVisibility(View.VISIBLE); 
			    	Log.e("SNAPSHOT", "SET ON UI THREAD");
			    	
			    	FNumberOfVideosRecorded = FNumberOfVideosRecorded + 1;
					if(response != null){
					try {   
						FLatestHighlightMediaId =  response.getString("mediaId") ; 
						FGlobal_Data.setVideoPath( response.getString("videoPath") ); 
			    		//######################################################################## 
			    		//String eventId, String coverImage, String largeImageUrl, String videoLocalPath, String imageLocalPath
						FGlobal_Data.setNewlyUploadedHighlightsData2( response, eventID, localImageFileName, "", localVideoFilePath, localImageFilePath, localVideoFileName );
			    		//########################################################################
						} catch (Exception e) {
							e.printStackTrace();
							}
					 
						if( GlobalVariablesHolder.firstTimeUseOfVideoCapture ){
		            		//startTimerOnVideoProcessingComplete(); 
		            		startPreview(true); 
		            		} 
					   
						FGlobal_Data.runThreadUploader(VideoCaptureActivityORIGINAL.this);  
						
					} else {
						FLatestHighlightMediaId = "";
						
						/*
						capture.inQueue = "0";
					    DB.createUpdateCapture(capture);
					    */ 
					} 
				}
	
				@Override
				public void onFinish() {
					// TODO Auto-generated method stub
					super.onFinish();
					// Log.v("onFinish", "onFinish"); 
				}
	
				@Override
				public void onFailure(String responseBody, Throwable error) {
					// TODO Auto-generated method stub
					super.onFailure(responseBody, error);
					Log.v("onFailure", "onFailure :" + responseBody + " : " + error);
					 
				}
			});  
	}
	
	private _MediaStorage FCurrentCapture; 
 
  

	private class setNextMediaId extends AsyncTask<String, Integer, Void> { 
		private String timeStamp;
		private String eventID;
		private String localVideoFilePath;
		private String localImageFilePath;
		private String localVideoFileName;
		private String localImageFileName; 
		private String latitude_; 
		private String longitude_;
		
		//private boolean hey;
		@Override
		protected void onPreExecute() {
			super.onPreExecute();   
		} 

		public setNextMediaId(  final String eventID, final String localVideoFilePath, final String localImageFilePath, final String localVideoFileName, final String localImageFileName  ) {
	        this.timeStamp 			= CommonFunctions_1.getCurrentTimeStamp();
			this.eventID 			= eventID;
			this.localVideoFilePath	= localVideoFilePath;
			this.localImageFilePath	= localImageFilePath;
			this.localVideoFileName	= localVideoFileName;
			this.localImageFileName	= localImageFileName; 
			this.latitude_			= String.valueOf( GlobalVariablesHolder.user_Latitude ); 
			this.longitude_ 		= String.valueOf( GlobalVariablesHolder.user_Longitude ); 
		}

		@Override
		protected Void doInBackground(String... params) { 
	    //############################################### 
	        //this.hey = false;
			JSONObject nextMediaIdObject = new JSONObject(); 
			try { 
				nextMediaIdObject.put("_ts", 				this.timeStamp);
				nextMediaIdObject.put("eventId", 			this.eventID );
				nextMediaIdObject.put("videoLocalPath",		this.localVideoFilePath );
				nextMediaIdObject.put("imageLocalPath",		this.localImageFilePath );
				nextMediaIdObject.put("latitude", 			this.latitude_ );
				nextMediaIdObject.put("longitude",			this.longitude_ );
				nextMediaIdObject.put("localVideoFileName",	this.localVideoFileName );
				nextMediaIdObject.put("localImageFileName",	this.localImageFileName );
				 
				} catch (JSONException e) {
				// TODO Auto-generated catch block
					e.printStackTrace();
				} 
  
			getApplicationContext();
			SharedPreferences sharedpreferences = getSharedPreferences( "com.sportxast.SportXast.highlights", Context.MODE_PRIVATE );
  
			String newJSONString = "";   
			if (sharedpreferences.contains( Constants.sharePrefKey_uploadedHighlights ) )
				{
				newJSONString = sharedpreferences.getString(Constants.sharePrefKey_uploadedHighlights, ""); 
				}
		  
				newJSONString = newJSONString +"||"+ nextMediaIdObject.toString();
		  
		//################################################################################## 
				if( newJSONString.substring(0, 2).equals("||") ){
					 newJSONString = newJSONString.substring(2);
				 }
				   
		      Editor editor = sharedpreferences.edit();
		      editor.putString(Constants.sharePrefKey_uploadedHighlights, newJSONString); 
		      editor.commit();  
			  //this.hey =true;
				return null;
			} 

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			 
			//Toast.makeText(getApplicationContext(), "success!! success!!", Toast.LENGTH_LONG).show();
			//Toast.makeText(getApplicationContext(), "YO BRO WHAT'S UP MAN???", Toast.LENGTH_LONG).show(); 
		}
	}
	@Override
	public FrameLayout getCameraPreviewFrame() {
		// TODO Auto-generated method stub
		return cameraPreviewFrame;
	}
	
	private int FNumberOfVideosRecorded = 0;  
	
	private boolean videoCaptureModeIsOn;
	
	private void beginVideoCaptureMode(){
		 
		defaultOrientation = getDeviceDefaultOrientation();
        if (defaultOrientation == Configuration.ORIENTATION_PORTRAIT) { 
        	portraitview_cover.setVisibility(View.VISIBLE);
        	return;
        }        
        
		//###########################################################################
        //###########################################################################
        
        if( videoCaptureModeIsOn ){
        	
        }else{ 
	    	portraitview_cover.setVisibility(View.GONE); 
	        if(this.FCallingActivityID == Constants.requestCode_Menu_Activity){ 
	        	
	        	if( GlobalVariablesHolder.FLatestEvent != null){
	        	
		        	if(GlobalVariablesHolder.alreadyCheckedIntoAnEvent){ 
						
		        		//supplyChosenEvent( GlobalVariablesHolder.FLatestEvent );
						
						}
		        	else
						{ 
						showCheckIntoDialog(1, GlobalVariablesHolder.FLatestEvent.eventName);
						}   
	        	}
	        } 
	        
	        this.waitText.setVisibility(View.VISIBLE); 
	        captureButton_cont.setVisibility(View.GONE);
	        //captureButton.setVisibility(View.GONE);
	         
	        // initialize the camera in background, as this may take a while
	        System.out.println("------------------------------------------------------------ ON RESUME"); 
	        new prepareCameraTask().execute(); 
	        videoCaptureModeIsOn = true;
        }
	}
 
	@Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
 
        switch(newConfig.orientation) { 
            case Configuration.ORIENTATION_LANDSCAPE: 
            	/*
            	Toast.makeText(getApplicationContext(), "SCREENWIDTH: "+portraitview_cover.getWidth() +"\n" +
						   "SCREENHEIGHT: "+portraitview_cover.getHeight(), Toast.LENGTH_LONG).show();
            	*/ 
            	beginVideoCaptureMode(); 
                break;
            case Configuration.ORIENTATION_PORTRAIT:
            	Log.e("onConfigurationChanged", "PORTRAIT"); 
            	portraitview_cover.setVisibility(View.GONE); 
            	releaseResources(); 
            	
            	/*
            	if( FCallingActivityID == Constants.requestCode_Highlight_Activity ){
            		//######################################################
                	Intent returnIntent = new Intent();
                	returnIntent.putExtra("numberOfVideosRecorded", FNumberOfVideosRecorded);
                	setResult(RESULT_OK, returnIntent); 
                	//###################################################### 
            	}
            	else if( FCallingActivityID == Constants.requestCode_Create_Activity ){
            		gotoHighlight_Activity();
            	} 
            	*/
             
            	if( FChosenEvent == null){
            		gotoProfile_Activity();
            	}else if( FNumberOfVideosRecorded > 0 )
            		gotoHighlight_Activity();
            	else{
            		finish();
            	}
            	//finish();
                break;
        }
        
        if (newConfig.hardKeyboardHidden == Configuration.KEYBOARDHIDDEN_YES && uiIsShowing()) {
        	uicontainer.setVisibility(View.VISIBLE);
        }
    }
	
	private class prepareCameraTask extends AsyncTask<Void, Void, Camera> { 
	  
		@Override
		protected void onPreExecute() {
			super.onPreExecute();   
			captureButton_cont.setVisibility(View.GONE);
			
			releaseCamera(); 
			if( cameraPreviewFrame.getVisibility() == View.GONE )
				cameraPreviewFrame.setVisibility(View.VISIBLE);
			
			//captureButton.setVisibility(View.GONE);
		}
		
		@Override
		protected Camera doInBackground(Void... params) {  
            try {
            	 
               	devCamera = Camera.open();
               	  
                return devCamera == null ? Camera.open(0) : devCamera;
                
            } catch (RuntimeException e) {
            	 
            	//this.devCamera = Camera.open(0);
                Log.wtf(TAG, "Failed to get camera", e); 
                //return null;
                return Camera.open(0);
            }  
        } 
		
		@Override
		protected void onPostExecute(Camera devCamera) {
		super.onPostExecute(devCamera);

			 if (devCamera == null) { 
				 /*
				   try {
					   devCamera = Camera.open(0); 
					   
					   if(devCamera != null)
					   lolol();
					   //VideoCaptureActivity.this.initCamera(camera);  
		               } catch (RuntimeException e) { 
		            	new prepareCameraTask().execute();  
		            }  */
	    		 Toast.makeText(getApplicationContext(), "Camera not ready", Toast.LENGTH_SHORT).show();
	    		 
	         } else {
	             VideoCaptureActivityORIGINAL.this.initCamera(devCamera);  
	             //Toast.makeText(getApplicationContext(), "NAA NA GYUY CAMERA!!!!!!!", Toast.LENGTH_SHORT).show();
	         }
		}
	}
 
    private void initCamera(Camera camera) {
        // we now have the camera
        this.devCamera = camera;
        // create a preview for our camera
        this.FCameraPreview = new CameraPreview(VideoCaptureActivityORIGINAL.this, this.devCamera, vcListner);
        // add the preview to our preview frame
        this.cameraPreviewFrame.addView(this.FCameraPreview, 0);
        this.waitText.setVisibility(View.GONE);
        
        captureButton_cont.setVisibility(View.VISIBLE);
        //this.captureButton.setVisibility(View.VISIBLE); 
	     showTooltip( true ); 
        // startRecording();
        // enable just the record button
    }
    
	  
	private int getScreenOrientation() {
	    return Utils.getScreenOrientation(this);
	}

	@Override
	public void onDeleteCapture(_MediaStorage capture) {
		
		/*
		 
		// TODO Auto-generated method stub
		if (uiIsShowing()) {
			showUi(false);
			//hideUi();
		}
		 
		updateCurrentCapture();
		FCurrentCapture = DB.updateCapture(capture);
//		Utils.updateMediaInfo(this, capture);
		
		*/
		
	}

	@Override
	public void onCommentAdd(_MediaStorage capture) {
		
		/*
		// TODO Auto-generated method stub
		btnAddComment.setImageResource(R.drawable.bbtn_comment_on);
		btnAddComment.setEnabled(false);
		updateCurrentCapture();
		FCurrentCapture = DB.updateCapture(capture);
//		Utils.updateMediaInfo(this, capture);
		hideKeyboard();
		
		*/
		
	}

	@Override
	public void onFavoriteSuccess(_MediaStorage capture) {
		
		/*
		
		// TODO Auto-generated method stub
		btnAddFav.setImageResource(R.drawable.bbtn_favorite_on);
		btnAddFav.setEnabled(false);
		updateCurrentCapture();
		FCurrentCapture = DB.updateCapture(capture);
//		Utils.updateMediaInfo(this, capture);
		
		*/
		
	}
	@Override
	public void onTagSuccess(_MediaStorage capture) {
		/*
		// TODO Auto-generated method stub
		btnAddTag.setImageResource(R.drawable.bbtn_tag_on);
		btnAddTag.setEnabled(false);
		updateCurrentCapture();
		FCurrentCapture = DB.updateCapture(capture);
//		Utils.updateMediaInfo(this, capture);
		*/
	}
	
	
	public void onShareSuccess(_MediaStorage capture) {
		
		/*
		
		// TODO Auto-generated method stub
		btnAddTag.setImageResource(R.drawable.bbtn_share_on);
		btnAddTag.setEnabled(false);
		updateCurrentCapture();
		FCurrentCapture = DB.updateCapture(capture);
//		Utils.updateMediaInfo(this, capture);
		
		*/
	}
	
	public void setMute() {
		AudioManager audioMgr = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
		oldStreamVolume = audioMgr.getStreamVolume(AudioManager.STREAM_RING);
		audioMgr.setStreamVolume(AudioManager.STREAM_RING, 0, 0);
	}
	
	public void restoreSounds() {
		AudioManager audioMgr = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
		oldStreamVolume = audioMgr.getStreamVolume(AudioManager.STREAM_RING);
		audioMgr.setStreamVolume(AudioManager.STREAM_RING, oldStreamVolume, 0);
	}
	
	public void hideKeyboard()
	{
		if (uicontainer.getVisibility() == View.GONE && uiIsShowing()) {
			uicontainer.setVisibility(View.VISIBLE);
		}
		try {
			System.out.println("Try to hide keyboard");
		        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
		        imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void startPreview(boolean autoPlay) { 
		//Toast.makeText(getApplicationContext(), previewPath, Toast.LENGTH_LONG).show();
		if ( (canPlayPreview && ! previewPath.equals(""))   ) { 
			//Toast.makeText(getApplicationContext(), "NAKASULOD!", Toast.LENGTH_LONG).show(); 
			Intent tostart = new Intent(this, VideoPreviewActivity.class);
			tostart.putExtra("mediaUrl", previewPath);  
			tostart.putExtra("callingActivityID", Constants.requestCode_VideoCapture_Activity); 
			startActivityForResult(tostart, Constants.requestCode_VideoPreviewActivity); 
			
			
			videoCaptureModeIsOn = false;
		} 
	}
	
	private Timer FRefresherTimer;  
    public void startTimerOnVideoProcessingCompleteXXXXXXXXX(){ 
		//final long timerWaitingTime = 700;  
		// ####################################### 
		FRefresherTimer = new Timer();
		FRefresherTimer.schedule(new TimerTask() { 
			@Override
			public void run() {
				runOnUiThread(new Runnable() {
					@Override
					public void run() {
						
						if(previewPath.length() > 0){
							stopTimer();
							startPreview(true);
							//Toast.makeText(getApplicationContext(), previewPath, Toast.LENGTH_LONG).show();
						} 
					}
				});
			} 
		}, 1000, 1000); //<== 700 interval, very important, do not change
		// ######################################## 
    }
	 

	public void stopTimer() {
		if (FRefresherTimer == null)
			return;
		// FRecordTimeLapse = 0;
		FRefresherTimer.cancel();
		FRefresherTimer.purge();
		FRefresherTimer = null; 
	}
 
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case Keys.share.RESULT_FROM_EMAIL:
				FCurrentCapture.isSharedOnMail = "1";
				break;
			case Keys.share.RESULT_FROM_FACEBOOK:
				FCurrentCapture.isSharedOnFacebook = "1";
				break;
			case Keys.share.RESULT_FROM_TWITTER:
				FCurrentCapture.isSharedOnTwitter = "1";
				break;
			case Keys.share.RESULT_FROM_SMS:
				FCurrentCapture.isSharedOnSms = "1";
				break;
			case Constants.requestCode_VideoPreviewActivity:    
				//Toast.makeText(getApplicationContext(), "balik na brad", Toast.LENGTH_LONG).show();
				GlobalVariablesHolder.firstTimeUseOfVideoCapture = false;
				showTooltip( false );
				
				beginVideoCaptureMode(); 
				break;  
			}
			FCurrentCapture.modified = "1";
			//DB.createUpdateCapture(FCurrentCapture);
		}
	}
	 
	 /*
	@Override
   protected void onResume() {
       super.onResume();
       this.waitText.setVisibility(View.VISIBLE);
       captureButton.setVisibility(View.GONE);
       
       // initialize the camera in background, as this may take a while
       System.out.println("------------------------------------------------------------ ON RESUME");
       new AsyncTask<Void, Void, Camera>() {
           @Override
           protected Camera doInBackground(Void... params) {
               try {
                   Camera camera = Camera.open();
                   return camera == null ? Camera.open(0) : camera;
               } catch (RuntimeException e) {
                   Log.wtf(TAG, "Failed to get camera", e);
                   return null;
               }
           }

           @SuppressWarnings("null")
			@Override
           protected void onPostExecute(Camera camera) {
     
           	
           	 if (camera == null) {
           		  
           		 camera.release(); 
           		 camera = null;
           		 camera = Camera.open(0); 
           		 VideoCaptureActivity.this.initCamera(camera); 
                   // Toast.makeText(getApplicationContext(), "Cannot_record", Toast.LENGTH_SHORT).show();
                } else {
                    VideoCaptureActivity.this.initCamera(camera);
                }
               
           }
       }.execute();
       defaultOrientation = getDeviceDefaultOrientation();
       if (defaultOrientation == Configuration.ORIENTATION_PORTRAIT) { 
       	portraitview_cover.setVisibility(View.VISIBLE);
       	//DialogOrientation.show();
       }        
   }
	*/
	 

	@Override
	public void updateCurrentCapture() {
		/*
		ContentValues contentValues = DB.getItem(
				Keys.KEY_MEDIA_STORAGE.TABLE_MEDIA_STORAGE,
				Keys.KEY_MEDIA_STORAGE.ID, FCurrentCapture.ID);
		if (contentValues.size() != 0) {
			FCurrentCapture = DB.cv2mediaStorage(contentValues);
		}
		*/
	}
}
