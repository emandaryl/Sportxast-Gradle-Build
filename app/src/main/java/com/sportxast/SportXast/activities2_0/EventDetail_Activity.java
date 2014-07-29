package com.sportxast.SportXast.activities2_0;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.webkit.URLUtil;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.androidquery.AQuery;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sportxast.SportXast.Global_Data;
import com.sportxast.SportXast.R;
import com.sportxast.SportXast.commons.CommonFunctions_1;
import com.sportxast.SportXast.commons.Constants;
import com.sportxast.SportXast.includes.HeaderUIClass;
import com.sportxast.SportXast.models._EventLists;
import com.sportxast.SportXast.models._EventLists.EventLists;
import com.sportxast.SportXast.models._EventLists.HighlightReelMedia;
import com.sportxast.SportXast.models._MediaLists;
import com.sportxast.SportXast.models._MediaLists.Tag;
import com.sportxast.SportXast.models._NewEventLists;
import com.sportxast.SportXast.models._NewEventLists.NewList;
import com.sportxast.SportXast.models._RecentEvent;
import com.sportxast.SportXast.thirdparty_class.Async_HttpClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

//import com.sportxast.SportXast.BaseSherlockActivity;
//public class EventDetail_Activity extends BaseSherlockActivity {
//import com.sportxast.SportXast.BaseSherlockActivity;
public class EventDetail_Activity extends Activity {

	/** Header components **/
	private HeaderUIClass headerUIClass;
	private RelativeLayout sx_header_wrapper;
	private RelativeLayout sx_header_wrapper2;
	
	AQuery FAndroidQuery;
	Async_HttpClient async_HttpClient;
	Global_Data FGlobal_Data;
	EventLists eventLists = new EventLists();
	_EventLists listEvents = new _EventLists();
	_MediaLists mediaLists = new _MediaLists();
	_NewEventLists playerList = new _NewEventLists();
	_NewEventLists fanList = new _NewEventLists();
	_NewEventLists favoriteList = new _NewEventLists();
	HighlightReelMedia highlightReelMedia = new HighlightReelMedia();
	
	TextView txtvw_sportname;
	TextView txtvw_venuename;
	TextView txtvw_date;
	TextView txtvw_commentCount;
	TextView txtvw_favoriteCount;
	TextView txtvw_highlightlistCount;
	TextView txtvw_taglistCount;
	//TextView txtvw_playerlistCount;
	TextView txtve_fanslistCount;

	
	View highlightHeaderView;
	//View playerHeaderView;
	View tagsHeaderView;
	View fansHeaderView;
	 
	View highlightReelsView;
	LinearLayout teamListView;
	LinearLayout highlightListView;
	LinearLayout tagListView;
	LinearLayout playerListView;
	LinearLayout fansListView;
	View subView;
	
	VideoView video_media;
	ImageView imgvw_mediaphoto;
	ImageButton imgbtn_play;
	ProgressBar pbar_loader;
	 
	private boolean isPause = false;
	private static final String TAG = "EventDetail";
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    // TODO Auto-generated method stub
	    setContentView(R.layout.layout_eventdetail);
	    
	   // initActionBarObjects();
	     
	    FAndroidQuery  	= new AQuery(this);
	    mediaLists.aq 	= new AQuery(this);
	    FGlobal_Data	= (Global_Data)getApplicationContext();
	    async_HttpClient= new Async_HttpClient(this);
	    eventLists 		= FGlobal_Data.getEventList();
	     
	   // getActionbar_EventsDetail("Event", true); 
	    prepareHeader();
	    /*
	    public void getActionbar_EventsDetail(String string_type, boolean isToday){
		reset();
		showBack(true);
		showTitle(true);
		showCamera(isToday?true:false);
		setTitleText(string_type); 
		}  */
	    
	    initializeResources();
	    
	    subView = addView(); 
	    video_media = (VideoView)findViewById(R.id.video_media);
		imgvw_mediaphoto = (ImageView)findViewById(R.id.imgvw_photo_eventdetail);
		imgbtn_play = (ImageButton)findViewById(R.id.imgbtn_play);
		pbar_loader = (ProgressBar)findViewById(R.id.progress_medialoading);
	    
