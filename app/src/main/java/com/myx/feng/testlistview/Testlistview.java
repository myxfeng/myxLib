package com.myx.feng.testlistview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.myx.feng.R;
import com.myx.library.util.ToastUtils;

public class Testlistview extends AppCompatActivity {
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testlistview);
        listView = (ListView) findViewById(R.id.list);
        listView.setAdapter(new Testadapter(this));

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ToastUtils.showShort(Testlistview.this,"setOnItemClickListener+"+position);
            }
        });
        listView.setItemsCanFocus(true);
        listView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                Log.i("xxx","xxxxxonFocusChange="+hasFocus);
            }
        });

    }

    public void test(View view){

    }
}
