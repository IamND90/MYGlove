package external;

import java.util.ArrayList;

public class GY {

	
	public static final int PEAK_TOP = 125;
	public static final int PEAK_MIN = 20;
	public static final int PEAK_STILL = 10;

	public static final int GYRO_MAX_DIFF_STATIC = 25;
	public static final int ANGLE_MAX_DIFF_STATIC = 20;
	public static final int LINACC_MAX_DIFF_STATIC = 10;
	
	private  int GYRO_MAX_DIFF = GYRO_MAX_DIFF_STATIC;
	private  int ANGLE_MAX_DIFF = ANGLE_MAX_DIFF_STATIC;
	private  int LINACC_MAX_DIFF = ANGLE_MAX_DIFF_STATIC;
	
	
	public static final int GESTURE_MIN_TIME = 500;
	public static final int GESTURE_RECOR_TIME = 1500;
	public static final int GESTURE_WAIT_TO_RECORD_TIME = 2500;

	
	
	public static Gesture 	EMPTY_GESTURE	 = new Gesture("", new ArrayList<Point>());
	
	public int changeSensivity_Gyro(int sens){
		

		GYRO_MAX_DIFF= (int) (GYRO_MAX_DIFF_STATIC + ((sens-50)/2));
		return GYRO_MAX_DIFF;
	}
	
	public int changeSensivity_Angle(int sens){
		

		ANGLE_MAX_DIFF= (int) (ANGLE_MAX_DIFF_STATIC + ((sens-50)/2));
		return ANGLE_MAX_DIFF;
	}
	
	public int changeSensivity_LinAcc(int sens){
		

		LINACC_MAX_DIFF = (int) (LINACC_MAX_DIFF_STATIC + ((sens-50)/2));
		return LINACC_MAX_DIFF;
	}
	
	public static  int getIndexOfSmallest(int a[]){
		
		int index =0;
		
		for ( int i=1; i<a.length ;i++){
			
			if ( a[i] >0 && a[i] < a[index]) index=i;
			
		}
		
		return index;
	}
	
	public static  int getIndexOfSmallest(long a[]){
		
		int index =0;
		
		for ( int i=1; i<a.length ;i++){
			
			if ( a[i] >0 && a[i] < a[index]) index=i;
			
		}
		
		return index;
	}
	
	public static  int getIndexOfBiggest(int a[]){
		
		int index =0;
		
for ( int i=1; i<a.length ;i++){
			
			if ( a[i] >0 && a[i] > a[index]) index=i;
			
		}
		
		return index;
	}
	
	public static  int getIndexOfBiggest(long a[]){
		
		int index =0;
		
		for ( int i=1; i<a.length ;i++){
			
			if ( a[i] >0 && a[i] > a[index]) index=i;
			
		}
		
		return index;
	}

	public static ArrayList<Point> shortListOnLen(ArrayList<Point> in, int len){
		ArrayList<Point> list = new ArrayList<Point>(in);
		int size = list.size();
		if ( len >= size) return list;
	
		for ( int i = 0; i< size -len; i++)
			list.remove(0);
			
		size = list.size();
		for ( int i = 0; i< size ; i++)
			list.get(i).RawIndex= i;
		
		return list;
	}
	
	public static ArrayList<Point> removeFromList(ArrayList<Point> in, int bot, int top){
		ArrayList<Point> list = new ArrayList<Point>(in);
		int size = list.size();
		
		for ( int i = 0; i< top && top < size; i++)
			list.remove(size-1-top);
		for ( int i = 0; i< bot; i++)
			list.remove(0);
			
		size = list.size();
		for ( int i = 0; i< size ; i++)
			list.get(i).RawIndex= i;
		
		return list;
	}
	

	
	
	public static boolean isStillBack(  ArrayList<Point> p, int i, int minvalue){
		 
		
		 if ( p.size()< i) return true;
		 int g=0;
		 for ( int x=0; x<i; x++){
			 	if (  p.get(p.size()-1-x).getAbsAvgGyro() < minvalue) g++;
		 }
		 if ( g==i) return true;
		 
		return false;
	 }
	
