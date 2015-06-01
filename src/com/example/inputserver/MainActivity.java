package com.example.inputserver;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;
import java.util.UUID;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
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
	public final static String TAG = "InputTest";
	public static final int REQUEST_TO_ENABLE_BT = 100;
	private BluetoothAdapter mBluetoothAdapter;
	private UUID MY_UUID = UUID
			.fromString("D04E3068-E15B-4482-8306-4CABFA1726E7");
	private final static String CBT_SERVER_DEVICE_NAME = "IM-T100K";
	private BluetoothServerSocket mServerSocket;
	private IEventListener mCallback=new IEventListener.Stub() {
		
		@Override
		public void OnTouchEvent(float x, float y) throws RemoteException {
			// TODO Auto-generated method stub
			MainActivity.this.OnTouchEvent(x,y);
		}

		@Override
		public void getInt(int x, int y) throws RemoteException {
			// TODO Auto-generated method stub
			MainActivity.this.getInt(x, y);
		}
	};
	public void getInt(int x,int y)
	{
		Log.d(TAG, "get int from Service :"+x+" "+y);
	}
	private ServiceConnection mConnection = new ServiceConnection() {

		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			// TODO Auto-generated method stub
			mService=IEventDataService.Stub.asInterface(service);
			try {
				mService.SetTouchListener(mCallback);
				Log.d(TAG, "service connected");
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
	/*	mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		if (mBluetoothAdapter == null) {
			Log.d(TAG, "device does not support bluetooth");
			return;
		} else {
			if (!mBluetoothAdapter.isEnabled()) {
				Log.d(TAG, "bluetooth support but not enabled");
				Intent enableBtIntent = new Intent(
						BluetoothAdapter.ACTION_REQUEST_ENABLE);
			//	startActivityForResult(enableBtIntent, REQUEST_TO_START_BT);
			} else {
				Log.d(TAG, "bluetooth enabled");
				new AcceptThread().start();
			}
		}*/
		Log.d(TAG, "MainActivity onCreate");
		startService(new Intent(this,BltService.class));
	}
	private class AcceptThread extends Thread {
		// private BluetoothServerSocket mServerSocket;
		public AcceptThread() {
			try {
				mServerSocket = mBluetoothAdapter
						.listenUsingRfcommWithServiceRecord(
								"ClassicBluetoothServer", MY_UUID);
			} catch (IOException e) {
				Log.d(TAG, "get listend sock is error");
			}
		}

		public void run() {
			BluetoothSocket socket = null;
			// Keep listening until exception occurs or a socket is returned
			while (true) {
				try {
					socket = mServerSocket.accept(); // blocking call
				} catch (IOException e) {
					Log.v(TAG, e.getMessage());
					break;
				}
				Log.d(TAG, "socket is accepted");
				// If a connection was accepted
				if (socket != null) {
					// Do work in a separate thread
					new ConnectedThread(socket).start();
					Log.d(TAG, "connectedThread is called");
				}
			}
		}
	}


	private class ConnectedThread extends Thread {
		private final BluetoothSocket mSocket;
		private int bytesRead;
		private Handler h;
		private InputStream reader;
		private BufferedWriter writer;
		private DataInputStream in;

		public ConnectedThread(BluetoothSocket socket) {
			mSocket = socket;
			InputStream tmp = null;
			try {
				tmp = socket.getInputStream();
			//	in=new DataInputStream(tmp);
				// reader = new BufferedReader(new
				// InputStreamReader(socket.getInputStream()));
				// writer = new BufferedWriter(new
				// OutputStreamWriter(socket.getOutputStream()));
			} catch (IOException e) {
				e.printStackTrace();
			}
			reader = tmp;
		}

		public void run() {
			int i=0;
			float x,y;
			byte[] buffer=new byte[100];
			try {
				while(reader.read(buffer) > 0)
				{
					Log.d(TAG, "data:"+buffer[0]+" "+buffer[1]);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			/*while(true)
			{
				try{
				while(in.available()>0){
					
						i=in.readInt();
						x=in.readFloat();
						y=in.readFloat();
						Log.d(TAG, "x:"+x+"y:"+y);
				}
				}catch(Exception e)
				{
					e.printStackTrace();
					try {
						reader.close();
						// BuffInputStream.close();
						if (mSocket.isConnected())
							mSocket.close();
					} catch (IOException ex) {
						// TODO Auto-generated catch block
						ex.printStackTrace();
					}
				}
			}*/
		}
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
		stopService(new Intent(this,BltService.class));
	}
	protected void OnTouchEvent(float x, float y) {
		// TODO Auto-generated method stub
		Log.d(TAG, "OnTouchEvent : "+x+" "+y);
	}
	
}
