package com.example.telebeetle.cometchatapi;


import android.Manifest;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.telecom.Call;
import android.util.Log;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;


import com.cometchat.chat.constants.CometChatConstants;
import com.cometchat.chat.helpers.CometChatHelper;
import com.cometchat.chat.models.BaseMessage;
import com.cometchat.chat.models.Group;
import com.cometchat.chat.models.MediaMessage;
import com.example.telebeetle.R;
import com.example.telebeetle.activities.GeneralViewActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

//import constant.StringContract;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseService";
    private JSONObject json;
    private Intent intent;
    private int count = 0;
    private Call call;
    public static String token;
    private static final int REQUEST_CODE = 12;

    private boolean isCall;

    @Override
    public void onNewToken(String s) {
        token = s;
        Log.d(TAG, "onNewToken: " + s);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        try {
            count++;
            json = new JSONObject(remoteMessage.getData());
            Log.d(TAG, "JSONObject: " + json.toString());
            JSONObject messageData = new JSONObject(json.getString("message"));
            //MediaMessage mediaMessage = (MediaMessage) CometChatHelper.processMessage(new JSONObject(remoteMessage.getData().get("message")));
            BaseMessage baseMessage = CometChatHelper.processMessage(new JSONObject(remoteMessage.getData().get("message")));
            /*if (baseMessage instanceof Call) {
                call = (Call) baseMessage;
                isCall = true;
            }*/
            showNotifcation(baseMessage);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Bitmap getBitmapFromURL(String strURL) {
        if (strURL != null) {
            try {
                URL url = new URL(strURL);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                return myBitmap;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }

    private void showNotifcation(BaseMessage baseMessage) {

        try {
            int m = (int) ((new Date().getTime()));
            String GROUP_ID = "group_id";

            Intent messageIntent = new Intent(getApplicationContext(),
                    GeneralViewActivity.class);
            // Agregar información sobre el fragmento al Intent
            messageIntent.putExtra("FRAGMENT_TAG", "chatFragment");
            if (baseMessage.getReceiverType().equals(CometChatConstants.RECEIVER_TYPE_GROUP)) {
                //messageIntent.putExtra("guid", (Serializable) baseMessage.getReceiver());
                messageIntent.putExtra("guid",  ((Group)baseMessage.getReceiver()).getGuid());
                messageIntent.putExtra("name",
                        ((Group)baseMessage.getReceiver()).getName());
                messageIntent.putExtra("group_description",
                        ((Group) baseMessage.getReceiver()).getDescription());
                messageIntent.putExtra("group_type",
                        ((Group) baseMessage.getReceiver()).getGroupType());
                messageIntent.putExtra("group_owner",
                        ((Group) baseMessage.getReceiver()).getOwner());
                messageIntent.putExtra("member_count",
                        ((Group) baseMessage.getReceiver()).getMembersCount());
            }


            /*messageIntent.putExtra("type",baseMessage.getReceiverType());
            if (baseMessage.getReceiverType().equals(CometChatConstants.RECEIVER_TYPE_USER)) {

                messageIntent.putExtra("name",
                        baseMessage.getSender().getName());
                messageIntent.putExtra("uid",
                        baseMessage.getSender().getUid());
                messageIntent.putExtra("avatar",
                        baseMessage.getSender().getAvatar());
                messageIntent.putExtra(UIKitConstants.IntentStrings.STATUS,
                        baseMessage.getSender().getStatus());

            } else if (baseMessage.getReceiverType().equals(CometChatConstants.RECEIVER_TYPE_GROUP)) {

                messageIntent.putExtra(UIKitConstants.IntentStrings.GUID,
                        ((Group)baseMessage.getReceiver()).getGuid());
                messageIntent.putExtra(UIKitConstants.IntentStrings.NAME,
                        ((Group)baseMessage.getReceiver()).getName());
                messageIntent.putExtra(UIKitConstants.IntentStrings.GROUP_DESC,
                        ((Group) baseMessage.getReceiver()).getDescription());
                messageIntent.putExtra(UIKitConstants.IntentStrings.GROUP_TYPE,
                        ((Group) baseMessage.getReceiver()).getGroupType());
                messageIntent.putExtra(UIKitConstants.IntentStrings.GROUP_OWNER,
                        ((Group) baseMessage.getReceiver()).getOwner());
                messageIntent.putExtra(UIKitConstants.IntentStrings.MEMBER_COUNT,
                        ((Group) baseMessage.getReceiver()).getMembersCount());
            }*/

            int requestID = (int) System.currentTimeMillis();
            //messageIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

            PendingIntent messagePendingIntent = PendingIntent.getActivity(this,
                    0123,messageIntent,PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE );//| PendingIntent.FLAG_IMMUTABLE







            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "2")
                    .setSmallIcon(R.drawable.telito)
                    .setContentTitle(json.getString("title"))
                    .setContentText(json.getString("alert"))
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setColor(getResources().getColor(R.color.colorPrimary))
                    .setLargeIcon(getBitmapFromURL(baseMessage.getSender().getAvatar()))
                    .setGroup(GROUP_ID)
                    .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                    .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));

            //Show image incase of media message
            if (baseMessage.getType().equals(CometChatConstants.MESSAGE_TYPE_IMAGE)) {
                builder.setStyle(new NotificationCompat.BigPictureStyle()
                        .bigPicture(getBitmapFromURL(((MediaMessage)baseMessage).getAttachment()
                                .getFileUrl())));
            }

            NotificationCompat.Builder summaryBuilder = new NotificationCompat.Builder(this, "2")
                    .setContentTitle("CometChat")
                    .setContentText(count + " messages")
                    .setSmallIcon(R.drawable.telito)
                    .setGroup(GROUP_ID)
                    .setGroupSummary(true);
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

            /*if (isCall) {
                builder.setGroup(GROUP_ID + "Call");
                if (json.getString("alert").equals("Incoming audio call") ||
                        json.getString("alert").equals("Incoming video call")) {
                    builder.setOngoing(true);
                    builder.setPriority(NotificationCompat.PRIORITY_HIGH);
                    builder.setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE));
                    builder.addAction(0, "Answers",
                            PendingIntent.getBroadcast(getApplicationContext(),
                                    REQUEST_CODE,
                                    getCallIntent("Answers"),
                                    PendingIntent.FLAG_UPDATE_CURRENT));
                    builder.addAction(0, "Decline",
                            PendingIntent.getBroadcast(getApplicationContext(), 1,
                                    getCallIntent("Decline"),
                                    PendingIntent.FLAG_UPDATE_CURRENT));
                }
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                notificationManager.notify(05, builder.build());
            }
            else {
                notificationManager.notify(baseMessage.getId(), builder.build());
                notificationManager.notify(0, summaryBuilder.build());
            }*/
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            builder.setContentIntent(messagePendingIntent);
            builder.setAutoCancel(true);
            notificationManager.notify(baseMessage.getId(), builder.build());
            notificationManager.notify(0, summaryBuilder.build());
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

   /* private Intent getCallIntent(String title){
        Intent callIntent = new Intent(getApplicationContext(), CallNotificationAction.class);
        callIntent.putExtra(StringContract.IntentStrings.SESSION_ID,call.getSessionId());
        callIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        callIntent.setAction(title);
        return callIntent;
    }*/
}
