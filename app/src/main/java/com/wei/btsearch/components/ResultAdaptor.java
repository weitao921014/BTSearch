package com.wei.btsearch.components;

import android.content.ClipboardManager;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.wei.btsearch.R;
import com.wei.btsearch.btengine.BTItem;

import java.util.ArrayList;

/**
 * Created by wei on 17-3-26.
 */
public class ResultAdaptor extends BaseAdapter {
    ArrayList<BTItem> list;
    Context context;
    ClipboardManager clipboardManager;


    public ResultAdaptor(ArrayList<BTItem> list, Context context) {
        this.list = list;
        this.context = context;
        clipboardManager = (ClipboardManager) context.getSystemService(context.CLIPBOARD_SERVICE);
    }

    @Override
    public int getCount() {
        return list.size();
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
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.copymagnet:
                        clipboardManager.setText(list.get(i).magnetUrl);
                        Toast.makeText(context, list.get(i).magnetUrl, Toast.LENGTH_SHORT).show();
                }
            }
        };

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.layout_item, null);
        }

        ((TextView) view.findViewById(R.id.title)).setText(Html.fromHtml(list.get(i).title));

        ((TextView) view.findViewById(R.id.files)).setText("文件数:" + list.get(i).fileCount);
        ((TextView) view.findViewById(R.id.data)).setText("创建时间:" + list.get(i).createTime);
        ((TextView) view.findViewById(R.id.hot)).setText("热度:" + list.get(i).hotIndex);
        ((TextView) view.findViewById(R.id.size)).setText("大小:" + list.get(i).filesSize);

        ((Button) view.findViewById(R.id.copymagnet)).setOnClickListener(clickListener);

        return view;
    }
}
