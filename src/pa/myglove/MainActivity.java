package pa.myglove;


import external.Arduino;
import external.AudioPlayer;
import external.GoPro;
import external.MyBT;
import external.MyWiFi;
import fragments.F_Info;
import fragments.F_Main;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import android.provider.MediaStore;



public class MainActivity extends Activity {

	public static SharedPreferences SP_Main;
	public static  Activity GETACTIVITY;
	public static Context GETCONTEXT;
	
	
	public static Arduino ard;
	public static MyBT mybt;
	public static MyWiFi mywifi;
	public static GoPro gopro;
	public static AudioPlayer audio;
	
	
	public static FragmentManager fragmentManager;
	
	public static boolean isOnPause;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        GETACTIVITY = this;
        GETCONTEXT = this.getApplicationContext();
        
        fragmentManager=getFragmentManager();
        
        if (savedInstanceState == null) {
            fragmentManager.beginTransaction()
                    .add(R.id.container, new F_Main())
                    .commit();
        }
        
        SP_Main = getSharedPreferences(C.SP_MAIN, MODE_PRIVATE);
        
        mybt = 	new MyBT();
		ard =	new Arduino();
		mywifi= new MyWiFi();
		gopro = new GoPro();
		audio = new AudioPlayer();
        
		isOnPause = false;
		
		
    }

    @Override
    public void onBackPressed() {
    	// TODO Auto-generated method stub
    	System.exit(0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }




	public static void addInfoText(String s) {
		// TODO Auto-generated method stub
		F_Info.addInfoText(s.replaceAll("\n", "\t"));
	}


	public static void setToastMessage(String string) {
		// TODO Auto-generated method stub
		Toast.makeText(GETACTIVITY.getApplicationContext(), string, Toast.LENGTH_SHORT).show();

	}
	

}
