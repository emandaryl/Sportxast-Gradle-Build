package com.sportxast.SportXast.activities2_0;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.androidquery.AQuery;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.sportxast.SportXast.Global_Data;
import com.sportxast.SportXast.R;
import com.sportxast.SportXast.commons.CommonFunctions_1;
import com.sportxast.SportXast.includes.HeaderUIClass;
import com.sportxast.SportXast.localdatabase.Keys.KEY_S3;
import com.sportxast.SportXast.models._Profile;
import com.sportxast.SportXast.thirdparty_class.Async_HttpClient;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
//import com.sportxast.SportXast.BaseSherlockActivity;

//public class ProfileEdit_Activity extends BaseSherlockActivity {

public class ProfileEdit_Activity extends Activity {

	/** Header composition **/
	private HeaderUIClass headerUIClass;
	private RelativeLayout sx_header_wrapper; 
	
	private Global_Data FGlobal_Data;
	private Async_HttpClient async_HttpClient;
	
	private static final int REQUEST_IMAGE_CAPTURE = 1;
	private static final int SELECT_PHOTO = 100;
	 
	private Bitmap imageBitmap;
	
	private String FUsername = "";
	private String FFullname = "";
	private String FPhoto = "";
	private String FEmail = "";
	private String FMotto = "";
	private int avatarCount = 0;
	
	//SimpleDateFormat dateFormat;
	String fileName;// = dateFormat.format(new Date()) + ".jpg";
	private File FAvatarImageFile;
	
	private EditText edittxt_username;
	private EditText edittxt_fullname;
	private EditText edittxt_email;
	private EditText edittxt_motto;
	private ImageView imgvw_photo;
	private Button btn_editphoto;
	 
	private Dialog dialog;
	
	private int screen_w;
	private int screen_h;
	
	boolean isSelectAPhoto = false;
	
