package com.sportxast.SportXast.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sportxast.SportXast.R;
import com.sportxast.SportXast.adapter2_0.FansAdapter;
import com.sportxast.SportXast.commons.Constants;
import com.sportxast.SportXast.localdatabase.Keys.KEY_PROFILE;
import com.sportxast.SportXast.models._Follower_Following;
import com.sportxast.SportXast.models._Follower_Following.Follow;
import com.sportxast.SportXast.thirdparty_class.Async_HttpClient;

import org.json.JSONObject;

import java.util.ArrayList;

public class FollowingFragment extends ListFragment {

	private Async_HttpClient httpClient;
	private _Follower_Following following;
	private String userId;
	private static final String TAG = "FollowingFragment";
	private ArrayList<Follow> followingList;
	private SharedPreferences sharedPref;
	private SharedPreferences.Editor sharedEditor;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		httpClient = new Async_HttpClient(getActivity());
		following = new _Follower_Following();
		
		sharedPref = getActivity().getSharedPreferences("com.sportxast.SportXast", Context.MODE_PRIVATE);
		userId = sharedPref.getString(KEY_PROFILE.USER_ID, "");
//		userId = getArguments().getInt(Constant.EXTRA_USER_ID);
		followingList = new ArrayList<Follow>();
		exportFollowing();
		
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setListShown(true);
		getListView().setDivider(new ColorDrawable(R.color.grey_light));
		getListView().setDividerHeight(1);
	}
	
	private void exportFollowing() {
		RequestParams params = new RequestParams();
		params.put(Constants.EXTRA_USER_ID, userId);
		
		httpClient.GET("ExportFollowingList", params, new JsonHttpResponseHandler() {
			
			@Override
			public void onSuccess(JSONObject response) {
				super.onSuccess(response);
				Log.i(TAG, "Exporting following success.");
				sharedPref = getActivity().getSharedPreferences(Constants.SHARED_FOLLOWING, Context.MODE_PRIVATE);
				sharedEditor = sharedPref.edit();
				
				if(response.isNull("error") == false) {
					// error found.
					
				} else {
					// success
					following.parseFollow(response);
					followingList = following.Follow;// Follow is ArrayList<Follow> list
					
					for(int i=0; i<followingList.size(); i++) {
						String userId = followingList.get(i).userId;
						String displayName = followingList.get(i).displayName;
						
						if(!sharedPref.contains(userId)) {
							// If it does not contain the userId, add it.
							sharedEditor.putString(userId, displayName).apply();
						}
					}
					
					FansAdapter adapter = new FansAdapter(getActivity());
					adapter.setDataFollowing(followingList);
					setListAdapter(adapter);
				}
			}
			
			@Override
			public void onFailure(String responseBody, Throwable error) {
				super.onFailure(responseBody, error);
				Log.i(TAG, "Exporting followers failed.");
			}
			
		});
	}
}
