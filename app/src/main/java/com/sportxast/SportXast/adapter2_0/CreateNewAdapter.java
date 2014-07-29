package com.sportxast.SportXast.adapter2_0;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sportxast.SportXast.R;

public class CreateNewAdapter extends BaseAdapter{

	Context context;
	
	String[] titles = new String[]{"Choose a Sport","Choose a Venue","Home Team","Away Team (Optional)"};
	public CreateNewAdapter(Context context) {
		// TODO Auto-generated constructor stub
		this.context = context;
	}
	
	static class ItemHolder{
		TextView txtvw_title;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return titles.length;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return titles[arg0];
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
			itemHolder.txtvw_title = (TextView)convertView.findViewById(R.id.txtvw_listabout);
			
			itemHolder.txtvw_title.setTextColor(Color.parseColor("#444444"));
			
			convertView.setTag(itemHolder);
			
			
		}else{
			itemHolder = (ItemHolder)convertView.getTag();
		}
		
		
		itemHolder.txtvw_title.setText(titles[arg0]);
		
		
		return convertView;
	}
	
	
	
	public View SelectAnItem(ItemHolder itemHolder,View convertView){
		
		return convertView;
		
	}
	
	public View ChooseItem(ItemHolder itemHolder,View convertView){
		
		
		return convertView;
		
	}
	

}
