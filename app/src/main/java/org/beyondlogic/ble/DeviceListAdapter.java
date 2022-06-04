package org.beyondlogic.ble;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class DeviceListAdapter extends ArrayAdapter<BluetoothDevice> {

    private ArrayList<BluetoothDevice> btDevices;
    private int layoutResourceId;
    private Context context;

    public DeviceListAdapter(Context context, int resource) {
        super(context, resource);
        this.layoutResourceId = resource;
        this.context = context;
        btDevices = new ArrayList<>();
    }

    public void addDevice(BluetoothDevice device) {
        if (!btDevices.contains(device)) {
            btDevices.add(device);
        }
    }

    public void clear() {
        btDevices.clear();
    }

    @Override
    public int getCount() {
        return btDevices.size();
    }

    @Override
    public BluetoothDevice getItem(int i) {
        return btDevices.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parentView) {
        if (convertView == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            convertView = inflater.inflate(layoutResourceId, parentView, false);
        }
        BluetoothDevice device = btDevices.get(position);

        TextView tvDeviceName = convertView.findViewById(R.id.tvDeviceName);
        TextView tvDeviceAddr = convertView.findViewById(R.id.tvDeviceAddr);

        tvDeviceName.setText(device.getName());
        tvDeviceAddr.setText(device.getAddress());

        return convertView;
    }
}
