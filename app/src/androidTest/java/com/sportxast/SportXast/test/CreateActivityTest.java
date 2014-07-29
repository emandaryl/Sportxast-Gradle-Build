package com.sportxast.SportXast.test;

import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.robotium.solo.Solo;
import com.sportxast.SportXast.activities2_0.ChooseASport_Activity;
import com.sportxast.SportXast.activities2_0.ChooseATeam_Activity;
import com.sportxast.SportXast.activities2_0.ChooseAVenue_Activity;
import com.sportxast.SportXast.activities2_0.Create_Activity;
import com.sportxast.SportXast.thirdparty_class.HeaderListView;

public class CreateActivityTest extends ActivityInstrumentationTestCase2<Create_Activity> {

	private Create_Activity createActivity;
	private HeaderListView headerListView;
	private Button buttonBack;
	private TextView buttonDone;
	//private DatabaseHelper dbHelper;
	private Solo solo;

	public CreateActivityTest() {
		super(Create_Activity.class);
	}
	
	@Override
	public void setUp() throws Exception {
		super.setUp();
		
		setActivityInitialTouchMode(false);
		
		createActivity = getActivity();
		
		//dbHelper = new DatabaseHelper(createActivity);
		solo = new Solo(getInstrumentation(), createActivity);
		headerListView = (HeaderListView) createActivity.findViewById(com.sportxast.SportXast.R.id.headerListView_menu);
		buttonDone = (TextView) createActivity.findViewById(com.sportxast.SportXast.R.id.btn_Done);
		buttonBack = (Button) createActivity.findViewById(com.sportxast.SportXast.R.id.imgbtn_back);
	}
	
	public void testPreconditions() {
		solo.assertCurrentActivity("I'm in Create_Activity", Create_Activity.class);
		solo.assertMemoryNotLow();
		
		assertNotNull(createActivity);
		assertNotNull(headerListView);
		assertNotNull(buttonDone);
		assertNotNull(buttonBack);
		
		//String actionBarTitle = createActivity.getActionBarTitle();
		//assertEquals("Create", actionBarTitle);
		
		assertEquals(View.VISIBLE, buttonDone.getVisibility());
	}
	
	public void testSelectASport() {
		solo.clickOnText("Select a Sport");
		getInstrumentation().waitForIdleSync();
		solo.assertCurrentActivity("I should be in ChooseASport_Activity", ChooseASport_Activity.class);
		solo.goBack();
	}
	
	public void testSelectAVenue() {
		solo.clickOnText("Select a Venue");
		getInstrumentation().waitForIdleSync();
		solo.assertCurrentActivity("I am in ChooseAVenue_Activity", ChooseAVenue_Activity.class);
		solo.goBack();
	}
	
	public void testSelectATeam() {
		solo.clickOnText("Add a Team");
		getInstrumentation().waitForIdleSync();
		solo.assertCurrentActivity("Am I in ChooseATeam_Activity", ChooseATeam_Activity.class);
		solo.goBack();
	}
	
	public void testListViewIsNotEmpty() {
		// Adapter should not be blank
		assertFalse(headerListView.getListView().getAdapter().isEmpty());
	}
	
	@Override
	public void tearDown() throws Exception {
		solo.finishOpenedActivities();
		super.tearDown();
	}
	
}
