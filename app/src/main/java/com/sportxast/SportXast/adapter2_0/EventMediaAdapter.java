package com.sportxast.SportXast.adapter2_0;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.method.MovementMethod;
import android.text.style.ClickableSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.androidquery.AQuery;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.image.SmartImageView;
import com.sportxast.SportXast.Global_Data;
import com.sportxast.SportXast.R;
import com.sportxast.SportXast.activities2_0.Fav_Tag_Comment_Activity;
import com.sportxast.SportXast.activities2_0.Highlight_Activity;
import com.sportxast.SportXast.activities2_0.Profile_Activity;
import com.sportxast.SportXast.activities2_0.VideoFullScreenActivity;
import com.sportxast.SportXast.commons.CommonFunctions_1;
import com.sportxast.SportXast.commons.Constants;
import com.sportxast.SportXast.commons.GlobalVariablesHolder;
import com.sportxast.SportXast.models.EditTextBackEvent;
import com.sportxast.SportXast.models.SportsTags;
import com.sportxast.SportXast.models._MediaLists.Comments;
import com.sportxast.SportXast.models._MediaLists.MediaList;
import com.sportxast.SportXast.models._MediaLists.Tag;
import com.sportxast.SportXast.models._MediaLists.UserInFavorites;
import com.sportxast.SportXast.thirdparty_class.Async_HttpClient;
import com.sportxast.SportXast.thirdparty_class.ClickableTextsTextView;
import com.sportxast.SportXast.thirdparty_class.ClickableTextsTextView.TextLinkClickListener;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class EventMediaAdapter extends BaseAdapter{

    private Global_Data FGlobal_Data;
    private Context context;
    ArrayList<MediaList> FArrMediaList;
    //private View commentView;
    private Drawable drawable_avatar;
    private Async_HttpClient async_HttpClient;
    private SportsTags sportsTags;

    private ArrayList<String> listTags;

    private PopupWindow popupWindow;

    //private int flag = 0;
    //private int screen_w;
    //private int screen_h;

    //private SharedPreferences sharedPreferences;
    public void updateListElements(ArrayList<MediaList> arrMediaLists) {
        this.FArrMediaList = arrMediaLists;
        //this.FArrMediaList = ( (Highlight_Activity) context ).FArrMediaList;
        //Triggers the list update  
        notifyDataSetChanged();
    }

    private boolean showListItem = true;

    //public EventMediaAdapter(Context context, ArrayList<MediaList> arrMediaLists, View commentView) {
    public EventMediaAdapter(Context context, ArrayList<MediaList> arrMediaLists) {
        // TODO Auto-generated constructor stub
        this.context 	 	= context;
        this.FArrMediaList  = arrMediaLists;
        //this.FArrMediaList = ( (HighlightResult_Activity) context ).FArrMediaList;
        drawable_avatar     = this.context.getResources().getDrawable(R.drawable.ic_avatar);
        FGlobal_Data 		= (Global_Data)context.getApplicationContext();
        async_HttpClient	= new Async_HttpClient(context);

    }

    static class ItemHolder{
        //View conView;
        SmartImageView imgvw_avatar;
        TextView txtvw_title;
        TextView txtvw_date;

        TextView  txtvw_favoriteCount;
        TextView  txtvw_tagCount;
        TextView  txtvw_commentCount;

        ClickableTextsTextView  txtvw_favoriteList;
        ClickableTextsTextView  txtvw_tagList;
        ClickableTextsTextView  txtvw_commentList;

        ImageView   imgvw_photo;
        ProgressBar progBar_favorite;
        ImageButton imgbtn_favorite;
        ImageButton imgbtn_tag;
        ImageButton imgbtn_comment;
        ImageButton imgbtn_shareapp;
        ImageButton imgbtn_delete;
        ImageButton imgbtn_expand;

        ImageView imgvw_comment_icon;

        RelativeLayout layout_media;
        private VideoView video_media;

        ProgressBar progess_medialoading;
        ImageButton imgbtn_play;
    }

    public class addClickableSpan extends ClickableSpan{

        String word;
        public addClickableSpan(String word) {
            // TODO Auto-generated constructor stub
            this.word = word;
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            // TODO Auto-generated method stub
            super.updateDrawState(ds);

            ds.setColor(context.getResources().getColor(R.color.uni_blue));
            ds.setUnderlineText(false);
        }

        @Override
        public void onClick(View widget) {
            // TODO Auto-generated method stub
            Toast.makeText(context, word, Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return FArrMediaList.size();
    }

    @Override
    public Object getItem(int arg0) {
        // TODO Auto-generated method stub
        return FArrMediaList.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return arg0;
    }


    private int previouslyAccessedConvertViewID = -1;

    @Override
    public View getView(final int viewPosition,  View convertView, final ViewGroup parentView) {
        // TODO Auto-generated method stub
        final ItemHolder itemHolder;

        //if(convertView == null){
        LayoutInflater inflater = ((Activity)context).getLayoutInflater();

        convertView = inflater.inflate(R.layout.list_item_eventmedia, null);
        int convertViewID = Integer.parseInt( "369" + "7377" + Integer.toString(viewPosition) );
        convertView.setId( convertViewID );

        itemHolder = new ItemHolder();
			
			/*
			itemHolder.imgbtn_play.setVisibility(View.VISIBLE);
			itemHolder.imgvw_photo.setVisibility(View.VISIBLE);
			itemHolder.progess_medialoading.setVisibility(View.GONE);
			*/
        itemHolder.imgvw_avatar 		= (SmartImageView)	convertView.findViewById(R.id.imgvw_highlight_avatar);
        itemHolder.txtvw_title 			= (TextView)		convertView.findViewById(R.id.txtvw_highlight_title);
        itemHolder.txtvw_date 			= (TextView)convertView.findViewById(R.id.txtvw_highlight_date);
        itemHolder.imgvw_photo 			= (ImageView)convertView.findViewById(R.id.imgvw_highlight_photo);

        itemHolder.progBar_favorite 	= (ProgressBar)convertView.findViewById(R.id.progBar_favorite);
        itemHolder.progBar_favorite.setVisibility(View.GONE);

        itemHolder.imgbtn_favorite 		= (ImageButton)convertView.findViewById(R.id.imgbtn_favorite);
        itemHolder.imgbtn_tag 			= (ImageButton)convertView.findViewById(R.id.imgbtn_tag);
        itemHolder.imgbtn_comment		= (ImageButton)convertView.findViewById(R.id.imgbtn_comment);
        itemHolder.imgbtn_shareapp		= (ImageButton)convertView.findViewById(R.id.imgbtn_shareapp);
        itemHolder.imgbtn_delete 		= (ImageButton)convertView.findViewById(R.id.imgbtn_delete);
        itemHolder.imgbtn_expand 		= (ImageButton)convertView.findViewById(R.id.imgbtn_expand);
        itemHolder.imgvw_comment_icon 	= (ImageView)convertView.findViewById(R.id.imgvw_comment_icon);

        itemHolder.txtvw_favoriteCount	= (TextView)convertView.findViewById(R.id.txtvw_favoriteCount);
        itemHolder.txtvw_tagCount 		= (TextView)convertView.findViewById(R.id.txtvw_tagCount);
        itemHolder.txtvw_commentCount 	= (TextView)convertView.findViewById(R.id.txtvw_commentCount);

        itemHolder.txtvw_favoriteList 	= (ClickableTextsTextView)convertView.findViewById(R.id.txtvw_favoriteList);
        itemHolder.txtvw_commentList 	= (ClickableTextsTextView)convertView.findViewById(R.id.txtvw_commentList);
        itemHolder.txtvw_tagList 		= (ClickableTextsTextView)convertView.findViewById(R.id.txtvw_tagList);

        itemHolder.layout_media 		= (RelativeLayout)convertView.findViewById(R.id.layout_media);

        itemHolder.video_media 			= (VideoView)convertView.findViewById(R.id.video_media);
        int videoMediaID = Integer.parseInt( 	"313" + "7377" + Integer.toString(viewPosition));
        itemHolder.video_media.setId( videoMediaID  );

        itemHolder.progess_medialoading = (ProgressBar)convertView.findViewById(R.id.progress_medialoading);
        int progressLoaderID = Integer.parseInt("323" + "7377" + Integer.toString(viewPosition) );
        itemHolder.progess_medialoading.setId( progressLoaderID  );

        itemHolder.imgbtn_play 			= (ImageButton)convertView.findViewById(R.id.imgbtn_play);
        int buttonPlayID = Integer.parseInt( 	"333" + "7377" + Integer.toString(viewPosition) );
        itemHolder.imgbtn_play.setId( buttonPlayID  );

        //itemHolder.conView 				= convertView;
        convertView.setTag(itemHolder);
			/* 
		}
		else{
			itemHolder = (ItemHolder)convertView.getTag();
		} 
		*/

        if(CommonFunctions_1.parseToInteger(FArrMediaList.get(viewPosition).user.avatarCount) > 0){
            itemHolder.imgvw_avatar.getLayoutParams().height = drawable_avatar.getMinimumHeight();
            itemHolder.imgvw_avatar.getLayoutParams().width = drawable_avatar.getMinimumWidth();
            itemHolder.imgvw_avatar.setImageUrl(FArrMediaList.get(viewPosition).user.avatarUrl);
        }

        if(FArrMediaList.get(viewPosition).user.fullName.length() > 0){
            itemHolder.txtvw_title.setText(FArrMediaList.get(viewPosition).user.fullName);
        }else if(FArrMediaList.get(viewPosition).user.userName.length() > 0){
            itemHolder.txtvw_title.setText(FArrMediaList.get(viewPosition).user.userName);
        }
        else{
            itemHolder.txtvw_title.setText("userId: " + FArrMediaList.get(viewPosition).user.userId);
            //	itemHolder.txtvw_title.setText( "userId" );
        }

        itemHolder.txtvw_date.setText(FArrMediaList.get(viewPosition).age);
        itemHolder.txtvw_title.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intentPr = new Intent(context,Profile_Activity.class);
                intentPr.putExtra("userId", FArrMediaList.get(viewPosition).mediaUserId);
                intentPr.putExtra("userDisplayname", FArrMediaList.get(viewPosition).user.displayName);
                ((Activity)context).startActivity(intentPr);
            }
        });

        itemHolder.txtvw_favoriteCount.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(context, Fav_Tag_Comment_Activity.class);
                intent.putExtra("mediaId", FArrMediaList.get(viewPosition).mediaId);
                intent.putExtra("listType", "favorite");

                ((Activity) context).startActivity(intent);
            }
        });

        itemHolder.txtvw_tagCount.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(context, Fav_Tag_Comment_Activity.class);
                intent.putExtra("mediaId", FArrMediaList.get(viewPosition).mediaId);
                intent.putExtra("listType", "tag");

                ((Activity) context).startActivity(intent);
            }
        });

        itemHolder.txtvw_commentCount.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent intent = new Intent(context, Fav_Tag_Comment_Activity.class);
                intent.putExtra("mediaId", FArrMediaList.get(viewPosition).mediaId);
                intent.putExtra("listType", "comment");

                ((Activity) context).startActivity(intent);
            }
        });

        AQuery androidQuery = new AQuery(context);
        File localImageFile = new File(FArrMediaList.get(viewPosition).imageLocalPath);
        String coverImagePath = "";
        if( localImageFile.exists() ){
            //Toast.makeText(context, "image exists: " + viewPosition, Toast.LENGTH_LONG).show();
            coverImagePath = FArrMediaList.get(viewPosition).imageLocalPath;
        }else{
            coverImagePath = FArrMediaList.get(viewPosition).coverImage;
        }

        androidQuery.id(itemHolder.imgvw_photo).image( coverImagePath, false, true);
        //aq.id(itemHolder.imgvw_photo).image( FArrMediaList.get(viewPosition).coverImage, false, true);

        itemHolder.txtvw_favoriteCount.setText(FArrMediaList.get(viewPosition).userInFavorites.size()+" Favorites");

        if(FArrMediaList.get(viewPosition).userInFavorites.size()>0){
            itemHolder.txtvw_favoriteList.setVisibility(View.VISIBLE);
            itemHolder.txtvw_favoriteList.setText("");
            for (int i = 0; i < FArrMediaList.get(viewPosition).userInFavorites.size(); i++) {
                itemHolder.txtvw_favoriteList.append("@"+FArrMediaList.get(viewPosition).userInFavorites.get(i).displayName+" ");
            }
        }
        else{
            itemHolder.txtvw_favoriteList.setVisibility(View.GONE);
        }

        itemHolder.txtvw_favoriteList.setLinkTextColor(context.getResources().getColor(R.color.uni_blue));
        itemHolder.txtvw_favoriteList.gatherLinksForText(itemHolder.txtvw_favoriteList.getText().toString());
        itemHolder.txtvw_favoriteList.setMovementMethod(LinkMovementMethod.getInstance());
        itemHolder.txtvw_favoriteList.setOnTextLinkClickListener(new TextLinkClickListener() {

            @Override
            public void onTextLinkClick(View textView, String clickedString) {
                // TODO Auto-generated method stub
                Intent intentPr = new Intent(context,Profile_Activity.class);
                intentPr.putExtra("userId", clickedString.substring(1, clickedString.length()));
                intentPr.putExtra("userDisplayname", FArrMediaList.get(viewPosition).user.displayName);
                ((Activity)context).startActivity(intentPr);
            }
        });

        MovementMethod m3 = itemHolder.txtvw_favoriteList.getMovementMethod();
        if ((m3 == null) || !(m3 instanceof LinkMovementMethod)) {
            if (itemHolder.txtvw_favoriteList.getLinksClickable()) {
                itemHolder.txtvw_favoriteList.setMovementMethod(LinkMovementMethod.getInstance());
            }

        }
        String combinecomments   = "";
        itemHolder.txtvw_commentCount.setText(FArrMediaList.get(viewPosition).comments.size()+" Comments");
        if(FArrMediaList.get(viewPosition).comments.size() > 0){
            itemHolder.txtvw_commentList.setVisibility(View.VISIBLE);
            //itemHolder.txtvw_commentList.setText("");

            int len  = FArrMediaList.get(viewPosition).comments.size()>=5?5:FArrMediaList.get(viewPosition).comments.size();
            for (int i = 0; i < len; i++) {

                if(len==5 && i==0){
                    combinecomments =combinecomments + "View all "+FArrMediaList.get(viewPosition).comments.size()+" Comments";
                    combinecomments = combinecomments+"\n";
                    //itemHolder.txtvw_commentList.append("View all "+FArrMediaList.get(viewPosition).comments.size()+" Comments");
                    //itemHolder.txtvw_commentList.append("\n");
                }
                combinecomments = combinecomments+"@"+FArrMediaList.get(viewPosition).comments.get(i).commentUserName;
                combinecomments = combinecomments+" "+FArrMediaList.get(viewPosition).comments.get(i).commentBody;
                //itemHolder.txtvw_commentList.append("@"+FArrMediaList.get(viewPosition).comments.get(i).commentUserName);
                //itemHolder.txtvw_commentList.append(" "+FArrMediaList.get(viewPosition).comments.get(i).commentBody);
                if(i<(len-1))
                    combinecomments = combinecomments+"\n";
                //itemHolder.txtvw_commentList.append("\n");
            }
        }
        else{
            itemHolder.txtvw_commentList.setVisibility(View.GONE);
        }

        itemHolder.txtvw_commentList.setTextColor(context.getResources().getColor(R.color.uni_grey));
        itemHolder.txtvw_commentList.setLinkTextColor(context.getResources().getColor(R.color.uni_blue));
        itemHolder.txtvw_commentList.gatherLinksForText(combinecomments);//itemHolder.txtvw_commentList.getText().toString());
        itemHolder.txtvw_commentList.setMovementMethod(LinkMovementMethod.getInstance());
        itemHolder.txtvw_commentList.setOnTextLinkClickListener(new TextLinkClickListener() {
            @Override
            public void onTextLinkClick(View textView, String clickedString) {
                // TODO Auto-generated method stub
                Intent intentPr = new Intent(context,Profile_Activity.class);
                intentPr.putExtra("userId", clickedString.substring(1, clickedString.length()));
                intentPr.putExtra("userDisplayname", FArrMediaList.get(viewPosition).user.displayName);
                ((Activity)context).startActivity(intentPr);
            }
        });

        MovementMethod m1 = itemHolder.txtvw_commentList.getMovementMethod();
        if ((m1 == null) || !(m1 instanceof LinkMovementMethod)) {
            if (itemHolder.txtvw_commentList.getLinksClickable()) {
                itemHolder.txtvw_commentList.setMovementMethod(LinkMovementMethod.getInstance());
            }
        }

        itemHolder.txtvw_tagCount.setText(FArrMediaList.get(viewPosition).tags.size()+" Tags");
        if(FArrMediaList.get(viewPosition).tags.size()>0){
            itemHolder.txtvw_tagList.setVisibility(View.VISIBLE);
            itemHolder.txtvw_tagList.setText("");
            for (int i = 0; i < FArrMediaList.get(viewPosition).tags.size(); i++) {
                itemHolder.txtvw_tagList.append("#"+FArrMediaList.get(viewPosition).tags.get(i).name+" ");
            }
        }
        else{
            itemHolder.txtvw_tagList.setVisibility(View.GONE);
        }

        itemHolder.txtvw_tagList.setLinkTextColor(context.getResources().getColor(R.color.uni_blue));
        itemHolder.txtvw_tagList.gatherLinksForText(itemHolder.txtvw_tagList.getText().toString());
        itemHolder.txtvw_tagList.setMovementMethod(LinkMovementMethod.getInstance());
        itemHolder.txtvw_tagList.setOnTextLinkClickListener(new TextLinkClickListener() {

            @Override
            public void onTextLinkClick(View textView, String clickedString) {
                // TODO Auto-generated method stub
					
					/*
				    int isToday = 0; 
					Intent intent = new Intent(context, Highlight_Activity.class);
					intent.putExtra("eventId","");
					intent.putExtra("hashtag", clickedString.substring(1, clickedString.length()));
					
					intent.putExtra("eventTeams", "sampleTeamTitle");
					 
					intent.putExtra("isToday", ""+isToday);
					((Activity)context).startActivity(intent);
					*/

            }
        });

        MovementMethod m2 = itemHolder.txtvw_tagList.getMovementMethod();
        if ((m2 == null) || !(m2 instanceof LinkMovementMethod)) {
            if (itemHolder.txtvw_tagList.getLinksClickable()) {
                itemHolder.txtvw_tagList.setMovementMethod(LinkMovementMethod.getInstance());
            }
        }

        final MediaController mc = new MediaController(context);
        itemHolder.imgbtn_play.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                // TODO Auto-generated method stub
                //######################################################################
                /** Stop the Previous video(s) from playing **/

                if( previouslyAccessedConvertViewID == -1 ){
                }else{
                    int heyhey = previouslyAccessedConvertViewID;
                    View previouslyAccesssedConvertView = parentView.findViewById( previouslyAccessedConvertViewID );

                    if( previouslyAccesssedConvertView != null )
                    {
                        //int videoMediaID = itemHolder.video_media.getId();
                        String[] convertViewIdData = Integer.toString( previouslyAccessedConvertViewID ).split("7377");

                        int videoMediaID = Integer.parseInt( 	"313" + "7377" + convertViewIdData[1].toString() );
                        VideoView vidView = (VideoView) previouslyAccesssedConvertView.findViewById( videoMediaID );
                        if(vidView != null){
                            vidView.setMediaController(null);
                            vidView.stopPlayback();
                        }

                        int buttonPlayID = Integer.parseInt( 	"333" + "7377" + convertViewIdData[1].toString() );

                        ImageButton buttonPlay = (ImageButton) previouslyAccesssedConvertView.findViewById( buttonPlayID );
                        if(buttonPlay != null){
                            buttonPlay.setVisibility(View.VISIBLE);
                        }

                        int progressLoaderID = Integer.parseInt("323" + "7377" + convertViewIdData[1].toString() );
                        ProgressBar progressLoader = (ProgressBar) previouslyAccesssedConvertView.findViewById( progressLoaderID );
                        if(progressLoader != null)
                            progressLoader.setVisibility(View.GONE);

                    }else{
                        previouslyAccessedConvertViewID = -1;
                    }
                }

                //######################################################################
                //######################################################################
                itemHolder.imgbtn_play.setVisibility(View.GONE);
                itemHolder.progess_medialoading.setVisibility(View.VISIBLE);

                File localVideoFile = new File(FArrMediaList.get(viewPosition).videoLocalPath);

                if( localVideoFile.exists() ){
                    // Toast.makeText(context, "local file found", Toast.LENGTH_LONG).show();
                    String path = FArrMediaList.get(viewPosition).videoLocalPath;
                    //itemHolder.video_media.setVideoURI(null);
                    itemHolder.video_media.setVideoPath(path);

                }else{

                    String path = FArrMediaList.get(viewPosition).mediaUrl;
                    if(path.length() > 0){ //if URL from server is there
                        String rawStringURL = path.replace("https", "http");
                        Uri videoPath = Uri.parse( rawStringURL );
                        itemHolder.video_media.setVideoURI(videoPath);
                        //  itemHolder.video_media.setVideoPath(null);

                    }else{ //Play from local
                        path = FArrMediaList.get(viewPosition).videoLocalPath;
                        //  itemHolder.video_media.setVideoURI(null);
                        itemHolder.video_media.setVideoPath(path);
                    }

                }

                // video_media.setMediaController(mediaController);
                itemHolder.video_media.setMediaController(new MediaController(context) );
                itemHolder.video_media.requestFocus();
                itemHolder.video_media.setOnPreparedListener(new MediaPlayer.OnPreparedListener()
                {
                    public void onPrepared(MediaPlayer mp)
                    {
                        //Toast.makeText(getApplicationContext(), "heyho", Toast.LENGTH_LONG).show();
                        itemHolder.imgvw_photo.setVisibility(View.GONE);
                        //itemHolder.imgbtn_play.setVisibility(View.GONE);
                        itemHolder.progess_medialoading.setVisibility(View.GONE);

                        itemHolder.video_media.start();

                        //################################################################################
                        int videoMediaID = itemHolder.video_media.getId();
                        String[] videoMediaIdData = Integer.toString( videoMediaID ).split("7377");

                        // Toast.makeText(context, "Previously Played video ID: "+previouslyAccessedConvertViewID, Toast.LENGTH_LONG).show();
                        int currentConvertViewID = Integer.parseInt( "369" + "7377" + videoMediaIdData[1].toString() );
                        previouslyAccessedConvertViewID = currentConvertViewID;

                    }
                });

                int videoViewID = itemHolder.video_media.getId();
                //  Toast.makeText(context, "VIDEO VIEW ID: " + Integer.toString( videoViewID ), Toast.LENGTH_LONG).show();
                // Toast.makeText(context, path, Toast.LENGTH_LONG).show();
                //################################################

                itemHolder.video_media.setOnCompletionListener(new OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {

                        //Toast.makeText(context, "DONE PLAYING", Toast.LENGTH_LONG).show();
                        // TODO Auto-generated method stub
                        mc.hide();
                        itemHolder.video_media.setMediaController(null);

                        itemHolder.video_media.stopPlayback();
                        itemHolder.imgbtn_play.setVisibility(View.VISIBLE);
                        itemHolder.imgvw_photo.setVisibility(View.VISIBLE);
                        itemHolder.progess_medialoading.setVisibility(View.GONE);

                    }
                });

            }


        });




        itemHolder.progess_medialoading.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                itemHolder.video_media.stopPlayback();
                itemHolder.imgvw_photo.setVisibility(View.VISIBLE);
                itemHolder.progess_medialoading.setVisibility(View.GONE);
                itemHolder.imgbtn_play.setVisibility(View.VISIBLE);
            }
        });


        itemHolder.imgbtn_expand.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                itemHolder.video_media.stopPlayback();
                itemHolder.imgbtn_play.setVisibility(View.VISIBLE);
                itemHolder.imgvw_photo.setVisibility(View.VISIBLE);
                itemHolder.progess_medialoading.setVisibility(View.GONE);

                Intent intent = new Intent(context, VideoFullScreenActivity.class);
                intent.putExtra("mediaUrl",  FArrMediaList.get(viewPosition).mediaUrl);
                ((Activity)context).startActivity(intent);
                ((Activity)context).overridePendingTransition(R.anim.slide_in_top, R.anim.slide_out_top);

            }
        });

        if(CommonFunctions_1.parseToInteger(FArrMediaList.get(viewPosition).currentUserHasInFavorites)==0){
            // flag=1;
            itemHolder.imgbtn_favorite.setImageResource(R.drawable.ic_favorite_blue);
        }else{
            // flag=0;
            itemHolder.imgbtn_favorite.setImageResource(R.drawable.ic_favorite_blue_selected);
        }

        if(CommonFunctions_1.parseToInteger(FArrMediaList.get(viewPosition).currentUserIsOwner)==0){

            itemHolder.imgbtn_delete.setImageResource(R.drawable.selector_report);
        }else{

            itemHolder.imgbtn_delete.setImageResource(R.drawable.selector_delete);
        }

        itemHolder.imgbtn_favorite.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(final View v) {
                // TODO Auto-generated method stub
                //CommonFunctions_1.favoriteHighlight( context, FArrMediaList, (ImageButton) v, FArrMediaList.get(viewPosition).mediaId, viewPosition );
					/*
				    if(context instanceof Highlight_Activity){
				    	itemHolder.progBar_favorite.setVisibility(View.VISIBLE); 
				    	((Highlight_Activity) context).favoriteHighlight( (ImageButton) v, FArrMediaList.get(viewPosition).mediaId, viewPosition );
				     }  */
                v.setEnabled(false);
                RequestParams params = new RequestParams();
                params.put("mediaId", FArrMediaList.get(viewPosition).mediaId);

                Async_HttpClient async_HttpClient = new Async_HttpClient(context);
                async_HttpClient.GET("MediaToFavorites", params, new JsonHttpResponseHandler(){
                    @Override
                    public void onSuccess(JSONObject response) {
                        // TODO Auto-generated method stub
                        super.onSuccess(response);
                        try {
                            if( CommonFunctions_1.parseToInteger(response.get("error").toString()) == 0 ){
                                if(CommonFunctions_1.parseToInteger(FArrMediaList.get(viewPosition).currentUserHasInFavorites)==0){
                                    FArrMediaList.get(viewPosition).currentUserHasInFavorites = "1";
                                    UserInFavorites userInFavorites = new UserInFavorites();
                                    userInFavorites.displayName= GlobalVariablesHolder.X_USER_NAME;
                                    userInFavorites.userId 	= GlobalVariablesHolder.X_USER_ID;
                                    userInFavorites.userName 	= GlobalVariablesHolder.X_USER_NAME;
                                    FArrMediaList.get(viewPosition).userInFavorites.add(0,userInFavorites);
                                }else{
                                    FArrMediaList.get(viewPosition).currentUserHasInFavorites="0";
                                    FArrMediaList.get(viewPosition).userInFavorites.remove(0);
                                }

                                // reloadListView(FArrMediaList);
                                v.setEnabled(true);
                                notifyDataSetChanged();
                            }
                        } catch (NumberFormatException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Throwable e, JSONObject errorResponse) {
                        // TODO Auto-generated method stub
                        super.onFailure(e, errorResponse);
                        v.setEnabled(true);

                    }
                });

            }
        });

        itemHolder.imgbtn_shareapp.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
					/*
					 * Share Mail, SMS, Facebook and Twitter
					 */
                // get location to anchor our popup
                int[] locAnchor = new int[2];
                v.getLocationOnScreen(locAnchor);

                Point point = new Point();
                point.x = locAnchor[0];// x
                point.y = locAnchor[1];// y