		_RecentEvent selectedEvent = new _RecentEvent();
		selectedEvent.eventId = eventLists.eventId;
		selectedEvent.eventName = eventLists.eventName;
		selectedEvent.eventFirstTeam = eventLists.eventFirstTeam;
		selectedEvent.eventSecondTeam = eventLists.eventSecondTeam;
		FGlobal_Data.setNewEvent(selectedEvent);
		
		highlightHeaderView.setOnClickListener(new OnClickListener() { 
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub 
//				Intent intent = new Intent(EventDetail_Activity.this,Highlight_Activity.class);
//		
//				String team1 = eventLists.eventFirstTeam;
//				String team2 = eventLists.eventSecondTeam;
//				String eventTeams = team1.length() > 0 ? team1
//						+ (team2.length() > 0 ? " \n " + team2 : "") : team2.length() > 0 ? team2 : eventLists.eventName;
//		
//				intent.putExtra("eventId",eventLists.eventId);
//				intent.putExtra("eventTeams", eventTeams);
//				intent.putExtra("eventDate",eventLists.eventStartDateShort);
//				intent.putExtra("isToday",eventLists.eventIsOpen);
//				intent.putExtra("hashtag", "");
				
				Intent intent = new Intent(EventDetail_Activity.this, Highlight_Activity.class);
				intent.putExtra("eventParcel", CommonFunctions_1.parseToEventParcel(eventLists));
//				intent.putExtra("eventParcel", CommonFunctions_1.parseToEventParcel(  eventLists.eventLists.get(arg2-index) ) ); 
				startActivity(intent);
			}
		});
		
	   populateContentView();
	   populateEventTeamView();
	  // populate_Highlight_Tags_Comments_View();
	   
	   populateHighlightListView();
	   populateTagListView();
	   populatePlayerListView();
	   populateFanListView();
	   
	   fetchHighlightReelData();
