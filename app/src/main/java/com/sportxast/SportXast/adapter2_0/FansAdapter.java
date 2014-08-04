package com.sportxast.SportXast.adapter2_0;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.activeandroid.util.Log;
import com.androidquery.AQuery;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sportxast.SportXast.R;
import com.sportxast.SportXast.commons.Constants;
import com.sportxast.SportXast.models._EventLists.MediaWithPlayer;
import com.sportxast.SportXast.models._Follower_Following.Follow;
import com.sportxast.SportXast.thirdparty_class.Async_HttpClient;

import org.json.JSONObject;

import java.util.ArrayList;

public class FansAdapter extends BaseAdapter {

	private LayoutInflater inflater;
	private AQuery query;
	private Async_HttpClient httpClient;
	private static final String TAG = "FansAdapter";
	private SharedPreferences sharedPref;
	private SharedPreferences.Editor sharedEditor;
	private ArrayList<MediaWithPlayer> fansList;
	private ArrayList<Follow> followingList;
	
	private MediaWithPlayer fan;
	private Follow follow;
	
	private static final int TYPE_FANS = 5;
	private static final int TYPE_FOLLOWING = 6;
	private int adapterType;
	
	private Context context;
	
	public FansAdapter(Context context) {
//		super(context, android.R.layout.simple_list_item_2);
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		query = new AQuery(context);
		httpClient = new Async_HttpClient(context);
		sharedPref = context.getSharedPreferences(Constants.SHARED_FOLLOWING, Context.MODE_PRIVATE);
		sharedEditor = sharedPref.edit();
		this.context = context;
	}
	
	public void setData(ArrayList<MediaWithPlayer> fansList) {
		//clear();
//		if(fansList != null) {
//			for(MediaWithPlayer fan : fansList) {
//				add(fan);
//			}
//		}
		this.fansList = fansList;
		adapterType = TYPE_FANS;
	}
	
	public void setDataFollowing(ArrayList<Follow> followingList) {
		this.followingList = followingList;
		adapterType = TYPE_FOLLOWING;
	}
	
