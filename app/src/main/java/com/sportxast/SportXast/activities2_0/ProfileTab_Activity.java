package com.sportxast.SportXast.activities2_0;

//import com.sportxast.SportXast.BaseSherlockActivity;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sportxast.SportXast.Global_Data;
import com.sportxast.SportXast.R;
import com.sportxast.SportXast.adapter2_0.FollowerFollowingAdapter;
import com.sportxast.SportXast.adapter2_0.HighlightAdapter;
import com.sportxast.SportXast.commons.CommonFunctions_1;
import com.sportxast.SportXast.models._Follower_Following;
import com.sportxast.SportXast.models._MediaLists;
import com.sportxast.SportXast.thirdparty_class.Async_HttpClient;

import org.apache.http.Header;
import org.json.JSONObject;


//public class ProfileTab_Activity extends BaseSherlockActivity {
	
public class ProfileTab_Activity extends Activity {
	
	private ListView listView;
	private View footervw;
	
	private Async_HttpClient async_HttpClient;
	private Global_Data global_Data;
	private _Follower_Following list_follower = new _Follower_Following();
	private _Follower_Following list_following = new _Follower_Following();
	private _MediaLists list_highlight = new _MediaLists();
	
	//private SearchUserAdapter follower_followingAadapter;
	//private HighlightAdapter highlightAdapter;

	private int currentPage;
	private int pageCount;
	
	private String username="";
	private String userId="";
	private int tab = 1;
	
	private boolean isLoad = false;
	
	private View commentView;
	private static final String TAG = "ProfileTabActivity";
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	
	    // TODO Auto-generated method stub
	    setContentView(contentView());
	    
	    username=  CommonFunctions_1.getIntentExtrasString(this, "username");
	    userId 	=  CommonFunctions_1.getIntentExtrasString(this, "userId"); 
	    tab = getIntent().getExtras().getInt("tab");
	    
	    //Log.i(TAG, "userId: " + userId + "& tab " + Profile_Activity.tabChosen);
	    /*
	    username = getIntentExtrasString(this, "username");
	    userId = getIntentExtrasString(this, "userId"); 
	    */
	    
	    
	   // Toast.makeText(ProfileTab_Activity.this, "Execute:" + tab, Toast.LENGTH_SHORT).show(); 
	    if(tab==2 || tab==3){
			listView.setOnScrollListener(onScrollListener);
			footervw = getLayoutInflater().inflate(R.layout.progressbar_small, null);
			listView.addFooterView(footervw);
		}
		
		commentView = getLayoutInflater().inflate(R.layout.layout_comment_field, null);
		addContentView(commentView, new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
		commentView.setVisibility(View.GONE);
	   fetchData();
	}
 
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
		
		   if (newConfig.keyboardHidden == Configuration.KEYBOARDHIDDEN_NO) {
		        Toast.makeText(this, "keyboard visible", Toast.LENGTH_SHORT).show();
		    } else if (newConfig.keyboardHidden == Configuration.KEYBOARDHIDDEN_YES) {
		        Toast.makeText(this, "keyboard hidden", Toast.LENGTH_SHORT).show();
		    	commentView.setVisibility(View.GONE);
		    	
		    }
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			Toast.makeText(this, "back button pressed", Toast.LENGTH_SHORT).show();
		    commentView.setVisibility(View.GONE);
		}
		
		return super.onKeyDown(keyCode, event);
		 
	}
	 
	public View contentView(){
		global_Data = (Global_Data)getApplicationContext();
		listView = new ListView(this);
		listView.setId(R.id.list_profiletab);
		listView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
		listView.setDivider(null);
		listView.setAdapter(new HighlightAdapter(this, list_highlight));
		 
		return listView; 
	}
	
	public void fetchData(){
		
		
		async_HttpClient = new Async_HttpClient(this);
		String appendUrl = "";
		RequestParams requestParams = new RequestParams();
		
		Log.i(TAG, "tab on " + tab);
		switch (tab) {
		case 1:
		{
			currentPage = Integer.parseInt(list_highlight.currentPage);
			pageCount =  Integer.parseInt(list_highlight.pageCount);
			
			appendUrl = "ExportUserMedia";
			requestParams.put("username", username.length()>0?username:userId);
			requestParams.put("latitude", global_Data.getCoordinate().latitude);
			requestParams.put("longitude",global_Data.getCoordinate().longitude);
		}
			break;
		case 2:
		{
			currentPage = Integer.parseInt(list_follower.currentPage);
			pageCount =  Integer.parseInt(list_follower.pageCount);
			
			appendUrl="ExportFollowerList";
			requestParams.put("userId", userId);
			requestParams.put("page",""+(currentPage+1));
			requestParams.put("pageSize", "5");
		}
			break;
		case 3:
		{
			currentPage = Integer.parseInt(list_following.currentPage);
			pageCount =  Integer.parseInt(list_following.pageCount);
			
			appendUrl="ExportFollowingList";
			requestParams.put("userId", userId);
			requestParams.put("page",""+(currentPage+1));
			requestParams.put("pageSize", "5");
		}
			break;

		default:
			break;
		}
		int size =  tab==1?list_highlight.mediaLists.size():
			tab==2?list_follower.Follow.size():
			tab==3?list_following.Follow.size():0;			
		if (size==0 || currentPage < pageCount) {
			
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
							
							Log.e("profile tab : "+tab, response.toString());
							
							switch (tab) {
							case 1:
							{
								parseHighlight(response);
							}
								break;
							case 2:
							{
								parseFollower(response);
							}
								break;
							case 3:
							{
								parseFollowing(response);
							}
								break;

							default:
								break;
							}
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
	}
	
	public void parseHighlight(JSONObject response){
		list_highlight = new _MediaLists();
		list_highlight.aq = new AQuery(this);
		list_highlight.parseMediaList(response);
		
		//commented by ATAN June 3 2014//
		//listView.setAdapter(new EventMediaAdapter(this, list_highlight.mediaLists, commentView));
		
	}
	
	public void parseFollower(JSONObject response){
		//list_follower = new _Follower_Following();
		list_follower.parseFollow(response);
		//if(Integer.parseInt(list_follower.pageCount)<=1){listView.removeFooterView(footervw);}
		listView.setAdapter(new FollowerFollowingAdapter(this, list_follower));
	}
	public void parseFollowing(JSONObject response){
		//list_following = new _Follower_Following();
		list_following.parseFollow(response);
		//if(Integer.parseInt(list_following.pageCount)<=1){listView.removeFooterView(footervw);}
		listView.setAdapter(new FollowerFollowingAdapter(this, list_following));
	}
	
	 
	OnScrollListener onScrollListener = new OnScrollListener() {

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {
			// TODO Auto-generated method stub

			int cPage = tab==1? Integer.parseInt(list_highlight.currentPage):
						tab==2?Integer.parseInt(list_follower.currentPage):
						tab==3?Integer.parseInt(list_following.currentPage):0;
			
						
			int size =  tab==1?list_highlight.mediaLists.size():
						tab==2?list_follower.Follow.size():
						tab==3?list_following.Follow.size():0;			
			final int lastItem = firstVisibleItem + visibleItemCount;
			if (size > 0
					&& currentPage != cPage) {
				// Log.e("onScroll", "true");
				if (lastItem == (totalItemCount-1))
					isLoad = true;
			} else {
				isLoad = false;
			}

			if (isLoad) {
				fetchData();
				isLoad = false;
			}

		}
	};
	

	}
