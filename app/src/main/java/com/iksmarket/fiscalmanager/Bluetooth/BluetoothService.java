package com.iksmarket.fiscalmanager.Bluetooth;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.github.ivbaranov.rxbluetooth.BluetoothConnection;
import com.iksmarket.fiscalmanager.Bluetooth.Driver.Commands;

import org.apache.commons.lang3.ArrayUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class BluetoothService extends Service {

    public InputStream mmInStream;
    public OutputStream mmOutStream;


    private static final String LOG_TAG = "dd" ;
    final int handlerState = 0;
    Handler bluetoothIn;
    BluetoothDevice devices;
    public BluetoothAdapter btAdapter = null;

    public ConnectingThread mConnectingThread;
    public ConnectedThread mConnectedThread;
    List<Byte> temp = new ArrayList<>();


    List<Byte> commandToSend = new ArrayList<Byte>();

    private static final UUID BTMODULEUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    private static final String MAC_ADDRESS = "YOUR:MAC:ADDRESS:HERE";
    private              StringBuilder recDataString = new StringBuilder();

    BroadcastReceiver broadcastReceiver =  new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle b = intent.getExtras();
            String message = b.getString("CommandToSend");
            Log.e("Send to printer:", "" + message);

             if (message != null && message.equals("PrintVersion")) {
                mConnectedThread.write(ArrayUtils.toPrimitive(Commands.printVer().toArray(new Byte[Commands.printVer().size()])));
             }

            if (message != null && message.equals("dayClearReport")) {
                mConnectedThread.write(ArrayUtils.toPrimitive(Commands.dayClearReport(0).toArray(new Byte[Commands.dayClearReport(0).size()])));
            }
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("BT SERVICE", "SERVICE CREATED");
        registerReceiver(broadcastReceiver, new IntentFilter("PrinterBroadcast"));
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("BT SERVICE", "SERVICE STARTED");
        bluetoothIn = new Handler() {

            public void handleMessage(android.os.Message msg) {
                Log.d("DEBUG", "handleMessage");
                if (msg.what == handlerState) {                                     //if message is what we want
                    String readMessage = (String) msg.obj;                                                                // msg.arg1 = bytes from connect thread
                    recDataString.append(readMessage);

                    Log.d("RECORDED", recDataString.toString());
                    // Do stuff here with your data, like adding it to the database
                }
                recDataString.delete(0, recDataString.length());                    //clear all string data
            }
        };


        btAdapter = BluetoothAdapter.getDefaultAdapter();       // get BluetoothService adapter
        checkBTState();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        bluetoothIn.removeCallbacksAndMessages(null);

        if (mConnectedThread != null) {
            mConnectedThread.closeStreams();
        }
        if (mConnectingThread != null) {
            mConnectingThread.closeSocket();
        }
        Log.d("SERVICE", "onDestroy");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(LOG_TAG, "MyService onBind");
        return new Binder();
    }


    private void checkBTState() {

        if (btAdapter == null) {
            Log.d("BT SERVICE", "BLUETOOTH NOT SUPPORTED BY DEVICE, STOPPING SERVICE");
            stopSelf();
        } else {
            if (btAdapter.isEnabled()) {
                Log.d("DEBUG BT", "BT ENABLED! BT ADDRESS : " + btAdapter.getAddress() + " , BT NAME : " + btAdapter.getName());
                try {
                    Set<BluetoothDevice> pairedDevices = btAdapter.getBondedDevices();
                    if (pairedDevices.size() > 0) {
                        for (BluetoothDevice device : pairedDevices) {
                            if ("MARKET".equals(device.getName())) {
                                devices = device;
                            }
                        }
                    }

                    Log.d("DEBUG BT", "ATTEMPTING TO CONNECT TO REMOTE DEVICE : " + MAC_ADDRESS);
                    mConnectingThread = new ConnectingThread(devices);
                    mConnectingThread.start();
                } catch (IllegalArgumentException e) {
                    Log.d("DEBUG BT", "PROBLEM WITH MAC ADDRESS : " + e.toString());
                    Log.d("BT SEVICE", "ILLEGAL MAC ADDRESS, STOPPING SERVICE");
                    stopSelf();
                }
            } else {
                Log.d("BT SERVICE", "BLUETOOTH NOT ON, STOPPING SERVICE");
                stopSelf();
            }
        }
    }


    private class ConnectingThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final BluetoothDevice mmDevice;

        public ConnectingThread(BluetoothDevice device) {
            Log.d("DEBUG BT", "IN CONNECTING THREAD");
            mmDevice = device;
            BluetoothSocket temp = null;
            Log.d("DEBUG BT", "MAC ADDRESS : " + MAC_ADDRESS);
            Log.d("DEBUG BT", "BT UUID : " + BTMODULEUUID);
            try {
                temp = mmDevice.createRfcommSocketToServiceRecord(BTMODULEUUID);
                Log.d("DEBUG BT", "SOCKET CREATED : " + temp.toString());
            } catch (IOException e) {
                Log.d("DEBUG BT", "SOCKET CREATION FAILED :" + e.toString());
                Log.d("BT SERVICE", "SOCKET CREATION FAILED, STOPPING SERVICE");
                stopSelf();
            }
            mmSocket = temp;
        }

        @Override
        public void run() {
            super.run();
            Log.d("DEBUG BT", "IN CONNECTING THREAD RUN");
            btAdapter.cancelDiscovery();
            try {
                mmSocket.connect();
                Log.d("DEBUG BT", "BT SOCKET CONNECTED");
                mConnectedThread = new ConnectedThread(mmSocket);
               BluetoothConnection bluetoothConnection = new BluetoothConnection(mmSocket);

                bluetoothConnection.observeByteStream()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(new Consumer<Byte>() {
                            @Override public void accept(Byte aByte) throws Exception {
                                packetAdding(aByte);
                            }

                        }, new Consumer<Throwable>() {
                            @Override public void accept(Throwable throwable) throws Exception {
                                System.out.println("Some fucking error: " + throwable.getMessage());

                            }
                        });


                mConnectedThread.start();
                Log.d("DEBUG BT", "CONNECTED THREAD STARTED");

                mConnectedThread.write(new byte[]{1});

            } catch (IOException e) {
                try {
                    Log.d("DEBUG BT", "SOCKET CONNECTION FAILED : " + e.toString());
                    Log.d("BT SERVICE", "SOCKET CONNECTION FAILED, STOPPING SERVICE");
                    mmSocket.close();
                    stopSelf();
                } catch (IOException e2) {
                    Log.d("DEBUG BT", "SOCKET CLOSING FAILED :" + e2.toString());
                    Log.d("BT SERVICE", "SOCKET CLOSING FAILED, STOPPING SERVICE");
                    stopSelf();

                }
            } catch (IllegalStateException e) {
                Log.d("DEBUG BT", "CONNECTED THREAD START FAILED : " + e.toString());
                Log.d("BT SERVICE", "CONNECTED THREAD START FAILED, STOPPING SERVICE");
                stopSelf();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public void closeSocket() {
            try {

                mmSocket.close();
            } catch (IOException e2) {

                Log.d("DEBUG BT", e2.toString());
                Log.d("BT SERVICE", "SOCKET CLOSING FAILED, STOPPING SERVICE");
                stopSelf();
            }
        }

        void packetAdding(Byte aByte){
            temp.add(aByte);
            switch (temp.get(0)){
                case 6:
                    temp.clear();
                    System.out.println("Confirmed");
                    break;
                case 22:
                    temp.clear();
                    System.out.println("Processing");
                    break;
            }

            if (temp.size() > 10){
                if (temp.get(temp.size() - 5) != (byte) 16 && temp.get(temp.size() - 4) == (byte) 16 && temp.get(temp.size() - 3) == (byte) 3)
                {
                    System.out.println("Here we fucking go!");
                    System.out.println(temp);

                    Intent i = new Intent("PrinterResponse");
                    i.putExtra("message", ResponseFromPrinter.decode(temp).get(0));
                    sendBroadcast(i);

                    temp.clear();
                }
            }
        }
    }

    public class ConnectedThread extends Thread {

        public ConnectedThread(BluetoothSocket socket) {
            Log.d("DEBUG BT", "IN CONNECTED THREAD");
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            try {

                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
                Log.d("DEBUG BT", e.toString());
                Log.d("BT SERVICE", "UNABLE TO READ/WRITE, STOPPING SERVICE");
                stopSelf();
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            Log.d("DEBUG BT", "IN CONNECTED THREAD RUN");
            byte[] mmBuffer = new byte[1024];
            int numBytes; // bytes returned from read()
            List<Byte> temp = new ArrayList<>();

        }


        public void write(byte[] input) {
            try {
                mmOutStream.write(input);

            } catch (IOException e) {

                Log.d("DEBUG BT", "UNABLE TO READ/WRITE " + e.toString());
                Log.d("BT SERVICE", "UNABLE TO READ/WRITE, STOPPING SERVICE");
                stopSelf();
            }
        }

        public void closeStreams() {
            try {

                mmInStream.close();
                mmOutStream.close();
            } catch (IOException e2) {

                Log.d("DEBUG BT", e2.toString());
                Log.d("BT SERVICE", "STREAM CLOSING FAILED, STOPPING SERVICE");
                stopSelf();
            }
        }
    }
}