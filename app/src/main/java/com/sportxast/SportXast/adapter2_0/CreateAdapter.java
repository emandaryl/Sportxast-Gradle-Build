package com.sportxast.SportXast.adapter2_0;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sportxast.SportXast.Global_Data;
import com.sportxast.SportXast.Global_Data.Coordinate;
import com.sportxast.SportXast.R;
import com.sportxast.SportXast.activities2_0.ChooseASport_Activity;
import com.sportxast.SportXast.activities2_0.ChooseATeam_Activity;
import com.sportxast.SportXast.activities2_0.ChooseAVenue_Activity;
import com.sportxast.SportXast.models._RecentEvent;
import com.sportxast.SportXast.thirdparty_class.Async_HttpClient;
import com.sportxast.SportXast.thirdparty_class.SectionAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

//import com.sportxast.SportXast.BaseSherlockActivity;

public class CreateAdapter extends SectionAdapter {
	private Global_Data global_Data;
	private Async_HttpClient async_HttpClient;

	private Context context;
	private ArrayList<_RecentEvent> lists_recent = new ArrayList<_RecentEvent>();
	private _RecentEvent event = new _RecentEvent();
	private String[] header = new String[] {};
	private Coordinate coordinate;
	
	private int atIndex = 0;
	private boolean isRecent = false;

	public CreateAdapter(Context context,	ArrayList<_RecentEvent> lists_recent,Coordinate coordinate) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.lists_recent = lists_recent;
		this.coordinate = coordinate;
		global_Data = (Global_Data)this.context.getApplicationContext();
		async_HttpClient = new Async_HttpClient(this.context);
		 
