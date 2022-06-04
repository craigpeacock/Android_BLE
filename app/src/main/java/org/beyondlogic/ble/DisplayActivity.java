package org.beyondlogic.ble;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class DisplayActivity extends AppCompatActivity {

    private TextView tvDeviceAddress;
    private Button bOKButton;
    private Intent intent;
    private BluetoothAdapter btAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        tvDeviceAddress = findViewById(R.id.tvDeviceAddress);
        bOKButton = findViewById(R.id.bOKButton);
        bOKButton.setOnClickListener(clickListener);

        intent = getIntent();
        String bleaddr = intent.getStringExtra("ADDR");
        tvDeviceAddress.setText(bleaddr);

        btAdapter = BluetoothAdapter.getDefaultAdapter();
        if (btAdapter.isEnabled()) {
            BluetoothDevice btDevice = btAdapter.getRemoteDevice(bleaddr);
        }
    }

    private View.OnClickListener clickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            //intent.putExtra("Example", "Hello World");
            setResult(Activity.RESULT_OK, intent);
            finish();
        }
    };
}