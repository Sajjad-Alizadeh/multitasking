package com.example.testapp;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.os.ParcelUuid;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.content.res.ResourcesCompat;

public class MusicPlayerService extends Service {
    private static final String TAG = "MusicPlayerService";
    private static final String ACTION_PLAY = "com.example.testapp.ACTION_PLAY";
    private static final String ACTION_REWIND = "com.example.testapp.ACTION_REEWIND";
    private static final String ACTION_FORWARD = "com.example.testapp.ACTION_FORWARD";

    private MyBinder myBinder = new MyBinder();

    public MediaPlayer mediaPlayer;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.i(TAG, "MusicPlayerService: onBind");
        setupMediaPlayer();

        return myBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "MusicPlayerService: onStartCommand");

        if (intent.getAction()==null){
            intent.setAction("");
        }
        switch (intent.getAction()) {
            case ACTION_REWIND:
                mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() - 5000);
                break;
            case ACTION_PLAY:
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                } else {
                    mediaPlayer.start();
                }
                break;
            case ACTION_FORWARD:
                mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() + 5000);
                break;


            default:
                Intent showMusicPlayerActivity = new Intent(this, MusicPlayerActivity.class);
                showMusicPlayerActivity.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

                Intent rewindIntent = new Intent(this, MusicPlayerService.class);
                rewindIntent.setAction(ACTION_REWIND);
                PendingIntent rewindPendingIntent = PendingIntent.getActivity(this, 0, rewindIntent, 0);

                Intent playIntent = new Intent(this, MusicPlayerService.class);
                playIntent.setAction(ACTION_PLAY);
                PendingIntent playPendingIntent = PendingIntent.getActivity(this, 0, playIntent, 0);

                Intent ForwardIntent = new Intent(this, MusicPlayerService.class);
                ForwardIntent.setAction(ACTION_REWIND);
                PendingIntent ForwardPendingIntent = PendingIntent.getActivity(this, 0, ForwardIntent, 0);

                Notification notification = new NotificationCompat.Builder(this)
                        .setContentTitle("My Music Player")
                        .setSmallIcon(R.drawable.ic_music_note_black_36dp)
                        .setContentIntent(PendingIntent.getActivity(this, 0, showMusicPlayerActivity, 0))
                        .setContentText("Content Text")
                        .addAction(R.drawable.ic_fast_rewind_black_48dp, "rewind", rewindPendingIntent)
                        .addAction(R.drawable.ic_action_play_black, "play", playPendingIntent)
                        .addAction(R.drawable.ic_fast_forward_black_48dp, "forward", ForwardPendingIntent)
                        .setOngoing(true)
                        .build();

                startForeground(100, notification);
                break;


        }

        return START_STICKY;
    }

    private void setupMediaPlayer() {
        mediaPlayer = MediaPlayer.create(this, R.raw.shadmehr_delam_khast); // az dakhele khode app play mishe

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                stopSelf();
                stopForeground(true);
            }
        });
    }


    public MediaPlayer getMediaPlayer() {
        return this.mediaPlayer;
    }

    public class MyBinder extends Binder {

        public MusicPlayerService getService() {
            return MusicPlayerService.this;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy: ");
    }
}
