package com.sportxast.SportXast.commons;
 
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.os.Handler;
import android.os.Message;

public class Dialog {
	
	
	private static ProgressDialog FProgress;
	
	private static int dialogSettingsResult;
	private static AlertDialog.Builder dialog;
	
	public static void AlertMe(Context AContext, String ATitle, String AAlert, OnClickListener AListener){
		AlertDialog.Builder ab2 = new Builder(AContext);
	   	ab2.setCancelable(false);
	   	ab2.setTitle(ATitle);
		ab2.setMessage(AAlert);
		ab2.setNegativeButton("OK", AListener);
		ab2.show();
	}
	
	public static void QuestionMe(Context aContext, String aTitle, String anAlert, OnClickListener positiveBtnListener, OnClickListener negativeBtnListener) {
		AlertDialog.Builder dialog = new AlertDialog.Builder(aContext);
		dialog.setCancelable(false);
		dialog.setTitle(aTitle);
		dialog.setMessage(anAlert);
		dialog.setPositiveButton("Yes", positiveBtnListener);
		dialog.setNegativeButton("No", negativeBtnListener);
		//dialog.setIcon(R.drawable.question);
		dialog.show();
	}
	
	public static void ShowProgressSpinner(Context AContext, String AMessage){
		try{
			if(FProgress != null){
				if(FProgress.isShowing()){
					FProgress.dismiss();
				}
			}
			FProgress = new ProgressDialog(AContext);
		    //FProgress.setTitle(AContext.getString(R.string.please_wait));
			FProgress.setTitle("Please wait");
		    FProgress.setMessage(AMessage);
		    FProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		    FProgress.setCancelable(false);
		    FProgress.show();
		}
		catch(Exception e){
			//don't know what to do here :)
		}
	}
	
	public static void ShowProgressSpinner(Context AContext, String ATitle, String AMessage){
		try{
			if(FProgress != null){
				if(FProgress.isShowing()){
					FProgress.dismiss();
				}
			}
			FProgress = new ProgressDialog(AContext);
			FProgress.setTitle(ATitle);
			FProgress.setMessage(AMessage);
			FProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			FProgress.setCancelable(false);
			FProgress.show();
		}
		catch(Exception e){
			//don't know what to do here :)
		}
	} 
	
	
	public static void DismissProgressSpinner(){
		Message m = FHandler.obtainMessage();
		m.arg1 = 1;
		FHandler.sendMessage(m);
	}
	
	
	private static Handler FHandler = new Handler(){
		@Override
		public void handleMessage(Message msg){
			switch(msg.arg1){
			case 1: //dismiss
				FProgress.dismiss();
			break;
			}
		}
	};
	 
 //###############################################################
	
	public static int getDialogSettingsResult(){
		return dialogSettingsResult;
	}
	
	private static int selectedLanguage; 
	
	public static int showSettingsDialog(DialogSettings dialog, final Context context){
		 dialogSettingsResult = 0;
		  
		dialog.show();
		  
		/*
		dialog.setOnUpdateClickListener(new DialogSettings.UpdateClickListener() {
			
			@Override
			public void onUpdateClick() {
				 dialogSettingsResult = 1;

			}
		});
		*/  
		return dialogSettingsResult;
	}
	  
	public static void showDialogNoButtons(Context AContext, String ATitle, String AAlert){
		dialog = new Builder(AContext);
		dialog.setCancelable(true);
		dialog.setTitle(ATitle);
		dialog.setMessage(AAlert);
		dialog.show();
	}
	
}




