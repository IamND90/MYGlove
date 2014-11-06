package external;

import java.util.ArrayList;

	public class Gesture {

		
			
		public String GestureName;

		public ArrayList<Point> PointList;

		ArrayList<Point> PeakList;
		
		int gesture_count, firstPeakValue ;
		long firstPeakTime, timeFirstToLastPeak;
		
		public int wasCutted;
		boolean bad_gesture;
		
		int maxDiv[];
		float complexity;
		float stat_complexity;
		
		String assignedCom;
		
	public Gesture(String name , ArrayList<Point> keyPointList){

		bad_gesture = false;
		GestureName = name;
		if ( keyPointList.isEmpty()) { gesture_count =-1; bad_gesture = true; return ;}
		//if ( keyPointList.size() > GY.GESTURE_LENGHT_VALUES) {gesture_count = -1; return;}
		PointList = new ArrayList<Point>(keyPointList);
		//PeakList = GY.getPeaks(PointList);
		
		gesture_count = PointList.size();
		
		wasCutted =0;	
		complexity =0.f;
		
		
		maxDiv= new int[3];
		findDivergence();
		findComplexity();
		stat_complexity = complexity;
		
		assignedCom="x";
		
	}
	
	public void findDivergence(){
		int small = GY.PEAK_TOP;
		int big= -GY.PEAK_TOP;
		for ( int i=0; i<3; i++){
			for ( int k=0; k< gesture_count; k++){
				
				
				
				 if ( PointList.get(k).GyroValue[i] < small ) small = PointList.get(k).GyroValue[i];
				 if ( PointList.get(k).GyroValue[i]> big ) big = PointList.get(k).GyroValue[i];
			}
			
			maxDiv[i] = big-small;
		}
		
		
	}
	
	public void findComplexity(){
		complexity=0;
		
		for ( int i=0; i<3; i++){
			for ( int k=0; k< gesture_count; k++){
				complexity+= Math.abs(PointList.get(k).GyroValue[i]);
			}
		}
		complexity = complexity / gesture_count /3;
	}
	
	
	
	
	
	public Point getFirstPoint(){
		return PointList.get(0);
	}
	public Point getLastPoint(){
		return PointList.get(PointList.size()-1);
	}
	
	public boolean isAGesture(){
		if (  gesture_count == -1 || bad_gesture )return false;
		return true;
	}
}
