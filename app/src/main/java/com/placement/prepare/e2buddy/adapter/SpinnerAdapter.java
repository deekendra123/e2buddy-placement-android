package com.placement.prepare.e2buddy.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.placement.prepare.e2buddy.R;
import com.placement.prepare.e2buddy.object.InternShipDomain;

import java.util.ArrayList;
import java.util.List;

public class SpinnerAdapter extends ArrayAdapter<InternShipDomain> {
    private Context mContext;
    private ArrayList<InternShipDomain> listState;
    private SpinnerAdapter spinnerAdapter;
    private boolean isFromView = false;

    public SpinnerAdapter(Context context, int resource, List<InternShipDomain> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.listState = (ArrayList<InternShipDomain>) objects;
        this.spinnerAdapter = this;
    }

    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {

        final ViewHolder holder;
        if (convertView == null) {
            LayoutInflater layoutInflator = LayoutInflater.from(mContext);
            convertView = layoutInflator.inflate(R.layout.spin_layout, null);
            holder = new ViewHolder();
            holder.mTextView = convertView
                    .findViewById(R.id.text);
            holder.textView =  convertView
                    .findViewById(R.id.checkbox);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.mTextView.setTextColor(Color.BLACK);

        holder.textView.setTextColor(Color.BLACK);

        holder.mTextView.setText(listState.get(position).getDomainName());
        holder.textView.setText(""+listState.get(position).getNumberOfOpening());

        return convertView;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            LayoutInflater layoutInflator = LayoutInflater.from(mContext);
            convertView = layoutInflator.inflate(R.layout.spin_layout, null);
            holder = new ViewHolder();
            holder.mTextView = convertView
                    .findViewById(R.id.text);
            holder.textView =  convertView
                    .findViewById(R.id.checkbox);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.mTextView.setTextColor(Color.WHITE);
        holder.textView.setVisibility(View.GONE);


        holder.mTextView.setText(listState.get(position).getDomainName());


        return convertView;
    }

    private class ViewHolder {
        private TextView mTextView;
        private TextView textView;
    }
}
