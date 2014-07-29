package com.sportxast.SportXast.adapter2_0;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.sportxast.SportXast.R;
import com.sportxast.SportXast.models._EventLists.MediaTagWithHighlight;

import java.util.ArrayList;

public class TagsAdapter extends ArrayAdapter<MediaTagWithHighlight> {
	
	private LayoutInflater inflater;

	public TagsAdapter(Context context) {
		super(context, android.R.layout.simple_list_item_2);
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	public void setData(ArrayList<MediaTagWithHighlight> listTags) {
		clear();
		if(listTags != null) {
			for(MediaTagWithHighlight tag : listTags) {
				add(tag);
			}
		}
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		
		if(convertView == null) {
			convertView = inflater.inflate(R.layout.tags_list, parent, false);
			
			holder = new ViewHolder();
			holder.txtUsername = (TextView) convertView.findViewById(R.id.txtUsername);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		MediaTagWithHighlight tag = getItem(position);
		
		holder.txtUsername.setText(tag.tagName);
		
		return convertView;
	}
	
	private static class ViewHolder {
		TextView txtUsername;
	}
}