	private boolean FProfileHasBeenEdited;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState); 
	    // TODO Auto-generated method stub
	    setContentView(R.layout.layout_profile_edit);
	    FGlobal_Data = (Global_Data)getApplicationContext();
	     
	    this.FProfileHasBeenEdited = false;
	    
	    /*
	    initActionBarObjects();
	    getActionbar_Menu_Item("Edit Profile");
	    showDone(true);
	    setDoneText("Save");
	    */ 
	    
	    async_HttpClient = new Async_HttpClient(this);
    	FUsername 	= CommonFunctions_1.getIntentExtrasString(ProfileEdit_Activity.this, "username");
    	FFullname 	= CommonFunctions_1.getIntentExtrasString(ProfileEdit_Activity.this, "fullname");
    	FPhoto		= CommonFunctions_1.getIntentExtrasString(ProfileEdit_Activity.this, "photo");
    	FEmail 		= CommonFunctions_1.getIntentExtrasString(ProfileEdit_Activity.this, "email");
    	FMotto 		= CommonFunctions_1.getIntentExtrasString(ProfileEdit_Activity.this, "motto");
    	avatarCount = CommonFunctions_1.getIntentExtrasInt(   ProfileEdit_Activity.this, "avatarCount");
	     
	    prepareHeader(); 
	    initializeResources(); 
	    populateView(); 
	     
	    previousDataInString = edittxt_username.getText().toString().trim() + 
	    					   edittxt_fullname.getText().toString().trim() +
	    					   edittxt_email.getText().toString().trim() +
	    					   edittxt_motto.getText().toString().trim();
	}
	 
	/** Exit this page/activity **/
	private void exitProfileEditPage( final boolean profileEdited ){
		
		String tempImagePath = "";
		if(FAvatarImageFile != null){
			tempImagePath = FAvatarImageFile.getAbsolutePath();
		}
			 
		//######################################################
    	Intent returnIntent = new Intent();
    	returnIntent.putExtra("profileEdited", profileEdited);
    	returnIntent.putExtra("tempAvatarPath", tempImagePath); 
    	setResult(RESULT_OK, returnIntent); 
    	//######################################################
    	 
    	finish(); 
	}
	
	private boolean checkIfProfileHasBeenEdited(){
		
		boolean profileHasBeenEdited = false;
		
		String newDataInString = edittxt_username.getText().toString().trim() + 
				   edittxt_fullname.getText().toString().trim() +
				   edittxt_email.getText().toString().trim() +
				   edittxt_motto.getText().toString().trim();
		
		if(newDataInString.equals(previousDataInString) ){
			
		}else{
			profileHasBeenEdited = true;
		}
		
		if(isSelectAPhoto == true)
			profileHasBeenEdited = true;
		
		return profileHasBeenEdited; 
	}
	
	private String previousDataInString;
	
	private void initializeResources(){
		WindowManager w = getWindowManager();
		Display d = w.getDefaultDisplay();
		DisplayMetrics metrics = new DisplayMetrics();
		d.getMetrics(metrics);

		screen_w = metrics.widthPixels;
		screen_h = metrics.heightPixels;
		  
	    edittxt_username = (EditText)findViewById(R.id.edittxt_username);
	    /*
	    edittxt_username.addTextChangedListener(new TextWatcher(){ 
		      
			@Override
			public void afterTextChanged(Editable editable) {
				// TODO Auto-generated method stub 
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				// TODO Auto-generated method stub  
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				
			}
	    }); 
	    */
	     
	    edittxt_fullname= (EditText)findViewById(R.id.edittxt_fullname);
	    edittxt_email 	= (EditText)findViewById(R.id.edittxt_email);
	    edittxt_motto 	= (EditText)findViewById(R.id.edittxt_motto);
	    
	    imgvw_photo 	= (ImageView)findViewById(R.id.imgvw_photo);
	    btn_editphoto 	= (Button)findViewById(R.id.btn_editphoto);
	}

	private void prepareHeader(){ 
		this.sx_header_wrapper = (RelativeLayout)findViewById(R.id.sx_header_wrapper);
		this.headerUIClass = new HeaderUIClass(this, sx_header_wrapper); 
		this.headerUIClass.setMenuTitle("Edit Profile");
	  
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
		this.headerUIClass.setDoneButtonText("Done"); 
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
				if( checkIfProfileHasBeenEdited() )
				   saveData();
				else{
					exitProfileEditPage( false );
					//finish();
					//Toast.makeText(getApplicationContext(), "walay na-edit boss", Toast.LENGTH_LONG).show();
				}
			}
			
			@Override
			public void onCameraClicked() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onBackClicked() {
				// TODO Auto-generated method stub
				exitProfileEditPage( checkIfProfileHasBeenEdited() );
			}
			
			@Override
			public void onAddClicked() {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onAboutClicked() {
				// TODO Auto-generated method stub
				
			}
		});
	}
	 
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		 
		if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
	        Bundle extras = data.getExtras();
	        imageBitmap =scaleDown((Bitmap) extras.get("data"), CommonFunctions_1.scaleSizeToDP(this, 100), CommonFunctions_1.scaleSizeToDP(this, 100), true); 
	        		//Bitmap.createScaledBitmap((Bitmap) extras.get("data"), scaleSizeToDP(this, 100), scaleSizeToDP(this, 100), true);
	        imgvw_photo.setImageBitmap(imageBitmap);
	        FAvatarImageFile = new File(getRealPathFromURI(getImageUri(this, imageBitmap)));
	        dialog.dismiss();
	        
	        isSelectAPhoto = true;
	         
	    }
		else if(requestCode == SELECT_PHOTO && resultCode == RESULT_OK){  
 
	            Uri selectedImage = data.getData();
				try {
				  InputStream	imageStream = getContentResolver().openInputStream(selectedImage);
				   imageBitmap=scaleDown(BitmapFactory.decodeStream(imageStream), CommonFunctions_1.scaleSizeToDP(this, 100), CommonFunctions_1.scaleSizeToDP(this, 100), true);
						   //Bitmap. createScaledBitmap(BitmapFactory.decodeStream(imageStream), scaleSizeToDP(this, 100), scaleSizeToDP(this, 100), true) ;
				   imgvw_photo.setImageBitmap(imageBitmap);
				   
				   FAvatarImageFile = new File(getRealPathFromURI(getImageUri(this, imageBitmap)));
				   
				   dialog.dismiss();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					//e.printStackTrace(); 
					Toast.makeText(ProfileEdit_Activity.this, "The image is too large.", Toast.LENGTH_SHORT).show();
				}
	           
				isSelectAPhoto = true;
	        }
	}
	
	public void populateView(){
		edittxt_username.setText( FUsername );
		edittxt_fullname.setText(FFullname);
		edittxt_email.setText(FEmail);
		edittxt_motto.setText(FMotto);
		
		if(FPhoto.length()>0){
			AQuery aq = new AQuery(this);
			aq.id(imgvw_photo).image(FPhoto, false, true);
			
			btn_editphoto.setOnClickListener( new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					dialog = new Dialog(ProfileEdit_Activity.this);
					dialog.setTitle("Choose picture");
					dialog.setContentView(EditPhotoButtons());
					dialog.show();
				}
			});
		} 
		
	}
	
	public View EditPhotoButtons(){
		LinearLayout layout = new LinearLayout(this);
		
		Button btn_camera 	= new Button(this);
		Button btn_photos 	= new Button(this);
		Button btn_facebook = new Button(this);
		Button btn_twitter	= new Button(this);
		Button btn_cancel 	= new Button(this);
		
		layout.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT));
		layout.setPadding(4, 4, 4, 4);
		layout.setOrientation(LinearLayout.VERTICAL);
		
		btn_camera.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
		btn_photos.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
		btn_facebook.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
		btn_twitter.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
		btn_cancel.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));
