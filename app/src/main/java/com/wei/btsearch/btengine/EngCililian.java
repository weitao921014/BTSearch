package com.wei.btsearch.btengine;

import com.wei.btsearch.utils.HttpUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by wei on 17-3-24.
 */
public class EngCililian extends BTEngine {
    String content;

    public EngCililian(String content) {
        this.content = content;
    }

    @Override
    public ArrayList<BTItem> getItems(String search, int index) throws IOException {
        ArrayList<BTItem> list = new ArrayList<>();
        String url = "http://cililian.me/list/" + content + "/" + index + ".html";
        String urlResult = HttpUtils.getURLStringByOk(url);
        Pattern pattern = Pattern.compile("<li>.*?</li>", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(urlResult);
        while (matcher.find()) {
            final BTItem item = getItem(matcher.group());
            if (item != null) list.add(item);
        }
        return list;
    }

    private BTItem getItem(String str) {
        BTItem item = null;
        final Pattern patternItem = Pattern.compile("<a name=[\\D\\d]*?>([\\D\\d]*?)</a>[\\d\\D]*(<span>[\\d\\D]+?</span>)[\\d\\D]*(<span>[\\d\\D]+?</span>)" +
                "[\\d\\D]*(<span>[\\d\\D]+?</span>)[\\d\\D]*(magnet[\\d\\D&&[^\"]]+)");
        final Matcher matcherItem = patternItem.matcher(str);
        final String title;
        final String size;
        final String count;
        final String data;
        final String magnet;
        if (matcherItem.find()) {
            title = matcherItem.group(1).replace("<span class=\"mhl\">", "<font color='#FF0000'>").replace("</span>", "</font>");
            size = matcherItem.group(2).replace("<span>", "").replace("</span>", "");
            count = matcherItem.group(3).replace("<span>", "").replace("</span>", "");
            data = matcherItem.group(4).replace("<span>", "").replace("</span>", "");
            magnet = matcherItem.group(5);
            item = new BTItem(title, null, count, size, data,
                    "", magnet, "");
        }
        return item;
    }
}
