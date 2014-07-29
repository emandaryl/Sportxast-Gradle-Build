package com.sportxast.SportXast.test;

import android.app.Activity;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.ListView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.robotium.solo.Solo;
import com.sportxast.SportXast.StaticVariables.KEY;
import com.sportxast.SportXast.activities2_0.Fav_Tag_Comment_Activity;
import com.sportxast.SportXast.commons.Constant;
import com.sportxast.SportXast.test.constants.Values;
import com.sportxast.SportXast.thirdparty_class.Async_HttpClient;

import org.apache.http.Header;
import org.json.JSONObject;

import java.util.concurrent.CountDownLatch;

public class FavTagCommentActivityTest extends ActivityInstrumentationTestCase2<Fav_Tag_Comment_Activity> {

	private Fav_Tag_Comment_Activity ftcActivity;
	private ListView listView;
	private View footerView;
//	private View commentView;
	
	private String sampleMediaId = "5009";
	private String sampleEventId = "456";
	
	private Async_HttpClient httpClient;
	
	private String[] arrKeys = new String[]{
			KEY.favorite,
			KEY.tag,
			KEY.comment,
			KEY.fans
	};
	
	private JSONObject jsonResponse = null;
	private String extendedUrl = "";
	
	private Solo solo;
	
	public FavTagCommentActivityTest() {
		super(Fav_Tag_Comment_Activity.class);
	}
	
	@Override
	public void setUp() throws Exception {
		super.setUp();
		
		setActivityInitialTouchMode(false);
		
		// Mock Intent
		Intent intent = new Intent();
		intent.putExtra(Constant.EXTRA_EVENT_ID, "32");
		intent.putExtra(Constant.EXTRA_MEDIA_ID, "345");
		intent.putExtra(Values.TAG_LIST_TYPE, "favorite");
		setActivityIntent(intent);
		
		ftcActivity = getActivity();
		solo = new Solo(getInstrumentation(), ftcActivity);
		httpClient = new Async_HttpClient(ftcActivity);
		
		listView = (ListView) ftcActivity.findViewById(com.sportxast.SportXast.R.id.listview_comment);
		footerView = ftcActivity.getLayoutInflater().inflate(com.sportxast.SportXast.R.layout.progressbar_small, null);
//		commentView = (View) ftcActivity.findViewById(com.sportxast.SportXast.R.id.layout_comment);
	}
	
	public void testListType() {
		for(int i=0; i<arrKeys.length; i++) {
			setActivityInitialTouchMode(false);
			
			Intent intent = new Intent();
			intent.putExtra(Values.TAG_LIST_TYPE, arrKeys[i]);
			setActivityIntent(intent);
			
			Activity activity = getActivity();
			View comment = activity.findViewById(com.sportxast.SportXast.R.id.layout_comment);
			
			if(activity.getIntent().getStringExtra(Values.TAG_LIST_TYPE).equals(KEY.comment)) {
				// If list type is "comment", commentView should be visible.
				assertTrue("CommentView is " + comment.getVisibility(),
						comment.getVisibility() == View.VISIBLE);
				//fail();
			} else {
				// else commentView should be gone
				assertTrue("CommentView is " + comment.getVisibility(),
						comment.getVisibility() == View.GONE);
			}
			
			activity.finish();
			setActivity(null);
		}
	}
	
