package graph.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import appsmaven.graph.com.graph.AddPoints;
import appsmaven.graph.com.graph.GlobalConstant;
import appsmaven.graph.com.graph.R;


public class ServiceActivity extends Service {

    final static int RQS_1 = 1;
    static Integer counter=0;
    private static int notificatn_counter = 0;
    private static int count = 0;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    IBinder mBinder;

    boolean mAllowRebind;


    @Override
    public void onCreate() {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
        {

            // TODO Auto-generated method stub
            preferences = PreferenceManager
                    .getDefaultSharedPreferences(getApplicationContext());
            String time_ = preferences.getString("time", "0");
            String format = preferences.getString("format", "AM");
            String reminder_chk = preferences.getString("reminder_chk", "yes");

            if(reminder_chk.equals("yes")){
                String current_time_ = GlobalConstant.get_current_time();
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm a");
                Date current_dt = null;
                Date save_dt = null;
                if(time_!="0"){
                    Log.e("current_time",current_time_);
                    try {
                        current_dt = sdf.parse(current_time_);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    try {
                        save_dt = sdf.parse(time_);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                    if(current_dt.equals(save_dt)){
                        if(counter.equals(0)){
                            Calendar now = Calendar.getInstance();
                            int a = now.get(Calendar.AM_PM);
                            Log.e("current format",a+"");
                            if(a == Calendar.AM){
                                if(format.equals("AM")){
                                    generatenotification();
                                }
                            }
                            else if(a == Calendar.PM){
                                if(format.equals("PM")){
                                    generatenotification();
                                }
                            }

                        }
                        else{
                            counter++;
                        }

                    }else{
                        counter=0;
                        long delay= 1000*60;
                        Handler h = new Handler();

                        h.postDelayed(new Runnable() {
                            public void run() {
                                Intent myIntent = new Intent(getApplicationContext(), ServiceActivity.class);
                                startService(myIntent);
                            }
                        }, delay);
                    }
                }
            }
        return START_STICKY;
    }

    /**
     * A client is binding to the service with bindService()
     */
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    /**
     * Called when all clients have unbound with unbindService()
     */
    @Override
    public boolean onUnbind(Intent intent) {
        return mAllowRebind;
    }

    /**
     * Called when a client is binding to the service with bindService()
     */
    @Override
    public void onRebind(Intent intent) {

    }

    /**
     * Called when The service is no longer used and is being destroyed
     */
    @Override
    public void onDestroy() {

    }


    private void generatenotification() {

        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

        Intent notificationIntent = null;
        notificationIntent = new Intent(getApplicationContext(), AddPoints.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent intent = PendingIntent.getActivity(getApplicationContext(), 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        if (defaultSound == null) {
            defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
            if (defaultSound == null) {
                defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
            }
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(
                getApplicationContext()).setContentTitle("GraphPlotter").setContentText("Enter your point")
                .setContentIntent(intent).setSmallIcon(R.drawable.app_icon)
                .setLights(Color.MAGENTA, 1, 2).setAutoCancel(true)
                .setSound(defaultSound);
        builder.setNumber(notificatn_counter);
        Notification not = new NotificationCompat.BigTextStyle(builder)
                .bigText("Enter your point").build();

        if (defaultSound == null) {

        } else {
            not.defaults |= Notification.DEFAULT_VIBRATE;
            not.defaults |= Notification.DEFAULT_SOUND;
        }
        builder.setVisibility(View.VISIBLE);
        if(counter==0){
            notificationManager.notify(notificatn_counter, not);
        }
        counter++;
    }

}