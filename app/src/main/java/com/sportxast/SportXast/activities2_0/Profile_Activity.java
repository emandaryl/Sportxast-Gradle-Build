package com.sportxast.SportXast.activities2_0;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings.Secure;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sportxast.SportXast.Global_Data;
import com.sportxast.SportXast.R;
import com.sportxast.SportXast.adapter2_0.EventMediaAdapter;
import com.sportxast.SportXast.adapter2_0.ProfileDataAdapter;
import com.sportxast.SportXast.commons.CommonFunctions_1;
import com.sportxast.SportXast.commons.Constants;
import com.sportxast.SportXast.commons.GlobalVariablesHolder;
import com.sportxast.SportXast.includes.HeaderUIClass;
import com.sportxast.SportXast.models.NextMediaID;
import com.sportxast.SportXast.models._MediaLists.Comments;
import com.sportxast.SportXast.models._MediaLists.MediaList;
import com.sportxast.SportXast.models._MediaLists.Tag;
import com.sportxast.SportXast.models._MediaLists.User;
import com.sportxast.SportXast.models._MediaLists.UserInFavorites;
import com.sportxast.SportXast.models._Profile;
import com.sportxast.SportXast.models._Profile.followCounts;
import com.sportxast.SportXast.tasks.StreamImageTask;
import com.sportxast.SportXast.thirdparty_class.Async_HttpClient;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

@SuppressWarnings("deprecation")
public class Profile_Activity extends Activity {

    /** Header composition **/
    private HeaderUIClass headerUIClass;
    private RelativeLayout sx_header_wrapper;
    //private Global_Data global_Data;
    private String FUserId = "";
    private String FUserDisplayname = "";

    boolean inProfileTab = true;

    public _Profile FCurrentProfile = new _Profile();

    public TabHost tabHost;
    private ImageView imgvw_avatar_profile;
    private TextView txtvw_highlightCount;
    private TextView txtvw_followerCount;
    public TextView txtvw_followingCount;

    boolean isPause = false;
    boolean FUserIsProfileOwner = false;

    private static Profile_Activity theInstance;

    /*
    public static Profile_Activity getInstance()
    {
        return Profile_Activity.theInstance;
    }
    */

    public Profile_Activity() {
        Profile_Activity.theInstance = this;
    }

    private int FCallingActivityID;

    private RelativeLayout pbLoading_container;
    private RelativeLayout pbLoading_container2;

    /** Coordinates **/
    private Double FCorLatitude;
    private Double FCorLongitude;

    @Override
    protected void onResume() {
        super.onResume();
        GlobalVariablesHolder.currentActivityContext = Profile_Activity.this;
        FGlobal_Data.runThreadUploader(Profile_Activity.this);
        FGlobal_Data.getCurrentLocation();
    }