	public static boolean isStillFront(  ArrayList<Point> p, int i, int minvalue){
		 
		
		 if ( p.size()< i) return true;
		 int g=0;
		 for ( int x=0; x<i; x++){
			 	if (  p.get(x).getAbsAvgGyro() < minvalue) g++;
		 }
		 if ( g==i) return true;
		 
		return false;
	 }
	
	

	
	/*
	public  float isThisGesture( Gesture G,  ArrayList<Point> list, boolean off) {
		// TODO Auto-generated method stub
	
		int size = G.gesture_count;

		int totalDiffGyro =0; 
		int divGyro =0;
		

		float	diff = GYRO_MAX_DIFF;
		diff+= G.complexity/ 4;
		
		//GYRO
		for ( int i=0; i<3; i++){
			totalDiffGyro=0;
			float div = (G.maxDiv[i]+PEAK_MIN*2) /PEAK_TOP;
			if ( div > 1) div = 1;
			if ( div < 0.6) div = 0.5f;
			//div = 1-div;
			
			for ( int p=0; p<size; p++){
				Point a= G.PointList.get(p);
				Point b= list.get(p);

				divGyro 	=Math.abs( b.GyroValue[i] - a.GyroValue[i]);
				totalDiffGyro 	+= Math.abs(b.GyroValue[i] - a.GyroValue[i]);
				
				
				if (p > 2 ){
					
					
					if( totalDiffGyro > (p+2)*(diff * div) ||
						divGyro >  GYRO_MAX_DIFF_STATIC*2)return -1;
					
					
				}
			}
			
		}
			
	
		//ANGLE
		/*
		for ( int i=0; i<2; i++){
			totalDiffAngle=0;
			int offset=0;
			
			
			for ( int p=0; p<size; p++){
				
				Point a= G.PointList.get(p);
				Point b= list.get(p);
				
				if( off && p==0) offset = a.AngleValue[i] - b.AngleValue[i];
			
				totalDiffAngle += Math.abs(a.AngleValue[i] - b.AngleValue[i] -offset);
				
				
				if (p > size/10 ){
					if(  totalDiffAngle > (p+1)*ANGLE_MAX_DIFF)return -2;
					
				}
			}
		}
		
		

		return totalDiffGyro / size;
}
*/	
	

	
	public  float isThisGesture2( Gesture G,  ArrayList<Point> list) {
		// TODO Auto-generated method stub
	/*
		for( int x=0; x<2;x++)
			if ( Math.abs(G.PointList.get(0).AngleValue[x] - list.get(0).AngleValue[x]) > ANGLE_MAX_DIFF) return -2;
		*/
		
		int size = G.gesture_count;

		int totalDiffGyro =0; 
		int totalDivGyro =0;
		int totalDiffLinacc =0;
		int momDifLin =0;
		int momDifGyro =0;

		float	diff = GYRO_MAX_DIFF;
		diff+= (G.complexity-GYRO_MAX_DIFF)/ 4;
		
		
		for ( int i=0; i<3; i++){
			totalDiffGyro=0;
			float div = G.maxDiv[i] /PEAK_TOP;
			if ( div > 1) div = 1;
			if ( div < 0.3) div = 0.3f;
			
			for ( int p=0; p<size; p++){
				Point a= G.PointList.get(p);
				Point b= list.get(p);

				momDifGyro =Math.abs(b.GyroValue[i] - a.GyroValue[i]);
				totalDivGyro 	+= b.GyroValue[i] - a.GyroValue[i];
				totalDiffGyro 	+= momDifGyro;
				
				
				if (p > size/10 ){
									
					if( totalDiffGyro > (p+2)*(diff * div)&&
						Math.abs( totalDivGyro) > (( 5*p/ size + size/5)* diff))return -1;
					if ( momDifGyro > diff *3) return -1;
					
				}
			}
			
		}
			
		
		
		for ( int i=0; i<3; i++){
			
			totalDiffLinacc =0;
			
		
			for ( int p=0; p<size; p++){
				Point a= G.PointList.get(p);
				Point b= list.get(p);

				momDifLin = Math.abs(b.LinaccValue[i] - a.LinaccValue[i]);
				totalDiffLinacc 	+= momDifLin;
				
				
				if (p > 2 ){
					
					if( totalDiffLinacc > (p+2)*(LINACC_MAX_DIFF ))return -1;
					if ( momDifLin > LINACC_MAX_DIFF* 2)return -1;
					
				}
			}
			
		}
		
		
		

		return totalDiffGyro / size;
}

