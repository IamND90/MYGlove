package fragments;

import pa.myglove.R;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public  class F_Start extends Fragment {

	static View rootView;
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		rootView = inflater.inflate(R.layout.fragment_start, container,false);
		
		this.X_findID();
		this.X_onCLick();
		
	
		
		return rootView;
	}


	private void X_onCLick() {
		// TODO Auto-generated method stub
		
		
	}

	

	private void X_findID() {
		// TODO Auto-generated method stub
		
	}
}
