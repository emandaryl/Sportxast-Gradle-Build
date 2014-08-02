package com.sportxast.SportXast.activities2_0;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sportxast.SportXast.Global_Data;
import com.sportxast.SportXast.Global_Data.Coordinate;
import com.sportxast.SportXast.R;
import com.sportxast.SportXast.SportX2_Main;
import com.sportxast.SportXast.adapter2_0.CreateAdapter;
import com.sportxast.SportXast.commons.CommonFunctions_1;
import com.sportxast.SportXast.commons.Constants;
import com.sportxast.SportXast.commons.GlobalVariablesHolder;
import com.sportxast.SportXast.includes.HeaderUIClass;
import com.sportxast.SportXast.models.EventParcel;
import com.sportxast.SportXast.models._RecentEvent;
import com.sportxast.SportXast.thirdparty_class.Async_HttpClient;
import com.sportxast.SportXast.thirdparty_class.GPSTracker;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;

public class Create_Activity extends Activity {
    //TODO: Create An Event
    //TODO: Display recent event on list
    //TODO: Populate fields using recent event data

    /** Called when the activity is first created. */
    private HeaderUIClass headerUIClass;
    private RelativeLayout sx_header_wrapper;

    private Global_Data FGlobal_Data;
    private Async_HttpClient async_HttpClient;

    private GPSTracker gpsTracker;
    private Coordinate coordinate ;
    //private HeaderListView headerListView;
    //private ActionBar actionBar;

    //private ArrayList<_RecentEvent> list_recentEvent = new ArrayList<_RecentEvent>();
    private CreateAdapter adapter;

    boolean isPause = false;
    private ArrayList<_RecentEvent> FArrRecentEvents;

    /** Coordinates **/
    private Double FCorLatitude;
    private Double FCorLongitude;

    private LinearLayout recentevent_list_cont1;
    private RelativeLayout pbLoading_container;

    @SuppressWarnings("deprecation")
    public String getRotation(Context context){
        final int rotation = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getOrientation();
        switch (rotation) {
            case Surface.ROTATION_0:
                return "portrait";
            case Surface.ROTATION_90:
                return "landscape";
            case Surface.ROTATION_180:
                return "reverse portrait";
            default:
                return "reverse landscape";
        }
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        GlobalVariablesHolder.currentActivityContext = Create_Activity.this;
        FGlobal_Data.runThreadUploader(Create_Activity.this);
        FGlobal_Data.getCurrentLocation();
    }

    private class NextMediaIdData{
        private String eventID      = "";
        private String twitterCardUrl = "";
        private String shareUrl     = "";
        private String mediaId      = "";
        private String shareText    = "";
        private String added        = "";

        /**server path for IMAGE**/
        private String imagePath    = "";
        /**server path for VIDEO**/
        private String videoPath    = "";

        private String localImageFileName = "";
        private String localImageFilePath = "";
        private String localVideoFilePath = "";
        private String localVideoFileName = "";

    }
    private NextMediaIdData FNextMediaIdData;

    private int FWithPendingHighlight;
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ///this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        // TODO Auto-generated method stub
        setContentView(R.layout.layout_create);
        //setContentView(R.layout.layout_menu);

        //Toast.makeText(getApplicationContext(), getRotation(Create_Activity.this), Toast.LENGTH_LONG).show();
        FGlobal_Data = (Global_Data) getApplicationContext();

        try {
            FWithPendingHighlight = getIntent().getExtras().getInt("withPendingHighlight", 0);
        }
        catch (Exception e) {
            FWithPendingHighlight = 0;
        }

        String highlightInitialData = getIntent().getExtras().getString("highlightInitialData");

        this.FNextMediaIdData = parseHighlightInitialData( highlightInitialData );

        async_HttpClient = new Async_HttpClient(this);
        this.FCorLatitude = GlobalVariablesHolder.user_Latitude;
        this.FCorLongitude= GlobalVariablesHolder.user_Longitude;

