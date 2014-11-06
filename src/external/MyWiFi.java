package external;

import java.util.ArrayList;
import java.util.List;

import fragments.F_Settings;

import pa.myglove.MainActivity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.net.wifi.p2p.WifiP2pManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;


public class MyWiFi {

	
	
	WifiConfiguration wifiConf;
	public WifiManager wifiManager;
	WifiReceiver wifiReceiver;
	WifiInfo wifiInfo;
	
	public String default_SSID;
	public String default_Password;
	
	 List<WifiConfiguration> 	wifiConf_saved;
	 List<String> 			myList_saved,myList_found;
	 List<ScanResult> 		scanResults;
		
	
	public MyWiFi(){
		
		
		wifiManager = (WifiManager) MainActivity.GETACTIVITY.getSystemService(Context.WIFI_SERVICE);
		wifiReceiver = new WifiReceiver();
		
		myList_found = new ArrayList<String>();
		myList_saved = new ArrayList<String>();
		scanResults = new ArrayList<ScanResult>();
		wifiInfo = wifiManager.getConnectionInfo();
	
		wifiConf_saved= wifiManager.getConfiguredNetworks();
		
		default_SSID = "";
		default_Password= "";
		
		
		IntentFilter intentFilter = new IntentFilter(WifiManager.NETWORK_STATE_CHANGED_ACTION);
		MainActivity.GETACTIVITY.registerReceiver(wifiReceiver, intentFilter);

		
	}
	
	private WifiConfiguration findInConfigurations(String bssid){
		
		for(int i =0; i<wifiConf_saved.size();i++){
			String b = wifiConf_saved.get(i).BSSID;
			String c = wifiConf_saved.get(i).SSID;
			if(b != null)
				if(b.equals(bssid))return wifiConf_saved.get(i);
			if(c != null)
				if(c.equals(bssid))return wifiConf_saved.get(i);
		}
		return null;
	}
	
	private ScanResult findInScanresults(String bssid){
		
		for(int i =0; i<scanResults.size();i++){
			String b = scanResults.get(i).BSSID;
			String c = scanResults.get(i).SSID;
			if(b != null)
				if(b.equals(bssid))return scanResults.get(i);
			if(c != null)
				if(c.equals(bssid))return scanResults.get(i);
			
		}
		return null;
	}
	
	private WifiConfiguration getNewWifiConfWPA(String ssid, String pw){
		WifiConfiguration W = new WifiConfiguration();
		W.SSID = String.format("\"%s\"", ssid);
		W.preSharedKey = String.format("\"%s\"",pw );
		
		return W;
		
	}

	private void refreshWifi() {
 		if (wifiManager.getWifiState() == WifiManager.WIFI_STATE_DISABLED) {
 			wifiManager.setWifiEnabled(true);
 		}
 		wifiInfo = wifiManager.getConnectionInfo();

 		if (wifiInfo.getSSID() == null || !wifiInfo.getSSID().equals(default_SSID)) {

 			wifiConf_saved = wifiManager.getConfiguredNetworks();
 			for (WifiConfiguration i : wifiConf_saved) {
 				if (i.SSID != null && i.SSID.equals("\"" + default_SSID + "\"")) {
 					wifiManager.disconnect();
 					wifiManager.enableNetwork(i.networkId, true);
 					wifiManager.reconnect();

 					break;
 				}
 			}
 		}

 	}
     
	 private static void addInfoText(String s,boolean del){
    	F_Settings.addInfoText(s);
 	}
     
     private class WifiReceiver extends BroadcastReceiver {
 		public void onReceive(Context c, Intent intent) {
 			NetworkInfo wifiNetworkInfo = (NetworkInfo) intent
 					.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
 			
 			
 			if (wifiNetworkInfo.getState() == State.CONNECTED) {
 				wifiInfo = wifiManager.getConnectionInfo();
 				F_Settings.updateFoundWifiDevice(wifiInfo);
 				
 				if (wifiManager != null
 						&& wifiManager.getConnectionInfo() != null
 						&& wifiManager.getConnectionInfo().getSSID() != null
 						&& !wifiManager.getConnectionInfo().getSSID()
 								.equals(default_SSID)) {
 					
 					refreshWifi();
 				}
 			}  else if (wifiNetworkInfo.getState() == State.DISCONNECTED) {
 				addInfoText("\n Disconnected", false);
 			}

 			
 		}

 	}

	
     public void enable(boolean is) {
		// TODO Auto-generated method stub
		wifiManager.setWifiEnabled(is);
	}

     public void startScan() {
		// TODO Auto-generated method stub
		if( !wifiManager.isWifiEnabled())enable(true);
		
		wifiManager.startScan();
	}

	 public void setNetwork(int pos) {
		// TODO Auto-generated method stub
		 WifiConfiguration w =  wifiConf_saved.get(pos);
		 wifiManager.disconnect();
		 wifiManager.enableNetwork(w.networkId, true);
		 wifiManager.reconnect();
	}

	public void connectToNetwork(String mac, String password) {
		// TODO Auto-generated method stub
		default_SSID= mac;
		default_Password = password;
		
		WifiConfiguration w = findInConfigurations(mac);
		if ( w != null){
			
			wifiManager.disconnect();
			wifiManager.enableNetwork(w.networkId, true);
			wifiManager.reconnect();
			return;
		}
		
		ScanResult R= findInScanresults(mac);
		if ( R != null){
			
			WifiConfiguration W = getNewWifiConfWPA(R.SSID, password);
			
			wifiManager.disconnect();
			int id = wifiManager.addNetwork(W);
			wifiManager.saveConfiguration();
			wifiManager.enableNetwork(id, true);
			wifiManager.reconnect();
			}
	}

	public String getMyAddress() {
		// TODO Auto-generated method stub
		if ( wifiInfo == null)return "";
		return wifiInfo.getBSSID();
	}

     
     
}
