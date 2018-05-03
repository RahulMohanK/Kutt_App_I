package com.example.root.kutt_app_i;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;
import android.widget.Toast;

public class TheService extends Service {



    public final String CHANNEL_ID="my_notification_channel";
    private static final int idUnique=1234;
    DatabaseHelper myDb;

    @Override
    public void onCreate() {
        super.onCreate();



    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        final ClipboardManager clipboard = (ClipboardManager) this.getSystemService(Context.CLIPBOARD_SERVICE);

        clipboard.addPrimaryClipChangedListener( new ClipboardManager.OnPrimaryClipChangedListener() {
            public void onPrimaryClipChanged() {
                String a = clipboard.getText().toString();
                //myDb = new DatabaseHelper(TheService.this);

                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O) {

                    CharSequence name = "Personal notifications ";
                    String descriptions = "Include Personal notifications";
                    int importance = NotificationManager.IMPORTANCE_DEFAULT;


                    NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, name, importance);
                    notificationChannel.setDescription(descriptions);

                    NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    notificationManager.createNotificationChannel(notificationChannel);
                }

                RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.coustomnotification);

                // Set Notification Title
       /* String strtitle = "This is title";
        // Set Notification Text
        String strtext = "This is text";*/

                // Open NotificationView Class on Notification Click
                Intent intent = new Intent(TheService.this, ActionReceiver.class);
                Intent i = new Intent();
                i.setAction(Intent.ACTION_SEND);
                i.putExtra(Intent.EXTRA_TEXT, a);
                i.setType("text/plain");
                // Send data to NotificationView Class
                /*intent.putExtra("title", "1");*/
                //startActivity(intent);
                //intent.putExtra("action","action1");
                // Open NotificationView.java Activity
                PendingIntent pIntent = PendingIntent.getBroadcast(TheService.this, 0, intent,
                        0);
                PendingIntent share = PendingIntent.getActivity(TheService.this, 0, i,
                        0);

                NotificationCompat.Builder builder = new NotificationCompat.Builder(TheService.this,CHANNEL_ID)
                        // Set Icon
                        .setSmallIcon(R.drawable.ic_share_white_24dp)
                        // Set Ticker Message
                        .setTicker("Kutt: do you want to save it")
                        // Dismiss Notification
                        // Set PendingIntent into Notification
                        //.setContentIntent(pIntent)
                        // Set RemoteViews into Notification
                        // .setContentTitle("New data in Clipboard")
                        // .setContentText("Copy??")
                        //.addAction(R.drawable.ic_file_notification,"ADD",pIntent)
                        //.setContentIntent(pIntent)
                        .setAutoCancel(true)
                        .setContent(remoteViews)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);


                remoteViews.setOnClickPendingIntent(R.id.image2,pIntent);
                remoteViews.setOnClickPendingIntent(R.id.image1,share);
                NotificationManager notificationmanager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                // Build Notification with Notification Manager
                notificationmanager.notify(0, builder.build());

                Toast.makeText(getBaseContext(),"Copy:\n"+a,Toast.LENGTH_LONG).show();

                //String text = Clipboard_Utils.getDataFromClipboard(TheService.this);
                /*String[] text1 = a.split(":");
                if (!a.equals("")) {
                    if (text1[0].equals("http") || text1[0].equals("https")) {
                        boolean isInserted = myDb.insertData(a);
                        if (isInserted == true) {
                            Toast.makeText(TheService.this, "Data Inserted ", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(TheService.this, "Not a legal link", Toast.LENGTH_SHORT).show();
                        }
                    } else
                        Toast.makeText(TheService.this, "Clipboard is empty.", Toast.LENGTH_SHORT).show();
                }*/
            }
        });
        Toast.makeText(TheService.this, "Service Started", Toast.LENGTH_SHORT).show();

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
