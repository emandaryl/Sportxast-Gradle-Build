package com.sportxast.SportXast.adapter2_0;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sportxast.SportXast.Global_Data;
import com.sportxast.SportXast.R;
import com.sportxast.SportXast.activities2_0.ChooseAVenue_Activity;
import com.sportxast.SportXast.models._Venue;
import com.sportxast.SportXast.thirdparty_class.SectionAdapter;

import java.util.ArrayList;
//import com.sportxast.SportXast.BaseSherlockActivity;

public class ChooseAVenueListAdapter extends SectionAdapter{

	Global_Data global_Data;
	Context context;
	ArrayList<_Venue> lists_venue = new ArrayList<_Venue>();
	public ChooseAVenueListAdapter(Context context,ArrayList<_Venue> lists_venue) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.lists_venue = lists_venue;
		
		global_Data = (Global_Data)this.context.getApplicationContext();
	}
	
	
	static class HeaderHolder {
		
		TextView txt_title;
		
	}

	static class ItemHolder {
		ImageView imgvw_next;
		TextView txtvw_title;

	}

	
	
	@Override
	public int numberOfSections() {
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public int numberOfRows(int section) {
		// TODO Auto-generated method stub
		return lists_venue.size();
	}

	@Override
	public View getRowView(int section, final int row, View convertView,
			ViewGroup parent) {
		// TODO Auto-generated method stub
		
		ItemHolder itemHolder;
		
		if(convertView==null){
			LayoutInflater inflater = ((Activity)context).getLayoutInflater();
			convertView = inflater.inflate(R.layout.list_item_information, null);
			
			itemHolder = new ItemHolder();
			
			itemHolder.imgvw_next = (ImageView)convertView.findViewById(R.id.imgvw_next);
			itemHolder.txtvw_title = (TextView)convertView.findViewById(R.id.txtvw_listabout);
			
			
					
			convertView.setTag(itemHolder);
			
		}
		else{
			itemHolder = (ItemHolder)convertView.getTag();
		}
		_Venue venue = lists_venue.get(row);
		itemHolder.imgvw_next.setVisibility(View.INVISIBLE);
		itemHolder.txtvw_title.setText(venue.placeName);
		
		
		
		convertView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				_Venue venue = lists_venue.get(row);

				global_Data.getNewEvent().venueId = venue.placeId;
				global_Data.getNewEvent().venueName = venue.placeName;
				global_Data.getNewEvent().venueAddress = venue.placeAddr;
				global_Data.getNewEvent().venueLatitude = venue.placeLatitude;
				global_Data.getNewEvent().venueLongitude = venue.placeLongitude;
				
				
				if( context instanceof ChooseAVenue_Activity ){
					( (ChooseAVenue_Activity)context ).exitActivity( venue );
				}
				
				
				//((Activity)context).finish();
			}
		});
		
		
		return convertView;
	}

	@Override
	public Object getRowItem(int section, int row) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public boolean hasSectionHeaderView(int section) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public int getSectionHeaderViewTypeCount() {
		// TODO Auto-generated method stub
		return 2;
	}

	@Override
	public int getSectionHeaderItemViewType(int section) {
		// TODO Auto-generated method stub
		return section % 2;
	}

	@Override
	public View getSectionHeaderView(final int section, View convertView,
			ViewGroup parent) {
		// TODO Auto-generated method stub
//		if(numberOfSections()==0){
//			return null;
//		}
//		
//		HeaderHolder headerHolder;
//		if(convertView==null){
//			LayoutInflater inflater = ((Activity)context).getLayoutInflater();
//			convertView = inflater.inflate(R.layout.list_header_addevents, null);
//			
//			headerHolder = new HeaderHolder();
//	
//			headerHolder.txt_title = (TextView)convertView.findViewById(R.id.txtvw_header);
//			
//					
//			convertView.setTag(headerHolder);
//			
//		}
//		else{
//			headerHolder = (HeaderHolder)convertView.getTag();
//		}
//		
//		headerHolder.txt_title.setText(sportsList.letters.get(section));
//		
		return headerSectionView("NEARBY");
	}
	
	
	public View headerSectionView(String s) {

		TextView header = new TextView(context);
		header.setBackgroundResource(R.drawable.layerlist_lightgreyshapewithlinebelow);
		// header.setLayoutParams(new
		// LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
		header.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
		header.setTextColor(Color.parseColor("#444444"));
		header.setPadding(8, 2, 2, 2);
		header.setText(s);
		return header;
	}


}
