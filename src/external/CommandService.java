package external;

import java.util.ArrayList;

import fragments.F_Gestures;

import pa.myglove.MainActivity;
import pa.myglove.R;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.AudioManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.IBinder;
import android.provider.MediaStore;


public  class CommandService extends Service {



	SharedPreferences SP;

	public int CurrentTemperature;
	ArrayList<Point> pointList;

	ArrayList<String> CommandFound;


	public GesturePack Gp;
	GY gy;
	
	int listMark;
	int lastListTime;
	
	int  gestures_existing;
	

	
	public boolean 	recordingGesture, runRecognition, updateGraph;
	public long startTime, dataReceiveCount, totalDataReceived;
	
	public static AudioPlayer audio;
	public static GoPro gopro;
	public static MyWiFi mywifi;
	
	
	
	public CommandService() {
		
		CurrentTemperature = -271;
		
		startTime=0;
	
		listMark = 0;
		
		
	
		pointList = new ArrayList<Point>();
		CommandFound = new ArrayList<String>();
		Gp = new GesturePack();
		
		startTime = System.currentTimeMillis();
		lastListTime =-1;
		recordingGesture= false;
		runRecognition = false;
		
		dataReceiveCount =0;
		totalDataReceived =0;
		gy= new GY();
		
		updateGraph = false;
		
		gestures_existing = 0;
		
		mywifi= MainActivity.mywifi;
		gopro = MainActivity.gopro;
		audio = MainActivity.audio;
	
	}
	
	//1
	public void addSensorData(String command){
		
			String string[] = command.split(",");

				if (string[0].equals("f") && string.length ==7){
					int Gyro[], Linacc[]; 
					Gyro= new int[3];  Linacc = new int[3];
					
					
					for(int i =0; i<3; i++){
		
						Gyro[i] = Integer.parseInt( string[i+1]);
						Linacc[i] = Integer.parseInt( string[i+4]);
					}

					
					Point p = new Point(Gyro,  Linacc, pointList.size());
					pointList.add(p);
					dataReceiveCount++;
				
					
					if ( updateGraph ){ 
						int temp[] = new int[6];
						temp[0] = Gyro[0];temp[1] = Gyro[1];temp[2] = Gyro[2];
						
						temp[3] = Linacc[0];temp[4] = Linacc[1];temp[5] = Linacc[2];
						
						F_Gestures.updateChart1(dataReceiveCount,temp);
						}
					
					//if (! Gp.Gestures.isEmpty()) pointList = GY.shortListOnLen(pointList, Gp.longestGesture_count);
					
					if (!recordingGesture && !Gp.Gestures.isEmpty() && pointList.size() > 2*Gp.longestGesture_count) 
						pointList.remove(0);
					
				
					
					if ( runRecognition && !recordingGesture)RunRecognition();
					
					
			
					return;
				}
				
				if (string[0].equals("temp")){
					CurrentTemperature = Integer.parseInt(string[1]);
					
				}
			
				//printValues();
	

	}

	
	//2 Finds all peaks in an arraylist

	public boolean enableRecognition(){
		runRecognition= !runRecognition;
		return runRecognition;
	}
	public boolean enableGraphUpdate(){
		updateGraph= !updateGraph;
		return updateGraph;
	}
	
