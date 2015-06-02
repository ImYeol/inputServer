package com.example.inputserver;

import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import android.app.Instrumentation;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.SystemClock;
import android.util.Log;
import android.view.MotionEvent;

public class BltService extends Service {

	public final static String TAG = "InputTest/blt";
	public static final int REQUEST_TO_ENABLE_BT = 100;
	private BluetoothAdapter mBluetoothAdapter;
	private UUID MY_UUID = UUID
			.fromString("D04E3068-E15B-4482-8306-4CABFA1726E7");
	private final static String CBT_SERVER_DEVICE_NAME = "IM-T100K";
	private BluetoothServerSocket mServerSocket;
	private final IBinder mBinder=new EventDataBinder();
	private IEventListener mCallback;
	private Instrumentation mInstumentation;
	
	private int IsDown=0;
	
	public class EventDataBinder extends IEventDataService.Stub {

		@Override
		public void SetTouchListener(IEventListener listener)
				throws RemoteException {
			// TODO Auto-generated method stub
			BltService.this.mCallback=listener;
		}

	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		Log.d(TAG, "onBind");
		return mBinder;
	}
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		mInstumentation=new Instrumentation();
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		Log.d(TAG, "blt Service onCreate");
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
		}
	}

	/*
	 * @Override protected void onDestroy() { // TODO Auto-generated method stub
	 * super.onDestroy(); try { mServerSocket.close(); } catch (IOException e) {
	 * // TODO Auto-generated catch block e.printStackTrace(); } }
	 */
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
		private long downTime=0;

		public ConnectedThread(BluetoothSocket socket) {
			mSocket = socket;
			InputStream tmp = null;
			try {
				tmp = socket.getInputStream();
				in=new DataInputStream(tmp);
				// reader = new BufferedReader(new
				// InputStreamReader(socket.getInputStream()));
				// writer = new BufferedWriter(new
				// OutputStreamWriter(socket.getOutputStream()));
			} catch (IOException e) {
				Log.d(TAG, e.getMessage());
			}
			reader = tmp;
		}

		public void run() {
			int i=0;
			float x,y;
			byte[] buffer=new byte[100];
		/*	try {
				while(reader.read(buffer) > 0)
				{
					Log.d(TAG, "data:"+buffer[0]+" "+buffer[1]);
					mCallback.getInt(buffer[0], buffer[1]);
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				Log.d(TAG, e.getMessage());
			} catch(RemoteException ex)
			{
				Log.d(TAG, ex.getMessage());
			}
			*/
			while(true)
			{
				try{
				while((i=in.readInt())>=0){
					
						//i=in.readInt();
						x=in.readFloat();
						y=in.readFloat();
						Log.d(TAG, "x:"+x+"y:"+y);
						//mCallback.OnTouchEvent(x, y);
						//long downTime = SystemClock.uptimeMillis();
						MotionEvent event=null;
						if(i==0)
						{
							downTime = SystemClock.uptimeMillis();
							long eventTime = SystemClock.uptimeMillis();
							event = MotionEvent.obtain(downTime,eventTime,MotionEvent.ACTION_DOWN, x,y, 0);
						}
						else if(i==1)
						{
							long eventTime = SystemClock.uptimeMillis();
							event = MotionEvent.obtain(downTime,eventTime,MotionEvent.ACTION_MOVE, x,y, 0);
						}
						else if(i==2)
						{
							long eventTime = SystemClock.uptimeMillis();
							event = MotionEvent.obtain(downTime,eventTime,MotionEvent.ACTION_UP, x,y, 0);
						}
						mInstumentation.sendPointerSync(event);
						//mInstumentation.sendPointerSync(up_event);
						//mInstumentation.waitForIdleSync();
				}
				}catch(Exception e)
				{
					e.printStackTrace();
					Log.d(TAG, "run exception e: "+e.getMessage());
					mInstumentation.waitForIdleSync();
					/*try {
						reader.close();
						if (mSocket.isConnected())
							mSocket.close();
					} catch (IOException ex) {
						// TODO Auto-generated catch block
						Log.d(TAG, "run exception2 ex: "+ex.getMessage());
					//	break;
					}*/
					
				}
			}
			
		}
	}
	@Override
	public boolean onUnbind(Intent intent) {
		// TODO Auto-generated method stub
		Log.d(TAG, "service onUnbind");
		return super.onUnbind(intent);
	}
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.d(TAG, "service OnDestroy");
	}
	private Handler EventHandler =new Handler();

}
