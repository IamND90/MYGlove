package external;

public class Point {

	
	public int[] GyroValue;
	public int[] LinaccValue;
	int PeakValue;
	
	int tol_G[],  tol_L[];

	private int[] stat_G,stat_L;
	int RawIndex;
	
	public Point(int[] Gyro,  int[] Linacc, int index){
		int lg;
		lg = Gyro.length; 
		if (lg == 3  ){
		
			GyroValue= Gyro;
			
			LinaccValue = Linacc;
			
			stat_G= Gyro;
			
			stat_L = Linacc;
	
			RawIndex = index;
			
			PeakValue = getBiggest();
			
			tol_G = new int[3];
			tol_L = new int[3];
			
		
		}
		
		else {
	
			GyroValue= Gyro;
			

		
			RawIndex = -1;
		}
	}

	
	private int getBiggest() {
		// TODO Auto-generated method stub
		
		int big =0;int i=0;
		for ( ; i<3; i++){
			big = Math.abs(GyroValue[i]) >= big ?Math.abs(GyroValue[i]):big ;
				
		}
		
		return big;
	}


	public int getAbsGyro() {
		// TODO Auto-generated method stub
		int  x= 0;
		int len = GyroValue.length;
	
		for ( int i=0; i<len; i++){
			x += Math.abs(GyroValue[i]);
				
		}
		return x;
	}
	
	public int getAbsAvgGyro() {
		// TODO Auto-generated method stub
		int  x= 0;
		int len = GyroValue.length;
	
		for ( int i=0; i<len; i++){
			x += Math.abs(GyroValue[i]);
				
		}
		return (int)(x/len);
	}
	
	public int getAbsAvgGyroDiff(int[] lastGyro) {
		// TODO Auto-generated method stub
		int  x= 0;
		int len = GyroValue.length;
	if ( len ==0)return 0;
		
		for ( int i=0; i<len; i++){
			x += Math.abs(GyroValue[i]) - Math.abs(lastGyro[i]);
				
		}
		return (int)(x/len);
	}
	
	
	public void adjustPoint( int[] Gyro,  int[] LinAcc){
		
		
		for ( int i=0; i<3;i++){
			
			
			GyroValue[i] = stat_G[i] -  (stat_G[i] -Gyro[i])/4;
			LinaccValue[i] = stat_L[i] -  (stat_L[i] -LinAcc[i])/2;
		
		}
	
	}
	
	

}
