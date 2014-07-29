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

public class FollowersFragment extends ListFragment {
	
	private Async_HttpClient httpClient;
	private String userId;
	private _Follower_Following followers;
	private static final String TAG = "FollowersFragment";
	private ArrayList<Follow> followersList;
	private SharedPreferences sharedPref;
	private SharedPreferences.Editor sharedEditor;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sharedPref = getActivity().getSharedPreferences("com.sportxast.SportXast", Context.MODE_PRIVATE);
		userId = sharedPref.getString(KEY_PROFILE.USER_ID, "");
		
		httpClient = new Async_HttpClient(getActivity());
//		userId = getArguments().getInt(Constant.EXTRA_USER_ID);
		followers = new _Follower_Following();
		exportFollowers();
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setListShown(true);
		getListView().setDivider(new ColorDrawable(R.color.grey_light));
		getListView().setDividerHeight(1);
	}
	
	private void exportFollowers() {
		RequestParams params = new RequestParams();
		params.put(Constants.EXTRA_USER_ID, userId);
		
		httpClient.GET("ExportFollowerList", params, new JsonHttpResponseHandler() {
			
			@Override
			public void onSuccess(JSONObject response) {
				super.onSuccess(response);
				Log.i(TAG, "Exporting followers success.");
				sharedPref = getActivity().getSharedPreferences(Constants.SHARED_FOLLOWERS, Context.MODE_PRIVATE);
				sharedEditor = sharedPref.edit();
				
				if(response.isNull("error") == false) {
					// error found.
					//setListShown(true);
//					setEmptyText("No Followers");
				} else {
					// success
					followers.parseFollow(response);
					followersList = followers.Follow;
					
					for(int i=0; i<followersList.size(); i++) {
						String userId = followersList.get(i).userId;
						String displayName = followersList.get(i).displayName;
						
						if(!sharedPref.contains(userId)) {
							sharedEditor.putString(userId, displayName).apply();
						}
					}
					
					FansAdapter adapter = new FansAdapter(getActivity());
					adapter.setDataFollowing(followersList);
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
