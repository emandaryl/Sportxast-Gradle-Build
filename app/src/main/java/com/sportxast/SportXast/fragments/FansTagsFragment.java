package com.sportxast.SportXast.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListView;

import com.activeandroid.util.Log;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sportxast.SportXast.Global_Data;
import com.sportxast.SportXast.R;
import com.sportxast.SportXast.activities2_0.FansTagsActivity;
import com.sportxast.SportXast.activities2_0.Highlight_Activity;
import com.sportxast.SportXast.activities2_0.Profile_Activity;
import com.sportxast.SportXast.adapter2_0.FansAdapter;
import com.sportxast.SportXast.adapter2_0.TagsAdapter;
import com.sportxast.SportXast.commons.Constants;
import com.sportxast.SportXast.commons.GlobalVariablesHolder;
import com.sportxast.SportXast.models._EventLists.EventLists;
import com.sportxast.SportXast.thirdparty_class.Async_HttpClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FansTagsFragment extends ListFragment {

	private int listType = 0;
	private EventLists eventList;
	private Global_Data globalData;
	private Async_HttpClient httpClient;
	private String theUserId;
	private SharedPreferences sharedPref;
	private SharedPreferences.Editor sharedEditor;
	private static final String TAG = "FansTagsFragment";
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		globalData = (Global_Data) getActivity().getApplicationContext();
		eventList = globalData.getEventList();
		httpClient = new Async_HttpClient(getActivity());
		theUserId = GlobalVariablesHolder.X_USER_ID;
		sharedPref = getActivity().getSharedPreferences(Constants.SHARED_FOLLOWING, Context.MODE_PRIVATE);
		sharedEditor = sharedPref.edit();
		
		Log.i(TAG, "User Id: " + theUserId);
		
		Bundle bundle = getArguments();
		if(bundle != null) {
			listType = bundle.getInt(Constants.EXTRA_EVENT_TYPE);
		}
		
		if(listType == FansTagsActivity.LIST_TAGS) {
			
			if(parseInt(eventList.eventTotalTagCount) > 0) {
				//eventLists.mediaTagWithHighlight
				TagsAdapter adapter = new TagsAdapter(getActivity());
				adapter.setData(eventList.mediaTagWithHighlight);
				
				setListAdapter(adapter);
//				Toast.makeText(getActivity(), "Tags List loading...", Toast.LENGTH_LONG).show();
			}
		} else if(listType == FansTagsActivity.LIST_FANS) {
			
			grabFollowingList();
			
			if(parseInt(eventList.eventTotalFanCount) > 0) {
				//eventLists.eventFans
				FansAdapter adapter = new FansAdapter(getActivity());
				adapter.setData(eventList.eventFans);
				
				setListAdapter(adapter);
			}
		}
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		getListView().setDivider(new ColorDrawable(R.color.grey_light));
		getListView().setDividerHeight(1);
		
		if(listType == FansTagsActivity.LIST_TAGS) {
			if(parseInt(eventList.eventTotalTagCount) == 0) {
				setListShown(true);
				setEmptyText("No Tags Found");
			}
		} else if(listType == FansTagsActivity.LIST_FANS) {
			if(parseInt(eventList.eventTotalFanCount) == 0) {
				setListShown(true);
				setEmptyText("No Fans Found");
			}
		}
	}
	
	@Override
	public void onListItemClick(ListView listView, View view, int position, long id) {
		if(listType == FansTagsActivity.LIST_FANS) {
			Intent intentProfile = new Intent(getActivity(), Profile_Activity.class);
			intentProfile.putExtra("userId", eventList.eventFans.get(position).id);
			intentProfile.putExtra("userDisplayname", eventList.eventFans.get(position).name);
			intentProfile.putExtra("isMyProfile", false);
			startActivity(intentProfile);
		} else if(listType == FansTagsActivity.LIST_TAGS) {			
			Intent intent = new Intent(getActivity(), Highlight_Activity.class);
			intent.putExtra(Constants.EXTRA_EVENT_ID, eventList.eventId); //eventId
			intent.putExtra(Constants.EXTRA_EVENT_TEAMS, "");
			//intent.putExtra(Constants.EXTRA_EVENT_DATE, eventList.event);
			intent.putExtra(Constants.EXTRA_IS_TODAY, eventList.eventIsOpen);
			intent.putExtra(Constants.EXTRA_HASHTAG, eventList.mediaTagWithHighlight.get(position).tagName);
			intent.putExtra(Constants.EXTRA_FROM_EVENT_PAGE, true);
			startActivity(intent);
		}
		
	}
	
	private void grabFollowingList() {
		RequestParams params = new RequestParams();
		params.put(Constants.EXTRA_USER_ID, theUserId);
		httpClient.GET("ExportFollowingList", params, new JsonHttpResponseHandler() {
			
			@Override
			public void onSuccess(JSONObject response) {
				super.onSuccess(response);
				if(response.isNull(Constants.KEY_FOLLOWING)) {
					/*
					 * Error. User has not followed anyone yet.
					 */
					Log.i(TAG, "ExportFollowing error");
				} else {
					Log.i(TAG, "ExportFollowing success");
					try {
						JSONArray array = response.getJSONArray(Constants.KEY_FOLLOWING);
						
						for(int i=0; i<array.length(); i++) {
							JSONObject jsonUserInfo = array.getJSONObject(i);
							// key : userId
							// value : fullName
							// temporary
							String userId = jsonUserInfo.getString(Constants.EXTRA_USER_ID);
							String fullname = jsonUserInfo.getString(Constants.EXTRA_FULLNAME);
							if(!sharedPref.contains(userId)) {
								// IF no userId key found, add it.
								sharedEditor.putString(userId, fullname).apply();
							}
						}
						
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
				
			}
		});
	}
//	
//	private void getMediaTags() {
//		RequestParams params = new RequestParams();
//		
//	}
	
	private int parseInt(String s){
		try {
			return Integer.parseInt(s);
			
		} catch (Exception e) {
		return 0;
		}
	}
}
