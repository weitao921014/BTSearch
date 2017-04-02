package com.wei.btsearch.components;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import com.wei.btsearch.R;
import com.wei.btsearch.btengine.BTEngine;
import com.wei.btsearch.btengine.BTItem;
import com.wei.btsearch.btengine.EngCililian;
import com.wei.btsearch.configurations.AppConfiguration;
import com.wei.btsearch.customviews.SwipeLoadLayout;

import java.io.IOException;
import java.util.ArrayList;

public class SearchResult extends AppCompatActivity implements SwipeLoadLayout.OnLoadListener {

    private static final String TAG = "SearchResult";
    private static final int MSG_SEARCHOK = 1;
    private static final int MSG_SEARCH_RESULTNULL = 2;
    private static final int MSG_NETWORK_FAILD = 3;

    Toolbar toolbar;
    ListView listView;
    SwipeLoadLayout swipeLoadLayout;
    ProgressBar progressBar;
    String content;
    ArrayList<BTItem> list = new ArrayList<>();
    int nextSearchPage = 1;
    BTEngine btEngine;

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            switch (message.what) {
                case MSG_SEARCHOK:
                    list.addAll((ArrayList<BTItem>) message.obj);
                    nextSearchPage += 1;
                    int position = listView.getFirstVisiblePosition();
                    if (AppConfiguration.DEBUG) {
                        System.out.println("x: " + position);
                    }
                    listView.setAdapter(new ResultAdaptor(list, SearchResult.this));
                    listView.setSelection(position);
                    break;
                case MSG_SEARCH_RESULTNULL:
                    Toast.makeText(SearchResult.this, "搜不到了", Toast.LENGTH_SHORT).show();
                    break;
                case MSG_NETWORK_FAILD:
                    Toast.makeText(SearchResult.this, "网络错误", Toast.LENGTH_SHORT).show();
                    break;
            }
            progressBar.setVisibility(View.GONE);
            swipeLoadLayout.setLoading(false);
            if (AppConfiguration.DEBUG) {
                System.out.println("handler work out");
            }
            return true;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        init();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_searchresult, menu);
        return true;
    }

    private void init() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        content = getIntent().getStringExtra(AppConfiguration.SEARCH_CONTENT);
        System.out.println("contetn " + content);
        toolbar.setTitle(content);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listView = (ListView) findViewById(R.id.list);
        progressBar = (ProgressBar) findViewById(R.id.progress);
        swipeLoadLayout = (SwipeLoadLayout) findViewById(R.id.swipeload);
        swipeLoadLayout.setEnabled(false);
        swipeLoadLayout.setOnLoadListener(this);

        btEngine = new EngCililian(content);
        list.clear();
        swipeLoadLayout.requestLoadData();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void doSearch() {
        progressBar.setVisibility(View.VISIBLE);


        new Thread(new Runnable() {
            @Override
            public void run() {
                Message message;
                ArrayList<BTItem> result;
                try {
                    if (AppConfiguration.DEBUG) {
                        System.out.println("nextSearchPage: " + nextSearchPage);
                    }

                    result = btEngine.getItems(content, nextSearchPage);

                    if (result == null) {
                        message = handler.obtainMessage(MSG_SEARCH_RESULTNULL);
                        handler.sendMessage(message);
                    } else {
                        message = handler.obtainMessage(MSG_SEARCHOK);
                        message.obj = result;
                        handler.sendMessage(message);
                    }
                } catch (IOException e) {
                    message = handler.obtainMessage(MSG_NETWORK_FAILD);
                    handler.sendMessage(message);
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onLoad() {
        doSearch();
    }
}
