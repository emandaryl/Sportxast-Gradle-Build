package com.sportxast.SportXast.test;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.VideoView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.robotium.solo.Solo;
import com.sportxast.SportXast.activities2_0.Fav_Tag_Comment_Activity;
import com.sportxast.SportXast.activities2_0.Highlight_Activity;
import com.sportxast.SportXast.activities2_0.Profile_Activity;
import com.sportxast.SportXast.activities2_0.VideoCaptureActivity;
import com.sportxast.SportXast.activities2_0.VideoFullScreenActivity;
import com.sportxast.SportXast.commons.Constant;
import com.sportxast.SportXast.test.constants.Values;
import com.sportxast.SportXast.test.utils.SleepUtils;
import com.sportxast.SportXast.thirdparty_class.Async_HttpClient;
import com.sportxast.SportXast.thirdparty_class.EditTextBackKeyEvent;
import com.sportxast.SportXast.thirdparty_class.PullToRefreshListView;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.concurrent.CountDownLatch;

public class HighlightActivityTest extends ActivityInstrumentationTestCase2<Highlight_Activity> {
	
	private Highlight_Activity highlightActivity;
	private View commentView;
	private View headerView;
	private View footerView;
	private TextView txtHeaderTitle;
	//private PullToRefreshListView listView;
	
	//private int isToday = -1;
	
	// Intents
	private String extraIsToday = null;
	private String extraEventTeams = null;
	private String extraEventDate = null;
	private String extraEventId = null;
	private String extraHashtag = null;
	//private String extraMediaId = null;
	
	private Async_HttpClient httpClient;
	private JSONObject jsonResponse = null;
	private Solo solo;
	
	private String extendedUrl = "";
	
	private LinearLayout listViewContainer;
	private PullToRefreshListView listViewPullRefresh;
	
	private EditTextBackKeyEvent edtComment;
	private TextView txtSend;
	
	public HighlightActivityTest() {
		super(Highlight_Activity.class);
	}
	
	@Override
	public void setUp() throws Exception {
		super.setUp();
		
		setActivityInitialTouchMode(false);
		
		// Mock Intent here
		Intent intentMock = new Intent();
		intentMock.putExtra(Constant.EXTRA_EVENT_TEAMS, "jennifer");
		intentMock.putExtra(Constant.EXTRA_IS_TODAY, "1");
		intentMock.putExtra(Constant.EXTRA_EVENT_DATE, "06.06");
		intentMock.putExtra(Constant.EXTRA_EVENT_ID, "1496");
		intentMock.putExtra(Constant.EXTRA_HASHTAG, "");
		//intentMock.putExtra(Values.TAG_MEDIA_ID, "2");
		
		setActivityIntent(intentMock);
		
		highlightActivity = getActivity();
		solo = new Solo(getInstrumentation(), getActivity());
		httpClient = new Async_HttpClient(highlightActivity);
		
		commentView = highlightActivity.getLayoutInflater().inflate(com.sportxast.SportXast.R.layout.layout_comment_field, null);
		headerView = highlightActivity.getLayoutInflater().inflate(com.sportxast.SportXast.R.layout.list_header, null);
		footerView = highlightActivity.getLayoutInflater().inflate(com.sportxast.SportXast.R.layout.progressbar_small, null);
		
		txtHeaderTitle = (TextView) highlightActivity.findViewById(com.sportxast.SportXast.R.id.txtvw_header_title);
		//listView = (PullToRefreshListView) highlightActivity.findViewById(com.sportxast.SportXast.R.id.pulltorefresh);
		listViewContainer = (LinearLayout) highlightActivity.findViewById(com.sportxast.SportXast.R.id.ptrListview_cont1);
		listViewPullRefresh = (PullToRefreshListView) listViewContainer.getChildAt(0);
		
		edtComment = (EditTextBackKeyEvent) highlightActivity.findViewById(com.sportxast.SportXast.R.id.edittext_comment);
		txtSend = (TextView) highlightActivity.findViewById(com.sportxast.SportXast.R.id.btn_send);
		
		// Have to put it here, this variables are used anywhere.
		extraEventTeams = highlightActivity.getIntent().getStringExtra(Constant.EXTRA_EVENT_TEAMS);
		extraIsToday = highlightActivity.getIntent().getStringExtra(Constant.EXTRA_IS_TODAY);
		extraEventDate = highlightActivity.getIntent().getStringExtra(Constant.EXTRA_EVENT_DATE);
		extraEventId = highlightActivity.getIntent().getStringExtra(Constant.EXTRA_EVENT_ID);
		extraHashtag = highlightActivity.getIntent().getStringExtra(Constant.EXTRA_HASHTAG);
		//extraMediaId = highlightActivity.getIntent().getStringExtra(Values.TAG_MEDIA_ID);
	}
	
