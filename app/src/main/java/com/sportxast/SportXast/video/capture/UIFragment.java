package com.sportxast.SportXast.video.capture;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sportxast.SportXast.R;

public class UIFragment extends Fragment {
	@Override
	  public View onCreateView(LayoutInflater inflater, ViewGroup container,
	      Bundle savedInstanceState) {
	    View view = inflater.inflate(R.layout.video_capture_preview_fragment,
	        container, false);
	    
	    return view;
	  }
	@Override
	  public void onAttach(Activity activity) {
	    super.onAttach(activity);
//	    if (activity instanceof OnItemSelectedListener) {
//	      listener = (OnItemSelectedListener) activity;
//	    } else {
//	      throw new ClassCastException(activity.toString()
//	          + " must implemenet MyListFragment.OnItemSelectedListener");
//	    }
	  }

	  @Override
	  public void onDetach() {
	    super.onDetach();
//	    listener = null;
	  }

}
