package com.wei.btsearch.components;

import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
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
import com.wei.btsearch.btengine.EngBTDao;
import com.wei.btsearch.btengine.EngCililian;
import com.wei.btsearch.configurations.AppConfiguration;
import com.wei.btsearch.customviews.SwipeLoadLayout;

import java.io.File;
import java.io.FileWriter;
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
    int engineType;
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
                    getSupportActionBar().setTitle(content + ": " + list.size());
                    break;
                case MSG_SEARCH_RESULTNULL:
                    Toast.makeText(SearchResult.this, "搜不到了", Toast.LENGTH_SHORT).show();
                    swipeLoadLayout.setFinished(true);
                    break;
                case MSG_NETWORK_FAILD:
                    Toast.makeText(SearchResult.this, "网络错误", Toast.LENGTH_SHORT).show();
                    swipeLoadLayout.setFinished(true);
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
        engineType = getSharedPreferences(AppConfiguration.ENGINE_SHAREDPREFERENCE, 0)
                .getInt(AppConfiguration.ENGINE_DEFAULT, 0);

        if (AppConfiguration.DEBUG) {
            System.out.println("engine type: " + engineType);
        }

        System.out.println("contetn " + content);
        toolbar.setTitle(content);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listView = (ListView) findViewById(R.id.list);
        progressBar = (ProgressBar) findViewById(R.id.progress);
        swipeLoadLayout = (SwipeLoadLayout) findViewById(R.id.swipeload);
        swipeLoadLayout.setEnabled(false);
        swipeLoadLayout.setOnLoadListener(this);

        btEngine = determineEngine();
        list.clear();
        swipeLoadLayout.requestLoadData();
    }

    private BTEngine determineEngine() {
        BTEngine engine = null;

        switch (engineType) {
            case 0:
                engine = new EngCililian(content);
                break;
            case 1:
                engine = new EngBTDao(content);
                break;
//            case 2:
//                break;
//            case 3:
//                break;
            default:
                engine = new EngBTDao(content);
                break;
        }
        return engine;
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

                    if (result == null || result.size() == 0) {
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
        } else if (item.getItemId() == R.id.save_to_loacle) {
            if (list == null || list.size() == 0) {
                Toast.makeText(this, "当前并无结果可保存", Toast.LENGTH_SHORT).show();
            } else {
                final boolean[] checkedItem = new boolean[]{true, true, true, true};
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("保存到/sdcard/btsearch/")
                        .setCancelable(true)
                        .setMultiChoiceItems(R.array.saveitems, checkedItem, new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i, boolean b) {
                                checkedItem[i] = b;
                            }
                        })
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (AppConfiguration.DEBUG) {
                                    for (int m = 0; m < checkedItem.length; m++) {
                                        System.out.println(m + ": " + checkedItem[m]);
                                    }
                                }
                                if (checkedItem[0] | checkedItem[1] | checkedItem[2] | checkedItem[3]) {
                                    try {
                                        String dirPath = "/sdcard/btsearch/";
                                        File dir = new File(dirPath);
                                        if (!dir.exists()) {
                                            dir.mkdirs();
                                        }

                                        String filePath = System.currentTimeMillis() + content + ".txt";
                                        File file = new File(dirPath, filePath);
                                        file.createNewFile();
                                        if (AppConfiguration.DEBUG) {
                                            System.out.println(file.getAbsolutePath());
                                        }
                                        FileWriter writer = new FileWriter(file);
                                        for (int n = 0; n < list.size(); n++) {
                                            if (checkedItem[0]) {
                                                writer.write(list.get(n).getTitle().replace("<font color='#FF0000'>", "")
                                                        .replace("</font>", "") + "\t\n");
                                            }
                                            if (checkedItem[2]) {
                                                writer.write(list.get(n).getMagnetUrl() + "\t\n");
                                            }
                                            if (checkedItem[3]) {
                                                if (list.get(n).getThunderUrl() != "") {
                                                    writer.write(list.get(n).getThunderUrl() + "\t\n");
                                                }
                                            }
                                            if (checkedItem[1]) {
                                                writer.write("文件:" + list.get(n).fileCount
                                                        + "  " + "时间:" + list.get(n).createTime
                                                        + "  " + "热度:" + list.get(n).hotIndex
                                                        + "  " + "大小:" + list.get(n).filesSize
                                                        + "\t\n");
                                            }
                                            writer.write("\t\n");
                                        }
                                        writer.close();
                                        Toast.makeText(SearchResult.this, filePath, Toast.LENGTH_SHORT).show();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                } else {
                                    Toast.makeText(SearchResult.this, "请至少选择一个项目", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .setNegativeButton(android.R.string.cancel, null);

                builder.create().show();
            }
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onLoad() {
        doSearch();
    }
}
