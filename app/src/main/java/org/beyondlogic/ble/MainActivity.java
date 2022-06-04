package org.beyondlogic.ble;

import static org.beyondlogic.ble.R.*;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ShowAlertDialogs alertDialogs;
    private static final String TAG = "BLEMainActivity";
    private static final int REQUEST_LOCATION_PERMISSION = 1;
    private static final int REQUEST_ENABLE_BLUETOOTH = 2;
    private BluetoothAdapter btAdapter;
    private BluetoothLeScanner bleScanner;
    private DeviceListAdapter deviceListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate");

        setContentView(layout.activity_main);
        alertDialogs = new ShowAlertDialogs(this);

        deviceListAdapter = new DeviceListAdapter(this, layout.device_list_item);
        ListView deviceListView = findViewById(id.deviceListView);
        deviceListView.setOnItemClickListener(deviceListClickListener);
        deviceListView.setAdapter(deviceListAdapter);
        deviceListView.setSmoothScrollbarEnabled(true);

        // Check if app has ACCESS_FINE_LOCATION required for BLE, if not request it.
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getBluetoothAdapter();
        } else {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission for BLE granted, get adapter
                getBluetoothAdapter();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ENABLE_BLUETOOTH && resultCode == MainActivity.RESULT_OK) {
            // Bluetooth Enabled, start scanning
            startBleScan();
        }
    }

    private void getBluetoothAdapter() {
        btAdapter = BluetoothAdapter.getDefaultAdapter();
        if (btAdapter.isEnabled()) {
            startBleScan();
        } else {
            // BLE not enabled, request user to enable
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, REQUEST_ENABLE_BLUETOOTH);
            //registerForActivityResult()
        }
    }

    private ScanCallback bleScanCallback = new ScanCallback() {
        //@Override
        public void onScanResult(int callbackType, ScanResult result) {
            BluetoothDevice device = result.getDevice();
            deviceListAdapter.addDevice(device);                // Add device
            deviceListAdapter.notifyDataSetInvalidated();       // Update List
        }
    };

    private void startBleScan() {
        bleScanner = btAdapter.getBluetoothLeScanner();
        deviceListAdapter.clear();
        List<ScanFilter> scanFilterList = new ArrayList<>();
        //scanFilterList.add(new ScanFilter.Builder().setDeviceName("My Product").build());
        ScanSettings scanSettings = new ScanSettings.Builder().setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY).build();
        bleScanner.startScan(scanFilterList, scanSettings, bleScanCallback);
    }

    // Main Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {

            case id.menu_help:
                return true;

            case id.menu_about:
                alertDialogs.ShowAppAboutDialog(this);
                return true;

            case id.menu_exit:
                alertDialogs.ShowAppExitDialog(this);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private AdapterView.OnItemClickListener deviceListClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            final BluetoothDevice device = deviceListAdapter.getItem(i);
            Intent intent = new Intent(getApplicationContext(), DisplayActivity.class);
            intent.putExtra("ADDR", device.getAddress());
            startActivity(intent);
        }
    };
}