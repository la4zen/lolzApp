package com.la4zen.lolzapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences mSettings;
    private String cookie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSettings = getSharedPreferences("lolzapp_Configuration", Context.MODE_PRIVATE);
        cookie = mSettings.getString("cookie", null);
        System.out.println(cookie);
        if (cookie == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        } else {
            System.out.println("logged");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

    }
}