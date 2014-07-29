package com.sportxast.SportXast.commons;

import android.content.Context;
import android.net.ConnectivityManager;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class TestConnection {
	
	public static boolean checkInternetConnection( Context FContext ) 
	 {
		ConnectivityManager cm = (ConnectivityManager) FContext.getSystemService(  Context.CONNECTIVITY_SERVICE );
        if (cm.getActiveNetworkInfo() != null
                && cm.getActiveNetworkInfo().isAvailable()
                && cm.getActiveNetworkInfo().isConnected()) {
            return true;
        } else {
            return false;
        }
	  }
	
	public static boolean pingHost(String host) {
		
		//host = host.replace("http://", ""); //remove http://
		
		if(host.contains("https://")){
			host = host.replace("https://", ""); //remove https://
			
		}else if(host.contains("http://")){
			host = host.replace("http://", ""); //remove http://
		}
		
		boolean result = false;
		try {
			StringBuffer echo = new StringBuffer();
			Runtime runtime = Runtime.getRuntime();
			Process proc = runtime.exec("ping -c 2 " + host);
			proc.waitFor();
			int exit = proc.exitValue();
			
			if (exit == 0) {
				InputStreamReader reader = new InputStreamReader(
						proc.getInputStream());
				BufferedReader buffer = new BufferedReader(reader);
				String line = "";
				while ((line = buffer.readLine()) != null) {
					echo.append(line + "\n");
				}
				result = true;
			} else {
				result = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}
	
	    
}







