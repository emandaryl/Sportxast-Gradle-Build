package com.sportxast.SportXast.adapter2_0;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sportxast.SportXast.R;
import com.sportxast.SportXast.models._Links;

import java.util.ArrayList;

public class InformationAdapter extends BaseAdapter{

	Context context;
	
	//String[] titles = new String[]{"Privacy Policy","Terms of Service","Help","About"};
	ArrayList<_Links> list_links = new ArrayList<_Links>();
	public InformationAdapter(Context context,ArrayList<_Links> list_links ) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.list_links = list_links;
	}
	
	static class ItemHolder{
		TextView txtvw_title;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list_links.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return list_links.get(arg0);
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
			
			convertView.setTag(itemHolder);
			
			
		}else{
			itemHolder = (ItemHolder)convertView.getTag();
		}
		
		
		itemHolder.txtvw_title.setText(list_links.get(arg0).title);
		
		
		return convertView;
	}

}
