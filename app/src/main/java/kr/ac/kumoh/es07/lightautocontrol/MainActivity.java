package kr.ac.kumoh.es07.lightautocontrol;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    public final String TAG = "Error";
    public final int PERMISSION_REQUEST_CODE_S = 101;
    public final int PERMISSION_REQUEST_CODE = 100;
    public final int MESSAGE_READ = 1;
    public final int REQUEST_ENABLE_BT = 2;

    static Handler mHandler;

    private BluetoothAdapter mBluetoothAdapter = null;
    public static BluetoothSocket btSocket = null;

    public static ConnectedThread mConnectedThread;

    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    private final ArrayList<String> pairedNames = new ArrayList<>();
    private final ArrayList<String> pairedAddresses = new ArrayList<>();
    private ListView availableList;
    private final ArrayList<String> availableNames = new ArrayList<>();
    private final ArrayList<String> availableAddresses = new ArrayList<>();


    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        blInitialize();
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null)
            Toast.makeText(this, "Bluetooth not found in this device", Toast.LENGTH_SHORT).show();
        else if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        } else {
            showPairedDevices();
            searchAvailableDevices(null);
            if (pairedAddresses.contains("98:D3:71:FD:F7:0E") || availableAddresses.contains("98:D3:71:FD:F7:0E")) {
                connect("98:D3:71:FD:F7:0E");
            }
        }
    }
    private void blInitialize() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                    checkSelfPermission(Manifest.permission.BLUETOOTH_SCAN) != PackageManager.PERMISSION_GRANTED ||
                    checkSelfPermission(Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                requestBlPermissions();
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    requestBlPermissions();
                }
            }
        }
    }

    private void requestBlPermissions() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            requestPermissions(
                    new String[]{
                            Manifest.permission.BLUETOOTH_SCAN,
                            Manifest.permission.BLUETOOTH_CONNECT,
                            Manifest.permission.ACCESS_FINE_LOCATION
                    }, PERMISSION_REQUEST_CODE_S);
        }
        else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(
                        new String[] {Manifest.permission.ACCESS_FINE_LOCATION},
                        PERMISSION_REQUEST_CODE);
            }
        }
    }

    @SuppressLint("MissingPermission")
    private void showPairedDevices() {

         Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();
        ListView pairedList = findViewById(R.id.paired_list);
        if (pairedDevices.size() > 0) {
            for (BluetoothDevice device : pairedDevices) {
                pairedNames.add(device.getName());
                pairedAddresses.add(device.getAddress());
            }
        }
        else {
            TextView noneText = findViewById(R.id.none_text);
            noneText.setVisibility(View.VISIBLE);
            pairedList.setVisibility(View.GONE);
        }
        pairedList.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, pairedNames));
        pairedList.setOnItemClickListener((adapterView, view, i, l) -> connect(pairedAddresses.get(i)));
    }


    @SuppressLint("MissingPermission")
    public void searchAvailableDevices(View view) {
        mBluetoothAdapter.startDiscovery();
        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver, filter);
        availableList = findViewById(R.id.list_found);
        availableList.setOnItemClickListener((adapterView, view1, i, l) -> connect(availableAddresses.get(i)));
    }

    public class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        private ConnectedThread(BluetoothSocket socket) {
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            try {
                tmpIn = socket.getInputStream();
            } catch (IOException e) {
                Log.e(TAG, "Error occurred when creating input stream", e);
            }
            try {
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
                Log.e(TAG, "Error occurred when creating output stream", e);
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            byte[] mmBuffer = new byte[1024];
            int numBytes;

            while (true) {
                try {
                    if (mmInStream.available() > 0) {
                        numBytes = mmInStream.read(mmBuffer);
                        mHandler.obtainMessage(MESSAGE_READ, numBytes, -1, mmBuffer)
                                .sendToTarget();
                    } else SystemClock.sleep(100);
                } catch (IOException e) {
                    break;
                }
            }
        }

        public void write(String message) {
            try {
                byte[] bytes = message.getBytes();
                mmOutStream.write(bytes);
            } catch (IOException e) {
                Log.e(TAG, "Error occurred when sending data", e);
            }
        }

        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
                Log.e(TAG, "Could not close the connect socket", e);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            showPairedDevices();
            searchAvailableDevices(null);
        }
        if (resultCode == RESULT_CANCELED)
            finish();
    }

    @SuppressLint("MissingPermission")
    public void connect(String address) {
        Log.d("Mac", address);
        Toast.makeText(this, "Connecting", Toast.LENGTH_SHORT).show();
        BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(address);
        try {
            btSocket = device.createRfcommSocketToServiceRecord(MY_UUID);
        } catch (IOException e) {
            finish();
        }
        mBluetoothAdapter.cancelDiscovery();
        try {
            btSocket.connect();
        } catch (IOException e) {
            try {
                btSocket.close();
                Toast.makeText(this, "Failed to connect" , Toast.LENGTH_SHORT).show();
            } catch (IOException e2) {
                finish();
            }
        }
        if (btSocket.isConnected()) {
            mConnectedThread = new ConnectedThread(btSocket);
            mConnectedThread.start();
            startActivity(new Intent(this, ControlActivity.class));
            Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show();
        }
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @SuppressLint("MissingPermission")
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                availableNames.add(device.getName());
                availableAddresses.add(device.getAddress());
                availableList.setAdapter(new ArrayAdapter<>(context, android.R.layout.simple_list_item_1, availableNames));
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (MainActivity.mConnectedThread != null) {
            MainActivity.mConnectedThread.cancel();
            try {
                btSocket.close();
            } catch (IOException e2) {
                finish();
            }
        }
        unregisterReceiver(mReceiver);
    }
}