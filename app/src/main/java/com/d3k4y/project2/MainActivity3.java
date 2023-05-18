package com.d3k4y.project2;

import android.os.Bundle;

import com.d3k4y.project2.ui.main.Chats_Fragment;
import com.d3k4y.project2.ui.main.Store_Fragment;
import com.d3k4y.project2.ui.main.Posts_Fragment;
import com.d3k4y.project2.ui.main.Settings_Fragment;
import com.d3k4y.project2.ui.main.ViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Process;

public class MainActivity3 extends AppCompatActivity {

    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        TabLayout tabs = findViewById(R.id.tabs);

        viewPager = findViewById(R.id.view_pager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new Chats_Fragment(), "Chats");
        adapter.addFragment(new Store_Fragment(), "Store");
        adapter.addFragment(new Posts_Fragment(), "Posts");
        adapter.addFragment(new Settings_Fragment(), "Settings");
        viewPager.setAdapter(adapter);

        tabs.setupWithViewPager(viewPager);
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
        Process.killProcess(Process.myPid());
        System.exit(1);
    }
}