package com.sportxast.SportXast.test;

import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.robotium.solo.Solo;
import com.sportxast.SportXast.activities2_0.VideoCaptureActivity;
import com.sportxast.SportXast.activities2_0.VideoPreviewActivity;
import com.sportxast.SportXast.video.capture.CustomVideoView;
import com.sportxast.SportXast.video.capture.TrimVideoUtils;

import java.io.File;

public class VideoCaptureTest extends ActivityInstrumentationTestCase2<VideoCaptureActivity> {

	private VideoCaptureActivity videoCaptureActivity;
	private FrameLayout frameCamPrevFrame;
	private RelativeLayout relUiContainer;
	private RelativeLayout relHeader;
	private TableLayout relFooter;
	private ImageView imgvwSnapshot;
	
	private ImageButton imgbtnCapture;
	private ImageButton imgbtnAddFav;
	private ImageButton imgbtnAddComment;
	private ImageButton imgbtnAddTag;
	private ImageButton imgbtnShare;
	
	private ImageButton imgbtnDelete;
	private TextView txtWaitToText;
	
	private ImageView imgvwMenu;
	private ImageView imgvwPreviewPlay;
	private CustomVideoView videoCustom;
	
	private RelativeLayout relComment;
	private TextView txtEventTitle;
	private ImageView imgvwNotif;
	
	private TextView txtToolTip;
	
	private Solo solo;
	
	public VideoCaptureTest() {
		super(VideoCaptureActivity.class);
	}
	
	@Override
	public void setUp() throws Exception {
		super.setUp();
		
		videoCaptureActivity = getActivity();
		solo = new Solo(getInstrumentation(), videoCaptureActivity);
		
		frameCamPrevFrame = (FrameLayout) videoCaptureActivity.findViewById(com.sportxast.SportXast.R.id.camera_preview);
		/*
		 * UI container is the container for the 
		 * comments, favorite, etc.
		 */
		relUiContainer = (RelativeLayout) videoCaptureActivity.findViewById(com.sportxast.SportXast.R.id.uicontainer);
		relHeader = (RelativeLayout) videoCaptureActivity.findViewById(com.sportxast.SportXast.R.id.header);
		relFooter = (TableLayout) videoCaptureActivity.findViewById(com.sportxast.SportXast.R.id.footer);
		imgvwSnapshot = (ImageView) videoCaptureActivity.findViewById(com.sportxast.SportXast.R.id.snapshot);
		
		imgbtnCapture = (ImageButton) videoCaptureActivity.findViewById(com.sportxast.SportXast.R.id.captureButton);
		imgbtnAddFav = (ImageButton) videoCaptureActivity.findViewById(com.sportxast.SportXast.R.id.btn_add_fav);
		imgbtnAddComment = (ImageButton) videoCaptureActivity.findViewById(com.sportxast.SportXast.R.id.btn_add_comment);
		imgbtnAddTag = (ImageButton) videoCaptureActivity.findViewById(com.sportxast.SportXast.R.id.btn_add_tag);
		imgbtnShare = (ImageButton) videoCaptureActivity.findViewById(com.sportxast.SportXast.R.id.btn_share);
		
		imgbtnDelete = (ImageButton) videoCaptureActivity.findViewById(com.sportxast.SportXast.R.id.btn_delete);
		txtWaitToText = (TextView) videoCaptureActivity.findViewById(com.sportxast.SportXast.R.id.waitText);
		
		imgvwMenu = (ImageView) videoCaptureActivity.findViewById(com.sportxast.SportXast.R.id.menuBtn);
		imgvwPreviewPlay = (ImageView) videoCaptureActivity.findViewById(com.sportxast.SportXast.R.id.previewPlay);
		videoCustom = (CustomVideoView) videoCaptureActivity.findViewById(com.sportxast.SportXast.R.id.smallPreview);
		
		relComment = (RelativeLayout) videoCaptureActivity.findViewById(com.sportxast.SportXast.R.id.comment_field_layout);
		txtEventTitle = (TextView) videoCaptureActivity.findViewById(com.sportxast.SportXast.R.id.event_title);
		// imageView2 is the notification bell at the right! Fuck lazy naming!
		imgvwNotif = (ImageView) videoCaptureActivity.findViewById(com.sportxast.SportXast.R.id.imageView2);
		
		txtToolTip = (TextView) videoCaptureActivity.findViewById(android.R.id.text1); // bogus, because there is no tooltip yet.
	}
	
