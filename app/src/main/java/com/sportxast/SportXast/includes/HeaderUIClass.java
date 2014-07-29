package com.sportxast.SportXast.includes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sportxast.SportXast.R;
 
public class HeaderUIClass {
	private Context context;

	private RelativeLayout rlHeaderLayout;
	private LayoutInflater liHeader;
	private RelativeLayout llHeaderContent;
 
	private RelativeLayout  layout_menubar;
	private RelativeLayout layout_left;

	private Button imgbtn_back; 

	private ImageButton imgbtn_add;
	private ImageButton imgbtn_menu; 

	private RelativeLayout layout_right;  
	private ProgressBar progress_refresh;  
	private ImageButton imgbtn_refresh; 
	private ImageButton imgbtn_about;
	private ImageButton imgbtn_search; 

	private TextView btn_Done; 

	private ImageButton imgbtn_camera; 
 
	private TextView txtvw_menutitle0; 

	private TextView txtvw_menutitle; 
	  
	
	public HeaderUIClass(Context context, RelativeLayout rlRootLayout) {
		this.context = context;
		this.rlHeaderLayout = rlRootLayout;

		prepareHeader();
	} 
	
	private void prepareHeader() {
  
		this.liHeader = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.llHeaderContent = (RelativeLayout) liHeader.inflate( R.layout.layout_actionbar, null);
		this.rlHeaderLayout.addView(llHeaderContent,  new RelativeLayout.LayoutParams( LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
 
		this.layout_menubar = (RelativeLayout) 	rlHeaderLayout.findViewById(R.id.layout_menubar);
		this.layout_left 	= (RelativeLayout) 	rlHeaderLayout.findViewById(R.id.layout_left); 
		this.imgbtn_back 	= (Button) 			rlHeaderLayout.findViewById(R.id.imgbtn_back); 
		this.imgbtn_add 	= (ImageButton) 	rlHeaderLayout.findViewById(R.id.imgbtn_add);
		this.imgbtn_menu 	= (ImageButton) 	rlHeaderLayout.findViewById(R.id.imgbtn_menu); 
		this.layout_right 	= (RelativeLayout) 	rlHeaderLayout.findViewById(R.id.layout_right); 
		this.progress_refresh=(ProgressBar) 	rlHeaderLayout.findViewById(R.id.progress_refresh); 
		this.imgbtn_refresh = (ImageButton) 	rlHeaderLayout.findViewById(R.id.imgbtn_refresh);
		this.imgbtn_about 	= (ImageButton) 	rlHeaderLayout.findViewById(R.id.imgbtn_about);
		this.imgbtn_search 	= (ImageButton) 	rlHeaderLayout.findViewById(R.id.imgbtn_search); 
		this.btn_Done 		= (TextView) 		rlHeaderLayout.findViewById(R.id.btn_Done); 
		this.imgbtn_camera 	= (ImageButton) 	rlHeaderLayout.findViewById(R.id.imgbtn_camera); 
		this.txtvw_menutitle0=(TextView) 		rlHeaderLayout.findViewById(R.id.txtvw_menutitle0); 
		this.txtvw_menutitle =(TextView) 		rlHeaderLayout.findViewById(R.id.txtvw_menutitle);
		 
		this.imgbtn_back.setOnClickListener( 	onBackButtonClicked );
		this.imgbtn_add.setOnClickListener(  	onAddButtonClicked );
		this.imgbtn_menu.setOnClickListener(  	onMenuButtonClicked );
		this.imgbtn_refresh.setOnClickListener(	onRefreshButtonClicked );
		this.imgbtn_about.setOnClickListener( 	onAboutButtonClicked );
		this.imgbtn_search.setOnClickListener(	onSearchButtonClicked );
		this.btn_Done.setOnClickListener(		onDoneButtonClicked );
		this.imgbtn_camera.setOnClickListener(	onCameraButtonClicked ); 
	}

	public void setMenuTitle(String menuTitle){
		this.txtvw_menutitle.setText(menuTitle);
	}
//########################################################################  
	public void setMenuTitle0(String menuTitle0){
		this.txtvw_menutitle0.setText(menuTitle0);
	}
//########################################################################
//########################################################################	 
	public void showBackButton(final boolean isShow) { 
		this.imgbtn_back.setVisibility(isShow ? View.VISIBLE : View.GONE); 
	}
//########################################################################  
	public void showAddButton(final boolean isShow) { 
		this.imgbtn_add.setVisibility(isShow ? View.VISIBLE : View.GONE); 
	}
//######################################################################## 
	public void showMenuButton(final boolean isShow) {
		this.imgbtn_menu.setVisibility(isShow ? View.VISIBLE : View.GONE); 
	}	
//########################################################################  
	public void showRefreshButton(final boolean isShow) {
		this.imgbtn_refresh.setVisibility(isShow ? View.VISIBLE : View.GONE); 
	}	 
//######################################################################## 
	public void showAboutButton(final boolean isShow) {
		this.imgbtn_about.setVisibility(isShow ? View.VISIBLE : View.GONE); 
	}	 	
//######################################################################## 
	public void showSearchButton(final boolean isShow) {
		this.imgbtn_search.setVisibility(isShow ? View.VISIBLE : View.GONE); 
	}	
//######################################################################## 
	public void showDoneButton(final boolean isShow) {
		this.btn_Done.setVisibility(isShow ? View.VISIBLE : View.GONE);
	}	
//########################################################################  
	public void showCameraButton(final boolean isShow) {
		this.imgbtn_camera.setVisibility(isShow ? View.VISIBLE : View.GONE); 
	}	
//########################################################################  
	public void showMenuTitle(final boolean isShow) {
		this.txtvw_menutitle.setVisibility(isShow ? View.VISIBLE : View.GONE); 
	}
	
//########################################################################  
	public void showMenuTitle0(final boolean isShow) {
		this.txtvw_menutitle0.setVisibility(isShow ? View.VISIBLE : View.GONE); 
	}
	
//########################################################################
//########################################################################	
	public void setDoneButtonText(String doneButtonText){
		this.btn_Done.setText(doneButtonText);
	} 
//########################################################################	
	public void enableDoneButton(boolean enableButton) {
		this.btn_Done.setEnabled(enableButton); 
	}	 
//########################################################################
//########################################################################
	public OnClickListener onBackButtonClicked = new OnClickListener() {
		@Override
		public void onClick(View v) {
			headerButtonClicked.onBackClicked();
		}
	};
	public OnClickListener onAddButtonClicked = new OnClickListener() {
		@Override
		public void onClick(View v) {
			headerButtonClicked.onAddClicked();
		}
	};

	public OnClickListener onMenuButtonClicked = new OnClickListener() {
		@Override
		public void onClick(View v) {
			headerButtonClicked.onMenuClicked();
		}
	};

	public OnClickListener onRefreshButtonClicked= new OnClickListener() {
		@Override
		public void onClick(View v) {
			headerButtonClicked.onRefreshClicked();
		}
	};
	
	
	public OnClickListener onAboutButtonClicked = new OnClickListener() {
		@Override
		public void onClick(View v) {
			headerButtonClicked.onAboutClicked();
		}
	};

	public OnClickListener onSearchButtonClicked = new OnClickListener() {
		@Override
		public void onClick(View v) {
			headerButtonClicked.onSearchClicked();
		}
	};
	public OnClickListener onDoneButtonClicked = new OnClickListener() {
		@Override
		public void onClick(View v) {
			headerButtonClicked.onDoneClicked();
		}
	};
	
	public OnClickListener onCameraButtonClicked = new OnClickListener() {
		@Override
		public void onClick(View v) {
			headerButtonClicked.onCameraClicked();
		}
	};
	public interface OnHeaderButtonClickedListener {
		 
		public void onBackClicked();
		public void onAddClicked();
		public void onMenuClicked();
		public void onRefreshClicked();
		public void onAboutClicked();
		public void onSearchClicked();
		public void onDoneClicked();
		public void onCameraClicked();
		 
		/*
		this.layout_menubar = (RelativeLayout) 	rlHeaderLayout.findViewById(R.id.layout_menubar);
		this.layout_left 	= (RelativeLayout) 	rlHeaderLayout.findViewById(R.id.layout_left); 
		onButtonBackClicked() 	= (Button) 			rlHeaderLayout.findViewById(R.id.imgbtn_back); 
		onButtonAddClicked() 	= (ImageButton) 	rlHeaderLayout.findViewById(R.id.imgbtn_add);
		onButtonMenuClicked() 	= (ImageButton) 	rlHeaderLayout.findViewById(R.id.imgbtn_menu); 
		this.layout_right 	= (RelativeLayout) 	rlHeaderLayout.findViewById(R.id.layout_right); 
		this.progress_refresh=(ProgressBar) 	rlHeaderLayout.findViewById(R.id.progress_refresh); 
		onButtonRefreshClicked() = (ImageButton) 	rlHeaderLayout.findViewById(R.id.imgbtn_refresh);
		onButtonAboutClicked() 	= (ImageButton) 	rlHeaderLayout.findViewById(R.id.imgbtn_about);
		onButtonSearchClicked() 	= (ImageButton) 	rlHeaderLayout.findViewById(R.id.imgbtn_search); 
		onButtonDoneClicked() 		= (TextView) 		rlHeaderLayout.findViewById(R.id.btn_Done); 
		onButtonCameraClicked() 	= (ImageButton) 	rlHeaderLayout.findViewById(R.id.imgbtn_camera); 
		this.txtvw_menutitle0=(TextView) 		rlHeaderLayout.findViewById(R.id.txtvw_menutitle0); 
		this.txtvw_menutitle =(TextView) 		rlHeaderLayout.findViewById(R.id.txtvw_menutitle);
		
		*/ 
	}

	private OnHeaderButtonClickedListener headerButtonClicked;

	public void setOnHeaderButtonClickedListener(
			OnHeaderButtonClickedListener headerButtonClicked) {
		this.headerButtonClicked = headerButtonClicked;
	}
}
