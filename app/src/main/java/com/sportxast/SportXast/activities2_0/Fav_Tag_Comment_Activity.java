package com.sportxast.SportXast.activities2_0;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sportxast.SportXast.Global_Data;
import com.sportxast.SportXast.R;
import com.sportxast.SportXast.StaticVariables.KEY;
import com.sportxast.SportXast.adapter2_0.CommentListAdapter;
import com.sportxast.SportXast.adapter2_0.FavoritesListAdapter;
import com.sportxast.SportXast.adapter2_0.SearchUserAdapter;
import com.sportxast.SportXast.commons.CommonFunctions_1;
import com.sportxast.SportXast.includes.HeaderUIClass;
import com.sportxast.SportXast.models._FansList;
import com.sportxast.SportXast.models._MediaFavorites;
import com.sportxast.SportXast.models._MediaLists;
import com.sportxast.SportXast.models._MediaLists.Comments;
import com.sportxast.SportXast.models._MediaLists.Tag;
import com.sportxast.SportXast.models._MediaLists.User;
import com.sportxast.SportXast.models._MediaLists.UserInFavorites;
import com.sportxast.SportXast.models._MediaTag;
import com.sportxast.SportXast.thirdparty_class.Async_HttpClient;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;



public class Fav_Tag_Comment_Activity extends Activity {
    //public class Fav_Tag_Comment_Activity extends BaseSherlockActivity {
    //TODO: Display Lists of available Favorites/Tags/Comments data


    /** Called when the activity is first created. */
    private HeaderUIClass headerUIClass;
    private RelativeLayout sx_header_wrapper;


    private Global_Data global_Data;

    private ListView listView;
    private View footervw;

    private Async_HttpClient async_HttpClient;

    _MediaFavorites mediaFavorites = new _MediaFavorites();
    _MediaTag mediaTag = new _MediaTag();
    ArrayList<Comments> commentLists = new ArrayList<Comments>();
    _FansList fansLists = new _FansList();

    CommentListAdapter commentListAdapter;
    FavoritesListAdapter favoritesListAdapter;
    SearchUserAdapter searchUserAdapter;
    ArrayAdapter<String> adapter;

    String[] list_tags = new String[0];
    String[] list_fav = new String[0];

    String eventId = "";
    String mediaId= "";
    String listType = "";


    int currentPage;
    int pageCount;
    int atIndex = 0;

    boolean isLoad = false;


