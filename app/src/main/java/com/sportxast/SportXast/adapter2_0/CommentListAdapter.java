package com.sportxast.SportXast.adapter2_0;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.loopj.android.image.SmartImageView;
import com.sportxast.SportXast.R;
import com.sportxast.SportXast.models._MediaLists.Comments;

import java.util.ArrayList;

public class CommentListAdapter extends BaseAdapter{

	Context context;
	ArrayList<Comments> commentLists;
	Drawable drawable_avatar;
	public CommentListAdapter(Context context,ArrayList<Comments> commentLists) {
		// TODO Auto-generated constructor stub
	
		this.context = context;
		this.commentLists = commentLists;
		drawable_avatar = this.context.getResources().getDrawable(R.drawable.ic_avatar);
	}
	
	static class ItemHolder{
		SmartImageView img_avatar;
		TextView txt_userId;
		TextView txt_comment;
		TextView txt_date;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return commentLists.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return commentLists.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int row, View convertview, ViewGroup vgroup) {
		// TODO Auto-generated method stub
		
		if(getCount()==0) return null;
		
		ItemHolder itemHolder;
		
		if(convertview==null){
			LayoutInflater inflater = ((Activity)context).getLayoutInflater();
			convertview =  inflater.inflate(R.layout.list_item_comment, null);
			
			itemHolder = new ItemHolder();
			itemHolder.img_avatar = (SmartImageView)convertview.findViewById(R.id.imgvw_avatar);
			itemHolder.txt_userId = (TextView)convertview.findViewById(R.id.txtvw_userId);
			itemHolder.txt_comment = (TextView)convertview.findViewById(R.id.txtvw_comment);
			itemHolder.txt_date = (TextView)convertview.findViewById(R.id.txtvw_date);
			
			convertview.setTag(itemHolder);
			
		}
		else{
		
			itemHolder = (ItemHolder)convertview.getTag();
		}
		
		itemHolder.img_avatar.getLayoutParams().height = drawable_avatar.getMinimumHeight();
		itemHolder.img_avatar.getLayoutParams().width = drawable_avatar.getMinimumWidth();
		itemHolder.img_avatar.setImageUrl(commentLists.get(row).user.avatarUrl);
		
		itemHolder.txt_userId.setText(commentLists.get(row).commentUserName);
		itemHolder.txt_comment.setText(commentLists.get(row).commentBody);
		itemHolder.txt_date.setText(commentLists.get(row).commentAge);
		
		Log.v("commentadapter", "end");
		return convertview;
	}

}
