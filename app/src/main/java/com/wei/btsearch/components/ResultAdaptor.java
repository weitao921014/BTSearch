package com.wei.btsearch.components;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
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
                        break;
                    case R.id.copythunder:
                        if (list.get(i).getThunderUrl().equals("")) {
                            Toast.makeText(context, "此引擎不含迅雷地址", Toast.LENGTH_SHORT).show();
                        } else {
                            clipboardManager.setText(list.get(i).getThunderUrl());
                            Toast.makeText(context, list.get(i).getThunderUrl(), Toast.LENGTH_SHORT).show();
                        }
                        break;
                    case R.id.open:
                        try {
                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse(list.get(i).getMagnetUrl()));
                            context.startActivity(intent);
                        } catch (Exception e) {
                            Toast.makeText(context, "没有能相应的程序", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                        break;
                }
            }
        };

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.layout_item, null);
        }

        ((TextView) view.findViewById(R.id.title)).setText(Html.fromHtml(list.get(i).title));

        ((TextView) view.findViewById(R.id.details)).setText("文件:" + list.get(i).fileCount
                + "  " + "时间:" + list.get(i).createTime
                + "  " + "热度:" + list.get(i).hotIndex
                + "  " + "大小:" + list.get(i).filesSize);

        ((TextView) view.findViewById(R.id.preview)).setText(list.get(i).getFileShortcut());


        ((TextView) view.findViewById(R.id.copymagnet)).setOnClickListener(clickListener);
        ((TextView) view.findViewById(R.id.copythunder)).setOnClickListener(clickListener);
        ((TextView) view.findViewById(R.id.open)).setOnClickListener(clickListener);

        return view;
    }
}
