package com.sportxast.SportXast.adapter2_0;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.sportxast.SportXast.R;
import com.sportxast.SportXast.SportX2_Main;
import com.sportxast.SportXast.Tutorial_Activity;
import com.sportxast.SportXast.activities2_0.Create_Activity;
import com.sportxast.SportXast.activities2_0.Information_Activity;
import com.sportxast.SportXast.activities2_0.Menu_Activity;
import com.sportxast.SportXast.activities2_0.Profile_Activity;
import com.sportxast.SportXast.activities2_0.Search_Activity;
import com.sportxast.SportXast.activities2_0.Share_Class;
import com.sportxast.SportXast.activities2_0.VideoCaptureActivity;
import com.sportxast.SportXast.commons.Constants;
import com.sportxast.SportXast.thirdparty_class.SectionAdapter;

import java.util.ArrayList;
//import com.sportxast.SportXast.BaseSherlockActivity;

public class MenuAdapter extends SectionAdapter {
	private Context context;
	
	String[] header = new String[] {};
 
	ArrayList<ContentValues> list_events = new ArrayList<ContentValues>();
	ArrayList<ContentValues> list_others = new ArrayList<ContentValues>();

	public MenuAdapter(Context context,String[] header,ArrayList<ContentValues> list_events,ArrayList<ContentValues> list_others) {
		// TODO Auto-generated constructor stub
		this.context = context;
		
		this.header = header;
		this.list_events = list_events;
		this.list_others = list_others;
	}
 
	static class ItemHolder {
		ImageView imgvw_icon;
		TextView txtvw_title;
	}

	@Override
	public int numberOfSections() {
		// TODO Auto-generated method stub
		return header.length;
	}

	@Override
	public int numberOfRows(int section) {
		// TODO Auto-generated method stub

		if (section == 0)
				return list_events.size();
		else 	return list_others.size();

		//return 0;
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
	public View getSectionHeaderView(int section, View convertView,
			ViewGroup parent) {
		// TODO Auto-generated method stub
		return headerSectionView(header[section]);
	}

	@Override
	public View getRowView(int section, int row, View convertView,
			ViewGroup parent) {
		// TODO Auto-generated method stub

		ItemHolder itemHolder;

		if (convertView == null) {
			convertView = ((Activity) context).getLayoutInflater().inflate( R.layout.list_item_menu, null);

			itemHolder = new ItemHolder();
			itemHolder.imgvw_icon = (ImageView) convertView.findViewById(R.id.imgvw_menu_icon);
			itemHolder.txtvw_title = (TextView) convertView.findViewById(R.id.txtvw_menu_title);

			convertView.setTag(itemHolder);

		} else {
			itemHolder = (ItemHolder) convertView.getTag();
		}
		//try {
			if (section == 0) {
				itemHolder.imgvw_icon.setImageResource(list_events.get(row).getAsInteger("icon"));
				itemHolder.txtvw_title.setText(list_events.get(row).getAsString("title"));
			} else if (section == 1) {
				itemHolder.imgvw_icon.setImageResource(list_others.get(row).getAsInteger("icon"));
				itemHolder.txtvw_title.setText(list_others.get(row).getAsString("title"));
			}
//
//		} catch (Exception e) {
//			// TODO: handle exception
//		}
		
		return convertView;
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
	public void onRowItemClick(AdapterView<?> parent, View view, int section,
			int row, long id) {
		// TODO Auto-generated method stub
		super.onRowItemClick(parent, view, section, row, id);

		
		int callingActivityID = -1;
		if(  context instanceof Tutorial_Activity){ 
			callingActivityID = Constants.requestCode_Tutorial_Activity; 
		}else if(context instanceof VideoCaptureActivity){
    		callingActivityID = Constants.requestCode_VideoCapture_Activity; 
    	}else if(context instanceof Menu_Activity){
    		callingActivityID = Constants.requestCode_Menu_Activity;  
    	}
		 
//#####################################################################################
		
		if (section == 0) {
			switch (row) {
			case 0: {//home
				((Activity)context).finish();
			}
				break;
			case 1: {//search
				((Activity) context).startActivity(new Intent( context, Search_Activity.class));
			}
				break;
			case 2: {//create
				((Activity) context).startActivity(new Intent( context, Create_Activity.class));
			}
				break;
			case 3: {//capture
				//((Activity) context).startActivity(new Intent( context, VideoCaptureActivity.class)); 
				Intent intent = new Intent(context, VideoCaptureActivity.class); 
				//intent.putExtra("eventId", FEventId);
				intent.putExtra("callingActivityID", callingActivityID);
				context.startActivity(intent);  
			}
				break;
			case 4: {//popular
			 //	Toast.makeText(context, "POPULAR YEAH YEAH", Toast.LENGTH_LONG).show();
				((Activity) context).startActivity(new Intent( context, SportX2_Main.class).putExtra("type", "popular"));
			}
				break;
			case 5: {//nearby
				((Activity) context).startActivity(new Intent( context, SportX2_Main.class).putExtra("type", "nearby"));
			}
			break;
			
			case 6: {//favorite
				((Activity) context).startActivity(new Intent( context, SportX2_Main.class).putExtra("type", "favorite"));
			}
				break;
			default:
				break;
			}
		} else if (section == 1) {
			switch (row) {
			case 0: {//notification
				
			}
				break;
			case 1: {//share
				   
				//Toast.makeText(context, "SHARE YEAH YEAH", Toast.LENGTH_LONG).show();
					new Share_Class(context);
			}
				break;
			case 2: {//profile
				
				//((Activity) context).startActivity(new Intent( context, VideoCaptureActivity.class)); 
				Intent intent = new Intent(context, Profile_Activity.class); 
				//intent.putExtra("eventId", FEventId);
				intent.putExtra("callingActivityID", callingActivityID);
				context.startActivity(intent);   
			}
				break;
			case 3: {
				((Activity) context).startActivity(new Intent( context, Information_Activity.class));
			}
				break;
			
			default:
				break;
			}
		}

	}

	public View headerSectionView(String s) {
	
		TextView header = new TextView(context);
		header.setBackgroundResource(R.drawable.layerlist_lightgreyshapewithlinebelow);
		header.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
		header.setTextColor(Color.parseColor("#444444"));
		header.setPadding(8, 2, 2, 2);
		header.setText(s);
		return header;
	}

}
