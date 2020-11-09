package com.la4zen.lolzapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences mSettings;
    private String cookie;

    TextView balanceTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        balanceTextView = findViewById(R.id.balanceTextView);

        mSettings = getPreferences(Context.MODE_PRIVATE);
        cookie = mSettings.getString("cookie", null);

        System.out.println(cookie);

        if (cookie == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivityForResult(intent, 1);
        } else {
            new loadPage().execute();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        } new loadPage().execute();
    }


    @Override
    protected void onPause() {
        super.onPause();

    }
    public class loadPage extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... args) {
            try {
                Document doc = Jsoup.connect("https://lolz.guru").header("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
                    .header("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/85.0.4183.121 Safari/537.36 OPR/71.0.3770.310 (Edition Yx GX)")
                    .header("cookie", mSettings.getString("cookie", null)).get();
                System.out.println(doc.html());
                balanceTextView.setText(doc.select("span.balanceValue").text());
                return null;
            } catch (IOException e) {
                System.out.println(e);
                return "Произошла неизвестная ошибка.";
            }
        }

        @Override
        protected void onPostExecute(String result) {

        }
    }
}