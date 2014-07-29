package com.sportxast.SportXast.adapter2_0;

import android.app.Activity;
import android.content.Context;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sportxast.SportXast.R;
import com.sportxast.SportXast.models._SearchEventLists;

import java.util.ArrayList;

public class SearchResultAdapter extends BaseAdapter{
	
	private Context context;
	private ArrayList<Object> lists;
	private String type = "";
	public SearchResultAdapter(Context context,String type,ArrayList<Object> lists) {
		// TODO Auto-generated constructor stub
		
		this.context = context;
		this.type = type;
		this.lists = lists;
		
		Log.e("searchreasult", "result "+lists.size());
	}
	static class EventHolder {
		ImageView img_avatar;
		ImageView img_cal;
		TextView txt_title;
		TextView txt_subtitle;
		TextView txt_date;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return lists.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return lists.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		
		if(type.equals("event")) return searchEventView(arg1,arg0);
		else if(type.equals("user")) return searchUserView(arg1,arg0);
		else if(type.equals("hashtag")) return searchHashtagView(arg1,arg0); 
		
		return null;
	}
	
	public View searchEventView(View convertView,int pos){
		EventHolder headerHolder;
		if(convertView==null){
			LayoutInflater inflater = ((Activity)context).getLayoutInflater();
			convertView = inflater.inflate(R.layout.list_item_search, null);
			
			headerHolder = new EventHolder();
			
			headerHolder.img_avatar = (ImageView)convertView.findViewById(R.id.imgvw_avatar_events);
			headerHolder.img_cal = (ImageView)convertView.findViewById(R.id.imgvw_cal);
			headerHolder.txt_title = (TextView)convertView.findViewById(R.id.txtvw_title);
			headerHolder.txt_subtitle = (TextView)convertView.findViewById(R.id.txtvw_subtitle);
			headerHolder.txt_date = (TextView)convertView.findViewById(R.id.txtvw_event_date);
			
					
			convertView.setTag(headerHolder);
			
		}
		else{
			headerHolder = (EventHolder)convertView.getTag();
		}
		
		_SearchEventLists searchEventLists= (_SearchEventLists)lists.get(pos);
		
		String logo = searchEventLists.event.eSport.sportLogo;
		String packageName = logo.substring(logo.lastIndexOf("sport/"), logo.indexOf(".png")).replace("sport/", "");
		packageName = packageName.replace(" ", "_");
		int id = context.getResources().getIdentifier(packageName, "drawable", context.getPackageName());
		int color = context.getResources().getColor(R.color.uni_orange);
		Mode mMode = Mode.SRC_ATOP;
		
		Drawable d ;
		try {
			d= context.getResources().getDrawable(id);
		} catch (Exception e) {
			// TODO: handle exception
			d = context.getResources().getDrawable(R.drawable.ic_istoday);
		}
		
		d.setColorFilter(color,mMode);
		headerHolder.img_avatar.setImageDrawable(d);
		
		
		
		headerHolder.txt_title.setText(searchEventLists.event.eventLocation);
		
		headerHolder.txt_subtitle.setText("");
		for (String str : searchEventLists.event.eventTags) {
			headerHolder.txt_subtitle.append(str+" ");
		}
		
//		if(Integer.parseInt(searchEventLists.event.eventIsToday)==1){
//			itemHolder.imgvw_isToday.setVisibility(View.VISIBLE);
//			itemHolder.txtvw_date.setVisibility(View.GONE);
//		}
//		else{
//			itemHolder.imgvw_isToday.setVisibility(View.GONE);
//			itemHolder.txtvw_date.setVisibility(View.VISIBLE);
//			itemHolder.txtvw_date.setText(eventLists.eventLists.get(pos).eventStartDateShort);
//		}
		headerHolder.txt_date.setText(searchEventLists.event.eventStartDateShort);
		
//		convertView.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				Intent intent = new Intent(context, EventsMediaActivity.class);
//				intent.putExtra("eventId",searchEventLists.event.eventId);
//				intent.putExtra("eventLocation", searchEventLists.event.eventLocation);
//				intent.putExtra("sportLogo", searchEventLists.event.eSport.sportLogo);
//				
//				
//			}
//		});
//		
		return convertView;
		}
	public View searchUserView(View convertView,int pos){
		
		
		
		
		return convertView;
		}
	public View searchHashtagView(View convertView,int pos){ return convertView;}
	
}
