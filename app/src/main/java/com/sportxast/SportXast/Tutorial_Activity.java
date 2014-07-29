package com.sportxast.SportXast;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.sportxast.SportXast.activities2_0.VideoCaptureActivity;
import com.sportxast.SportXast.commons.Constants;

import java.util.ArrayList;

//import com.actionbarsherlock.view.Window;

public class Tutorial_Activity extends Activity {
	private ViewPager viewPager;
	private PagerAdapter pagerAdapter;
	 
	private Button btn_next; 
	//Button btn_allowaccess; 
	private ArrayList<ImageView> FImageViewDotsList;
	 
	private int FPageLimit = 6;
	private ArrayList<ImageView> populatePageDots(){
		
		ArrayList<ImageView> imageViewDotsList = new ArrayList<ImageView>(); 
	    LayoutInflater infalInflater = (LayoutInflater) getBaseContext().getSystemService(getBaseContext().LAYOUT_INFLATER_SERVICE);
	    LinearLayout pageDotCont1 = (LinearLayout) findViewById(R.id.pageDotCont1); 
		  
	    for (int i = 0; i < FPageLimit; i++) { 
	    	ImageView lyt1 = (ImageView) infalInflater.inflate( R.drawable.pagedot_item, null);
			lyt1.setTag(i);
			
			LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
			//lp.setMargins(left, top, right, bottom);
			lp.setMargins(5, 0, 5, 0);
			lyt1.setLayoutParams(lp);
			  
			lyt1.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					int tagIndex = Integer.parseInt( v.getTag().toString() ); 
					//Toast.makeText(getApplicationContext(), Integer.toString(tagIndex), Toast.LENGTH_LONG).show();
					viewPager.setCurrentItem( tagIndex );
				}
				
			});
			  
			pageDotCont1.addView(lyt1);
			//pageDotCont1.addView(lyt1, new LinearLayout.LayoutParams(190, LayoutParams.WRAP_CONTENT)); 
			//pageDotCont1.addView(lyt1, new RelativeLayout.LayoutParams(190, LayoutParams.WRAP_CONTENT));
			
			//(ImageView) dotsList[i] = (ImageView) pageDotCont1.getChildAt(i);   
			imageViewDotsList.add( (ImageView) pageDotCont1.getChildAt(i) ); 
			//( (ImageView) dotsList[i] ).get
		}
		
		return imageViewDotsList; 
	}
	
	private boolean alreadyOnTheTryItPage;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    requestWindowFeature(Window.FEATURE_NO_TITLE);
	    // TODO Auto-generated method stub
	    setContentView(R.layout.layout_tutorial);
	    
	    alreadyOnTheTryItPage = false;
	    viewPager= (ViewPager)findViewById(R.id.pager);
	    btn_next = (Button)findViewById(R.id.btn_next);
	    //btn_allowaccess = (Button)findViewById(R.id.btn_allowaccess);
	    
	    /* 
	    dots = new ImageView[]{ ((ImageView)findViewById(R.id.p1)),((ImageView)findViewById(R.id.p2)),((ImageView)findViewById(R.id.p3))};
	    	//	((ImageView)findViewById(R.id.p4)),((ImageView)findViewById(R.id.p5)),((ImageView)findViewById(R.id.p6))};
	    */
	    
	    FImageViewDotsList = populatePageDots();
	    
	    //dots[0].setSelected(true);
	    ((ImageView) FImageViewDotsList.get(0)).setSelected(true);
	    pagerAdapter = new PagerAdapter() {
			
	    	@Override
			public boolean isViewFromObject(View arg0, Object arg1) {
				// TODO Auto-generated method stub
				return  arg0 == ((ImageView) arg1);
			}

			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				//return 3;
				return FPageLimit;
			}
			 
			@Override
			public Object instantiateItem(View container, int position) {
				// TODO Auto-generated method stub
				ImageView imageView = new ImageView(Tutorial_Activity.this); 
				imageView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.MATCH_PARENT));
				//imageView.getLayoutParams().width = scaleSizeToDP(Tutorial_Activity.this, 166);
				//imageView.getLayoutParams().height = scaleSizeToDP(Tutorial_Activity.this, 96);
				//imageView.setAdjustViewBounds(true);
				//imageView.setScaleType(ScaleType.); 
				
				alreadyOnTheTryItPage = false;
				int icon = 0;
				switch (position) {
				case 0:{icon = R.drawable.tutorial1;}
					break;
				case 1:{icon = R.drawable.tutorial2;}
					break;
				case 2:{icon = R.drawable.tutorial3;}
					break;
				case 3:{icon = R.drawable.tutorial4;}
					break;
				case 4:{icon = R.drawable.tutorial5;}
					break;
				case 5:{
					alreadyOnTheTryItPage = true;
					icon = R.drawable.tutorial6;
					} 
					break;
				default:
					{icon = R.drawable.tutorial0;}
					break;
				}
				 
				imageView.setImageResource(icon); 
				((ViewPager) container).addView(imageView);
				return imageView;
			}

			
			@Override
			public void destroyItem(ViewGroup container, int position,
					Object object) {
				// TODO Auto-generated method stub
				((ViewPager) container).removeView((View) object);
			}
			
		};
		
		viewPager.setAdapter(pagerAdapter);
		viewPager.setCurrentItem(0);
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				 
				for (int i = 0; i < FImageViewDotsList.size(); i++) {
					ImageView imgvw = (ImageView) FImageViewDotsList.get(i);

					//if (i <= arg0) {
				    if (i == arg0) {
						imgvw.setSelected(true);
					} else {
						imgvw.setSelected(false);
					}
				} 
				
				if( arg0 == (pagerAdapter.getCount()-1) ){
					btn_next.setText("Done");
					//btn_next.setVisibility(View.INVISIBLE);
					//btn_allowaccess.setVisibility(View.VISIBLE);
					
				}else{ 
					btn_next.setText("Next >");
					//btn_allowaccess.setVisibility(View.GONE);
				}
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub 
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub 
			}
			
		});
	     
		btn_next.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Button btn = (Button)v;
				if(btn.getText().toString().equalsIgnoreCase("Done")){  
					//#####################################################################################
					//##################################################################################### 
					final String PREFS_NAME = "MyPrefsFile";
					 
					SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
					if (settings.getBoolean("my_first_time", true)) {  
					    //the app is being launched for first time, do something        
					    Log.d("Comments", "First time"); 
					    // first time task 
					    // record the fact that the app has been started at least once
					    settings.edit().putBoolean("my_first_time", false).commit();  
					}
					//#####################################################################################
					//#####################################################################################
					  
					finish();
					startActivity(new Intent(Tutorial_Activity.this, SportX2_Main.class));
				}else{
					viewPager.setCurrentItem(viewPager.getCurrentItem()+1);
				}
				
				
//				//if(viewPager.getCurrentItem()<(pagerAdapter.getCount()-1))
//					viewPager.setCurrentItem(viewPager.getCurrentItem()+1);
			}
		});
//		btn_allowaccess.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				finish();
//				startActivity(new Intent(Tutorial_Activity.this, SportX2_Main.class));
//			}
//		});
	    
	}
	
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub 
		super.onConfigurationChanged(newConfig); 
		if(newConfig.orientation==Configuration.ORIENTATION_LANDSCAPE){
			if( alreadyOnTheTryItPage ){
				
				Intent intent = new Intent(Tutorial_Activity.this, VideoCaptureActivity.class);
				intent.putExtra("eventId", Constants.default_tutorial_eventID);
				
			//	startActivity(intent);  
				startActivityForResult(intent, Constants.requestCode_Tutorial_Activity); 
			}
			//Toast.makeText(getApplicationContext(), "landscape", Toast.LENGTH_LONG).show();
		}else{
			
			//Toast.makeText(getApplicationContext(), "portrait", Toast.LENGTH_LONG).show();
		}
	}
	

}
