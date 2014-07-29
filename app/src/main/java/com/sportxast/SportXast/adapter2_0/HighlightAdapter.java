package com.sportxast.SportXast.adapter2_0;

import android.app.Activity;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.LabeledIntent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.text.Html;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.ProgressBar;
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
import com.sportxast.SportXast.activities2_0.Profile_Activity;
import com.sportxast.SportXast.models._MediaLists;
import com.sportxast.SportXast.thirdparty_class.Async_HttpClient;
import com.sportxast.SportXast.thirdparty_class.SectionAdapter;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
//import com.sportxast.SportXast.BaseSherlockActivity;

public class HighlightAdapter extends SectionAdapter{

	Global_Data FGlobal_Data;
	Context context;
	_MediaLists mediaLists;
	
	View commentView;
	Drawable drawable_avatar;
	Async_HttpClient async_HttpClient;
	
	int flag = 0;
	
	int screen_w;
	int screen_h;
	
	public HighlightAdapter(Context context,_MediaLists mediaLists) {
		// TODO Auto-generated constructor stub 
		this.context = context;
		this.mediaLists = mediaLists;
		drawable_avatar = this.context.getResources().getDrawable(R.drawable.ic_avatar);
		FGlobal_Data = (Global_Data)context.getApplicationContext();
		async_HttpClient = new Async_HttpClient(context);
		
		commentView = ((Activity)this.context).getLayoutInflater().inflate(R.layout.layout_comment_field, null);
		((Activity)context).addContentView(commentView, new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
		commentView.setVisibility(View.GONE); 
	}

	
	static class HeaderHolder{
		SmartImageView imgvw_avatar;
		TextView txtvw_title;
		TextView txtvw_date;
	}
	static class ItemHolder{
		ImageView imgvw_photo;
		ImageButton imgbtn_favorite;
		ImageButton imgbtn_tag;
		ImageButton imgbtn_comment;
		ImageButton imgbtn_shareapp;
		ImageButton imgbtn_delete;
		
		ImageView imgvw_comment_icon;
		TextView  txtvw_favoriteCount;
		
		VideoView video_media;
		
		ProgressBar progess_medialoading;
		ImageButton imgbtn_play;
		
		LinearLayout content;
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
	public int numberOfSections() {
		// TODO Auto-generated method stub
		return mediaLists.mediaLists.size();
	}

	@Override
	public int numberOfRows(int section) {
		// TODO Auto-generated method stub
		return 1;
	}

	

	@Override
	public Object getRowItem(int section, int row) {
		// TODO Auto-generated method stub
		return mediaLists.mediaLists.get(row);
	}
	
	@Override
	public View getRowView(final int section, final int row, View convertView,
			ViewGroup parent) {
		// TODO Auto-generated method stub


		Log.e("highlight", "row :"+section);
		
		final ItemHolder itemHolder;
		if(convertView==null){
			LayoutInflater inflater = ((Activity)context).getLayoutInflater();
			convertView = inflater.inflate(R.layout.list_item_highlight, null);
			
			itemHolder = new ItemHolder();
		
			itemHolder.imgvw_photo = (ImageView)convertView.findViewById(R.id.imgvw_highlight_photo);
			itemHolder.imgbtn_favorite = (ImageButton)convertView.findViewById(R.id.imgbtn_favorite);
			itemHolder.imgbtn_tag = (ImageButton)convertView.findViewById(R.id.imgbtn_tag);
			itemHolder.imgbtn_comment = (ImageButton)convertView.findViewById(R.id.imgbtn_comment);
			itemHolder.imgbtn_shareapp = (ImageButton)convertView.findViewById(R.id.imgbtn_shareapp);
			itemHolder.imgbtn_delete = (ImageButton)convertView.findViewById(R.id.imgbtn_delete);

			
			itemHolder.imgvw_comment_icon = (ImageView)convertView.findViewById(R.id.imgvw_comment_icon);
			itemHolder.txtvw_favoriteCount = (TextView)convertView.findViewById(R.id.txtvw_favoriteCount);
			itemHolder.content = (LinearLayout)convertView.findViewById(R.id.layout_comments);
			
			itemHolder.video_media = (VideoView)convertView.findViewById(R.id.video_media);
			itemHolder.progess_medialoading = (ProgressBar)convertView.findViewById(R.id.progress_medialoading);
			itemHolder.imgbtn_play = (ImageButton)convertView.findViewById(R.id.imgbtn_play);
			convertView.setTag(itemHolder);
			
		}
		else{
			itemHolder = (ItemHolder)convertView.getTag();
		}
		
		if(Integer.parseInt(mediaLists.mediaLists.get(section).commentsCount)==0){
			itemHolder.imgvw_comment_icon.setVisibility(View.GONE);
		}else{
			itemHolder.imgvw_comment_icon.setVisibility(View.VISIBLE);
		}
		
		AQuery aq = new AQuery(context);
		aq.id(itemHolder.imgvw_photo).image(mediaLists.mediaLists.get(section).coverImage, false, true);
	
         
		itemHolder.txtvw_favoriteCount.setText(mediaLists.mediaLists.get(section).favoritesCount+" favorites");
		itemHolder.content.removeAllViews();
		if(Integer.parseInt(mediaLists.mediaLists.get(section).commentsCount)>0){
			for (int i = 0; i< mediaLists.mediaLists.get(section).comments.size(); i++) {
				final String commentUserName = mediaLists.mediaLists.get(section).comments.get(i).commentUserName;
				String commentBody = mediaLists.mediaLists.get(section).comments.get(i).commentBody;
				
				LinearLayout linearLayout = new LinearLayout(context);
				linearLayout.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
				linearLayout.setOrientation(LinearLayout.HORIZONTAL);
			
				
				if(Integer.parseInt(mediaLists.mediaLists.get(section).commentsCount)>9 && i==1){
					TextView txt_viewAll = new TextView(context);
					txt_viewAll.setClickable(true);
					txt_viewAll.setSingleLine(true);
					txt_viewAll.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
					txt_viewAll.setText(Html.fromHtml(" "+"<font color='#999C9E'>"+"View all "+mediaLists.mediaLists.get(section).commentsCount+" comments"+"</font><br/>"));
					txt_viewAll.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
							Intent intent = new Intent(context,Fav_Tag_Comment_Activity.class);
							intent.putExtra("mediaId", mediaLists.mediaLists.get(section).mediaId);
						
							((Activity)context).startActivity(intent);
								
						}
					});
					
					linearLayout.addView(txt_viewAll,new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
				}
				else{
					
					TextView txt_user = new TextView(context);
					TextView txt_comment = new TextView(context);
					txt_user.setClickable(true);
					txt_user.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
					txt_comment.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
					txt_comment.setSingleLine(true);
					
					txt_user.setText(Html.fromHtml(""+"<font color='#0099cc'>"+commentUserName+"</font>"));
					txt_comment.setText(Html.fromHtml(" "+"<font color='#999C9E'>"+commentBody+"</font><br/>"));
						
					final Intent intent = new Intent(context,Profile_Activity.class);
					intent.putExtra("userId", mediaLists.mediaLists.get(section).mediaUserId);
					txt_user.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
						((Activity)context).startActivity(intent);
						
						}
					});
					
					
					
					linearLayout.addView(txt_user,new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
					linearLayout.addView(txt_comment,new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
				}
				
				
				
				itemHolder.content.addView(linearLayout);
			}
		}
		
	
		
		
	

