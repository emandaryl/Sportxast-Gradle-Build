package com.sportxast.SportXast.commons;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.sportxast.SportXast.R;
import com.sportxast.SportXast.activities2_0.VideoCaptureActivity;
 
 
/** Context context, int userId, int setMasterID **/
public class DialogSettings extends  android.app.Dialog{
	
	private TextView prompt_title; //@string/settings 
	private TextView prompt_eventName; //@string/settings  
	private Button prompt_btn_yes;
	private Button prompt_btn_no;
	
	private Context mContext;  
	//private int FUserID;  
	public DialogSettings(final Context context, final int promptMessageType, String eventName) { 
		super(context); 
		
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		this.getWindow().setBackgroundDrawable(new ColorDrawable(0));
		this.mContext = context; 
		
		this.setContentView(R.layout.layout_dialog_settings);
		this.setCanceledOnTouchOutside(false);
		   
		this.prompt_title	    = (TextView)findViewById(R.id.prompt_title);
		this.prompt_eventName	= (TextView)findViewById(R.id.prompt_eventName);
        this.prompt_btn_yes     = (Button)findViewById(R.id.prompt_btn_yes);
		 
		if(promptMessageType == 1){ //ASK to check into an event 
			this.prompt_title.setText("Check Into Event?");
			prompt_eventName.setText(eventName); 
			//final int latestEventId = Integer.parseInt( latestEvent.eventId ); 
			this.prompt_btn_yes.setOnClickListener(new View.OnClickListener() { 
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub  
					GlobalVariablesHolder.alreadyCheckedIntoAnEvent = true; 
					 
					DialogSettings.this.dismiss(); 
					//############################################
					 
					if(mContext instanceof VideoCaptureActivity){
						( (VideoCaptureActivity)mContext ).supplyChosenEvent( GlobalVariablesHolder.FLatestEvent, "-1" );
					}
				}
			});
			
			this.prompt_btn_no = (Button)findViewById(R.id.prompt_btn_no);
			this.prompt_btn_no.setOnClickListener(new View.OnClickListener() { 
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					
					GlobalVariablesHolder.alreadyCheckedIntoAnEvent = false; 
					DialogSettings.this.dismiss(); 
					//############################################
					if(mContext instanceof VideoCaptureActivity){
						( (VideoCaptureActivity)mContext ).showCheckIntoDialog(2, "");
					} 
					
				}
			});
			 
		}
		else if(promptMessageType == 2)//ASK to CREATE an event
		{
			this.prompt_title.setText("Create an Event?");
			this.prompt_eventName.setText("Label you event so other people may find to capture, watch and share highlights");
			
			this.prompt_btn_yes.setOnClickListener(new View.OnClickListener() { 
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub  
					DialogSettings.this.dismiss();

					if(mContext instanceof VideoCaptureActivity){
						((VideoCaptureActivity) mContext ).gotoCreateEventPage(1);
					}
				}
			});

			this.prompt_btn_no = (Button)findViewById(R.id.prompt_btn_no);
			this.prompt_btn_no.setOnClickListener(new View.OnClickListener() { 
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					DialogSettings.this.dismiss();
                    //############################################
                    if(mContext instanceof VideoCaptureActivity){
                        //Proceed to Saving, WITHOUT EVENT OR NOT CHECKED INTO AN EVENT
                        ( (VideoCaptureActivity)mContext ).supplyChosenEvent( null, "-1" );
                }
				}
			});
		
		} 
	}
	
	private String getVersionInfo(){
		String versionName = "";
		PackageInfo pinfo;
		
		try{
			pinfo = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0);
			versionName = pinfo.versionName;
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return versionName;
	}
	
	public interface ConfirmChangeLangListener{ public void onConfirmChangeLangClick();}
	 
	  
	private ConfirmChangeLangListener confirmChangeLangClick;
	 
	  
	public void setOnConfirmChangeLangListener(ConfirmChangeLangListener confirmChangeLangClick){
		this.confirmChangeLangClick = confirmChangeLangClick;
	} 
}
