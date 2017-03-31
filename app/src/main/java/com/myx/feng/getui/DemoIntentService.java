package com.myx.feng.getui;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.igexin.sdk.GTIntentService;
import com.igexin.sdk.PushConsts;
import com.igexin.sdk.PushManager;
import com.igexin.sdk.Tag;
import com.igexin.sdk.message.FeedbackCmdMessage;
import com.igexin.sdk.message.GTCmdMessage;
import com.igexin.sdk.message.GTTransmitMessage;
import com.igexin.sdk.message.SetTagCmdMessage;
import com.myx.feng.R;

import java.util.HashMap;
import java.util.Random;

/**
 * 继承 GTIntentService 接收来自个推的消息, 所有消息在线程中回调, 如果注册了该服务, 则务必要在 AndroidManifest中声明, 否则无法接受消息<br>
 * onReceiveMessageData 处理透传消息<br>
 * onReceiveClientId 接收 cid <br>
 * onReceiveOnlineState cid 离线上线通知 <br>
 * onReceiveCommandResult 各种事件处理回执 <br>
 */
public class DemoIntentService extends GTIntentService {

    private static final String TAG = "GetuiSdkDemo";
    public DemoIntentService() {

    }

    @Override
    public void onReceiveServicePid(Context context, int pid) {
    }

    @Override
    public void onReceiveMessageData(Context context, GTTransmitMessage msg) {
        byte[] payload = msg.getPayload();
        String pkg = msg.getPkgName();
        if (payload != null) {
            String data = new String(payload);
            sendMessage(context, data);
        }
    }

    @Override
    public void onReceiveClientId(Context context, String clientid) {
        Log.e(TAG, "onReceiveClientId -> " + "clientid = " + clientid);

    }

    @Override
    public void onReceiveOnlineState(Context context, boolean online) {
        Log.e(TAG, "onReceiveOnlineState -> " + (online ? "online" : "offline"));
    }

    @Override
    public void onReceiveCommandResult(Context context, GTCmdMessage cmdMessage) {
    }


    private void sendMessage(Context context, String data) {
//        Message msg = Message.obtain();
//        msg.what = what;
//        msg.obj = data;
//        DemoApplication.sendMessage(msg);
        Log.i(TAG, data);
        Intent intent = new Intent(context, Main2Activity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        Bitmap btm = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
        PendingIntent notificationPendingIt = PendingIntent.getActivity(context, new Random().nextInt(1000), intent, 0);


//        RemoteViews mRemoteViews = new RemoteViews(context.getPackageName(), R.layout.notifation_layout);
//        mRemoteViews.setTextViewText(R.id.title, title);
//        mRemoteViews.setTextViewText(R.id.desc, description);
//        mRemoteViews.setImageViewResource(R.id.icon, R.drawable.ic_launcher);
//        mRemoteViews.setTextViewText(R.id.time, getTime());


        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification n = new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(btm)
                .setTicker(data + "=Ticker")
                .setContentTitle(data + "=Title")
                .setContentText(data + "=Text")
                .setContentIntent(notificationPendingIt).build();
        n.flags |= Notification.FLAG_AUTO_CANCEL;
        n.defaults |= Notification.DEFAULT_SOUND;
        n.defaults |= Notification.DEFAULT_VIBRATE;
        int nextInt = new Random().nextInt(10000);
        notificationManager.notify(nextInt, n);
    }
}