//		
		btn_camera.setBackgroundResource(R.drawable.layerlist_whiteshapewithlinebelow);
		btn_photos.setBackgroundResource(R.drawable.layerlist_whiteshapewithlinebelow);
		btn_facebook.setBackgroundResource(R.drawable.layerlist_whiteshapewithlinebelow);
		btn_twitter.setBackgroundResource(R.drawable.layerlist_whiteshapewithlinebelow);
		btn_cancel.setBackgroundResource(R.color.white);
		
		btn_camera.setTextColor(Color.BLUE);
		btn_photos.setTextColor(Color.BLUE);
		btn_facebook.setTextColor(Color.BLUE);
		btn_twitter.setTextColor(Color.BLUE);
		btn_cancel.setTextColor(Color.BLUE);
		 
		btn_camera.setText("From Camera");
		btn_photos.setText("From Photos");
		btn_facebook.setText("Import from Facebook");
		btn_twitter.setText("Import from Twitter");
		btn_cancel.setText("Cancel");
		
		layout.addView(btn_camera);
		layout.addView(btn_photos);
		layout.addView(btn_facebook);
		layout.addView(btn_twitter);
		layout.addView(btn_cancel);
		 
		btn_cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});
		
		
		btn_camera.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				 Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
				    	
				    //	fileName = dateFormat.format(new Date()) + ".jpg";
				    //	File photo = new File(Environment.getExternalStorageDirectory(), fileName);
				    	//takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photo));
				        startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
				    }
			}
		});
		 
		btn_photos.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
				photoPickerIntent.setType("image/*");
				startActivityForResult(photoPickerIntent, SELECT_PHOTO);
			}
		});
		
		return layout;
	}
 
	public static int calculateInSampleSize(
        BitmapFactory.Options options, int reqWidth, int reqHeight) {
	    // Raw height and width of image
	    final int height = options.outHeight;
	    final int width = options.outWidth;
	    int inSampleSize = 1;
	
	    if (height > reqHeight || width > reqWidth) { 
	        final int halfHeight = height / 2;
	        final int halfWidth = width / 2;
	
	        // Calculate the largest inSampleSize value that is a power of 2 and keeps both
	        // height and width larger than the requested height and width.
	        while ((halfHeight / inSampleSize) > reqHeight
	        		&& (halfWidth / inSampleSize) > reqWidth) {
	            inSampleSize *= 2;
	        }
	    }
	
	    return inSampleSize;
	}
	
	
	public static Bitmap decodeSampledBitmapFromResource(InputStream res,
	        int reqWidth, int reqHeight) {

	    // First decode with inJustDecodeBounds=true to check dimensions
	    final BitmapFactory.Options options = new BitmapFactory.Options();
	    options.inJustDecodeBounds = true;
	    BitmapFactory.decodeStream(res, null, options);

	    // Calculate inSampleSize
	    options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

	    // Decode bitmap with inSampleSize set
	    options.inJustDecodeBounds = false;
	    return   BitmapFactory.decodeStream(res, null, options);
	}
	
	
	public void saveData(){
	
		String avatarCount_S  = avatarCount==0?"1":""+(avatarCount+1); 
		RequestParams requestParams = new RequestParams();
		
		requestParams.put("avatarName", "avatar_"+ avatarCount_S +".jpg");
		requestParams.put("info", 		edittxt_motto.getText().toString());
		requestParams.put("name", 		edittxt_fullname.getText().toString());
		requestParams.put("username", 	edittxt_username.getText().toString());
		//requestParams.put("image", new ByteArrayEntity(imageByte));
		
		async_HttpClient.POST("saveUserInfo", requestParams, new JsonHttpResponseHandler(){
			 
			@Override
			public void onSuccess(int statusCode, Header[] headers,
					JSONObject response) {
				// TODO Auto-generated method stub
				super.onSuccess(statusCode, headers, response);
				
				for (int i = 0; i < headers.length; i++) {
					Log.e("header"," "+ headers[i].toString());
				}
				
				Log.e("saveinfo", " "+response.toString());
				 
				parseSaveInfoReturn(response);
					 
				exitProfileEditPage(true);
				//finish();
				
				//onBack(null);
				//Toast.makeText(ProfileEdit_Activity.this,"Success: "+response.toString() , Toast.LENGTH_LONG).show();
				//Toast.makeText(ProfileEdit_Activity.this,"Success: LOLOLOL " , Toast.LENGTH_LONG).show();
			//	finish(); 
				/*
				final Object[] myTaskParams = { response }; 
			 	new parseSaveInfoReturn().execute( myTaskParams ); 
			 	*/
			 	
			}
			
			 
			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2,
					Throwable arg3) {
				// TODO Auto-generated method stub
				super.onFailure(arg0, arg1, arg2, arg3); 
				Toast.makeText(ProfileEdit_Activity.this,"Fail: "+arg3.getMessage() , Toast.LENGTH_LONG).show();
			}
 
			
		});
	}
	
	/*
	private class parseSaveInfoReturn extends AsyncTask<Object, Integer, Void> {		
		 
		//private Points points;
		private JSONObject response;
		@Override
		protected void onPreExecute() {
			super.onPreExecute();  
			//Dialog.ShowProgressSpinner(MainActivityGroup.this, getString(R.string.please_wait), getString(R.string.gather_data));  
		}
		
		@Override
		protected Void doInBackground(Object... params) { 
			
			this.response = (JSONObject) params[0]; 
			 
			try {
				JSONObject jObject_info = this.response.getJSONObject("info");
				_Profile profile = new _Profile();
				
				profile.avatarPath = jObject_info.get("avatarPath").toString();
				profile.aboutMe= jObject_info.get("aboutMe").toString();
				profile.avatarCount= jObject_info.get("avatarCount").toString();
				profile.avatarName= jObject_info.get("avatarName").toString();
				profile.favoriteCount= jObject_info.get("favoriteCount").toString();
				profile.email= jObject_info.get("email").toString();
				profile.avatarUrl= jObject_info.get("avatarUrl").toString();
				profile.userId= jObject_info.get("userId").toString();
				profile.userName= jObject_info.get("userName").toString();
				profile.postCount= jObject_info.get("postCount").toString();
				profile.fullName= jObject_info.get("fullName").toString();
				profile.hasAvatar= jObject_info.get("hasAvatar").toString();
				profile.displayName= jObject_info.get("displayName").toString();
				profile.viewCount= jObject_info.get("viewCount").toString();
				
				Toast.makeText(ProfileEdit_Activity.this,"Success: "+profile.userId , Toast.LENGTH_LONG).show();
				if (isSelectAPhoto) {
					uploadImageS3(profile);
					isSelectAPhoto = false;
				}
				
				
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		return null;
	} 
	 
	@Override
	protected void onPostExecute(Void result) {
		super.onPostExecute(result);
		 
		Toast.makeText(ProfileEdit_Activity.this,"Success: LOLOLOL " , Toast.LENGTH_LONG).show();
	    
		}
	}
	*/
	
	
	
	public void parseSaveInfoReturn(JSONObject response){
		try {
			JSONObject jObject_info = response.getJSONObject("info");
			_Profile profile = new _Profile();
			
			profile.avatarPath = jObject_info.get("avatarPath").toString();
			profile.aboutMe= jObject_info.get("aboutMe").toString();
			profile.avatarCount= jObject_info.get("avatarCount").toString();
			profile.avatarName= jObject_info.get("avatarName").toString();
			profile.favoriteCount= jObject_info.get("favoriteCount").toString();
			profile.email= jObject_info.get("email").toString();
			profile.avatarUrl= jObject_info.get("avatarUrl").toString();
			profile.userId= jObject_info.get("userId").toString();
			profile.userName= jObject_info.get("userName").toString();
			profile.postCount= jObject_info.get("postCount").toString();
			profile.fullName= jObject_info.get("fullName").toString();
			profile.hasAvatar= jObject_info.get("hasAvatar").toString();
			profile.displayName= jObject_info.get("displayName").toString();
			profile.viewCount= jObject_info.get("viewCount").toString();
			
			Toast.makeText(ProfileEdit_Activity.this,"Success: "+profile.userId , Toast.LENGTH_LONG).show();
			if (isSelectAPhoto) {
				uploadImageS3(profile);
				isSelectAPhoto = false;
			}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
		
		
	}
	
	public void uploadImageS3(final _Profile profile){
		
		
		Log.e("uploadImageS3", "bucket : "+FGlobal_Data.getAppSetting_settings("S3_IMAGE_BUCKET") + "key:"+profile.avatarPath+"/"+profile.avatarName );
		new AsyncTask<Object, Object, Object>() {

			@Override
			protected Object doInBackground(Object... params) {
				// TODO Auto-generated method stub
			
				AmazonS3Client s3Client = new AmazonS3Client( new BasicAWSCredentials( KEY_S3.ACCESS_KEY, KEY_S3.SECRET_KEY ) );
				//s3Client.createBucket(global_Data.getAppSetting().appSetting.S3_IMAGE_BUCKET);
				
				
			   PutObjectRequest por = new PutObjectRequest(FGlobal_Data.getAppSetting_settings("S3_IMAGE_BUCKET") , 
				 		profile.avatarPath+"/"+profile.avatarName, FAvatarImageFile);
			   s3Client.putObject( por );
				
				return null;
			}
			
		}.execute();
		
	}
	
	private String getRealPathFromURI(Uri contentURI) {
		    String result;
		    Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
		    if (cursor == null) { // Source is Dropbox or other similar local file path
		        result = contentURI.getPath();
		    } else { 
		        cursor.moveToFirst(); 
		        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA); 
		        result = cursor.getString(idx);
		        cursor.close();
		    }
		    return result;
		}
		
	
public Uri getImageUri(Context inContext, Bitmap inImage) {
			  ByteArrayOutputStream bytes = new ByteArrayOutputStream();
			  inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
			  String path = Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
			  return Uri.parse(path);
			}
	
public Bitmap scaleDown(Bitmap realImage, int width,int height,boolean filter) {
			   
		        DisplayMetrics displaymetrics = new DisplayMetrics();
		        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
		        float maxImageSize = width;
		        
		        float ratio = Math.min((float) maxImageSize / realImage.getWidth(),
		                (float) maxImageSize / realImage.getHeight());
		 
		        int w = Math.round((float) ratio * realImage.getWidth());
		        int h = Math.round((float) ratio * realImage.getHeight());
		 
		        Matrix matrix = new Matrix();
		       // matrix.postRotate(90);
		        
		        
		        Bitmap newBitmap = Bitmap.createScaledBitmap(realImage, w, h,  true);
		        return Bitmap.createBitmap(newBitmap, 0, 0, newBitmap.getWidth(), newBitmap.getHeight(), matrix, true);
		    }
}
