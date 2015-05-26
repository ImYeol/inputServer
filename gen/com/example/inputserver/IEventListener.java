/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: /home/yeol/workspace/InputServer/src/com/example/inputserver/IEventListener.aidl
 */
package com.example.inputserver;
public interface IEventListener extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.example.inputserver.IEventListener
{
private static final java.lang.String DESCRIPTOR = "com.example.inputserver.IEventListener";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.example.inputserver.IEventListener interface,
 * generating a proxy if needed.
 */
public static com.example.inputserver.IEventListener asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.example.inputserver.IEventListener))) {
return ((com.example.inputserver.IEventListener)iin);
}
return new com.example.inputserver.IEventListener.Stub.Proxy(obj);
}
@Override public android.os.IBinder asBinder()
{
return this;
}
@Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
{
switch (code)
{
case INTERFACE_TRANSACTION:
{
reply.writeString(DESCRIPTOR);
return true;
}
case TRANSACTION_OnTouchEvent:
{
data.enforceInterface(DESCRIPTOR);
float _arg0;
_arg0 = data.readFloat();
float _arg1;
_arg1 = data.readFloat();
this.OnTouchEvent(_arg0, _arg1);
reply.writeNoException();
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements com.example.inputserver.IEventListener
{
private android.os.IBinder mRemote;
Proxy(android.os.IBinder remote)
{
mRemote = remote;
}
@Override public android.os.IBinder asBinder()
{
return mRemote;
}
public java.lang.String getInterfaceDescriptor()
{
return DESCRIPTOR;
}
@Override public void OnTouchEvent(float x, float y) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeFloat(x);
_data.writeFloat(y);
mRemote.transact(Stub.TRANSACTION_OnTouchEvent, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_OnTouchEvent = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
}
public void OnTouchEvent(float x, float y) throws android.os.RemoteException;
}
