package com.sportxast.SportXast.activities2_0;

//import com.sportxast.SportXast.Global_Data;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sportxast.SportXast.R;
import com.sportxast.SportXast.adapter2_0.ChooseATeamListAdapter;
import com.sportxast.SportXast.includes.HeaderUIClass;
import com.sportxast.SportXast.models._SearchHashtag;
import com.sportxast.SportXast.thirdparty_class.Async_HttpClient;
import com.sportxast.SportXast.thirdparty_class.HeaderListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ChooseATeam_Activity extends Activity {
    //TODO : Display available Teams to List
    /** Called when the activity is first created. */

    private HeaderUIClass headerUIClass;
    private RelativeLayout sx_header_wrapper;

    private HeaderListView headerListView;
    private EditText edittxt_search;

    private Async_HttpClient async_HttpClient;
    private ArrayList<_SearchHashtag> lists_team;
    private ArrayList<_SearchHashtag> lists_team_filtered;

    private int team = 0;

    private JSONObject JSONSportChosen;
    public void exitActivity( String teamName ){

        this.JSONSportChosen = new JSONObject();
        try {
            this.JSONSportChosen.put("team"+Integer.toString(FTeamNumber), teamName);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        //######################################################
        Intent returnIntent = new Intent();
        returnIntent.putExtra("teamChosen", this.JSONSportChosen.toString() );
        returnIntent.putExtra("teamNumber", FTeamNumber);
        setResult(RESULT_OK, returnIntent);
        //######################################################

        finish();
    }

    private int FTeamNumber;

    private RelativeLayout pbLoading_container;
    private RelativeLayout suggested_text_cont;
    private TextView suggested_text;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_sportslist);
        // TODO Auto-generated method stub
        async_HttpClient = new Async_HttpClient(this);

        try {
            team = getIntent().getExtras().getInt("team");
        } catch (Exception e) {
            // TODO: handle exception
        }

        try {
            FTeamNumber = getIntent().getExtras().getInt("teamNumber", 1);
        } catch (Exception e) {
            // TODO: handle exception
            FTeamNumber = 1;
        }

        prepareHeader();
        initializeResources();

        gatherRecentTeams();
    }

    private SharedPreferences FSharedpreferences;
    private String FRecentTeamsKey = "knG7QubcZbyK39";

    /** retrieved locally saved Recent Created Events **/
    public void gatherRecentTeams(){
        //ArrayList<_RecentEvent> arrRecentEvents = new ArrayList<_RecentEvent>();
        FSharedpreferences = getSharedPreferences("com.sportxast.SportXast.recentTeamsLocal", Context.MODE_PRIVATE);
        if ( FSharedpreferences.contains( FRecentTeamsKey ) ){

            String highlightDataInString = FSharedpreferences.getString(FRecentTeamsKey, "");
            String[] arrHighlightsData	= highlightDataInString.split("\\|\\|");

            if(arrHighlightsData.length > 0 ){
                recentteam_cont1.setVisibility(View.VISIBLE);
            }else{
                recentteam_cont1.setVisibility(View.GONE);
            }

            LayoutInflater infalInflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            int loopLimit = 0;
            if(arrHighlightsData.length > 5)
                loopLimit = arrHighlightsData.length - 5;

            for (int i = (arrHighlightsData.length - 1); i >= loopLimit; i--) {
                // for (int i = 0; i < arrHighlightsData.length; i++) {
                RelativeLayout lyt1 = (RelativeLayout) infalInflater.inflate( R.layout.list_item_information, null);
                TextView recentTeamTextview = (TextView)lyt1.findViewById(R.id.txtvw_listabout);
                //recentTeamTextview.setTextColor( Color.parseColor("#fc4c06") ); //fc4c06 - uni orange
                recentTeamTextview.setText( arrHighlightsData[i].toString() );
                recentTeamTextview.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        exitActivity( ((TextView)v ).getText().toString() );
                    }
                });

                ( (ImageView) lyt1.findViewById(R.id.imgvw_next) ).setVisibility(View.GONE);
                //recentteam_list_cont1.setPadding(20, 20, 20, 20);
                this.recentteam_list_cont1.addView(lyt1, new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

            }
        }
    }

    private LinearLayout recentteam_cont1;
    private LinearLayout recentteam_list_cont1;

    private void initializeResources(){
        pbLoading_container = (RelativeLayout) findViewById( R.id.pbLoading_container );
        edittxt_search 		= (EditText) findViewById( R.id.edittext_search );
        edittxt_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
                if (arg0.toString().length() > 2) {
                    ( (RelativeLayout) findViewById(R.id.rl_listvw_cont1) ).setVisibility(View.VISIBLE);
                    recentteam_cont1.setVisibility(View.GONE);
                    gatherSearchResults();
                }else{
                    //	suggested_text_cont.setVisibility(View.GONE);
                    ( (RelativeLayout) findViewById(R.id.rl_listvw_cont1) ).setVisibility(View.GONE);
                    recentteam_cont1.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                // TODO Auto-generated method stub
            }

        });

        this.suggested_text_cont = (RelativeLayout) findViewById(R.id.suggested_text_cont);
        this.suggested_text_cont.setVisibility(View.GONE);
        suggested_text =  (TextView) suggested_text_cont.findViewById(R.id.suggested_text);
        this.suggested_text.setTag("");
        suggested_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //Toast.makeText(getApplicationContext(),  ( (TextView) v ).getText().toString() , Toast.LENGTH_LONG).show();
                //exitActivity( ( (TextView) v ).getText().toString() );
                exitActivity( ( (TextView) v ).getTag().toString() );
            }
        });

        recentteam_cont1 =  (LinearLayout) findViewById(R.id.recentteam_cont1);
        recentteam_list_cont1 =  (LinearLayout) recentteam_cont1.findViewById(R.id.recentteam_list_cont1);

        headerListView = (HeaderListView) findViewById(R.id.listvw_sportlists);
        if(FTeamList == null)
            FTeamList = new ArrayList<_SearchHashtag>();

        listTeamAdapter = new ChooseATeamListAdapter(ChooseATeam_Activity.this, FTeamList, team);
        headerListView.setAdapter( listTeamAdapter );
    }

    private ChooseATeamListAdapter listTeamAdapter;
    private void gatherSearchResults(){
        this.pbLoading_container.setVisibility(View.VISIBLE);

        /**https://dev.sportxast.com/phone/apiV2/Search?limit=10&page=1&q=stan&type=team **/
        RequestParams requestParams = new RequestParams();
        requestParams.put("limit", "10");
        requestParams.put("page", "1");

        //requestParams.put("q", "team");
        final String stringToSearch =  edittxt_search.getText().toString();
        requestParams.put("q", stringToSearch );
        requestParams.put("type", "team");

        //Log.e("team", requestParams.toString());
        async_HttpClient.GET("Search", requestParams,
                new JsonHttpResponseHandler() {

                    @Override
                    public void onStart() {
                        // TODO Auto-generated method stub
                        super.onStart();
                        Log.v("onStart", "onStart");
                    }

                    @Override
                    public void onSuccess(JSONObject response) {
                        // TODO Auto-generated method stub
                        super.onSuccess(response);

                        pbLoading_container.setVisibility(View.GONE);
                        //Toast.makeText(getApplicationContext(), "HEYZ-- "+ response.toString(), Toast.LENGTH_LONG).show();
                        if( !parseTeamData(response) ){
                            headerListView.setVisibility(View.GONE);

                            //Toast.makeText(getApplicationContext(), "nops", Toast.LENGTH_LONG).show();
                            suggested_text_cont.setVisibility(View.VISIBLE);
                            suggested_text.setText("Add \""+stringToSearch + "\"?" );
                            suggested_text.setTag(stringToSearch);
                        }else{
                            headerListView.setVisibility(View.VISIBLE);
                            recentteam_cont1.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onFinish() {
                        // TODO Auto-generated method stub
                        super.onFinish();
                        Log.v("onFinish", "onFinish");
                    }

                    @Override
                    public void onFailure(String responseBody, Throwable error) {
                        // TODO Auto-generated method stub
                        super.onFailure(responseBody, error);
                        Log.v("onFailure", "onFailure :" + responseBody + " : " + error);
                    }
                });
    }

    private ArrayList<_SearchHashtag> FTeamList;
    /** returns: if empty? true or false **/
    public boolean parseTeamData(JSONObject response) {

        boolean listCreated = false;
        FTeamList = new ArrayList<_SearchHashtag>();
        try {
            JSONArray jsonArray_response = response.getJSONArray("list");
            for (int i = 0; i < jsonArray_response.length(); i++) {
                JSONObject object 	= jsonArray_response.getJSONObject(i);
                _SearchHashtag team = new _SearchHashtag();
                team.id 			= "" + object.get("id");
                team.name 			= "" + object.get("name");
                team.avatarPath 	= "" + object.get("avatarPath");
                team.type 			= "" + object.get("type");
                FTeamList.add(team);
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if( FTeamList.size() > 0 ){
            listCreated = true;
            //Toast.makeText(getApplicationContext(), "size:-- " + FTeamList.size(), Toast.LENGTH_LONG).show();
            listTeamAdapter.updateListElements( FTeamList );
        }else{
            listCreated = false;
            //Toast.makeText(getApplicationContext(), "walay nakuha brad", Toast.LENGTH_LONG).show();
        }

        return listCreated;
    }

    private void prepareHeader(){

        this.sx_header_wrapper = (RelativeLayout)findViewById(R.id.sx_header_wrapper);
        this.headerUIClass = new HeaderUIClass(this, sx_header_wrapper);
        this.headerUIClass.setMenuTitle("Teams");
        this.headerUIClass.showBackButton(	 true);
        this.headerUIClass.showAddButton(	 false);
        this.headerUIClass.showMenuButton(	 false);
        this.headerUIClass.showRefreshButton(false);
        this.headerUIClass.showAboutButton(	 false);
        this.headerUIClass.showSearchButton( false);
        this.headerUIClass.showDoneButton(	 false);
        this.headerUIClass.showCameraButton( false);
        this.headerUIClass.showMenuTitle(	 true);
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

            @Override
            public void onFollowClicked() {
                // TODO Auto-generated method stub

            }
        });
    }

    @SuppressLint("DefaultLocale")
    public void getSearchResult(String search) {
        int strToSearchLength = search.length();

        lists_team_filtered = new ArrayList<_SearchHashtag>();
        //boolean matchIsFound = false;

        for (int j = 0; j < 4; j++) {

            for (int i = 0; i < lists_team.size(); i++) {
                String placeName_full = lists_team.get(i).name;

                String[] sportWordElements = placeName_full.split("\\s+");

                if(sportWordElements.length < (j+1) )
                    continue;

                String placeName_sub = sportWordElements[j].toString().toLowerCase();

                int strToSearchLength_ = strToSearchLength;
                if(strToSearchLength_ > placeName_sub.length() )
                    strToSearchLength_ = placeName_sub.length();

                String placeNameInitialChars = placeName_sub.substring( 0, strToSearchLength_ ).toLowerCase();
                //sportNameInitialChars = sportNameInitialChars.toLowerCase();

                if( placeNameInitialChars.equals(search) ){
                    lists_team_filtered.add( (_SearchHashtag) lists_team.get(i) );
                    //sportsInLetter_.sportsInLetter.add(sports);
                    //matchIsFound = true;
                }
            }
			
			/*
			if(matchIsFound)
				 break;
			*/
        }

        headerListView.setAdapter(new ChooseATeamListAdapter(this, lists_team_filtered, team ));
    }


    /** @category Custom Method
     * Fetch list of available team from server.**/
    public void fetchData() {
        RequestParams requestParams = new RequestParams();
        requestParams.put("q", edittxt_search.getText().toString());
        requestParams.put("type", "team");
        Log.e("team", requestParams.toString());
        async_HttpClient.GET("Search", requestParams,
                new JsonHttpResponseHandler() {

                    @Override
                    public void onStart() {
                        // TODO Auto-generated method stub
                        super.onStart();
                        Log.v("onStart", "onStart");
                    }

                    @Override
                    public void onSuccess(JSONObject response) {
                        // TODO Auto-generated method stub
                        super.onSuccess(response);
                        parseData(response);
                    }

                    @Override
                    public void onFinish() {
                        // TODO Auto-generated method stub
                        super.onFinish();
                        Log.v("onFinish", "onFinish");

                    }

                    @Override
                    public void onFailure(String responseBody, Throwable error) {
                        // TODO Auto-generated method stub
                        super.onFailure(responseBody, error);
                        Log.v("onFailure", "onFailure :" + responseBody + " : "
                                + error);

                    }
                });
    }

    public void parseData(JSONObject response) {
        lists_team = new ArrayList<_SearchHashtag>();
        try {
            JSONArray jsonArray_response = response.getJSONArray("list");
            for (int i = 0; i < jsonArray_response.length(); i++) {

                JSONObject object 	= jsonArray_response.getJSONObject(i);
                _SearchHashtag team = new _SearchHashtag();
                team.id 			= "" + object.get("id");
                team.name 			= "" + object.get("name");
                team.avatarPath 	= "" + object.get("avatarPath");
                team.type 			= "" + object.get("type");
                lists_team.add(team);

            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


        lists_team_filtered = lists_team;
        headerListView.setAdapter(new ChooseATeamListAdapter(this, lists_team_filtered, team));
    }

}