		itemHolder.imgbtn_play.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub 
				
				   itemHolder.imgbtn_play.setVisibility(View.GONE);
				   itemHolder.progess_medialoading.setVisibility(View.VISIBLE);
				   	 
				
				      String path = mediaLists.mediaLists.get(section).mediaUrl;
				       MediaController mc = new MediaController(context);
				        mc.setAnchorView( itemHolder.video_media);
				        Uri uri = Uri.parse(path);
				        
				        itemHolder.video_media.setMediaController(mc);
				        itemHolder.video_media.setVideoURI(uri);
			      //  itemHolder.video_media.start();
					
			}
			
		
		});
		
		 itemHolder.video_media.setOnPreparedListener(new OnPreparedListener() {
				
				@Override
				public void onPrepared(MediaPlayer mp) {
					// TODO Auto-generated method stub
					
					itemHolder.imgvw_photo.setVisibility(View.INVISIBLE);
					 
					   itemHolder.progess_medialoading.setVisibility(View.GONE);
					   itemHolder.video_media.start();
				}
			});
		 
		 itemHolder.video_media.setOnCompletionListener(new OnCompletionListener() {
			
			@Override
			public void onCompletion(MediaPlayer mp) {
				// TODO Auto-generated method stub
				 itemHolder.imgbtn_play.setVisibility(View.VISIBLE);
				itemHolder.imgvw_photo.setVisibility(View.VISIBLE);
				itemHolder.progess_medialoading.setVisibility(View.GONE);
				
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
		 
		 if(Integer.parseInt(mediaLists.mediaLists.get(section).currentUserHasInFavorites)==0){
			 flag=1;
			 itemHolder.imgbtn_favorite.setImageResource(R.drawable.ic_favorite_blue);
		 }else{
			 flag=0;
			 itemHolder.imgbtn_favorite.setImageResource(R.drawable.ic_favorite_blue_selected);
		 }
		 
		 
		 if(Integer.parseInt(mediaLists.mediaLists.get(section).currentUserIsOwner)==0){
			
			 itemHolder.imgbtn_delete.setImageResource(R.drawable.selector_report);
		 }else{
			 
			 itemHolder.imgbtn_delete.setImageResource(R.drawable.selector_delete);
		 }
		 
	
		 
		 itemHolder.imgbtn_favorite.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				 if(Integer.parseInt(mediaLists.mediaLists.get(section).currentUserHasInFavorites)==0){
					 mediaLists.mediaLists.get(section).currentUserHasInFavorites="1";
					 itemHolder.imgbtn_favorite.setImageResource(R.drawable.ic_favorite_blue);
				 }else{
					 mediaLists.mediaLists.get(section).currentUserHasInFavorites="0";
					 itemHolder.imgbtn_favorite.setImageResource(R.drawable.ic_favorite_blue_selected);
				 }
				
				RequestParams params = new RequestParams();
				params.put("mediaId", mediaLists.mediaLists.get(section).mediaId);
				
				async_HttpClient.GET("MediaToFavorites", params, new JsonHttpResponseHandler(){
					
				});
				
			}
		});
		 itemHolder.imgbtn_shareapp.setOnClickListener(new OnClickListener() {
				
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				AQuery aq = new AQuery(context);
				String url = mediaLists.mediaLists.get(section).coverImage;         
		        File file = aq.getCachedFile(url);

//		        Intent intent = new Intent(Intent.ACTION_SEND);
//		      
//		    	intent.putExtra(android.content.Intent.EXTRA_SUBJECT, global_Data.getAppSetting().appSetting.MEDIA_SHARE_MAIL_SUBJECT+" "+ mediaLists.mediaLists.get(section).mediaUserName);
//		    	intent.putExtra(Intent.EXTRA_TEXT, mediaLists.mediaLists.get(section).mediaShareString +" "+ mediaLists.mediaLists.get(section).mediaShortUrl);
//		    	//intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//		    	  intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
//		    	   intent.setType("image/png");
//				((Activity)context).startActivity(Intent.createChooser(intent, "Share via:"));
//				
				
				
				
				Resources resources = context.getResources();

			    Intent emailIntent = new Intent();
			    emailIntent.setAction(Intent.ACTION_SEND);
			    // Native email client doesn't currently support HTML, but it doesn't hurt to try in case they fix it
			    emailIntent.putExtra(Intent.EXTRA_TEXT, 	FGlobal_Data.getAppSetting_settings("APP_SHARE_TEXT"));
			    emailIntent.putExtra(Intent.EXTRA_SUBJECT,  FGlobal_Data.getAppSetting_settings("APP_SHARE_MAIL_SUBJECT"));
			   // emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
			    emailIntent.setType("message/rfc822");

			    PackageManager pm = context.getPackageManager();
			    Intent sendIntent = new Intent(Intent.ACTION_SEND);     
			    sendIntent.setType("text/plain");


			    Intent openInChooser = Intent.createChooser(emailIntent,"Share via:");

			    List<ResolveInfo> resInfo = pm.queryIntentActivities(sendIntent, 0);
			    List<LabeledIntent> intentList = new ArrayList<LabeledIntent>();        
			    for (int i = 0; i < resInfo.size(); i++) {
			        // Extract the label, append it, and repackage it in a LabeledIntent
			        ResolveInfo ri = resInfo.get(i);
			        String packageName = ri.activityInfo.packageName;
			        if(packageName.contains("android.email")) {
			            emailIntent.setPackage(packageName);
			        } else if(packageName.contains("twitter") || packageName.contains("facebook") || packageName.contains("mms") || packageName.contains("android.gm")) {
			            Intent intent = new Intent();
			            intent.setComponent(new ComponentName(packageName, ri.activityInfo.name));
			            intent.setAction(Intent.ACTION_SEND);
			            intent.putExtra(Intent.EXTRA_TEXT, FGlobal_Data.getAppSetting_settings("APP_SHARE_TEXT"));
			            //intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
			            intent.setType("text/plain");
			            if(packageName.contains("twitter")) {
			            	  intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
					          intent.setType("image/png");
			               
			            } else if(packageName.contains("facebook")) {
			                // Warning: Facebook IGNORES our text. They say "These fields are intended for users to express themselves. Pre-filling these fields erodes the authenticity of the user voice."
			                // One workaround is to use the Facebook SDK to post, but that doesn't allow the user to choose how they want to share. We can also make a custom landing page, and the link
			                // will show the <meta content ="..."> text from that page with our link in Facebook.
			            	 intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
					            intent.setType("image/png");
			            } else if(packageName.contains("mms")) {
			               
			            } else if(packageName.contains("android.gm")) {
			                         
			                intent.setType("message/rfc822");
			            }

			            intentList.add(new LabeledIntent(intent, packageName, ri.loadLabel(pm), ri.icon));
			        }
			    }

			    // convert intentList to array
			    LabeledIntent[] extraIntents = intentList.toArray( new LabeledIntent[ intentList.size() ]);

			    openInChooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, extraIntents);
			    ((Activity)context).startActivity(openInChooser); 
				
				
			
			}
		});
		 
		 
		 
		 itemHolder.imgbtn_delete.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			
				final Dialog dialog;
				dialog = new Dialog(context);
				 if(Integer.parseInt(mediaLists.mediaLists.get(section).currentUserIsOwner)==0){
					 dialog.setTitle("Report");
				 }else{
					 dialog.setTitle("Delete Highlight?");
				 }
				
				
				LinearLayout layout = new LinearLayout(context);
				
				
				
				Button btn_delete = new Button(context);
				Button btn_cancel = new Button(context);
				
				
				layout.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
				layout.setPadding(4, 4, 4, 4);
				layout.setOrientation(LinearLayout.VERTICAL);
				
				btn_delete.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
				btn_cancel.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
