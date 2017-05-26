package com.example.test;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import cn.neillee.composedmenu.ComposedMenu;
import cn.neillee.composedmenu.OnMenuStatusChangedListener;
import cn.neillee.composedmenu.RotatingArcMenu;

/**
 * 作者：Neil on 2017/5/25 21:22.
 * 邮箱：cn.neillee@gmail.com
 */

public class ArcMenuActivity extends AppCompatActivity
        implements ComposedMenu.OnMenuItemClickListener, OnMenuStatusChangedListener {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_arc_menu);

        initViews();
    }

    private void initViews() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArcMenuActivity.this.finish();
            }
        });

        RotatingArcMenu ram1 = (RotatingArcMenu) findViewById(R.id.ram1);
        ram1.setCenterButtonIcon(R.drawable.composer_icn_plus);
        ram1.addItem(new RotatingArcMenu.MenuItem(ram1)
                .setIcon(R.drawable.composer_music), 0);
        ram1.addItem(new RotatingArcMenu.MenuItem(ram1)
                .setIcon(R.drawable.composer_camera), 1);
        ram1.addItem(new RotatingArcMenu.MenuItem(ram1)
                .setIcon(R.drawable.composer_thought), 2);

        RotatingArcMenu ram2 = (RotatingArcMenu) findViewById(R.id.ram2);
        ram2.setCenterButtonIcon(R.drawable.composer_icn_plus);
        ram2.addItem(new RotatingArcMenu.MenuItem(ram2)
                .setIcon(R.drawable.composer_music), 0);
        ram2.addItem(new RotatingArcMenu.MenuItem(ram2)
                .setIcon(R.drawable.composer_camera), 1);
        ram2.addItem(new RotatingArcMenu.MenuItem(ram2)
                .setIcon(R.drawable.composer_thought), 2);

        RotatingArcMenu ram3 = (RotatingArcMenu) findViewById(R.id.ram3);
        ram3.setCenterButtonIcon(R.drawable.composer_icn_plus);
        ram3.addItem(new RotatingArcMenu.MenuItem(ram3)
                .setIcon(R.drawable.composer_music), 0);
        ram3.addItem(new RotatingArcMenu.MenuItem(ram3)
                .setIcon(R.drawable.composer_camera), 1);
        ram3.addItem(new RotatingArcMenu.MenuItem(ram3)
                .setIcon(R.drawable.composer_thought), 2);

        RotatingArcMenu ram4 = (RotatingArcMenu) findViewById(R.id.ram4);
        ram4.setCenterButtonIcon(R.drawable.composer_icn_plus);
        ram4.addItem(new RotatingArcMenu.MenuItem(ram4)
                .setIcon(R.drawable.composer_music), 0);
        ram4.addItem(new RotatingArcMenu.MenuItem(ram4)
                .setIcon(R.drawable.composer_camera), 1);
        ram4.addItem(new RotatingArcMenu.MenuItem(ram4)
                .setIcon(R.drawable.composer_thought), 2);

        ram1.setOnMenuItemClickListener(this);
        ram1.setOnMenuStatusChangedListener(this);
        ram2.setOnMenuItemClickListener(this);
        ram2.setOnMenuStatusChangedListener(this);
        ram3.setOnMenuItemClickListener(this);
        ram3.setOnMenuStatusChangedListener(this);
        ram4.setOnMenuItemClickListener(this);
        ram4.setOnMenuStatusChangedListener(this);
    }

    @Override
    public void onMenuItemClick(ViewGroup parent, View v, int pos) {
        String info = "";
        switch (parent.getId()) {
            case R.id.ram1:
                info = "RAM1-" + pos + "Clicked!";
                break;
            case R.id.ram2:
                info = "RAM2-" + pos + "Clicked!";
                break;
            case R.id.ram3:
                info = "RAM3-" + pos + "Clicked!";
                break;
            case R.id.ram4:
                info = "RAM4-" + pos + "Clicked!";
                break;
        }
        Toast.makeText(this, info, android.widget.Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStatusChanged(ViewGroup parent, ComposedMenu.Status from, ComposedMenu.Status to) {
        String info = "";
        switch (parent.getId()) {
            case R.id.ram1:
                info = "RAM1-" + to.name();
                break;
            case R.id.ram2:
                info = "RAM2-" + to.name();
                break;
            case R.id.ram3:
                info = "RAM3-" + to.name();
                break;
            case R.id.ram4:
                info = "RAM4-" + to.name();
                break;
        }
        Toast.makeText(this, info, Toast.LENGTH_SHORT).show();
    }
}
