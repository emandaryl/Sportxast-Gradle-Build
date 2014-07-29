package com.sportxast.SportXast.test;

import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.robotium.solo.Solo;
import com.sportxast.SportXast.activities2_0.ProfileEdit_Activity;
import com.sportxast.SportXast.test.constants.Values;
import com.sportxast.SportXast.thirdparty_class.Async_HttpClient;

public class ProfileEditTest extends ActivityInstrumentationTestCase2<ProfileEdit_Activity> {

	private ProfileEdit_Activity profEditActivity;
	
	private String username = null;
	private String fullname = null;
	private String photo = null;
	private String email = null;
	private String motto = null;
	private int avatarCount = 0;
	
	private EditText edtUsername;
	private EditText edtFullname;
	private EditText edtEmail;
	private EditText edtMotto;
	private ImageView imgvwPhoto;
	private Button btnEditPhoto;
	
	private Async_HttpClient httpClient;
	private Solo solo;
	
	public ProfileEditTest() {
		super(ProfileEdit_Activity.class);
	}
	
	@Override
	public void setUp() throws Exception {
		super.setUp();
		
		setActivityInitialTouchMode(false);
		
		Intent intent = new Intent();
		intent.putExtra(Values.TAG_USERNAME, "law");
		intent.putExtra(Values.TAG_FULLNAME, "lawrence gimenez");
		intent.putExtra(Values.TAG_PHOTO, "droid");
		intent.putExtra(Values.TAG_EMAIL, "law@law.com");
		intent.putExtra(Values.TAG_MOTTO, "test motto");
		intent.putExtra(Values.TAG_AVATAR_COUNT, 1);
		setActivityIntent(intent);
		
		profEditActivity = getActivity();
		solo = new Solo(getInstrumentation(), profEditActivity);
		httpClient = new Async_HttpClient(profEditActivity);
		
		edtUsername = (EditText) profEditActivity.findViewById(com.sportxast.SportXast.R.id.edittxt_username);
		edtFullname = (EditText) profEditActivity.findViewById(com.sportxast.SportXast.R.id.edittxt_fullname);
		edtEmail = (EditText) profEditActivity.findViewById(com.sportxast.SportXast.R.id.edittxt_email);
		edtMotto = (EditText) profEditActivity.findViewById(com.sportxast.SportXast.R.id.edittxt_motto);
		
		imgvwPhoto = (ImageView) profEditActivity.findViewById(com.sportxast.SportXast.R.id.imgvw_photo);
		btnEditPhoto = (Button) profEditActivity.findViewById(com.sportxast.SportXast.R.id.btn_editphoto);
		
		username = profEditActivity.getIntent().getStringExtra(Values.TAG_USERNAME);
		fullname = profEditActivity.getIntent().getStringExtra(Values.TAG_FULLNAME);
		photo = profEditActivity.getIntent().getStringExtra(Values.TAG_PHOTO);
		email = profEditActivity.getIntent().getStringExtra(Values.TAG_EMAIL);
		motto = profEditActivity.getIntent().getStringExtra(Values.TAG_MOTTO);
		avatarCount = profEditActivity.getIntent().getIntExtra(Values.TAG_AVATAR_COUNT, -1);
	}
	
	public void testPreconditions() {
		solo.assertCurrentActivity("Profile Edit I should be here", ProfileEdit_Activity.class);
		solo.assertMemoryNotLow();
		
		assertNotNull(profEditActivity);
		assertNotNull(httpClient);
		
		assertNotNull(edtUsername);
		assertNotNull(edtFullname);
		assertNotNull(edtEmail);
		assertNotNull(edtMotto);
		
		assertNotNull(imgvwPhoto);
		assertNotNull(btnEditPhoto);
		
//		assertEquals("Edit Profile", profEditActivity.getActionBarTitle());
	}
	
	public void testPopulateView() {
//		int lengthUsername = edtUsername.getText().toString().trim().length();
		assertTrue(edtUsername.getText().toString().trim().length() > 0);
		assertTrue(edtFullname.getText().toString().trim().length() > 0);
//		assertTrue()
	}
	
	public void testIntents() {
		assertEquals("law", username);
		assertEquals("lawrence gimenez", fullname);
		assertEquals("droid", photo);
		assertEquals("law@law.com", email);
		assertEquals("test motto", motto);
		assertEquals(1, avatarCount);
	}
}
