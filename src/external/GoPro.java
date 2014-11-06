package external;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;

import pa.myglove.C;
import pa.myglove.MainActivity;

import android.content.Context;
import android.content.SharedPreferences;


public class GoPro {

	public final static String GP_POWER_ON = "http://10.5.5.9/bacpac/PW?t=#&pw01";
	public final static String GP_POWER_OFF = "http://10.5.5.9/bacpac/PW?t=#&pw00";
	public final static String GP_SHUT = "http://10.5.5.9/camera/SH?t=#&pw01";
	public final static String GP_STOP = "http://10.5.5.9/camera/SH?t=#&pw00";
	public final static String GP_MODE_CAM="http://10.5.5.9/camera/CM?t=#&pw00";
	
	public final static String GP_MODE_VID="http://10.5.5.9/camera/CM?t=#&pw01";
	public final static String GP_MODE_TIMEL="http://10.5.5.9/camera/CM?t=#&pw03";
	public final static String GP_SOUND_OFF="http://10.5.5.9/camera/BS?t=#&pw00";
	public final static String GP_SOUND_ON="http://10.5.5.9/camera/BS?t=#&pw01";
	
	private static  String password;
	
	public int Mode; //0-Vid 1-Cam
	public boolean IsShooting, Power;
	
	SharedPreferences SP;
	
	private static ArrayList<String> list;
	
	public  ArrayList<String> getComNames(){
		
		ArrayList<String> s = new ArrayList<String>();
		for(String i :list){
			s.add(i);
		}
		
		return s;
	}
	
	public void setPassword(String pw){
		password= pw;
	}
	public boolean hasPassword(){
		if(!password.equals(""))
			return true;
		return false;
	}
	
	
	public  void sendcommand(final String StrUrl)
    {        
           // addInfoText("Sending:"+StrUrl,false);    
             (new Thread(new Runnable() {


         @Override
         public void run() {
             try {
            	
            	 String sUrl = StrUrl.replace("#", password);
            	 
                 URL url = new URL(sUrl);
                 java.net.URLConnection con = url.openConnection();
                 con.connect();
                 java.io.BufferedReader in =new java.io.BufferedReader(new java.io.InputStreamReader(con.getInputStream()));
                 
             } catch (MalformedURLException e) {
                 // TODO Auto-generated catch block
            	 //GloveMainActivity.setToastMessage("did not send, ensuce connection to gopro");
                 //e.printStackTrace();
             } catch (IOException e) {
                 // TODO Auto-generated catch block
            	 //GloveMainActivity.setToastMessage("did not send, ensuce connection to gopro");
                //e.printStackTrace();
             }


         }
     })).start();
    }
	
	
	
	public GoPro(){
		
		IsShooting= false;
		Power= false;
		Mode=0;
		SP = MainActivity.GETACTIVITY.getSharedPreferences(C.SP_SETTINGS, Context.MODE_PRIVATE);
		
		password=SP.getString("password", "");
		
		list  = new ArrayList<String>();
		
		list.add(GP_POWER_ON);
		
		list.add(GP_POWER_OFF);
		list.add(GP_SHUT);
		list.add(GP_STOP);
		list.add(GP_MODE_CAM);
		list.add(GP_MODE_TIMEL);
		list.add(GP_MODE_VID);
		list.add(GP_SOUND_ON);
		list.add(GP_SOUND_OFF);
		
		
		
	}
	
	
}
