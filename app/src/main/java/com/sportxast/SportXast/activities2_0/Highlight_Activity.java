package com.sportxast.SportXast.activities2_0;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.Session;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sportxast.SportXast.Global_Data;
import com.sportxast.SportXast.R;
import com.sportxast.SportXast.adapter2_0.EventMediaAdapter;
import com.sportxast.SportXast.commons.CommonFunctions_1;
import com.sportxast.SportXast.commons.Constants;
import com.sportxast.SportXast.commons.GlobalVariablesHolder;
import com.sportxast.SportXast.fragments.FacebookFragment;
import com.sportxast.SportXast.includes.HeaderUIClass;
import com.sportxast.SportXast.models.EditTextBackEvent;
import com.sportxast.SportXast.models.EventParcel;
import com.sportxast.SportXast.models.NextMediaID;
import com.sportxast.SportXast.models._MediaLists.Comments;
import com.sportxast.SportXast.models._MediaLists.MediaList;
import com.sportxast.SportXast.models._MediaLists.Tag;
import com.sportxast.SportXast.models._MediaLists.User;
import com.sportxast.SportXast.models._MediaLists.UserInFavorites;
import com.sportxast.SportXast.tasks.TaskDownloadAndPostToFb;
import com.sportxast.SportXast.thirdparty_class.Async_HttpClient;
import com.sportxast.SportXast.thirdparty_class.PullToRefreshListView.OnRefreshListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


//public class Highlight_Activity extends Activity {

public class Highlight_Activity extends FragmentActivity{

    // TODO: Display available Media of the Event
    private HeaderUIClass headerUIClass;
    private RelativeLayout sx_header_wrapper;

    private Global_Data FGlobal_Data;
    //GPSTracker gpsTracker;

    private ListView FPullToRefreshListView;

    //private ExtendedListView FPullToRefreshListView;

    //private PullToRefreshListView FPullToRefreshListView;

    private Async_HttpClient async_HttpClient;
    private EventMediaAdapter FAdapter;

    private String FEventId		= "";
    private String hashtag 		= "";
    private String FEventFirstTeam 	= "";
    private String FEventSecondTeam = "";
    private String FEventDate	= "";
    private String FEventIsToday= "0";

    private String FSinceId = "";
    private String FUntilId = "";

    boolean isLoad 	  = false;
    boolean isRefresh = false;

    private ArrayList<MediaList> FArrMediaList;
    private ArrayList<MediaList> FArrMediaListOriginalCopy;

    private SharedPreferences sharedpreferences;

    private View headerView;
    private View footerView;
    private View RefresherHeader;

    private LinearLayout header_pb_cont1;
    private LinearLayout footer_pb_cont1;
    private LinearLayout header_pb_eventInfo;
    private EditTextBackEvent edittext_comment;

    private void initializeHighlightActivitySharedPreferences(){
        sharedpreferences = getSharedPreferences("com.sportxast.SportXast.highlights", Context.MODE_PRIVATE);
    }

    /** possible Values: "LIVE" or "CHECK IN" **/
    private String FEventStatus;

    private boolean FCanCaptureHighlight;
    private EventParcel FChosenEvent;
    private String FEventName;

    private int numberOfRemainingLocalHighlights;
    private RelativeLayout pbLoading_container;

    private int FCallingActivityID;

    private int FNumberOfVideosRecorded = 0;

    private static final String TAG = "HighlightActivity";
    private static final List<String> PERMISSIONS = Arrays.asList("publish_actions");
    private static final String PENDING_PUBLISH_KEY = "pendingPublishReauthorization";
    private boolean pendingPublishReauthorization = false;


    public void krungkrung(String newHighlightData){
        //Toast.makeText(getApplicationContext(), "KRUNG KRUNG KRUNG KRUNG\n" + newHighlightData, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        GlobalVariablesHolder.currentActivityContext = Highlight_Activity.this;
        GlobalVariablesHolder.pauseBackgroundService = false;
        FGlobal_Data.runThreadUploader(Highlight_Activity.this);
        FGlobal_Data.getCurrentLocation();

    }

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_highlight);
        initializeHighlightActivitySharedPreferences();

        FGlobal_Data = (Global_Data) getApplicationContext();
        FChosenEvent = getIntent().getExtras().getParcelable("eventParcel");

        FEventId 		= FChosenEvent.eventId;
        FEventFirstTeam = FChosenEvent.eventFirstTeam;
        FEventSecondTeam= FChosenEvent.eventSecondTeam;
        FEventIsToday	= FChosenEvent.eventIsOpen;
        FEventStatus	= FChosenEvent.eventIsOpenString;
        FEventName 	    = FChosenEvent.eventName;

        FCallingActivityID = getIntent().getExtras().getInt("callingActivityID", -1);
        FNumberOfVideosRecorded = getIntent().getIntExtra("numberOfVideosRecorded", 0);

        if(FEventStatus.equals("CHECK IN")){
            FCanCaptureHighlight = true;
        }else	FCanCaptureHighlight = false;

        prepareHeader(FEventFirstTeam + "\n" + FEventSecondTeam, FEventName);
        numberOfRemainingLocalHighlights = 0;

        initializeResources();

        initializeCustomListView();
        gatherEventList();
    }

