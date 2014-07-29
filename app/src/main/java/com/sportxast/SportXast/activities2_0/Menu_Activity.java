package com.sportxast.SportXast.activities2_0;

import android.app.Activity;
import android.content.ContentValues;
import android.os.Bundle;
import android.widget.RelativeLayout;

import com.sportxast.SportXast.R;
import com.sportxast.SportXast.adapter2_0.MenuAdapter;
import com.sportxast.SportXast.includes.HeaderUIClass;
import com.sportxast.SportXast.thirdparty_class.HeaderListView;

import java.util.ArrayList;

//import com.sportxast.SportXast.BaseSherlockActivity;

//public class Menu_Activity extends BaseSherlockActivity {

public class Menu_Activity extends Activity {
	//TODO: Display list Menu
	
	/** Called when the activity is first created. */
	private HeaderUIClass headerUIClass;
	private RelativeLayout sx_header_wrapper; 
	private HeaderListView headerListView; 
	private String[] header = new String[] { "EVENTS", "OTHER" };
	private ArrayList<ContentValues> list_events = new ArrayList<ContentValues>();
	private ArrayList<ContentValues> list_others = new ArrayList<ContentValues>();
 
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    // TODO Auto-generated method stub
	    setContentView(R.layout.layout_menu);
	    /*
	    initActionBarObjects();
	    getActionbar_Menu();
	    */ 
	    prepareHeader();
	    initMenuView(); 
	}

	private void prepareHeader(){ 
		this.sx_header_wrapper = (RelativeLayout)findViewById(R.id.sx_header_wrapper);
		this.headerUIClass = new HeaderUIClass(this, sx_header_wrapper); 
		this.headerUIClass.setMenuTitle("Menu");
	  
		this.headerUIClass.showBackButton(	 true ); 
		this.headerUIClass.showAddButton(	 false);  
		this.headerUIClass.showMenuButton(	 false);  
		this.headerUIClass.showRefreshButton(false);  
		this.headerUIClass.showAboutButton(	 false); 
		this.headerUIClass.showSearchButton( false); 
		this.headerUIClass.showDoneButton(	 false);  
		this.headerUIClass.showCameraButton( false); 
		this.headerUIClass.showMenuTitle(	 true ); 
		
		//this.headerUIClass.setMenuTitle(	 false);
		this.headerUIClass.showMenuTitle0(	 false); 
		addHeaderButtonListener(); 
	}

	private void addHeaderButtonListener(){
		this.headerUIClass.setOnHeaderButtonClickedListener(new HeaderUIClass.OnHeaderButtonClickedListener() {
			
			@Override
			public void onSearchClicked() {
				// TODO Auto-generated method stub 
			}
			
			@Override
			public void onRefreshClicked() {
				// TODO Auto-generated method stub 
			}
			
			@Override
			public void onMenuClicked() {
				// TODO Auto-generated method stub 
			}
			
			@Override
			public void onDoneClicked() {
				// TODO Auto-generated method stub 
			}
			
			@Override
			public void onCameraClicked() {
				// TODO Auto-generated method stub 
			}
			
			@Override
			public void onBackClicked() {
				// TODO Auto-generated method stub
				finish();
			}
			
			@Override
			public void onAddClicked() {
				// TODO Auto-generated method stub 
			}
			
			@Override
			public void onAboutClicked() {
				// TODO Auto-generated method stub 
			}
		});
	}
	/*
	
	@Override
	public void onBack(View v) {
		// TODO Auto-generated method stub
		super.onBack(v);
		finish();
	}
	
	*/
	 
	public void initMenuView(){
		setEventsItems();
		setOthersItems();
		headerListView = (HeaderListView)findViewById(R.id.headerListView_menu);
		headerListView.setAdapter(new MenuAdapter(this,header,list_events,list_others));
	}
	
	public void setActionBar(){
		
	} 
	
	public void setEventsItems() {
		
		ContentValues c0 = new ContentValues();
		c0.put("title", "Home");
		c0.put("icon", R.drawable.ic_home);

		ContentValues c1 = new ContentValues();
		c1.put("title", "Search");
		c1.put("icon", R.drawable.ic_search);

		ContentValues c2 = new ContentValues();
		c2.put("title", "Create");
		c2.put("icon", R.drawable.ic_create);

		ContentValues c3 = new ContentValues();
		c3.put("title", "Capture");
		c3.put("icon", R.drawable.ic_camera);

		ContentValues c4 = new ContentValues();
		c4.put("title", "Popular");
		c4.put("icon", R.drawable.ic_popular);

		ContentValues c5 = new ContentValues();
		c5.put("title", "Nearby");
		c5.put("icon", R.drawable.ic_nearby);

		ContentValues c6 = new ContentValues();
		c6.put("title", "Favorite");
		c6.put("icon", R.drawable.ic_favorite);

		list_events.add(c0);
		list_events.add(c1);
		list_events.add(c2);
		list_events.add(c3);
		list_events.add(c4);
		list_events.add(c5);
		list_events.add(c6);
	}

	public void setOthersItems() {

		ContentValues c1 = new ContentValues();
		c1.put("title", "Notification");
		c1.put("icon", R.drawable.ic_notification);

		ContentValues c2 = new ContentValues();
		c2.put("title", "Share App");
		c2.put("icon", R.drawable.ic_share_blue);

		ContentValues c3 = new ContentValues();
		c3.put("title", "Profile");
		c3.put("icon", R.drawable.ic_profile);

		ContentValues c4 = new ContentValues();
		c4.put("title", "Information");
		c4.put("icon", R.drawable.ic_info);

		list_others.add(c1);
		list_others.add(c2);
		list_others.add(c3);
		list_others.add(c4);
	}

	

}
