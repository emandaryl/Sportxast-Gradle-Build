package com.sportxast.SportXast.activities2_0;

//import com.sportxast.SportXast.BaseSherlockActivity;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.sportxast.SportXast.Global_Data;
import com.sportxast.SportXast.R;
import com.sportxast.SportXast.includes.HeaderUIClass;

//public class Web_Activity extends BaseSherlockActivity {
public class Web_Activity extends Activity {

    /** Header composition **/
    private HeaderUIClass headerUIClass;
    private RelativeLayout sx_header_wrapper;

    private Global_Data global_Data;
    private WebView webact_wvInfo;

    private RelativeLayout webviewCont1;
    private RelativeLayout rw_pbLoading_container;
    private ProgressBar rw_pbLoading;

    private String FURLLink="";
    private String FTitle="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        //setContentView(loaderView());
        setContentView( R.layout.layout_webactivity );

        try {
            FURLLink= getIntent().getExtras().getString("urlLink");
            FTitle 	= getIntent().getExtras().getString("title");

        } catch (Exception e) {
            // TODO: handle exception
        }
		/*
		initActionBarObjects();
		getActionbar_Menu_Item(title);
		*/
        prepareHeader();

        initializeResources();
        initializeWebView();
        //resizeReportWindow();
        getInfo( FURLLink );
    }


    private void prepareHeader(){
        this.sx_header_wrapper = (RelativeLayout)findViewById(R.id.sx_header_wrapper);
        this.headerUIClass = new HeaderUIClass(this, sx_header_wrapper);
        this.headerUIClass.setMenuTitle( FTitle );

        this.headerUIClass.showBackButton(	 true);
        this.headerUIClass.showAddButton(	 false);
        this.headerUIClass.showMenuButton(	 false);
        this.headerUIClass.showRefreshButton(false);
        this.headerUIClass.showAboutButton(	 false);
        this.headerUIClass.showSearchButton( false);
        this.headerUIClass.showDoneButton( 	 true);
        this.headerUIClass.showCameraButton( false);
        this.headerUIClass.showMenuTitle(	 true);

        //this.headerUIClass.setMenuTitle(	 false);
        this.headerUIClass.showMenuTitle0(	 false);
        //this.headerUIClass.setDoneButtonText("Save");


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

    @SuppressLint("SetJavaScriptEnabled")
    private void initializeWebView() {
        webact_wvInfo = (WebView) findViewById(R.id.webact_wvInfo);
        //webact_wvInfo = (WebView) findViewById(R.id.wewebact_wvInfo);
        webact_wvInfo.setWebViewClient(new MyWebViewClient());
        webact_wvInfo.getSettings().setUseWideViewPort(true);
        webact_wvInfo.getSettings().setLoadWithOverviewMode(true);

        //#######################################################################
	    /* JavaScript must be enabled if you want it to work, obviously */
        webact_wvInfo.getSettings().setJavaScriptEnabled(true);
		
		 /* Register a new JavaScript interface called HTMLOUT */
        //fwvReport.addJavascriptInterface(new MyJavaScriptInterface(), "HTMLOUT");
    }


    private void initializeResources(){
        webviewCont1 = (RelativeLayout) findViewById(R.id.webviewCont1a);

        rw_pbLoading_container = (RelativeLayout) findViewById(R.id.webact_pbLoading_container);
        //rw_pbLoading_container.setVisibility(View.GONE);
        rw_pbLoading = (ProgressBar) findViewById(R.id.webact_pbLoading);
    }
	
	/*
	private void resizeReportWindow(){
		  
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		//int screenHeight = metrics.heightPixels;
		int screenWidth = metrics.widthPixels;
		 
		webviewCont1.setLayoutParams( new LinearLayout.LayoutParams( screenWidth,  LayoutParams.FILL_PARENT) );
	 }
	*/

    private void getInfo(String URLPath) {
		/*
		String sUrl = FCIPath.getHTTPHost() + "/~jiwu_LOGIN/program_link/point_view_detailV2.php?user_id="+Integer.toString(FUserID)+"&program_id=104";

		sUrl = "http://192.168.169.151/JIWU_WEB/appPage/report/reportCat.php?user_id=12&program_id=104&catId=3221";
		*/
        //fwvReport.loadUrl(sUrl);
        String hey = URLPath;
        String xx = hey;

        //String ngek = "http://sportxast.com/tos/";

        webact_wvInfo.loadUrl( URLPath );
    }

    private class MyWebViewClient extends WebViewClient{
        @Override
        public void onPageFinished(WebView view, String url)
        {   /* This call inject JavaScript into the page which just finished loading. */
            //	rw_wbUpdate.loadUrl("javascript:window.HTMLOUT.showHTML('<head>'+document.getElementsByTagName('html')[0].innerHTML+'</head>');");

            //	fwvReport.setVisibility(View.VISIBLE);
            //	Toast.makeText(getApplicationContext(), "yoyo", Toast.LENGTH_LONG).show();

            rw_pbLoading_container.setVisibility(View.GONE);

            //
            //Toast.makeText(getApplicationContext(), "A: " + url, Toast.LENGTH_LONG).show();

        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url)
        {
			/*shitSample();*/
            view.loadUrl(url);
            // Toast.makeText(getApplicationContext(), "B: " + url, Toast.LENGTH_LONG).show();
            return true;
        }

        @Override
        public void onLoadResource(WebView view, String url) {
            // TODO Auto-generated method stub
            super.onLoadResource(view, url);

            //	 Toast.makeText(getApplicationContext(), "C: " + url, Toast.LENGTH_LONG).show();

        }
    }


    /* An instance of this class will be registered as a JavaScript interface */
    public class MyJavaScriptInterface
    {
        public void showHTML(String html)
        {
            new AlertDialog.Builder(Web_Activity.this)
                    .setTitle("HTML")
                    .setMessage(html)
                    .setPositiveButton(android.R.string.ok, null)
                    .setCancelable(false)
                    .create();
            //   pageHTML = html;
            //.show();
            //  Toast.makeText(getApplicationContext(), pageHTML, Toast.LENGTH_LONG).show();
        }
    }
	 
	/*
	
	@Override
		public void onBack(View v) {
			// TODO Auto-generated method stub
			super.onBack(v);
		
			finish();
	}
	
	*/
/*
	public View  loaderView(){
		
		return getLayoutInflater().inflate(R.layout.progressbar_medium, null);
	}

	*/
}
