package com.sportxast.SportXast.adapter2_0;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.sportxast.SportXast.R;
import com.sportxast.SportXast.models._SearchHashtag;
import com.sportxast.SportXast.thirdparty_class.Async_HttpClient;

import java.util.ArrayList;

public class HashTagAdapter extends BaseAdapter{
	
	private Async_HttpClient async_HttpClient; 
	private Context context;
	//_Follower_Following list_follower = new _Follower_Following(); 
	private ArrayList<_SearchHashtag> FArrHashtagList;
	
	public void updateListElements( ArrayList<_SearchHashtag> arrHashtagList) {
		this.FArrHashtagList = arrHashtagList;
 
        //Triggers the list update  
	   notifyDataSetChanged(); 
    }
	    
	/** tabType: 1-followers, 2-following **/
	public HashTagAdapter(Context context, ArrayList<_SearchHashtag> arrHashtagList ) {
		// TODO Auto-generated constructor stub
		 
		this.context= context;
		this.FArrHashtagList = arrHashtagList;
		this.async_HttpClient = new Async_HttpClient(this.context);
		 
	}
	
	static class ItemHolder{
		TextView txtvw_hashtag_element;
		 
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return this.FArrHashtagList.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return this.FArrHashtagList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(final int itemPosition, View convertView, final ViewGroup parent) {
		// TODO Auto-generated method stub
		ItemHolder itemHolder;
		 
		if(convertView==null){
			convertView = ((Activity)context).getLayoutInflater().inflate(R.layout.list_item_hashtag, null); 
			itemHolder  = new ItemHolder();
			 
			itemHolder.txtvw_hashtag_element 		= (TextView)convertView.findViewById(R.id.txtvw_hashtag_element); 
			convertView.setTag(itemHolder); 
		}else{
			itemHolder = (ItemHolder)convertView.getTag();
		}
		 
		//_Profile xx = this.FArrFollowersList.get(itemPosition); 
		itemHolder.txtvw_hashtag_element.setText("#"+ FArrHashtagList.get(itemPosition).name );
		 
		return convertView;
	}
	
	

}
