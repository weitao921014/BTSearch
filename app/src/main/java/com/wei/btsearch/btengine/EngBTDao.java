package com.wei.btsearch.btengine;

import com.wei.btsearch.utils.HttpUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by wei on 17-3-21.
 */
public class EngBTDao extends BTEngine {
//    private String url="http://www.btdao.biz/list/[name]-s1d-[index].html";
//    http://www.btdao5.com/list/11-s1d-1.html

    String content;

    public EngBTDao(String content) {
        this.content = content;
    }

    @Override
    public ArrayList<BTItem> getItems(String search, int index) {
        ArrayList<BTItem> list = new ArrayList<>();

        try {
            String url = "http://www.btdao5.com/list/" + content + "-s1d-" + index + ".html";
            System.out.println(url);
            String urlResult = HttpUtils.getURLStringByOk(url);

            Pattern pattern = Pattern.compile("<li>.*?</li>", Pattern.DOTALL);
            Matcher matcher = pattern.matcher(urlResult);
            while (matcher.find()) {
                final BTItem item = getItem(matcher.group());
                if (item != null) list.add(item);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    private BTItem getItem(String str) {
        BTItem item = null;
        final Pattern patternItem = Pattern.compile(
                "<a title=[\\D\\d]*?>([\\D\\d]*?)</a>" + "[\\D\\d]*" +
                        "<dt>大小:<span>([\\D\\d]*?)</span>" + "[\\D\\d]*" +
                        "文件数:<span>([\\D\\d]*?)</span>" + "[\\D\\d]*" +
                        "创建日期:<span>([\\D\\d]*?)</span>" + "[\\D\\d]*" +
                        "热度:<span>([\\D\\d]*?)</span>" + "[\\D\\d]*" +
                        "(magnet:[\\D\\d&&[^\"]]*)" + "[\\D\\d]*" +
                        "(thunder:[\\D\\d&&[^\"]]*)"
        );
        final Pattern patternShortcut = Pattern.compile("<div class=\"item-list\">([\\D\\d]*?)</div>");
        final Matcher matcherItem = patternItem.matcher(str);
        final Matcher matcherShortcut = patternShortcut.matcher(str);

        final String title;
        final String size;
        final String count;
        final String date;
        final String hot;
        final String magnet;
        final String thunder;
        final String shortcut;

        if (matcherItem.find()) {
            title = matcherItem.group(1).replaceAll("<span class=[\\D\\d]*?</script>", "")
                    .replace("<span class=\"mhl\">", "<font color='#FF0000'>")
                    .replace("</span>", "</font>");
            size = matcherItem.group(2);
            count = matcherItem.group(3);
            date = matcherItem.group(4);
            hot = matcherItem.group(5);
            magnet = matcherItem.group(6);
            thunder = matcherItem.group(7);
            StringBuilder shortcutBuider = new StringBuilder("");
            while (matcherShortcut.find()) {
                shortcutBuider.append(matcherShortcut.group(1).replaceAll("<a class=[\\D\\d]*?</script>", "")
                        + "\t\n");
            }
            shortcut = shortcutBuider.toString();
            item = new BTItem(title, shortcut, count, size, date, hot, magnet, thunder);
        }
        return item;
    }
}