	public void testGetFavorites() throws Throwable {
		for(int i=0; i<2; i++) {
			final RequestParams params = new RequestParams();
			
			if(i == 0) {
				/*
				 * First test is that mediaId is not empty.
				 * So we will use the mediaId as param.
				 */
				params.put(Constant.EXTRA_MEDIA_ID, sampleMediaId);
				extendedUrl = Values.GET_EXPORT_USER_IN_MEDIA_FAV;
			} else {
				// If i == 1, of course!
				params.put(Constant.EXTRA_EVENT_ID, sampleEventId);
				extendedUrl = Values.GET_EXPORT_USER_IN_EVENT_FAV;
			}
			
			params.put(Values.TAG_PAGE, "" + 5); // Sample value
			params.put(Values.TAG_PAGE_SIZE, "20");
			
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
							super.onFailure(e, errorResponse);
							jsonResponse = null;
							signal.countDown();
						}
					});
				}
			});
			
			signal.await();
			assertNotNull("response: " + jsonResponse, jsonResponse);
		} // end of for loop
		
		jsonResponse = null;// make em null for the other tests
	}
	
	public void testTags() throws Throwable {
		for(int i=0; i<2; i++) {
			final RequestParams params = new RequestParams();
			
			if(i == 0) {
				/*
				 * First test is that mediaId is not empty.
				 * So we will use the mediaId as param.
				 */
				params.put(Constant.EXTRA_MEDIA_ID, sampleMediaId);
				extendedUrl = Values.GET_EXPORT_MEDIA_TAG;
			} else {
				// If i == 1, of course!
				params.put(Constant.EXTRA_EVENT_ID, sampleEventId);
				extendedUrl = Values.GET_EXPORT_EVENT_TAG;
			}
			
			params.put(Values.TAG_PAGE, "" + 5); // Sample value
			params.put(Values.TAG_PAGE_SIZE, "20");
			
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
							super.onFailure(e, errorResponse);
							jsonResponse = null;
							signal.countDown();
						}
					});
				}
			});
			
			signal.await();
			assertNotNull("response: " + jsonResponse, jsonResponse);
		} // end of for loop
		
		jsonResponse = null;
	}
	
	public void testGetComments() throws Throwable {
		for(int i=0; i<2; i++) {
			final RequestParams params = new RequestParams();
			
			if(i == 0) {
				/*
				 * First test is that mediaId is not empty.
				 * So we will use the mediaId as param.
				 */
				params.put(Values.TAG_PARENT_ID, sampleMediaId);
				params.put(Values.TAG_TYPE, "media");
				//extendedUrl = Values.GET_EXPORT_MEDIA_TAG;
			} else {
				// If i == 1, of course!
				params.put(Values.TAG_PARENT_ID, sampleEventId);
				params.put(Values.TAG_TYPE, "event");
				//extendedUrl = Values.GET_EXPORT_EVENT_TAG;
			}
			
			params.put(Values.TAG_PAGE, "1");
			params.put(Values.TAG_PAGE_SIZE, "20");
			
			final CountDownLatch signal = new CountDownLatch(1);
			runTestOnUiThread(new Runnable() {
				
				@Override
				public void run() {
					httpClient.GET(Values.GET_EXPORT_COMMENTS, params, new JsonHttpResponseHandler() {
						
						@Override
						public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
							super.onSuccess(statusCode, headers, response);
							jsonResponse = response;
							signal.countDown();
						}
						
						@Override
						public void onFailure(Throwable e, JSONObject errorResponse) {
							super.onFailure(e, errorResponse);
							jsonResponse = null;
							signal.countDown();
						}
					});
				}
			});
			
			signal.await();
			assertNotNull("response: " + jsonResponse, jsonResponse);
		}
		
		jsonResponse = null;
	}
	
	public void testGetFansList() throws Throwable {
		final RequestParams params = new RequestParams();
		params.put(Constant.EXTRA_EVENT_ID, sampleEventId);
		params.put(Values.TAG_PAGE, "" + 2);
		params.put(Values.TAG_PAGE_SIZE, "20");
		
		final CountDownLatch signal = new CountDownLatch(1);
		runTestOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				httpClient.GET(Values.GET_EXPORT_FANS_LIST, params, new JsonHttpResponseHandler() {
					
					@Override
					public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
						super.onSuccess(statusCode, headers, response);
						jsonResponse = response;
						signal.countDown();
					}
					
					@Override
					public void onFailure(Throwable e, JSONObject errorResponse) {
						super.onFailure(e, errorResponse);
						jsonResponse = null;
						signal.countDown();
					}
				});
			}
		});
		
		signal.await();
		assertNotNull("response: " + jsonResponse, jsonResponse);
		
		jsonResponse = null;
	}
	
	public void testPreconditions() {
		solo.assertCurrentActivity("I should be on Fav_Tag_Comment_Activity", Fav_Tag_Comment_Activity.class);
		solo.assertMemoryNotLow();
		
		assertNotNull(listView);
		assertNotNull(footerView);
		int footerCount = listView.getFooterViewsCount();
		assertTrue("ListView footer count is " + footerCount, footerCount > 0);
//		assertNotNull(listView.getAdapter());//asserterror
	}
	
	public void testIntents() {
		String eventId = ftcActivity.getIntent().getStringExtra(Constant.EXTRA_EVENT_ID);
		assertEquals("ExtraIntentId: " + eventId + " and expected 32", "32", eventId);
		
		String mediaId = ftcActivity.getIntent().getStringExtra(Constant.EXTRA_MEDIA_ID);
		assertEquals("ExtraMediaId: " + mediaId + " and expected 345", "345", mediaId);
		
		String listType = ftcActivity.getIntent().getStringExtra(Values.TAG_LIST_TYPE);
		assertEquals("ExtraListType: " + listType + " and expected favorite", "favorite", listType);
	}
	
//	public void testAdapter() {
//		for(int i=0; i<arrKeys.length; i++) {
//			setActivityInitialTouchMode(false);
//			
//			Intent intent = new Intent();
//			intent.putExtra(Values.TAG_LIST_TYPE, arrKeys[i]);
//			setActivityIntent(intent);
//			
//			Activity activity = getActivity();
//			ListView viewList = (ListView) activity.findViewById(com.sportxast.SportXast.R.id.listview_comment);
//			
//			String extraListType = activity.getIntent().getStringExtra(Values.TAG_LIST_TYPE);
//			if(extraListType.equals(KEY.favorite)) {
//				Adapter adapter = viewList.getAdapter();
//				assertNotNull(adapter);
//			} else if(extraListType.equals(KEY.tag)) {
//				Adapter adapter = viewList.getAdapter();
//				assertNotNull(adapter);
//			} else if(extraListType.equals(KEY.comment)) {
//				Adapter adapter = viewList.getAdapter();
//				assertNotNull(adapter);
//			} else if(extraListType.equals(KEY.fans)) {
//				Adapter adapter = viewList.getAdapter();
//				assertNotNull(adapter);
//			}
//			
////			if(activity.getIntent().getStringExtra(Values.TAG_LIST_TYPE).equals(KEY.comment)) {
////				// If list type is "comment", commentView should be visible.
////				assertTrue("CommentView is " + comment.getVisibility(),
////						comment.getVisibility() == View.VISIBLE);
////				//fail();
////			} else {
////				// else commentView should be gone
////				assertTrue("CommentView is " + comment.getVisibility(),
////						comment.getVisibility() == View.GONE);
////			}
////			
//			activity.finish();
//			setActivity(null);
//		}
//	}
}
