package external;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.UUID;

import fragments.F_Info;
import fragments.F_Settings;

import pa.myglove.C;
import pa.myglove.MainActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Handler;
import android.telephony.TelephonyManager;

public class MyBT  {

	  public CommandService com;
	
	  public BluetoothAdapter bluetoothAdapter;
	  
	   public BluetoothSocket btSocket;
	   public BluetoothDevice btDevice;
	  

	  private OutputStream outStream = null;
	  private InputStream inStream = null;
	  
	  
	  private Handler handler;
	  private byte delimiter = 10;
	  private boolean stopWorker = false;
	  private int readBufferPosition = 0;
	  private byte[] readBuffer = new byte[1024];
	  private byte[] msgBuffer = new byte[1024];
	  
	  
	  protected String StandartConnectionAddress;

	  public boolean  btConnected, dataListenerCreated;

	  
	  private boolean isConnecting;
	  public boolean ready;
	  
	  public long countReceived;
	  public int tempReceived;
	  public long lastReceived;
	  public boolean finishGestureUpload;
	 
	  
	  public int 				receivedT;
	  public ArrayList<String> 	receivedText;
	  public String assignedGesture;
	  
	  public MyBT(){
		  
		
		  btSocket = null;
		  btDevice = null;
		  
		  lastReceived = System.currentTimeMillis();
		  
		  bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		  bluetoothAdapter.cancelDiscovery();
		  
		  handler = new Handler();
		  isConnecting = false;
		 
		
		  tempReceived=0;
		  countReceived =0;
		  
		
		  com = new CommandService( );
		  
		  btConnected = false;
		  dataListenerCreated = false;
		  ready = false;
		  finishGestureUpload=false;
		  
		  StandartConnectionAddress= getAddress();
	
		  receivedText = new ArrayList<String>();
		  
		  receivedT=-1;
		  assignedGesture="x";
		  
		  BroadcastReceiver mReceiver = new BroadcastReceiver() {
				public void onReceive(Context context, Intent intent) {
					String action = intent.getAction();
					// When discovery finds a device
					if (BluetoothDevice.ACTION_FOUND.equals(action)) {
						// Get the BluetoothDevice object from the Intent
						BluetoothDevice device = intent
								.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

						F_Settings.updateFoundDevice(device);
					}
					
					if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {
						// Get the BluetoothDevice object from the Intent
						F_Info.checkbox_connected.setChecked(true);
					}
					if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action) ||
							BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED.equals(action)) {
						// Get the BluetoothDevice object from the Intent
						F_Info.checkbox_connected.setChecked(false); 
						disconnectArduino();
					}
					if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {
						// Get the BluetoothDevice object from the Intent
						BluetoothDevice device = intent
								.getParcelableExtra(BluetoothDevice.EXTRA_BOND_STATE);

						MainActivity.setToastMessage("State:");
					}
					
				}
			};
			
			IntentFilter filter1 = new IntentFilter(BluetoothDevice.ACTION_FOUND);
			IntentFilter filter2 = new IntentFilter(BluetoothDevice.ACTION_ACL_CONNECTED);
			IntentFilter filter3 = new IntentFilter(BluetoothDevice.ACTION_ACL_DISCONNECTED);
			IntentFilter filter4 = new IntentFilter(BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED);
			IntentFilter filter5 = new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
			MainActivity.GETACTIVITY.registerReceiver(mReceiver, filter1);
			MainActivity.GETACTIVITY.registerReceiver(mReceiver, filter2);
			MainActivity.GETACTIVITY.registerReceiver(mReceiver, filter3);
			MainActivity.GETACTIVITY.registerReceiver(mReceiver, filter4);
			MainActivity.GETACTIVITY.registerReceiver(mReceiver, filter5);
	  }
	  

		 public boolean 	CheckBlueToothState(){
		     if (bluetoothAdapter == null){
		    	 //StartActivity.addInfoText("Bluetooth NOT support");
		    	 return false;
		        }else{
		         if (bluetoothAdapter.isEnabled()){
			          if(bluetoothAdapter.isDiscovering()){
			        	  //StartActivity.addInfoText("Bluetooth is currently in device discovery process");
			        	  return true;
			          }else{
			        	  
			        	  //StartActivity.addInfoText("Bluetooth is Enabled");
			        	  return true;
			          }
		         }else{
		        	 btConnected = false;
		        	 //StartActivity.addInfoText("Bluetooth is NOT Enabled");
		        	 return false;
		        	 
		             
		             //StartSearching();
		         }
		         
		        }
		    }
		 
	
		 
		 public void 		connectToAddress() {
			 
			  if (! bluetoothAdapter.isEnabled()) return ;
			  
			  	
				if ( bluetoothAdapter.isDiscovering()) bluetoothAdapter.cancelDiscovery();
				
				
				new AttemptConnection().execute((Void)null);
				
				
				
		    
			}
		 
		 public boolean 	disconnectArduino(){
			 
			 if (btSocket.equals(null) || btDevice.equals(null)) return true;
			 try {
		            
				 	outStream.close();
					inStream.close();
				 	//writeData(C.COM_STOP);
				 	stopWorker= true;
					btSocket.close();
					
					
					
					
					}catch(IOException e){
						
						
						return false;
					}
			 
			 return true;
		 }
		 

		 private void 		beginListenForData()   {
		        try {
		                       inStream = btSocket.getInputStream();
		               } catch (IOException e) {
		               }
		        
		       Thread workerThread = new Thread(new Runnable()
		       {
		       	
		       	
		           public void run()
		           {                
		              while(!Thread.currentThread().isInterrupted() && !stopWorker)
		              {
		                   try
		                   {
		                       int bytesAvailable = inStream.available();  
		                       
		                       if(bytesAvailable > 0)
		                       {
		                           byte[] packetBytes = new byte[bytesAvailable];
		                           inStream.read(packetBytes);
		                           for(int i=0;i<bytesAvailable;i++)
		                           {
		                               byte b = packetBytes[i];
		                               if(b == delimiter )
		                               {
		                                   byte[] encodedBytes = new byte[readBufferPosition];
		                                   System.arraycopy(readBuffer, 0, encodedBytes, 0, encodedBytes.length);
		                                   final String data = new String(encodedBytes, "US-ASCII");
		                                   readBufferPosition = 0;
		                  
		                                   
		                                   handler.post(new Runnable()
		                                   {
		                                       public void run()
		                                       {
		                                    	  
				                                  MainActivity.addInfoText( "<" +data.replaceAll("\n", "") + ">");
			                                    	    
			                                       
		                                       }
		                                   });
		                                   
		                               }
		                               
		                               if(b == 59)
		                               {
		                                   byte[] encodedBytes = new byte[readBufferPosition];
		                                   System.arraycopy(readBuffer, 0, encodedBytes, 0, encodedBytes.length);
		                                   final String data = new String(encodedBytes, "US-ASCII");
		                                   readBufferPosition = 0;
		                                   
		                                   handler.post((new Runnable()
		                                   
		                                   {	
		                                       public void run()
		                                       {
		                                             if ( data.replaceAll("\\s+","").startsWith("f")){
		                                            	 com.addSensorData(data);
		                                             }
		                                             else {
		                                            	 
		                                            	 String d= data;
				                                    	  d= d.replaceAll("\\s+","");
				                                    	 // d= d.replaceAll("\\W", "");
				                                    	  if ( d.length()>1){
					                                    	  
					                                    	   
					                                    	   
					                                    	   String g[] = d.split(":");

					                                    	   receivedText.add(g[0]);
					                                    	   receivedT++;
					                                    	   countReceived++;
					                                    	   
					                                    	   if ( g[0].equals("Command")){
					                                    		  com.executeCommand(g[1]);
					                                    		  
					                                    		  //MainActivity.addInfoText(data);
					                                    		   
					                                    	   }
					                                    	   if ( g[0].equals("FM")){
					                                    		   MainActivity.ard.updateMemory(Integer.parseInt(g[1]));
					                                    		   MainActivity.addInfoText("Free Memory:" +g[1]);
						                                    	   }
					                                    	
					                                    	   if ( d.equals("Success")){
					                                    		   finishGestureUpload=true;
					                                    		   ready= true;
					                                    		   
					                                    		   }
					                                    	   
					                                    	   if( g[0].equals("Gestures")) {
					                                    		   MainActivity.addInfoText(data);
					                                    		   
					                                    	   }
					                                    	 
					                                    	   if ( d.equals("Ready")){
					                                    		   writeData("S");
					                                    		   ready= true;
					                                    		   
					                                    		   }
					                                    	   if ( d.equals("RunRec")){
					                                    		   MainActivity.ard.isRunningRecognition = Integer.parseInt(g[1])==0?false:true;
					                                    		   MainActivity.addInfoText(data);
					                                    		   }
					                                    	   
					                                    	   if( g[0].equals("Rdy")) {
					                                    		   finishGestureUpload=false;
					                                    		   assignedGesture=g[1];
					                                    		   tempReceived=1;
					                                    		   
					                                    	   }
					                                    	   if ( d.equals("ok")){ d = d +tempReceived;tempReceived++;}
					                                    	  
					                                    		   
					                                    	   
					                                    	   
					                                       }
				                                    	  
				                                    	  
		                                             }
		                                             
		                                             //textCount.setText("" + count++);
		                                             lastReceived = System.currentTimeMillis();
		                                            	 
		                                             
		                                             
		                                       }
											
		                                   }));
		                                   
		                               }
		                               
		                               
		                               else
		                               {
		                                   readBuffer[readBufferPosition++] = b;
		                               }
		                           }
		                       }
		                       
		                   }
		                   catch (IOException ex)
		                   {
		                       stopWorker = true;
		                   }
		                   
		              }
		           }
		       });

		       workerThread.start();
		   }
		  

		 public  void 		writeData(String data) {
			 
			//if (!btConnected) return;
			
			data = "/" + data + ";";
			 

			 if ( !btSocket.isConnected()) 
				 return;
				 
			 
			 
		     msgBuffer = data.getBytes();
		     
		     try {
		    	 
		             outStream.write(msgBuffer);
		           
		             //addInfoText("send");
		        
		     } catch (IOException e) {
		             
		    	 //
		    	 MainActivity.addInfoText("failed to send... ");
		     }
		}
		 

		 public String 		getAddress(){
			 
			 String ad = MainActivity.GETACTIVITY.getSharedPreferences(C.SP_SETTINGS, Context.MODE_PRIVATE)
					 .getString("devname", "");
			 if ( ad.equals(""))
				 return "";
			 else return ad;
		 }
		 
		 public void 		setAddress(String ad){
			 
			 StandartConnectionAddress= ad;
		 }
		 
		 
		 
		 final class AttemptConnection extends AsyncTask<Void, Void, Boolean> {
			 boolean interrupt = false;
			 @Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();
				
				if( btConnected){
					disconnectArduino();
					MainActivity.addInfoText("Disconnected");
					cancel(true);
					
				}
				
				if ( isConnecting) {
					interrupt = true;
					MainActivity.addInfoText("Still connecting");
					isConnecting= false;
					cancel(false);
					}
				
				if (! interrupt && !btConnected){
					MainActivity.addInfoText("Connecting to..." + getAddress());
					
				}
			}
			 
			@Override
			protected Boolean doInBackground(Void... arg0) {
				// TODO Auto-generated method stub
				if ( interrupt) return false;
				if ( btConnected) disconnectArduino();
				
				try {
					
					//if (btSocket.isConnected()) return true;
					
					btDevice = bluetoothAdapter.getRemoteDevice(getAddress());
					
		            btSocket = btDevice.createRfcommSocketToServiceRecord(C.MY_UUID);
		            TelephonyManager tManager = (TelephonyManager) MainActivity.GETACTIVITY.getSystemService(Context.TELEPHONY_SERVICE);
		            UUID.nameUUIDFromBytes(tManager.getDeviceId().getBytes("utf8"));
		            
		            
					btSocket.connect();
					
					outStream = btSocket.getOutputStream();
					inStream = btSocket.getInputStream();
					
					
					
					stopWorker = false;
					beginListenForData();
					btConnected = true;
					
					return true;
				
				} catch (IOException e) {
					try {
						
						btSocket.close();
						btConnected = false;
					} catch (IOException e2) {
						
					}
				
					return false;
				}
				
				
				
				
			}
			 
			@Override
			protected void onPostExecute(Boolean result) {
				// TODO Auto-generated method stub
				super.onPostExecute(result);
				
				
				if(result){
					
					
					//FragmentMain.button_connect.setBackgroundResource(R.drawable.custom_btn_genoa);
					
					writeData(C.COM_CONNECTION);
					//writeData(C.COM_START);
					
					MainActivity.setToastMessage("connected");
					//if ( testConnectivity())addInfoText("....connectivity established");
				}
				
				else{
					//Toast.makeText(context, "failed to connect", Toast.LENGTH_SHORT).show();
					MainActivity.addInfoText("....failed");
				}
				isConnecting = false;
			}
			 
			
	}
		 
		 
		 public boolean setState(Boolean state){
			 if( bluetoothAdapter == null)return false;
			 
			 if( state)
				bluetoothAdapter.enable();
			 else 
				 bluetoothAdapter.disable();
			 
			 return state;
		 }
		 
		 
}






		 
		

		 