//				
				btn_delete.setBackgroundResource(R.drawable.layerlist_whiteshapewithlinebelow);
				btn_cancel.setBackgroundResource(R.color.white);
				
				
				btn_delete.setTextColor(Color.RED);
				btn_cancel.setTextColor(Color.BLUE);
				

				 if(Integer.parseInt(mediaLists.mediaLists.get(section).currentUserIsOwner)==0){
						btn_delete.setText("Report Inappropriate");
				 }else{
						btn_delete.setText("Delete");
				 }
			
				
				btn_cancel.setText("Cancel");
				
				

				
				btn_delete.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						
							
							 RequestParams params = new RequestParams();
								params.put("mediaId", mediaLists.mediaLists.get(section).mediaId);
								
								async_HttpClient.GET("DeleteMedia", params, new JsonHttpResponseHandler(){
									
								});
								
								 
								// mediaLists.mediaLists.remove(section);
							//	 notifyDataSetChanged();
						 
						
					}
				});
				
				
				btn_cancel.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				});
				
				
				
				
				layout.addView(btn_delete);
				layout.addView(btn_cancel);
				dialog.setContentView(layout);
				dialog.show();
				
			}
		});
		 
		 
		 itemHolder.imgbtn_comment.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			
				commentView.setVisibility(View.VISIBLE);
				final EditText edittxt_comment  = (EditText)commentView.findViewById(R.id.edittext_comment);
				Button btn_send = (Button)commentView.findViewById(R.id.btn_send);
			
				InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.showSoftInput(edittxt_comment, InputMethodManager.SHOW_IMPLICIT);
				
				commentView.findViewById(R.id.view_outside).setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						commentView.setVisibility(View.GONE);
						InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.showSoftInput(edittxt_comment, InputMethodManager.HIDE_IMPLICIT_ONLY);
					}
				});
				
				
				btn_send.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						 RequestParams requestParams = new RequestParams();
						// requestParams.put("mediaId", mediaLists.mediaLists.get(section).mediaId);
						 requestParams.put("parentId", mediaLists.mediaLists.get(section).mediaId);
						 requestParams.put("type", "media");
						 requestParams.put("comment",edittxt_comment.getText().toString());
						 
						
						 	async_HttpClient.POST("AddComment", requestParams, new JsonHttpResponseHandler(){
						 		@Override
						 		public void onSuccess(JSONObject response) {
						 			// TODO Auto-generated method stub
						 			super.onSuccess(response);
						 			
						 			Log.v("comment", "Success"+response.toString());
						 			
						 			commentView.setVisibility(View.GONE);
						 		}
						 		
						 		@Override
						 		public void onFailure(int arg0, Header[] arg1, byte[] arg2,
						 				Throwable arg3) {
						 			// TODO Auto-generated method stub
						 			super.onFailure(arg0, arg1, arg2, arg3);
						 		}
						 	});
					}
				});
			}
		});
		 
		 
		 
		 itemHolder.imgbtn_tag.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				String[] list_tag = new String[]{"Goal","Shot","Almost","Save","Penalty","Defense","Ouch"};
				final Dialog dialog;
				dialog = new Dialog(context);
				 dialog.setTitle("Hockey Tag");
				 
				
				
				LinearLayout layout = new LinearLayout(context);
						
				layout.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
				layout.setPadding(4, 4, 4, 4);
				layout.setOrientation(LinearLayout.VERTICAL);
				
				for (int i = 0; i < list_tag.length; i++) {
					Button btn = new Button(context);
					btn.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
					btn.setBackgroundResource(R.drawable.layerlist_whiteshapewithlinebelow);
					btn.setTextColor(Color.BLUE);
					btn.setTag(i);
					btn.setText(list_tag[i]);
					
					layout.addView(btn);
				}
				
			
				Button btn_cancel = new Button(context);
				btn_cancel.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
				btn_cancel.setBackgroundResource(R.color.white);
				btn_cancel.setTextColor(Color.BLUE);
				btn_cancel.setText("Cancel");
				
				layout.addView(btn_cancel);
				
				dialog.setContentView(layout);
				dialog.show();
				
			}
		});
