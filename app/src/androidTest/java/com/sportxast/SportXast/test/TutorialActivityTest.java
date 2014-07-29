package com.sportxast.SportXast.test;

import android.support.v4.view.ViewPager;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.widget.Button;

import com.robotium.solo.Solo;
import com.sportxast.SportXast.SportX2_Main;
import com.sportxast.SportXast.Tutorial_Activity;

public class TutorialActivityTest extends ActivityInstrumentationTestCase2<Tutorial_Activity> {

	private Tutorial_Activity tutorialActivity;
	private ViewPager mPagerTutorial;
	private Button mButtonNext;
	/*
	 * This button is only temporary.
	 * It will take you directly to Events Page.
	 * For dev only.
	 */
	//private Button mButtonAllowAccess;
	public static final int TUTORIAL_LENGTH = 6;
	
	public static final int INITIAL_POSITION = 0;
	public static final int EXPECTED_POS_0 = 0;
	public static final int EXPECTED_POS_1 = 1;
	public static final int EXPECTED_POS_2 = 2;
	public static final int EXPECTED_POS_3 = 3;
	public static final int EXPECTED_POS_4 = 4;
	public static final int EXPECTED_POS_5 = 5;
	
	private Solo solo;
	
//	private static final String TAG = "TutorialActivityTest";
//	private ImageView[] arrImageViews;
	
	public TutorialActivityTest() {
		super(Tutorial_Activity.class);
	}
	
	@Override
	public void setUp() throws Exception {
		super.setUp();
		
		setActivityInitialTouchMode(false);
		tutorialActivity = (Tutorial_Activity) getActivity();
		solo = new Solo(getInstrumentation(), tutorialActivity);
		mButtonNext = (Button) tutorialActivity.findViewById(com.sportxast.SportXast.R.id.btn_next);
		mPagerTutorial = (ViewPager) tutorialActivity.findViewById(com.sportxast.SportXast.R.id.pager);
		
//		arrImageViews = new ImageView[] {
//				(ImageView) tutorialActivity.findViewById(com.sportxast.SportXast.R.id.p1),
//				(ImageView) tutorialActivity.findViewById(com.sportxast.SportXast.R.id.p2),
//				(ImageView) tutorialActivity.findViewById(com.sportxast.SportXast.R.id.p3),
//				(ImageView) tutorialActivity.findViewById(com.sportxast.SportXast.R.id.p4),
//				(ImageView) tutorialActivity.findViewById(com.sportxast.SportXast.R.id.p5),
//				(ImageView) tutorialActivity.findViewById(com.sportxast.SportXast.R.id.p6)
//		};
	}
	
	public void testPreConditions() {
		
		assertInitViews();
//		
//		for(int i=0; i<arrImageViews.length; i++) {
//			assertNotNull("WTF at index " + i, arrImageViews[i]);
//		}
	}
	
	private void assertInitViews() {
		assertNotNull(tutorialActivity);
		assertNotNull(mPagerTutorial);
		assertNotNull(mButtonNext);
	}
	
	public void testSwipe() {
		/*
		 * Swipe right.
		 */
		for(int i=0; i<TUTORIAL_LENGTH-1; i++) {
			if(i == 0) {
				assertEquals("index " + i + " and current item " + mPagerTutorial.getCurrentItem() , i, mPagerTutorial.getCurrentItem());
				solo.scrollViewToSide(solo.getView(com.sportxast.SportXast.R.id.pager), Solo.RIGHT);
			} else {
				solo.scrollViewToSide(solo.getView(com.sportxast.SportXast.R.id.pager), Solo.RIGHT);
				getInstrumentation().waitForIdleSync();
				Log.i("Tutorial Test", "Current item is " + mPagerTutorial.getCurrentItem());
				assertEquals("index " + (i + 1) + " and current item " + mPagerTutorial.getCurrentItem() , i + 1, mPagerTutorial.getCurrentItem());
			}
			
		}
		
		/*
		 * Swipe Left. Go back
		 */
		for(int i=TUTORIAL_LENGTH - 1; i>0; i--) {
			solo.scrollViewToSide(solo.getView(com.sportxast.SportXast.R.id.pager), Solo.LEFT);
			getInstrumentation().waitForIdleSync();
			assertEquals(i - 1, mPagerTutorial.getCurrentItem());
		}
	}
	
	public void testOrientation() {
		solo.setActivityOrientation(Solo.LANDSCAPE);
		assertInitViews();
		
		solo.setActivityOrientation(Solo.PORTRAIT);
		assertInitViews();
	}
	
	public void testViewPagerUi() {
		
		int currentItem = INITIAL_POSITION;
		String btnLabel = "Next >";
		
		// Expect position to be at 0
		currentItem = mPagerTutorial.getCurrentItem();
		assertEquals(EXPECTED_POS_0, currentItem);
		
		for(int i=0; i<TUTORIAL_LENGTH; i++) {
			btnLabel = mButtonNext.getText().toString();
			Log.i("Test ViewPager", "index at " + i + " and label " + btnLabel);
			if(i != 5) {
				assertEquals("index " + i + " and current item " + mPagerTutorial.getCurrentItem() , i, mPagerTutorial.getCurrentItem());
				solo.clickOnButton("Next >");
				getInstrumentation().waitForIdleSync();
				assertTrue("Button label is " + btnLabel, btnLabel.equals("Next >"));
			} else {
				assertTrue("Button label is " + btnLabel, btnLabel.equals("X"));
				solo.clickOnButton("X");
				getInstrumentation().waitForIdleSync();
				
//				assertEquals("index " + (i + 1) + " and current item " + mPagerTutorial.getCurrentItem() , i + 1, mPagerTutorial.getCurrentItem());
				solo.assertCurrentActivity("I should be in Main Activity", SportX2_Main.class);
			}
			
		}
		
//		for(int i=0; i<TUTORIAL_LENGTH-1; i++) {
//			if(i == EXPECTED_POS_5) {
//				/*
//				 * This is the last item. Button label should be done.
//				 * Ready to go the Main Activity.
//				 */
//				btnLabel = mButtonNext.getText().toString();
//				assertTrue("Button label is " + btnLabel, btnLabel.equals("Done"));
//				
//				solo.clickOnButton("Done");
//				getInstrumentation().waitForIdleSync();
//				
//			} else {
//				
//				/*
//				 * Just click Next.
//				 */
//				solo.clickOnButton("Next >");
//				getInstrumentation().waitForIdleSync();
//				assertEquals(i + 1, mPagerTutorial.getCurrentItem());
//				
//				btnLabel = mButtonNext.getText().toString();
//				
//				Log.i("Test ViewpagerUI", "index at " + i + " label is " + btnLabel);
//				assertTrue("Button label is " + btnLabel, btnLabel.equals("Next >"));
//			}
//		}
	}
	
	@Override
	public void tearDown() throws Exception {
		solo.finishOpenedActivities();
		super.tearDown();
	}
}
