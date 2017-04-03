package com.wei.btsearch.components;

import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import com.wei.btsearch.R;
import com.wei.btsearch.configurations.AppConfiguration;
import com.wei.btsearch.storage.DataBaseOperation;
import com.wei.btsearch.storage.HistoryItem;

import java.util.List;

/**
 * Created by wei on 17-4-2.
 */
public class HistoryAdaptor extends BaseAdapter {

    public interface OnItemOperate {
        public void onItemDeleted();
    }

    Context context;
    DataBaseOperation operation;
    List<HistoryItem> history;
    OnItemOperate operate;

    public HistoryAdaptor(Context context, DataBaseOperation operation, List<HistoryItem> history, OnItemOperate operate) {
        this.context = context;
        this.operation = operation;
        this.history = history;
        this.operate = operate;
    }

    @Override
    public int getCount() {
        if (history.size() > 0) {
            return history.size() + 1;
        }
        return history.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        if (AppConfiguration.DEBUG) {
            System.out.println("get item " + i);
        }

        view = LayoutInflater.from(context).inflate(R.layout.layout_history_item, null);
        TextView text = (TextView) view.findViewById(R.id.text);
        ImageButton delete = (ImageButton) view.findViewById(R.id.delete);

        if (i == (history.size())) {
            delete.setVisibility(View.GONE);
            text.setGravity(Gravity.CENTER);
            text.setText("清除搜索记录");
            text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    operation.deleteAll();
                    operate.onItemDeleted();
                }
            });

        } else {
            text.setText(history.get(i).getContent());
            text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, SearchResult.class);
                    intent.putExtra(AppConfiguration.SEARCH_CONTENT, history.get(i).getContent());
                    context.startActivity(intent);
                }
            });

            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    operation.deleteItem(history.get(i).getId());
                    operate.onItemDeleted();
                }
            });
        }

        return view;
    }
}
