package com.sportxast.SportXast.test;

import android.test.ActivityInstrumentationTestCase2;

import com.robotium.solo.Solo;
import com.sportxast.SportXast.SportX2_Main;
import com.sportxast.SportXast.activities2_0.Create_Activity;
import com.sportxast.SportXast.activities2_0.Information_Activity;
import com.sportxast.SportXast.activities2_0.Menu_Activity;
import com.sportxast.SportXast.activities2_0.Profile_Activity;
import com.sportxast.SportXast.activities2_0.Search_Activity;
import com.sportxast.SportXast.activities2_0.VideoCaptureActivity;
import com.sportxast.SportXast.adapter2_0.MenuAdapter;
import com.sportxast.SportXast.thirdparty_class.HeaderListView;

public class MenuActivityTest extends ActivityInstrumentationTestCase2<Menu_Activity> {

	private Menu_Activity menuActivity;
	private HeaderListView headerListView;
	private MenuAdapter menuAdapter;
//	private int position;
	
	private static final int TOTAL_EVENT_ITEMS = 7;
	private static final int TOTAL_OTHER_ITEMS = 4;
	private static final int TOTAL_SECTIONS = 2;

	private Solo solo;
	
	public MenuActivityTest() {
		super(Menu_Activity.class);
	}
	
	@Override
	public void setUp() throws Exception {
		super.setUp();
		
		setActivityInitialTouchMode(false);
		
		menuActivity = getActivity();
		
		solo = new Solo(getInstrumentation(), getActivity());
		
		headerListView = (HeaderListView) menuActivity.findViewById(com.sportxast.SportXast.R.id.headerListView_menu);
		menuAdapter = (MenuAdapter) headerListView.getListView().getAdapter();
	}
	
	public void testPreconditions() {
		assertNotNull(headerListView);
		
		//String actionBarTitle = menuActivity.getActionBarTitle();
		//assertEquals("Current actionbar title is " + actionBarTitle, "Menu", actionBarTitle);
	}
	
	public void testTotalItems() {
		assertEquals(TOTAL_EVENT_ITEMS + TOTAL_OTHER_ITEMS + TOTAL_SECTIONS, menuAdapter.getCount());
		assertEquals(TOTAL_EVENT_ITEMS, menuAdapter.numberOfRows(0));
		assertEquals(TOTAL_OTHER_ITEMS, menuAdapter.numberOfRows(1));
		assertEquals(TOTAL_SECTIONS, menuAdapter.numberOfSections());
	}
	
	public void testItems() {
		// position 2
		solo.clickOnText("Search");
		solo.assertCurrentActivity("I should be in Search_Activity", Search_Activity.class);
		solo.goBack();
		
		// position 3
		solo.clickOnText("Create");
		solo.assertCurrentActivity("I should be in Create_Activity", Create_Activity.class);
		solo.goBack();
		
		// position 4, Capture
		solo.clickOnText("Capture");
		solo.assertCurrentActivity("I should be in VideoCaptureActivity", VideoCaptureActivity.class);
		solo.setActivityOrientation(Solo.LANDSCAPE);
		
		getInstrumentation().waitForIdleSync();
		solo.setActivityOrientation(Solo.PORTRAIT);
//		solo.goBack();
		
		// position 5, popular
		solo.clickOnText("Popular");
		solo.assertCurrentActivity("Should be in SportX2_Main", SportX2_Main.class);
		solo.goBack();
		
		// position 6, nearby
		solo.clickOnText("Nearby");
		solo.assertCurrentActivity("Should also be in SportX2_Main", SportX2_Main.class);
		solo.goBack();
		
		// position 7, favorite
		solo.clickOnText("Favorite");
		solo.assertCurrentActivity("Should also be in SportX2_Main", SportX2_Main.class);
		solo.goBack();
		
		// position 8 is a label
		
		// position 9 is notification, not yet implemented
		
		// position 10 is share
		// it is an object
		
		// position 11, Profile_Activity
		solo.clickOnText("Profile");
		solo.assertCurrentActivity("Should be in Profile_Activity", Profile_Activity.class);
		solo.goBack();
		
		// position 12, Information_Activity
		solo.clickOnText("Information");
		solo.assertCurrentActivity("Get me to Information_Activity", Information_Activity.class);
		solo.goBack();
	}
}
