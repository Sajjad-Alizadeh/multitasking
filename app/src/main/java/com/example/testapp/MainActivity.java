package com.example.testapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import model.InfoModel;

public class MainActivity extends AppCompatActivity {
    private TextView toolbarText;
    private TextView toolbarConnectingTxt;
    private RecyclerView recyclerView;
    private MyBroadcastReceiver myBroadcastReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        setupToolbar();

        setupNavigation();
        setupRecyclerView();
        checkMemosIsEmpty();


        FloatingActionButton addMemoFab = (FloatingActionButton) findViewById(R.id.add_memo_fab);
        addMemoFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AddMemoActivity.class));


            }
        });


    }

    private void setupToolbar() {
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.my_drawer_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);

        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, 0, 0);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

    }

    private void setupNavigation() {
        NavigationView navigationView = findViewById(R.id.my_navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_profile:
                        Toast.makeText(MainActivity.this, "Profile Clicked", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.navigation_music_player:
                        startActivity(new Intent(MainActivity.this, MusicPlayerActivity.class));
                        break;
                    case R.id.navigation_video_player:
                        startActivity(new Intent(MainActivity.this, VideoPlayerActivity.class));
                }
                return true;
            }
        });
    }

    private void setupRecyclerView() {
        recyclerView = findViewById(R.id.memo_list_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        MyDatabase myDatabase = new MyDatabase(this);
        List<InfoModel> memos = myDatabase.getMemos();
        MemoRecyclerAdapter adapter = new MemoRecyclerAdapter(this, memos);
        recyclerView.setAdapter(adapter);
    }

    private void checkMemosIsEmpty() {
        MyDatabase myDatabase = new MyDatabase(this);
        List<InfoModel> memos = myDatabase.getMemos();
        TextView empty_txt = (TextView) findViewById(R.id.empty_memos_txt);
        if (memos.size() > 0) {
            empty_txt.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        toolbarText = (TextView) findViewById(R.id.text_toolbar_txt);
        toolbarConnectingTxt = (TextView) findViewById(R.id.connecting_toolbar_txt);
        myBroadcastReceiver = new MyBroadcastReceiver();
        registerReceiver(myBroadcastReceiver, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(myBroadcastReceiver);
    }

    private class MyBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
            NetworkInfo ni = cm.getActiveNetworkInfo();

            boolean isConnected = ni != null && ni.isConnectedOrConnecting();

            if (isConnected) {
                toolbarText.setText("TestApp");
            } else {
                toolbarText.setText("Connecting...");
        }
    }
}}