//					Log.i("sharebtn", "x: " + locAnchor[0]);
//					Log.i("sharebtn", "y: " + locAnchor[1]);
                LayoutInflater layoutInflater = ((Activity)context).getLayoutInflater();
                View popupLayout = layoutInflater.inflate(R.layout.popup_share, null);
                popupWindow = new PopupWindow(context);
                popupWindow.setContentView(popupLayout);
                popupWindow.setWidth(LayoutParams.WRAP_CONTENT);
                popupWindow.setHeight(LayoutParams.WRAP_CONTENT);
                popupWindow.setBackgroundDrawable(context.getResources().getDrawable(android.R.drawable.dialog_holo_light_frame));
                popupWindow.setOutsideTouchable(true);
                popupWindow.setFocusable(true);
                popupWindow.setAnimationStyle(R.style.PopupAnimation);

                if(point.y <= 520) {
						/*
						 * Almost touching the Header/ActionBar.
						 * Popup below the anchor. Add offset 100.
						 * This will anchor PopupWindow below the
						 * share button.
						 */
                    popupWindow.showAtLocation(popupLayout, Gravity.NO_GRAVITY, point.x, point.y + 100);
                } else {
						/*
						 * Offset = -235
						 * This will anchor PopupWindow above the
						 * share button.
						 */
                    Log.i("EventMediaAdapter", "offset: " + offSet());
                    popupWindow.showAtLocation(popupLayout, Gravity.NO_GRAVITY, point.x, point.y - offSet());
                }

                ImageButton imgbtnShareEmail 	= (ImageButton) popupLayout.findViewById(R.id.imgbtn_share_email);
                ImageButton imgbtnShareSms 	= (ImageButton) popupLayout.findViewById(R.id.imgbtn_share_sms);
                ImageButton imgbtnShareTwitter = (ImageButton) popupLayout.findViewById(R.id.imgbtn_share_twitter);
                ImageButton imgbtnShareFacebook= (ImageButton) popupLayout.findViewById(R.id.imgbtn_share_facebook);

                imgbtnShareEmail.setOnClickListener(onclickShare);
                imgbtnShareSms.setOnClickListener(onclickShare);
                imgbtnShareTwitter.setOnClickListener(onclickShare);
                imgbtnShareFacebook.setOnClickListener(onclickShare);

					/*
					 * Pass PopupWindow object as Tag. So onClickListener can
					 * dismiss it.
					 */

                String shareMsg = FArrMediaList.get(viewPosition).mediaShareString + "\n"
                        + FArrMediaList.get(viewPosition).twitterCardUrl;

                String additionalTag = FArrMediaList.get(viewPosition).coverImageThumb
                        + Constants.SEPARATOR + FArrMediaList.get(viewPosition).videoLocalPath
                        + Constants.SEPARATOR + FArrMediaList.get(viewPosition).mp4Url
                        + Constants.SEPARATOR + FArrMediaList.get(viewPosition).mediaId;

                imgbtnShareEmail.setTag(shareMsg);
                imgbtnShareSms.setTag(shareMsg);
                imgbtnShareTwitter.setTag(shareMsg);

					/*
					 * For sharing on Facebook, implode important Strings.
					 * index 0 = shareMsg = The caption/description for the video to be uploaded.
					 * index 1 = coverImageThumb = Image file for thumbnail.
					 * index 2 = videoLocalPath = Path to the video stored on the phone.
					 * index 3 = mp4url = Url of the video.
					 * index 4 = media id
					 * Pass it to setTag()
					 */
                imgbtnShareFacebook.setTag(shareMsg + Constants.SEPARATOR + additionalTag);}
        });

                itemHolder.imgbtn_delete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
					
					/*
				    if(context instanceof Highlight_Activity){
				    	((Highlight_Activity) context).showDeleteSectionPanel( true, FArrMediaList.get(viewPosition).mediaId, viewPosition );
				    }
				    */

                // custom dialog
                final Dialog dialog = new Dialog(context, android.R.style.Theme_Translucent_NoTitleBar);
                dialog.setContentView(R.layout.layout_delete_field);
                //dialog.setTitle("Title...");

                // set the custom dialog components - text, image and button

                Button btn_delete = ( Button ) dialog.findViewById(R.id.btn_delete);
                btn_delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub

                        //Toast.makeText(getApplicationContext(), "blahblah2", Toast.LENGTH_LONG).show();
                        RequestParams params = new RequestParams();
                        params.put("mediaId", FArrMediaList.get(viewPosition).mediaId);

                        async_HttpClient.GET("DeleteMedia", params, new JsonHttpResponseHandler(){

                            @Override
                            public void onSuccess(int statusCode,
                                                  JSONObject response) {
                                //TODO Auto-generated method stub
                                super.onSuccess(statusCode, response);

                                Toast.makeText(context, "Deleted successfully.", Toast.LENGTH_SHORT).show();
                                FArrMediaList.remove(viewPosition);
									/*
									showDeleteSectionPanel(false, "", -1); 
									reloadListView(FArrMediaList); 
									*/
                                notifyDataSetChanged();
                                dialog.dismiss();
                            }
                            @Override
                            public void onFailure(int statusCode,
                                                  Throwable e,
                                                  JSONObject errorResponse) {
                                // TODO Auto-generated method stub
                                super.onFailure(statusCode, e, errorResponse);
                                Toast.makeText(context, "Failed to delete media", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }

                        });
                    }
                });


                Button dialogButton = (Button) dialog.findViewById(R.id.btn_cancel);
                // if button is clicked, close the custom dialog
                dialogButton.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                View view_outside = ( View ) dialog.findViewById(R.id.view_outside);
                view_outside.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });

        itemHolder.imgbtn_comment.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
					/*
				    if(context instanceof Highlight_Activity){
				    	((Highlight_Activity) context).showCommentSectionPanel( true, FArrMediaList.get(viewPosition).mediaId, viewPosition );
				    }
				    */
                //############################################################################################
                // custom dialog
                final Dialog dialog = new Dialog(context, android.R.style.Theme_Translucent_NoTitleBar);
                dialog.setContentView(R.layout.layout_comment_field);
                //dialog.setTitle("Title...");
                // set the custom dialog components - text, image and button
                final EditTextBackEvent edittext_comment = (EditTextBackEvent) dialog.findViewById(R.id.edittext_comment);

                Button btn_send = ( Button ) dialog.findViewById(R.id.btn_send);
                btn_send.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        //Toast.makeText(context, edittext_comment.getText().toString(), Toast.LENGTH_LONG).show();

                        RequestParams requestParams = new RequestParams();
                        requestParams.put("parentId",	FArrMediaList.get(viewPosition).mediaId);
                        requestParams.put("type",		"media");
                        requestParams.put("comment", 	edittext_comment.getText().toString());

                        async_HttpClient.POST("AddComment", requestParams, new JsonHttpResponseHandler(){
                            @Override
                            public void onSuccess(JSONObject response) {
                                // TODO Auto-generated method stub
                                super.onSuccess(response);
                                //Toast.makeText(getApplicationContext(), "COMMENT ADDED MAH NIGGA.", Toast.LENGTH_SHORT).show();
                                Log.e("comment", "Success comment"+response.toString());
                                Comments comments 		= new Comments();
                                comments.commentBody 	= edittext_comment.getText().toString();
                                comments.commentUserId 	= GlobalVariablesHolder.X_USER_ID;
                                comments.commentUserName= GlobalVariablesHolder.X_USER_NAME;

                                FArrMediaList.get( viewPosition ).comments.add(0, comments);

                                //HIDE keyboard
                                ((InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(edittext_comment.getWindowToken(), 0);

                                dialog.dismiss();
                                notifyDataSetChanged();
                            }

                            @Override
                            public void onFailure(int arg0, Header[] arg1, byte[] arg2,
                                                  Throwable arg3) {
                                // TODO Auto-generated method stub
                                super.onFailure(arg0, arg1, arg2, arg3);
                                dialog.dismiss();
                            }
                        });

                    }
                });


                View view_outside = ( View ) dialog.findViewById(R.id.view_outside);
                view_outside.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        //HIDE keyboard
                        ((InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(edittext_comment.getWindowToken(), 0);
                        //getWindow().setSoftInputMode(  WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                        dialog.dismiss();
                    }
                });


                dialog.show();
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput( edittext_comment, InputMethodManager.SHOW_IMPLICIT );

                edittext_comment.setFocusableInTouchMode(true);
                edittext_comment.setCursorVisible(true);
                edittext_comment.requestFocus();

                //SHOW keyboard
                ((InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE)).toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
            }
        });

        itemHolder.imgbtn_tag.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                final PopupMenu popupMenu = new PopupMenu(context, itemHolder.imgbtn_tag);
                final int sportId = parseToInteger(FArrMediaList.get(viewPosition).sportId);