	/*
	private int getItemsOverflow(ArrayList<int[]> list, int time){
		
		
		int size = list.size();
		if ( time == 0) return size-1;
		
		int timelast = list.get(size-1)[0];
		
		for (int i = 0 ;i<size-1; i++){
			
			if (list.get(i)[0] < (timelast - time) ) return i;
		}
		
		
		return 0;
	}

	// deletes 
	private void cleanLists(int from , int to){
		int items = to-from;
		
		if ( items > 0){
			for ( int i = 0; i< items; i++){
				listAngle.remove(from);
				listGyro.remove(from);
				listTime.remove(from);
			}
			
			
		}
		
	}
	
	private void cleanListsOnLen(int len){
		
		int size =listGyro.size();
		if ( len >= size) return;
		if ( len ==0){
			
			listTime.clear();
			listAngle.clear();
			listGyro.clear();
			return;
		}
		for ( int i =0; i< size-len; i++){
			listTime.remove(0);
			listAngle.remove(0);
			listGyro.remove(0);
		}
		
	}
	// cuts away count values top and bot
	private ArrayList<int[]> trimList(ArrayList<int[]> list, int bot , int top){
		int size = list.size();
		
		if ( bot > 0){
			
			for ( int i = 0; i< top; i++){
				list.remove(size - top );
			}
			
			
			for ( int i = 0; i< bot; i++){
				list.remove(0);
			}

		}
		return list;
	}
	
	private ArrayList<Long> trimListL(ArrayList<Long> list, int bot , int top){
		int size = list.size();
		
		if ( bot > 0){
			
			for ( int i = 0; i< top; i++){
				list.remove(size - top );
			}
			
			
			for ( int i = 0; i< bot; i++){
				list.remove(0);
			}

		}
		return list;
	}
	
	
	
	private ArrayList<KeyPoint> getNextList(){

		
		ArrayList<int[]> listG = new ArrayList<>();
		ArrayList<int[]> listA = new ArrayList<>();

		int size = listGyro.size();
		if ( lastListTime < 0 )lastListTime = listGyro.get(0)[0];
		final int nowListTime = listGyro.get(size-1)[0];
		
		if ( (nowListTime - lastListTime) >(int) (Gp.longest_Gesture*0.33)){
			int len_ms = nowListTime - listGyro.get(0)[0];
			int len_min = (int)(Gp.longest_Gesture );
			
			if ( len_ms >= len_min){
				int indexDeleteFrom= getItemsOverflow(listGyro, len_min);
				if ( indexDeleteFrom>0 ){
					//indexDeleteFrom = getFirstIndexOfTimePassed(arraylistGyro, G_SHIFT);
					cleanLists( 0, indexDeleteFrom);
					
				}
				listG= listGyro;
				listA = listAngle;
			}
			lastListTime = nowListTime;
		}
		
		
		return getKeypoints(listG, listA, listTime);
	}
	
	
	
	public void 				RunRecognition(){
		
		
		new Runnable() {
            // After call for background.start this run method call
            public void run() {
                
            	
            	for ( int g =0; g< Gp.Gestures.size() ; g++){
            		
            		
            		Gesture G = Gp.Gestures.get(g);
            		ArrayList<Point > list = GY.shortListOnLen(pointList, G.gesture_count);
            		
            		
            		
            		for ( int k =0; k<3; k++){
            			
            			//Gp.hasGesture(list)
            		}
            	
            		/*
            		double percentTot =  100; double percentHit;
            		for ( int k =0; k<3; k++){
            			for ( int i =0; i<maxcount; i ++){
            			
            				percentHit = GY.isInTolerance(G.PointList.get(i).GyroValue[k], list.get(i).GyroValue[k]);
            				
            				percentTot = (percentTot *0.8 + percentHit*0.2);
            				
            				if ( percentTot < 60  && percentHit ==0) {
            					i = maxcount; k = 3; 
            				}
            				
            			}
            		}
            		if ( percentTot > 80) CommandFound.add(G.GestureName);
            		
            		
            	}
            	
            }
	 }.run();
	
		
	}
	
	
	
	*/

 	

	public String 		getCommand(){
		
		
		
		if ( CommandFound.isEmpty())
		return "";
		
		else{
			String s = CommandFound.get(0);
			CommandFound.remove(0);
			return s;
		}
		
}
	
	

	

