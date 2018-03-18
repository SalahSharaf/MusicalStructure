package com.example.android.musicalstructure;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import java.io.IOException;

/**
 * Created by ALQasem on 10/03/2018.
 */

public class MyService extends Service {
    static MediaPlayer player;
    MVideo video;
    int PLAY = 1;
    int PAUSE = 0;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            video = DisplayVideoActivity.workingVideo;
            int position = DisplayVideoActivity.videoMedia.getCurrentPosition();
            player.setDataSource(video.getData().getAbsolutePath());
            player.seekTo(position);
            player.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
        startNotificationService();
        return START_STICKY;
    }

    private void startNotificationService() {
        String channelName = "Media Player";
        String channelID = "1";
        String description = "This is a notification for media player service ";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationManager notificationManager = (NotificationManager) getBaseContext().getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel channel = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            channel = new NotificationChannel(channelID, channelName, importance);
            channel.setDescription(description);
            notificationManager.createNotificationChannel(channel);
        }
        RemoteViews smallLayout = new RemoteViews(getPackageName(), R.layout.notification_small_layout);
        smallLayout.setImageViewBitmap(R.id.small_notification_video_thumbonial, video.getThumbonial());
        smallLayout.setTextViewText(R.id.small_notification_name, video.getTitle());
        RemoteViews largeLayout = new RemoteViews(getPackageName(), R.layout.notification_large_layout);
        largeLayout.setImageViewBitmap(R.id.large_notification_thumbonail, video.getThumbonial());
        largeLayout.setTextViewText(R.id.large_notification_name, video.getTitle());
        largeLayout.setProgressBar(R.id.progressBar, Integer.parseInt(video.getDuration().toString()), DisplayVideoActivity.videoMedia.getCurrentPosition(), false);
        Intent intentPlay = new Intent(getApplicationContext(), MyService.class);
        intentPlay.putExtra("Play", PLAY);
        Intent intentPause = new Intent(getApplicationContext(), MyService.class);
        intentPause.putExtra("Play", PAUSE);
        PendingIntent play = PendingIntent.getService(getBaseContext(), PLAY, intentPlay, 0);
        PendingIntent pause = PendingIntent.getService(getBaseContext(), PAUSE, intentPlay, 0);
        largeLayout.setOnClickPendingIntent(R.id.large_notification_play_button, play);
        NotificationCompat.Builder customNotification = new NotificationCompat.Builder(getBaseContext(), channelID);
        customNotification.setSmallIcon(R.drawable.ic_video_library_black_24dp);
        customNotification.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_video_library_black_24dp));
        customNotification.setCustomContentView(smallLayout);
        customNotification.setCustomBigContentView(largeLayout);
        customNotification.setStyle(new android.support.v4.media.app.NotificationCompat.DecoratedMediaCustomViewStyle());
        customNotification.build();
    }
    
    public static void stop() {
        player.stop();
    }

    public static void start() {
        player.start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        player.stop();
    }

}