//				final String mediaId = FArrMediaLists.get(viewPosition).mediaId;
//				Log.i(TAG, "sportID: " + sportId + " mediaId: " + mediaId);

                sportsTags = new SportsTags(sportId);
                listTags = sportsTags.populateTags();

                //Log.i(TAG, "hashtag to split" + itemHolder.txtvw_tagList.getText().toString());
                String[] arrTagsSelected = itemHolder.txtvw_tagList.getText().toString().split("#");
                for(int i=0; i<arrTagsSelected.length; i++) {
                    listTags.remove(arrTagsSelected[i].trim());
                }

                for(int i=0; i<listTags.size(); i++) {
                    popupMenu.getMenu().add(0, i, i, listTags.get(i));
                }

                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(new OnMenuItemClickListener() {

                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        final String itemTag = item.getTitle().toString().trim();
                        RequestParams requestParams = new RequestParams();
                        requestParams.put("mediaId", FArrMediaList.get(viewPosition).mediaId);
                        requestParams.put("tagName", itemTag);

//						itemHolder.imgbtn_tag.setTag(itemTag + ","); 
                        async_HttpClient.POST("SaveMediaTag", requestParams, new JsonHttpResponseHandler(){
                            @Override
                            public void onSuccess(JSONObject response) {
                                super.onSuccess(response);

                                Tag tag = new Tag();
                                tag.name = itemTag;

                                FArrMediaList.get(viewPosition).tags.add(0,tag);
                                notifyDataSetChanged();
                                parentView.invalidate();

                                popupMenu.getMenu().clear();
                                listTags.remove(itemTag);
                            }

                            @Override
                            public void onFailure(Throwable arg3,JSONObject response) {
                                super.onFailure(arg3, response);
                                Toast.makeText(context, "fail to add tag. try again.", Toast.LENGTH_SHORT).show();
                            }

                        });

                        return false;
                    }
                });




            }
        });


        if((FArrMediaList.get(viewPosition).mediaId.length() <= 0) &&
                (FArrMediaList.get(viewPosition).eventId.length() <= 0)&&
                (FArrMediaList.get(viewPosition).sportId.length() <= 0)&&
                (FArrMediaList.get(viewPosition).mediaUserId.length() <= 0)&&
                (FArrMediaList.get(viewPosition).videoLocalPath.length() <= 0)&&
                (FArrMediaList.get(viewPosition).imageLocalPath.length() <= 0) ){
            convertView.setVisibility(View.INVISIBLE);
        }

        return convertView;
    }

    public int parseToInteger(String s){
        int i = 0;
        try {
            i= Integer.parseInt(s);
        } catch (Exception e) {
            // TODO: handle exception
            i=0;
        }
        return i;
    }

    private OnClickListener onclickShare = new OnClickListener() {

        @Override
        public void onClick(View view) {
            int id = ((ImageButton) view).getId();

            popupWindow.dismiss();

            //Log.i(TAG, "Extra Text: " + view.getTag().toString());

            switch(id) {
                case R.id.imgbtn_share_email:

                    Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                    String uriEmailText =
                            "mailto:" + Uri.encode("") +
                                    "?subject=" + Uri.encode(FGlobal_Data.getAppSetting_settings("APP_SHARE_MAIL_SUBJECT")) +
                                    "&body=" + Uri.encode(view.getTag().toString());
                    Uri uriEmail = Uri.parse(uriEmailText);
                    emailIntent.setData(uriEmail);
                    context.startActivity(Intent.createChooser(emailIntent, "Share Via Email"));

                    break;
                case R.id.imgbtn_share_sms:

                    Uri uri = Uri.parse("smsto:");
                    Intent smsIntent = new Intent(Intent.ACTION_VIEW, uri);
                    smsIntent.putExtra("sms_body", view.getTag().toString());
                    smsIntent.setType("vnd.android-dir/mms-sms");
                    context.startActivity(smsIntent);

                    break;
                case R.id.imgbtn_share_twitter:

                    String tweet = "https://twitter.com/intent/tweet?text=" + view.getTag().toString();
                    Intent tweetIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(tweet));

                    PackageManager pm = context.getPackageManager();
                    List<ResolveInfo> infoList = pm.queryIntentActivities(tweetIntent, 0);

                    for(ResolveInfo info : infoList) {
                        if(info.activityInfo.packageName.toLowerCase().startsWith("com.twitter")) {
                            tweetIntent.setPackage(info.activityInfo.packageName);
                            break;
                        }
                    }

                    context.startActivity(tweetIntent);

                    break;
                case R.id.imgbtn_share_facebook:
                    Highlight_Activity activity = (Highlight_Activity) context;
                    activity.openUpFacebookFragment(view.getTag().toString());
                    break;
            }
        }
    };
    private int offSet() {
        int densityDpi = context.getResources().getDisplayMetrics().densityDpi;
        if(densityDpi == DisplayMetrics.DENSITY_MEDIUM) {
            return 50;
        } else if(densityDpi == DisplayMetrics.DENSITY_HIGH) {
            return 100;
        } else if(densityDpi == DisplayMetrics.DENSITY_XHIGH) {
            return 150;
        } else if(densityDpi == DisplayMetrics.DENSITY_XXHIGH) {
            return 220;
        } else {
            return 20;
        }
    }
}

