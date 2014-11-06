package external;


public class Arduino {

	
	public int freeMemory;
	public boolean isRunningRecognition;
	
	public final static int BYTE_MIN =  280;
	public final static int BYTE_POINT =  7;
	
	int uploadedgestures;
	
	public final static int getSize(int points){
		return points * BYTE_POINT+1;
	}
	
	public Arduino(){
		
		freeMemory =1156;
		isRunningRecognition=false;
		uploadedgestures=0;
	}
	public int getFreeMemory(){
		return freeMemory;
	}
	
	public boolean fits( Gesture G){
		
		if( getSize(G.gesture_count) > freeMemory -BYTE_MIN)return false;
		
		return true;
	}
	
	public void updateMemory(int freemem){
		
		freeMemory= freemem;
		//FragmentMain.updateInfo("Free Memory: " + freeMemory );
	}
	
	
}