		if(this.lists_recent.size()>0){
			header = new String[]{"SPORT","VENUE","TEAM","RECENT"};
		}else{
			header = new String[]{"SPORT","VENUE","TEAM"};
		} 
	}


	static class ItemHolder {
		ImageView imgvw_sportlogo;
		TextView txtvw_eventName;
		TextView txtvw_sportname;
		

	}
	@Override
	public int numberOfSections() {
		// TODO Auto-generated method stub
		return header.length;
	}

	@Override
	public int numberOfRows(int section) {
		// TODO Auto-generated method stub
		int s = 0;
		switch (section) {
		case 0:{s=1;}break;
		case 1:{s=1;}break;
		case 2:{s=2;}break;
		case 3:{s=lists_recent.size();}break;
		default:
			break;
		}

		
		
	return s;
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
		if(convertView==null){
			LayoutInflater inflater = ((Activity)context).getLayoutInflater();
			convertView = inflater.inflate(R.layout.list_item_event_add, null);
			
			itemHolder = new ItemHolder();
			
			itemHolder.txtvw_eventName = (TextView)convertView.findViewById(R.id.txtvw_sportname);
			itemHolder.txtvw_sportname = (TextView)convertView.findViewById(R.id.txtvw_sportsubname);
			itemHolder.imgvw_sportlogo = (ImageView)convertView.findViewById(R.id.imgvw_sportlogo);
			
					
			convertView.setTag(itemHolder);
			
		}
		else{
			itemHolder = (ItemHolder)convertView.getTag();
		}
		try {
					
			if(isRecent){
				event = lists_recent.get(atIndex);
				isRecent = false;
			//	Toast.makeText(context, "size"+event.team.size(), Toast.LENGTH_SHORT).show();
			}else{
				event  = global_Data.getNewEvent();
			}
					 
				if(section==3){
					itemHolder.imgvw_sportlogo.setVisibility(View.VISIBLE);
					itemHolder.txtvw_sportname.setVisibility(View.VISIBLE);
					itemHolder.txtvw_eventName.setPadding(0, 0, 0, 0);
					itemHolder.txtvw_eventName.setTextColor(context.getResources().getColor(R.color.uni_orange));
					
				
				}else{
					itemHolder.imgvw_sportlogo.setVisibility(View.GONE);
					itemHolder.txtvw_sportname.setVisibility(View.GONE);
					itemHolder.txtvw_eventName.setPadding(16, 0, 0, 0);
					
				}
				
				/*
				if(event.sportName.length()==0 || event.venueName.length()==0){
					((Activity)context).btn_done.setEnabled(false);
					((Activity)context).btn_done.setTextColor(context.getResources().getColor(R.color.uni_light_grey));
				}else{
					((Activity)context).btn_done.setEnabled(true);
					((Activity)context).btn_done.setTextColor(context.getResources().getColor(R.color.uni_blue));
				
				}
				*/
				
				switch (section) {
				case 0:
				{
					
					String text = event.sportName.length()>0?event.sportName:"Select a Sport";
					itemHolder.txtvw_eventName.setText(text);
					itemHolder.txtvw_eventName.setTextColor(context.getResources().getColor( event.sportName.length()>0?R.color.uni_orange:R.color.uni_grey));
		
				}
					break;
				case 1:
				{
					
					String text = event.venueName.length()>0?event.venueName:"Select a Venue";
					itemHolder.txtvw_eventName.setText(text);
					itemHolder.txtvw_eventName.setTextColor(context.getResources().getColor( event.venueName.length()>0?R.color.uni_orange:R.color.uni_grey));
		
					
				}
					break;
				case 2:
				{
					
					if(event.team.size()>0){
						String team1 = "t1" ,team2 = "t2";
						if(event.team.size()==1){
							team1= event.team.get(0);
							team2 = "Add another Team";
						}
						else{
							team1=  event.team.get(0);
							team2 = event.team.get(1);
						}
						
						if(row==0){
							itemHolder.txtvw_eventName.setText(team1);
							itemHolder.txtvw_eventName.setTextColor(context.getResources().getColor(R.color.uni_orange));
		
							
						}
						if(row==1){
							itemHolder.txtvw_eventName.setText(team2);
							itemHolder.txtvw_eventName.setTextColor(context.getResources().getColor( 
									event.team.size()==1?R.color.uni_grey:R.color.uni_orange));
		
						}
					}else{
						if(row==0){
						itemHolder.txtvw_eventName.setText("Add a Team");	
						itemHolder.txtvw_eventName.setTextColor(context.getResources().getColor( R.color.uni_grey));
		
						}else{
							
							return new View(context);
						
							
						}
					}
					
					
					
					
				}
					break;
				case 3:
				{
					
					_RecentEvent recent = lists_recent.get(row);
					
					String team = recent.team.size()>0? recent.team.get(0):"";
					itemHolder.txtvw_eventName.setText(team);
					itemHolder.txtvw_sportname.setText(recent.sportName);
					
					Drawable d ;
					int color = context.getResources().getColor(R.color.uni_orange);
					Mode mMode = Mode.SRC_ATOP;
					try {
					String logo = recent.sportLogo;
					String packageName = logo.substring(logo.lastIndexOf("sport/"), logo.indexOf(".png")).replace("sport/", "");
					packageName = packageName.replace(" ", "_");
					//Log.e("sportLogo", ":"+eventLists.eventLists.get(pos).eventSport.sportLogo);
					//Log.e("packageName", ""+packageName);
					
					int id = context.getResources().getIdentifier(packageName, "drawable", context.getPackageName());
					
						d= context.getResources().getDrawable(id);
					} catch (Exception e) {
						// TODO: handle exception
						d = context.getResources().getDrawable(R.drawable.ic_istoday);
					}
					
					d.setColorFilter(color,mMode);
					itemHolder.imgvw_sportlogo.setImageDrawable(d);
				}
					break;
		
				default:
					break;
				}
				
	
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		return convertView;
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

		
		switch (section) {
		case 0:
		{
			((Activity)context).startActivity(new Intent(context, ChooseASport_Activity.class));
		}
			break;
		case 1:
		{
			((Activity)context).startActivity(new Intent(context, ChooseAVenue_Activity.class));
		}
			break;
		case 2:
		{
			Intent intent = new Intent(context, ChooseATeam_Activity.class);
			intent.putExtra("team", row+1);
			((Activity)context).startActivity(intent);
		}
			break;
		case 3:
		{
			 isRecent = true;
			 atIndex = row;
			 global_Data.setNewEvent(lists_recent.get(row));
			 CreateAdapter.this.notifyDataSetChanged();
			 parent.invalidate();
			 getNearestLocation(parent,row);
			
			 
		}
			break;

		default:
			break;
		}
		
	}
	
	public void getNearestLocation(final AdapterView<?> parent,final int row){
		
		
		
		RequestParams params = new RequestParams();
		params.put("longitude", ""+coordinate.longitude);
		params.put("latitude", ""+coordinate.latitude);
		
		//Log.e("params", " "+params.toString());
	
		async_HttpClient.GET("GetNearest", params, new JsonHttpResponseHandler(){
			
		
			@Override
			public void onSuccess(JSONObject response) {
				// TODO Auto-generated method stub
				super.onSuccess(response);
			//	Log.e("onSuccess", ""+response.toString());
				
				try{ 
					lists_recent.get(row).venueName = response.getJSONObject("data").getString("placeName");
					
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				 isRecent = true;
				 CreateAdapter.this.notifyDataSetChanged();
				 parent.invalidate();
			}
			@Override
			public void onFailure(int statusCode, Throwable e,
					JSONObject errorResponse) {
				// TODO Auto-generated method stub
				super.onFailure(statusCode, e, errorResponse);
				
				Log.e("onFailure", "Failed");
				 CreateAdapter.this.notifyDataSetChanged();
				 parent.invalidate();
			}
			
		});
		
		
	}

}