//	private void hideSoftKeyboard( EditText editTextWithKeyboard ){
//		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//		imm.hideSoftInputFromWindow(editTextWithKeyboard.getWindowToken(), 0);
//	}

    public void initializeResources() {

        //mediaLists.aq = new AQuery(this);
        //final View activityRootView = findViewById(R.id.activityRoot);
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

                    if( edittext_comment != null ){
                        edittext_comment.setFocusableInTouchMode(true);
                        edittext_comment.setCursorVisible(true);
                        edittext_comment.requestFocus();
                    }
                }else{
                    //hidden = true;
                    //showCommentSectionPanel(false);
                    //Toast.makeText(getApplicationContext(), "wagtang keybARDS height: "+heightDiff, Toast.LENGTH_LONG).show();
                }
            }
        });

        pbLoading_container = (RelativeLayout) findViewById(R.id.pbLoading_container);
        async_HttpClient = new Async_HttpClient(this);

        headerView  = getLayoutInflater().inflate(R.layout.list_header, null);
        footerView   = getLayoutInflater().inflate(R.layout.list_footer, null);

        TextView txtvw_headertitle = (TextView) headerView.findViewById(R.id.txtvw_header_title);
        txtvw_headertitle.setText("Capture a Highlight");
        txtvw_headertitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                stopTimer();
                gotoVideoCaptureActivity();
            }
        });

        header_pb_eventInfo = (LinearLayout) headerView.findViewById(R.id.header_pb_eventInfo);

        ImageView imgvw_events_sportsicon 	= (ImageView) header_pb_eventInfo.findViewById(R.id.imgvw_events_sportsicon);
        //imgvw_events_sportsicon.setImageDrawable( CommonFunctions_1.getDrawableIcon(Highlight_Activity.this, FChosenEvent.sportLogo) );
        TextView txtvw_sportname 	 		= (TextView) header_pb_eventInfo.findViewById(R.id.txtvw_sportname);
        txtvw_sportname.setText(FChosenEvent.eventSportName);

        TextView txtvw_events_title0 		= (TextView) header_pb_eventInfo.findViewById(R.id.txtvw_events_title0);

        txtvw_events_title0.setText(FChosenEvent.eventFirstTeam);
        TextView txtvw_events_title  		= (TextView) header_pb_eventInfo.findViewById(R.id.txtvw_events_title);
        txtvw_events_title.setText(FChosenEvent.eventSecondTeam);

        if( (FEventFirstTeam.trim() + FEventSecondTeam.trim()).length() <= 0 ){
            txtvw_events_title0.setVisibility(View.GONE);
            txtvw_events_title.setText(FEventName);
        }

        TextView txtvw_events_date   		= (TextView) header_pb_eventInfo.findViewById(R.id.txtvw_events_date);
        txtvw_events_date.setText(FChosenEvent.eventStartDateFormatted);
        TextView txtvw_events_location  	= (TextView) header_pb_eventInfo.findViewById(R.id.txtvw_events_location);
        txtvw_events_location.setText(FChosenEvent.eventLocation);

        if(FCanCaptureHighlight)
            header_pb_eventInfo.setVisibility(View.VISIBLE);
        else header_pb_eventInfo.setVisibility(View.GONE);

        header_pb_cont1 = (LinearLayout) headerView.findViewById(R.id.header_pb_cont1);
        footer_pb_cont1 = (LinearLayout) footerView.findViewById(R.id.footer_pb_cont1);
        footer_pb_cont1.setVisibility(View.GONE);


    }

    private void gotoVideoCaptureActivity(){
        Intent intent = new Intent(Highlight_Activity.this, VideoCaptureActivity.class);

        intent.putExtra("eventParcel", FChosenEvent );
        //intent.putExtra("eventId", FEventId);
        intent.putExtra("callingActivityID", Constants.requestCode_Highlight_Activity);
        //startActivityForResult(intent, Constants.requestCode_Highlight_Activity);
        startActivity(intent);
        finish();
    }

    private void initializeCustomListView( ) {
        FPullToRefreshListView = (ListView) findViewById(R.id.ptrListViewHighlight);

        if ( FCanCaptureHighlight )
            FPullToRefreshListView.addHeaderView(headerView);

        FPullToRefreshListView.addFooterView(footerView);

        if(FArrMediaList == null){
            FArrMediaList = new ArrayList<MediaList>();
            MediaList mediaList 	= new MediaList();
            mediaList.mediaId 		= "";
            mediaList.eventId 		= "";
            mediaList.sportId		= "";
            mediaList.mediaUserId 	= "";
            mediaList.mediaUserName = "";
            mediaList.mediaType 	= "";
            mediaList.coverImage 	= "";
            mediaList.coverImageThumb = "";
            mediaList.largeImageUrl = "";
            mediaList.mediumImageUrl= "";
            mediaList.smallImageUrl = "";
            mediaList.mediaUrl 		= "";
            mediaList.mediaShortUrl = "";
            mediaList.m3u8Url 		= "";
            mediaList.vp8Url 		= "";
            mediaList.mp4Url 		= "";
            mediaList.videoLocalPath= "";
            mediaList.imageLocalPath= "";
            mediaList.mediaShareString = "";
            mediaList.onDate 		= "";
            mediaList.age 			= "";
            mediaList.viewCount 	= "";
            mediaList.favoritesCount= "";
            mediaList.commentsCount = "";
            mediaList.shareCount 	= "";
            mediaList.score 		= "";
            mediaList.currentUserIsOwner = "";
            mediaList.currentUserHasInFavorites = "";
            mediaList.transcoderJobStatus = "";
            mediaList.twitterCardUrl = "";

            mediaList.user				= new User();
            mediaList.comments 			= new ArrayList<Comments>();
            mediaList.userInFavorites 	= new ArrayList<UserInFavorites>();
            mediaList.tags 				= new ArrayList<Tag>();

            FArrMediaList.add(mediaList);
        }

        FAdapter = new EventMediaAdapter(Highlight_Activity.this, FArrMediaList);
        FPullToRefreshListView.setAdapter(FAdapter);

        FPullToRefreshListView.setOnScrollListener(onScrollListener);
        //FPullToRefreshListView.setOnItemClickListener(onitemListener);
        //FPullToRefreshListView.setOnRefreshListener(onRefreshListener);

    }


    /**@category Listener
     *  A refreshListener for listView.  * ***/
    OnRefreshListener onRefreshListener = new OnRefreshListener() {

        @Override
        public void onRefresh() {
			/*
			// TODO Auto-generated method stub
			if (FArrMediaList.size() > 0) {
				isRefresh = true;
				FUntilId = "";
				FSinceId = eventLists.eventLists.get(0).eventId;
				gatherEventList();
			} 
			*/
        }
    };

    /**
     * @category Listener scrollListener for listView. Includes filter for reload
     *           more media.
     * **/
    private int firstVisibleItemIndex;



    OnScrollListener onScrollListener = new OnScrollListener() {

        private int mLastFirstVisibleItem;
        private boolean mIsScrollingUp;

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            // TODO Auto-generated method stub

            Rect scrollBounds = new Rect();
            view.getHitRect(scrollBounds);
            if (headerView.getLocalVisibleRect(scrollBounds)) {
                // Any portion of the imageView, even a single pixel, is within the visible window
            } else {
                // NONE of the imageView is within the visible window
                //Toast.makeText(getApplicationContext(), "heyXXX", Toast.LENGTH_LONG).show();
            }

            //Toast.makeText(getApplicationContext(), "heyXXX", Toast.LENGTH_LONG).show();
			/*
		    final ListView lw = FPullToRefreshListView;

		    if (view.getId() == lw.getId()) { //just making if the same listview
		    	
		    	 
		        final int currentFirstVisibleItem = lw.getFirstVisiblePosition();

		        if (currentFirstVisibleItem > mLastFirstVisibleItem) {
		        	Toast.makeText(getApplicationContext(), "down: " + scrollState, Toast.LENGTH_LONG).show();
		            mIsScrollingUp = false;
		        } else if (currentFirstVisibleItem < mLastFirstVisibleItem) {
		        	Toast.makeText(getApplicationContext(), "up: " + scrollState, Toast.LENGTH_LONG).show();
		            mIsScrollingUp = true;
		        }

		        mLastFirstVisibleItem = currentFirstVisibleItem; 
		    }  
			*/
            //Toast.makeText(getApplicationContext(), "scrollState: " + scrollState, Toast.LENGTH_LONG).show();
        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem,
                             int visibleItemCount, int totalItemCount) {
            // TODO Auto-generated method stub
            //Toast.makeText(getApplicationContext(), "heyXXX", Toast.LENGTH_LONG).show();

			/*
			Rect scrollBounds = new Rect();
			view.getHitRect(scrollBounds);
			if(!headerView.getLocalVisibleRect(scrollBounds)
		    || scrollBounds.height() < headerView.getHeight()) { 
				
		        // imageView is not within or only partially within the visible window
				
				//Toast.makeText(getApplicationContext(), "PARTIALLY VISIBLE BABY", Toast.LENGTH_LONG).show();
				
		    } else {
		    	// NONE of the imageView is within the visible window
		    	
		    	Toast.makeText(getApplicationContext(), "TOTALLY, TOTALLY VISIBLE BABY", Toast.LENGTH_LONG).show();
		    }
			*/


            firstVisibleItemIndex = firstVisibleItem;


            if( footer_pb_cont1.getVisibility() == View.VISIBLE ){
                return;
            }

            final int lastItem = firstVisibleItem + visibleItemCount;

            if (FArrMediaList.size() > 0) {
                if ( lastItem == (totalItemCount - 1)  ) {

                    String sinceId = "";
                    //String untilId = mediaLists.mediaLists.get(mediaLists.mediaLists.size() - 1).mediaId;
                    String untilId = FArrMediaList.get(FArrMediaList.size() - 1).mediaId;

                    //Toast.makeText(getApplicationContext(), "heyXXX", Toast.LENGTH_LONG).show();
                    //footer_pb_cont1.setVisibility(View.VISIBLE);

                    /** Show the footer progress Loader  **/
                    footer_pb_cont1.setVisibility(View.VISIBLE);
                    gatherAdditionalHighlights(sinceId, untilId);
                }
            }

        }
    };

    private boolean alreadyscrolled = false;
	/*
	private class sampleTasks extends AsyncTask<String, Integer, Void> {		
		 
	    private int dataIsSaved;
		private String sReturn;
	    
		@Override
		protected void onPreExecute() {
			super.onPreExecute();   
		}
		
		@Override
		protected Void doInBackground(String... params) { 
	    //###############################################
	    	  try {
    			sharedpreferences = getSharedPreferences("com.sportxast.SportXast.highlights", Highlight_Activity.this.MODE_PRIVATE);
				String highlightDataInString = sharedpreferences.getString(FEventId, "");  
				String[] arrHighlightsData = highlightDataInString.split("\\|\\|");  
	    		 
				if( sharePrefLenghtHasChanged(highlightDataInString) ){
					//Show the progress Loader
					//header_pb_cont1.setVisibility(View.VISIBLE);
					
					expandCurrentData( prepareNewlyUploadedHighlightsData(highlightDataInString), highlightDataInString );  
					previousHighlightDataInString = highlightDataInString; 
					//stopTimer();
				//	Toast.makeText(getApplicationContext(), "NIGGA BE CHANGIN", Toast.LENGTH_LONG).show(); 
				}else{
				 //	Toast.makeText(getApplicationContext(), "NO CHANGES", Toast.LENGTH_LONG).show(); 
				}
				//Thread.sleep(1000);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
	} 
		
	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);
		Toast.makeText(getApplicationContext(), "YO BRO WHAT'S UP MAN???", Toast.LENGTH_LONG).show();
		
		FPullToRefreshListView.onRefreshComplete();
		//FPullToRefreshListView.removeFooterView(footervw);
		}
	}
	*/

    private void prepareHeader(final String menuTitle, final String menuTitle0 ) {

        this.sx_header_wrapper 	= (RelativeLayout) findViewById(R.id.sx_header_wrapper);
        this.headerUIClass 		= new HeaderUIClass(this, sx_header_wrapper);
        //this.headerUIClass.setMenuTitle(menuTitle);
        this.headerUIClass.setMenuTitle(  	"Highlights");

        this.headerUIClass.showBackButton(true);
        this.headerUIClass.showAddButton(false);
        this.headerUIClass.showMenuButton(false);
        this.headerUIClass.showRefreshButton(false);
        this.headerUIClass.showAboutButton(false);
        this.headerUIClass.showSearchButton(false);
        this.headerUIClass.showDoneButton( !FCanCaptureHighlight );

        this.headerUIClass.showCameraButton( FCanCaptureHighlight );
        this.headerUIClass.showMenuTitle(menuTitle.trim().length() <= 0 ? false: true);
        //this.headerUIClass.setMenuTitle0(menuTitle0);
        //this.headerUIClass.showMenuTitle0(menuTitle0.length() <= 0 ? false: true);
        this.headerUIClass.showMenuTitle0(false);
        addHeaderButtonListener();
    }

    private void addHeaderButtonListener() {
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

            @Override
            public void onDoneClicked() {
                // TODO Auto-generated method stub
            }

            @Override
            public void onCameraClicked() {
                // TODO Auto-generated method stub

                //Toast.makeText(getApplicationContext(), "camera ni brad", Toast.LENGTH_LONG).show();
                gotoVideoCaptureActivity();
            }

            @Override
            public void onBackClicked() {
                // TODO Auto-generated method stub

                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }

            @Override
            public void onAddClicked() {
                // TODO Auto-generated method stub
            }

            @Override
            public void onAboutClicked() {
                // TODO Auto-generated method stub
            }

            @Override
            public void onFollowClicked() {
                // TODO Auto-generated method stub

            }
        });
    }

	/*
	 * @category Override onBack- from BaseSherlockActivityclass custom method
	 *           from BaseSherlockActivity.Invoked when back button (found on
	 *           left side of the action bar) is clicked/pressed.
	 * 
	 */
	
	/*
	 * @Override public void onBack(View v) { // TODO Auto-generated method stub
	 * super.onBack(v); finish(); }
	 */

    /**
     * @category Custom Method Create a custom contentView
     * @return View - listView
     * */

	/*
	public View contentViewXXXXX() {

		if (FPullToRefreshListView != null)
			return FPullToRefreshListView;

		FPullToRefreshListView = new PullToRefreshListView(this);
		FPullToRefreshListView.setCacheColorHint(Color.parseColor("#00000000"));
		return FPullToRefreshListView;
	} 
	*/


    /**
     * @category Custom Method Custom function for initializing content view and
     *           declared objects
     */



    /**
     * @category Custom Method Fetch list of media of the event from server.
     **/
    private int fetchDataSuccessfull = 0;

    //int currentPage = 0;
    //int arraySize = 0;

    private boolean noMoreHighlightsToDisplay = false;

    private void gatherAdditionalHighlights(final String sinceId, final String untilId){

        if(noMoreHighlightsToDisplay){
            /** HIDE the footer progress Loader  **/
            footer_pb_cont1.setVisibility(View.GONE);
            return;
        }

        RequestParams requestParams = new RequestParams();
        requestParams.put("eventId", FEventId);
        requestParams.put("sinceId", sinceId);
        requestParams.put("untilId", untilId);

        requestParams.put("pageSize", "5");
        Log.e("highlight", requestParams.toString());

        async_HttpClient.GET(hashtag.length() > 0 ? "ExportMedia" : "ExportEventMedia", requestParams,
                new JsonHttpResponseHandler() {

                    @Override
                    public void onStart() {
                        // TODO Auto-generated method stub
                        super.onStart();
                        // Log.v("onStart", "onStart");
                    }

                    @Override
                    public void onSuccess(final JSONObject response) {
                        // TODO Auto-generated method stub
                        super.onSuccess(response);
                        Log.e("onSuccess", response.toString());

                        if( checkJSONStringIfCorrect(response.toString()) )
                            appendAdditionalHighlights(  CommonFunctions_1.parseMediaList(response) );
                        else{

                            noMoreHighlightsToDisplay = true;

                            //	Toast.makeText(getApplicationContext(), "OVER THE LINE DUDE!!!", Toast.LENGTH_LONG).show();
                        }

                        // Toast.makeText(getApplicationContext(), "WOOOPS GOT IT DUDE!", Toast.LENGTH_LONG).show();
                        //Toast.makeText(getApplicationContext(), "WOOOPS GOT IT DUDE!", Toast.LENGTH_LONG).show();
                        /** HIDE the footer progress Loader  **/
                        footer_pb_cont1.setVisibility(View.GONE);
                        //	 footerView.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFinish() {
                        // TODO Auto-generated method stub
                        super.onFinish();
                        // Log.v("onFinish", "onFinish");
                    }

                    @Override
                    public void onFailure(String responseBody,
                                          Throwable error) {
                        // TODO Auto-generated method stub
                        super.onFailure(responseBody, error);
                        Log.v("onFailure", "onFailure :" + responseBody + " : " + error);

                    }
                });
    }

    private boolean checkJSONStringIfCorrect(String JSONResponse){


        boolean hasNoProblem = true;
        JSONObject JSONObject_ = null;
        try {
            JSONObject_ = new JSONObject( JSONResponse );


            if( JSONObject_.has("message") )
                hasNoProblem =  false;
            else  hasNoProblem =  true;


        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();

            hasNoProblem = false;

        }

        return hasNoProblem;

    }

    private void appendAdditionalHighlights(  ArrayList<MediaList> arrAdditionalMediaList ){

        for (int i = 0; i < arrAdditionalMediaList.size(); i++) {

            FArrMediaList.add( arrAdditionalMediaList.get(i) );

        }

        reloadListView( FArrMediaList );
    }

    private int gatherLocalHighlightData(final String eventId){

        int numberOfSavedLocalHighlights = 0;

        sharedpreferences = getSharedPreferences("com.sportxast.SportXast.highlights", Highlight_Activity.this.MODE_PRIVATE);
        String highlightDataInString = "";

        highlightDataInString 		= sharedpreferences.getString(eventId, "");
        //Toast.makeText(getApplicationContext(), "NASUDLAN UG: " + arrHighlightsData.length, Toast.LENGTH_LONG).show();

        ArrayList<NextMediaID> arrNextMediaID = prepareNewlyUploadedHighlightsData(highlightDataInString);

        numberOfSavedLocalHighlights = arrNextMediaID.size();
        numberOfRemainingLocalHighlights = expandCurrentData( arrNextMediaID );
        previousHighlightDataInString 	  = highlightDataInString;

        return numberOfSavedLocalHighlights;
    }

    private void gatherEventList() {

        if(pbLoading_container != null)
            pbLoading_container.setVisibility(View.VISIBLE);

        RequestParams requestParams = new RequestParams();
		
		/*
		if (hashtag.length() > 0) {
			requestParams.put("hashTag", hashtag);
			requestParams.put("page", "" + (currentPage + 1));
		} else {
			requestParams.put("eventId", FEventId);
			requestParams.put("sinceId", sinceId);
			requestParams.put("untilId", untilId);
		}
		*/

        requestParams.put("eventId", FEventId);
        requestParams.put("sinceId", FSinceId);
        requestParams.put("untilId", FUntilId);
        requestParams.put("pageSize", "5");
        Log.e("highlight", requestParams.toString());

        //async_HttpClient.GET(hashtag.length() > 0 ? "ExportMedia" : "ExportEventMedia", requestParams,
        async_HttpClient.GET( "ExportEventMedia", requestParams,
                new JsonHttpResponseHandler() {

                    @Override
                    public void onStart() {
                        // TODO Auto-generated method stub
                        super.onStart();
                        // Log.v("onStart", "onStart");
                    }

                    @Override
                    public void onSuccess(final JSONObject response) {
                        // TODO Auto-generated method stub
                        super.onSuccess(response);
                        Log.e("onSuccess", response.toString());

                        FArrMediaList = CommonFunctions_1.parseMediaList(response);
                        FArrMediaListOriginalCopy = CommonFunctions_1.parseMediaList(response);

                        isLoad 		= false;
                        isRefresh 	= false;

                        fetchDataSuccessfull = 1;

                        int numberOfSavedLocalHighlights = gatherLocalHighlightData( FEventId );

                        //Toast.makeText(getApplicationContext(), "SAVED LOCAL HIGHLIGHTS: " + numberOfSavedLocalHighlights, Toast.LENGTH_LONG).show();
                        //reloadListView( FArrMediaList );

                        pbLoading_container.setVisibility(View.GONE);

                        //#####################################################
						/*
						if(FCanCaptureHighlight){
							FPullToRefreshListView.addHeaderView(headerView); 
						 
						}
						*/
                        //###############################################################################
                        startTimer5( FNumberOfVideosRecorded, numberOfSavedLocalHighlights );
                    }

                    @Override
                    public void onFinish() {
                        // TODO Auto-generated method stub
                        super.onFinish();
                        // Log.v("onFinish", "onFinish");
                    }

                    @Override
                    public void onFailure(String responseBody,
                                          Throwable error) {
                        // TODO Auto-generated method stub
                        super.onFailure(responseBody, error);
                        Log.v("onFailure", "onFailure :" + responseBody
                                + " : " + error);
                        isLoad = false;
                        isRefresh = false;

                        //FPullToRefreshListView.onRefreshComplete();
						
						/*
						if (arraySize == mediaLists.mediaLists.size()) {
							FPullToRefreshListView.removeFooterView(footervw);
						} 
						*/
                    }
                });
    }


    private String previousHighlightDataInString = "";

    private boolean sharePrefLenghtHasChanged(String highlightDataInString){
        if( highlightDataInString.length() == previousHighlightDataInString.length() )
            return false; //didn't change
        else return true;
    }

    /*
    private String getSharedPrefHighlightData(){ 
    	sharedpreferences = getSharedPreferences("com.sportxast.SportXast.highlights", Highlight_Activity.this.MODE_PRIVATE);
		String highlightDataInString = sharedpreferences.getString(FEventId, "");   
    	return highlightDataInString;
    }
    */
    public void startTimer5(final int numberOfVideosRecorded, final int numberOfSavedLocalHighlights){

    }

    public void startxTimer51(final int numberOfVideosRecorded){

        if( numberOfVideosRecorded > 0 ){
            /** Show the progress Loader **/
            header_pb_cont1.setVisibility(View.VISIBLE);
            // FPullToRefreshListView.setOnRefreshListener(null);
            //fetchLatestData();
            freshUploadShownVideoCount = 0;
            //startTimer5();

        }else{
            stopTimer();
            return;
        }

        //final long timerWaitingTime = 700;
        // #######################################
        FRefresherTimer = new Timer();
        FRefresherTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        sharedpreferences = getSharedPreferences("com.sportxast.SportXast.highlights", Highlight_Activity.this.MODE_PRIVATE);
                        String highlightDataInString = sharedpreferences.getString(FEventId, "");
                        String[] arrHighlightsData = highlightDataInString.split("\\|\\|");

                        if( sharePrefLenghtHasChanged(highlightDataInString) ){
                            /** Show the progress Loader **/
                            //header_pb_cont1.setVisibility(View.VISIBLE);
                            expandCurrentData( prepareNewlyUploadedHighlightsData(highlightDataInString)  );
                            previousHighlightDataInString = highlightDataInString;
                            //stopTimer();
                            //Toast.makeText(getApplicationContext(), "NIGGA BE CHANGIN", Toast.LENGTH_LONG).show();
                        }else{
                            //Toast.makeText(getApplicationContext(), "NO CHANGES", Toast.LENGTH_LONG).show();
                        }

                        if( FArrMediaList.size() > FArrMediaListOriginalCopy.size() ){

                            int hey = FArrMediaList.size() - FArrMediaListOriginalCopy.size();


                            if(hey >= (numberOfRemainingLocalHighlights + numberOfVideosRecorded) ){

                                /** HIDE the header progress Loader **/
                                header_pb_cont1.setVisibility(View.GONE);
                                stopTimer();
                                //Toast.makeText(getApplicationContext(), "COMPLETO NA BRAD", Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
            }
        }, 1000, 1000); //<== 700 interval, very important, do not change
        // ########################################
    }
	/**
	 * @category Custom Method Called after fetching new data and reload listView
	 *           adapter to view updated data.*
	 */

    public void reloadListView( ArrayList<MediaList> arrMediaLists ) {
        /*
        DO NOT REMOVE!!!!
        - LAW!!!
         */
        if(FAdapter != null) {
            FAdapter.updateListElements( arrMediaLists );
        }

        FPullToRefreshListView.invalidateViews();
        FPullToRefreshListView.setCacheColorHint(Color.TRANSPARENT);

        //FPullToRefreshListView.scrollTo( 0, sx_header_wrapper.getHeight() );

        //FAdapter.notifyDataSetChanged();
        //################################################################
    }
	
	/*
	private void gatherEventListORIGINAL() {
		currentPage = Integer.parseInt(mediaLists.currentPage);
		arraySize = mediaLists.mediaLists.size();

		if (mediaLists.isFirstLoad || isLoad || isRefresh) {
			mediaLists.isFirstLoad = false;
			RequestParams requestParams = new RequestParams();
			if (hashtag.length() > 0) {
				requestParams.put("hashTag", hashtag);
				requestParams.put("page", "" + (currentPage + 1));
			} else {
				requestParams.put("eventId", FEventId);
				requestParams.put("sinceId", sinceId);
				requestParams.put("untilId", untilId);
			}

			requestParams.put("pageSize", "5"); 
			Log.e("highlight", requestParams.toString());

			async_HttpClient.GET(hashtag.length() > 0 ? "ExportMedia" : "ExportEventMedia", requestParams,
					new JsonHttpResponseHandler() {

						@Override
						public void onStart() {
							// TODO Auto-generated method stub
							super.onStart();
							// Log.v("onStart", "onStart"); 
						}

						@Override
						public void onSuccess(final JSONObject response) {
							// TODO Auto-generated method stub
							super.onSuccess(response);
							Log.e("onSuccess", response.toString());
							mediaLists.isRefresh = isRefresh;
							mediaLists.parseMediaList(response);
							reloadListView();
							isLoad 		= false;
							isRefresh 	= false;
							 
							fetchDataSuccessfull = 1;
						 
							if (arraySize == mediaLists.mediaLists.size()) {
								FPullToRefreshListView.removeFooterView(footervw);
							}
							
							gatherLocalHighlightData();
							// startTimer();
						}

						@Override
						public void onFinish() {
							// TODO Auto-generated method stub
							super.onFinish();
							// Log.v("onFinish", "onFinish"); 
						}

						@Override
						public void onFailure(String responseBody,
								Throwable error) {
							// TODO Auto-generated method stub
							super.onFailure(responseBody, error);
							Log.v("onFailure", "onFailure :" + responseBody
									+ " : " + error);
							isLoad = false;
							isRefresh = false;
							
							//FPullToRefreshListView.onRefreshComplete();
							
							if (arraySize == mediaLists.mediaLists.size()) {
								FPullToRefreshListView.removeFooterView(footervw);
							} 
						}
					});
		}

	}
	
	*/

    private int beforeLength = 0;
    /**
     * @category Listener refreshListener for listView. Callback create from
     *           PulltoRefresh custom class.
     * **/

    private int heyhey = 0;
    private boolean windowIsFocused = true;
    private boolean windowFirstTimeVisible = true;
    //private boolean windowShown = false;



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
		
		/*
		 * Please do not REMOVE!!!!!!
		 */
        if(requestCode == Session.DEFAULT_AUTHORIZE_ACTIVITY_CODE) {
//			Log.i(TAG, "requestCode " + requestCode);
//			Log.i(TAG, "resultCode " + resultCode);
            FacebookFragment fragment = (FacebookFragment) getSupportFragmentManager().findFragmentByTag(Constants.TAG_FACEBOOK);
            if(fragment != null) {
				/*
				 * Explicit call Fragment's onActivityResult because
				 * host Activity will override this method.
				 */
                fragment.onActivityResult(requestCode, resultCode, data);
            }
        }

        windowFirstTimeVisible = true;

        //if(resultCode == RESULT_OK){
        if(resultCode == 69){ //<- temporary 69
            switch (requestCode) {
                case Constants.requestCode_Highlight_Activity:

                    FNumberOfVideosRecorded = data.getIntExtra("numberOfVideosRecorded", 0);
                    //Toast.makeText(	getApplicationContext(), "data RESULT: "+ FNumberOfVideosRecorded, Toast.LENGTH_LONG).show();
                    // FPullToRefreshListView.
                    if( FNumberOfVideosRecorded > 0 ){

                        /** Show the progress Loader **/
                        header_pb_cont1.setVisibility(View.VISIBLE);

                        // FPullToRefreshListView.setOnRefreshListener(null);
                        //fetchLatestData();
                        freshUploadShownVideoCount = 0;


                        startTimer5( FNumberOfVideosRecorded, 0 );


                        //	handler.post(updateTextRunnable);

                    }
                    break;

                default:
                    break;
            }

        }else if (resultCode == RESULT_CANCELED) {
            //Write your code if there's no result
        }

    }

    private int remainingTime;


    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub

        //HIDE soft keyboard
        if(edittext_comment != null){
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(edittext_comment.getWindowToken(), 0);
        }

        stopTimer();
        super.onDestroy();

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        // TODO Auto-generated method stub
        super.onWindowFocusChanged(hasFocus);

        if(hasFocus){
        }else{
            stopTimer();
        }
    }


    private ArrayList<NextMediaID> prepareNewlyUploadedHighlightsData(String dataCombinedString){

        ArrayList<NextMediaID> arrNextMediaID = new ArrayList<NextMediaID>();
        String[] arrHighlightsData = dataCombinedString.split("\\|\\|");

        for (int i = 0; i < arrHighlightsData.length; i++) {
            try {
                JSONObject obj = new JSONObject( arrHighlightsData[i].toString() );

                NextMediaID nextMediaID = CommonFunctions_1.parseNextMediaIDList(obj);
                arrNextMediaID.add(nextMediaID);
                //  Log.d("My App", obj.toString());
            } catch (Throwable t) {
                //  Log.e("My App", "Could not parse malformed JSON: \"" + json + "\"";
            }
        }
        return arrNextMediaID;
    }

    private void removeExistingSharePrefElement(final String eventId, final String mediaID, String dataCombinedString){

        String[] arrHighlightsData = dataCombinedString.split("\\|\\|");

        String newJSONString = "";
        for (int i = 0; i < arrHighlightsData.length; i++) {
            try {
                JSONObject jsonObject = new JSONObject( arrHighlightsData[i].toString() );

                String mediaID_ = jsonObject.getString("mediaId");

                if(mediaID_.equals(mediaID))
                    continue;
				    /*
					jsonObject.getString("shareUrl") ;
					jsonObject.getString("mediaId")  ;
					jsonObject.getString("shareText");
					jsonObject.getString("added") 	 ;
					jsonObject.getString("imagePath");
					jsonObject.getString("videoPath");
				//####################################### 
					jsonObject.getString("eventId");
					jsonObject.getString("coverImage");
					jsonObject.getString("largeImageUrl");
					jsonObject.getString("videoLocalPath");
					jsonObject.getString("imageLocalPath"); 
					*/
                newJSONString = newJSONString +"||"+ jsonObject.toString();

            } catch (Throwable t) {
                //  Log.e("My App", "Could not parse malformed JSON: \"" + json + "\"";
            }

        }
        if( newJSONString.length() > 0 ){
            if( newJSONString.substring(0, 2).equals("||") ){
                newJSONString = newJSONString.substring(2);
            }
        }

        if(newJSONString.length() <= 0){
            //Toast.makeText(getApplicationContext(),"wala nay sulod sharedPref: "+eventId , Toast.LENGTH_LONG).show();
            SharedPreferences settings = Highlight_Activity.this.getSharedPreferences( "com.sportxast.SportXast.highlights", this.MODE_PRIVATE );
            settings.edit().remove(eventId).commit();
            return;
        }
        sharedpreferences = getSharedPreferences("com.sportxast.SportXast.highlights", Highlight_Activity.this.MODE_PRIVATE);

        Editor editor = sharedpreferences.edit();
        editor.putString(eventId, newJSONString);
        editor.commit();
    }

    /**
     * Expand current array, adding newly uploaded data taken from local storage
     **/

    private int freshUploadShownVideoCount = 0;


    private int expandCurrentData( ArrayList<NextMediaID> arrNextMediaID  ) {
        int beforeSize = FArrMediaList.size();
        boolean highlightHasBeenAdded = false;

        int totalNumberOfHighlightsAdded = 0;
        for (int i = 0; i < arrNextMediaID.size(); i++) {

            MediaList mediaList = new MediaList();
            String eventId = arrNextMediaID.get(i).getEventId();
            String mediaID = arrNextMediaID.get(i).getMediaId();
            //if( mediaIdAlreadyExists(FArrMediaList, arrNextMediaID.get(i).getMediaId()) ){
            if( mediaIdAlreadyExists(FArrMediaListOriginalCopy, arrNextMediaID.get(i).getMediaId()) ){
                //Toast.makeText(getApplicationContext(),"EXISTING ALREADY --\n DATA DELETED"+ arrNextMediaID.get(i).getVideoLocalPath(), Toast.LENGTH_LONG).show();

                //removeExistingSharePrefElement(eventId, mediaID, dataCombinedString);

                continue;
            }else{

            }

            mediaList.mediaId 			= arrNextMediaID.get(i).getMediaId();
            mediaList.eventId 			= arrNextMediaID.get(i).getEventId();
            mediaList.sportId 			= "";
            mediaList.mediaUserId 		= "";
            mediaList.mediaUserName 	= "";
            mediaList.mediaType 		= "";
            mediaList.coverImage 		= "";
            mediaList.coverImageThumb 	= "";
            mediaList.largeImageUrl 	= arrNextMediaID.get(i).getLargeImageUrl();
            mediaList.mediumImageUrl	= "";
            mediaList.smallImageUrl 	= "";
            mediaList.mediaUrl 			= "";
            mediaList.mediaShortUrl 	= "";
            mediaList.m3u8Url 			= "";
            mediaList.vp8Url 			= "";
            mediaList.mp4Url 			= arrNextMediaID.get(i).getVideoServerPath();
            mediaList.videoLocalPath	= arrNextMediaID.get(i).getVideoLocalPath();
            mediaList.imageLocalPath	= arrNextMediaID.get(i).getImageLocalPath();
            mediaList.mediaShareString 	= "";
            mediaList.onDate 			= "";
            mediaList.age 				= "";
            mediaList.viewCount 		= "0";
            mediaList.favoritesCount	= "";
            mediaList.commentsCount 	= "";
            mediaList.shareCount 		= "0";
            mediaList.score 			= "0";
            mediaList.currentUserIsOwner= "";
            mediaList.currentUserHasInFavorites = "";
            mediaList.transcoderJobStatus= "";
            mediaList.twitterCardUrl = arrNextMediaID.get(i).getTwitterCardUrl();

            FArrMediaList.add(0, mediaList);

            highlightHasBeenAdded = true;

            // Toast.makeText(getApplicationContext(), "PASOK", Toast.LENGTH_LONG).show();
            freshUploadShownVideoCount = freshUploadShownVideoCount + 1;


            totalNumberOfHighlightsAdded = totalNumberOfHighlightsAdded + 1;
        }


        if( highlightHasBeenAdded == false ){
            //Toast.makeText(getApplicationContext(), addedD, Toast.LENGTH_LONG).show();
            //Toast.makeText(getApplicationContext(),"wala nay sulod sharedPref: "+eventId , Toast.LENGTH_LONG).show();
            SharedPreferences settings = Highlight_Activity.this.getSharedPreferences( "com.sportxast.SportXast.highlights", this.MODE_PRIVATE );
            settings.edit().remove( FEventId ).commit();

            totalNumberOfHighlightsAdded = 0;
        }

        reloadListView( FArrMediaList );

        if( freshUploadShownVideoCount >= FNumberOfVideosRecorded ){

            freshUploadShownVideoCount = 0;
            /** HIDE the progress Loader **/
            header_pb_cont1.setVisibility(View.GONE);
        }
        return totalNumberOfHighlightsAdded;
    }

    private boolean mediaIdAlreadyExists( ArrayList<MediaList> arrMediaList, final String mediaId){
        boolean existingId = false;
        for (int i = 0; i < arrMediaList.size(); i++) {
            String mediaId_ = arrMediaList.get(i).mediaId;

            if( mediaId.equals( mediaId_ ) ){
                existingId = true;
                break;
            }
        }
        return existingId;
    }


    /*
    private void destroyCurrentAdapter(){
        FAdapter.notifyDataSetInvalidated();
        FAdapter = null;
    }
    */
    private Timer FRefresherTimer;

    @Override
    public void onStop() {
        super.onStop();

        /*
        Important!
        Do not
        Remove!
        -Law
         */
        FAdapter = null;
        stopTimer();
    }

    public void stopTimer() {
        if (FRefresherTimer == null)
            return;
        // FRecordTimeLapse = 0;
        FRefresherTimer.cancel();
        FRefresherTimer.purge();
        FRefresherTimer = null;
        // FRecorder.release();
        // FTvRecTimer.setText("00:00");
    }
	
	/*
	private class NewDataTracker implements Runnable {
		private boolean threadStopper;
		private Object FMediaPlayer_a;
		private int stringLength;

		public void setThreadStopper(boolean threadStopper) {
			this.threadStopper = threadStopper;
			this.stringLength = FGlobal_Data.getVideoPath().length();
		} 
		@Override
		public void run() {
			while (this.threadStopper == false) {

				if (FGlobal_Data.getVideoPath().length() > 0) {
					this.threadStopper = true;
					// Toast.makeText(getApplicationContext(), "GOT IT BABY!",
					// Toast.LENGTH_LONG).show();
					continue;
				}
			} 
		} 
	}
	*/

    public void openUpFacebookFragment(String message) {
        FragmentManager fm = getSupportFragmentManager();
        FacebookFragment fbFragment = FacebookFragment.newInstance(message);
        fbFragment.show(fm, Constants.TAG_FACEBOOK);
    }

    public void uploadVideoToFb(File videoFile, String urlPath, String caption, String mediaId) {
        // Get Facebook's active session.
        Session session = Session.getActiveSession();
		
		/*
		 * Check published permissions first.
		 */
        List<String> permissionList = session.getPermissions();
        if(!isSubsetOf(PERMISSIONS, permissionList)) {
            pendingPublishReauthorization = true;
			/*
			 * Set additional permission requests to be able
			 * to publish on the Facebook feed.
			 * Inside PERMISSIONS is just "publish_actions".
			 */
            Session.NewPermissionsRequest newPermissionRequest = new Session.NewPermissionsRequest(this, PERMISSIONS);
            session.requestNewPublishPermissions(newPermissionRequest);
            return;
        }

        new TaskDownloadAndPostToFb(this, videoFile, urlPath, caption, mediaId).execute();

    }

    private boolean isSubsetOf(Collection<String> subset, Collection<String> superset) {
        for(String string : subset) {
            if(!superset.contains(string)) {
                return false;
            }
        }

        return true;
    }
}