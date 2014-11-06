package external;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import pa.myglove.C;
import pa.myglove.MainActivity;
import pa.myglove.R;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class GesturePack {

	SharedPreferences sp;
	public ArrayList<Gesture> 	Gestures;
	public ArrayList<String>	AvailableGestures;
	public List<String> 		Commands;
	
	int longestGesture_count;
	
	public GesturePack(){
		
	
		Gestures = new ArrayList<Gesture>(); 
		AvailableGestures = new ArrayList<String>();
		
		sp= MainActivity.GETACTIVITY.getSharedPreferences(C.P_GESTURES, Context.MODE_PRIVATE);
		Commands  = Arrays.asList(MainActivity.GETACTIVITY.getResources().getStringArray(
				R.array.commands));
		
		longestGesture_count =1;
		
		//loadGestures();
	}
	
	
	public void setSharedPrefs(SharedPreferences s){
		
		sp = s;
		loadGestures();
	}
	
	
	
	public void addNewGesture( Gesture Ges){
		

			String gname = Ges.GestureName;
	
			
			for ( int g =0; g < Gestures.size(); g++){
				if (Gestures.get(g).GestureName.equals(gname)) Gestures.remove(g);
				if (AvailableGestures.get(g).equals(gname)) AvailableGestures.remove(g);
			}
					
			AvailableGestures.add(Ges.GestureName);
			Gestures.add(Ges);
				
			
			calculateLongestGesture();
					
	}


	public Gesture getGesture( String s){
		
		if ( Gestures.isEmpty()) return GY.EMPTY_GESTURE;
		
		for ( int i=0; i <Gestures.size(); i++)
			if (Gestures.get(i).GestureName.equals(s)) return Gestures.get(i);
		return GY.EMPTY_GESTURE;
	}
	
	public Gesture getGesture( int s){
		if( s>= Gestures.size()) return GY.EMPTY_GESTURE;
		return Gestures.get(s);
	}

	public String[] getExistingString(){
		// returns list of existing gestures
		
		String r[] = new String[Gestures.size()];
		
		for ( int i=0; i<Gestures.size(); i++){
			  
			for ( int j =0; j<Commands.size(); j++){
				
				if ( Commands.get(j).equals(Gestures.get(i).GestureName)) {
					r[i] = Gestures.get(i).GestureName;
					j=1000;
				}
				
				
			}
			
			
		}
		
		return r;
	}
	

	
	public int[] getExistingIndex(){
		// returns list of existing gestures
		
		int r[] = new int[Gestures.size()];
		
		for ( int i=0; i<Gestures.size(); i++){
			  
			for ( int j =0; j<Commands.size(); j++){
				
				if ( Commands.get(j).equals(Gestures.get(i).GestureName)) {
					r[i] = j;
					j=1000;
				}
				
				
			}
			
			
		}
		
		return r;
	}
	
	
	public boolean hasGesture( String name ) {
		
		for ( int i=0; i<Gestures.size(); i++){
			if ( name.equals(Gestures.get(i).GestureName))return true;
		}
		
		return false;
	}
	
	
	
	
	private void calculateLongestGesture() {
		// TODO Auto-generated method stub
		
		for ( int i=0; i< Gestures.size(); i++){
			longestGesture_count = Gestures.get(i).gesture_count >= longestGesture_count ? Gestures.get(i).gesture_count :longestGesture_count;
		}
		if (Gestures.isEmpty()) longestGesture_count = -1;
	}


 	public void saveGesture(Gesture g) {
		// TODO Auto-generated method stub

		String name = g.GestureName;
		ArrayList<Point> p = g.PointList;
		
		Set<String> set  = sp.getStringSet("savedCommands", new HashSet<String>());
		boolean hasGesture =false;
		
		if (!set.isEmpty()){
		for ( int i =0; i < set.size() ; i++)
			if ( set.toArray()[i].equals(name))hasGesture = true;
		}
		
		if ( !hasGesture)
			set.add(name);
		Editor t = sp.edit();

		t.putStringSet("savedCommands", set);
		t.putString("assignedCom", g.assignedCom);
		t.
		putInt(name + "_" + "size",	p.size());
		
		for ( int i=0; i < p.size(); i++){
			
			for ( int j=0; j<3	; j++){
			t.
				putInt(name + "_" + i + "_" + j + "_" +"G",	p.get(i).GyroValue[j]).
				putInt(name + "_" + i + "_" + j + "_" +"L",	p.get(i).LinaccValue[j]);
			
			}
		
		
			
		
			t.putInt(name + "_" + i + "_" + "I", p.get(i).RawIndex);
		}
		t.apply();
		
		//StartActivity.changeColor(name, Color.YELLOW);
	}
 	
 	
	
 	public void saveAllGestures(){
 		deleteAllSavedGestures();
 		for ( int i=0; i< Gestures.size();i++)
 			saveGesture(Gestures.get(i));
 	}
 	
	public int loadGestures() {
		// TODO Auto-generated method stub
		Gestures.clear();
		longestGesture_count=0;
		
		
		Set<String> set  = sp.getStringSet("savedCommands", new HashSet<String>());
		
		try{
			
			if ( !set.isEmpty()){
	
				for ( int i =0; i < set.size() ; i++){
					ArrayList<Point> l = new ArrayList<Point>();
					String name = (String) set.toArray()[i];
				
					int size= sp.getInt(name + "_" + "size", 0);
					
					for ( int s =0; s<size; s++){
						int[] gyro = new int[3];
						
						int[] lin = new int[3];
						
						for ( int j=0; j<3	; j++){
							gyro[j] = sp.getInt(name + "_" + s + "_" + j +"_" + "G", 250);
							lin[j] = sp.getInt(name + "_" + s + "_" + j +"_" + "L", 250);
							}
						
					
						
						l.add(new Point(gyro, lin, s));
					}
					Gesture mg = new Gesture(name,l);
					mg.assignedCom = sp.getString("assignedCom", "");
					addNewGesture(mg);
					
					
				}
			}
		}catch( NullPointerException  e){
			
			//GloveMainActivity.setToastMessage("error loading Gestures");
		}
			
		
		
		//GloveMainActivity.setToastMessage("loaded " + set.size() + " gestures");
		
		
		return set.size();
	}
	
	
	public void deleteAllSavedGestures(){
		
		sp.edit().clear().commit();
	}

	public void deleteALLGestures(){
		
		deleteAllSavedGestures();
		Gestures.clear();
		AvailableGestures.clear();

	}


	public String getAssigned(String string) {
		// TODO Auto-generated method stub
		for ( int i=0; i <Gestures.size(); i++){
			
			if ( Gestures.get(i).assignedCom.equals(string)) return Gestures.get(i).GestureName;
		}
		
		
		return "";
	}
}
