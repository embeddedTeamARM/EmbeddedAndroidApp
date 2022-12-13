package kr.ac.kumoh.es07.lightautocontrol;

import android.util.Log;

public class Rule {
    private int time_start_h;
    private int time_start_m;
    private int time_end_h;
    private int time_end_m;
    private int bright;
    private String rule_name;

    public Rule (String Receive_s) {
        Log.d("Receive", Receive_s);
        this.time_start_h = Integer.parseInt(Receive_s.substring(0,2));
        this.time_start_m = Integer.parseInt(Receive_s.substring(2,4));
        this.time_end_h = Integer.parseInt(Receive_s.substring(4,6));
        this.time_end_m = Integer.parseInt(Receive_s.substring(6,8));
        this.bright = Integer.parseInt(Receive_s.substring(8,11));
        this.rule_name = "";
    }

    public String getSTime12H() {
        if (time_start_h%12 == 0)
            return String.format("%d:%d",12, time_start_m);
        else
            return String.format("%d:%d",time_start_h%12, time_start_m);
    }
    public String getETime12H() {
        if (time_end_h%12 == 0)
            return String.format("%d:%d", 12, time_end_m);
        else
            return String.format("%d:%d", time_end_h%12, time_end_m);
    }
    public String ampmSTime() {
        String ampm;
        if (time_start_h < 12)
            ampm = "오전";
        else
            ampm = "오후";
        return ampm;
    }
    public String ampmETime() {
        String ampm;
        if (time_end_h < 12)
            ampm = "오전";
        else
            ampm = "오후";
        return ampm;
    }
    public String getSTime24H() {
        return String.format("%d:%d",time_start_h, time_start_m);
    }
    public String getETime24H() {
        return String.format("%d:%d", time_end_h, time_end_m);
    }
    public String getBright() {
        return Integer.toString(bright);
    }
    public String getData() {
        return String.format("%02%02d%02d%02d%03d", time_start_h, time_start_m, time_end_h, time_end_m, bright);
    }
    public String getName() {
        return rule_name;
    }
}
