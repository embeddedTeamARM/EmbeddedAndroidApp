package kr.ac.kumoh.es07.lightautocontrol;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import java.io.IOException;

public class RuleChangeActivity extends AppCompatActivity {
    private Intent intent;
    private SeekBar mSetBrightValue;
    private TextView mSetBrightText;
    private TextView mSetStartHour;
    private TextView mSetStartMin;
    private TextView mSetEndHour;
    private TextView mSetEndMin;
    private EditText mSetRuleName;
    private Button mSaveBtn;
    private Button mCanBtn;

    private String Stime;
    private String Etime;
    private String bright;
    private String rule_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rule_change);
        intent = getIntent();
        Stime = intent.getStringExtra("Stime");
        Etime = intent.getStringExtra("Etime");
        bright = intent.getStringExtra("bright");
        rule_name = intent.getStringExtra("name");

        mSetStartHour = findViewById(R.id.crStartHour);
        mSetStartMin = findViewById(R.id.crStartMin);
        mSetEndHour = findViewById(R.id.crEndHour);
        mSetEndMin = findViewById(R.id.crEndMin);
        mSetBrightValue = findViewById(R.id.brightSetSeekbar);
        mSetBrightText = findViewById(R.id.txtSetBright);
        mSetRuleName = findViewById(R.id.rule_name);
        mSaveBtn = findViewById(R.id.btnSetRuleSav);
        mCanBtn = findViewById(R.id.btnSetRuleCan);

        mSetBrightValue.setProgress(((Integer.parseInt(bright)-10) / 5));
        mSetBrightText.setText(""+(10 + mSetBrightValue.getProgress() * 5));

        mSetStartHour.setText(Stime.split(":")[0]);
        mSetStartMin.setText(Stime.split(":")[1]);
        mSetEndHour.setText(Etime.split(":")[0]);
        mSetEndMin.setText(Etime.split(":")[1]);
        mSetRuleName.setText(rule_name);

        mSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentR = new Intent();
                intentR.putExtra("SetSTime", mSetStartHour.getText().toString()+":"+mSetStartMin.getText().toString());
                intentR.putExtra("SetSEime", mSetEndHour.getText().toString()+":"+mSetEndMin.getText().toString());
                intentR.putExtra("SetBright", String.format("%d", 10+mSetBrightValue.getProgress()*5));
                setResult(RESULT_OK, intentR);
                finish();
            }
        });
        mCanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });

        mSetBrightValue.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                mSetBrightText.setText(String.format("%d", 10 + i * 5));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }
    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        super.onBackPressed();
    }
}