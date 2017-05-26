package com.example.test;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import cn.neillee.composedmenu.ComposedMenu;
import cn.neillee.composedmenu.LinearFlickMenu;
import cn.neillee.composedmenu.OnMenuStatusChangedListener;

/**
 * 作者：Neil on 2017/5/25 21:44.
 * 邮箱：cn.neillee@gmail.com
 */

public class FlickMenuActivity extends AppCompatActivity implements OnMenuStatusChangedListener, ComposedMenu.OnMenuItemClickListener {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flick_menu);

        initViews();
    }

    private void initViews() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FlickMenuActivity.this.finish();
            }
        });

        LinearFlickMenu lfm1 = (LinearFlickMenu) findViewById(R.id.lfm1);
        lfm1.setCenterButtonIcon(R.drawable.composer_place);
        lfm1.addItem(new ComposedMenu.MenuItem(lfm1).setIcon(R.drawable.composer_music), 0);
        lfm1.addItem(new ComposedMenu.MenuItem(lfm1).setIcon(R.drawable.composer_place), 1);
        lfm1.addItem(new ComposedMenu.MenuItem(lfm1).setIcon(R.drawable.composer_sleep), 2);

        LinearFlickMenu lfm2 = (LinearFlickMenu) findViewById(R.id.lfm2);
        lfm2.setCenterButtonIcon(R.drawable.composer_place);
        lfm2.addItem(new ComposedMenu.MenuItem(lfm2).setIcon(R.drawable.composer_music), 0);
        lfm2.addItem(new ComposedMenu.MenuItem(lfm2).setIcon(R.drawable.composer_place), 1);
        lfm2.addItem(new ComposedMenu.MenuItem(lfm2).setIcon(R.drawable.composer_sleep), 2);

        LinearFlickMenu lfm3 = (LinearFlickMenu) findViewById(R.id.lfm3);
        lfm3.setCenterButtonIcon(R.drawable.composer_place);
        lfm3.addItem(new ComposedMenu.MenuItem(lfm3).setIcon(R.drawable.composer_music), 0);
        lfm3.addItem(new ComposedMenu.MenuItem(lfm3).setIcon(R.drawable.composer_place), 1);
        lfm3.addItem(new ComposedMenu.MenuItem(lfm3).setIcon(R.drawable.composer_sleep), 2);

        LinearFlickMenu lfm4 = (LinearFlickMenu) findViewById(R.id.lfm4);
        lfm4.setCenterButtonIcon(R.drawable.composer_place);
        lfm4.addItem(new ComposedMenu.MenuItem(lfm4).setIcon(R.drawable.composer_music), 0);
        lfm4.addItem(new ComposedMenu.MenuItem(lfm4).setIcon(R.drawable.composer_place), 1);
        lfm4.addItem(new ComposedMenu.MenuItem(lfm4).setIcon(R.drawable.composer_sleep), 2);

        lfm1.setOnMenuItemClickListener(this);
        lfm1.setOnMenuStatusChangedListener(this);
        lfm2.setOnMenuItemClickListener(this);
        lfm2.setOnMenuStatusChangedListener(this);
        lfm3.setOnMenuItemClickListener(this);
        lfm3.setOnMenuStatusChangedListener(this);
        lfm4.setOnMenuItemClickListener(this);
        lfm4.setOnMenuStatusChangedListener(this);
    }

    @Override
    public void onMenuItemClick(ViewGroup parent, View v, int pos) {
        String info = "";
        switch (parent.getId()) {
            case R.id.lfm1:
                info = "LFM1-" + pos + "Clicked!";
                break;
            case R.id.lfm2:
                info = "LFM2-" + pos + "Clicked!";
                break;
            case R.id.lfm3:
                info = "LFM3-" + pos + "Clicked!";
                break;
            case R.id.lfm4:
                info = "LFM4-" + pos + "Clicked!";
                break;
        }
        Toast.makeText(this, info, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStatusChanged(ViewGroup parent, ComposedMenu.Status from, ComposedMenu.Status to) {
        String info = "";
        switch (parent.getId()) {
            case R.id.lfm1:
                info = "LFM1-" + to.name();
                break;
            case R.id.lfm2:
                info = "LFM2-" + to.name();
                break;
            case R.id.lfm3:
                info = "LFM3-" + to.name();
                break;
            case R.id.lfm4:
                info = "LFM4-" + to.name();
                break;
        }
        Toast.makeText(this, info, Toast.LENGTH_SHORT).show();
    }
}
