package com.sportxast.SportXast.adapter2_0;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sportxast.SportXast.R;
import com.sportxast.SportXast.models._SearchHashtag;

import java.util.ArrayList;

public class SearchHashtagAdapter extends BaseAdapter{
	
	Context context;
	ArrayList<_SearchHashtag> lists_hashtag = new ArrayList<_SearchHashtag>();
	
	public SearchHashtagAdapter(Context context,ArrayList<_SearchHashtag> lists_hashtag) {
		// TODO Auto-generated constructor stub
		
		this.context= context;
		this.lists_hashtag = lists_hashtag;
	}
	
	static class ItemHolder{
		//ImageView imgvw_photo;
		TextView txtvw_title;
//		TextView txtvw_subtitle;
//		TextView txtvw_date;
//		ImageView imgvw_sportsicon;
//		TextView txtvw_isToday;
		
		
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return lists_hashtag.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return lists_hashtag.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int arg0, View convertView, ViewGroup arg2) {
		// TODO Auto-generated method stub
		ItemHolder itemHolder;
		
		if(convertView==null){
			convertView = ((Activity)context).getLayoutInflater().inflate(R.layout.list_item_information, null);
			
			itemHolder = new ItemHolder();
		//	itemHolder.imgvw_photo = (ImageView)convertView.findViewById(R.id.imgvw_events_photo);
			itemHolder.txtvw_title = (TextView)convertView.findViewById(R.id.txtvw_listabout);
//			itemHolder.txtvw_subtitle = (TextView)convertView.findViewById(R.id.txtvw_events_subtitle);
//			itemHolder.txtvw_date = (TextView)convertView.findViewById(R.id.txtvw_events_date);
//			itemHolder.imgvw_sportsicon = (ImageView)convertView.findViewById(R.id.imgvw_events_sportsicon);
//			itemHolder.txtvw_isToday = (TextView) convertView.findViewById(R.id.imgvw_events_isToday);
//			
			
			
			convertView.setTag(itemHolder);
			
			
		}else{
			
			itemHolder = (ItemHolder)convertView.getTag();
		}
		
		_SearchHashtag hashtag = lists_hashtag.get(arg0);
		itemHolder.txtvw_title.setText("#"+hashtag.name);
		
		return convertView;
	}
	
	

}
