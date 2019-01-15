package com.myx.feng.testlistview;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.myx.feng.R;

/**
 * @author mayuxin
 * @date 2018/8/17
 */

public class TestAdapert2 extends RecyclerView.Adapter {
    private Context context;
    private int cu = 0;

    public  TestAdapert2(Context context) {
        this.context = context;
    }

    @Override
    public TestVV onCreateViewHolder(ViewGroup parent, int viewType) {
        return new TestVV(LayoutInflater.from(context).inflate(R.layout.testitem, null));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final TestVV ssss = (TestVV) holder;
        ssss.itemView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Log.i("xxx","xxxxxx="+position+hasFocus);
                Log.i("xxx","xxxxxx=cu"+cu);

                if(hasFocus){




                    cu = position;
                    ssss.textView.setTextColor(Color.RED);
                    ssss.textView.setTextSize(40);
                }else{
                    ssss.textView.setTextColor(Color.BLACK);
                    ssss.textView.setTextSize(20);
                }

            }
        });

        ssss.textView.setText("test:+" + position);
        ssss.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("xxx","xxxxxxsetOnClickListener="+position);

            }
        });
        if (cu == position) {
            ssss.textView.setTextColor(Color.RED);
            ssss.textView.setTextSize(40);
        } else {
            ssss.textView.setTextColor(Color.BLACK);
            ssss.textView.setTextSize(20);


        }

    }


    @Override
    public int getItemCount() {
        return 10;
    }


    class TestVV extends RecyclerView.ViewHolder {
        private TextView textView;

        public TestVV(View itemView) {
            super(itemView);

            textView = (TextView) itemView.findViewById(R.id.textView2);
        }
    }
}
