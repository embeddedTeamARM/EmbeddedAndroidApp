package kr.ac.kumoh.es07.lightautocontrol;//package kr.ac.kumoh.es07.lightautocontrol;
//
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import androidx.recyclerview.widget.RecyclerView;
//
//import java.util.ArrayList;
//

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import kr.ac.kumoh.es07.lightautocontrol.R;
import kr.ac.kumoh.es07.lightautocontrol.Rule;

public class ControlAdapter extends RecyclerView.Adapter<ControlAdapter.ViewHolder>{

    private ArrayList<Rule> rule;
    private Context context;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        private View mView;
        private ViewHolder(View v) {
            super(v);
            mView = v;
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public ControlAdapter(ArrayList<Rule> rule, Context context) {
        this.rule = rule;
        this.context = context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public ControlAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        // create a new view
        View v;
        v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rule_view, parent, false);

        // set the view's size, margins, paddings and layout parameters
        return new ViewHolder(v);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        View mView= holder.mView;
        TextView mdiv_s = mView.findViewById(R.id.div_s);
        TextView mStartText = mView.findViewById(R.id.start_time);
        TextView mdiv_e = mView.findViewById(R.id.div_e);
        TextView mEndText = mView.findViewById(R.id.end_time);
        TextView mBright = mView.findViewById(R.id.txt_bright);
        Switch mRuleName = mView.findViewById(R.id.sw_use);

        mdiv_s.setText(rule.get(position).ampmSTime());
        mdiv_e.setText(rule.get(position).ampmETime());
        mStartText.setText(rule.get(position).getSTime12H());
        mEndText.setText(rule.get(position).getETime12H());
        mBright.setText(rule.get(position).getBright());
        mRuleName.setText(rule.get(position).getName());

        mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                String STime_ampm = mdiv_s.getText().toString();
//                String ETime_ampm = mdiv_e.getText().toString();
//                String STime = mStartText.getText().toString();
//                String ETime = mEndText.getText().toString();
//                String Bright = mBright.getText().toString();

                Intent intent;
                intent = new Intent(context, RuleChangeActivity.class);

                intent.putExtra("name", rule.get(position).getName());
                intent.putExtra("Stime", rule.get(position).getSTime24H());
                intent.putExtra("Etime", rule.get(position).getETime24H());
                intent.putExtra("bright", rule.get(position).getBright());

//                context.startActivity(intent);
                ((Activity) context).startActivityForResult(intent, 100+position);

            }
        });
    }

    public void modify() {

    }
    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return rule.size();
    }
}
//public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder>{
//
//    private ArrayList<Message> chat;
//
//    // Provide a reference to the views for each data item
//    // Complex data items may need more than one view per item, and
//    // you provide access to all the views for a data item in a view holder
//    public static class ViewHolder extends RecyclerView.ViewHolder {
//        // each data item is just a string in this case
//        private View mView;
//        private ViewHolder(View v) {
//            super(v);
//            mView = v;
//        }
//    }
//
//    // Provide a suitable constructor (depends on the kind of dataset)
//    public ChatAdapter(ArrayList<Message> chat) {
//        this.chat = chat;
//    }
//
//    // Create new views (invoked by the layout manager)
//    @Override
//    public ChatAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
//                                                     int viewType) {
//        // create a new view
//        View v;
//        if (viewType == 0)
//            v = LayoutInflater.from(parent.getContext())
//                    .inflate(R.layout.text_view_sent, parent, false);
//        else
//            v = LayoutInflater.from(parent.getContext())
//                    .inflate(R.layout.text_view_received, parent, false);
//
//        // set the view's size, margins, paddings and layout parameters
//        return new ViewHolder(v);
//    }
//
//    // Replace the contents of a view (invoked by the layout manager)
//    @Override
//    public void onBindViewHolder(ViewHolder holder, int position) {
//        // - get element from your dataset at this position
//        // - replace the contents of the view with that element
//        View mView= holder.mView;
//        TextView mTextView = mView.findViewById(R.id.textview);
//        mTextView.setText(chat.get(position).getMessage());
//    }
//
//    // Return the size of your dataset (invoked by the layout manager)
//    @Override
//    public int getItemCount() {
//        return chat.size();
//    }
//
//    @Override
//    public int getItemViewType(int position) {
//        return chat.get(position)