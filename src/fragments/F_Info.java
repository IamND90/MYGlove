package fragments;

import pa.myglove.MainActivity;
import pa.myglove.R;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

public  class F_Info extends Fragment {

	View rootView;
	Button b1,b2,b3;
	ImageButton b_bt,b_wifi;
	public static CheckBox checkbox_connected,checkbox_running;
	
	static TextView text;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		rootView = inflater.inflate(R.layout.fragment_fast, container,false);
		
		X_findID();
		X_onCLick();
		
		
		return rootView;
	}


	private void X_onCLick() {
		// TODO Auto-generated method stub
		
		b1.setOnClickListener(new Button.OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						MainActivity.mybt.connectToAddress();
					}
					
				});
		
		b2.setOnClickListener(new Button.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				//MainActivity.mybt.com.runRecognition= true;
				boolean run = MainActivity.mybt.com.enableRecognition();
				checkbox_running.setChecked(run);
				//changeColor(button_runRecognition_local, run);
			}
			
		});
	}


	private void X_findID() {
		// TODO Auto-generated method stub
		b1= (Button) rootView.findViewById(R.id.button_info_connect);
		b2= (Button) rootView.findViewById(R.id.button_info_run);
		text = (TextView) rootView.findViewById(R.id.textView_fast);
		
		checkbox_connected = (CheckBox) rootView.findViewById(R.id.checkBox_connected);
		checkbox_running = (CheckBox) rootView.findViewById(R.id.checkBox_running);
		
	}
	
	public static void addInfoText(String s){
		
		text.setText(s);
	}
}
