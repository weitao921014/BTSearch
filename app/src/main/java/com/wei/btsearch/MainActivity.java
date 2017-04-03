package com.wei.btsearch;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.*;
import android.widget.*;
import com.wei.btsearch.components.HistoryAdaptor;
import com.wei.btsearch.components.ReadmeActivity;
import com.wei.btsearch.components.SearchResult;
import com.wei.btsearch.configurations.AppConfiguration;
import com.wei.btsearch.storage.DataBaseOperation;
import com.wei.btsearch.storage.HistoryItem;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, HistoryAdaptor.OnItemOperate {

    Toolbar toolbar;
    DrawerLayout drawer;
    NavigationView navigationView;
    SearchView searchView;
    ListView gridView;

    DataBaseOperation operation;
    SharedPreferences sharedPreferences;
    List<HistoryItem> history = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
    }

    private void initViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        gridView = (ListView) findViewById(R.id.history);

        searchView = (SearchView) findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                operation.insertContent(query);
                Intent intent = new Intent(MainActivity.this, SearchResult.class);
                intent.putExtra(AppConfiguration.SEARCH_CONTENT, query);
//                intent.putExtra(AppConfiguration.ENGINE_DEFAULT, sharedPreferences.getInt(AppConfiguration.ENGINE_DEFAULT, 0));
                startActivity(intent);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        operation = new DataBaseOperation(MainActivity.this);
        sharedPreferences = getSharedPreferences(AppConfiguration.ENGINE_SHAREDPREFERENCE, 0);

    }

    private void setHistoryList() {
        history.clear();
        Cursor cursor = operation.queryAll();
        while (cursor.moveToNext()) {
            history.add(new HistoryItem(cursor.getInt(0), cursor.getString(1)));
        }

        if (AppConfiguration.DEBUG) {
            System.out.println(history);
        }

        gridView.setAdapter(new HistoryAdaptor(MainActivity.this, operation, history, MainActivity.this));

    }

    @Override
    protected void onStart() {
        setHistoryList();
        super.onStart();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
//         Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.settings) {
            final SharedPreferences preferences = sharedPreferences;

            final int[] engine = {preferences.getInt(AppConfiguration.ENGINE_DEFAULT, 0)};
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("选择解析引擎")
                    .setCancelable(true)
                    .setSingleChoiceItems(R.array.engines, engine[0], new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            engine[0] = i;
                            if (AppConfiguration.DEBUG) {
                                System.out.println(engine[0] + "selected");
                            }
                        }
                    })
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putInt(AppConfiguration.ENGINE_DEFAULT, engine[0]);
                            editor.commit();
                        }
                    })
                    .setNegativeButton(android.R.string.cancel, null);

            builder.create().show();
        } else if (id == R.id.changetheme) {
            Toast.makeText(this, "只做了一个主题，没得换了", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.readme) {
            startActivity(new Intent(this, ReadmeActivity.class));
        }
        return true;
    }

    @Override
    public void onItemDeleted() {
        setHistoryList();
    }
}