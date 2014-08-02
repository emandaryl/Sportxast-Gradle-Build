package com.sportxast.SportXast.activities2_0;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sportxast.SportXast.R;
import com.sportxast.SportXast.adapter2_0.ChooseASportListAdapter;
import com.sportxast.SportXast.includes.HeaderUIClass;
import com.sportxast.SportXast.models._SportsList;
import com.sportxast.SportXast.models._SportsList.Sports;
import com.sportxast.SportXast.models._SportsList.SportsInLetter;
import com.sportxast.SportXast.thirdparty_class.Async_HttpClient;
import com.sportxast.SportXast.thirdparty_class.HeaderListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Collections;

public class ChooseASport_Activity extends Activity {

    // TODO: Display Lists of Sports.
    // TODO: Use HeaderListView class for ListView

    /** Called when the activity is first created. */
    private HeaderUIClass 	headerUIClass;
    private RelativeLayout 	sx_header_wrapper;
    private HeaderListView 	headerListView;
    private EditText 		edittxt_search;

    private _SportsList searchsportsList = new _SportsList();
    private _SportsList mainsportsList = new _SportsList();
    //ChooseASportListAdapter chooseASportListAdapter;

    //private Sports FSportChosen;
    private JSONObject JSONSportChosen;

    private String FSportChosenStr;
    private RelativeLayout pbLoading_container;

    public void exitActivity( Sports sportChosen ){
        this.JSONSportChosen = new JSONObject();
        //sample Jason outpud:
        // {"shareUrl":"http:\/\/goo.gl\/1GdDMn","mediaId":"11929","shareText":"Watch this highlight captured by SportXast","added":"new media added","imagePath":"db\/event_media\/2014\/06\/02\/11929.jpg","videoPath":"db\/event_media\/2014\/06\/02\/11929.mp4"}
        try {
            this.JSONSportChosen.put("sportId", 		sportChosen.sportId);
            this.JSONSportChosen.put("sportName", 		sportChosen.sportName);
            this.JSONSportChosen.put("sportFirstLetter",sportChosen.sportFirstLetter);
            this.JSONSportChosen.put("sportLogo",		sportChosen.sportLogo);
            this.JSONSportChosen.put("sportWhiteLogo",	sportChosen.sportWhiteLogo);

        }catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        //######################################################
        Intent returnIntent = new Intent();
        returnIntent.putExtra("JSONSportChosen", this.JSONSportChosen.toString() );
        setResult(RESULT_OK, returnIntent);
        //######################################################

        finish();
        //	return jsonObject;
    }