    private Global_Data FGlobal_Data;
    /** Called when the activity is first created. */
    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_profile_atan);
        FGlobal_Data = (Global_Data)getApplicationContext();
        //FEventId 	        = CommonFunctions_1.getIntentExtrasString(this, "eventId");
        FUserId 			= CommonFunctions_1.getIntentExtrasString(this, "userId");
        FUserDisplayname 	= CommonFunctions_1.getIntentExtrasString(this,	"userDisplayname");
        //FUserDisplayname  = sharedPref.getString(KEY_PROFILE.USER_NAME, "");

        FCallingActivityID 	= getIntent().getExtras().getInt("callingActivityID", -1);
        if( (this.FCallingActivityID == Constants.requestCode_Menu_Activity) ||
            (this.FCallingActivityID == Constants.requestCode_VideoCapture_Activity) ){

            this.FUserIsProfileOwner = true;
            this.FUserId = GlobalVariablesHolder.X_USER_ID;
            this.FUserDisplayname = GlobalVariablesHolder.X_USER_ID;

            //Toast.makeText(getApplicationContext(), "IMUHA NI BRAD", Toast.LENGTH_LONG).show();
        }else{
            this.FUserIsProfileOwner = false;
        }

        this.FCorLatitude	= GlobalVariablesHolder.user_Latitude;
        this.FCorLongitude	= GlobalVariablesHolder.user_Longitude;

        initializeResources();
        /** SHOW progress bar **/
        pbLoading_container.setVisibility(View.VISIBLE);

        prepareHeader();
        FStillProcessing = true;
        gatherProfileData();
        if(FCallingActivityID == Constants.requestCode_Menu_Activity){
            //Toast.makeText(getApplicationContext(), "IMUHA NI BRAD", Toast.LENGTH_LONG).show();
        }else{
        }
       // this.btn_tab_highlights.performClick();
    }

    private Button btn_tab_highlights;
    private Button btn_tab_followers;
    private Button btn_tab_following;

    private void initializeResources(){
        pbLoading_container  = (RelativeLayout) findViewById(R.id.pbLoading_container);
        pbLoading_container2 = (RelativeLayout) findViewById(R.id.pbLoading_container2);

        imgvw_avatar_profile = (ImageView) findViewById(R.id.imgvw_avatar_profile);
        txtvw_highlightCount = (TextView)findViewById(R.id.txtvw_highlightscount);
        txtvw_followerCount	 = (TextView)findViewById(R.id.txtvw_followercount);
        txtvw_followingCount = (TextView)findViewById(R.id.txtvw_followingcount);

        this.btn_tab_highlights = (Button) findViewById(R.id.btn_tab_highlights);
        this.btn_tab_highlights.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                initializeCustomListView_EVENTS();
                gatherEventList();
                updateButtonBackground( 0 );
    }
});

        this.btn_tab_followers 	= (Button) findViewById(R.id.btn_tab_followers);
        this.btn_tab_followers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                initializeCustomListView_FOLLOWERS();
                gatherFollowersList();

                updateButtonBackground( 1 );
            }
        });
        this.btn_tab_following	= (Button) findViewById(R.id.btn_tab_following);
        this.btn_tab_following.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                initializeCustomListView_FOLLOWING();
                gatherFollowingList();
                updateButtonBackground( 2 );
            }
        });
    }

    @SuppressLint("ResourceAsColor")
    private void updateButtonBackground(int buttonIndex){

        this.btn_tab_highlights.setBackgroundResource(R.drawable.btn_pnl_white_left1);
        this.btn_tab_followers.setBackgroundResource(R.drawable.btn_pnl_white_mid1);
        this.btn_tab_following.setBackgroundResource(R.drawable.btn_pnl_white_right1);

        this.btn_tab_highlights.setTextColor(R.color.uni_blue);
        this.btn_tab_followers.setTextColor(R.color.uni_blue);
        this.btn_tab_following.setTextColor(R.color.uni_blue);

        switch (buttonIndex) {
            case 0:
                this.btn_tab_highlights.setBackgroundResource(R.drawable.btn_pnl_blue_left1);
                this.btn_tab_highlights.setTextColor(Color.WHITE);
                break;
            case 1:
                this.btn_tab_followers.setBackgroundResource(R.drawable.btn_pnl_blue_mid1);
                this.btn_tab_followers.setTextColor(Color.WHITE);
                break;
            case 2:
                this.btn_tab_following.setBackgroundResource(R.drawable.btn_pnl_blue_right1);
                this.btn_tab_following.setTextColor(Color.WHITE);
                break;
            default:
                break;
        }
    }

    private void prepareHeader(){
        this.sx_header_wrapper = (RelativeLayout)findViewById(R.id.sx_header_wrapper);
        this.headerUIClass = new HeaderUIClass(this, sx_header_wrapper);
        this.headerUIClass.setMenuTitle( FUserDisplayname.length() > 0? FUserDisplayname:FUserId.length()>0?FUserId:"Profile");

        this.headerUIClass.showBackButton(	 true);
        this.headerUIClass.showAddButton(	 false);
        this.headerUIClass.showMenuButton(	 false);
        this.headerUIClass.showRefreshButton(false);
        this.headerUIClass.showAboutButton(	 false);
        this.headerUIClass.showSearchButton( false);
        //this.headerUIClass.showDoneButton(   CommonFunctions_1.sharedPreferences.getString(KEY_PROFILE.USER_ID, "").equals(FUserId) || FUserIsProfileOwner?true:false);
        this.headerUIClass.showDoneButton( 	 true);

        this.headerUIClass.showCameraButton( false);
        this.headerUIClass.showMenuTitle(	 true);

        this.headerUIClass.showMenuTitle0(	 false);
        this.headerUIClass.setDoneButtonText("Edit");

        this.headerUIClass.setFollowButtonText("Follow");
        this.headerUIClass.showFollowButton( !this.FUserIsProfileOwner );

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

            @Override
            public void onDoneClicked() {
                // TODO Auto-generated method stub

                Intent intent = new Intent(Profile_Activity.this, ProfileEdit_Activity.class);
                intent.putExtra("username", FCurrentProfile.userName);
                intent.putExtra("fullname", FCurrentProfile.fullName);
                intent.putExtra("photo", 	FCurrentProfile.avatarUrl);
                intent.putExtra("email", 	FCurrentProfile.email);
                intent.putExtra("motto", 	FCurrentProfile.aboutMe);
                intent.putExtra("avatarCount", Integer.parseInt(FCurrentProfile.avatarCount));

                startActivityForResult(intent, Constants.requestCode_Profile_Activity);
                //startActivity(intent);
            }

            @Override
            public void onCameraClicked() {
                // TODO Auto-generated method stub

            }

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

            @Override
            public void onFollowClicked() {
                // TODO Auto-generated method stub
                followUser(FCurrentProfile);
            }
        });
    }
    public void rePopulateListView(){
        if(FStillProcessing){
            Toast.makeText(getApplicationContext(), "WALA PA HUMAN BRAD!!!!!!", Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(getApplicationContext(), "HOOOOOYYY!!!!!!", Toast.LENGTH_LONG).show();
            //gatherEventList();
        }
    }

    private ArrayList<_Profile> FArrFollowersList;
    private void gatherFollowersList(){

        showProgressCover(View.VISIBLE, View.VISIBLE);

        Async_HttpClient async_HttpClient = new Async_HttpClient(Profile_Activity.this);

        String appendUrl = "";
        RequestParams requestParams = new RequestParams();

        appendUrl = "ExportFollowerList";
        requestParams.put("page", "1");
        requestParams.put("pageSize", "10");
        requestParams.put("userId", this.FUserId);

        //https://test.sportxast.com/phone/apiV2/ExportFollowerList?page=1&pageSize=10&userId=200820
        async_HttpClient.GET(appendUrl, requestParams,
                new JsonHttpResponseHandler() {

                    @Override
                    public void onStart() {
                        // TODO Auto-generated method stub
                        super.onStart();
                    }

                    @Override
                    public void onSuccess(JSONObject response) {
                        // TODO Auto-generated method stub
                        super.onSuccess(response);

                        String error = "0";
                        try {
                            if( response.has("error") )
                                error = response.getString("error");
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                        if(error.equals("1")){
                            showProgressCover(View.VISIBLE, View.GONE);
                            return;
                        }
                        //#################################
						
						
						/* SAMPLE response
						 {
						 "pageCount":1,
						 "resultCount":"2",
						 "followers":[
						 		{
						 		"avatarPath":"db\/user_avatar\/000\/055\/673",
						 		"aboutMe":"fighting crimes trying to save the world",
						 		"avatarCount":4,
						 		"avatarName":"avatar_3.jpg",
						 		"favoriteCount":"17",
						 		"avatarUrl":"http:\/\/d3kqeckc4y59p1.cloudfront.net\/db\/user_avatar\/000\/055\/673\/avatar_3.jpg",
						 		"userId":"55673",
						 		"userName":"jencurativo12",
						 		"postCount":"257",
						 		"fullName":"Jennifer Curativo",
						 		"hasAvatar":1,
						 		"displayName":"jencurativo12",
						 		"isFollowing":0,
						 		"viewCount":"740"
						 		},
						 		
						 		{
						 		"avatarPath":"db\/user_avatar\/000\/085\/864",
						 		"aboutMe":"curiosity always",
						 		"avatarCount":2,
						 		"avatarName":"avatar_1.jpg",
						 		"favoriteCount":"5",
						 		"avatarUrl":"http:\/\/d3kqeckc4y59p1.cloudfront.net\/db\/user_avatar\/000\/085\/864\/avatar_1.jpg",
						 		"userId":"85864",
						 		"userName":"ABQMeta&7",
						 		"postCount":"8",
						 		"fullName":"Meta C. Hirschl",
						 		"hasAvatar":1,
						 		"displayName":"ABQMeta&7",
						 		"isFollowing":0,"viewCount":"155"
						 		} 
						 		],
						 "currentPage":"1"
						 }
						 */
                        String hey = "";
                        String ho = hey;

                        JSONArray followersListJSON = null;
                        try {
                            if( response.has("followers") )
                                followersListJSON = response.getJSONArray("followers");
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                        if(followersListJSON != null)
                            FArrFollowersList = parseFollowers( followersListJSON );
                        //	else FArrFollowersList = new ArrayList<_Profile>();
                        FArrFollowersList.trimToSize();
                        reloadListView( 1, FArrFollowersList );

                        FStillProcessing = false;

                        showProgressCover(View.GONE, View.GONE);

                    }

                    @Override
                    public void onFailure(int arg0, Header[] arg1,
                                          byte[] arg2, Throwable arg3) {
                        // TODO Auto-generated method stub
                        super.onFailure(arg0, arg1, arg2, arg3);
                    }

                    @Override
                    public void onFinish() {
                        // TODO Auto-generated method stub
                        super.onFinish();
                    }
                });
    }

    private ArrayList<_Profile> parseFollowers( JSONArray followersListJSON ){
        ArrayList<_Profile> arrFollowersList = new ArrayList<_Profile>();
        try {
            for (int j = 0; j < followersListJSON.length(); j++) {
                JSONObject profileDataJSON = followersListJSON.getJSONObject(j);
				
				/*
				"avatarPath":"db\/user_avatar\/000\/055\/673",
		 		"aboutMe":"fighting crimes trying to save the world",
		 		"avatarCount":4,
		 		"avatarName":"avatar_3.jpg",
		 		"favoriteCount":"17",
		 		"avatarUrl":"http:\/\/d3kqeckc4y59p1.cloudfront.net\/db\/user_avatar\/000\/055\/673\/avatar_3.jpg",
		 		"userId":"55673",
		 		"userName":"jencurativo12",
		 		"postCount":"257",
		 		"fullName":"Jennifer Curativo",
		 		"hasAvatar":1,
		 		"displayName":"jencurativo12",
		 		"isFollowing":0,
		 		"viewCount":"740"
				*/

                String profUserId = "";
                if( profileDataJSON.has("userId")){
                    profUserId = profileDataJSON.getString("userId");
                }

                if( profUserId.equals(GlobalVariablesHolder.X_USER_ID) )
                    continue;

                _Profile followerProfileData = new _Profile();

                followerProfileData.avatarPath		= profileDataJSON.getString("avatarPath");
                followerProfileData.aboutMe			= profileDataJSON.getString("aboutMe");
                followerProfileData.avatarCount		= profileDataJSON.getString("avatarCount");
                followerProfileData.avatarName		= profileDataJSON.getString("avatarName");
                followerProfileData.favoriteCount	= profileDataJSON.getString("favoriteCount");
                followerProfileData.avatarUrl		= profileDataJSON.getString("avatarUrl");
                followerProfileData.userId			= profileDataJSON.getString("userId");
                followerProfileData.userName		= profileDataJSON.getString("userName");
                followerProfileData.postCount		= profileDataJSON.getString("postCount");
                followerProfileData.fullName		= profileDataJSON.getString("fullName");
                followerProfileData.hasAvatar		= profileDataJSON.getString("hasAvatar");
                followerProfileData.displayName		= profileDataJSON.getString("displayName");
                followerProfileData.isFollowing		= profileDataJSON.getString("isFollowing");

                if( profileDataJSON.has("viewCount") )
                    followerProfileData.viewCount		= profileDataJSON.getString("viewCount");

                followerProfileData.email			= "";
                followerProfileData.fCounts 		= new followCounts();

                arrFollowersList.add( followerProfileData );
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return arrFollowersList;
    }
    private ArrayList<_Profile> FArrFollowingList;
    private void gatherFollowingList(){


        showProgressCover(View.VISIBLE, View.VISIBLE);

        Async_HttpClient async_HttpClient = new Async_HttpClient(Profile_Activity.this);
        String appendUrl = "";
        RequestParams requestParams = new RequestParams();

        appendUrl = "ExportFollowingList";
        requestParams.put("page", "1");
        requestParams.put("pageSize", "10");
        requestParams.put("userId", this.FUserId);

        //https://test.sportxast.com/phone/apiV2/ExportFollowingList?page=1&pageSize=10&userId=200820
        async_HttpClient.GET(appendUrl, requestParams,
                new JsonHttpResponseHandler() {

                    @Override
                    public void onStart() {
                        // TODO Auto-generated method stub
                        super.onStart();
                    }

                    @Override
                    public void onSuccess(JSONObject response) {
                        // TODO Auto-generated method stub
                        super.onSuccess(response);

                        String error = "0";
                        try {
                            if( response.has("error") )
                                error = response.getString("error");
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                        if(error.equals("1")){
                            showProgressCover(View.VISIBLE, View.GONE);
                            return;
                        }
                        //#################################
						
						/* SAMPLE response
						 {
						 "pageCount":1,
						 "resultCount":"2",
						 "following":[
						 	{
						 	"avatarPath":"db\/user_avatar\/000\/055\/673",
						 	"aboutMe":"fighting crimes trying to save the world",
						 	"avatarCount":4,
						 	"avatarName":"avatar_3.jpg",
						 	"favoriteCount":"17",
						 	"avatarUrl":"http:\/\/d3kqeckc4y59p1.cloudfront.net\/db\/user_avatar\/000\/055\/673\/avatar_3.jpg",
						 	"userId":"55673",
						 	"userName":"jencurativo12",
						 	"postCount":"257",
						 	"fullName":"Jennifer Curativo",
						 	"hasAvatar":1,
						 	"displayName":"jencurativo12",
						 	"isFollowing":1,
						 	"viewCount":"740"
						 	},
						 	 
						 	{
						 	"avatarPath":"db\/user_avatar\/000\/078\/899",
						 	"aboutMe":null,
						 	"avatarCount":0,
						 	"avatarName":"",
						 	"favoriteCount":"8",
						 	"avatarUrl":"http:\/\/d3kqeckc4y59p1.cloudfront.net\/db\/user_avatar\/000\/078\/899\/",
						 	"userId":"78899",
						 	"userName":"",
						 	"postCount":"512",
						 	"fullName":"",
						 	"hasAvatar":0,
						 	"displayName":"78899",
						 	"isFollowing":0,
						 	"viewCount":"994"
						 	}
						 ],
						 "currentPage":"1"}
						 */
                        String hey = "";
                        String ho = hey;

                        JSONArray followingListJSON = null;
                        try {
                            if( response.has("following") )
                                followingListJSON = response.getJSONArray("following");
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                        if(followingListJSON != null)
                            FArrFollowingList = parseFollowers( followingListJSON );
                        //	else FArrFollowersList = new ArrayList<_Profile>();

                        FArrFollowingList.trimToSize();
                        reloadListView( 2, FArrFollowingList );

                        FStillProcessing = false;

                        showProgressCover(View.GONE, View.GONE);
                    }

                    @Override
                    public void onFailure(int arg0, Header[] arg1,
                                          byte[] arg2, Throwable arg3) {
                        // TODO Auto-generated method stub
                        super.onFailure(arg0, arg1, arg2, arg3);
                    }

                    @Override
                    public void onFinish() {
                        // TODO Auto-generated method stub
                        super.onFinish();
                    }
                });
    }

    /** parameters must be: View.VISIBLE, View.GONE, View.INVISIBLE
     @params **/
    private void showProgressCover( final int showView, final int showWithCover){

        if(pbLoading_container2 != null ){
            pbLoading_container2.setVisibility(showView);
            ( (ProgressBar) findViewById(R.id.pbLoading2) ).setVisibility(showWithCover);
        }
    }

    private boolean FStillProcessing;
    private void gatherEventList(){

        showProgressCover(View.VISIBLE, View.VISIBLE);

        Async_HttpClient async_HttpClient = new Async_HttpClient(Profile_Activity.this);
        async_HttpClient = new Async_HttpClient(this);
        String appendUrl = "";
        RequestParams requestParams = new RequestParams();

        appendUrl = "ExportUserMedia";
        requestParams.put("username", this.FUserDisplayname);
        requestParams.put("latitude", this.FCorLatitude);
        requestParams.put("longitude",this.FCorLongitude);

        async_HttpClient.GET(appendUrl, requestParams,
                new JsonHttpResponseHandler() {

                    @Override
                    public void onStart() {
                        // TODO Auto-generated method stub
                        super.onStart();
                    }

                    @Override
                    public void onSuccess(JSONObject response) {
                        // TODO Auto-generated method stub
                        super.onSuccess(response);

                        String error = "0";
                        try {
                            if( response.has("error") )
                                error = response.getString("error");
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }

                        if(error.equals("1")){
                            showProgressCover(View.VISIBLE, View.GONE);
                            return;
                        }

                        //#################################
                        FArrMediaList = CommonFunctions_1.parseMediaList(response);
                        FArrMediaListOriginalCopy = CommonFunctions_1.parseMediaList(response);
                        //int numberOfSavedLocalHighlights = gatherLocalHighlightData( FEventId );

                        //Toast.makeText(getApplicationContext(), "SAVED LOCAL HIGHLIGHTS: " + numberOfSavedLocalHighlights, Toast.LENGTH_LONG).show();
                        boolean hy = FUserIsProfileOwner;
                        if(FUserIsProfileOwner){
                            int numberOfSavedLocalHighlights = gatherLocalHighlightData( Constants.sharePrefKey_uploadedHighlights_self );
                        }else{
                            reloadListView( 0, FArrMediaList );
                        }

                        FStillProcessing = false;
                        showProgressCover(View.GONE, View.GONE);
                    }

                    @Override
                    public void onFailure(int arg0, Header[] arg1,
                                          byte[] arg2, Throwable arg3) {
                        // TODO Auto-generated method stub
                        super.onFailure(arg0, arg1, arg2, arg3);
                    }

                    @Override
                    public void onFinish() {
                        // TODO Auto-generated method stub
                        super.onFinish();

                        showProgressCover(View.GONE, View.GONE);

                    }
                });

    }

    private int numberOfRemainingLocalHighlights;
    private String previousHighlightDataInString = "";
    private int gatherLocalHighlightData(final String eventId){

        int numberOfSavedLocalHighlights = 0;

        SharedPreferences sharedpreferences = getSharedPreferences("com.sportxast.SportXast.highlights", Profile_Activity.this.MODE_PRIVATE);
        String highlightDataInString = "";

        //highlightDataInString 		= sharedpreferences.getString(eventId, "");

        highlightDataInString 		= sharedpreferences.getString(Constants.sharePrefKey_uploadedHighlights, "");
        //Toast.makeText(getApplicationContext(), "NASUDLAN UG: " + arrHighlightsData.length, Toast.LENGTH_LONG).show();
        ArrayList<NextMediaID> arrNextMediaID = prepareNewlyUploadedHighlightsData(highlightDataInString);

        numberOfSavedLocalHighlights = arrNextMediaID.size();
        numberOfRemainingLocalHighlights = expandCurrentData( arrNextMediaID );
        previousHighlightDataInString 	 = highlightDataInString;

        return numberOfSavedLocalHighlights;
    }

    private ArrayList<MediaList> FArrMediaListOriginalCopy;

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

            FArrMediaList.add(0, mediaList);

            highlightHasBeenAdded = true;

            totalNumberOfHighlightsAdded = totalNumberOfHighlightsAdded + 1;
        }

        reloadListView( 0, FArrMediaList );

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

    /** listType: 0 - Events, 1 - followers **/
    @SuppressWarnings("unchecked")
    public void reloadListView( int listType, Object dataArray  ) {

        switch (listType) {

            case 0:
                FAdapter.updateListElements( (ArrayList<MediaList>) dataArray );
                break;
            case 1:
                FProfileAdapter.updateListElements(  (ArrayList<_Profile>) dataArray  );

                showProgressCover(View.GONE, View.GONE);

                break;
            case 2:
                FProfileAdapter.updateListElements(  (ArrayList<_Profile>) dataArray  );
                showProgressCover(View.GONE, View.GONE);
                break;
            default:
                break;

        }

        //FAdapter.updateListElements( arrMediaLists );

        FPullToRefreshListView.invalidateViews();
        FPullToRefreshListView.setCacheColorHint(Color.TRANSPARENT);
        //FAdapter.notifyDataSetChanged();
        //################################################################
    }

    private ListView FPullToRefreshListView;
    private ArrayList<MediaList> FArrMediaList;
    private EventMediaAdapter FAdapter;

    private ProfileDataAdapter FProfileAdapter;
    private void initializeCustomListView_EVENTS( ) {
        //FPullToRefreshListView = (ProperListView) findViewById(R.id.ptrListViewHighlight);
        FPullToRefreshListView = (ListView) findViewById(R.id.ptrListViewHighlight);
        //FPullToRefreshListView = new PullToRefreshListView(this);
        //FPullToRefreshListView.setCacheColorHint(Color.parseColor("#00000000"));
        FPullToRefreshListView.setCacheColorHint(Color.TRANSPARENT);

        //FPullToRefreshListView.addFooterView(footervw);
        FPullToRefreshListView.setDivider(null);

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

            mediaList.user				= new User();
            mediaList.comments 			= new ArrayList<Comments>();
            mediaList.userInFavorites 	= new ArrayList<UserInFavorites>();
            mediaList.tags 				= new ArrayList<Tag>();

            FArrMediaList.add(mediaList);
        }

        FAdapter = new EventMediaAdapter(Profile_Activity.this, FArrMediaList);
        FPullToRefreshListView.setAdapter(FAdapter);
        //	FPullToRefreshListView.setOnScrollListener(onScrollListener);
    }

    private void initializeCustomListView_FOLLOWING( ) {

        if(FPullToRefreshListView != null){
            FPullToRefreshListView.invalidateViews();
            FPullToRefreshListView.setAdapter(null);
        }

        FPullToRefreshListView = (ListView) findViewById(R.id.ptrListViewHighlight);
        //FPullToRefreshListView = new PullToRefreshListView(this);
        //FPullToRefreshListView.setCacheColorHint(Color.parseColor("#00000000"));

        FPullToRefreshListView.setCacheColorHint(Color.TRANSPARENT);

        //FPullToRefreshListView.addFooterView(footervw);
        FPullToRefreshListView.setDivider(null);

        if(FArrFollowingList == null){
            FArrFollowingList = new ArrayList<_Profile>();

            _Profile dummyProfile = new _Profile();
            FArrFollowingList.add(dummyProfile);
        }

        FArrFollowingList.trimToSize();

        FProfileAdapter = new ProfileDataAdapter(Profile_Activity.this, FArrFollowingList, 2);
        FPullToRefreshListView.setAdapter(FProfileAdapter);
        FPullToRefreshListView.setOnItemClickListener(onitemListener_Following);
        //	FPullToRefreshListView.setOnScrollListener(onScrollListener);
    }


    OnItemClickListener onitemListener_Following = new OnItemClickListener() {
        public void onItemClick(android.widget.AdapterView<?> arg0, View arg1,
                                int itemPosition, long arg3) {

            //Toast.makeText(getApplicationContext(), "itemPosition: " + itemPosition, Toast.LENGTH_LONG).show();
            Intent intent = new Intent(Profile_Activity.this,Profile_Activity.class);

            intent.putExtra("userId", 		    FArrFollowingList.get(itemPosition).userId);
            intent.putExtra("userDisplayname",  FArrFollowingList.get(itemPosition).userName);
            intent.putExtra("callingActivityID", Constants.requestCode_Profile_Activity);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        };
    };


    private void initializeCustomListView_FOLLOWERS( ) {

        if(FPullToRefreshListView != null){
            FPullToRefreshListView.invalidateViews();
            FPullToRefreshListView.setAdapter(null);
        }

        FPullToRefreshListView = (ListView) findViewById(R.id.ptrListViewHighlight);
        //FPullToRefreshListView = new PullToRefreshListView(this);
        //FPullToRefreshListView.setCacheColorHint(Color.parseColor("#00000000"));

        FPullToRefreshListView.setCacheColorHint(Color.TRANSPARENT);

        //FPullToRefreshListView.addFooterView(footervw);
        FPullToRefreshListView.setDivider(null);

        if(FArrFollowersList == null){
            FArrFollowersList = new ArrayList<_Profile>();

            _Profile dummyProfile = new _Profile();
            FArrFollowersList.add(dummyProfile);
        }
        int sizex = FArrFollowersList.size();
        FArrFollowersList.trimToSize();
        int sizexd = FArrFollowersList.size();
        FProfileAdapter = new ProfileDataAdapter(Profile_Activity.this, FArrFollowersList, 1);
        FPullToRefreshListView.setAdapter(FProfileAdapter);
        FPullToRefreshListView.setOnItemClickListener(onitemListener_Follower);
        //	FPullToRefreshListView.setOnScrollListener(onScrollListener);

    }

    OnItemClickListener onitemListener_Follower = new OnItemClickListener() {
        public void onItemClick(android.widget.AdapterView<?> arg0, View arg1,
                                int itemPosition, long arg3) {
            //Toast.makeText(getApplicationContext(), "itemPosition: " + itemPosition, Toast.LENGTH_LONG).show();
            Intent intent = new Intent(Profile_Activity.this,Profile_Activity.class);

            intent.putExtra("userId", 		    FArrFollowersList.get(itemPosition).userId);
            intent.putExtra("userDisplayname",  FArrFollowersList.get(itemPosition).userName);
            intent.putExtra("callingActivityID", Constants.requestCode_Profile_Activity);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        };
    };

    public void gatherProfileData(){
        if( pbLoading_container.getVisibility() == View.GONE ){
            /** SHOW progress bar **/
            pbLoading_container.setVisibility(View.VISIBLE);
        }

        RequestParams requestParams = new RequestParams();
        //if(userId.length()>0){
        //requestParams.add("userId", FUserId);
        String userID_ = Secure.getString(Profile_Activity.this.getContentResolver(), Secure.ANDROID_ID);
        requestParams.add("userId", FUserId);
        //requestParams.add("userId", userID_ );
        //  requestParams.add("userId", "696933" );

        Async_HttpClient async_HttpClient = new Async_HttpClient(Profile_Activity.this);
        async_HttpClient.GET("User", requestParams, new JsonHttpResponseHandler(){
            @Override
            public void onStart() {
                // TODO Auto-generated method stub
                super.onStart();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers,
                                  JSONObject response) {
                // TODO Auto-generated method stub
                super.onSuccess(statusCode, headers, response);
                Log.e("saveinfo", " "+response.toString());
                /* sample error response:
                {"message":"This username is already taken","error":1}
                */

                String error = "0";
                try {
                    if( response.has("error") )
                        error = response.getString("error");
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

                if(error.equals("1")){
                    showProgressCover(View.VISIBLE, View.GONE);
                    return;
                 }

                parseData(response);
                /** HIDE progress bar **/
                pbLoading_container.setVisibility(View.GONE);


                btn_tab_highlights.performClick();
            }

        });
        //}

    }

    public void parseData(JSONObject object_response){
        try {
            JSONObject jObject_info 	= object_response.getJSONObject("info");
			/* 
			 { 
			 "isPlayer":0,
			 "lastName":"Verizon",
			 "avatarPath":"db\/user_avatar\/000\/033\/650",
			 "followCounts":{"following":"1","follower":0},
			 "aboutMe":"",
			 "avatarCount":4,
			 "avatarName":"avatar_4.jpg",
			 "favoriteCount":"5",
			 "address":null,
			 "email":"",
			 "avatarUrl":"http:\/\/d3kqeckc4y59p1.cloudfront.net\/db\/user_avatar\/000\/033\/650\/avatar_4.jpg",
			 "userId":"33650",
			 "userName":"markv",
			 "postCount":"41",
			 "fullName":"Mark Verizon",
			 "hasAvatar":1,
			 "displayName":"markv",
			 "firstName":"Mark",
			 "isFollowing":0,"viewCount":"38"
			 }  */
            FCurrentProfile.userId 				= ""+jObject_info.get("userId");
            FCurrentProfile.avatarPath 			= ""+jObject_info.get("avatarPath");
            FCurrentProfile.avatarUrl 			= ""+jObject_info.get("avatarUrl");
            FCurrentProfile.hasAvatar 			= ""+jObject_info.get("hasAvatar");
            FCurrentProfile.avatarCount 		= ""+jObject_info.get("avatarCount");
            FCurrentProfile.avatarName 			= ""+jObject_info.get("avatarName");
            FCurrentProfile.fullName 			= ""+jObject_info.get("fullName");
            FCurrentProfile.displayName 		= ""+jObject_info.get("displayName");
            FCurrentProfile.userName 			= ""+jObject_info.get("userName");
            FCurrentProfile.email 				= ""+jObject_info.get("email");
            FCurrentProfile.aboutMe 			= ""+jObject_info.get("aboutMe");
            FCurrentProfile.postCount 			= ""+jObject_info.get("postCount");
            FCurrentProfile.favoriteCount 		= ""+jObject_info.get("favoriteCount");
            FCurrentProfile.viewCount 			= ""+jObject_info.get("viewCount");
            FCurrentProfile.fCounts.follower 	= ""+jObject_info.getJSONObject("followCounts").get("follower");
            FCurrentProfile.fCounts.following 	= ""+jObject_info.getJSONObject("followCounts").get("following");
            FCurrentProfile.isFollowing			= ""+jObject_info.get("isFollowing");

            FUserId = FCurrentProfile.userId;
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //this.headerUIClass.setMenuTitle( "DEVICE ID: "+Secure.getString(Profile_Activity.this.getContentResolver(), Secure.ANDROID_ID) );

        this.headerUIClass.setMenuTitle( FCurrentProfile.displayName.length()>0?FCurrentProfile.displayName:FCurrentProfile.userId );
        //setTitleText(profile.displayName.length()>0?profile.displayName:profile.userId);

        String appendHighlight = Integer.parseInt(FCurrentProfile.postCount)>1?" highlights":" highlight";
        String appendFollower = Integer.parseInt(FCurrentProfile.fCounts.follower)>1?" followers":" follower";

        txtvw_highlightCount.setText(FCurrentProfile.postCount	+appendHighlight);
        txtvw_followingCount.setText(FCurrentProfile.fCounts.following+" following");
        txtvw_followerCount.setText(FCurrentProfile.fCounts.follower+appendFollower);

        String ngeks = FCurrentProfile.avatarUrl;
        String he = ngeks;

        if( Integer.parseInt(FCurrentProfile.hasAvatar) > 0 ){

            //setAvatarFromStream( FProfile.avatarUrl );
            //new DownloadImageTask( imgvw_avatar_profile ).execute(FProfile.avatarUrl);
            new StreamImageTask( imgvw_avatar_profile,  (ProgressBar) findViewById(R.id.pbLoading_avatar)  ).execute(FCurrentProfile.avatarUrl);
        }else{

            if(FTemporyAvatarPath.length() > 0){
                setTemporaryLocalAvatar( FTemporyAvatarPath );
            }
            else imgvw_avatar_profile.setImageResource(R.drawable.ic_avatar_profile);
        }

        if( Integer.parseInt(FCurrentProfile.isFollowing) == 1 ){
            this.headerUIClass.setFollowButtonText("Following");
        }else{
            this.headerUIClass.setFollowButtonText("Follow");
        }
    }

    private void followUser(final _Profile currentUser){
        // TODO Auto-generated method stub
        final String appendURL = Integer.parseInt(currentUser.isFollowing) == 0?"Follow":"Unfollow";
        RequestParams requestParams = new RequestParams();
        //requestParams.put("followingID", CommonFunctions_1.sharedPreferences.getString(KEY_PROFILE.USER_ID, ""));

        requestParams.put("followingID", currentUser.userId);
        Async_HttpClient async_HttpClient = new Async_HttpClient(Profile_Activity.this);
        async_HttpClient.POST(appendURL, requestParams, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, JSONObject response) {
                // TODO Auto-generated method stub
                super.onSuccess(statusCode, response);

                Toast.makeText(getApplicationContext(),""+appendURL+"ed successfully.", Toast.LENGTH_SHORT).show();
                if (Integer.parseInt(currentUser.isFollowing) == 0){
                    headerUIClass.setFollowButtonText("Following");
                }else{
                    headerUIClass.setFollowButtonText("Follow");
                }
            }
        });
    }

    private void setTemporaryLocalAvatar(String filePath){

        File imgFile = new  File(filePath);
        if(imgFile.exists()){
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

            imgvw_avatar_profile.setImageBitmap(myBitmap);
		    
		    
		    /*
		    ImageView myImage = (ImageView) findViewById(R.id.imageviewTest);
		    myImage.setImageBitmap(myBitmap);
			*/
        }

    }



    private String FTemporyAvatarPath = "";
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            switch (requestCode) {
                case Constants.requestCode_Profile_Activity:

                    boolean profileEdited = data.getBooleanExtra("profileEdited", true);
                    this.FTemporyAvatarPath = data.getStringExtra("tempAvatarPath");

                    Toast.makeText(getApplicationContext(), FTemporyAvatarPath, Toast.LENGTH_LONG).show();

                    if( profileEdited ){

                        gatherProfileData();
                    }

                    break;

                default:
                    break;
            }

        }else if (resultCode == RESULT_CANCELED) {
            //Write your code if there's no result
        }

    }
	
	/*
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		
		 if (isPause) {
			 gatherProfileData();
			isPause = false;
		}
	}
	*/

    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();

        isPause = true;
    }


    public void addTab(Intent intent, String title){
        View v = getLayoutInflater().inflate(R.layout.tab_search, null);
        TextView t  = (TextView)v.findViewById(R.id.tab);
        t.setText(title);

        TabSpec spec = tabHost.newTabSpec(title).setContent(intent).setIndicator(v);

        tabHost.addTab(spec);

    }

    public int getTableLayoutWidth() {
        WindowManager w = ((Activity) findViewById(R.id.layout_above).getContext()).getWindowManager();
        Display d = w.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        d.getMetrics(metrics);

        return metrics.widthPixels;

    }
}
