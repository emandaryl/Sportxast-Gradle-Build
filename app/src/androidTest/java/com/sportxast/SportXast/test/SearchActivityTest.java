package com.sportxast.SportXast.test;

/*
File name renamed.
Will uncomment if changed back to original.
Check if it is changed.
 */
//import com.sportxast.SportXast.activities2_0.Search_Activity;
//
//public class SearchActivityTest extends ActivityInstrumentationTestCase2<Search_Activity> {
//
//	//private Search_Activity searchActivity;
//	private TabHost tabHost;
//	private static final int TOTAL_NUMBER_TABS = 3;
//	private static final String TAB_EVENTS = "Events";
//	private static final String TAB_FANS = "Fans";
//	private static final String TAB_TAGS = "Tags";
//
//	private Solo solo;
//
//	public SearchActivityTest() {
//		super(Search_Activity.class);
//	}
//
//	@Override
//	public void setUp() throws Exception {
//		super.setUp();
//
//		//searchActivity = getActivity();
//		solo = new Solo(getInstrumentation(), getActivity());
//		tabHost = (TabHost) solo.getView(android.R.id.tabhost);
//	}
//
//	public void testPreconditions() {
//		solo.waitForActivity(Search_Activity.class);
//		solo.assertCurrentActivity("I should be in Search_Activity", Search_Activity.class);
//		solo.assertMemoryNotLow();
//		assertNotNull(tabHost);
////
////		assertEquals("Search", searchActivity.getActionBarTitle());
//	}
//
//	public void testNumberOfTabs() {
//		assertEquals(TOTAL_NUMBER_TABS, tabHost.getTabWidget().getTabCount());
//		//check the labels are correct
//		assertEquals(TAB_EVENTS, getTextViewFromLabel(0));
//		assertEquals(TAB_FANS, getTextViewFromLabel(1));
//		assertEquals(TAB_TAGS, getTextViewFromLabel(2));
//	}
//
//	private String getTextViewFromLabel(int childIndex) {
//		TextView tab = (TextView) tabHost.getTabWidget().getChildAt(childIndex)
//				.findViewById(com.sportxast.SportXast.R.id.tab);
//		return tab.getText().toString().trim();
//	}
//}
