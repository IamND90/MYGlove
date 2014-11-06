package fragments;

import external.GoPro;
import external.MyBT;
import pa.myglove.C;
import pa.myglove.MainActivity;
import pa.myglove.R;
import android.app.AlertDialog;
import android.app.Fragment;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.wifi.WifiInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

public  class F_Settings extends Fragment {

	View rootView;
	Button b1,b2,b3,bsave;
	ImageButton b_bt,b_wifi;
	static TextView t_device,t_gpName,t_gpPw, t_info;
	
	static SharedPreferences SP ;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		rootView = inflater.inflate(R.layout.fragment_setup, container,false);
		SP = getActivity().getSharedPreferences(C.SP_SETTINGS, Context.MODE_PRIVATE);
		
		X_findID();
		X_onCLick();
		
		loadSettings();
		
		return rootView;
	}

	public static void addInfoText(String string) {
		// TODO Auto-generated method stub
		if( t_info != null)
		t_info.setText(string);
	}

	private void X_onCLick() {
		// TODO Auto-generated method stub
		
		b3.setOnClickListener(new Button.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			    // Get the layout inflater
			    LayoutInflater inflater = getActivity().getLayoutInflater();

			    // Inflate and set the layout for the dialog
			    // Pass null as the parent view because its going in the dialog layout
			    builder.setTitle("Enter your GoPro password");
			    final View rootV = inflater.inflate(R.layout.dialog_enter_text, null);
			    final EditText T = (EditText) rootV.findViewById(R.id.editText_password);
			    T.setText(MainActivity.GETACTIVITY.getSharedPreferences(C.SP_SETTINGS, Context.MODE_PRIVATE)
			    		.getString("password", ""));
			    builder.setView(rootV)
			    // Add action buttons
			           .setPositiveButton("ok", new DialogInterface.OnClickListener() {
			               @Override
			               public void onClick(DialogInterface dialog, int id) {
			                   
			                   String S = T.getText().toString();
			                   
			                  
			                   SP
			                   .edit().putString("password", S).commit();
			                   
			                   loadSettings();
			                   
			               }
			           })
			           .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
			               public void onClick(DialogInterface dialog, int id) {
			                  
			               }
			           });      
			    builder.create().show();
			}
			
		});
		
		
		b1.setOnClickListener(new Button.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				MainActivity.mybt.bluetoothAdapter.startDiscovery();
				addInfoText("Bluetooth is discovering");
			}
			
		});
		
		b2.setOnClickListener(new Button.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				MainActivity.mywifi.startScan();
			}
			
		});
		bsave.setOnClickListener(new Button.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				saveSettings();
			}
			
		});
		
	}

	
	private void saveSettings(){
		
		
		SP.edit()
		.putString("devname", t_device.getText().toString())
		.putString("gpname", t_gpName.getText().toString())
		.commit();
		
	}
	
	private void loadSettings(){
		
		t_device.setText(SP.getString("devname", ""));
		t_gpName.setText(SP.getString("gpname", ""));
		
		String pw = SP.getString("password", "");
		t_gpPw.setText(pw.equals("") ? "NO" : "YES");
		
		
	}

	private void X_findID() {
		// TODO Auto-generated method stub
		b1= (Button) rootView.findViewById(R.id.button_scan_dev);
		b2= (Button) rootView.findViewById(R.id.button_gp_scan);
		b3= (Button) rootView.findViewById(R.id.button_gp_pw);
		bsave= (Button) rootView.findViewById(R.id.button_saveSettings);
		
		t_device = (TextView) rootView.findViewById(R.id.textView_dev_address);
		t_gpName = (TextView) rootView.findViewById(R.id.textView_gp_name);
		t_gpPw = (TextView) rootView.findViewById(R.id.textView_gp_pw);
	
		t_info = (TextView) rootView.findViewById(R.id.textView_settings);
	}


	public static void updateFoundDevice(BluetoothDevice device) {
		// TODO Auto-generated method stub
		String name, ad, saved;
		
		name= device.getName();
		ad = device.getAddress();
		saved = SP.getString("devname", "");
		if(!saved.equals("")){
			t_device.setText(ad);
			if(saved.equals(ad)){
				MainActivity.mybt.connectToAddress();
				
			}
			
		}
		
		t_device.setText(ad);
		
		t_info.setText(name +"\t" + ad);
		
		
	}


	


	public static void updateFoundWifiDevice(WifiInfo wifiInfo) {
		// TODO Auto-generated method stub
		
		String dev = wifiInfo.getSSID().toString();
		String must  = SP.getString("gpname", "");
		
		if(must.equals("")){
			
			t_gpName.setText(dev);
		}
		
		addInfoText("Connected to " + dev);
	}
	
	
}
