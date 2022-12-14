package kr.ac.kumoh.es07.lightautocontrol;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import java.io.IOException;
import java.util.ArrayList;

public class ControlActivity extends AppCompatActivity {
    public final int MESSAGE_READ = 1;
    ArrayList<Rule> rule = new ArrayList<>();
    private ControlAdapter mAdapter;
    private int first_flag = 1;

    @SuppressLint("HandlerLeak")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MainActivity.mConnectedThread.write("9");

        setContentView(R.layout.activity_control);

        RecyclerView mRecyclerView = findViewById(R.id.rule_list);
        mRecyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new ControlAdapter(rule, this);
        mRecyclerView.setAdapter(mAdapter);

        MainActivity.mHandler = new Handler() {
            @SuppressLint("NotifyDataSetChanged")
            public void handleMessage(android.os.Message msg) {
                switch (msg.what) {
                    case MESSAGE_READ:
                        byte[] readBuf = (byte[]) msg.obj;
                        String readString = new String(readBuf, 0, msg.arg1);

                        if (readString.length() >= 2) {
                            if (readString.startsWith("##")) {
                                if (readString.substring(2).length() >= 11) {
                                    if (first_flag == 1) {
                                        rule.add(new Rule(readString.substring(2)));
                                        mAdapter.notifyDataSetChanged();
                                        first_flag = 0;
                                    }
                                    else {
                                        rule.set(0, new Rule(readString.substring(2)));
                                        mAdapter.notifyDataSetChanged();
                                    }
                                }
                            }
                        }
                }
            }
        };
    }

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
                Rule tmpRule = rule.get(pos);
                assert data != null;
                tmpRule.modifySTime(data.getExtras().getString("setSTime"));
                tmpRule.modifyETime(data.getExtras().getString("setETime"));
                tmpRule.modifyName(data.getExtras().getString("ruleName"));
                tmpRule.modifyBright(data.getExtras().getString("setBright"));
                rule.set(pos, tmpRule);
                mAdapter.notifyItemChanged(pos);

                MainActivity.mConnectedThread.write(tmpRule.getData());
            }
        }
    }
}