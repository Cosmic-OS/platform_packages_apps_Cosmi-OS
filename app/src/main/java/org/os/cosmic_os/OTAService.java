package org.os.cosmic_os;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutionException;

public class OTAService extends JobService {
    String[] serverNodes;
    String Url = "https://raw.githubusercontent.com/Cosmic-OS/platform_vendor_ota/oreo-mr1/";
    @Override
    public boolean onStartJob(JobParameters params) {
        Process process;
        try {
            process = Runtime.getRuntime().exec("/system/bin/getprop persist.ota.version");
            InputStream stdin = process.getInputStream();
            InputStreamReader isr = new InputStreamReader(stdin);
            BufferedReader br = new BufferedReader(isr);
            String localVersion = br.readLine();

            process = Runtime.getRuntime().exec("/system/bin/getprop ro.product.device");
            stdin = process.getInputStream();
            isr = new InputStreamReader(stdin);
            br = new BufferedReader(isr);
            String device = br.readLine();

            HomeFragment.XMLParser xmlParser = new HomeFragment.XMLParser();
            serverNodes = xmlParser.execute(Url+device+".xml").get();

            if (!localVersion.equals(serverNodes[0])) {
                int notifyID = 1;
                String CHANNEL_ID = "my_channel_01";// The id of the channel.
                CharSequence name = "Cosmic Update";// The user-visible name of the channel.
                int importance = 0;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    importance = NotificationManager.IMPORTANCE_HIGH;
                }
                NotificationChannel notificationChannel = null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    notificationChannel = new NotificationChannel(CHANNEL_ID, name, importance);
                }
                NotificationCompat.Builder notification =
                        new NotificationCompat.Builder(this,CHANNEL_ID)
                                .setSmallIcon(R.drawable.ic_cosmic)
                                .setContentTitle("Cosmic Update")
                                .setContentText("A system update is available");
                NotificationManager mNotificationManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                assert mNotificationManager != null;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    assert notificationChannel != null;
                    mNotificationManager.createNotificationChannel(notificationChannel);
                }
                Intent resultIntent = new Intent(this, MainActivity.class);
                TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
                stackBuilder.addParentStack(MainActivity.class);

                // Adds the Intent that starts the Activity to the top of the stack
                stackBuilder.addNextIntent(resultIntent);
                PendingIntent resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
                notification.setContentIntent(resultPendingIntent);
                mNotificationManager.notify(notifyID , notification.build());
            }
            else Log.e("zeromod","NO updates");
            jobFinished(params,false);
        } catch (IOException | ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }
}