//		 
//		 itemHolder.imgbtn_comment.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//			//	CommentActivity commentActivity = new CommentActivity().newInstance(mediaLists.mediaLists.get(section).mediaId);
//				
//				Intent intent = new Intent(context,CommentActivity.class);
//				intent.putExtra("mediaId", mediaLists.mediaLists.get(section).mediaId);
//				
//				
//				global_Data.getTabGroupActivity2().startChildActivity("Comments", intent);
//				
//				
//			}
//		});
//		 

//		 
//		 itemHolder.imgbtn_expand.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				Intent intent = new Intent(context, VideoFullScreenActivity.class);
//				intent.putExtra("mediaUrl",  mediaLists.mediaLists.get(section).mediaUrl);
//				((Activity)context).startActivity(intent);
//			}
//		});
		
		
		return convertView;
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
		if(numberOfSections()==0){
			return null;
		}
		
		HeaderHolder headerHolder;
		if(convertView==null){
			LayoutInflater inflater = ((Activity)context).getLayoutInflater();
			convertView = inflater.inflate(R.layout.list_header_highlight, null);
			
			headerHolder = new HeaderHolder(); 
			headerHolder.imgvw_avatar 	= (SmartImageView)convertView.findViewById(R.id.imgvw_highlight_avatar);
			headerHolder.txtvw_title 	= (TextView)convertView.findViewById(R.id.txtvw_highlight_title);
			headerHolder.txtvw_date 	= (TextView)convertView.findViewById(R.id.txtvw_highlight_date); 
			convertView.setTag(headerHolder); 
		}
		else{
			headerHolder = (HeaderHolder)convertView.getTag();
		}
		
		if(Integer.parseInt(mediaLists.mediaLists.get(section).user.avatarCount)>0){
			
			headerHolder.imgvw_avatar.getLayoutParams().height = drawable_avatar.getMinimumHeight();
			headerHolder.imgvw_avatar.getLayoutParams().width = drawable_avatar.getMinimumWidth();
			headerHolder.imgvw_avatar.setImageUrl(mediaLists.mediaLists.get(section).user.avatarUrl);
			
		}
		