	public void RunRecognition(){
		 ArrayList<Point > list; float percent =-1; 

		try{
			for(int i =0; i<  Gp.Gestures.size() ; i++){
				
				Gesture G= Gp.Gestures.get(i);
				if ( pointList.size() >= G.gesture_count ){
					
					list = GY.shortListOnLen(pointList, G.gesture_count);
		        	
		        	if ( list.size() == G.gesture_count) {
		        		percent = gy.isThisGesture2( G,list);
		        		
			        	if (percent <100 && percent >0) {
				   
	
				        	pointList.clear();
				        		
					        if ( percent <G.complexity/2){
					        	G = GY.cutGestures( G, list);
					        	Gp.addNewGesture(G);
					        	MainActivity.addInfoText(""+G.GestureName + " edited \t" + (int)percent + " / " + (int)G.complexity);
					        }
					        else
					        	MainActivity.addInfoText(""+G.GestureName + "\t" + (int)percent + " / " + (int)G.complexity + "\t" + (int)(G.complexity-GY.GYRO_MAX_DIFF_STATIC/3));
					        
				        	CommandFound.add(G.GestureName);
				        	
				        	
				        	//FragmentGestures.updateChart1(G, true);
							
				        }       
			        	else {
			        		//Glove.addInfoText(""+G.GestureName + "\t" + (int)percent);
			        	}
			     
			        	
					}
		        	
					
				}
			}
			
		}catch (IndexOutOfBoundsException i	){
			//infoText.append("\n !!error recognizing!! \n");
			}
		
	}
	
	
	
	public void RecordGesture(String name){
		if ( name.equals("")) return;
		
		new RecordGesture().execute(name);
	
		
	}
	
	private class RecordGesture extends AsyncTask<String, Void, Integer> {

		String s;long starttime; 
		 Gesture g; boolean state1, state2;
		 ArrayList<Point> list;
		 
		 @Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			if ( !recordingGesture) MainActivity.addInfoText("Start recording new Gesture");
			if ( recordingGesture) {
				recordingGesture= false;
				F_Gestures.b_record.setBackgroundResource(R.drawable.custom_btn_arsenic);
				cancel(true);
				
			}
			F_Gestures.b_record.setBackgroundResource(R.drawable.custom_btn_genoa);
		}
		
	
		@Override
		protected Integer doInBackground(String... arg0) {
			try{
				
				if ( recordingGesture) return 0;
				if ( pointList.size()< 5) return 5;
				
				s=arg0[0];
				state1= runRecognition;
				state2= updateGraph;
				
				runRecognition= false;
				recordingGesture = true;
				updateGraph = false;
				
				
			
			
				// STRT ACTUALLY HERE
				starttime = System.currentTimeMillis()+ GY.GESTURE_WAIT_TO_RECORD_TIME;
				
				while( GY.isStillBack(pointList,2, GY.PEAK_MIN) &&
						System.currentTimeMillis() < starttime);
				
				
				//then wait until motion begins
	
				if (System.currentTimeMillis()>=( starttime)) {return 2;}
				
				int mark = pointList.size();
				
	
				starttime = System.currentTimeMillis()+ GY.GESTURE_RECOR_TIME;
				
				
				// recording gesture
				while ((System.currentTimeMillis() <  starttime ) && !GY.isStillBack(pointList, 4, GY.PEAK_STILL) ){
					
					int sz = pointList.size();
					while ( pointList.size() <= sz);
				}
				
				//finish recording
				
				if (System.currentTimeMillis()>=( System.currentTimeMillis()+ GY.GESTURE_MIN_TIME)) {return -1;}
				if (System.currentTimeMillis()>=( starttime)) {return 3;}
				
	
				list =  GY.shortListOnLen(pointList, pointList.size()-mark);
	
				//list = GY.cutAtPeaks(list,new ArrayList<Point>( GY.getPeaks(list)));
				int startlen = list.size();
				
				while ( GY.isStillBack(list, 1,GY.PEAK_MIN) && list.size() > 2) list.remove(list.size()-1);
				while ( GY.isStillFront(list, 1,GY.PEAK_MIN) && list.size() > 2) list.remove(0);
				
				if ( list.size() <10 || list.size() < startlen*0.6)return -4;
				
				// check for similar gestures alredy recorded
				//if ( hasGesture(g))return -3;
				/*
				for ( int i =0; i<Gp.Gestures.size(); i++){
					Gesture F= Gp.Gestures.get(i);
					if(!F.GestureName.equals(s)){
						
						if (gy.isThisGesture(F, list, false)>0) {s = F.GestureName; return -3;}
					}
				}*/
				
				///--------------CREATE GESTURE---------------------------
				
				if ( list.size()>11){
					g = new Gesture(s, new ArrayList<Point>(list));
	
					Gp.addNewGesture(g);
					return 1;
				}
				
				
			}catch (NullPointerException i	){
				return -2;
				}
			
			return 2;
			
		}
		 

		

