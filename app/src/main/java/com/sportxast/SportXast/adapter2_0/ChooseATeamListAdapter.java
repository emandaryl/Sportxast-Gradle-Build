package com.sportxast.SportXast.adapter2_0;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sportxast.SportXast.Global_Data;
import com.sportxast.SportXast.R;
import com.sportxast.SportXast.activities2_0.ChooseATeam_Activity;
import com.sportxast.SportXast.models._SearchHashtag;
import com.sportxast.SportXast.thirdparty_class.SectionAdapter;

import java.util.ArrayList;
//import com.sportxast.SportXast.BaseSherlockActivity;

public class ChooseATeamListAdapter extends SectionAdapter{

	Global_Data global_Data;
	Context context;
	private ArrayList<_SearchHashtag> lists_team = new ArrayList<_SearchHashtag>();
	int atTeam = 0;
	  
	public void updateListElements(ArrayList<_SearchHashtag> lists_team){
		 this.lists_team = lists_team;
		 
		 notifyDataSetChanged();
	} 
	 
	public ChooseATeamListAdapter(Context context,ArrayList<_SearchHashtag> lists_team,int team) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.lists_team = lists_team;
		this.atTeam = team;
		
		global_Data = (Global_Data)this.context.getApplicationContext();
		
		Log.e("adapter", "team");
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
		return lists_team.size();
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
		 
		itemHolder.imgvw_next.setVisibility(View.INVISIBLE);
		final _SearchHashtag team = lists_team.get(row);
		itemHolder.txtvw_title.setText(team.name);
		 
		convertView.setOnClickListener(new OnClickListener() { 
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub 
				
				//int size = global_Data.getNewEvent().team.size();
				if(atTeam==1){
					global_Data.getNewEvent().team.add(0, team.name);
				}
				if(atTeam==2){
					global_Data.getNewEvent().team.add(1, team.name);
				}
				
				 
				if( context instanceof ChooseATeam_Activity ){
						( (ChooseATeam_Activity)context ).exitActivity( team.name );
				}
				
				//((Activity)context).finish();
			}
		});
		
		
		Log.e("display", ""+row);
		
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
	public View getSectionHeaderView(int section, View convertView,
			ViewGroup parent) {
		// TODO Auto-generated method stub
		return new View(context);
	}
}
