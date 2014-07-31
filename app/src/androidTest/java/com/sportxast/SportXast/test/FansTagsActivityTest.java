package com.sportxast.SportXast.test;

import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;

import com.sportxast.SportXast.activities2_0.FansTagsActivity;
import com.sportxast.SportXast.commons.Constants;
import com.sportxast.SportXast.fragments.FansTagsFragment;

public class FansTagsActivityTest extends ActivityInstrumentationTestCase2<FansTagsActivity> {

	private FansTagsActivity activity;
	private FansTagsFragment listFragment;
	private static final int DIVIDER_HEIGHT = 1;
	//private Global_Data globalData;
	
	public FansTagsActivityTest() {
		super(FansTagsActivity.class);
	}
	
	@Override
	public void setUp() throws Exception {
		super.setUp();
		setActivityInitialTouchMode(false);
		
		Intent intentMock = new Intent();
		intentMock.putExtra(Constants.EXTRA_EVENT_TYPE, FansTagsActivity.LIST_FANS);
		setActivityIntent(intentMock);
		
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		activity = getActivity();
		//globalData = (Global_Data) activity.getApplicationContext();
		
		getInstrumentation().waitForIdleSync();
		listFragment = (FansTagsFragment) activity.getSupportFragmentManager().findFragmentById(com.sportxast.SportXast.R.id.containerEventInfosList);
	}
	
	public void testPreconditions() {
		assertNotNull(activity);
		assertNotNull(listFragment);
		
//		assertEquals(new ColorDrawable(R.color.grey_light).getConstantState(), listFragment.getListView().getDivider().getConstantState());
//		assertTrue(new ColorDrawable(R.color.grey_light).equals((ColorDrawable) listFragment.getListView().getDivider()));
//		assertThat(listFragment.getListView().getDivider()).isEqualTo(new ColorDrawable(R.color.grey_light));
		assertEquals(DIVIDER_HEIGHT, listFragment.getListView().getDividerHeight());
		
	}
	
//	public void testFansData() {
//		FansAdapter adapter = new FansAdapter(activity);
//		adapter.setData(fansList);
////		if(parseInt(globalData.getEventList().eventTotalFanCount) > 0) {
////			assertTrue(listFragment.getListView().getAdapter().getCount() > 0);
////		} else {
////			assertTrue(listFragment.getListView().getAdapter().getCount() == 0);
////		}
//	}
	
//	public void testTagsData() {
//		assertNotNull(globalData.getEventList());
//		
//		activity.finish();
//		setActivity(null);
//		
//		Intent intentMock = new Intent();
//		intentMock.putExtra(Constant.EXTRA_EVENT_TYPE, FansTagsActivity.LIST_TAGS);
//		setActivityIntent(intentMock);
//		activity = getActivity();
//		
//		if(parseInt(globalData.getEventList().eventTotalTagCount) > 0) {
//			assertTrue(listFragment.getListView().getAdapter().getCount() > 0);
//		} else {
//			assertTrue(listFragment.getListView().getAdapter().getCount() == 0);
//		}
//	}
	
//	private int parseInt(String s){
//		try {
//			return Integer.parseInt(s);
//			
//		} catch (Exception e) {
//		return 0;
//		}
//	}
	
}
