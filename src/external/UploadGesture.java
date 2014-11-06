package external;

import pa.myglove.C;
import pa.myglove.MainActivity;
import android.os.AsyncTask;


public class UploadGesture extends AsyncTask<Gesture, Void, Void> {

	static final int TIMEOUT = 10;
	static final int TIME_TO_UPLOAD = 3000;
	static final int ERROR_MAX = TIME_TO_UPLOAD / TIMEOUT;

	int errorcount = 0;
	int mycount = 0;
	
	int totalgestures =0;
	

	MyBT mybt;
	Gesture G;
	String towrite = "0";

	@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
		mybt = MainActivity.mybt;
	}
	
	@Override
	protected Void doInBackground(Gesture... params) {
		// TODO Auto-generated method stub

		totalgestures = params.length;
		mybt.writeData(C.COM_DELETE_ALL);
		delay(TIMEOUT);
		
		for ( int gestures = 0; gestures< params.length; gestures++){
			G = params[gestures];

			errorcount = -1;
			mycount = 0;
			towrite = "0";
			int received = mybt.receivedT;

			mybt.writeData(C.COM_NEW_GESTURE + "," + G.gesture_count);
			// addInfoText(G.GestureName + ","+ G.gesture_count);
			delay(TIMEOUT);

			for (int i = 0; i < G.gesture_count;) {

				if (errorcount > ERROR_MAX)
					return null;

				if (received < mybt.receivedT
						&& (mybt.receivedText.get(mybt.receivedT).equals("ok") || mybt.receivedText
								.get(mybt.receivedT).equals("Rdy"))) {

					if (mybt.receivedText.get(mybt.receivedT).equals("Rdy"))
						mybt.com.Gp.getGesture(G.GestureName).assignedCom = mybt.assignedGesture;

					if (i == G.gesture_count)
						return null;

					towrite = C.COM_NEW_GESTURE;

					for (int j = 0; j < 3; j++) {
						towrite += ",";
						towrite += G.PointList.get(i).GyroValue[j];
					}

					for (int j = 0; j < 3; j++) {
						towrite += ",";
						towrite += G.PointList.get(i).LinaccValue[j];
					}

					towrite += ";";

					// addInfoText(towrite);
					mybt.writeData(towrite);
					

					mycount++;
					received++;
					i++;
					
					publishProgress();
				} else {
					delay(TIMEOUT);
					errorcount++;
				}
			}

			
			mybt.writeData(C.COM_DONE);
			mybt.writeData(C.COM_FREE_MEM);
			MainActivity.ard.uploadedgestures++;
			publishProgress();
			delay(200);
		}
		
		
		return null;
	}

	@Override
	protected void onPostExecute(Void result) {

		if (errorcount == -1)
			addInfoText("Memory is full");
		// TODO Auto-generated method stub
		if (errorcount < ERROR_MAX && errorcount >= 0) {
			
			
			//addInfoText("Gestures succesfully uploaded");
			if ( MainActivity.ard.uploadedgestures == mybt.com.Gp.Gestures.size()){
				MainActivity.ard.uploadedgestures=0;
				addInfoText("Gestures succesfully uploaded");
			}
			
			

		} else {
			addInfoText("ERROR: uploaded: " + mycount + " last: " + towrite);
			mybt.writeData(C.COM_ERROR);
		}
	}

	private void addInfoText(String string) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void onProgressUpdate(Void... values) {
		
		//FragmentMain.progressbar_upload_overall.setProgress(100/totalgestures * (ard.uploadedgestures));
	}
	
	void delay(int time) {

		long t = System.currentTimeMillis();
		while (System.currentTimeMillis() < t + time)
			;
	}

}