        initializeResources();
        prepareHeader();
        FArrRecentEvents = getRecentEvents();
        new populateRecentlyCreatedEvents( FArrRecentEvents, recentevent_list_cont1 ).execute();
    }

    private NextMediaIdData parseHighlightInitialData(String highlightInitialData){
        if(highlightInitialData.length() <= 0){
            return null;
        }
        //##########################
        NextMediaIdData nextMediaIdData = new NextMediaIdData();
        String[] arrHighlightInitialData = highlightInitialData.split("\\|\\|");

        nextMediaIdData.eventID            = arrHighlightInitialData[0].toString();
        nextMediaIdData.localVideoFilePath = arrHighlightInitialData[1].toString();
        nextMediaIdData.localImageFilePath = arrHighlightInitialData[2].toString();
        nextMediaIdData.localVideoFileName = arrHighlightInitialData[3].toString();
        nextMediaIdData.localImageFileName = arrHighlightInitialData[4].toString();

        return nextMediaIdData;
    }

    private TextView tv_selectSport;
    private TextView tv_selectVenue;
    private TextView tv_selectTeam1;
    private TextView tv_selectTeam2;

    private void initializeResources(){
        this.pbLoading_container = (RelativeLayout) findViewById(R.id.pbLoading_container);

        tv_selectSport = (TextView) findViewById(R.id.tv_selectSport);
        tv_selectSport.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(Create_Activity.this, ChooseASport_Activity.class);
                //intent.putExtra("username", FProfile.userName);
                startActivityForResult(intent, Constants.requestCode_ChooseASport_Activity);
            }
        });

        tv_selectVenue = (TextView) findViewById(R.id.tv_selectVenue);
        tv_selectVenue.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(Create_Activity.this, ChooseAVenue_Activity.class);
                //intent.putExtra("username", FProfile.userName);
                startActivityForResult(intent, Constants.requestCode_ChooseAVenue_Activity);
            }
        });

        tv_selectTeam1  = (TextView) findViewById(R.id.tv_selectTeam1);
        tv_selectTeam1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(Create_Activity.this, ChooseATeam_Activity.class);
                intent.putExtra("teamNumber", 1);
                startActivityForResult(intent, Constants.requestCode_ChooseATeam_Activity);
            }
        });

        tv_selectTeam2  = (TextView) findViewById(R.id.tv_selectTeam2);
        tv_selectTeam2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(Create_Activity.this, ChooseATeam_Activity.class);
                intent.putExtra("teamNumber", 2);
                startActivityForResult(intent, Constants.requestCode_ChooseATeam_Activity);
            }
        });

        tv_selectTeam2.setVisibility(View.GONE);
        recentevent_list_cont1 = (LinearLayout) findViewById(R.id.recentevent_list_cont1);
    }

    private JSONObject convertStringToJSON( String stringToConvert ){
        JSONObject JSONObject_ = null;
        try {
            JSONObject_ = new JSONObject(stringToConvert);
            //Log.d("My App", obj.toString());

        } catch (Throwable t) {
            //Log.e("My App", "Could not parse malformed JSON: \"" + json + "\"";
            JSONObject_ = new JSONObject();
        }
        return JSONObject_;
    }

    private String FCreatedEventDataString = "";
    private void supplyDataToCreatedEvent(String strDataToAdd, /** 1-sport, 2-venue, 3-team1, 4-team2 **/int eventDataType){

        String jsonStr2 = strDataToAdd.substring( 1, strDataToAdd.length()-1 );

        if(FCreatedEventDataString.length() <= 0)
            FCreatedEventDataString = FCreatedEventDataString + jsonStr2;
        else 	FCreatedEventDataString = FCreatedEventDataString +","+ jsonStr2;

        JSONObject JSONObjectEventData = convertStringToJSON( strDataToAdd );

        switch (eventDataType) {
            case 1:
                String sportName = "";

                try {
                    sportName = JSONObjectEventData.getString("sportName");
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    sportName = "";
                }

                tv_selectSport.setText( sportName );
                break;

            case 2:
                String placeName = "";

                try {
                    placeName = JSONObjectEventData.getString("placeName");
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    placeName = "";
                }

                tv_selectVenue.setText( placeName );
                break;

            case 3:
                String teamName = "";

                try {
                    teamName = JSONObjectEventData.getString("team1");
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    teamName = "";
                }

                if(teamName.length() > 0){
                    tv_selectTeam2.setVisibility(View.VISIBLE);
                }else{

                }
                tv_selectTeam1.setText( teamName );
                break;

            case 4:
                try {
                    teamName = JSONObjectEventData.getString("team2");
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    teamName = "";
                }

                tv_selectTeam2.setText( teamName );

                break;

            default:
                break;
        }

        enableDoneButton();

    }

    private void enableDoneButton(){
        if( (tv_selectSport.getText().toString().length() > 0) && (tv_selectVenue.getText().toString().length() > 0 ) ){
            this.headerUIClass.enableDoneButton(true);
        }else{
            this.headerUIClass.enableDoneButton(false);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            String createdEventDataString = "";
            switch (requestCode) {

                case Constants.requestCode_ChooseASport_Activity:
                    createdEventDataString = data.getStringExtra("JSONSportChosen");
                    supplyDataToCreatedEvent( createdEventDataString, 1 );
                    break;

                case Constants.requestCode_ChooseAVenue_Activity:
                    createdEventDataString = data.getStringExtra("JSONVenueChosen");
                    supplyDataToCreatedEvent( createdEventDataString, 2 );
                    break;

                case Constants.requestCode_ChooseATeam_Activity:
                    createdEventDataString = data.getStringExtra("teamChosen");
                    int teamNumber = data.getIntExtra("teamNumber", 1) + 2;

                    supplyDataToCreatedEvent( createdEventDataString, teamNumber );
                    break;

                default:
                    break;
            }

        }else if (resultCode == RESULT_CANCELED) {
            //Write your code if there's no result
        }
    }

    private void prepareHeader(){
        this.sx_header_wrapper = (RelativeLayout)findViewById(R.id.sx_header_wrapper);
        this.headerUIClass = new HeaderUIClass(this, sx_header_wrapper);
        this.headerUIClass.setMenuTitle("Create");
        this.headerUIClass.showMenuButton(false);
        this.headerUIClass.showBackButton(true);
        this.headerUIClass.showSearchButton(false);
        //this.headerUIClass.hideDoneButton();
        this.headerUIClass.showDoneButton(true);
        this.headerUIClass.enableDoneButton(false);

        addHeaderButtonListener();
    }

    private static String BACKBUTTON_CALLING_CODE = "aaaxxx76347634";
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

                //Toast.makeText(getApplicationContext(), "heyhey", Toast.LENGTH_LONG).show();

                saveCreatedEventToServer();
				/*
				headerUIClass.enableDoneButton(false); 
				
				finish();
				*/
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

                if( FNextMediaIdData != null ){//means there pending captured highlight
                    getNextMediaId( BACKBUTTON_CALLING_CODE, null, FNextMediaIdData.localVideoFilePath, FNextMediaIdData.localImageFilePath, FNextMediaIdData.localVideoFileName, FNextMediaIdData.localImageFileName);
                } else {

                    finish();
                }

                //finish();
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

    private EventParcel FNewlyCreatedEvent;
    public void saveCreatedEventToServer() {

        if(this.pbLoading_container != null)
            this.pbLoading_container.setVisibility(View.VISIBLE);

        JSONObject createdEventDataJSON = null;
        try {
            String createdEventDataString = "{"+FCreatedEventDataString+"}";
            createdEventDataJSON = new JSONObject( createdEventDataString );
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //,"sportName":"Adventure Sports","sportId":"1","sportWhiteLogo":"http:\/\/dev.sportxast.com\/images\/icons\/sport\/white\/athletics.png","sportFirstLetter":"A","sportLogo":"http:\/\/dev.sportxast.com\/images\/icons\/sport\/athletics.png"},"placeAddr":"Cardinal Rosales Avenue, Cebu City","placeName":"Cebu City Marriott Hotel","placeLatitude":"10.318571","placeLongitude":"123.908067","placeId":"dcf321052c90c975e92c2d116e62b6086eeea1df"}
        String team1 = "";
        String team2 = "";
        RequestParams requestParams = new RequestParams();
        try {
			
		/*	{
			"sportId":"30",
			"placeAddr":"F. Sotto Dr, Cebu City",
			"placeId":"2011b08bdef932708aeb3f450e5d6025186485ff",
			"placeLatitude":"10.314126",
			"placeLongitude":"123.901397",
			"placeName":"Richmond Plaza Hotel",
			"sportFirstLetter":"B",
			"sportWhiteLogo":"http:\/\/dev.sportxast.com\/images\/icons\/sport\/white\/ballooning.png", 
			"sportLogo":"http:\/\/dev.sportxast.com\/images\/icons\/sport\/ballooning.png",
			"sportName":"Ballooning", 
			"team1":"St. Ann's  Steamers"
			} */

            String sportId_str = "-69";

            if( createdEventDataJSON.has("sportId") )
                sportId_str = createdEventDataJSON.getString("sportId");

            if( sportId_str.equals("-69") ){//-69 means custom sport
                requestParams.put("sportName", 		createdEventDataJSON.getString("sportName"));
            }else{
                requestParams.put("sportId", 		createdEventDataJSON.getString("sportId"));
            }

            requestParams.put("venueAddr", 		createdEventDataJSON.getString("placeAddr"));

            String placeId_str = "-69";

            if( createdEventDataJSON.has("placeId") )
                placeId_str = createdEventDataJSON.getString("placeId");

            if( placeId_str.equals("-69") ){//-69 means custom venue

            }else{
                requestParams.put("venueId", 		createdEventDataJSON.getString("placeId"));
            }

            requestParams.put("venueLatitude", 	createdEventDataJSON.getString("placeLatitude"));
            requestParams.put("venueLongitude", createdEventDataJSON.getString("placeLongitude"));
            requestParams.put("venueName", 		createdEventDataJSON.getString("placeName"));
            requestParams.put("longitude", ""+ 	FCorLongitude);
            requestParams.put("latitude", "" + 	FCorLatitude);

            if(createdEventDataJSON.has("team1"))
                team1 = createdEventDataJSON.getString("team1");

            if(createdEventDataJSON.has("team2"))
                team2 = createdEventDataJSON.getString("team2");

            JSONArray arrayTags = new JSONArray();
            if ( (team1.length() > 0) || (team2.length() > 0) ) {

                String[] teams = new String[] {};

                if ( (team1.length() > 0) && (team2.length() > 0) ){
                    teams = new String[] { team1, team2 };
                }else if( (team1.length() > 0)  ){
                    teams = new String[] { team1 };
                }else if( (team2.length() > 0)  ){
                    teams = new String[] { team2 };
                }

                arrayTags = new JSONArray(Arrays.asList(teams));
            }

            requestParams.put("tags",  arrayTags.toString());
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        final String team1Final = team1;
        final String team2Final = team2;
        async_HttpClient.POST("SaveEvent", requestParams,
                new JsonHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, JSONObject response) {
                        // TODO Auto-generated method stub
                        super.onSuccess(statusCode, response);

                         /*
                        {
                        "message":"Event was saved",
                        "error":0,
                        "event":{
                                "eventShareMessage":"Watch this Ballooning event from Ayala Center in Cebu captured by SportXast",
                                "eventBroadcastRadius":10,
                                "eventDateTime":"2014-08-01 14:06:32",
                                "eventStartDateShort":"08.01",
                                "eventLongitude":"123.904753",
                                "eventTeams":"teanx",
                                "eventId":"1863",
                                "eventSport":
                                        {"sportName":"Ballooning","sportId":"30","sportTags":[],"sportWhiteLogo":"http:\/\/dev.sportxast.com\/images\/icons\/sport\/white\/ballooning.png","sportFirstLetter":"B","sportLogo":"http:\/\/dev.sportxast.com\/images\/icons\/sport\/ballooning.png","sportBroadcastRadius":"10000"},"eventTotalMediaCount":null,"eventFirstTeam":"teanx","eventInFavorites":0,"eventTimeZone":"Asia\/Manila 2014-08-01 14:06:32","eventDateDiff":0,"eventBroadcastRadiusUnit":"km","eventShareURL":"http:\/\/goo.gl\/PdQMgr","eventIsEnded":0,"eventLatitude":"10.318487","eventLocation":"Ayala Center in Cebu","eventTotalFavoriteCount":"0","eventStartDate":"2014-08-01","eventStartDateFormatted":"Fri, Aug 01","eventTotalCommentCount":"0","eventIsOpenString":"CHECK IN","eventLocalDateTime":"2014-08-01 14:06:32","eventSportId":"30","eventDistanceUnit":"km","eventDistance":0,"eventName":"Ballooning @ Ayala Center in Cebu","eventIsOpen":1,"eventSportName":"Ballooning","eventTags":["teanx"]}}
                        */
                        //String response_ = sampleResponse;

                        headerUIClass.enableDoneButton(true);
                        //Toast.makeText(Create_Activity.this, "successfully saved event.", Toast.LENGTH_SHORT).show();

                        saveNewlyCreatedEventToLocal();
                        //global_Data.setNewEvent(new _RecentEvent());
                        //Create_Activity.this.finish();
                        //pbLoading_container.setVisibility(View.GONE);
                        //############################################################

                        FNewlyCreatedEvent =  CommonFunctions_1.parseToEventParcel(response, team1Final, team2Final);

                        if( FNextMediaIdData != null ){//means there pending captured highlight

                            getNextMediaId("", FNewlyCreatedEvent.eventId, FNextMediaIdData.localVideoFilePath, FNextMediaIdData.localImageFilePath, FNextMediaIdData.localVideoFileName, FNextMediaIdData.localImageFileName);

                        } else {
                            gotoVideoCaptureActivity(FNewlyCreatedEvent);
                            finish();
                        }
                        //########################################################
                        //pbLoading_container.setVisibility(View.GONE);
                    }

                    @Override
                    public void onFailure(int arg0, Header[] arg1, byte[] arg2,
                                          Throwable arg3) {
                        // TODO Auto-generated method stub
                        super.onFailure(arg0, arg1, arg2, arg3);
                        // Log.e("saveevent", "error : "+arg3.getStackTrace());
                        headerUIClass.enableDoneButton(true);
                        //btn_done.setEnabled(true);//sherlock stuff
                        Toast.makeText(Create_Activity.this,
                                "Fail to save event.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /** parameter callingMethod = the calling function,  **/
    private void getNextMediaId( final String callingMethod, final String eventID, final String localVideoFilePath, final String localImageFilePath, final String localVideoFileName, final String localImageFileName ) {
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

        Async_HttpClient async_HttpClient = new Async_HttpClient( Create_Activity.this );
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

                        //captureButton_cont.setVisibility(View.VISIBLE);
                        Log.e("SNAPSHOT", "SET ON UI THREAD");

                        //FNumberOfVideosRecorded = FNumberOfVideosRecorded + 1;
                        if(response != null){

                            try {
                                FNextMediaIdData.twitterCardUrl = response.getString("twitterCardUrl");
                                FNextMediaIdData.shareUrl       = response.getString("shareUrl");
                                FNextMediaIdData.mediaId        = response.getString("mediaId");
                                FNextMediaIdData.shareText      = response.getString("shareText");
                                FNextMediaIdData.added          = response.getString("added");
                                FNextMediaIdData.imagePath      = response.getString("imagePath");
                                FNextMediaIdData.videoPath      = response.getString("videoPath");

                                FNextMediaIdData.localImageFileName = localImageFileName;
                                FNextMediaIdData.localImageFilePath = localImageFilePath;

                                FNextMediaIdData.localVideoFilePath = localVideoFilePath;
                                FNextMediaIdData.localVideoFileName = localVideoFileName;

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            String eventID_ = "";
                            eventID_ = eventID;

                            if(eventID_ == null){
                                eventID_ = Constants.sharePrefKey_uploadedHighlights_self;
                            }

                            try {
                                //FLatestHighlightMediaId =  response.getString("mediaId") ;
                                FGlobal_Data.setVideoPath( response.getString("videoPath") );
                                //########################################################################
                                //String eventId, String coverImage, String largeImageUrl, String videoLocalPath, String imageLocalPath
                                FGlobal_Data.setNewlyUploadedHighlightsData2( response, eventID_, localImageFileName, "", localVideoFilePath, localImageFilePath, localVideoFileName );
                                //########################################################################
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                            GlobalVariablesHolder.pauseBackgroundService = false;
                            FGlobal_Data.runThreadUploader(Create_Activity.this);

                            if( callingMethod.equals(BACKBUTTON_CALLING_CODE) ){
                                gotoSportX2_Main();
                            }else {
                                //########PROCEED TO HIGHLIGHT ACTIVITY
                                gotoHighlight_Activity(FNewlyCreatedEvent);
                            }


                           // btn_add_fav.setTag(0); //btn_add_fav Tag: 1 - already favorited, 0 - not yet favorited
                        } else {
                            //FLatestHighlightMediaId = "";

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
    public void gotoSportX2_Main(){
        // TODO Auto-generated method stub
        Intent intent = new Intent(Create_Activity.this, SportX2_Main.class);
        //intent.putExtra("eventParcel", eventParcel);
        startActivity(intent);
        //runThreadUploader();
        //####################################################################
        // releaseResources();
        finish();
    }

    public void gotoHighlight_Activity( EventParcel eventParcel ){
        // TODO Auto-generated method stub
        Intent intent = new Intent(Create_Activity.this, Highlight_Activity.class);
        intent.putExtra("eventParcel", eventParcel);
        intent.putExtra("numberOfVideosRecorded", 1);
        intent.putExtra("callingActivityID", Constants.requestCode_Create_Activity);
        startActivity(intent);

        //runThreadUploader();
        //####################################################################
       // releaseResources();
        finish();
    }

    /** {
     "message":"Event was saved",
     "error":0,
     "event":{
     "eventShareMessage":"Watch this Band event from Castle Peak Hotel captured by SportXast",
     "eventBroadcastRadius":0.5,
     "eventStartDateShort":"06.17",
     "eventLongitude":"123.913798",
     "eventId":"1561",

     "eventSport":{
     "sportName":"Band",
     "sportId":"54",
     "sportTags":[],
     "sportWhiteLogo":"http:\/\/dev.sportxast.com\/images\/icons\/sport\/white\/band.png",
     "sportFirstLetter":"B","sportLogo":"http:\/\/dev.sportxast.com\/images\/icons\/sport\/band.png",
     "sportBroadcastRadius":"500"
     },

     "eventTotalMediaCount":null,
     "eventInFavorites":0,
     "eventTimeZone":"Asia\/Manila 2014-06-17 11:12:42",
     "eventBroadcastRadiusUnit":"km",
     "eventShareURL":"http:\/\/dev.sportxast.com\/event\/?eventId=1561",
     "eventIsEnded":0,
     "eventLatitude":"10.322644",
     "eventLocation":"Castle Peak Hotel",
     "eventTotalFavoriteCount":"0",
     "eventStartDate":"2014-06-17",
     "eventStartDateFormatted":"Tue, Jun 17",
     "eventTotalCommentCount":"0",
     "eventIsOpenString":"CHECK IN",
     "eventSportId":"54",
     "eventDistanceUnit":"km",
     "eventDistance":0,
     "eventName":"Band @ Castle Peak Hotel",
     "eventIsOpen":1,
     "eventSportName":"Band",
     "eventTags":""
     }
     } **/

    private String sampleResponse ="";


    public void gotoVideoCaptureActivity( EventParcel eventParcel ){
        String hey = "";
        String xxx = hey;

        Intent intent = new Intent( Create_Activity.this, VideoCaptureActivity.class );
        //intent.putExtra("eventId", FEventId);
        intent.putExtra("callingActivityID", Constants.requestCode_Create_Activity);
        intent.putExtra("eventParcel", eventParcel);
        startActivity(intent);
    }

    /**@category Override onPause - from Activity class
     * **/
    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        isPause = true;
    }

    private class populateRecentlyCreatedEvents extends AsyncTask<String, Object, String> {
        private ArrayList<_RecentEvent> arrRecentEvents;
        private LinearLayout recentevent_list_cont1;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        public populateRecentlyCreatedEvents(ArrayList<_RecentEvent> arrRecentEvents, LinearLayout recentevent_list_cont1) {
            this.arrRecentEvents = arrRecentEvents;
            this.recentevent_list_cont1 = recentevent_list_cont1;
        }

        @Override
        protected String doInBackground(String... params) {

            // LinearLayout panelContSub1 = null;
            LayoutInflater infalInflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            for (int i = 0; i < this.arrRecentEvents.size(); i++) {
                _RecentEvent recentEvent = this.arrRecentEvents.get(i);
                LinearLayout lyt1 = (LinearLayout) infalInflater.inflate( R.layout.list_item_event_add, null);
                lyt1.setTag(i);
                publishProgress(new Object[]{i, recentEvent, lyt1});
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Object... tagName) {
            super.onProgressUpdate(tagName);

            final int i = Integer.parseInt( tagName[0].toString() );
            final _RecentEvent recentEvent = (_RecentEvent) tagName[1];

            final String JSONInString = createJSONObject( recentEvent );
            LinearLayout lyt1 = (LinearLayout) tagName[2];

            lyt1.setTag( recentEvent );

            final int listIndexToAccess = i;
            //##########################################################################################################
            //##########################################################################################################
            ImageView imgvw_sportlogo	= (ImageView)lyt1.findViewById(	R.id.imgvw_sportlogo);
            TextView txtvw_sportname	= (TextView) lyt1.findViewById(	R.id.txtvw_sportname);
            String firstTeam =  recentEvent.eventFirstTeam;
            txtvw_sportname.setText( recentEvent.eventFirstTeam );

            TextView txtvw_sportsubname	= (TextView) lyt1.findViewById(	R.id.txtvw_sportsubname);
            txtvw_sportsubname.setText( recentEvent.sportName );

            Drawable drawable = null;
            int color = getResources().getColor(R.color.uni_orange);
            Mode mMode = Mode.SRC_ATOP;

            try {
                String logo = recentEvent.sportLogo;
                String packageName = logo.substring(logo.lastIndexOf("sport/"), logo.indexOf(".png")).replace("sport/", "");
                packageName = packageName.replace(" ", "_");
                //Log.e("sportLogo", ":"+eventLists.eventSport.sportLogo);
                //Log.e("packageName", ""+packageName);

                int id = getApplicationContext().getResources().getIdentifier(packageName, "drawable", getApplicationContext().getPackageName());

                drawable = getApplicationContext().getResources().getDrawable(id);
            } catch (Exception e) {
                // TODO: handle exception
                drawable = getApplicationContext().getResources().getDrawable(R.drawable.ic_istoday);
            }

            drawable.setColorFilter(color,mMode);
            imgvw_sportlogo.setImageDrawable(drawable);

            //##########################################################################################################
            lyt1.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub

                    supplyViewsWithExistingData( (_RecentEvent)v.getTag()  );
                    //Toast.makeText(getApplicationContext(), JSONInString, Toast.LENGTH_LONG).show();
                }
            });
            //##########################################################################################################
            this.recentevent_list_cont1.addView(lyt1, new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
        }
    }

    private void supplyViewsWithExistingData( _RecentEvent recentEvent ){

        JSONObject JSONEventCreated = new JSONObject();
        try {
            JSONEventCreated.put("sportId", 		recentEvent.sportId);
            JSONEventCreated.put("sportName", 		recentEvent.sportName);
            JSONEventCreated.put("sportFirstLetter",recentEvent.sportFirstLetter);
            JSONEventCreated.put("sportLogo",		recentEvent.sportLogo);
            JSONEventCreated.put("sportWhiteLogo",	recentEvent.sportLogo);
            JSONEventCreated.put("placeId", 		recentEvent.venueId);
            JSONEventCreated.put("placeName", 		recentEvent.venueName);
            JSONEventCreated.put("placeAddr",		recentEvent.venueAddress);
            JSONEventCreated.put("placeLatitude",	recentEvent.venueLatitude);
            JSONEventCreated.put("placeLongitude",	recentEvent.venueLongitude);
            JSONEventCreated.put("team1", 			recentEvent.eventFirstTeam);
            JSONEventCreated.put("team2",			recentEvent.eventSecondTeam);
        }catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        this.FCreatedEventDataString = JSONEventCreated.toString().substring(1, JSONEventCreated.toString().length()-1);

        //##############################################
        tv_selectSport.setText( recentEvent.sportName );
        tv_selectVenue.setText( recentEvent.venueName );

        if(recentEvent.eventFirstTeam.length() > 0)
            tv_selectTeam1.setText( recentEvent.eventFirstTeam );

        if(recentEvent.eventSecondTeam.length() > 0){
            tv_selectTeam2.setText( recentEvent.eventSecondTeam );
            tv_selectTeam2.setVisibility( View.VISIBLE );
        }

    }

    /** returns a String in JSON format **/
    private String createJSONObject( _RecentEvent recentEvent ){

        JSONObject JSONSportChosen = new JSONObject();
        try {
            JSONSportChosen.put("sportId", 			recentEvent.sportId);
            JSONSportChosen.put("placeAddr",		recentEvent.venueAddress);
            JSONSportChosen.put("placeId",			recentEvent.venueId);
            JSONSportChosen.put("placeLatitude", 	recentEvent.venueLatitude);
            JSONSportChosen.put("placeLongitude", 	recentEvent.venueLongitude);
            JSONSportChosen.put("placeName", 		recentEvent.venueName);
            JSONSportChosen.put("sportFirstLetter", recentEvent.sportFirstLetter);
            JSONSportChosen.put("sportWhiteLogo",	recentEvent.sportLogo);
            JSONSportChosen.put("sportLogo",		recentEvent.sportLogo);
            JSONSportChosen.put("sportName",		recentEvent.sportName);
            if(recentEvent.eventFirstTeam.length() > 0)
                JSONSportChosen.put("team1",			recentEvent.eventFirstTeam);
            if(recentEvent.eventSecondTeam.length() > 0)
                JSONSportChosen.put("team2",			recentEvent.eventSecondTeam);

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return JSONSportChosen.toString();
		  
	   /*
		{
			"sportId":"30",
			"placeAddr":"F. Sotto Dr, Cebu City",
			"placeId":"2011b08bdef932708aeb3f450e5d6025186485ff",
			"placeLatitude":"10.314126",
			"placeLongitude":"123.901397",
			"placeName":"Richmond Plaza Hotel",
			"sportFirstLetter":"B",
			"sportWhiteLogo":"http:\/\/dev.sportxast.com\/images\/icons\/sport\/white\/ballooning.png", 
			"sportLogo":"http:\/\/dev.sportxast.com\/images\/icons\/sport\/ballooning.png",
			"sportName":"Ballooning", 
			"team1":"St. Ann's  Steamers"
			} 
		*/
    }

    private SharedPreferences FSharedpreferences;
    private String FRecentEventsKey = "jmF6PuaaXbyK69";

    /** retrieve locally saved Recent Created Events **/
    public ArrayList<_RecentEvent> getRecentEvents(){
        ArrayList<_RecentEvent> arrRecentEvents = new ArrayList<_RecentEvent>();
        FSharedpreferences = getSharedPreferences("com.sportxast.SportXast.recentEventsLocal", Context.MODE_PRIVATE);
        if ( FSharedpreferences.contains( FRecentEventsKey ) ){

            String highlightDataInString = FSharedpreferences.getString(FRecentEventsKey, "");
            String[] arrHighlightsData	= highlightDataInString.split("\\|\\|");

            //int counter = 0;
            int loopLimit = 0;

            if(arrHighlightsData.length > 5)
                loopLimit = arrHighlightsData.length - 5;

            for (int i = (arrHighlightsData.length - 1); i >= loopLimit; i--) {
                // for (int i = 0; i < arrHighlightsData.length; i++) {
                try {
                    JSONObject jsonObject = new JSONObject( arrHighlightsData[i].toString() );
                    _RecentEvent recentEvent = new _RecentEvent();
				   /*
				   {
			 		"sportId":"30", 
				   	"sportName":"Ballooning",
				   	"sportLogo":"http:\/\/dev.sportxast.com\/images\/icons\/sport\/ballooning.png",
				   	"sportFirstLetter":"B", 
				   	"placeId":"2011b08bdef932708aeb3f450e5d6025186485ff",
				   	"placeName":"Richmond Plaza Hotel",
				   	"placeAddr":"F. Sotto Dr, Cebu City",
				   	"placeLatitude":"10.314126",
				   	"placeLongitude":"123.901397", 
				   	"sportWhiteLogo":"http:\/\/dev.sportxast.com\/images\/icons\/sport\/white\/ballooning.png", 
				   	"team1":"Steamboat Springs  Sailors",
				   	"team2":"Fulton  Steamers"
				   	}  	*/

                    recentEvent.sportId 			= jsonObject.getString("sportId");
                    recentEvent.sportName 		= jsonObject.getString("sportName");
                    recentEvent.sportLogo 		= jsonObject.getString("sportLogo");
                    recentEvent.sportFirstLetter = jsonObject.getString("sportFirstLetter");
                    recentEvent.venueId 			= jsonObject.getString("placeId");
                    recentEvent.venueName 		= jsonObject.getString("placeName");
                    recentEvent.venueAddress 	= jsonObject.getString("placeAddr");
                    recentEvent.venueLatitude	= jsonObject.getString("placeLatitude");
                    recentEvent.venueLongitude 	= jsonObject.getString("placeLongitude");

                    String firstTeam = "";
                    if(jsonObject.has("team1"))
                        firstTeam = jsonObject.getString("team1");

                    if(firstTeam.length() > 0){
                        recentEvent.eventFirstTeam = jsonObject.getString("team1");
                        recentEvent.team.add( firstTeam );
                    }

                    String secondTeam = "";
                    if(jsonObject.has("team2"))
                        secondTeam = jsonObject.getString("team2");

                    if(secondTeam.length() > 0){
                        recentEvent.eventSecondTeam = jsonObject.getString("team2");
                        recentEvent.team.add( secondTeam );
                    }
                    //########################################
                    arrRecentEvents.add( recentEvent );

                } catch (Throwable t) {
                    //  Log.e("My App", "Could not parse malformed JSON: \"" + json + "\"";

                }
            }
        }
        return arrRecentEvents;

    }

    /** save newly created event to local **/
    public void saveNewlyCreatedEventToLocal(){

        JSONObject createdEventDataJSON = null;
        try {
            String createdEventDataString = "{"+FCreatedEventDataString+"}";
            createdEventDataJSON = new JSONObject( createdEventDataString );
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        try {
//#############################################################################################
            this.FSharedpreferences = getSharedPreferences( "com.sportxast.SportXast.recentEventsLocal", this.MODE_PRIVATE );

            String newJSONString = "";
            if (FSharedpreferences.contains(FRecentEventsKey))
            {
                newJSONString = FSharedpreferences.getString(FRecentEventsKey, "");
            }
            newJSONString = newJSONString +"||"+ createdEventDataJSON.toString();

            //##################################################################################
            if( newJSONString.substring(0, 2).equals("||") ){
                newJSONString = newJSONString.substring(2);
            }

            Editor editor = FSharedpreferences.edit();
            editor.putString(FRecentEventsKey, newJSONString);
            editor.commit();

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        //######################################
	   /* 
	    "team1":"Steamboat Springs  Sailors",
		"team2":"Fulton  Steamers"} 
	    */
        String team1 = "";
        String team2 = "";
        try {
            if(createdEventDataJSON.has("team1"))
                team1 = createdEventDataJSON.getString("team1");

            if(createdEventDataJSON.has("team2"))
                team2 = createdEventDataJSON.getString("team2");

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        //String teams = "";
        if( ( team1.length() > 0 ) && ( team2.length() > 0 ) ){
            saveNewlyCreatedTeamToLocal( new String[]{team1, team2} );
        }else if( ( team1.length() > 0 ) ){
            saveNewlyCreatedTeamToLocal( new String[]{ team1 } );
        }else if( ( team2.length() > 0 ) ){
            saveNewlyCreatedTeamToLocal( new String[]{ team2 } );
        }
    }

    private String FRecentTeamsKey = "knG7QubcZbyK39";
    /** save newly created TEAM(s) to local **/
    public void saveNewlyCreatedTeamToLocal( String[] teamNames ){
        try {
//#############################################################################################
            this.FSharedpreferences = getSharedPreferences( "com.sportxast.SportXast.recentTeamsLocal", Context.MODE_PRIVATE );

            String newJSONString = "";
            if (FSharedpreferences.contains(FRecentTeamsKey))
            {
                newJSONString = FSharedpreferences.getString(FRecentTeamsKey, "");
            }
            //newJSONString = newJSONString +"||"+ createdEventDataJSON.toString();

            String namesToAddStr = "";
            for (int i = 0; i < teamNames.length; i++) {

                if(i == 0){
                    namesToAddStr = namesToAddStr +  teamNames[i].toString();
                }else
                    namesToAddStr = namesToAddStr + "||" + teamNames[i].toString();
            }
            newJSONString = newJSONString +"||"+ namesToAddStr;

            //##################################################################################
            if( newJSONString.substring(0, 2).equals("||") ){
                newJSONString = newJSONString.substring(2);
            }

            Editor editor = FSharedpreferences.edit();
            editor.putString(FRecentTeamsKey, newJSONString);
            editor.commit();

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


}
