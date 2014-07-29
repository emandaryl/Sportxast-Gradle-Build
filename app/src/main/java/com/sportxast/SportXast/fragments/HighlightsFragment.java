package com.sportxast.SportXast.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;

import com.androidquery.AQuery;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sportxast.SportXast.R;
import com.sportxast.SportXast.adapter2_0.EventMediaAdapter;
import com.sportxast.SportXast.commons.Constants;
import com.sportxast.SportXast.models._MediaLists;
import com.sportxast.SportXast.models._MediaLists.Comments;
import com.sportxast.SportXast.models._MediaLists.MediaList;
import com.sportxast.SportXast.models._MediaLists.Tag;
import com.sportxast.SportXast.models._MediaLists.User;
import com.sportxast.SportXast.models._MediaLists.UserInFavorites;
import com.sportxast.SportXast.thirdparty_class.Async_HttpClient;

import org.json.JSONObject;

import java.util.ArrayList;

public class HighlightsFragment extends ListFragment {
	
	private Async_HttpClient httpClient;
	private String userName;
	private SharedPreferences sharedPref;
	private _MediaLists media;
	private ArrayList<MediaList> mediaList;
	private EventMediaAdapter mediaAdapter;
	private View commentView;
	
	public HighlightsFragment() {
		media = new _MediaLists();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/*
		 * Inflate commentView
		 */
		commentView = getActivity().getLayoutInflater().inflate(R.layout.layout_comment_field, null);
		media.aq = new AQuery(getActivity());
		httpClient = new Async_HttpClient(getActivity());
		sharedPref = getActivity().getSharedPreferences("com.sportxast.SportXast", Context.MODE_PRIVATE);
		userName = sharedPref.getString(Constants.EXTRA_USERNAME, "");
//		userName = "yui"; 
		 
		initializeCustomListView();
		exportUserMedia();
	}
	

	private void initializeCustomListView() {  
		
		if(mediaList == null){
			mediaList = new ArrayList<MediaList>();
			
			MediaList mediaList_ 	= new MediaList();
			mediaList_.mediaId 		= "";
			mediaList_.eventId 		= "";
			mediaList_.sportId		= "";
			mediaList_.mediaUserId 	= "";
			mediaList_.mediaUserName= "";
			mediaList_.mediaType 	= "";
			mediaList_.coverImage 	= "";
			mediaList_.coverImageThumb = "";
			mediaList_.largeImageUrl = "";
			mediaList_.mediumImageUrl= "";
			mediaList_.smallImageUrl = "";
			mediaList_.mediaUrl 	= "";
			mediaList_.mediaShortUrl= "";
			mediaList_.m3u8Url 		= "";
			mediaList_.vp8Url 		= "";
			mediaList_.mp4Url 		= "";
			mediaList_.videoLocalPath= "";
			mediaList_.imageLocalPath= "";
			mediaList_.mediaShareString = "";
			mediaList_.onDate 		= "";
			mediaList_.age 			= "";
			mediaList_.viewCount 	= "";
			mediaList_.favoritesCount= "";
			mediaList_.commentsCount = "";
			mediaList_.shareCount 	= "";
			mediaList_.score 		= "";
			mediaList_.currentUserIsOwner = "";
			mediaList_.currentUserHasInFavorites = "";
			mediaList_.transcoderJobStatus = "";
			 
			mediaList_.user				= new User();
			mediaList_.comments 		= new ArrayList<Comments>();
			mediaList_.userInFavorites 	= new ArrayList<UserInFavorites>();
			mediaList_.tags 			= new ArrayList<Tag>();  
			mediaList.add( mediaList_ );
		}
		
		mediaAdapter = new EventMediaAdapter(getActivity(), mediaList);
		setListAdapter(mediaAdapter);
		  
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setListShown(true);
	}
	
	private void exportUserMedia() {
		RequestParams params = new RequestParams();
		params.put(Constants.EXTRA_USERNAME, userName);
		
		httpClient.GET("ExportUserMedia", params, new JsonHttpResponseHandler() { 
			@Override
			public void onSuccess(JSONObject response) {
				super.onSuccess(response);
				
				if(response.isNull(Constants.KEY_ERROR) == false) {
					// error found
					
				} else {
					// success
					media.parseMediaList(response);
					mediaList = media.mediaLists;
					 
					mediaAdapter.updateListElements( mediaList ); 
					/*
					//mediaAdapter = new EventMediaAdapter(getActivity(), mediaList, commentView);
					mediaAdapter = new EventMediaAdapter(getActivity(), mediaList);
					setListAdapter(mediaAdapter);
					*/
					
				}
			}
			
			@Override
			public void onFailure(String responseBody, Throwable error) {
				super.onFailure(responseBody, error);
			}
		});
	}
	
 
	
}
