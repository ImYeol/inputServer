package com.example.inputserver;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;


public class MainActivity extends Activity {

	private IEventDataService mService;
	private IEventListener mCallback=new IEventListener.Stub() {
		
		@Override
		public void OnTouchEvent(float x, float y) throws RemoteException {
			// TODO Auto-generated method stub
			MainActivity.this.OnTouchEvent(x,y);
		}
	};
	
	private ServiceConnection mConnection = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			// TODO Auto-generated method stub
			mService=IEventDataService.Stub.asInterface(service);
			try {
				mService.SetTouchListener(mCallback);
			} catch (RemoteException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		@Override
		public void onServiceDisconnected(ComponentName arg0) {
			mService=null;
			mCallback=null;
		}

	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//startService(new Intent(this,BltService.class));
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		bindService(new Intent(this,BltService.class), mConnection, BIND_AUTO_CREATE);
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unbindService(mConnection);
	}
	protected void OnTouchEvent(float x, float y) {
		// TODO Auto-generated method stub
		Toast.makeText(getBaseContext(), "x:"+x + " y:"+y, Toast.LENGTH_SHORT);
	}
	
}