    /** Called when the activity is first created. */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_sportslist);
		/*
		initActionBarObjects();
		getActionbar_Menu_Item("Sports"); 
		*/
        prepareHeader();
        initContentView();
        initSearch();
    }

    private void prepareHeader(){

        this.sx_header_wrapper = (RelativeLayout)findViewById(R.id.sx_header_wrapper);
        this.headerUIClass = new HeaderUIClass(this, sx_header_wrapper);
        this.headerUIClass.setMenuTitle("Sports");
        this.headerUIClass.showMenuButton(false);
        this.headerUIClass.showBackButton(true);
        this.headerUIClass.showSearchButton(false);
        this.headerUIClass.showDoneButton(false);

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

    /**
     * @category Custom Method
     * Custom function for initializing content view and declared objects */
    public void initContentView() {

        pbLoading_container = (RelativeLayout) findViewById(R.id.pbLoading_container);
        pbLoading_container.setVisibility(View.GONE);

        headerListView = (HeaderListView) findViewById(R.id.listvw_sportlists);
        // chooseASportListAdapter = new ChooseASportListAdapter(this,
        // mainsportsList);
        headerListView.setAdapter(new ChooseASportListAdapter(this, mainsportsList));
        //async_HttpClient = new Async_HttpClient(this);
        gatherSportsList();
    }

    private RelativeLayout suggested_text_cont;
    private TextView suggested_text;
    /**
     * @category Custom Method
     * Custom function for initializing edittxt_search and add a TextChangedListener to it to read edittxt_search input event. */
    public void initSearch() {

        this.suggested_text_cont = (RelativeLayout) findViewById(R.id.suggested_text_cont);
        this.suggested_text_cont.setVisibility(View.GONE);
        this.suggested_text 	 = (TextView) suggested_text_cont.findViewById(R.id.suggested_text);
        this.suggested_text.setTag("");
        this.suggested_text.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                String customSportName = v.getTag().toString();

                Sports sports 		= new Sports();

                sports.sportId	 	= "-69";
                sports.sportName 	= customSportName;
                sports.sportLogo 	= "";
                sports.sportFirstLetter = customSportName.substring(0, 1);

                exitActivity( sports );
            }
        });


        edittxt_search = (EditText) findViewById(R.id.edittext_search);
        edittxt_search.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub

                if (arg0.toString().length() > 0) {
                    getSearchResult(arg0.toString());

                } else {
                    headerListView.setAdapter(new ChooseASportListAdapter(
                            ChooseASport_Activity.this, mainsportsList));
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
    }


    /** @category Custom Method
     *  Called after edittxt_search text changes to display results on headerListView.  **/
	/*
	public void getSearchResultORIGINAL(String search) {
		searchsportsList = new _SportsList();

		for (int i = 0; i < mainsportsList.sports.size(); i++) {
			SportsInLetter sportsInLetter = mainsportsList.sports.get(i);
			for (int j = 0; j < sportsInLetter.sportsInLetter.size(); j++) {
				Sports sports = sportsInLetter.sportsInLetter.get(j);
				if (sports.sportName.contains(search)) {
					searchsportsList.sports.add(sportsInLetter);
					searchsportsList.letters.add(sports.sportFirstLetter);
				}
			}
		}
 
		HashSet hs = new HashSet();
		hs.addAll(searchsportsList.letters);
		searchsportsList.letters.clear();
		searchsportsList.letters.addAll(hs);

		Collections.sort(searchsportsList.letters);

		headerListView.setAdapter(new ChooseASportListAdapter(this, searchsportsList)); 
	}
	*/

    private void filter_SportsNamesArrayList(){

    }

    /** @category Custom Method
     *  Called after edittxt_search text changes to display results on headerListView.  **/
    public void getSearchResult(String search) {

        this.suggested_text_cont.setVisibility(View.GONE);

        searchsportsList = new _SportsList();

        search = search.toLowerCase().trim();
        int strToSearchLength = search.length();
		 /*
		String testy = "afdDKJLHfasf/af/"; 
		String ngek = testy.toLowerCase();
		 */
        boolean matchIsFound = false;
        //FIRST STEP IN SEARCHING: searching for initial/firsts characters of the FIRST WORD of the string
        SportsInLetter sportsInLetter_= new SportsInLetter();
        for (int i = 0; i < mainsportsList.sports.size(); i++) {
            SportsInLetter sportsInLetter = mainsportsList.sports.get(i);

            for (int j = 0; j < sportsInLetter.sportsInLetter.size(); j++) {
                Sports sports = sportsInLetter.sportsInLetter.get(j);

                int strToSearchLength_ = strToSearchLength;
                if(strToSearchLength_ > sports.sportName.length() )
                    strToSearchLength_ = sports.sportName.length();

                String sportName_ = sports.sportName;
                String sportDataID = sports.sportId;
                String sportNameInitialChars = sports.sportName.substring( 0, strToSearchLength_ ).toLowerCase();
                //sportNameInitialChars = sportNameInitialChars.toLowerCase();

                if( sportNameInitialChars.equals(search) ){
                    sportsInLetter_.sportsInLetter.add(sports);
                    matchIsFound = true;
                }

            }
        }

        if(matchIsFound){
            searchsportsList.sports.add(sportsInLetter_);
            searchsportsList.letters.add( search );
        }
        else if(matchIsFound == false){ //IF NO MATCH FOUND on the first SEARCH

            //SECOND STEP IN SEARCHING: searching for initial/firsts characters of the SECOND WORD of the string
            for (int i = 0; i < mainsportsList.sports.size(); i++) {
                SportsInLetter sportsInLetter = mainsportsList.sports.get(i);
                for (int j = 0; j < sportsInLetter.sportsInLetter.size(); j++) {
                    Sports sports = sportsInLetter.sportsInLetter.get(j);

                    String[] sportWordElements = sports.sportName.split("\\s+");

                    if(sportWordElements.length < 2)
                        continue;
				/*
				Pattern p = Pattern.compile("[^a-zA-Z0-9]");
				boolean hasSpecialChar = p.matcher( sportWordElements[1].toString() ).find();
				if(hasSpecialChar){}
				*/
                    int strToSearchLength_ = strToSearchLength;

                    if(strToSearchLength_ > sportWordElements[1].toString().length() )
                        strToSearchLength_ = sportWordElements[1].toString().length();

                    //String sportNameInitialChars = sportWordElements[1].toString().substring( 0, strToSearchLength ).toLowerCase();
                    String sportNameInitialChars = sportWordElements[1].toString().substring( 0, strToSearchLength_ ).toLowerCase();

                    if( sportNameInitialChars.equals(search) ){
                        sportsInLetter_.sportsInLetter.add(sports);
                        matchIsFound = true;
                    }
                }
            }

            if(matchIsFound){ //if match IS FOUND on the SECOND SEARCH
                searchsportsList.sports.add(sportsInLetter_);
                searchsportsList.letters.add( search );
            }

        }
        //#######################################################################
        //#######################################################################

        searchsportsList.letters.clear();
        searchsportsList.sports.add(sportsInLetter_);
        //searchsportsList.letters.add( "No results found" );
        //searchsportsList.letters.add( "Add " +  "\""+search+"\"");

        searchsportsList.letters.add( search );
		 
		/*
		this.suggested_text_cont = (RelativeLayout) findViewById(R.id.suggested_text_cont); 
		this.suggested_text_cont.setVisibility(View.GONE);
		this.suggested_text 	 = (TextView) findViewById(R.id.suggested_text); 
		*/


        //this.suggested_text_cont.setVisibility(View.VISIBLE);

        Collections.sort(searchsportsList.letters);

        headerListView.setAdapter(new ChooseASportListAdapter(this, searchsportsList));

        if(matchIsFound == false){
            //Toast.makeText(getApplicationContext(), "wala bai", Toast.LENGTH_SHORT).show();
            headerListView.setVisibility(View.GONE);

            suggested_text.setText("Add \""+search+"\"?");
            suggested_text.setTag( search );
            this.suggested_text_cont.setVisibility(View.VISIBLE);
        }else{

            //Toast.makeText(getApplicationContext(), "naa bai", Toast.LENGTH_SHORT).show();
            headerListView.setVisibility(View.VISIBLE);

        }

    }


    /** @category Custom Method
     * Fetch list of sport from server.**/
    public void gatherSportsList() {

        if(pbLoading_container != null)
            pbLoading_container.setVisibility(View.VISIBLE);

        Async_HttpClient async_HttpClient = new Async_HttpClient(this);
        async_HttpClient.GET("SportList", new RequestParams(),
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
                        Log.v("onSuccess", response.toString());
                        parseSportList(response.toString());

                        pbLoading_container.setVisibility(View.GONE);

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



    /** @category Custom Method
     *  Parse string result.
     *  @param (String) response - the string result.
     * **/
    public void parseSportList(String reponse) {

        try {
            JSONObject jObject_reponse = new JSONObject(reponse);
            JSONArray jArray_letters = jObject_reponse.getJSONArray("letters");
            JSONArray jArray_sports = jObject_reponse.getJSONArray("sports");

            for (int i = 0; i < jArray_letters.length(); i++) {
                mainsportsList.letters.add(jArray_letters.getString(i));

            }

            for (int i = 0; i < jArray_sports.length(); i++) {
                JSONArray jArray_sportInLetters = jArray_sports.getJSONArray(i);
                SportsInLetter sportsInLetter = new SportsInLetter();
                for (int j = 0; j < jArray_sportInLetters.length(); j++) {
                    JSONObject jObject_sport = jArray_sportInLetters
                            .getJSONObject(j);
                    Sports sports = new Sports();
                    sports.sportId = jObject_sport.getString("sportId");
                    sports.sportName = jObject_sport.getString("sportName");
                    sports.sportFirstLetter = jObject_sport
                            .getString("sportFirstLetter");
                    sports.sportLogo = jObject_sport.getString("sportLogo");
                    sports.sportWhiteLogo = jObject_sport
                            .getString("sportWhiteLogo");

                    sportsInLetter.sportsInLetter.add(sports);
                }

                mainsportsList.sports.add(sportsInLetter);

            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // sportsList = mainsportsList;
        headerListView.setAdapter(new ChooseASportListAdapter(this,
                mainsportsList));
    }



}
