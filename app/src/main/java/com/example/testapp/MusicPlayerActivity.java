package com.example.testapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class MusicPlayerActivity extends AppCompatActivity implements ServiceConnection {
    private static final String TAG = "MusicPlayerActivity";
    private MediaPlayer mediaPlayer;
    private SeekBar seekBar;
    private TextView currentMusicDurationTxt;
    private Timer timer;
    private ImageView playBtn;

    private MusicPlayerService.MyBinder myBinder = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = new Intent(this,MusicPlayerService.class);
        bindService(intent,this,BIND_AUTO_CREATE);


        setContentView(R.layout.activity_music_player);
        setupToolbar();


    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar)findViewById(R.id.music_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timer.cancel();
                finish();
                Toast.makeText(MusicPlayerActivity.this,"Music player closed",Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void setupViews() {
        playBtn = (ImageView) findViewById(R.id.imv_play_music);
        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startService(new Intent(MusicPlayerActivity.this,MusicPlayerService.class));
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    playBtn.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_play, null));
                } else {
                    playBtn.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_pause, null));
                    mediaPlayer.start();
                }
            }
        });


        TextView musicDurationTxt = (TextView) findViewById(R.id.txt_music_duration);
        currentMusicDurationTxt = (TextView) findViewById(R.id.txt_current_music_duration);
        musicDurationTxt.setText(formatDuration(mediaPlayer.getDuration()));
        currentMusicDurationTxt.setText(formatDuration(0));

        ImageView rewindBtn = (ImageView) findViewById(R.id.imv_rewind_music);
        ImageView forwardBtn = (ImageView) findViewById(R.id.imv_forward_music);
        rewindBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() - 5000);
            }
        });
        forwardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaPlayer.seekTo(mediaPlayer.getCurrentPosition() + 5000);
            }
        });

        seekBar = (SeekBar) findViewById(R.id.seek_bar_music_player);
        seekBar.setMax(mediaPlayer.getDuration());
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        timer = new Timer();
        timer.schedule(new MainTimer(), 0, 1000);


    }

    private String formatDuration(long duration) {
        int second = (int) (duration / 1000);
        int minute = second / 60;
        second %= 60;
        return String.format(Locale.ENGLISH, "%02d", minute) + ":" + String.format(Locale.ENGLISH, "%02d", second);
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        Log.i(TAG, "MusicPlayerActivity: onServiceConnected");
        myBinder = (MusicPlayerService.MyBinder) service;
        mediaPlayer = myBinder.getService().getMediaPlayer();
        setupViews();
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        Log.i(TAG, "MusicPlayerActivity: onServiceDisconnected");
    }


    private class MainTimer extends TimerTask {
        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    seekBar.setProgress(mediaPlayer.getCurrentPosition());
                    currentMusicDurationTxt.setText(formatDuration(mediaPlayer.getCurrentPosition()));
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        timer.purge();
        timer.cancel();
        unbindService(this);
        super.onDestroy();
    }
}