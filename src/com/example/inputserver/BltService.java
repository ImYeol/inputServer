package com.example.inputserver;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.WeakReference;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.Set;
import java.util.UUID;
import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

public class BltService extends Service {

	public final static String TAG = "BltService_Server";
	public static final int REQUEST_TO_ENABLE_BT = 100;
	private BluetoothAdapter mBluetoothAdapter;
	private UUID MY_UUID = UUID
			.fromString("D04E3068-E15B-4482-8306-4CABFA1726E7");
	private final static String CBT_SERVER_DEVICE_NAME = "IM-T100K";
	private BluetoothServerSocket mServerSocket;
	private final IBinder mBinder=new EventDataBinder();
	private IEventListener mCallback;
	
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
		mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
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

		public ConnectedThread(BluetoothSocket socket) {
			mSocket = socket;
			InputStream tmp = null;
			try {
				tmp = socket.getInputStream();
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
			int bufferSize = 512;
			byte[] buffer = new byte[bufferSize];
			try {
				bytesRead = -1;
				Log.d(TAG, "ready to receive");
				while ((bytesRead = reader.read(buffer)) > 0) {
					Log.d(TAG, "bytesRead=" + bytesRead);
				}
				Log.d(TAG, "receive success");
				float[] floatBuffer=ByteBuffer.wrap(buffer).asFloatBuffer().array();
				mCallback.OnTouchEvent(floatBuffer[0], floatBuffer[1]);
			} catch (IOException e) {
				Log.d(TAG, "error to get img");
				e.printStackTrace();
			}
			catch(Exception e){
				Log.d(TAG, "error to get img");
				e.printStackTrace();
			}
			
			try {
				reader.close();
				// BuffInputStream.close();
				if (mSocket.isConnected())
					mSocket.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private Handler EventHandler =new Handler();

}
