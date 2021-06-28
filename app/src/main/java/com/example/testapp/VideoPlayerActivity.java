package com.example.testapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class VideoPlayerActivity extends AppCompatActivity {

    private VideoView videoView;
    private TextView currentDurationTxt;
    private SeekBar seekBar;
    private ImageView playVideoImv;
    private Toolbar toolbar;
    private Timer timer;


    private RelativeLayout.LayoutParams portraitLayoutParam;
    private RelativeLayout.LayoutParams landscapeLayoutParam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);
        setupLayoutParam();
        setupToolbar();
        setupVideoView();
    }

    private void setupVideoView() {
        videoView = (VideoView) findViewById(R.id.video_view);
        videoView.setVideoURI(Uri.parse("https://aspb21.cdn.asset.aparat.com/aparat-video/81fc6f5eaba25bb8dc88fd6eb10eddef31676218-240p.mp4?wmsAuthSign=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ0b2tlbiI6Ijg1ZGY3ZTYzZGI5MmE1YTVlN2Q0NjhmZGEzYjVmZmQ0IiwiZXhwIjoxNjI0ODk5NDc5LCJpc3MiOiJTYWJhIElkZWEgR1NJRyJ9.-tgnC1twwasBVUdnLaKOLp-AKudW99PLS_E8mMhN73w"));
        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                setupViews();
            }
        });
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                playVideoImv.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_play, null));

            }
        });
    }

    private void setupViews() {
        playVideoImv = (ImageView) findViewById(R.id.imv_play_video);
        playVideoImv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (videoView.isPlaying()) {
                    videoView.pause();
                    playVideoImv.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_play, null));
                } else {
                    videoView.start();
                    playVideoImv.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_pause, null));
                }
            }
        });

        ImageView rewindVideoImv = (ImageView) findViewById(R.id.imv_rewind_video);
        rewindVideoImv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoView.seekTo(videoView.getCurrentPosition() - 5000);
            }
        });

        ImageView forwardVideoImv = (ImageView) findViewById(R.id.imv_forward_video);
        forwardVideoImv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoView.seekTo(videoView.getCurrentPosition() + 5000);
            }
        });

        TextView durationTxt = (TextView) findViewById(R.id.txt_video_duration);
        durationTxt.setText(formatDuration(videoView.getDuration()));

        currentDurationTxt = (TextView) findViewById(R.id.txt_current_video_duration);
        currentDurationTxt.setText(formatDuration(0));


        seekBar = (SeekBar) findViewById(R.id.seek_bar_video_player);
        seekBar.setMax(videoView.getDuration());
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    seekBar.setProgress(progress);
                    videoView.seekTo(progress);
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
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        currentDurationTxt.setText(formatDuration(videoView.getCurrentPosition()));
                        seekBar.setProgress(videoView.getCurrentPosition());
                        seekBar.setSecondaryProgress((videoView.getBufferPercentage() * videoView.getDuration()) / 100);
                    }
                });
            }
        }, 0, 500);


    }

    private void setupToolbar() {
        toolbar = (Toolbar) findViewById(R.id.video_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private String formatDuration(long duration) {
        int second = (int) (duration / 1000);
        int minute = second / 60;
        second %= 60;
        return String.format(Locale.ENGLISH, "%02d", minute) + ":" + String.format(Locale.ENGLISH, "%02d", second);
    }

    private void setupLayoutParam() {
        View toolbar = findViewById(R.id.video_toolbar);
        View mediaController = findViewById(R.id.video_media_controller);
        landscapeLayoutParam = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        portraitLayoutParam = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        portraitLayoutParam.addRule(RelativeLayout.ABOVE, mediaController.getId());
        portraitLayoutParam.addRule(RelativeLayout.BELOW, toolbar.getId());

    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        FrameLayout frameLayout = (FrameLayout) findViewById(R.id.video_frame_layout);
        if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            frameLayout.setLayoutParams(portraitLayoutParam);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            frameLayout.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
        } else {
            frameLayout.setLayoutParams(landscapeLayoutParam);
            frameLayout.bringToFront();
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            frameLayout.setBackgroundColor(ContextCompat.getColor(this, android.R.color.black));
        }

    }

    @Override
    protected void onDestroy() {
        if (timer!=null){
            timer.purge();
            timer.cancel();
        }
        super.onDestroy();

    }
}