	@SuppressLint("NewApi")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		
		if(convertView == null) {
			convertView = inflater.inflate(R.layout.fans_list, parent, false);
			
			holder = new ViewHolder();
			holder.imgvwProfPic = (ImageView) convertView.findViewById(R.id.imgvwProfPic);
			holder.txtUsername = (TextView) convertView.findViewById(R.id.txtUsername);
			holder.txtFollow = (TextView) convertView.findViewById(R.id.txtFollow);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		boolean isFollowing = false;
		//final MediaWithPlayer fan = null;
		
		if(adapterType == TYPE_FANS) {
//			final MediaWithPlayer fan = getItem(position);
			fan = fansList.get(position);
			
			if(fan.avatarUrl.trim().length() > 0) {
				query.id(holder.imgvwProfPic).image(fan.avatarUrl, false, true, 0, R.drawable.ic_avatar_profile);
			}
			
			if(sharedPref.contains(fan.id)) {
				isFollowing = true;
				holder.txtFollow.setTag(fan.id);
				holder.txtUsername.setText(fan.name);
			}
		} else {
			// TYPE_FOLLOWING
			follow = followingList.get(position);
			
			if(follow.avatarUrl.trim().length() > 0) {
				query.id(holder.imgvwProfPic).image(follow.avatarUrl, false, true, 0, R.drawable.ic_avatar_profile);
			}
			
			if(sharedPref.contains(follow.userId)) {
				isFollowing = true;
				holder.txtFollow.setTag(follow.userId);
				holder.txtFollow.setText(follow.displayName);
				Log.i("displayname", follow.displayName);
			}
		}
		
		
		if(isFollowing) {
			holder.txtFollow.setBackground(context.getResources().getDrawable(R.drawable.shape_selected_borderblue));
			holder.txtFollow.setTextColor(context.getResources().getColor(R.color.white));
			holder.txtFollow.setText(context.getResources().getText(R.string._Following));
		}
		
		
//		if(sharedPref.contains(fan.id)) {
//			// already following
//			holder.txtFollow.setBackground(context.getResources().getDrawable(R.drawable.shape_selected_borderblue));
//			holder.txtFollow.setTextColor(context.getResources().getColor(R.color.white));
//			holder.txtFollow.setText(context.getResources().getText(R.string._Following));
//			holder.txtFollow.setTag(fan.id);
//		} else {
//			
//		}
		
//		holder.txtUsername.setText(fan.name);
		holder.txtFollow.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				final RequestParams params = new RequestParams();
				if(adapterType == TYPE_FANS) {
					params.put("followingID", fan.id); // Current fan/user id to follow.
				} else if(adapterType == TYPE_FOLLOWING) {
					params.put("followingID", follow.userId);
				}
				
				
				httpClient.POST("Follow", params, new JsonHttpResponseHandler() {
					
					@SuppressLint("NewApi")
					@Override
					public void onSuccess(JSONObject response) {
						super.onSuccess(response);
						
						// Grab if there is a success key
						if(response.isNull(Constants.KEY_SUCCESS) == false) {
							// if returns success
							Log.i(TAG, "Follow success.");
							holder.txtFollow.setBackground(context.getResources().getDrawable(R.drawable.shape_selected_borderblue));
							holder.txtFollow.setTextColor(context.getResources().getColor(R.color.white));
							holder.txtFollow.setText(context.getResources().getText(R.string._Following));
							
							if(adapterType == TYPE_FANS) {
								sharedEditor.putString(fan.id, fan.name).apply();
							} else {
								// TYPE_FOLLOWING
								sharedEditor.putString(follow.userId, follow.displayName).apply();
							}
							
							
						} else if(response.isNull(Constants.KEY_ERROR) == false) {
							// if returns error
							Log.i(TAG, "Follow error/Already following.");
							if(adapterType == TYPE_FANS) {
								unfollowAUser(params, holder, fan.id);
							} else {
								unfollowAUser(params, holder, follow.userId);
							}
							
						}
					}
					
					@Override
					public void onFailure(String responseBody, Throwable error) {
						super.onFailure(responseBody, error);
					}
					
				});
			}
		});
		
		return convertView;
	}
	
	private void unfollowAUser(RequestParams params, final ViewHolder holder, final String fanId) {
		httpClient.POST("Unfollow", params, new JsonHttpResponseHandler() {
			
			@SuppressLint("NewApi")
			@Override
			public void onSuccess(JSONObject response) {
				super.onSuccess(response);
				Log.i(TAG, "Unfollow success.");
				holder.txtFollow.setBackground(context.getResources().getDrawable(R.drawable.shape_tabwhite_borderblue));
				holder.txtFollow.setTextColor(context.getResources().getColor(R.color.uni_blue));
				holder.txtFollow.setText(context.getResources().getText(R.string._Follow));
				holder.txtFollow.setTag(null);
				
				sharedEditor.remove(fanId).apply();
			}
			
			@Override
			public void onFailure(String responseBody, Throwable error) {
				super.onFailure(responseBody, error);
				Log.i(TAG, "Unfollow failed");
			}
			
		});
	}
	
	private static class ViewHolder {
		ImageView imgvwProfPic;
		TextView txtUsername;
		TextView txtFollow;
	}

	@Override
	public int getCount() {
		if(adapterType == TYPE_FANS) {
			return fansList.size();
		} else if(adapterType == TYPE_FOLLOWING) {
			return followingList.size();
		} else {
			return 0;
		}
	}

	@Override
	public Object getItem(int position) {
		if(adapterType == TYPE_FANS) {
			return fansList.get(position);
		} else if(adapterType == TYPE_FOLLOWING) {
			return followingList.get(position);
		} else {
			return null;
		}
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
}
