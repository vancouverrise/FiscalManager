package com.iksmarket.fiscalmanager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.iksmarket.fiscalmanager.Bluetooth.BluetoothService;
import com.iksmarket.fiscalmanager.Bluetooth.ResponseFromPrinter;

import org.apache.commons.lang3.ArrayUtils;

public class MainActivity extends AppCompatActivity {

    Intent intent;
    Button button, testbutton, testbutton2;

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

                System.out.println("Смотри сюда: " + Integer.toBinaryString(1));
            }
        });

        BroadcastReceiver printerReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Bundle b = intent.getExtras();
                String message = b.getString("message");
                if (message != null) {
                    Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                }

            }
        };

        registerReceiver(printerReceiver, new IntentFilter("PrinterResponse"));



        testbutton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ResponseFromPrinter.status((byte)2);
            }
        });
        intent = new Intent(this, BluetoothService.class);
        startService(intent);

       button.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               Bundle extras = intent.getExtras();
               Intent i = new Intent("broadCastName");

               i.putExtra("message", "bitch");

               sendBroadcast(i);

           }
       });



    }
}


