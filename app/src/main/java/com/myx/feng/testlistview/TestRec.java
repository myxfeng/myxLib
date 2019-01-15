package com.myx.feng.testlistview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.myx.feng.R;
import com.myx.feng.recycle.LinearLayoutManagerTV;

public class TestRec extends AppCompatActivity {
    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_rec);
        recyclerView= (RecyclerView) findViewById(R.id.mRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManagerTV(this));
        recyclerView.setAdapter(new TestAdapert2(this));
    }
}