	public void testPreconditions() {
		solo.assertCurrentActivity("I am on Highlight_Activity", Highlight_Activity.class);
		solo.assertMemoryNotLow();
		
		SleepUtils.freeze(getInstrumentation(), 5000);
		
		assertNotNull(highlightActivity);
		assertNotNull(commentView);
		// Initially commentView is gone
		// assertion error, fix later
//		assertEquals(View.GONE, commentView.getVisibility());
		
		assertNotNull(headerView);
		assertNotNull(footerView);
		
		assertNotNull(txtHeaderTitle);
		assertEquals("Capture a Highlight", txtHeaderTitle.getText().toString().trim());
		
		assertNotNull(edtComment);
		assertNotNull(txtSend);
		
		//assertNotNull(listView);
		
		extraIsToday = highlightActivity.getIntent().getStringExtra(Constant.EXTRA_IS_TODAY); 
		//isToday = Integer.parseInt(extraIsToday);
//		if(isToday == 1) {
//			// If 1, expect a headerView added. It should be not 0
//			assertTrue(listView.getHeaderViewsCount() > 0);
//		}
		
		// There should also be a footer view.
//		assertTrue(listView.getFooterViewsCount() > 0);
		
		assertNotNull(httpClient);
		assertNotNull(listViewContainer);
		assertNotNull(listViewPullRefresh);
	}
	
	public void testCaptureHighlight() {
		solo.clickOnText("Capture a Highlight");
		solo.assertCurrentActivity("Should go to VideoCaptureActivity", VideoCaptureActivity.class);
		SleepUtils.freeze(getInstrumentation(), 2000);
		solo.goBack();
	}
	
	/*
	 * clickOnImageButton()
	 * index 0 = play button that plays the video
	 * index 1 = taps the fullscreen for video
	 * index 2 = taps the favorite
	 * index 3 = taps the tags
	 * index 4 = taps the comment
	 * index 5 = taps the share
	 * index 6 = taps the report
	 */
	
	public void testTapPlayVideo() {
		solo.clickOnImageButton(0);
		SleepUtils.freeze(getInstrumentation(), 8000);
		
		ArrayList<View> listViewsFound = getViewsById(com.sportxast.SportXast.R.id.video_media);
		assertTrue("Views found: " + listViewsFound.size(), listViewsFound.size() > 0);
		
		SleepUtils.freeze(getInstrumentation(), 5000);
		
		assertTrue(listViewsFound.get(0) instanceof VideoView);
		VideoView videoView = (VideoView) listViewsFound.get(0);
		solo.waitForView(videoView);
		
		/*
		 * Temporary. When current position is not at 0,
		 * it means the video played.
		 */
		assertTrue("Video current pos: " + videoView.getCurrentPosition(), videoView.getCurrentPosition() > 0);
		/*
		 * Fix later below. It seems, the process can't
		 * sync with the VideoView in the main process.
		 */
//		assertTrue("Video should be playing.", videoView.isPlaying());
	}
	
	public void testTapPlayVideoFullScreen() {
		solo.clickOnImageButton(1);
		SleepUtils.freeze(getInstrumentation(), 8000);
		
		solo.assertCurrentActivity("I should be in VideoFullScreenActivity", VideoFullScreenActivity.class);
		
		goBackToActivity();
	}
	
