package com.sportxast.SportXast.adapter2_0;

//import com.sportxast.SportXast.BaseSherlockActivity;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.loopj.android.image.SmartImageView;
import com.sportxast.SportXast.R;
import com.sportxast.SportXast.models._MediaLists.UserInFavorites;
import com.sportxast.SportXast.thirdparty_class.ClickableTextsTextView;

import java.util.ArrayList;

public class FavoritesListAdapter extends BaseAdapter{

	private Context context;
	private ArrayList<UserInFavorites> favoriteList;
	private Drawable drawable_avatar;
	public FavoritesListAdapter(Context context,ArrayList<UserInFavorites> favoriteList) {
		// TODO Auto-generated constructor stub 
		this.context = context;
		this.favoriteList = favoriteList;
		drawable_avatar = this.context.getResources().getDrawable(R.drawable.ic_avatar);
	}
	
	static class ItemHolder{
		SmartImageView img_avatar;
		TextView txt_userId;
		ClickableTextsTextView txt_comment;
		TextView txt_date;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return favoriteList.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return favoriteList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(final int row, View convertview, ViewGroup vgroup) {
		// TODO Auto-generated method stub
		
		if(getCount()==0) return null;
		
		ItemHolder itemHolder;
		
		if(convertview==null){
			LayoutInflater inflater = ((Activity)context).getLayoutInflater();
			convertview =  inflater.inflate(R.layout.list_item_comment, null);
			
			itemHolder = new ItemHolder();
			itemHolder.img_avatar = (SmartImageView)convertview.findViewById(R.id.imgvw_avatar);
			itemHolder.txt_userId = (TextView)convertview.findViewById(R.id.txtvw_userId);
			itemHolder.txt_comment = (ClickableTextsTextView)convertview.findViewById(R.id.txtvw_comment);
			itemHolder.txt_date = (TextView)convertview.findViewById(R.id.txtvw_date);
			
			itemHolder.txt_date.setVisibility(View.GONE);
			itemHolder.txt_userId.setTextColor(context.getResources().getColor(R.color.uni_grey));
			itemHolder.txt_comment.setTextColor(context.getResources().getColor(R.color.uni_orange));
			
			
			convertview.setTag(itemHolder);
			
		}
		else{
		
			itemHolder = (ItemHolder)convertview.getTag();
		}
		
		itemHolder.img_avatar.getLayoutParams().height = drawable_avatar.getMinimumHeight();
		itemHolder.img_avatar.getLayoutParams().width = drawable_avatar.getMinimumWidth();
		if(favoriteList.get(row).avatarUrl.length()>0)
			itemHolder.img_avatar.setImageUrl(favoriteList.get(row).avatarUrl);
		
		itemHolder.txt_userId.setText(favoriteList.get(row).displayName);
	
		itemHolder.txt_comment.setText(favoriteList.get(row).fullName);
		


		
		
		
		return convertview;
	}

}
