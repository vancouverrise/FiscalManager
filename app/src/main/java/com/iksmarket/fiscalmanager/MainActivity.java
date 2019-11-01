package com.iksmarket.fiscalmanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.iksmarket.fiscalmanager.Bluetooth.BluetoothService;
import com.iksmarket.fiscalmanager.Bluetooth.ResponseFromPrinter;

import java.util.BitSet;

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
            fromByte((byte)1);
                System.out.println("Смотри сюда: " + fromByte((byte)1).toString().trim().replace("{", "").replace("}", ""));
            }
        });



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
               // Data you need to pass to activity
               i.putExtra("message", "bitch");

               sendBroadcast(i);
           }
       });



    }

    public BitSet fromByte(byte b)
    {
        BitSet bits = new BitSet(8);
        for (int i = 0; i < 8; i++)
        {
            bits.set(i, (b & 1) == 1);
            b >>= 1;
        }
        return bits;
    }




}


