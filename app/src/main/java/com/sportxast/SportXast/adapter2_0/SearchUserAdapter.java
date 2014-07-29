package com.sportxast.SportXast.adapter2_0;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sportxast.SportXast.R;
import com.sportxast.SportXast.models._MediaLists.UserInFavorites;
import com.sportxast.SportXast.models._SearchUser;
import com.sportxast.SportXast.thirdparty_class.Async_HttpClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
//import com.sportxast.SportXast.BaseSherlockActivity;

public class SearchUserAdapter extends BaseAdapter{
	//private Async_HttpClient async_HttpClient;
	private Context context;
	private ArrayList<_SearchUser> FUserList = new ArrayList<_SearchUser>();
	private String type="";
	 
	public void updateListElements(ArrayList<Object> userList){
		 //this.FUserList = userList;  
		this.FUserList = new ArrayList<_SearchUser>();
 
		for (int i = 0; i < userList.size(); i++) {
		   _SearchUser user = new _SearchUser();
		   if(type.equals("user")){
		    user = ( _SearchUser)userList.get(i);
		   }else if(type.equals("fans")){
			    
			   
			    user = setSearchUserModel( (UserInFavorites)userList.get(i) );
			   }
			   this.FUserList.add(user);
		  } 
 
		 notifyDataSetChanged();
	} 
	
	public SearchUserAdapter(Context context,String type,ArrayList<Object> userList) {
		  // TODO Auto-generated constructor stub
		  
		this.context= context;
		this.type = type;
  
		for (int i = 0; i < userList.size(); i++) {
		   _SearchUser user = new _SearchUser();
		   if(type.equals("user")){
		    user = ( _SearchUser)userList.get(i);
		   }else if(type.equals("fans")){
		    user = setSearchUserModel((UserInFavorites)userList.get(i));
		   }
		   this.FUserList.add(user);
		}  
	}
	/*
	public SearchUserAdapter(Context context, String type, ArrayList<Object> arrUserList) {
		// TODO Auto-generated constructor stub
		
		this.context= context;
		this.type = type;
		
		//async_HttpClient = new Async_HttpClient(this.context);
		CommonFunctions_1.initSharedPreferences(this.context);
		//((Activity)this.context).initSharedPreferences(this.context);
		
		
		for (int i = 0; i < FUserList.size(); i++) {
			_SearchUser user = new _SearchUser();
			if(type.equals("user")){
				user = ( _SearchUser)FUserList.get(i);
			}else if(type.equals("fans")){
				user = setSearchUserModel((UserInFavorites)arrUserList.get(i));
			}
			this.FUserList.add(user);
		} 
	}
	
	*/
	
