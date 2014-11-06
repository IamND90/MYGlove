package external;

import pa.myglove.MainActivity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.SystemClock;
import android.view.KeyEvent;

public class AudioPlayer   {

	
	
	public AudioPlayer() {
		
			lastMusicState = 0;

			lastVolDown = 0;
			lastVolUp = 0;
			lastPrevious = 0;

			volUp = 0;
			volDown = 0;
			
			M = (AudioManager) MainActivity.GETACTIVITY. getSystemService(Context.AUDIO_SERVICE);
		
	}

	public int lastMusicState, volUp, volDown;
	public long lastVolUp, lastVolDown, lastPrevious;

	public AudioManager M;
	
	
	
	
	
	public static Intent PLAY_PAUSE(){
		long eventtime = SystemClock.uptimeMillis();

		Intent downIntent = new Intent(Intent.ACTION_MEDIA_BUTTON, null);
		KeyEvent downEvent = new KeyEvent(eventtime, eventtime, KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE, 0);
		downIntent.putExtra(Intent.EXTRA_KEY_EVENT, downEvent);
		//sendOrderedBroadcast(downIntent, null);
		return downIntent;
		
	}
	
	public static Intent NEXT(){
		long eventtime = SystemClock.uptimeMillis();
		
		Intent downIntent = new Intent(Intent.ACTION_MEDIA_BUTTON, null);
		KeyEvent downEvent = new KeyEvent(eventtime, eventtime, KeyEvent.ACTION_DOWN,   KeyEvent.KEYCODE_MEDIA_NEXT, 0);
		downIntent.putExtra(Intent.EXTRA_KEY_EVENT, downEvent);
		
		return downIntent;
	}
	
	public static Intent PREVIOUS(){
		long eventtime = SystemClock.uptimeMillis();
		
		Intent downIntent = new Intent(Intent.ACTION_MEDIA_BUTTON, null);
		KeyEvent downEvent = new KeyEvent(eventtime, eventtime, KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_MEDIA_PREVIOUS, 0);
		downIntent.putExtra(Intent.EXTRA_KEY_EVENT, downEvent);
		
		return downIntent;
	}

	public static Intent CLOSE(){
		long eventtime = SystemClock.uptimeMillis();
		
		Intent downIntent = new Intent(Intent.ACTION_MEDIA_BUTTON, null);
		KeyEvent downEvent = new KeyEvent(eventtime, eventtime, KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_MEDIA_CLOSE, 0);
		downIntent.putExtra(Intent.EXTRA_KEY_EVENT, downEvent);
		
		return downIntent;
	}

	public static Intent FAST_FORWARD(){
		long eventtime = SystemClock.uptimeMillis();
		
		Intent downIntent = new Intent(Intent.ACTION_MEDIA_BUTTON, null);
		KeyEvent downEvent = new KeyEvent(eventtime, eventtime, KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_MEDIA_FAST_FORWARD, 0);
		downIntent.putExtra(Intent.EXTRA_KEY_EVENT, downEvent);
		
		return downIntent;
	}
	

	
}
