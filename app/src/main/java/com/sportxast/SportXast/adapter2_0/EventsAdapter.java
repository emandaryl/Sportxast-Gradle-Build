package com.sportxast.SportXast.adapter2_0;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.sportxast.SportXast.Global_Data;
import com.sportxast.SportXast.R;
import com.sportxast.SportXast.commons.CommonFunctions_1;
import com.sportxast.SportXast.models._EventLists.EventLists;

import java.util.ArrayList;

public class EventsAdapter extends BaseAdapter{
	Context context;
	//_EventLists eventLists;
	ArrayList<EventLists> FArrEventLists; 
	private Global_Data global_Data;
	 
	public void updateListElements(ArrayList<EventLists> arrEventLists){
		 this.FArrEventLists = arrEventLists;
		 
		 notifyDataSetChanged();
	} 
	 
	public EventsAdapter(Context context, ArrayList<EventLists> arrEventLists) {
		// TODO Auto-generated constructor stub
		this.context 		= context;
		this.FArrEventLists = arrEventLists;
		global_Data 		= (Global_Data)context.getApplicationContext(); 
	} 
	
	static class ItemHolder{
		ImageView imgvw_photo;
		
//		private TextView txtvw_sportname;
		
		
		TextView txtvw_title;
		TextView txtvw_title0;
		TextView txtvw_subtitle;
		TextView txtvw_date;
		ImageView imgvw_sportsicon;
		TextView txtvw_isToday; 
		ProgressBar pbLoading_avatar;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return FArrEventLists.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return  FArrEventLists.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int pos, View convertView, ViewGroup viewgroup) {
		// TODO Auto-generated method stub
		

		ItemHolder itemHolder;
		
		if(convertView==null){
			convertView = ((Activity)context).getLayoutInflater().inflate(R.layout.list_item_events, null);
			
			itemHolder = new ItemHolder();
			itemHolder.imgvw_photo 		= (ImageView)convertView.findViewById(R.id.imgvw_events_photo);
//			itemHolder.txtvw_sportname  = (TextView)convertView.findViewById(R.id.txtvw_sportname);
			
			itemHolder.txtvw_title0 	= (TextView)convertView.findViewById(R.id.txtvw_events_title0);
			itemHolder.txtvw_title		= (TextView)convertView.findViewById(R.id.txtvw_events_title);
			
			itemHolder.txtvw_subtitle 	= (TextView)convertView.findViewById(R.id.txtvw_events_subtitle);
			itemHolder.txtvw_date 		= (TextView)convertView.findViewById(R.id.txtvw_events_date);
			itemHolder.imgvw_sportsicon = (ImageView)convertView.findViewById(R.id.imgvw_events_sportsicon);
			itemHolder.txtvw_isToday 	= (TextView) convertView.findViewById(R.id.imgvw_events_isToday);
			itemHolder.pbLoading_avatar = (ProgressBar) convertView.findViewById(R.id.pbLoading_avatar);
			
			
			convertView.setTag(itemHolder); 
		}else{
			
			itemHolder = (ItemHolder)convertView.getTag();
		}
		
		itemHolder.txtvw_title0.setVisibility(View.VISIBLE);
		itemHolder.txtvw_title.setVisibility(View.VISIBLE);
//		itemHolder.txtvw_sportname.setText( FArrEventLists.get(pos).eventSportName );
		 
		String eventFirstTeam_ 	= FArrEventLists.get(pos).eventFirstTeam.trim();
		String eventSecondTeam_ = FArrEventLists.get(pos).eventSecondTeam.trim();
		 
		if( (eventFirstTeam_ + eventSecondTeam_ ).length() <= 0 ){
			itemHolder.txtvw_title0.setVisibility(View.GONE); 
			itemHolder.txtvw_title.setText(FArrEventLists.get(pos).eventName);
		}else{
			
			if(eventFirstTeam_.length() <= 0)
				 itemHolder.txtvw_title0.setVisibility(View.GONE);
			else itemHolder.txtvw_title0.setText(FArrEventLists.get(pos).eventFirstTeam);
		
			if(eventSecondTeam_.length() <= 0)
				 itemHolder.txtvw_title.setVisibility(View.GONE);
			else itemHolder.txtvw_title.setText(FArrEventLists.get(pos).eventSecondTeam);
		}
		
		if( itemHolder.txtvw_title.getText().toString().trim().length() <= 0 ){
			itemHolder.txtvw_title.setText(FArrEventLists.get(pos).eventSportName +" @ "+ FArrEventLists.get(pos).eventLocation);
		}
		
		itemHolder.txtvw_subtitle.setText(FArrEventLists.get(pos).eventLocation); 
		  
		itemHolder.imgvw_sportsicon.setImageDrawable( CommonFunctions_1.getDrawableIcon(this.context, FArrEventLists.get(pos).eventSport.sportLogo) ); 
		itemHolder.txtvw_isToday.setVisibility(View.GONE);
		   
		itemHolder.txtvw_date.setTextColor(Color.parseColor("#444444") );
		if(FArrEventLists.get(pos).eventIsOpenString.equals("CHECK IN")){
			//itemHolder.txtvw_isToday.setVisibility(View.VISIBLE);
			//itemHolder.txtvw_date.setVisibility(View.GONE);
			itemHolder.txtvw_date.setText("CHECK IN");
			itemHolder.txtvw_date.setTextColor(Color.parseColor("#fc4c06") );
			
		}else if(FArrEventLists.get(pos).eventIsOpenString.equals("LIVE")){
			//itemHolder.txtvw_isToday.setVisibility(View.VISIBLE);
			//itemHolder.txtvw_date.setVisibility(View.GONE);
			itemHolder.txtvw_date.setText("LIVE");
		}
		else{
		//	itemHolder.txtvw_isToday.setVisibility(View.GONE);
		//	itemHolder.txtvw_date.setVisibility(View.VISIBLE);
			//itemHolder.txtvw_date.setText(FArrEventLists.get(pos).eventStartDateShort);
			itemHolder.txtvw_date.setText(FArrEventLists.get(pos).eventStartDateFormatted);
		} 
		
		if(FArrEventLists.get(pos).favoriteCountWithMedia.size() > 0){ 
			//new StreamImageTask( itemHolder.imgvw_photo, itemHolder.pbLoading_avatar  ).execute(FArrEventLists.get(pos).favoriteCountWithMedia.get(0).coverImageThumb);
			AQuery aQuery = new AQuery(convertView.getContext());
			aQuery.id(itemHolder.imgvw_photo).image(FArrEventLists.get(pos).favoriteCountWithMedia.get(0).coverImageThumb, true, true);
			
		}else{
			itemHolder.imgvw_photo.setImageResource(R.drawable.ic_capture_photo);
		}
	
		
		return convertView;
	}
	
}

