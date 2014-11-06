package fragments;


import external.MyBT;
import external.MyWiFi;
import pa.myglove.MainActivity;
import pa.myglove.R;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public  class F_Main extends Fragment {

	static View rootView;
	
	static ImageButton button_menu_gesture,button_menu_setup,button_menu_maininfo,b_bt,b_wifi;

	static MyBT mybt;
	static MyWiFi mywifi;
	
	public static LinearLayout 	placeholder_fragment_top,placeholder_fragment_bot;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		rootView = inflater.inflate(R.layout.fragment_main, container,false);
		mybt = MainActivity.mybt;
		mywifi = MainActivity.mywifi;
		
		this.X_findID();
		this.X_onCLick();
		
		MainActivity.fragmentManager.beginTransaction().replace(R.id.placeholder_fragment_top, new F_Start()).commit();
		MainActivity.fragmentManager.beginTransaction().replace(R.id.placeholder_fragment_bot, new F_Info()).commit();
		
		
		refreshView();
		
		
		return rootView;
	}


	private void X_onCLick() {
		// TODO Auto-generated method stub
		b_bt.setOnClickListener(new ImageButton.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				boolean s = mybt.setState(!mybt.CheckBlueToothState());
				
				if(s)
					b_bt.setBackgroundResource(R.drawable.custom_btn_genoa);
				else
					b_bt.setBackgroundResource(R.drawable.custom_btn_orange);
				
			}
		});
		b_wifi.setOnClickListener(new ImageButton.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				boolean s = !mywifi.wifiManager.isWifiEnabled();
				mywifi.wifiManager.setWifiEnabled(s);
				
				if(s)
					b_wifi.setBackgroundResource(R.drawable.custom_btn_genoa);
				else
					b_wifi.setBackgroundResource(R.drawable.custom_btn_orange);
			}
		});
	
		
		button_menu_setup.setOnClickListener(new ImageButton.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				MainActivity.fragmentManager.beginTransaction().replace(R.id.placeholder_fragment_top, new F_Settings()).commit();
				
			}
		});
		
		button_menu_gesture.setOnClickListener(new ImageButton.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				MainActivity.fragmentManager.beginTransaction().replace(R.id.placeholder_fragment_top, new F_Gestures()).commit();
				
			}
		});
		button_menu_maininfo.setOnClickListener(new ImageButton.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				
				MainActivity.fragmentManager.beginTransaction().replace(R.id.placeholder_fragment_top, new F_Start()).commit();
				
			}
		});
		
	}

	
	
	

	public static void refreshView() {
		// TODO Auto-generated method stub

		if( mybt.CheckBlueToothState())
			b_bt.setBackgroundResource(R.drawable.custom_btn_genoa);
		else
			b_bt.setBackgroundResource(R.drawable.custom_btn_orange);
		
		if( mywifi.wifiManager.isWifiEnabled())
			b_wifi.setBackgroundResource(R.drawable.custom_btn_genoa);
		else
			b_wifi.setBackgroundResource(R.drawable.custom_btn_orange);
	}


	private void X_findID() {
		// TODO Auto-generated method stub
		button_menu_gesture= (ImageButton) rootView.findViewById(R.id.imageButton_main_Gestures);
		button_menu_setup= (ImageButton) rootView.findViewById(R.id.imageButton_main_setup);
		button_menu_maininfo= (ImageButton) rootView.findViewById(R.id.imageButton_main_maininfo);
		b_bt = (ImageButton) rootView.findViewById(R.id.imageButton_bt_state);
		b_wifi = (ImageButton) rootView.findViewById(R.id.imageButton_wifi_state);
		placeholder_fragment_top = (LinearLayout) rootView.findViewById(R.id.placeholder_fragment_top);
		placeholder_fragment_bot = (LinearLayout) rootView.findViewById(R.id.placeholder_fragment_bot);
	}
}
