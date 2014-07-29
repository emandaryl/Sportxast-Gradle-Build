package com.sportxast.SportXast.adapter2_0;

//import com.sportxast.SportXast.BaseSherlockActivity;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sportxast.SportXast.Global_Data;
import com.sportxast.SportXast.R;
import com.sportxast.SportXast.activities2_0.ChooseASport_Activity;
import com.sportxast.SportXast.models._SportsList;
import com.sportxast.SportXast.models._SportsList.Sports;
import com.sportxast.SportXast.thirdparty_class.SectionAdapter;

public class ChooseASportListAdapter extends SectionAdapter{

	Global_Data global_Data;
	Context context;
	_SportsList sportsList;
	public ChooseASportListAdapter(Context context,_SportsList sportsList) {
		// TODO Auto-generated constructor stub
		
		this.context = context;
		this.sportsList = sportsList;
		
		global_Data = (Global_Data)this.context.getApplicationContext();
	}
	
	
	static class HeaderHolder {
		
		TextView txt_title;
		
	}

	static class ItemHolder {
		ImageView imgvw_sportlogo;
		TextView txtvw_sportname;
		TextView txtvw_sportsubname;

	}

	
	
	@Override
	public int numberOfSections() {
		// TODO Auto-generated method stub
		return sportsList.letters.size();
	}

	@Override
	public int numberOfRows(int section) {
		// TODO Auto-generated method stub
		return sportsList.sports.get(section).sportsInLetter.size();
	}

	@Override
	public View getRowView(final int section, final int row, View convertView,
			ViewGroup parent) {
		// TODO Auto-generated method stub
		
		ItemHolder itemHolder;
		
		if(convertView==null){
			LayoutInflater inflater = ((Activity)context).getLayoutInflater();
			convertView = inflater.inflate(R.layout.list_item_event_add, null);
			
			itemHolder = new ItemHolder();
			
			itemHolder.txtvw_sportname = (TextView)convertView.findViewById(R.id.txtvw_sportname);
			itemHolder.txtvw_sportsubname = (TextView)convertView.findViewById(R.id.txtvw_sportsubname);
			itemHolder.imgvw_sportlogo = (ImageView)convertView.findViewById(R.id.imgvw_sportlogo);
			
					
			convertView.setTag(itemHolder);
			
		}
		else{
			itemHolder = (ItemHolder)convertView.getTag();
		}
		 
		itemHolder.txtvw_sportsubname.setVisibility(View.GONE);
		itemHolder.txtvw_sportname.setText(sportsList.sports.get(section).sportsInLetter.get(row).sportName);
		String logo = sportsList.sports.get(section).sportsInLetter.get(row).sportLogo;
		String packageName = logo.substring(logo.lastIndexOf("sport/"), logo.indexOf(".png")).replace("sport/", "");
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
		itemHolder.imgvw_sportlogo.setImageDrawable(d);
	
		
		convertView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Sports sports= sportsList.sports.get(section).sportsInLetter.get(row);
				
				global_Data.getNewEvent().sportId = sports.sportId;
				global_Data.getNewEvent().sportName = sports.sportName;
				global_Data.getNewEvent().sportLogo = sports.sportLogo;
				global_Data.getNewEvent().sportFirstLetter = sports.sportFirstLetter;
				 
				if( context instanceof ChooseASport_Activity ){
					( (ChooseASport_Activity)context ).exitActivity( sports );
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
		return headerSectionView(sportsList.letters.get(section));
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