	static class ItemHolder{
		ImageView imgvw_photo;
		TextView txtvw_title;
		TextView txtvw_title0;
		TextView txtvw_subtitle;
		TextView txtvw_date;
		ImageView imgvw_sportsicon;
		TextView txtvw_isToday;
		
		
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return FUserList.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return FUserList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(final int arg0, View convertView, final ViewGroup parent) {
		// TODO Auto-generated method stub
		final ItemHolder itemHolder;
		
		if(convertView==null){
			convertView = ((Activity)context).getLayoutInflater().inflate(R.layout.list_item_events, null);
			
			itemHolder = new ItemHolder();
			itemHolder.imgvw_photo = (ImageView)convertView.findViewById(R.id.imgvw_events_photo);
			itemHolder.txtvw_title = (TextView)convertView.findViewById(R.id.txtvw_events_title);
			itemHolder.txtvw_title0 = (TextView)convertView.findViewById(R.id.txtvw_events_title0);
			itemHolder.txtvw_subtitle = (TextView)convertView.findViewById(R.id.txtvw_events_subtitle);
			itemHolder.txtvw_date = (TextView)convertView.findViewById(R.id.txtvw_events_date);
			itemHolder.imgvw_sportsicon = (ImageView)convertView.findViewById(R.id.imgvw_events_sportsicon);
			itemHolder.txtvw_isToday = (TextView) convertView.findViewById(R.id.imgvw_events_isToday);
			
			//itemHolder.txtvw_isToday.setPadding(16, 4, 16, 4);
			
			convertView.setTag(itemHolder);
			   
		}else{
			
			itemHolder = (ItemHolder)convertView.getTag();
		}
		
		 _SearchUser user = FUserList.get(arg0);
		
//		if(type.equals("user")){
//			user = ( _SearchUser)FUserList.get(arg0);
//		}else if(type.equals("fans")){
//			user = setSearchUserModel((UserInFavorites)FUserList.get(arg0));
//		}
		
		if(user.fullName.length()>0 && user.userName.length()==0){
			itemHolder.txtvw_title0.setVisibility(View.GONE);
			itemHolder.txtvw_title.setText(user.fullName);
		}
		else if(user.fullName.length()==0 && user.userName.length()>0){
			itemHolder.txtvw_title0.setVisibility(View.GONE);
			itemHolder.txtvw_title.setText(user.userName);
		}
		else if(user.fullName.length()>0 && user.userName.length()>0){
			itemHolder.txtvw_title0.setVisibility(View.VISIBLE);
			itemHolder.txtvw_title0.setText(user.fullName);
			itemHolder.txtvw_title.setText(user.userName);
		}else if(user.displayName.length()>0 ){
			itemHolder.txtvw_title0.setVisibility(View.GONE);
			itemHolder.txtvw_title.setText(user.displayName);
		}else{
			itemHolder.txtvw_title0.setVisibility(View.GONE);
			itemHolder.txtvw_title.setVisibility(View.GONE);
		}
		itemHolder.txtvw_subtitle.setText(user.aboutMe);
		
		if(user.avatarPath.length()>0){
			Drawable avatar = context.getResources().getDrawable(R.drawable.ic_avatar_profile);
			AQuery aQuery = new AQuery(context);
			aQuery.id(itemHolder.imgvw_photo).image(user.avatarPath, false, true, avatar.getMinimumWidth(), R.drawable.ic_avatar_profile);
		}else
			itemHolder.imgvw_photo.setImageResource(R.drawable.ic_avatar_profile);
		
		
		
		if (Integer.parseInt(user.isFollowing) == 0)
			itemHolder.txtvw_isToday.setText("Follow");
		else
			itemHolder.txtvw_isToday.setText("Following");

		
		itemHolder.txtvw_date.setVisibility(View.INVISIBLE);
		itemHolder.imgvw_sportsicon.setVisibility(View.INVISIBLE);
		
		
		
		
		
		if (Integer.parseInt(user.isFollowing) == 0){
			itemHolder.txtvw_isToday.setText("Follow");
			itemHolder.txtvw_isToday.setTextColor(context.getResources().getColor(R.color.uni_blue));
			itemHolder.txtvw_isToday.setBackgroundResource(R.drawable.shape_white_withblueborder);
			}
		else{
			itemHolder.txtvw_isToday.setText("Following");
			itemHolder.txtvw_isToday.setTextColor(context.getResources().getColor(R.color.white));
			itemHolder.txtvw_isToday.setBackgroundResource(R.drawable.shape_blue_withblueborder);
		}
		
		
		final _SearchUser user2 = user;
	
		
		itemHolder.txtvw_isToday.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				 
				final String appendURL = Integer.parseInt(user2.isFollowing) == 0?"Follow":"Unfollow";
				RequestParams requestParams = new RequestParams();
				requestParams.put("followingID",user2.userId );
				
				Log.e(appendURL, requestParams.toString());
				
				Async_HttpClient async_HttpClient = new Async_HttpClient(context);
				async_HttpClient.POST(appendURL, requestParams, new JsonHttpResponseHandler(){
					
						@Override
						public void onSuccess(int statusCode, JSONObject response) {
							// TODO Auto-generated method stub
							super.onSuccess(statusCode, response);
							
							Log.e(appendURL, response.toString());
							//Toast.makeText(context,""+appendURL+" successfully.", Toast.LENGTH_SHORT).show();
							boolean isSuccess = false;
							try {
							 isSuccess= response.getInt("success")==1?true:false;
							} catch (JSONException e) {e.printStackTrace();}
							 
							
							if (Integer.parseInt(user2.isFollowing) == 0){
								FUserList.get(arg0).isFollowing="1";
//								itemHolder.txtvw_isToday.setText("Follow");
//								itemHolder.txtvw_isToday.setTextColor(context.getResources().getColor(R.color.uni_blue));
//								itemHolder.txtvw_isToday.setBackgroundResource(R.drawable.shape_white_withblueborder);
								}
							else{
								FUserList.get(arg0).isFollowing="0";
//								itemHolder.txtvw_isToday.setText("Following");
//								itemHolder.txtvw_isToday.setTextColor(context.getResources().getColor(R.color.white));
//								itemHolder.txtvw_isToday.setBackgroundResource(R.drawable.shape_blue_withblueborder);
							}
							
							notifyDataSetChanged();
							parent.invalidate();
						}
					
				});
			}
		});
		
		return convertView;
	}
	
	public  _SearchUser setSearchUserModel(UserInFavorites fans ){
		_SearchUser searchUser = new _SearchUser();
		
		searchUser.userId = fans.userId;
		searchUser.userName = fans.userName;
		searchUser.fullName = fans.fullName;
		searchUser.aboutMe = fans.aboutMe;
		searchUser.avatarPath = fans.avatarPath;
		searchUser.isFollowing = fans.isFollowing;
		searchUser.displayName = fans.displayName;
		
		return searchUser;
		
	}
	
	

}
