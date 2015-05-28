/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: /home/gyl115/InputTest/InputTest_/src/com/example/inputserver/IEventDataService.aidl
 */
package com.example.inputserver;
public interface IEventDataService extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements com.example.inputserver.IEventDataService
{
private static final java.lang.String DESCRIPTOR = "com.example.inputserver.IEventDataService";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an com.example.inputserver.IEventDataService interface,
 * generating a proxy if needed.
 */
public static com.example.inputserver.IEventDataService asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof com.example.inputserver.IEventDataService))) {
return ((com.example.inputserver.IEventDataService)iin);
}
return new com.example.inputserver.IEventDataService.Stub.Proxy(obj);
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
case TRANSACTION_SetTouchListener:
{
data.enforceInterface(DESCRIPTOR);
com.example.inputserver.IEventListener _arg0;
_arg0 = com.example.inputserver.IEventListener.Stub.asInterface(data.readStrongBinder());
this.SetTouchListener(_arg0);
reply.writeNoException();
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements com.example.inputserver.IEventDataService
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
@Override public void SetTouchListener(com.example.inputserver.IEventListener listener) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeStrongBinder((((listener!=null))?(listener.asBinder()):(null)));
mRemote.transact(Stub.TRANSACTION_SetTouchListener, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_SetTouchListener = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
}
public void SetTouchListener(com.example.inputserver.IEventListener listener) throws android.os.RemoteException;
}
