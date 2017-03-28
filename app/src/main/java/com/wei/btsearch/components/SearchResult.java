package com.wei.btsearch.components;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
import com.wei.btsearch.R;
import com.wei.btsearch.btengine.BTEngine;
import com.wei.btsearch.btengine.BTItem;
import com.wei.btsearch.btengine.EngCililian;
import com.wei.btsearch.configurations.AppConfiguration;

import java.io.IOException;
import java.util.ArrayList;

public class SearchResult extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "SearchResult";
    private static final int MSG_SEARCHOK = 1;
    private static final int MSG_SEARCHFAILD = 2;

    Toolbar toolbar;
    ListView listView;
    Button pre, next;
    ProgressBar progressBar;
    String content;
    ArrayList<BTItem> list = new ArrayList<>();
    int currentPage = 1;
    BTEngine btEngine;
    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            switch (message.what) {
                case MSG_SEARCHOK:
                    list = (ArrayList<BTItem>) message.obj;
                    listView.setAdapter(new ResultAdaptor(list, SearchResult.this));
                    break;
                case MSG_SEARCHFAILD:
                    Toast.makeText(SearchResult.this, "SearchFaild", Toast.LENGTH_SHORT).show();
                    break;
            }
            progressBar.setVisibility(View.GONE);
            return true;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        initUI();

    }

    private void initUI() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        content = getIntent().getStringExtra(AppConfiguration.SEARCH_CONTENT);
        toolbar.setTitle("搜索: " + content);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        listView = (ListView) findViewById(R.id.list);
        progressBar = (ProgressBar) findViewById(R.id.progress);
        pre = (Button) findViewById(R.id.pre);
        pre.setOnClickListener(this);
        next = (Button) findViewById(R.id.next);
        next.setOnClickListener(this);

        btEngine = new EngCililian(content);
    }

    @Override
    protected void onStart() {
        super.onStart();
        doSearch();
    }

    private void doSearch() {
        progressBar.setVisibility(View.VISIBLE);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message message;
                ArrayList<BTItem> result = null;
                try {
                    result = btEngine.getItems(content, currentPage);
                    System.out.println(result);
                    if (result == null) {
                        message = handler.obtainMessage(MSG_SEARCHFAILD);
                        handler.sendMessage(message);
                    } else {
                        message = handler.obtainMessage(MSG_SEARCHOK);
                        message.obj = result;
                        handler.sendMessage(message);
                    }
                } catch (IOException e) {
                    message = handler.obtainMessage(MSG_SEARCHFAILD);
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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.pre:
                if (currentPage < 1) {
                    currentPage = 1;
                } else {
                    currentPage -= 1;
                }
                doSearch();
                break;
            case R.id.next:
                currentPage += 1;
                doSearch();
                break;
        }
    }
}
