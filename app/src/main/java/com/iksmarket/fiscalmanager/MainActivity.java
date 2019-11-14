package com.iksmarket.fiscalmanager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.iksmarket.fiscalmanager.Bluetooth.BluetoothService;
import com.iksmarket.fiscalmanager.Bluetooth.Driver.Commands;
import com.shashank.sony.fancytoastlib.FancyToast;

public class MainActivity extends AppCompatActivity {

    Intent intent;
    Button button, testbutton, testbutton2;

    BroadcastReceiver printerReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle b = intent.getExtras();
            String message = null;
            if (b != null) {
                message = b.getString("message");
            }
            if (message != null) {
                FancyToast.makeText(MainActivity.this, message, FancyToast.LENGTH_LONG, FancyToast.ERROR, false).show();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = findViewById(R.id.buttonsend);
        testbutton = findViewById(R.id.testresult);
        testbutton2 = findViewById(R.id .testresult2);

        testbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Commands.printVer();
            }
        });


        registerReceiver(printerReceiver, new IntentFilter("PrinterResponse"));





        testbutton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent("PrinterBroadcast");
                i.putExtra("CommandToSend", "dayClearReport");
                sendBroadcast(i);
            }
        });

        intent = new Intent(this, BluetoothService.class);
        startService(intent);

       button.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent i = new Intent("PrinterBroadcasty");
               i.putExtra("CommandToSend", "PrintVersion");
               sendBroadcast(i);

           }
       });



    }

    @Override
    protected void onResume() {
        super.onResume();
        intent = new Intent(this, BluetoothService.class);
        startService(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(printerReceiver);
    }
}


