package pa.myglove;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import fragments.F_Info;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;


public class ProfileManager {

	static Activity A;
	
	public  SharedPreferences  SP_Prof;
	public String sp_current;
	public  ArrayList<String> gesturesPrefs;
	
	public ProfileManager(){
		A= MainActivity.GETACTIVITY;
		sp_current= C.P_GESTURES;
		
		initSavedPrefs();
	}
	
	private void initSavedPrefs() {

		
		SP_Prof = A.getSharedPreferences(C.SP_PROFILE, Context.MODE_PRIVATE);

		String lastSelected = SP_Prof.getString("lastSelectedProfile", "StandartProfil");
		
		
		Set<String> set = SP_Prof.getStringSet(C.MY_PREFS_PROFILES,
				new HashSet<String>());
		ArrayList<String> setlist = new ArrayList<String>(set);
		
			// savedGesturesPrefs = set.toArray(array)
		gesturesPrefs = new ArrayList<String>(setlist);

		if (!gesturesPrefs.contains(sp_current))
			gesturesPrefs.add(sp_current);
		for( int i=0; i< gesturesPrefs.size();i++){
			if( gesturesPrefs.get(i).toString().equals(lastSelected)){
				sp_current = gesturesPrefs.get(i);
			}
		}
		
		
		MainActivity.mybt.com.Gp
		.setSharedPrefs(A.getSharedPreferences(
				sp_current, Context.MODE_PRIVATE));


	}
	
	
public  void createNewProfile(){
		
		AlertDialog.Builder builder = new AlertDialog.Builder(A);
		// Get the layout inflater
		LayoutInflater inflater = A.getLayoutInflater();
		final View myView = inflater.inflate(R.layout.dialog_enter_text, null);
		// Inflate and set the layout for the dialog
		// Pass null as the parent view because its going in the dialog layout
		builder.setView(myView)
				// Add action buttons
				.setTitle("New Profile")
				.setPositiveButton("Create",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int id) {
								// sign in the user ...

								EditText t = (EditText) myView
										.findViewById(R.id.editText_password);
								String text = t.getText().toString();

								if (text.equals("")) {
									addInfoText(" no name entered");
									return;
								}
								
								sp_current = text;

								for (int i = 0; i < gesturesPrefs.size(); i++)
									if (gesturesPrefs.get(i).equals(sp_current)) {
										addInfoText("already exists...");
										return;
									}

								gesturesPrefs.add(sp_current);

								Set<String> set = SP_Prof.getStringSet(
										C.MY_PREFS_PROFILES,
										new HashSet<String>());
								set.add(sp_current);
								SP_Prof.edit().clear()
										.putStringSet(C.MY_PREFS_PROFILES, set)
										.commit();

								MainActivity.mybt.com.Gp
										.setSharedPrefs(A.getSharedPreferences(
												text, Context.MODE_PRIVATE));

								// mybt.com.Gp.loadGestures();
								select(gesturesPrefs.size()-1);
								
								dialog.dismiss();
							}
						})
				.setNegativeButton("Cancel",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								
								dialog.dismiss();
							}
						});
		builder.create().show();
		
	}


public  void deleteProfile( int pos ){
	
	String prof = gesturesPrefs.get(pos);
	gesturesPrefs.remove(pos);
	
	A.getSharedPreferences(prof, Context.MODE_PRIVATE).edit().clear().apply();

	Set<String> set = SP_Prof.getStringSet(
			C.MY_PREFS_PROFILES,
			new HashSet<String>());
	set.remove(prof);
	SP_Prof.edit().clear()
			.putStringSet(C.MY_PREFS_PROFILES, set)
			.commit();
		
	select(0);
	addInfoText("Profile " + prof + " deleted");
	
}


protected static void addInfoText(String string) {
	// TODO Auto-generated method stub
	F_Info.addInfoText(string);
}

public void select(int index) {
	// TODO Auto-generated method stub
	if (index <0) return;
	
	sp_current = gesturesPrefs.get(index);
	MainActivity.mybt.com.Gp
	.setSharedPrefs(A.getSharedPreferences(
			sp_current, Context.MODE_PRIVATE));
	MainActivity.mybt.com.Gp.loadGestures();

}

}
