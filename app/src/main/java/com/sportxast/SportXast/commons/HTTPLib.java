package com.sportxast.SportXast.commons;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings.Secure;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class HTTPLib{
	 private Context FContext;
	 private ProgressDialog Progress;
	 private onDownloadDoneListener DownloadDoneListener = null;
	 private Resources res;
	 public interface onDownloadDoneListener{ public void onDownloadDone(boolean AComplete);}
	 
	 private String maxVal;
	 private String progVal;
	 
	 public HTTPLib(Context Caller){
		 this.FContext = Caller;
	 }
	 
	 //HTTP Request
	 public InputStream HTTPRequest(String AURL, List<BasicNameValuePair> AParams, final String keyProfileSessionID){
		 DefaultHttpClient client = new DefaultHttpClient();
	     HttpPost httppost = new HttpPost(AURL);
	     
	     httppost.addHeader("USERID", 		Secure.getString(FContext.getContentResolver(), Secure.ANDROID_ID));
	     httppost.addHeader("USERSESSION", 	keyProfileSessionID); 
	     //httppost.addHeader("USERSESSION", 	sharedPreferences.getString(KEY_PROFILE.SESSION_ID, ""));
	     httppost.addHeader("PHONELANGUAGE",Locale.getDefault().getLanguage());  
	     String timezone_ = TimeZone.getDefault().getID();
		 httppost.addHeader("TIMEZONE", ""+TimeZone.getDefault().getID());
	      
	     try {
			UrlEncodedFormEntity p_entity = new UrlEncodedFormEntity(AParams, HTTP.UTF_8);
			httppost.setEntity(p_entity);
			HttpResponse response = client.execute(httppost);
			HttpEntity responseEntity = response.getEntity();
			InputStream insstr = responseEntity.getContent();
			return insstr;
	     } 
	     catch (Exception e) 
	     { 
	    	 String error = "-1";
	    	 ByteArrayInputStream bais = new ByteArrayInputStream(error.getBytes());
	    	 return bais;
	     } 
	 }
	 
	 public String convertStreamToString(InputStream is) throws IOException {
        if (is != null) {
            StringBuilder sb = new StringBuilder();
            String line;
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append("\n");
                }
            } finally {
                is.close();
            }
            return sb.toString();
        } else {        
            return "";
        }
    }
	
	//HTTP Download 
	public void HTTPDownload(String ASource, String ADestination){
		try{
			  res = FContext.getResources();
		    Progress = new ProgressDialog(FContext);
		    
		  //  Progress.setProgressDrawable(Caller.getResources().getDrawable(R.drawable.progressbar_irender));
		  
		    Progress.setMessage("Downloading...");
		    //  Progress.setMessage(res.getString(R.string.gather_data));
		    Progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		    Progress.setCancelable(false);
		    Progress.show();
		 //   Progress.setProgressNumberFormat("%1d KB/%2d KB");
		    ProgressThread progThread = new ProgressThread(handler, ASource, ADestination);
		    progThread.start();
		}
		catch(Exception e){
			String s = e.getMessage();
			String m = s;
			e.printStackTrace();
		}
	}
	
	public void setonDownloadDoneListener(onDownloadDoneListener aDownloadDoneListener){
		this.DownloadDoneListener = aDownloadDoneListener;
	}
	
	private int convertKBtoMB(int kilobytes_)
	{
		int convertedValue = 0;
		
		if(kilobytes_ < 1024)
			convertedValue =  0;
		else if(kilobytes_ == 1024)
			convertedValue =  1;
		else if(kilobytes_ > 1024)
		{
			//int samp = kilobytes_ % 1048576;
			convertedValue = kilobytes_ / 1024;
			 
		}
		return convertedValue;
	} 
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg){
			super.handleMessage(msg);
			switch(msg.arg1){
			case 1: // set total max;
				//Progress.setMax(msg.arg2);//ORIGINAL
				Progress.setMax(convertKBtoMB( msg.arg2 ) );
				maxVal = Integer.toString( convertKBtoMB( msg.arg2 ) ) + "MB";
			break;
			case 2: //update progress;
				//Progress.setProgress(msg.arg2);//ORIGINAL
				Progress.setProgress(convertKBtoMB( msg.arg2 ) );
				progVal = Integer.toString( convertKBtoMB( msg.arg2 ) ) + "MB";
			//	Progress.setMessage(res.getString(R.string.gather_data) +"    "+  progVal +" out of "+ maxVal);
				
			break;
			case 3: //dismiss progress dialog;
				Progress.dismiss();
				if(DownloadDoneListener != null){
					boolean bComplete = msg.arg2 == 1?true:false;
					DownloadDoneListener.onDownloadDone(bComplete);
				}
			break;
			default:
				//do nothing;
			break;
			}
		}
	};
	
	private class ProgressThread extends Thread{
		Handler mHandler;
		String Source;
		String Destination;
		
		ProgressThread(Handler mHandler, String Source, String Destination){
			this.mHandler = mHandler;
			this.Source = Source;
			this.Destination = Destination;
		}
		
		public void run(){
			 
			
    		try{
    			URL u = new URL(Source);
			    HttpURLConnection c = (HttpURLConnection) u.openConnection();
			    c.setRequestMethod("GET");
			    c.setRequestProperty("Content-Type", "application/zip");
			    c.setRequestProperty("Connection", "Keep-Alive");
			    c.setDoOutput(true);
			    c.connect();
			    FileOutputStream f = new FileOutputStream(new File(Destination));
			    InputStream in = c.getInputStream();
			    
			    Message myMessage = mHandler.obtainMessage();
			    myMessage.arg1 = 1;// set total max;
			    myMessage.arg2 = c.getContentLength();
			    mHandler.sendMessage(myMessage);
			    byte[] buffer = new byte[1024];
			    int totallen = 0;
			    int len1 = 0;
			    len1 = in.read(buffer);
			    while(len1 > 0){
			    	 f.write(buffer, 0, len1);
			         totallen += len1;
			         Message myMessage1 = mHandler.obtainMessage();
			         myMessage1.arg1 = 2; // set progress;
			         myMessage1.arg2 = totallen;
			         mHandler.sendMessage(myMessage1);
			         
			         len1 = in.read(buffer);
			    }
			    f.close();
			    Message myMessage1 = mHandler.obtainMessage();
    			myMessage1.arg1 = 3;
    			myMessage1.arg2 = totallen >= c.getContentLength()?1:0;
			    mHandler.sendMessage(myMessage1);
	    	}
    		/*catch(Exception ex){
    			
    			Toast.makeText(Caller, "nothing found", Toast.LENGTH_LONG).show();
    			Message myMessage = mHandler.obtainMessage();
    			myMessage.arg1 = 3;
    			myMessage.arg2 = 0;
    			mHandler.sendMessage(myMessage);
    		}*/
    		catch (MalformedURLException e) {
    			//Toast.makeText(Caller, "nothing found", Toast.LENGTH_LONG).show();
    			Message myMessage = mHandler.obtainMessage();
    			myMessage.arg1 = 3;
    			myMessage.arg2 = 0;
    			mHandler.sendMessage(myMessage);
				// TODO Auto-generated catch block
			//	e.printStackTrace();
			} catch (IOException e) {
				
			//	Toast.makeText(Caller, "nothing found", Toast.LENGTH_LONG).show();
    			Message myMessage = mHandler.obtainMessage();
    			myMessage.arg1 = 3;
    			myMessage.arg2 = 0;
    			mHandler.sendMessage(myMessage);
				
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
		}
	}

	public boolean HTTPUpload(String AURL, File AFile){
		FileInputStream fileInputStream = null; 
		URL connectURL;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        String Tag="3rd";

		if(AFile.exists()){
			if(AFile.canRead()){
	            try
	            {
	            	String sName = AFile.getName();
	            	connectURL = new URL(AURL);
					fileInputStream = new FileInputStream(AFile);
                    //------------------ CLIENT REQUEST

                    Log.e(Tag,"Starting to bad things");
                    // Open a HTTP connection to the URL

                    HttpURLConnection conn = (HttpURLConnection) connectURL.openConnection();
                    // Allow Inputs
                    conn.setDoInput(true);
                    // Allow Outputs
                    conn.setDoOutput(true);
                    // Don't use a cached copy.
                    conn.setUseCaches(false);
                    // Use a post method.
                    conn.setRequestMethod("POST");
                    conn.setRequestProperty("Connection", "Keep-Alive");
                    conn.setRequestProperty("Content-Type", "multipart/form-data;boundary="+boundary);
                    DataOutputStream dos = new DataOutputStream( conn.getOutputStream() );
                    dos.writeBytes(twoHyphens + boundary + lineEnd);
                    dos.writeBytes("Content-Disposition: form-data; name=\"uploadedfile\";filename=\"" + sName +"\"" + lineEnd);
                    dos.writeBytes(lineEnd);

                    Log.e(Tag,"Headers are written");
                    // create a buffer of maximum size
                    int bytesAvailable = fileInputStream.available();
                    int maxBufferSize = 1024;
                    int bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    byte[ ] buffer = new byte[bufferSize];

                    // read file and write it into form...

                    int bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                    while (bytesRead > 0)
                    {
                        dos.write(buffer, 0, bufferSize);
                        bytesAvailable = fileInputStream.available();
                        bufferSize = Math.min(bytesAvailable, maxBufferSize);
                        bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                    }

                    // send multipart form data necesssary after file data...
                    dos.writeBytes(lineEnd);
                    dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                    // close streams
                    Log.e(Tag,"File is written");
                    fileInputStream.close();
                    dos.flush();

                    InputStream is = conn.getInputStream();
                    // retrieve the response from server
                    int ch;

                    StringBuffer b =new StringBuffer();
                    while( ( ch = is.read() ) != -1 ){
                            b.append( (char)ch );
                    }
                    dos.close();


	            }	
	            catch (MalformedURLException ex)
	            {
	                    Log.e(Tag, "error URL: " + ex.getMessage(), ex);
	                    return false;
	            }
	
	            catch (IOException ioe)
	            {
	                   Log.e(Tag, "error IO: " + ioe.getMessage(), ioe);
	                   return false;
	            }
			}
			else{
				return false;
			}
		}
		else{
			return false;
		}
		return true;
	}
}
