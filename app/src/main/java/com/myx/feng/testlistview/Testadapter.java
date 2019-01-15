package com.myx.feng.testlistview;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.myx.feng.R;
import com.myx.library.util.ToastUtils;

/**
 * @author mayuxin
 * @date 2018/8/17
 */

public class Testadapter extends BaseAdapter {
    private Context context;
    private int cuF = 0;

    public Testadapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return 10;
    }

    @Override
    public Object getItem(int position) {
        return "test";
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.listitem, null);
            holder.textView = (TextView) convertView.findViewById(R.id.textView3);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.textView.setText("test+" + position);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.showShort(context, "xxxx+" + position);
            }
        });

        convertView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    cuF = position;
                    Log.i("xxx","xxxccccccccccu="+cuF);
//                    ToastUtils.showShort(context, "xx==" + position);
//                    notifyDataSetChanged();

                    holder.textView.setTextColor(Color.RED);
                } else {
                    holder.textView.setTextColor(Color.BLACK);

                }
            }
        });
        Log.i("xxx","xxxcu="+cuF);
        Log.i("xxx","xxxpos="+position);
//        if (cuF==position) {
//            holder.textView.setTextColor(Color.RED);
//
//        } else {
//
//            holder.textView.setTextColor(Color.BLACK);
//
//        }
        return convertView;
    }

    class ViewHolder {
        TextView textView;

    }

}
