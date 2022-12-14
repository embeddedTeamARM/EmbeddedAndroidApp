package kr.ac.kumoh.es07.lightautocontrol;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

public class RuleChangeActivity extends AppCompatActivity {
    private SeekBar mSetBrightValue;
    private TextView mSetBrightText;
    private TextView mSetStartHour;
    private TextView mSetStartMin;
    private TextView mSetEndHour;
    private TextView mSetEndMin;
    private EditText mSetRuleName;


    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rule_change);
        Intent intent = getIntent();
        String sTime = intent.getStringExtra("sTime");
        String eTime = intent.getStringExtra("eTime");
        String bright = intent.getStringExtra("bright");
        String rule_name = intent.getStringExtra("name");

        mSetStartHour = findViewById(R.id.crStartHour);
        mSetStartMin = findViewById(R.id.crStartMin);
        mSetEndHour = findViewById(R.id.crEndHour);
        mSetEndMin = findViewById(R.id.crEndMin);
        mSetBrightValue = findViewById(R.id.brightSetSeekbar);
        mSetBrightText = findViewById(R.id.txtSetBright);
        mSetRuleName = findViewById(R.id.rule_name);
        Button mSaveBtn = findViewById(R.id.btnSetRuleSav);
        Button mCanBtn = findViewById(R.id.btnSetRuleCan);

        mSetBrightValue.setProgress(((Integer.parseInt(bright)-10) / 5));
        mSetBrightText.setText(String.format("%d", 10 + mSetBrightValue.getProgress() * 5));

        mSetStartHour.setText(sTime.split(":")[0]);
        mSetStartMin.setText(sTime.split(":")[1]);
        mSetEndHour.setText(eTime.split(":")[0]);
        mSetEndMin.setText(eTime.split(":")[1]);
        mSetRuleName.setText(rule_name);
        Log.d("VALUE", sTime+" "+eTime);

        // 규칙수정 EditText 내용변경 이벤트 리스너
        // 잘못된 입력값 방지 및 포커스 자동 이동
        mSetStartHour.addTextChangedListener(new TextWatcher() {
            CharSequence beforeChar;
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                beforeChar = charSequence;
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (beforeChar.length() > 1) {
                    mSetStartMin.requestFocus();
                    if (Integer.parseInt(charSequence.toString()) > 23)
                        mSetStartHour.setText("23");
                }
                if (Integer.parseInt(charSequence.toString()) > 2)
                    mSetStartMin.requestFocus();
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        mSetStartMin.addTextChangedListener(new TextWatcher() {
            CharSequence beforeChar;
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                beforeChar = charSequence;
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (beforeChar.length() > 1) {
                    mSetEndHour.requestFocus();
                    if (Integer.parseInt(charSequence.toString()) > 59)
                        mSetStartMin.setText("59");
                }
                if (Integer.parseInt(charSequence.toString()) > 5)
                    mSetEndHour.requestFocus();
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        mSetEndHour.addTextChangedListener(new TextWatcher() {
            CharSequence beforeChar;
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                beforeChar = charSequence;
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (beforeChar.length() > 1) {
                    mSetEndMin.requestFocus();
                    if (Integer.parseInt(charSequence.toString()) > 23)
                        mSetEndHour.setText("23");
                }
                if (Integer.parseInt(charSequence.toString()) > 2)
                    mSetEndMin.requestFocus();
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });
        mSetEndMin.addTextChangedListener(new TextWatcher() {
            CharSequence beforeChar;
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                beforeChar = charSequence;
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (beforeChar.length() > 1) {
                    if (Integer.parseInt(charSequence.toString()) > 59) {
                        mSetEndMin.setText("59");
                        mSetEndMin.requestFocus();
                    }
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        // 저장 버튼 클릭시 이벤트 리스너 => 결과값을 가지고 Activity 종료 => ControlActivity로 전달
        mSaveBtn.setOnClickListener(view -> {
            Intent intentR = new Intent();
            intentR.putExtra("ruleName", mSetRuleName.getText().toString());
            intentR.putExtra("setSTime", mSetStartHour.getText().toString()+":"+mSetStartMin.getText().toString());
            intentR.putExtra("setETime", mSetEndHour.getText().toString()+":"+mSetEndMin.getText().toString());
            intentR.putExtra("setBright", String.format("%d", 10 + mSetBrightValue.getProgress()*5));
            setResult(RESULT_OK, intentR);
            finish();
        });

        // 취소 버튼 클릭시 이벤트 리스너
        mCanBtn.setOnClickListener(view -> {
            setResult(RESULT_CANCELED);
            finish();
        });

        // 탐색바 변경 시 밝기 값 텍스트 변경을 위한 이벤트 리스너
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