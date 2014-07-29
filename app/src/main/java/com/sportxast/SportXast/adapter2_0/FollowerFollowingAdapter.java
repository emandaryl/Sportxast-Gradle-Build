package com.sportxast.SportXast.adapter2_0;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sportxast.SportXast.R;
import com.sportxast.SportXast.commons.GlobalVariablesHolder;
import com.sportxast.SportXast.models._Follower_Following;
import com.sportxast.SportXast.models._Follower_Following.Follow;
import com.sportxast.SportXast.thirdparty_class.Async_HttpClient;

import org.json.JSONObject;
//import com.sportxast.SportXast.BaseSherlockActivity;

public class FollowerFollowingAdapter extends BaseAdapter{
	
	Async_HttpClient async_HttpClient;
	
	Context context;
	_Follower_Following list_follower = new _Follower_Following();
	
	public FollowerFollowingAdapter(Context context,_Follower_Following list_follower) {
		// TODO Auto-generated constructor stub
		
		this.context= context;
		this.list_follower = list_follower;
		async_HttpClient = new Async_HttpClient(this.context);
	 	}
	
	static class ItemHolder{
		ImageView imgvw_photo;
		TextView txtvw_title;
		TextView txtvw_subtitle;
		TextView txtvw_date;
		ImageView imgvw_sportsicon;
		TextView txtvw_isToday;
		
		
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list_follower.Follow.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return list_follower.Follow.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(final int arg0, View convertView, final ViewGroup parent) {
		// TODO Auto-generated method stub
		ItemHolder itemHolder;
		
		if(convertView==null){
			convertView = ((Activity)context).getLayoutInflater().inflate(R.layout.list_item_events, null);
			
			itemHolder = new ItemHolder();
			itemHolder.imgvw_photo = (ImageView)convertView.findViewById(R.id.imgvw_events_photo);
			itemHolder.txtvw_title = (TextView)convertView.findViewById(R.id.txtvw_events_title);
			itemHolder.txtvw_subtitle = (TextView)convertView.findViewById(R.id.txtvw_events_subtitle);
			itemHolder.txtvw_date = (TextView)convertView.findViewById(R.id.txtvw_events_date);
			itemHolder.imgvw_sportsicon = (ImageView)convertView.findViewById(R.id.imgvw_events_sportsicon);
			itemHolder.txtvw_isToday = (TextView) convertView.findViewById(R.id.imgvw_events_isToday);
			 
			convertView.setTag(itemHolder);
			
			
		}else{
			
			itemHolder = (ItemHolder)convertView.getTag();
		}
		
		final Follow follower = list_follower.Follow.get(arg0);
		
		
			itemHolder.txtvw_title.setText(follower.fullName+"\n"+follower.userName);

		
		itemHolder.txtvw_subtitle.setText(follower.aboutMe);
		
		if(Integer.parseInt(follower.hasAvatar)>0){
			AQuery aQuery = new AQuery(context);
			aQuery.id(itemHolder.imgvw_photo).image(follower.avatarUrl, false, true);
		}else{
			itemHolder.imgvw_photo.setImageResource(R.drawable.ic_avatar_profile);
		}
		
		
		
		itemHolder.txtvw_date.setVisibility(View.INVISIBLE);
		itemHolder.imgvw_sportsicon.setVisibility(View.INVISIBLE);
		//itemHolder.txtvw_isToday.setVisibility(View.INVISIBLE);
		
		
		

		
		if (Integer.parseInt(follower.isFollowing) == 0){
			itemHolder.txtvw_isToday.setText("Follow");
			itemHolder.txtvw_isToday.setTextColor(context.getResources().getColor(R.color.uni_blue));
			itemHolder.txtvw_isToday.setBackgroundResource(R.drawable.shape_white_withblueborder);
			}
		else{
			itemHolder.txtvw_isToday.setText("Following");
			itemHolder.txtvw_isToday.setTextColor(context.getResources().getColor(R.color.white));
			itemHolder.txtvw_isToday.setBackgroundResource(R.drawable.shape_blue_withblueborder);
		}
		 
		itemHolder.txtvw_isToday.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				 final String appendURL = Integer.parseInt(follower.isFollowing) == 0?"Follow":"Unfollow";
				RequestParams requestParams = new RequestParams();
				//requestParams.put("followingID", ((BaseSherlockActivity)context).sharedPreferences.getString(KEY_PROFILE.USER_ID, ""));
				requestParams.put("followingID", GlobalVariablesHolder.X_USER_ID);
				
				
				async_HttpClient.POST(appendURL, requestParams, new JsonHttpResponseHandler(){
					
						@Override
						public void onSuccess(int statusCode, JSONObject response) {
							// TODO Auto-generated method stub
							super.onSuccess(statusCode, response);
							
							Toast.makeText(context,""+appendURL+" successfully.", Toast.LENGTH_SHORT).show();
							
							
							if (Integer.parseInt(follower.isFollowing) == 0){
								list_follower.Follow.get(arg0).isFollowing="1";
							}else{
								list_follower.Follow.get(arg0).isFollowing="0";
							}
							
							FollowerFollowingAdapter.this.notifyDataSetChanged();
					 		 parent.invalidate();
						}
					
				});
			}
		});
		
		
		return convertView;
	}
	
	

}
