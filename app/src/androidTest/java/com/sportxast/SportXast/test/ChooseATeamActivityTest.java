package com.sportxast.SportXast.test;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.robotium.solo.Solo;
import com.sportxast.SportXast.activities2_0.ChooseATeam_Activity;
import com.sportxast.SportXast.test.constants.Values;
import com.sportxast.SportXast.thirdparty_class.Async_HttpClient;
import com.sportxast.SportXast.thirdparty_class.HeaderListView;

import org.json.JSONObject;

import java.util.concurrent.CountDownLatch;

public class ChooseATeamActivityTest extends ActivityInstrumentationTestCase2<ChooseATeam_Activity> {

	private ChooseATeam_Activity chooseTeamActivity;
	private HeaderListView headerListView;
	private EditText edtSearch;
	private Async_HttpClient httpClient;
	private JSONObject jsonResponse = null;
	private Solo solo;
	
	public ChooseATeamActivityTest() {
		super(ChooseATeam_Activity.class);
	}
	
	@Override
	public void setUp() throws Exception {
		super.setUp();
		chooseTeamActivity = getActivity();
		httpClient = new Async_HttpClient(chooseTeamActivity);
		solo = new Solo(getInstrumentation(), chooseTeamActivity);
		headerListView = (HeaderListView) chooseTeamActivity.findViewById(com.sportxast.SportXast.R.id.listvw_sportlists);
		edtSearch = (EditText) chooseTeamActivity.findViewById(com.sportxast.SportXast.R.id.edittext_search);
	}
	
	public void testPreconditions() {
		assertNotNull(chooseTeamActivity);
		assertNotNull(headerListView);
		assertNotNull(edtSearch);
		assertNotNull(httpClient);
		//String actionBarTitle = chooseTeamActivity.getActionBarTitle();
		//assertEquals("Team", actionBarTitle);
	}
	
	public void testFetchData() throws Throwable {
		final CountDownLatch signal = new CountDownLatch(1);
		solo.enterText(edtSearch, "Spurs");
		
		final RequestParams params = new RequestParams();
		params.put(Values.TAG_QUERY, edtSearch.getText().toString());
		params.put(Values.TAG_TYPE, "team");
		
		runTestOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				httpClient.GET(Values.GET_SEARCH, params, new JsonHttpResponseHandler() {
					
					@Override
					public void onSuccess(JSONObject response) {
						super.onSuccess(response);
						jsonResponse = response;
						signal.countDown();
					}
					
					@Override
					public void onFailure(String responseBody, Throwable error) {
						super.onFailure(responseBody, error);
						jsonResponse = null;
						signal.countDown();
					}
				});
			}
		});
		
		signal.await();
		assertNotNull(jsonResponse);
	}
	
	@Override
	public void tearDown() throws Exception {
		super.tearDown();
	}
}
