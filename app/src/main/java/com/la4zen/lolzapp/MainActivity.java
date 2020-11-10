package com.la4zen.lolzapp;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public class Post {
        public String title, author, thread;
        public void Create(String title, String author, String thread) {
            this.title = title;
            this.author = author;
            this.thread = thread;
        }
    }

    static LinearLayout mainLayout;
    public SharedPrefs sharedPrefs;
    public Context context;
    static ArrayList<Post> posts = new ArrayList<Post>();
    static String balance;
    Button logoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = getApplicationContext();
        mainLayout = findViewById(R.id.linearLayout);
        mainLayout.setBackgroundColor(Color.GRAY);
        sharedPrefs = new SharedPrefs();
        sharedPrefs.init(context);
        logoutButton = findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPrefs.setCookie(null);
                logoutButton.setText("Для выхода из аккаунта перезайдите в приложение.");
            }
        });
        String cookie = SharedPrefs.getCookie();
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
        new loadPage().execute();
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
                    .cookie("xf_session", SharedPrefs.getCookie()).cookie("df_id", "960319fc0b468f741693392b329868f7").get();
                Elements els = doc.select("div.discussionListItem");
                for (Element el : els) {
                    String author = el.select("span.username").get(0).text();
                    String title = el.select("span.spanTitle").get(0).text();
                    String thread = el.select("a.listBlock.main.PreviewTooltip").attr("abs:href");
                    Post post = new Post();
                    post.Create(title, author, thread);
                    posts.add(post);
                }
                System.out.println("loaded");
                return null;
            } catch (IOException e) {
                System.out.println(e);
                return "Произошла неизвестная ошибка.";
            }
        }

        @Override
        protected void onPostExecute(String result) {
            if (result == null) {
                for (Post post : posts) {
                    CardView card = new CardView(getApplicationContext());
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
                    );
                    LinearLayout content = new LinearLayout(getApplicationContext());
                    TextView title = new TextView(getApplicationContext()), author = new TextView(getApplicationContext());
                    content.setOrientation(LinearLayout.VERTICAL);
                    title.setText(post.title);
                    author.setText("Автор : " + post.author);
                    content.addView(title);
                    content.addView(author);
                    content.setPadding(15,15,15,15);
                    card.addView(content, params);
                    card.setRadius(50);
                    card.setClickable(true);
                    card.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // for future function
                        }
                    });
                    params.setMargins(20,10,20, 10);
                    mainLayout.addView(card, params);
                }
            }
        }
    }
    public class loadThread extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... args) {
            try {
                Document doc = Jsoup.connect("https://lolz.guru/").header("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
                        .header("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/85.0.4183.121 Safari/537.36 OPR/71.0.3770.310 (Edition Yx GX)")
                        .cookie("xf_session", SharedPrefs.getCookie()).cookie("df_id", "960319fc0b468f741693392b329868f7").get();
                Elements els = doc.select("div.discussionListItem");
                for (Element el : els) {
                    String author = el.select("span.username").get(0).text();
                    String title = el.select("span.spanTitle").get(0).text();
                    String thread = el.select("a.listBlock.main.PreviewTooltip").attr("abs:href");
                    Post post = new Post();
                    post.Create(title, author, "");
                    posts.add(post);
                }
                System.out.println("loaded");
                return null;
            } catch (IOException e) {
                System.out.println(e);
                return "Произошла неизвестная ошибка.";
            }
        }
    }
}