    /** Called when the activity is first created. */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_comment);
        global_Data = (Global_Data)getApplicationContext();
			
			/*
			initSharedPreferences(this);
			initActionBarObjects();
			*/

        eventId = CommonFunctions_1.getIntentExtrasString(this, "eventId");
        mediaId = CommonFunctions_1.getIntentExtrasString(this, "mediaId");
        listType= CommonFunctions_1.getIntentExtrasString(this, "listType");
			
			/*
			eventId = getIntentExtrasString(this, "eventId");
			mediaId = getIntentExtrasString(this, "mediaId");
			listType = getIntentExtrasString(this, "listType");
			*/

        listView = (ListView) findViewById(R.id.listview_comment);
        footervw = getLayoutInflater().inflate(R.layout.progressbar_small, null);
        listView.setOnItemClickListener(onItemClickListener);

        async_HttpClient = new Async_HttpClient(this);

        String headerTitle = "";

        if(listType.equals(KEY.favorite)){
            findViewById(R.id.layout_comment).setVisibility(View.GONE);
            headerTitle = "Favorites";
            //getActionbar_Menu_Item("Favorites");

            currentPage = Integer.parseInt(mediaFavorites.currentPage);
            pageCount = Integer.parseInt(mediaFavorites.pageCount);

            listView.addFooterView(footervw);
            listView.setOnScrollListener(onScrollListener);
            getFavorites();
        }
        else if(listType.equals(KEY.tag)){
            findViewById(R.id.layout_comment).setVisibility(View.GONE);
            headerTitle = "Tags";
            //getActionbar_Menu_Item("Tags");
            currentPage = Integer.parseInt(mediaTag.currentPage);
            pageCount = Integer.parseInt(mediaTag.pageCount);

            listView.addFooterView(footervw);
            listView.setOnScrollListener(onScrollListener);
            getTags();

        }
        else if(listType.equals(KEY.comment)){
            headerTitle = "Comments";
            //getActionbar_Menu_Item("Comments");
            getComments();
        }
        else if(listType.equals(KEY.fans )){
            findViewById(R.id.layout_comment).setVisibility(View.GONE);

            headerTitle = "Fans";
            //getActionbar_Menu_Item("Fans");
            currentPage = Integer.parseInt(fansLists.currentPage);
            pageCount = Integer.parseInt(fansLists.pageCount);

            listView.addFooterView(footervw);
            listView.setOnScrollListener(onScrollListener);
            getFansList();
        }


        prepareHeader( headerTitle );


    }



    private void prepareHeader(final String menuTitle){
        this.sx_header_wrapper = (RelativeLayout)findViewById(R.id.sx_header_wrapper);
        this.headerUIClass = new HeaderUIClass(this, sx_header_wrapper);

        this.headerUIClass.setMenuTitle(menuTitle);
        this.headerUIClass.showBackButton(true);
        this.headerUIClass.showAddButton(false);
        this.headerUIClass.showMenuButton(false);
        this.headerUIClass.showRefreshButton(false);
        this.headerUIClass.showAboutButton(false);
        this.headerUIClass.showSearchButton(false);
        this.headerUIClass.showDoneButton(false);
        this.headerUIClass.showCameraButton(false);
        this.headerUIClass.showMenuTitle(true);


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

            }
        });
    }

    /** @category Override onBack- from BaseSherlockActivityclass
     * custom method from BaseSherlockActivity.Invoked when back button (found
     * on left side of the action bar) is clicked/pressed.
     *
     * */
		
		/*
		@Override
		public void onBack(View v) {
			// TODO Auto-generated method stub
			super.onBack(v);
		
			finish();
		}
		
		*/


    /** @category Custom Method
     * Set the adapter of listView*/
    public void setAdapter(){
        if(listType.equals("favorite")){

            favoritesListAdapter = new FavoritesListAdapter(this, mediaFavorites.favoritesList );
            listView.setAdapter(favoritesListAdapter);
//				adapter = new ArrayAdapter<String>(Fav_Tag_Comment_Activity.this, R.layout.layout_item_textview, list_fav);
//				listView.setAdapter(adapter);
//				listView.setSelection(atIndex);
        }else if(listType.equals("tag")){
            adapter = new ArrayAdapter<String>(Fav_Tag_Comment_Activity.this, R.layout.layout_item_textview, list_tags);
            listView.setAdapter(adapter);
            listView.setSelection(atIndex);
        }else if(listType.equals("comment")){

            commentListAdapter = new CommentListAdapter(this,commentLists);
            listView.setAdapter(commentListAdapter);
        }else if(listType.equals("fans")){
            searchUserAdapter = new SearchUserAdapter(this, "fans", fansLists.fansList);
            listView.setAdapter(searchUserAdapter);
        }



    }

    /**  @category Custom Method
     * Called after fetching new data and reload listView adapter to view updated data.* */
    public void reloadListView(){
        commentListAdapter.notifyDataSetChanged();
        listView.invalidateViews();
    }

    /** @category Custom Method
     * Fetch list of favorites from server.**/
    public void getFansList(){
        currentPage = Integer.parseInt(fansLists.currentPage);
        pageCount = Integer.parseInt(fansLists.pageCount);

        RequestParams requestParams = new RequestParams();
        requestParams.put("eventId", eventId);
        requestParams.put("page", ""+(Integer.parseInt(mediaFavorites.currentPage)+1));
        requestParams.put("pageSize", "20");

        async_HttpClient.GET("ExportFanList", requestParams, new JsonHttpResponseHandler(){


            @Override
            public void onSuccess(JSONObject response) {
                // TODO Auto-generated method stub
                super.onSuccess(response);
                Log.e("fans", response.toString());
                fansLists.parseFanslist(response);
                setAdapter();
            }

            @Override
            public void onFailure(int statusCode, Throwable e,
                                  JSONObject errorResponse) {
                // TODO Auto-generated method stub
                super.onFailure(statusCode, e, errorResponse);
            }
        } );
    }
    public void getFavorites(){

        currentPage = Integer.parseInt(mediaFavorites.currentPage);
        pageCount = Integer.parseInt(mediaFavorites.pageCount);


        if (mediaFavorites.isFirstLoad || currentPage <= pageCount) {
            Log.e("favorites", "page: " + currentPage + " cnt: " + pageCount);
            RequestParams requestParams = new RequestParams();


            requestParams.put(mediaId.length()>0 ?"mediaId":"eventId",mediaId.length()>0 ?mediaId:eventId);
            requestParams.put("page", ""+(Integer.parseInt(mediaFavorites.currentPage)+1));
            requestParams.put("pageSize", "20");

            async_HttpClient.GET(mediaId.length()>0?"ExportUserInMediaFavorites":"ExportUserInEventFavorites", requestParams, new JsonHttpResponseHandler(){


                @Override
                public void onStart() {
                    // TODO Auto-generated method stub
                    super.onStart();
                }

                @Override
                public void onSuccess(JSONObject response) {
                    // TODO Auto-generated method stub
                    super.onSuccess(response);
                    parseFavorites(response);
                    setAdapter();
                }

                @Override
                public void onFailure(int statusCode, Throwable e,
                                      JSONObject errorResponse) {
                    // TODO Auto-generated method stub
                    super.onFailure(statusCode, e, errorResponse);
                }

            });
        }
    }

    /** @category Custom Method
     * Fetch list of tags from server.**/
    public void getTags(){
        currentPage = Integer.parseInt(mediaTag.currentPage);
        pageCount = Integer.parseInt(mediaTag.pageCount);


        if (mediaTag.isFirstLoad || currentPage <= pageCount) {
            Log.e("tags", "mediaId: " + mediaId + " eventId: " + eventId);

            RequestParams requestParams = new RequestParams();
            requestParams.put(mediaId.length()>0 ?"mediaId":"eventId",mediaId.length()>0 ? mediaId:eventId);
            requestParams.put("page", ""+(Integer.parseInt(mediaTag.currentPage)+1));
            requestParams.put("pageSize", "20");
            Log.e("tags",requestParams.toString());

            async_HttpClient.GET(mediaId.length()>0?"ExportMediaTag":"ExportEventTag", requestParams, new JsonHttpResponseHandler(){


                @Override
                public void onStart() {
                    // TODO Auto-generated method stub
                    super.onStart();
                }

                @Override
                public void onSuccess(JSONObject response) {
                    // TODO Auto-generated method stub
                    super.onSuccess(response);

                    parseTag(response);
                    setAdapter();

                }

                @Override
                public void onFailure(int statusCode, Throwable e,
                                      JSONObject errorResponse) {
                    // TODO Auto-generated method stub
                    super.onFailure(statusCode, e, errorResponse);
                }

            });
        }
    }

    /** @category Custom Method
     * Fetch list of comment from server.**/
    public void getComments(){

        RequestParams requestParams = new RequestParams();
        requestParams.put("parentId",mediaId.length()>0?mediaId:eventId);
        requestParams.put("type", mediaId.length()>0?"media":"event");
        requestParams.put("page", "1");
        requestParams.put("pageSize", "20");



        async_HttpClient.GET("ExportComments", requestParams, new JsonHttpResponseHandler(){
            @Override
            public void onStart() {
                // TODO Auto-generated method stub
                super.onStart();
                Log.v("onStart", "onStart");


            }
            @Override
            public void onSuccess(JSONObject response) {
                // TODO Auto-generated method stub
                super.onSuccess(response);
                Log.v("onSuccess", "onSuccess");
                commentLists = new _MediaLists().parseComments(response);
                Log.v("comments", ""+commentLists.toString());
                setAdapter();
                //reloadList();

            }

            @Override
            public void onProgress(int bytesWritten, int totalSize) {
                // TODO Auto-generated method stub
                Log.v("onProgress", "onProgress");
                super.onProgress(bytesWritten, totalSize);
                //reloadList();

            }
            @Override
            public void onFinish() {
                // TODO Auto-generated method stub
                super.onFinish();
                Log.v("onFinish", "onFinish");



            }
            @Override
            public void onFailure(String responseBody, Throwable error) {
                // TODO Auto-generated method stub
                super.onFailure(responseBody, error);
                Log.v("onFailure", "onFailure :"+responseBody+" : "+error);


            }
        });

    }

    public void parseFavorites(JSONObject response){

        mediaFavorites.isFirstLoad = false;
        atIndex = list_fav.length;
        try {
            mediaFavorites.resultCount = response.get("resultCount").toString();
            mediaFavorites.currentPage = response.get("currentPage").toString();
            mediaFavorites.pageCount = response.get("pageCount").toString();
        } catch (JSONException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        try {
            JSONArray u = response.getJSONArray("list");

            for (int i = 0; i < u.length(); i++) {
                JSONObject uObject = u.getJSONObject(i);
                UserInFavorites favorites = new UserInFavorites();
                favorites.userId = ""+uObject.get("userId");
                favorites.avatarPath = ""+uObject.get("avatarPath");
                favorites.avatarUrl = ""+uObject.get("avatarUrl");
                favorites.hasAvatar = ""+uObject.get("hasAvatar");
                favorites.avatarCount = ""+uObject.get("avatarCount");
                favorites.avatarName = ""+uObject.get("avatarName");
                favorites.fullName = ""+uObject.get("fullName");
                favorites.displayName = ""+uObject.get("displayName");
                favorites.userName = ""+uObject.get("userName");
                favorites.email = ""+uObject.get("email");
                favorites.aboutMe = ""+uObject.get("aboutMe");
                favorites.postCount = ""+uObject.get("postCount");
                favorites.favoriteCount = ""+uObject.get("favoriteCount");
                favorites.viewCount = ""+uObject.get("viewCount");



                mediaFavorites.favoritesList.add(favorites);

            }

            list_fav = new String[mediaFavorites.favoritesList.size()];
            for (int i = 0; i < mediaFavorites.favoritesList.size(); i++) {
                list_fav[i] = mediaFavorites.favoritesList.get(i).displayName;
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }

    public void parseTag(JSONObject response){
        mediaTag.isFirstLoad = false;
        atIndex = list_tags.length;
        try {
            mediaTag.resultCount = response.get("resultCount").toString();
            mediaTag.currentPage = response.get("currentPage").toString();
            mediaTag.pageCount = response.get("pageCount").toString();
        } catch (JSONException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        try {
            JSONArray jArray_list = response.getJSONArray("list");


            for (int i = 0; i < jArray_list.length(); i++) {
                JSONObject object = jArray_list.getJSONObject(i);
                Tag tag = new Tag();


                tag.id = ""+object.get("id");
                tag.name = ""+object.get("name");


                mediaTag.tagList.add(tag);
            }

            list_tags = new String[mediaTag.tagList.size()];
            for (int i = 0; i < mediaTag.tagList.size(); i++) {
                list_tags[i] ="#" + mediaTag.tagList.get(i).name;
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }

    public void parseAddComment(JSONObject jObject_response){
        Comments comment = new Comments();



        try {


            if(jObject_response.getInt("error")==0){
                JSONObject json_c = jObject_response.getJSONObject("comment");
                comment.commentId = json_c.getString("commentId");
                comment.commentParentID = json_c.getString("commentParentID");
                comment.commentParentType = json_c.getString("commentParentType");
                comment.commentBody = json_c.getString("commentBody");
                comment.commentAge = json_c.getString("commentAge");
                comment.commentUserName = json_c.getString("commentUserName");
                comment.commentUserId = json_c.getString("commentUserId");
                comment.commentTags = json_c.getString("commentTags");



                JSONObject json_u = json_c.getJSONObject("user");

                comment.user = new User();
                comment.user.userId  = json_u.getString("userId");
                comment.user.avatarPath  = json_u.getString("avatarPath");
                comment.user.avatarUrl  = json_u.getString("avatarUrl");
                comment.user.hasAvatar  = ""+json_u.get("hasAvatar");
                comment.user.avatarCount  = ""+json_u.get("avatarCount");
                comment.user.avatarName  = json_u.getString("avatarName");
                comment.user.fullName  = json_u.getString("fullName");
                comment.user.displayName  = json_u.getString("displayName");
                comment.user.userName  = json_u.getString("userName");
                comment.user.aboutMe  = json_u.getString("aboutMe");
                comment.user.postCount  = json_u.getString("postCount");
                comment.user.favoriteCount  = json_u.getString("favoriteCount");
                comment.user.viewCount  = json_u.getString("viewCount");

                commentLists.add(comment);

                //	reloadListView();

                //	listView.setSelection(commentLists.size()-1);
            }


        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }


    /** @category Custom Method
     * Called when Send button is click. Save comment to server.
     * **/
    public void onSend(View v){
        RequestParams requestParams = new RequestParams();
        // requestParams.put("mediaId", mediaId);
        requestParams.put("parentId",mediaId.length()>0?mediaId:eventId);
        requestParams.put("type", mediaId.length()>0?"media":"event");
        requestParams.put("comment",((EditText)findViewById(R.id.edittext_comment)).getText().toString() );

        Log.e("params", requestParams.toString());
        async_HttpClient.POST("AddComment", requestParams, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(JSONObject response) {
                // TODO Auto-generated method stub
                super.onSuccess(response);
                //	parseAddComment(response);
                Log.e("comment", response.toString());
                boolean isSuccess=false;
                try {
                    isSuccess= response.getInt("error")==0?false:true;
                } catch (JSONException e) {
                    isSuccess = true;}

                ///	if(isSuccess){
                parseAddComment(response);
//			 			Comments comments = new Comments();
//			 			comments.commentBody = ((EditText)findViewById(R.id.edittext_comment)).getText().toString();
//			 			comments.commentUserId = sharedPreferences.getString(KEY_PROFILE.USER_ID, "");
//			 			comments.commentUserName = sharedPreferences.getString(KEY_PROFILE.USER_NAME, "");
//			 			
//			 			commentLists.add(comments);
                //setAdapter();
                reloadListView();
                listView.setSelection(commentLists.size()-1);

                //EventDetail_Activity.getInstance().txtvw_commentCount.setText(""+commentLists.size() + " comments");
                //		}


            }

            @Override
            public void onFailure(int arg0, Header[] arg1, byte[] arg2,
                                  Throwable arg3) {
                // TODO Auto-generated method stub
                super.onFailure(arg0, arg1, arg2, arg3);
            }
        });
    }

    /**@category Listener
     * scrollListener for listView.
     * **/
    OnScrollListener onScrollListener = new OnScrollListener() {

        @Override
        public void onScrollStateChanged(AbsListView view, int scrollState) {
            // TODO Auto-generated method stub

        }

        @Override
        public void onScroll(AbsListView view, int firstVisibleItem,
                             int visibleItemCount, int totalItemCount) {
            // TODO Auto-generated method stub

            final int lastItem = firstVisibleItem + visibleItemCount;


            int size = 0;
            int cPage = 0;

            if (listType.equals("favorite")) {

                size = mediaFavorites.favoritesList.size();
                cPage = Integer.parseInt(mediaFavorites.currentPage);

            } else if (listType.equals("tag")) {

                size = mediaTag.tagList.size();
                cPage = Integer.parseInt(mediaTag.currentPage);
            }



            if (size > 0
                    && currentPage != cPage) {
                // Log.e("onScroll", "true");
                if (lastItem == (totalItemCount))
                    isLoad = true;
            } else {
                isLoad = false;
            }


            if (isLoad) {
                if (listType.equals("favorite")) {
                    getFavorites();
                }
                else if (listType.equals("tag")) {
                    getTags();
                }
                isLoad = false;
            }

            if(lastItem == totalItemCount && currentPage==pageCount && size> 0){

                listView.removeFooterView(footervw);

            }




        }
    };

    OnItemClickListener onItemClickListener = new OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                long arg3) {
            // TODO Auto-generated method stub

            if(listType.equals(KEY.favorite)){

            }
            else if(listType.equals(KEY.tag)){
                //goto Highlight
                Intent intent = new Intent(Fav_Tag_Comment_Activity.this,	Highlight_Activity.class);
                intent.putExtra("eventId","");
                intent.putExtra("hashtag", list_tags[arg2].replace("#", ""));
                intent.putExtra("eventTeams", list_tags[arg2].replace("#", ""));
                intent.putExtra("eventDate", "");
                intent.putExtra("isToday", ""+0);
                startActivity(intent);
            }
            else if(listType.equals(KEY.comment)){
                //nothing
            }
            else if(listType.equals(KEY.fans )){
                //goto Profile

                Intent intentPr = new Intent(Fav_Tag_Comment_Activity.this,Profile_Activity.class);
                intentPr.putExtra("userId", ((UserInFavorites)fansLists.fansList.get(arg2)).userId);
                intentPr.putExtra("userDisplayname",((UserInFavorites)fansLists.fansList.get(arg2)).displayName);
                startActivity(intentPr);
            }
        }
    };
}
