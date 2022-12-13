package kr.ac.kumoh.es07.lightautocontrol;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

public class ControlActivity extends AppCompatActivity {
    public final int MESSAGE_READ = 1;
    ArrayList<Rule> rule = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private SeekBar mBrightSeekBar;
    private int first_flag = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_control);

        mRecyclerView = findViewById(R.id.rule_list);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new ControlAdapter(rule, this);
        mRecyclerView.setAdapter(mAdapter);
        mBrightSeekBar = findViewById(R.id.control_light);
//        MainActivity.mConnectedThread.write("12330");

        mBrightSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                Log.d("Send", String.format("%d", 10 + i * 5));
//                MainActivity.mConnectedThread.write(String.format("5 %d\0", i * 5));
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        rule.add(new Rule("20300630095"));
        mAdapter.notifyDataSetChanged();
        Log.d("VAdapter", ""+mAdapter.getItemCount());

//        MainActivity.mConnectedThread.write("12330");

//        MainActivity.mHandler = new Handler() {
//            public void handleMessage(android.os.Message msg) {
//                switch (msg.what) {
//                    case MESSAGE_READ:
//                        byte[] readBuf = (byte[]) msg.obj;
//                        String readString = new String(readBuf, 0, msg.arg1);
//
//                        if (readString.length() >= 2) {
//                            Log.d("Receive", readString);
//                            if (readString.substring(0, 2).equals("RL")) {
//                                if (readString.substring(2).length() >= 11)
//                                rule.add(new Rule(readString.substring(2)));
//                                mAdapter.notifyDataSetChanged();
//                                first_flag = 0;
//                                mRecyclerView.scrollToPosition(rule.size() - 1);
//                                Log.d("Receive", "OK");
//                                Log.d("ma", "" + mAdapter.getItemCount());
//                            }
//                        }
////                        rule.add(new Rule(readString));
////                        mAdapter.notifyDataSetChanged();
////                        mRecyclerView.scrollToPosition(chat.size() - 1);
//                        break;
//                }
//            }
//        };

    }
//    public void sendMessage(View view) {
//        EditText editText = findViewById(R.id.text_edit);
//        String message = editText.getText().toString();
//        MainActivity.mConnectedThread.write(message);
//        editText.setText(null);
//        chat.add(new Message(message, 0));
//        mAdapter.notifyDataSetChanged();
//        mRecyclerView.scrollToPosition(chat.size() - 1);
//
//    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (MainActivity.mConnectedThread != null) {
            MainActivity.mConnectedThread.cancel();
            try {
                MainActivity.btSocket.close();
            } catch (IOException e2) {
                finish();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode/100 == 1) {
            int pos = requestCode % 100;
            if (resultCode == RESULT_OK) {
                Log.d("ACTRES", ""+data.getExtras().getString("SetSTime"));

            }
        }
    }
}


//    public final int MESSAGE_READ = 1;
//
//    ArrayList<Message> chat = new ArrayList<>();
//    private RecyclerView mRecyclerView;
//    private RecyclerView.Adapter mAdapter;
//    private RecyclerView.LayoutManager mLayoutManager;
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        setContentView(R.layout.activity_control);
//
//        mRecyclerView = findViewById(R.id.my_recycler_view);
//        mRecyclerView.setHasFixedSize(true);
//        mLayoutManager = new LinearLayoutManager(this);
//        mRecyclerView.setLayoutManager(mLayoutManager);
//        mAdapter = new ChatAdapter(chat);
//        mRecyclerView.setAdapter(mAdapter);
//
//        MainActivity.mHandler = new Handler() {
//            public void handleMessage(android.os.Message msg) {
//                switch (msg.what) {
//                    case MESSAGE_READ:
//                        byte[] readBuf = (byte[]) msg.obj;
//                        String readString = new String(readBuf, 0, msg.arg1);
//                        chat.add(new Message(readString, 1));
//                        mAdapter.notifyDataSetChanged();
//                        mRecyclerView.scrollToPosition(chat.size() - 1);
//                        break;
//                }
//            }
//        };
//    }
//
//    public void sendMessage(View view) {
//        EditText editText = findViewById(R.id.text_edit);
//        String message = editText.getText().toString();
//        MainActivity.mConnectedThread.write(message);
//        editText.setText(null);
//        chat.add(new Message(message, 0));
//        mAdapter.notifyDataSetChanged();
//        mRecyclerView.scrollToPosition(chat.size() - 1);
//
//    }
//
//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        if (MainActivity.mConnectedThread != null) {
//            MainActivity.mConnectedThread.cancel();
//            try {
//                MainActivity.btSocket.close();
//            } catch (IOException e2) {
//                finish();
//            }
//        }
//    }

//}