//	   fetchListData("ExportPlayerList");
//	   fetchListData("ExportFanList");
//	   fetchListData("ExportFavoriteList"); 
	} 

	private void prepareHeader(){ 
		this.sx_header_wrapper = (RelativeLayout)findViewById(R.id.sx_header_wrapper);
		this.headerUIClass = new HeaderUIClass(this, sx_header_wrapper); 
		 
		/* 
		reset();
		showBack(true);
		showTitle(true);
		showCamera(isToday?true:false);
		setTitleText(string_type); 
		*/ 
		boolean eventLiveNow = true;
		
		this.headerUIClass.setMenuTitle(  	"Event"); 
		this.headerUIClass.showBackButton( 	true); 
		this.headerUIClass.showAddButton(  	false);  
		this.headerUIClass.showMenuButton(	false);  
		this.headerUIClass.showRefreshButton(false);  
		this.headerUIClass.showAboutButton(	false); 
		this.headerUIClass.showSearchButton(false); 
		this.headerUIClass.showDoneButton(	false);  
		this.headerUIClass.showCameraButton(eventLiveNow); 
		this.headerUIClass.showMenuTitle(	true); 
		 
		/* 
		if(eventLiveNow){//if TRUE, show SECOND Header(orange) 
			this.sx_header_wrapper2 = (RelativeLayout)findViewById(R.id.sx_header_wrapper2);
			this.sx_header_wrapper2.setVisibility(View.VISIBLE);
			
			LayoutInflater liHeader = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			RelativeLayout list_header = (RelativeLayout) liHeader.inflate( R.layout.list_header, null);
			
			((TextView) list_header.findViewById(R.id.txtvw_header_title) ).setText("Capture a Highlight");
			 
			this.sx_header_wrapper2.addView(list_header,  new RelativeLayout.LayoutParams( LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			this.sx_header_wrapper2.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Toast.makeText(getApplicationContext(), "testtest", Toast.LENGTH_LONG).show();
				}
			});
			
		}
		*/
		
		
		
		addHeaderButtonListener(); 
	}

	private void addHeaderButtonListener(){
		this.headerUIClass.setOnHeaderButtonClickedListener(new HeaderUIClass.OnHeaderButtonClickedListener() {
			
			@Override
			public void onSearchClicked() {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void onRefreshClicked() {
				// TODO Auto-generated method stub
			}
			
			@Override
			public void onMenuClicked() {
				// TODO Auto-generated method stub
				
			}
			/** @category Override onDone- from BaseSherlockActivityclass
			 * custom method from BaseSherlockActivity.Invoked when done button (found
			 * on right side of the action bar) is clicked/pressed. */
			@Override
			public void onDoneClicked() {
				// TODO Auto-generated method stub 
				//saveEventToServer(); 
				//headerUIClass.enableDoneButton(false); 
				//v.setEnabled(false);
			}
			
			@Override
			public void onCameraClicked() {
				// TODO Auto-generated method stub
				
			}
			/** @category Override onBack- from BaseSherlockActivityclass
			 * custom method from BaseSherlockActivity.Invoked when back button (found
			 * on left side of the action bar) is clicked/pressed. */
			@Override
			public void onBackClicked() {
				// TODO Auto-generated method stub
				finish();
			}
			
			@Override
			public void onAddClicked() {
				// TODO Auto-generated method stub 
			}
			
			@Override
			public void onAboutClicked() {
				// TODO Auto-generated method stub
				
			}
		});
	} 
	
	private void initializeResources(){ 
	    txtvw_sportname 	= (TextView)findViewById(R.id.txtvw_sportname);
	    txtvw_venuename 	= (TextView)findViewById(R.id.txtvw_venuename);
	    txtvw_date 			= (TextView)findViewById(R.id.txtvw_date);
	    txtvw_commentCount 	= (TextView)findViewById(R.id.txtvw_commentCount);
	    txtvw_favoriteCount = (TextView)findViewById(R.id.txtvw_favoriteCount);
	    txtvw_highlightlistCount = (TextView)findViewById(R.id.txtvw_highlightListCount);
	    txtvw_taglistCount 	= (TextView)findViewById(R.id.txtvw_taglistCount);
	   // txtvw_playerlistCount = (TextView)findViewById(R.id.txtvw_playerlistCount);
	    txtve_fanslistCount = (TextView) findViewById(R.id.txtvw_fanslistCount);
	    
	    highlightHeaderView = (RelativeLayout)findViewById(R.id.layout_highlight_header);
	   // playerHeaderView = (RelativeLayout)findViewById(R.id.layout_player_header);
	    tagsHeaderView 		= (RelativeLayout)findViewById(R.id.layout_tags_header);
	    fansHeaderView 		= (RelativeLayout) findViewById(R.id.layout_fans_header);
	    
	    teamListView = (LinearLayout)findViewById(R.id.layout_teams_eventdetail);
	    highlightReelsView 	= (RelativeLayout)findViewById(R.id.layout_highlightreel);
	    highlightListView 	= (LinearLayout)findViewById(R.id.layout_highlightlist_eventdetail);
	    tagListView 		= (LinearLayout)findViewById(R.id.layout_taglist_eventdetail);
	    playerListView 		= (LinearLayout)findViewById(R.id.layout_playerlist_eventdetail);
	    fansListView 		= (LinearLayout) findViewById(R.id.layout_fanslist_eventdetail);
	    		
	    
	    tagsHeaderView.setOnClickListener(onclickFansTags);
	    fansHeaderView.setOnClickListener(onclickFansTags);
	}
	 
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		if (isPause) {
			exportTags();
			isPause = false;
		}
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		isPause = true;
	}
	/*
	
	@Override
	public void onBack(View v) {
		// TODO Auto-generated method stub
		super.onBack(v);
		
		finish();
	}
	*/
	
	private void exportTags() {
		/*
		 * Export tags to update count, when
		 * coming back from Highlights page.
		 */
		RequestParams params = new RequestParams();
		params.put(Constants.EXTRA_EVENT_ID, eventLists.eventId);
		
		async_HttpClient.GET(Constants.EXPORT_EVENT_TAG, params, new JsonHttpResponseHandler() {
			
			@Override
			public void onSuccess(JSONObject response) {
				super.onSuccess(response);
				
				if(response.isNull(Constants.EXTRA_RESULT_COUNT) == false) {
					/*
					 * Is null false. JSON mapping found.
					 */
					try {
						String resultTagCount = response.getString(Constants.EXTRA_RESULT_COUNT);
						eventLists.eventTotalTagCount = resultTagCount;
						txtvw_taglistCount.setText(resultTagCount);
						
						ArrayList<Tag> arrlistTags = new ArrayList<Tag>();
						JSONArray arrayTags = response.getJSONArray(Constants.EXTRA_LIST);
						for(int i=0; i<arrayTags.length(); i++) {
							Tag tag = new Tag();
							tag.id = arrayTags.getJSONObject(i).getString(Constants.EXTRA_ID);
							tag.name = arrayTags.getJSONObject(i).getString(Constants.EXTRA_NAME);
							
							arrlistTags.add(tag);
						}
						
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
				
			}
			
			@Override
			public void onFailure(String response, Throwable error) {
				super.onFailure(response, error);
				
			}
			
		});
	}
	
	private OnClickListener onclickFansTags = new OnClickListener() {
		
		@Override
		public void onClick(View view) {
			int id = ((RelativeLayout) view).getId();
			int listType = 0;
			if(id == tagsHeaderView.getId()) {
				listType = FansTagsActivity.LIST_TAGS;
			} else if(id == fansHeaderView.getId()) {
				listType = FansTagsActivity.LIST_FANS;
			}
			
			Intent intent = new Intent(EventDetail_Activity.this, FansTagsActivity.class);
			intent.putExtra(Constants.EXTRA_EVENT_ID, eventLists.eventId);
			intent.putExtra(Constants.EXTRA_EVENT_TYPE, listType);
			startActivity(intent);
		}
	};
	
	public void populateContentView(){
		
		
		txtvw_highlightlistCount.setText(eventLists.eventTotalMediaCount);
		txtvw_taglistCount.setText(eventLists.eventTotalTagCount);
		//txtvw_playerlistCount.setText(eventLists.eventTotalPlayerCount);
		txtve_fanslistCount.setText(eventLists.eventTotalFanCount);
		
		if(eventLists.eventIsOpen.equals("") ){
			eventLists.eventIsOpen = "0";
		}
		if(Integer.parseInt(eventLists.eventIsOpen)==0){
			highlightReelsView.setVisibility(View.VISIBLE);
		}else{
			highlightReelsView.setVisibility(View.GONE);
			
		}
		
		txtvw_sportname.setText(eventLists.eventSportName);
		txtvw_venuename.setText(eventLists.eventLocation);
		txtvw_date.setText(eventLists.eventStartDateFormatted);
		
			txtvw_favoriteCount.setText(eventLists.eventTotalFavoriteCount);
			txtvw_commentCount.setText(eventLists.eventTotalCommentCount);
	
			if(eventLists.favoriteCountWithMedia.size()>0){	
				FAndroidQuery.id(imgvw_mediaphoto).image(eventLists.favoriteCountWithMedia.get(0).coverImageThumb, false, true);
			}
	
			
			
		
	}
	public void populateEventTeamView(){
		
		if(eventLists.eventTeams.size()>0){
			for (int i = 0; i < eventLists.eventTeams.size(); i++) {
				View view = addTeamView();
				//aq.id((ImageView)view.findViewById(R.id.imgvw_photo)).image(eventLists.mediaTagWithHighlight.get(i).coverImageThumb, false, true);
				((TextView)view.findViewById(R.id.txtvw_subtitle)).setText(eventLists.eventTeams.get(i).name);
				teamListView.addView(view);	
			}
		}
		
	}
	public void populateHighlightReelsView(){
			 //Uri uri = Uri.parse(highlightReelMedia.url);
		
		
			//Log.e("url", ""+highlightReelMedia.url);
		
			

			final MediaController mc = new MediaController(this);
			mc.setAnchorView(video_media);
			mc.setMediaPlayer(video_media);
			//try {
				video_media.setVideoPath( highlightReelMedia.url );//getDataSource(highlightReelMedia.url) );
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
		//	video_media.setMediaController(mc);
			//video_media.setVideoURI(uri);
			
			video_media.start();
			video_media.requestFocus();
			
			video_media.setOnPreparedListener(new OnPreparedListener() {

				@Override
				public void onPrepared(MediaPlayer mp) {
					// TODO Auto-generated method stub

					mp.setOnBufferingUpdateListener(new OnBufferingUpdateListener() { 

						@Override
						public void onBufferingUpdate(MediaPlayer mp, int percent) {
							// TODO Auto-generated method stub

							if (percent == 100) {
								imgvw_mediaphoto.setVisibility(View.INVISIBLE);
								imgbtn_play.setVisibility(View.GONE);
								pbar_loader.setVisibility(View.GONE);
								
						    }
								else{
								imgvw_mediaphoto.setVisibility(View.VISIBLE);
								imgbtn_play.setVisibility(View.GONE);
								pbar_loader.setVisibility(View.VISIBLE);
							}

						}

					});

				}

			});
		
		
			//new  BackgroundAsyncTask().execute(highlightReelMedia.url);
		
			imgbtn_play.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					
					
					Toast.makeText(EventDetail_Activity.this, ""+"play video", Toast.LENGTH_SHORT).show();
					imgvw_mediaphoto.setVisibility(View.INVISIBLE);
					imgbtn_play.setVisibility(View.GONE);
					pbar_loader.setVisibility(View.GONE);
					video_media.requestFocus();
					video_media.start();
				}
			});
			
		
		
		
	}
	
	
	public void populate_Highlight_Tags_Comments_View(){
		
		
		
		
		if(parseInt(eventLists.eventTotalMediaCount)>=4){
			highlightListView.setVisibility(View.VISIBLE);
			highlightListView.removeAllViews();
			
			
			for (int i = 0; i < eventLists.favoriteCountWithMedia.size(); i++) {
				View view = addView();
				//aq.id((ImageView)view.findViewById(R.id.imgvw_photo)).image(eventLists.favoriteCountWithMedia.get(i).coverImageThumb, false, true);
				//((TextView)view.findViewById(R.id.txtvw_subtitle)).setText(eventLists.favoriteCountWithMedia.get(i).favoriteCount);
				highlightListView.addView(view);
			}
			
		}else{
				highlightListView.setVisibility(View.GONE);
		}
		
		
		if(parseInt(eventLists.eventTotalTagCount)>=4){
			tagListView.setVisibility(View.VISIBLE);
			tagListView.removeAllViews();
			
			
			for (int i = 0; i < eventLists.mediaTagWithHighlight.size(); i++) {
				View view = addView();
				FAndroidQuery.id((ImageView)view.findViewById(R.id.imgvw_photo)).image(eventLists.mediaTagWithHighlight.get(i).coverImageThumb, false, true);
				((TextView)view.findViewById(R.id.txtvw_subtitle)).setText(eventLists.mediaTagWithHighlight.get(i).tagName);
				tagListView.addView(view);
			}
			
		}else{
			tagListView.setVisibility(View.GONE);
		}
		
		
		
		if(parseInt(eventLists.eventTotalPlayerCount)>=4){
			playerListView.setVisibility(View.VISIBLE);
			playerListView.removeAllViews();
			
			
			for (int i = 0; i < eventLists.mediaWithPlayer.size(); i++) {
				View view = addView();
				FAndroidQuery.id((ImageView)view.findViewById(R.id.imgvw_photo)).image(eventLists.mediaWithPlayer.get(i).avatarPath, false, true);
				((TextView)view.findViewById(R.id.txtvw_subtitle)).setText(eventLists.mediaWithPlayer.get(i).name);
				playerListView.addView(view);
			}
			
		}else{
			playerListView.setVisibility(View.GONE);
		}
		
	
		
//		for (int i = 1; i < mediaLists.mediaLists.size(); i++) {
//			View hvw = addView();
//			aq.id((ImageView)hvw.findViewById(R.id.imgvw_photo)).image(mediaLists.mediaLists.get(i).coverImage, false, true);
//			((TextView)hvw.findViewById(R.id.txtvw_subtitle)).setText(mediaLists.mediaLists.get(i).favoritesCount);
//			
//			highlightListView.addView(hvw);
//		
//			
//			View tvw = addView();
//			aq.id((ImageView)tvw.findViewById(R.id.imgvw_photo)).image(mediaLists.mediaLists.get(i).coverImage, false, true);
//			((TextView)tvw.findViewById(R.id.txtvw_subtitle)).setText(""+mediaLists.mediaLists.get(i).tags.size());
//			
//			tagListView.addView(tvw);
//			
//			View cvw = addView();
//			aq.id((ImageView)cvw.findViewById(R.id.imgvw_photo)).image(mediaLists.mediaLists.get(i).coverImage, false, true);
//			((TextView)cvw.findViewById(R.id.txtvw_subtitle)).setText(""+mediaLists.mediaLists.get(i).commentsCount);
//			
//			playerlistView.addView(cvw);
//		}
	}
	
	public void populateHighlightListView(){
		if(parseInt(eventLists.eventTotalMediaCount)>=4){
			highlightListView.setVisibility(View.VISIBLE);
			highlightListView.removeAllViews();
			
			
			for (int i = 0; i < eventLists.favoriteCountWithMedia.size(); i++) {
				View view = addView();
				FAndroidQuery.id((ImageView)view.findViewById(R.id.imgvw_photo)).image(eventLists.favoriteCountWithMedia.get(i).coverImageThumb, false, true);
				((TextView)view.findViewById(R.id.txtvw_subtitle)).setText(eventLists.favoriteCountWithMedia.get(i).favoriteCount);
				highlightListView.addView(view);
			}
			
		}else{
			highlightListView.setVisibility(View.GONE);
		}
	}
	
	public void populateTagListView(){
		if(parseInt(eventLists.eventTotalTagCount)>=4){
			tagListView.setVisibility(View.VISIBLE);
			tagListView.removeAllViews();
			
			
			for (int i = 0; i < eventLists.mediaTagWithHighlight.size(); i++) {
				View view = addView();
				FAndroidQuery.id((ImageView)view.findViewById(R.id.imgvw_photo)).image(eventLists.mediaTagWithHighlight.get(i).coverImageThumb, false, true);
				((TextView)view.findViewById(R.id.txtvw_subtitle)).setText(eventLists.mediaTagWithHighlight.get(i).tagName);
				tagListView.addView(view);
			}
			
		}else{
			tagListView.setVisibility(View.GONE);
		}
	}
	
	
	public void populatePlayerListView(){
		if(parseInt(eventLists.eventTotalPlayerCount)>=4){
			playerListView.setVisibility(View.VISIBLE);
			playerListView.removeAllViews();
			 
			for (int i = 0; i <eventLists.mediaWithPlayer.size(); i++) {
				View view = addView();
				FAndroidQuery.id((ImageView)view.findViewById(R.id.imgvw_photo)).image(eventLists.mediaWithPlayer.get(i).avatarPath, false, true);
				((TextView)view.findViewById(R.id.txtvw_subtitle)).setText(eventLists.mediaWithPlayer.get(i).name);
				playerListView.addView(view);
			}
			
		}else{
			playerListView.setVisibility(View.GONE);
		}
	}

	public void populateFanListView(){
		Log.i(TAG, "Total fan count:" + eventLists.eventTotalFanCount);
		Log.i(TAG, "Array fan count: " + eventLists.eventFans.size());
		Log.i(TAG, "eventId: " + eventLists.eventId);
		if(parseInt(eventLists.eventTotalFanCount)>=4){
			fansListView.setVisibility(View.VISIBLE);
			fansListView.removeAllViews();
			
			
			for (int i = 0; i < eventLists.eventFans.size(); i++) {
				View view = addView();
				FAndroidQuery.id((ImageView)view.findViewById(R.id.imgvw_photo)).image(eventLists.eventFans.get(i).avatarUrl, false, true);
				((TextView)view.findViewById(R.id.txtvw_subtitle)).setText(eventLists.eventFans.get(i).name);
				fansListView.addView(view);
			}
			
		}else{
			fansListView.setVisibility(View.GONE);
		}
	}
	public View addTeamView(){
		int count = eventLists.eventTeams.size();
		int w_2 = CommonFunctions_1.getScreenWidth(this)/2;
		int frameW = count==1 || count == 2?w_2 : w_2-(CommonFunctions_1.scaleSizeToDP(this, 20));
			LinearLayout layout  = new LinearLayout(this);
			layout.setLayoutParams(new LayoutParams(frameW, CommonFunctions_1.scaleSizeToDP(this, 44)));
			layout.setOrientation(LinearLayout.HORIZONTAL);
		
			
			ImageView imageView = new ImageView(this);
			TextView textView = new TextView(this);
			
			imageView.setId(R.id.imgvw_photo);
			imageView.setLayoutParams(new LayoutParams(CommonFunctions_1.scaleSizeToDP(this, 30),CommonFunctions_1.scaleSizeToDP(this, 30)));
			imageView.setAdjustViewBounds(true);
			imageView.setScaleType(ScaleType.CENTER_CROP);
			imageView.setImageDrawable(getResources().getDrawable(R.drawable.ic_istoday));
			
			textView.setId(R.id.txtvw_subtitle);
			textView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
			textView.setGravity(Gravity.CENTER_VERTICAL);
			textView.setPadding(3, 3, 3, 3);
			textView.setBackgroundResource(R.drawable.shape_whitetrans_roundcorner);
			textView.setTextColor(getResources().getColor(R.color.uni_grey));
			textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
			
			layout.addView(imageView);
			layout.addView(textView);
		
		return layout;
	}

	public View addView(){
		RelativeLayout layout = new RelativeLayout(this);
		layout.setLayoutParams(new LayoutParams(CommonFunctions_1.getScreenWidth(this)/4, CommonFunctions_1.getScreenWidth(this)/4));
		layout.setPadding(1, 0, 1, 0);
		
		ImageView imageView = new ImageView(this);
		TextView textView = new TextView(this);
		
		imageView.setId(R.id.imgvw_photo);
		imageView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		imageView.setAdjustViewBounds(true);
		imageView.setScaleType(ScaleType.CENTER_CROP);
		
		
		textView.setId(R.id.txtvw_subtitle);
		RelativeLayout.LayoutParams lp=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
		lp.addRule(RelativeLayout.CENTER_IN_PARENT);
		textView.setLayoutParams(lp);
		textView.setGravity(Gravity.CENTER);
		textView.setPadding(3, 3, 3, 3);
		textView.setBackgroundResource(R.drawable.shape_whitetrans_roundcorner);
		textView.setTextColor(getResources().getColor(R.color.white));
		textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 13);
		
		layout.addView(imageView);
		layout.addView(textView);
		
		return layout;
	}
	public class BackgroundAsyncTask extends AsyncTask<String, Uri, Void> {
     
 
        protected void onPreExecute() {
        	imgvw_mediaphoto.setVisibility(View.VISIBLE);
			imgbtn_play.setVisibility(View.GONE);
			pbar_loader.setVisibility(View.VISIBLE);
        }
 
        protected void onProgressUpdate(final Uri... uri) {
 
            try {
 
                MediaController media=new MediaController(EventDetail_Activity.this);
                video_media.setMediaController(media);
              
 
                video_media.setVideoURI(uri[0]);
                video_media.requestFocus();
                video_media.setOnPreparedListener(new OnPreparedListener() {
 
                    public void onPrepared(MediaPlayer arg0) {
                    	video_media.start();
                    	imgvw_mediaphoto.setVisibility(View.INVISIBLE);
						imgbtn_play.setVisibility(View.GONE);
						pbar_loader.setVisibility(View.GONE);
                    }
                });
                 
 
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (SecurityException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
             
 
        }
 
        @Override
        protected Void doInBackground(String... params) {
            try {
                Uri uri = Uri.parse(params[0]);
                 
                publishProgress(uri);
            } catch (Exception e) {
                e.printStackTrace();
 
            }
 
            return null;
        }
 
        
     
         
    }

	public void fetchHighlightReelData() {

		RequestParams requestParams = new RequestParams();
		requestParams.put("eventId", eventLists.eventId);

		async_HttpClient.GET("ExportHighlightReel", requestParams,
				new JsonHttpResponseHandler() {


					@Override
					public void onSuccess(final JSONObject response) {
						// TODO Auto-generated method stub
						super.onSuccess(response);
						Log.e("onSuccess", response.toString());
						parseHighlightReelMedia(response);
						//populateHighlightReelsView();
						
					}

					

					@Override
					public void onFailure(String responseBody, Throwable error){
						// TODO Auto-generated method stub
						super.onFailure(responseBody, error);
						Log.v("onFailure", "onFailure :" + responseBody + " : "
								+ error);

					}
				});
	}
	
	public void fetchListData(final String url){
		RequestParams requestParams = new RequestParams();
		requestParams.put("eventId", eventLists.eventId);

		async_HttpClient.GET(url, requestParams,
				new JsonHttpResponseHandler() {


					@Override
					public void onSuccess(final JSONObject response) {
						// TODO Auto-generated method stub
						super.onSuccess(response);
						Log.e("onSuccess", response.toString());
						
						if(url.equalsIgnoreCase("ExportPlayerList")){
							playerList = parseListData(response);
							populatePlayerListView();
						}else if(url.equalsIgnoreCase("ExportFanList")){
							fanList = parseListData(response);
							populateFanListView();
						}else if(url.equalsIgnoreCase("ExportFavoriteList")){
							favoriteList = parseListData(response);
							populateTagListView();
						}
						
						
					}

					

					@Override
					public void onFailure(String responseBody, Throwable error){
						// TODO Auto-generated method stub
						super.onFailure(responseBody, error);
						Log.v("onFailure", "onFailure :" + responseBody + " : "
								+ error);

					}
				});
	}
	
	
	
	
	public void parseHighlightReelMedia(JSONObject object){
		
		try {
			highlightReelMedia.error = object.get("error").toString();
			highlightReelMedia.message = object.get("message").toString();
			highlightReelMedia.url = object.get("url").toString();
		} catch (JSONException e) {	}
		
	}
	
	public _NewEventLists parseListData(JSONObject object){
		_NewEventLists newEventLists = new _NewEventLists();
		try {
			
			newEventLists.resultCount = object.get("resultCount").toString();
			newEventLists.pageCount = object.get("pageCount").toString();
			newEventLists.currentPage = object.get("currentPage").toString();
		} catch (JSONException e) {	}	
		
		JSONArray array_list = new JSONArray();
		
		try {	
			array_list = object.getJSONArray("list");
		} catch (JSONException e) {	}		
		
		
		
			 for (int i = 0; i < array_list.length(); i++) {
				 JSONObject object_player = new JSONObject();
				 NewList newList = new NewList();
				 try {
					object_player = array_list.getJSONObject(i);
					
					
					newList.id = object_player.get("id").toString();
					newList.name = object_player.get("name").toString();
				
				 } catch (JSONException e) {	}
				JSONObject object_user = new JSONObject();
				
				try {
					object_user = object_player.getJSONObject("user");
					newList.user.userId = ""+object_user.get("userId");
					newList.user.avatarPath = ""+object_user.get("avatarPath");
					newList.user.avatarUrl = ""+object_user.get("avatarUrl");
					newList.user.hasAvatar = ""+object_user.get("hasAvatar");
					newList.user.avatarCount = ""+object_user.get("avatarCount");
					newList.user.avatarName = ""+object_user.get("avatarName");
					newList.user.fullName = ""+object_user.get("fullName");
					newList.user.displayName = ""+object_user.get("displayName");
					newList.user.userName = ""+object_user.get("userName");
					newList.user.email = ""+object_user.get("email");
					newList.user.aboutMe = ""+object_user.get("aboutMe");
					newList.user.postCount = ""+object_user.get("postCount");
					newList.user.favoriteCount = ""+object_user.get("favoriteCount");
					newList.user.viewCount = ""+object_user.get("viewCount");
					
					newList.user.isFollowing = ""+object_user.get("isFollowing");
				
				} catch (JSONException e) {	}
				
				try {
				JSONObject object_followCounts = object_user.getJSONObject("followCounts");
				newList.user.followCounts.follower = ""+object_followCounts.get("follower");
				newList.user.followCounts.following = ""+object_followCounts.get("following");
				} catch (JSONException e) {	}
				
				newEventLists.lists.add(newList);
				
			}
			 
			 return newEventLists;
			
			
		
		
	}
	
	public int parseInt(String s){
		try {
			return Integer.parseInt(s);
			
		} catch (Exception e) {
		return 0;
		}
	}

	 private String getDataSource(String path) throws IOException {
	        if (!URLUtil.isNetworkUrl(path)) {
	            return path;
	        } else {
	            URL url = new URL(path);
	            URLConnection cn = url.openConnection();
	            cn.connect();
	            InputStream stream = cn.getInputStream();
	            if (stream == null)
	                throw new RuntimeException("stream is null");
	            File temp = File.createTempFile("mediaplayertmp", "dat");
	            temp.deleteOnExit();
	            String tempPath = temp.getAbsolutePath();
	            FileOutputStream out = new FileOutputStream(temp);
	            byte buf[] = new byte[128];
	            do {
	                int numread = stream.read(buf);
	                if (numread <= 0)
	                    break;
	                out.write(buf, 0, numread);
	            } while (true);
	            try {
	                stream.close();
	            } catch (IOException ex) {
	               
	            }
	            return tempPath;
	        }
	    }
}
