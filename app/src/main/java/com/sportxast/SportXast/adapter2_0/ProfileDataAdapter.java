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
import com.sportxast.SportXast.models._Profile;
import com.sportxast.SportXast.thirdparty_class.Async_HttpClient;

import org.json.JSONObject;

import java.util.ArrayList;
//import com.sportxast.SportXast.BaseSherlockActivity;

public class ProfileDataAdapter extends BaseAdapter{
	
	Async_HttpClient async_HttpClient;
	
	Context context;
	//_Follower_Following list_follower = new _Follower_Following(); 
	private ArrayList<_Profile> FArrFollowersList;
	
	public void updateListElements( ArrayList<_Profile> arrFollowersList) {
		this.FArrFollowersList = arrFollowersList;
		//this.FArrMediaList = ( (Highlight_Activity) context ).FArrMediaList;
        //Triggers the list update  
	   notifyDataSetChanged(); 
    }
	   
	private int FTabType;
	/** tabType: 1-followers, 2-following **/
	public ProfileDataAdapter(Context context, ArrayList<_Profile> arrFollowersList, final int tabType) {
		// TODO Auto-generated constructor stub
		
		this.context= context;
		this.FArrFollowersList = arrFollowersList;
		async_HttpClient = new Async_HttpClient(this.context);
		
		this.FTabType = tabType;
	 	}
	
	static class ItemHolder{
		TextView txtvw_sportname;
		TextView txtvw_events_title0;
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
		return this.FArrFollowersList.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return this.FArrFollowersList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(final int itemPosition, View convertView, final ViewGroup parent) {
		// TODO Auto-generated method stub
		ItemHolder itemHolder;
		 
		if(convertView==null){
			convertView = ((Activity)context).getLayoutInflater().inflate(R.layout.list_item_events, null); 
			itemHolder  = new ItemHolder();
			 
			itemHolder.txtvw_sportname 		= (TextView)convertView.findViewById(R.id.txtvw_sportname);
			itemHolder.txtvw_events_title0 	= (TextView)convertView.findViewById(R.id.txtvw_events_title0);
			
			itemHolder.imgvw_photo 		= (ImageView)convertView.findViewById(R.id.imgvw_events_photo);
			itemHolder.txtvw_title 		= (TextView)convertView.findViewById(R.id.txtvw_events_title);
			itemHolder.txtvw_subtitle 	= (TextView)convertView.findViewById(R.id.txtvw_events_subtitle);
			itemHolder.txtvw_date 		= (TextView)convertView.findViewById(R.id.txtvw_events_date);
			itemHolder.imgvw_sportsicon = (ImageView)convertView.findViewById(R.id.imgvw_events_sportsicon);
			itemHolder.txtvw_isToday 	= (TextView) convertView.findViewById(R.id.imgvw_events_isToday); 
			convertView.setTag(itemHolder);
			
		}else{
			itemHolder = (ItemHolder)convertView.getTag();
		}
		 
		_Profile xx = this.FArrFollowersList.get(itemPosition);
		
		itemHolder.txtvw_sportname.setVisibility(View.GONE);
		itemHolder.txtvw_events_title0.setVisibility(View.GONE);
		 
		final _Profile follower = this.FArrFollowersList.get(itemPosition);
		 
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
				//requestParams.put("followingID", CommonFunctions_1.sharedPreferences.getString(KEY_PROFILE.USER_ID, ""));
				 
				requestParams.put("followingID", follower.userId); 
				
				async_HttpClient.POST(appendURL, requestParams, new JsonHttpResponseHandler(){
					  
						@Override
						public void onSuccess(int statusCode, JSONObject response) {
							// TODO Auto-generated method stub
							super.onSuccess(statusCode, response);
							
							Toast.makeText(context,""+appendURL+"ed successfully.", Toast.LENGTH_SHORT).show(); 
							if (Integer.parseInt(follower.isFollowing) == 0){
								FArrFollowersList.get(itemPosition).isFollowing="1";
							}else{
								FArrFollowersList.get(itemPosition).isFollowing="0";
								
								if(FTabType == 2){// remove from listview
									//Toast.makeText(context,"yox", Toast.LENGTH_SHORT).show();
									FArrFollowersList.remove( itemPosition ); 
								}
							}
							  
							notifyDataSetChanged();
					 		//parent.invalidate();
						}
					
				});
			}
		});
		 
		/*
		if( (follower.userId.length() 		<= 0) &&
			(follower.avatarPath.length() 	<= 0) &&
			(follower.avatarUrl.length() 	<= 0) && 
			(follower.fullName.length() 	<= 0) &&
			(follower.displayName.length() 	<= 0) &&
			(follower.userName.length() 	<= 0) ){
			convertView.setVisibility(View.INVISIBLE);
		}
		 */
		
		/*
		if(follower.userId.equals( GlobalVariablesHolder.X_USER_ID )){//If SELF
			convertView.setVisibility(View.GONE);
		}*/
		
		return convertView;
	}
	
	

}
