package com.abs.samih.fcm_ex01.telephony;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.widget.Toast;

import com.abs.samih.fcm_ex01.AddTaskActivity;
import com.abs.samih.fcm_ex01.MngTaskActivity;
import com.abs.samih.fcm_ex01.R;
import com.abs.samih.fcm_ex01.data.MyTask;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Date;

public class MySMSReceiver extends BroadcastReceiver {
    public MySMSReceiver() {
    }

    @Override
    public void onReceive(final Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
       // throw new UnsupportedOperationException("Not yet implemented");
        //Toast.makeText(context,"HIIIIHIHIHIHI",Toast.LENGTH_LONG).show();
        //1.
        if (intent.getAction().toString().equals("android.provider.Telephony.SMS_RECEIVED"))
        {
            Bundle bundle=intent.getExtras();
            Object[] pdus=(Object[]) bundle.get("pdus");
            String smsInfo="";
            String inPhoneNum= "";
            for (int i = 0; i < pdus.length ; i++) {
                SmsMessage smsMessage = SmsMessage.createFromPdu((byte[])pdus[i]);
                inPhoneNum=smsMessage.getDisplayOriginatingAddress();
                smsInfo+=smsMessage.getDisplayMessageBody();
            }

            if (FirebaseDatabase.getInstance()!= null)
            {
                Date date= Calendar.getInstance().getTime();

                MyTask myTask=new MyTask(smsInfo,false,date,1,"",inPhoneNum);
                DatabaseReference reference= FirebaseDatabase.getInstance().getReference();
                String email= FirebaseAuth.getInstance().getCurrentUser().getEmail();
                //all my task will be under my mail under the root MyTasks
                //child can not contain chars: $,#,[,], . ,.....
                reference.child(email.replace(".", "_"));
                reference.child("MyTasks");
                reference.push();
                reference.setValue(myTask, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        if (databaseError == null) {
                            Toast.makeText(context, "Save successful",
                                    Toast.LENGTH_LONG).show();
                            makeNotification(context);
                        } else {
                            Toast.makeText(context, "Save Failed" + databaseError.getMessage(),
                                    Toast.LENGTH_LONG).show();

                        }
                    }
                });
            }

            //auto sms response
            if (smsInfo.contains("hello"))
            {
                //you need to add SEND_SMS User Permission
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(inPhoneNum,null,"AUTO REPLAY",null,null);
            }
        }

    }
    private void makeNotification(Context context)
    {
        //1
        NotificationCompat.Builder builder = (NotificationCompat.Builder) new NotificationCompat.Builder(context)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Tasks Manager")
                .setContentText("New SMS ADDED to your tasks");

        //2 response to the notifications selection
        Intent resIntent = new Intent(context, MngTaskActivity.class);
        // intent for aplictions that are not one of our apps
        PendingIntent resPendingIntent = PendingIntent.getActivity(context,0,resIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        //3
        builder.setContentIntent(resPendingIntent);
        //4 service that is the system and we use it.
        NotificationManager notificationManager= (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        notificationManager.notify(22,builder.build());
    }
}