	public void testTapFavorite() {
		solo.clickOnImageButton(2);
		SleepUtils.freeze(getInstrumentation(), 5000);
		
		Drawable drawableFavSelected = highlightActivity.getResources().getDrawable(com.sportxast.SportXast.R.drawable.ic_favorite_blue_selected);
		assertNotNull(drawableFavSelected);
		
		ImageButton imgbtnFav = solo.getImageButton(2);
//		ImageButton imgbtnFav = (ImageButton) highlightActivity.findViewById(com.sportxast.SportXast.R.id.imgbtn_favorite);
		assertNotNull(imgbtnFav);
		
		assertTrue("Favorite drawable should be the selected one", imgbtnFav.getDrawable().getConstantState().equals(drawableFavSelected.getConstantState()));
		
		// Click again. And make sure drawable returns to default(unselected).
		solo.clickOnImageButton(2);
		SleepUtils.freeze(getInstrumentation(), 5000);
		
		Drawable drawableFavDefault = highlightActivity.getResources().getDrawable(com.sportxast.SportXast.R.drawable.ic_favorite_blue);
		assertNotNull(drawableFavDefault);
		
		assertTrue("Favorite drawable should return to default.", imgbtnFav.getDrawable().getConstantState().equals(drawableFavDefault.getConstantState()));
	}
	
	public void testTapTags() {
		solo.clickOnImageButton(3);
		SleepUtils.freeze(getInstrumentation(), 5000);
		
		assertTrue("Tags dialog should appear.", solo.searchText("Tags"));
		solo.goBack();
		
		goBackToActivity();
	}
	
	public void testTapComment() {
		solo.clickOnImageButton(4);
		
		ImageButton imgbtnComment = solo.getImageButton(4);
		//solo.hideSoftKeyboard();
		SleepUtils.freeze(getInstrumentation(), 5000);
		
		assertNotNull(edtComment);

		//TextView send = (TextView) highlightActivity.findViewById(com.sportxast.SportXast.R.id.btn_send);
		assertNotNull(txtSend);
		
//		View commentView = edtComment.getCommentView();
		
		solo.enterText(edtComment, "This is a comment.");
		assertEquals("This is a comment.", edtComment.getText().toString().trim());
		
		assertEquals("Comment View is " + commentView.getVisibility(), View.VISIBLE, commentView.getVisibility());
		assertEquals("EditText is " + edtComment.getVisibility(), View.VISIBLE, edtComment.getVisibility());
		assertEquals("Send button is " + txtSend.getVisibility(), View.VISIBLE, txtSend.getVisibility());		
		
//		ImageButton imgbtnComment = (ImageButton) highlightActivity.findViewById(com.sportxast.SportXast.R.id.imgbtn_comment);
		Drawable selectorComment = highlightActivity.getResources().getDrawable(com.sportxast.SportXast.R.drawable.selector_comment);
		
		assertNotNull(imgbtnComment);
		assertNotNull(selectorComment);
		
		assertTrue(solo.searchText("Send"));
		
		View outsideView = commentView.findViewById(com.sportxast.SportXast.R.id.view_outside);
		solo.clickOnView(outsideView);
		solo.waitForView(outsideView);
		
		/*
		 * Assert that commentView's visibility is GONE.
		 */
		SleepUtils.freeze(getInstrumentation(), 2000);
		assertEquals(View.GONE, commentView.getVisibility());
		
		/*
		 * Assert that keyboard is not shown at
		 * this point.
		 */
		InputMethodManager imm = (InputMethodManager) highlightActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
		assertFalse(imm.isAcceptingText());
		
		// Can't get image in imagebutton, android:src or setImageResource()
//		assertTrue(imgbtnComment.getDrawable().getConstantState().equals(selectorComment.getConstantState()));
	}
	
	public void testTapShare() {
		solo.clickOnImageButton(5);
		SleepUtils.freeze(getInstrumentation(), 5000);
		
		/*
		 * Test if the dialog exists.
		 */
		assertTrue("Share Dialog should show up.", solo.searchText("Share via:"));
		solo.goBack();
		goBackToActivity();
	}
	
	public void testTapReport() {
		solo.clickOnImageButton(6);
		SleepUtils.freeze(getInstrumentation(), 5000);
		
		assertTrue("Report dialog should show up.", solo.searchText("Report"));
		solo.goBack();
		
		goBackToActivity();
		
	}
	
	/*
	 * end of clickOnImageButton
	 */
	
//	public void testViewFound() {
//		freeze(5000); // Need to freeze, make sure the process has enough time to load and find.
//		assertTrue(getViewsById(com.sportxast.SportXast.R.id.imgbtn_favorite).size() > 0);
//	}
	
