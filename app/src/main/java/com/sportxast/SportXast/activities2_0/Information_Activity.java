package com.sportxast.SportXast.activities2_0;

//import com.sportxast.SportXast.BaseSherlockActivity;
//import com.sportxast.SportXast.Global_Data;
import com.sportxast.SportXast.R;
import com.sportxast.SportXast.adapter2_0.InformationAdapter;
import com.sportxast.SportXast.includes.HeaderUIClass;
import com.sportxast.SportXast.models._Links;
import com.sportxast.SportXast.thirdparty_class.Async_HttpClient;

import java.util.ArrayList;
import java.util.Locale;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.RelativeLayout;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

//public class Information_Activity extends BaseSherlockActivity {

@SuppressLint("DefaultLocale")
public class Information_Activity extends Activity {
    //TODO: Display lists of required Information

    /** Called when the activity is first created. */
    private HeaderUIClass headerUIClass;
    private RelativeLayout sx_header_wrapper;

    private RelativeLayout pbLoading_container;
    //private Global_Data global_Data;
    private Async_HttpClient async_HttpClient;

    //private ListView listView;
    private ListView listvw_infonames;
    private ArrayList<_Links> list_links = new ArrayList<_Links>();

    /** Called when the activity is first created. */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        //setContentView(contentView());
        setContentView(R.layout.layout_information);
		/*
		initActionBarObjects();
		getActionbar_Menu_Item("Information");
		*/
        prepareHeader();
        initializeResources();
    }

    /** @category Override onBack- from BaseSherlockActivityclass
     * custom method from BaseSherlockActivity.Invoked when back button (found
     * on left side of the action bar) is clicked/pressed.
     * */
	
	/*
	@Override
	public void onBack(View v) {
		// TODO Auto-generated method stub
		super.onBack(v);
		finish();
	} 
	*/

    /** @category Custom Method
     * Create a custom contentView
     * @return View  - listView
     * */
    private void initializeResources(){
        pbLoading_container = (RelativeLayout) findViewById(R.id.pbLoading_container);
        //listvw_infonames = new ListView(this);
        listvw_infonames = (ListView) findViewById(R.id.listvw_infonames);
		/*
		listvw_infonames.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
		listvw_infonames.setDivider(null);
		*/

        //	listView.setAdapter(new InformationAdapter(this,list_links));
        fetchData();

        listvw_infonames.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                // TODO Auto-generated method stub
                //Intent intent = new Intent(Information_Activity.this, Web_Activity.class);
                Intent intent = new Intent(Information_Activity.this, Web_Activity.class);
                intent.putExtra("urlLink",list_links.get(arg2).url);
                intent.putExtra("title",list_links.get(arg2).title);
                startActivity(intent);
            }

        });
        //return listView;
    }
	
	/*
	public View contentViewXXX(){
		
		listView = new ListView(this);
		listView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
		listView.setDivider(null);
	//	listView.setAdapter(new InformationAdapter(this,list_links));
		fetchData();
		
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub 
				//Intent intent = new Intent(Information_Activity.this, Web_Activity.class);
				Intent intent = new Intent(Information_Activity.this, Web_Activity.class);
				intent.putExtra("urlLink",list_links.get(arg2).url);
				intent.putExtra("title",list_links.get(arg2).title); 
				startActivity(intent);                  
			} 
			
		});
		
		return listView;
	}
	
	*/

    private void prepareHeader(){
        this.sx_header_wrapper = (RelativeLayout)findViewById(R.id.sx_header_wrapper);
        this.headerUIClass = new HeaderUIClass(this, sx_header_wrapper);
        this.headerUIClass.setMenuTitle("Information");

        this.headerUIClass.showBackButton(	 true);
        this.headerUIClass.showAddButton(	 false);
        this.headerUIClass.showMenuButton(	 false);
        this.headerUIClass.showRefreshButton(false);
        this.headerUIClass.showAboutButton(	 false);
        this.headerUIClass.showSearchButton( false);
        this.headerUIClass.showDoneButton(	 false);
        this.headerUIClass.showCameraButton( false);
        this.headerUIClass.showMenuTitle(	 true);

        //this.headerUIClass.setMenuTitle(	 false);
        this.headerUIClass.showMenuTitle0(	 false);

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
    /** @category Custom Method
     * Fetch list of links  from server.**/
    public void fetchData(){
        async_HttpClient = new Async_HttpClient(this);

        async_HttpClient.GET("Links", new RequestParams(), new JsonHttpResponseHandler(){

            @Override
            public void onStart() {
                // TODO Auto-generated method stub
                super.onStart();
            }

            @Override
            public void onSuccess(JSONObject response) {
                // TODO Auto-generated method stub
                super.onSuccess(response);
                parseData(response);
                pbLoading_container.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(int arg0, Header[] arg1, byte[] arg2,
                                  Throwable arg3) {
                // TODO Auto-generated method stub
                super.onFailure(arg0, arg1, arg2, arg3);
            }

            @Override
            public void onFinish() {
                // TODO Auto-generated method stub
                super.onFinish();
            }
        });
    }


    /** @category Custom Method
     *  Parse JSONObject result.
     *  @param (JSONObject) jObject_response - the JSONObject result.
     * **/
	/*
	public void parseDataORIGINAL(JSONObject jObject_response){
	
		try {
			JSONArray jsonArray_links = jObject_response.getJSONArray("links");
			for (int i = 0; i < jsonArray_links.length(); i++) {
				
				JSONObject object = jsonArray_links.getJSONObject(i);
				_Links links = new _Links();
				 
				links.title 	= ""+object.get("title"); 
				links.openInApp = ""+object.get("openInApp");
				links.url 		= ""+object.get("url");
				 
				list_links.add(links);
				
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		listView.setAdapter(new InformationAdapter(this, list_links));
		
	}
	*/

    private void parseData(JSONObject jObject_response){

        try {
            JSONArray jsonArray_links = jObject_response.getJSONArray("links");
            for (int i = 0; i < jsonArray_links.length(); i++) {

                JSONObject object = jsonArray_links.getJSONObject(i);
                _Links links = new _Links();

                if( object.has("title") ){
                    links.title 	= ""+object.get("title");
                }else{
                    links.title 	= "Title Not Found";
                }

                if( object.has("openInApp") ){
                    links.openInApp = ""+object.get("openInApp");
                }else{
                    links.openInApp = "1";
                }

                links.url 		= ""+object.get("url");


                if( determineRedundantLinks(links.title) == false)
                    continue;

                list_links.add(links);

            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        listvw_infonames.setAdapter(new InformationAdapter(this, list_links));

    }

    private boolean determineRedundantLinks(String strTitle){
        strTitle = strTitle.toLowerCase();
        String[] arrLinks = {"privacy policy", "terms of service", "help", "about"};

        for (int i = 0; i < arrLinks.length; i++) {

            if( strTitle.equals(arrLinks[i].toString().toLowerCase()) ){
                return true;
            }
        }

        return false;
    }

}

















