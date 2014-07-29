package com.sportxast.SportXast.activities2_0;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.sportxast.SportXast.R;
import com.sportxast.SportXast.commons.Constants;
import com.sportxast.SportXast.fragments.FansTagsFragment;

public class FansTagsActivity extends FragmentActivity {

	private FragmentManager fragmentManager;
	public static final int LIST_TAGS = 1;
	public static final int LIST_FANS = 2;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fans_tags_activity);
		
		Bundle bundle = new Bundle();
		bundle.putInt(Constants.EXTRA_EVENT_TYPE, getIntent().getExtras().getInt(Constants.EXTRA_EVENT_TYPE));
		
		fragmentManager = getSupportFragmentManager();
		Fragment fragment = fragmentManager.findFragmentById(R.id.containerEventInfosList);
		if(fragment == null) {
			fragment = new FansTagsFragment();
			fragment.setArguments(bundle);
			fragmentManager.beginTransaction().add(R.id.containerEventInfosList, fragment).commit();
		}
	}
}