	public void testPreconditions() {
		solo.assertCurrentActivity("I should be inside VideoCaptureActivity", VideoCaptureActivity.class);
		solo.assertMemoryNotLow();
		
		assertNotNull(videoCaptureActivity);
		assertNotNull(frameCamPrevFrame);
		assertNotNull(relUiContainer);
		
		assertNotNull(relFooter);
		assertNotNull(imgvwSnapshot);
		
		assertNotNull(imgbtnCapture);
		assertNotNull(imgbtnAddFav);
		assertNotNull(imgbtnAddComment);
		assertNotNull(imgbtnAddTag);
		assertNotNull(imgbtnShare);
		
		assertNotNull(imgbtnDelete);
		assertNotNull(txtWaitToText);
		
		assertNotNull(imgvwPreviewPlay);
		assertNotNull(videoCustom);
		assertNotNull(relComment);
		
		getInstrumentation().waitForIdleSync();
		
		/*
		 * In portrait, capture button is still gone.
		 * In landscape, this button will be visible. Maybe.
		 */
		assertEquals(View.GONE, imgbtnCapture.getVisibility());
	}
	
	public void testActionBar() {
		assertNotNull(relHeader);
		assertNotNull(imgvwMenu);
		assertNotNull(txtEventTitle);
		assertNotNull(imgvwNotif);
		
		assertEquals("Tutorial @ SportXast", txtEventTitle.getText().toString().trim());
	}
	
	public void testToolTip() {
		assertNotNull(txtToolTip);
		assertEquals(View.VISIBLE, txtToolTip.getVisibility());
	}
	
	public void testOrientation() throws Throwable {
		solo.setActivityOrientation(Solo.LANDSCAPE);
		getInstrumentation().waitForIdleSync();
		assertEquals(View.VISIBLE, imgvwMenu.getVisibility());
		assertEquals(View.VISIBLE, txtEventTitle.getVisibility());
		assertEquals(View.VISIBLE, imgvwNotif.getVisibility());
		assertEquals(View.VISIBLE, imgbtnCapture.getVisibility());
		
		/*
		 * When in orientation landscape, tap on
		 * the capture button.
		 */
		//getInstrumentation().waitForIdleSync();
		// Just wanna make sure.
		
		// sleep for 8 seconds
		//solo.sleep(8000);
		Thread.sleep(8000);
		
		solo.clickOnView(imgbtnCapture);
		
		solo.waitForView(imgbtnCapture);
		getInstrumentation().waitForIdleSync();
		
		File file = new File(TrimVideoUtils.getLocalTmpPath(getActivity()), "tmp_rec_tmp.mp4");
		assertTrue(file.exists());
		assertTrue(file.length() != 0);
		
		/*
		 * Check if a snapshot exists.
		 */
		solo.waitForView(imgvwSnapshot);
		assertNotNull(imgvwSnapshot.getDrawable());
		Thread.sleep(5000);
		solo.clickOnView(imgvwSnapshot);
		Thread.sleep(5000);
		solo.assertCurrentActivity("Should be in VideoPreviewActivity", VideoPreviewActivity.class);
//		solo.assertCurrentActivity("Should be in VideoPreviewActivity", Profile_Activity.class);
//		solo.goBack();
	}
	
	@Override
	public void tearDown() throws Exception {
		solo.finishOpenedActivities();
		super.tearDown();
	}
}
