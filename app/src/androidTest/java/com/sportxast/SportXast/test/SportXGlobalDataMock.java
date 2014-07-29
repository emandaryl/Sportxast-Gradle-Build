package com.sportxast.SportXast.test;

import android.content.SharedPreferences;
import android.test.ApplicationTestCase;

import com.sportxast.SportXast.Global_Data;
import com.sportxast.SportXast.Global_Data.Coordinate;
import com.sportxast.SportXast.thirdparty_class.GPSTracker;

public class SportXGlobalDataMock extends ApplicationTestCase<Global_Data> {

	private Global_Data globalData;
	private GPSTracker gpsTracker;
	private static final String PREFIX = "sportx.test";
	
	public SportXGlobalDataMock() {
		this("SportXGlobalDataMock");
	}
	
	public SportXGlobalDataMock(String name) {
		super(Global_Data.class);
		setName(name);
	}
	
	@Override
	public void setUp() throws Exception {
		super.setUp();
		
		// Comment this to prevent AssertionFailedError
		// createApplication will provide a functional Context for us.
//		SportXMockContext mockContext = new SportXMockContext(getContext());
//		setContext(mockContext);
		
		createApplication();
		
		globalData = getApplication();
		
		gpsTracker = new GPSTracker(globalData);
	}
	
	public void testPreConditions() {
		assertNotNull(globalData);
	}
	
	public void testValues() {
		// Coordinates
		Coordinate coord = whatMyCoordinate();
		if(coord != null) {
			globalData.setCoordinate(coord);
			// Latitude values
			assertEquals(coord.latitude, globalData.getCoordinate().latitude);
			assertEquals(coord.longitude, globalData.getCoordinate().longitude);
		}
		
		// List comments
		
		
	}
	
	@Override
	public void tearDown() throws Exception {
		super.tearDown();
	}
	
	public SharedPreferences getSharedPreferences(String name, int mode) {
		return getContext().getSharedPreferences(PREFIX + name, mode);
	}
	
	private Coordinate whatMyCoordinate() {
		if(gpsTracker.canGetLocation()) {
			Coordinate coord = new Coordinate();
			coord.latitude = gpsTracker.getLatitude();
			coord.longitude = gpsTracker.getLongitude();
			
			return coord;
		} else {
			return null;
		}
	}
}
