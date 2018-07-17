package com.myx.feng.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.myx.feng.R;

public class BluetoothActivity extends AppCompatActivity {
    int REQUEST_ENABLE=100010;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);

    }

    private void test(){
        BluetoothAdapter mAdapter= BluetoothAdapter.getDefaultAdapter();
        if(!mAdapter.isEnabled()){
//弹出对话框提示用户是后打开
            Intent enabler = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enabler, REQUEST_ENABLE);
//不做提示，强行打开，此方法需要权限&lt;uses-permissionandroid:name="android.permission.BLUETOOTH_ADMIN" /&gt;
// mAdapter.enable();
        }

    }

}
