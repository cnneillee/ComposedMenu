package com.example.test;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
    }

    private void initViews() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Button btnFam = (Button) findViewById(R.id.btn_lfm);
        Button btnRam = (Button) findViewById(R.id.btn_ram);
        btnFam.setOnClickListener(this);
        btnRam.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.btn_lfm:
                intent.setClass(this, FlickMenuActivity.class);
                break;
            case R.id.btn_ram:
                intent.setClass(this, ArcMenuActivity.class);
                break;
        }
        startActivity(intent);
    }
}
