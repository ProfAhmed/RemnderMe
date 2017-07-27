package ahmedpro.com.reminderme;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import ahmedpro.com.reminderme.TaskAdapter.TaskAdapter;

public class MyReceiverAlarm1 extends BroadcastReceiver {

    String title;

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent1 = new Intent(context, MainActivity.class);
        intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        if (intent != null) {
            String strdata = intent.getExtras().getString("Uniqid");
            if (strdata.equals("From Adapter")) {
//                title = intent.getStringExtra("title2");
                title = TaskAdapter.getTilt2();

                Toast.makeText(context, "Rec from adapter: " + title, Toast.LENGTH_LONG).show();
            }
            if (strdata.equals("From Add")) {
                //title = intent.getStringExtra("title");
                title = AddTaskDialog.getTilt1();
                Toast.makeText(context, "Rec from Add: " + title, Toast.LENGTH_LONG).show();

            }

        } else {
            title = "NULL";
        }
        //if we want ring on notifcation then uncomment below line// //
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 100, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context).setSmallIcon(R.mipmap.ic_launcher)
                .setSmallIcon(R.mipmap.ic_launcher).setContentIntent(pendingIntent)
                .setContentText("don't forget your task " +
                        title + " :)").setContentTitle("To Do List ")
                .setSound(alarmSound).setAutoCancel(true);
        notificationManager.notify(100, builder.build());
    }
}