		@Override
		protected void onPostExecute(Integer result) {
			// TODO Auto-generated method stub
		
		
			if ( result ==1) {
				MainActivity.setToastMessage("Gesture [" + s + "] recorded, size:" + g.gesture_count );
				F_Gestures.updateChart1(g, true);
				
				
			}
			
			if ( result ==2) 
				MainActivity.setToastMessage("Bad Gesture");
			if ( result ==-2) 
				MainActivity.setToastMessage("Error");
			if ( result ==3) 
				MainActivity.setToastMessage("Gesture too long");
			if ( result ==-3) 
				MainActivity.setToastMessage("similar Gesture recorded already for " + s);
				
			if  ( result ==0)
				MainActivity.setToastMessage("Gesture is still recording");
			

			if ( result ==-1)
				MainActivity.setToastMessage("Gesture too short");
			
			
			if  ( result ==5)
				MainActivity.setToastMessage("error, list too short");
			if  ( result ==-4)
				MainActivity.setToastMessage("too weak");
			
		
		
		if ( result != 0 && result != 5){
			pointList.clear();
			recordingGesture = false;
			runRecognition= state1;
			updateGraph=state2;
			
		}
		F_Gestures.b_record.setBackgroundResource(R.drawable.custom_btn_arsenic);
		
		}
		
		
		
}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}


	
public void executeCommand(String com) {

		
		if (com.equals(""))
			return;

		if (com.equals("Music_Play")) {
			sendOrderedBroadcast(AudioPlayer.PLAY_PAUSE(), null);
			
		}
		if (com.equals("Music_Next")) {
			sendOrderedBroadcast(AudioPlayer.NEXT(), null);

		}
		if (com.equals("Music_Previous")) {
			if (audio.lastPrevious > System.currentTimeMillis())
				sendOrderedBroadcast(AudioPlayer.PREVIOUS(), null);
			sendOrderedBroadcast(AudioPlayer.PREVIOUS(), null);
			audio.lastPrevious = System.currentTimeMillis() + 1000;

		}
		if (com.equals("Music_VolUp")) {

			if (audio.lastVolUp > System.currentTimeMillis())
				audio.volUp++;
			else
				audio.volUp = 1;

			audio.lastVolUp = System.currentTimeMillis() + 1200;

			for (int i = 0; i < audio.volUp; i++)
				audio.M.adjustVolume(AudioManager.ADJUST_RAISE,
						AudioManager.FLAG_SHOW_UI);

		}
		if (com.equals("Music_VolDown")) {

			if (audio.lastVolDown > System.currentTimeMillis())
				audio.volDown++;
			else
				audio.volDown = 1;

			audio.lastVolDown = System.currentTimeMillis() + 1200;

			for (int i = 0; i < audio.volDown; i++)
				audio.M.adjustVolume(AudioManager.ADJUST_LOWER,
						AudioManager.FLAG_SHOW_UI);

		}
		if (com.equals("Music_Mute")) {
			int state = audio.M.getStreamVolume(AudioManager.STREAM_MUSIC);
			if (state == 0) {
				for (int i = 0; i < audio.lastMusicState; i++)
					audio.M.adjustVolume(AudioManager.ADJUST_RAISE,
							AudioManager.FLAG_SHOW_UI);

				return;
			}

			else {
				for (int i = 0; i < state; i++)
					audio.M.adjustVolume(AudioManager.ADJUST_LOWER,
							AudioManager.FLAG_SHOW_UI);
				audio.lastMusicState = state;
				return;
			}

		}

		if (com.equals("Music_Playlist_Select")) {

			String[] projection = new String[] {
					MediaStore.Audio.Playlists.Members.PLAYLIST_ID,
					MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.DATA,
					MediaStore.Audio.Media.ALBUM,
					MediaStore.Audio.Playlists.Members.AUDIO_ID };
			Uri contentUri = MediaStore.Audio.Playlists.Members.getContentUri(
					"external", 0);
			contentUri = contentUri.buildUpon()
					.appendQueryParameter("limit", "1").build();
			Cursor playlistCursor = getContentResolver().query(contentUri,
					projection, null, null,
					MediaStore.Audio.Playlists.Members.PLAY_ORDER);

			for (int i = 0; i < projection.length; i++) {
				MainActivity.addInfoText("" + projection[i]);
			}

		}

		if (com.equals("Answer_Phone")) {
			Intent intent = new Intent();
			intent.setAction(Intent.ACTION_ANSWER);
			startActivity(intent);
		}

		if (com.equals("Launch_Musicplayer")) {

			Intent intent = new Intent();
			ComponentName comp = new ComponentName("com.android.music",
					"com.android.music.MediaPlaybackActivity");
			intent.setComponent(comp);
			intent.setAction(Intent.ACTION_RUN);
			startActivity(intent);

		}

		if (com.equals("App_Close")) {
			// mybt.com.Gp.saveAllGestures();
			System.exit(0);

		}

		if (com.equals("App_Background")) {
			// mybt.com.Gp.saveAllGestures();
			if (!MainActivity.isOnPause){
				
			}

				//moveTaskToBack(true);
				
			else {
				startActivity(new Intent(MainActivity.GETCONTEXT,MainActivity.class));
				/*
				Intent intent = new Intent(getPackageManager()
						.getLaunchIntentForPackage("com.example.doggydog"));
				intent.addCategory(Intent.CATEGORY_LAUNCHER);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(intent);
				*/
			}

		}
		
		if (com.equals("GoPro_Power")) {
			if (!mywifi.wifiManager.isWifiEnabled()){
				mywifi.enable(true);
				MainActivity.setToastMessage("Enabling Wifi first, please connect to your GoPro manually");
			}
			if( gopro.Power){
				gopro.sendcommand(GoPro.GP_POWER_OFF);
				gopro.Power=false;
				}
			else{
				gopro.sendcommand(GoPro.GP_POWER_ON);
				gopro.Power=true;
			}
		}
			
		if (com.equals("GoPro_ModeChange")) {
			
			if (!mywifi.wifiManager.isWifiEnabled()){
				mywifi.enable(true);
				MainActivity.setToastMessage("Enabling Wifi first, please connect to your GoPro manually");
			}
			
			if(gopro.Mode== 0){
				gopro.sendcommand(GoPro.GP_MODE_CAM);
				gopro.Mode=1;
				}
			else{
				gopro.sendcommand(GoPro.GP_MODE_VID);
				gopro.Mode=0;
			}
		}
		if (com.equals("GoPro_Shut_Stop")) {
			if (!mywifi.wifiManager.isWifiEnabled()){
				mywifi.enable(true);
				MainActivity.setToastMessage("Enabling Wifi first, please connect to your GoPro manually");
			}
			
			// mybt.com.Gp.saveAllGestures();
			if(!gopro.IsShooting)
				gopro.sendcommand(GoPro.GP_SHUT);
			else
				gopro.sendcommand(GoPro.GP_STOP);

		}
	}


	
	 
	 
		
	 
}
