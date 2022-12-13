package kr.ac.kumoh.es07.lightautocontrol;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ControlAdapter extends RecyclerView.Adapter<ControlAdapter.ViewHolder>{

    private final ArrayList<Rule> rule;
    private final Context context;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final View mView;
        private ViewHolder(View v) {
            super(v);
            mView = v;
        }
    }

    public ControlAdapter(ArrayList<Rule> rule, Context context) {
        this.rule = rule;
        this.context = context;
    }

    // 새로운 뷰를 생성
    @NonNull
    @Override
    public ControlAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v;
        v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rule_view, parent, false);

        return new ViewHolder(v);
    }

    // 아이템에 내용을 바인드
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        View mView= holder.mView;
        TextView mdiv_s = mView.findViewById(R.id.div_s);
        TextView mStartText = mView.findViewById(R.id.start_time);
        TextView mdiv_e = mView.findViewById(R.id.div_e);
        TextView mEndText = mView.findViewById(R.id.end_time);
        TextView mBright = mView.findViewById(R.id.txt_bright);
        TextView mRuleName = mView.findViewById(R.id.txtItemRuleName);
        mdiv_s.setText(rule.get(position).sTimeAmPm());

        mdiv_e.setText(rule.get(position).eTimeAmPm());
        mStartText.setText(rule.get(position).getSTime12H());
        mEndText.setText(rule.get(position).getETime12H());
        mBright.setText(rule.get(position).getBright());
        mRuleName.setText(rule.get(position).getName());

        Rule tmpRule = rule.get(position);

        // RecyclerView 아이템 이벤트 리스너, 값을 새로운 Activity 전달
        mView.setOnClickListener(view -> {
            Intent intent;
            intent = new Intent(context, RuleChangeActivity.class);

            intent.putExtra("name", tmpRule.getName());
            intent.putExtra("sTime", tmpRule.getSTime24H());
            intent.putExtra("eTime", tmpRule.getETime24H());
            intent.putExtra("bright", tmpRule.getBright());

            ((Activity) context).startActivityForResult(intent, 100 + position);
        });
    }

    @Override
    public int getItemCount() {
        return rule.size();
    }
}