	public static Gesture cutGestures ( Gesture n, ArrayList<Point> list){
		
		float comp = findComplexity(list) ;
		
		if ( comp  < n.stat_complexity * 0.75) return n;

		for ( int i=0; i < n.gesture_count ; i++){
			
				Point f = n.PointList.get(i);
				f.adjustPoint(list.get(i).GyroValue, list.get(i).LinaccValue);
				
				n.PointList.remove(i);
				n.PointList.add(i, f);

		}
		n.wasCutted++;
		n.findComplexity();
		
		return n;
	}
	
	public static float findComplexity(ArrayList<Point> list){
		float temp =0;
		
		for ( int i=0; i<3; i++){
			for ( int k=0; k< list.size(); k++){
				temp+= Math.abs(list.get(k).GyroValue[i]);
			}
		}
		return temp / list.size() /3;
	}
	
	/*
	static public ArrayList<Point>	getPeaks(ArrayList<Point> input){
		
		
		int size = input.size();
		ArrayList<Point> list = new ArrayList<Point>();
		
		if (!input.isEmpty()){
			for ( int j=0; j<3; j++){
				
				Point a;
				for ( int i=0; i<size; i++){
					
	
						int valueIn = 	Math.abs(input.get(i).GyroValue[j]);
						int peakstart, peakend;
						int tempi;
						
						//if ( i < derieved.size()-1) valueNext = derieved.get(i+1)[j];
						
						// Find A and B
						
							
						for ( ;  ( valueIn < GY.PEAK_MIN) && i<(size-1);i++) valueIn = Math.abs(input.get(i).GyroValue[j]);
						peakstart = i;
						i++;
						for ( ;  ( valueIn >= GY.PEAK_MIN) && i<(size-1);i++) valueIn = Math.abs(input.get(i).GyroValue[j]);
						peakend = i;
						
						tempi =i;
			
						
						if (peakend<=size-1 && peakstart < peakend){
							
							//Find C
							
							for ( ;Math.abs(input.get(peakstart).GyroValue[j])	<	Math.abs(input.get(peakstart+1).GyroValue[j]); peakstart++ );
							for ( ;Math.abs(input.get(peakend).GyroValue[j])	>	Math.abs(input.get(peakend-1).GyroValue[j]); peakend-- );
							
							if ( peakend >= peakstart)peakend = peakstart;
							

							a= new Point ( input.get(peakstart).GyroValue, input.get(peakstart).AngleValue,null, input.get(peakstart).RawIndex);
							list.add(a);
						}
						i= tempi+1;
						
		
				}
				
				
			}
			
		}
		
		
		return list;
		
	}
*/
	
	static public ArrayList<Point>	cutAtPeaks(ArrayList<Point> list, ArrayList<Point> peaks){
		
		int first=0;
		int last=0;
		
		for ( int i =0; i< peaks.size(); i++){
			int index=peaks.get(i).RawIndex;
			
			first = index<= first ?index:first;
			last = index>= last ? index:last;
			
		}
		last = list.size() - last;
		return removeFromList(list, first, last);
		
		
	}
	
	
	
	
}