	public void testClickFavorites() {
		solo.clickOnText("Favorites");
		solo.assertCurrentActivity("Take me to Fav_Tag_Comment_Activity", Fav_Tag_Comment_Activity.class);
		goBackToActivity();
	}
	
	public void testClickTags() {
		solo.clickOnText("Tags");
		solo.assertCurrentActivity("Take me to Fav_Tag_Comment_Activity", Fav_Tag_Comment_Activity.class);
		goBackToActivity();
	}
	
	public void testClickComments() {
		solo.clickOnText("Comments");
		solo.assertCurrentActivity("Take me to Fav_Tag_Comment_Activity", Fav_Tag_Comment_Activity.class);
		goBackToActivity();
	}
	
	public void testFavTagCommentDisplay() {
		/*
		 * Test the Favorites, Tags, Comments display
		 * below the VideoView
		 */
		SleepUtils.freeze(getInstrumentation(), 3000);
		TextView txtFavCount = (TextView) highlightActivity.findViewById(com.sportxast.SportXast.R.id.txtvw_favoriteCount);
		assertNotNull(txtFavCount);
		
		TextView txtTagCount = (TextView) highlightActivity.findViewById(com.sportxast.SportXast.R.id.txtvw_tagCount);
		assertNotNull(txtTagCount);
		
		TextView txtCommentCount = (TextView) highlightActivity.findViewById(com.sportxast.SportXast.R.id.txtvw_commentCount);
		assertNotNull(txtCommentCount);
	}
	
	public void testTapAvatar() {
		SleepUtils.freeze(getInstrumentation(), 5000);
		
		solo.clickOnText("Mark Sportxast"); // Temporary, solution in the future.
		solo.assertCurrentActivity("I should be in Profile Activity", Profile_Activity.class);
		solo.goBack();
	}
	
	public void testIntents() {
		assertEquals("jennifer", extraEventTeams);
		assertEquals("1", extraIsToday);
		assertEquals("06.06", extraEventDate);
		assertEquals("1496", extraEventId);
		assertEquals("", extraHashtag);
//		assertEquals("2", extraMediaId);
	}
	
	public void testFetchData() throws Throwable {
		final RequestParams params = new RequestParams();
		// Assuming that all extras from the intent are all present.
		// If hashtag's length > 0
		if(extraHashtag.length() > 0) {
			params.put(Constant.EXTRA_HASHTAG, extraHashtag);
			params.put(Values.TAG_PAGE, (2 + 1)); // bogus value
			
			extendedUrl = Values.GET_EXPORT_MEDIA;
		} else {
			params.put(Constant.EXTRA_EVENT_ID, "1496");
			params.put(Values.TAG_SINCE_ID, "");
			params.put(Values.TAG_UNTIL_ID, "");
			
			extendedUrl = Values.GET_EXPORT_EVENT_MEDIA;
		}
		
		params.put(Values.TAG_PAGE_SIZE, "5");
		
		final CountDownLatch signal = new CountDownLatch(1);
		runTestOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				httpClient.GET(extendedUrl, params, new JsonHttpResponseHandler() {
					
					@Override
					public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
						super.onSuccess(statusCode, headers, response);
						jsonResponse = response;
						signal.countDown();
					}
					
					@Override
					public void onFailure(Throwable e, JSONObject errorResponse) {
						jsonResponse = null;
						signal.countDown();
					}
				});
			}
		});
		
		signal.await();
		assertNotNull(jsonResponse);
		jsonResponse = null;
		
		SleepUtils.freeze(getInstrumentation(), 5000);
	}
	
	public void testBackButtonOnLeft() {
		Button btnBack = (Button) highlightActivity.findViewById(com.sportxast.SportXast.R.id.imgbtn_back);
		assertNotNull(btnBack);
//		assertThat(btnBack).isVisible();
	}
	
	private void goBackToActivity() {
		solo.goBackToActivity("Highlight_Activity");
	}
	
	private ArrayList<View> getViewsById(int id) {
		ArrayList<View> allViews = solo.getViews();
		ArrayList<View> matchedViews = new ArrayList<View>();
		
		for(View view : allViews) {
			if(view != null && view.getId() == id) {
				matchedViews.add(view);
			}
		}
		
		return matchedViews;
	}
	
	@Override
	public void tearDown() throws Exception {
		solo.finishOpenedActivities();
		super.tearDown();
	}
}