//		Toast.makeText(context, "fullname : "+mediaLists.mediaLists.get(0).user.fullName+
//				"\n username :"+mediaLists.mediaLists.get(0).user.userName, Toast.LENGTH_SHORT).show();
//		
		
		if(mediaLists.mediaLists.get(0).user.fullName.length()>0){
			headerHolder.txtvw_title.setText(mediaLists.mediaLists.get(0).user.fullName);
		}else if(mediaLists.mediaLists.get(0).user.userName.length()>0){
			headerHolder.txtvw_title.setText(mediaLists.mediaLists.get(0).user.userName);
		}
		else{
			headerHolder.txtvw_title.setText(mediaLists.mediaLists.get(0).user.userId);
		}
		
		//headerHolder.txtvw_title.setText(mediaLists.mediaLists.get(section).mediaUserName);
		headerHolder.txtvw_date.setText(mediaLists.mediaLists.get(section).age);
		
		final Intent intent = new Intent(context,Profile_Activity.class);
		intent.putExtra("userId", mediaLists.mediaLists.get(section).mediaUserId);
	
		headerHolder.txtvw_title.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				((Activity)context).startActivity(intent);
			}
		});
		
		return convertView;
	}

	
	public void onShareClick(String mediaId,final String service){
		
		RequestParams requestParams = new RequestParams();
		requestParams.put("mediaId", mediaId);
		requestParams.put("service", service);
		
		async_HttpClient.GET("MediaWasShared", requestParams, new JsonHttpResponseHandler(){
			@Override
			public void onSuccess(JSONObject response) {
				// TODO Auto-generated method stub
				super.onSuccess(response);
				
				String error = "0";
				try {
					error = response.getString("error");
				} catch (JSONException e) {e.printStackTrace();}
				
				if (Integer.parseInt(error)==0) {
					Toast.makeText(context, "shared through "+service, Toast.LENGTH_SHORT).show();
				}
				else{
					Toast.makeText(context, "failed to share", Toast.LENGTH_SHORT).show();
				}
			}
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				// TODO Auto-generated method stub
				super.onFailure(arg0, arg1, arg2, arg3);
				Toast.makeText(context, "failed to share", Toast.LENGTH_SHORT).show();
			}
		});
	}
	
}
