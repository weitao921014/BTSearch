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


    @Override
    public ArrayList<BTItem> getItems(String search, int index) {
        String url = "http://www.btdao.biz/list/" + search + "-s1d-" + index + ".html";
        ArrayList<BTItem> list=new ArrayList<>();
        try {
            String webpage = HttpUtils.getURLString(url);
            Pattern pattern = Pattern.compile("<li>.*?</li>",Pattern.DOTALL);
            Matcher matcher = pattern.matcher(webpage);
            while (matcher.find()){
                list.add(getItem(matcher.group()));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return list;
    }

    public static BTItem getItem(String result) {
        Matcher matcher;
        Pattern pattern;

        String title = "";
        String big = "";
        String files = "";
        String date = "";
        String hot = "";
        String magnet = "";
        String thunder = "";

        pattern = Pattern.compile("<a title=\".*?\"");
        matcher = pattern.matcher(result);
        if (matcher.find()) {
            title = matcher.group();
            title = title.substring(9);
        }

        pattern = Pattern.compile("大小:<span>.*?<");
        matcher = pattern.matcher(result);
        if (matcher.find()) {
            big = matcher.group();
            big = big.substring(9, big.length() - 1);
        }

        pattern = Pattern.compile("件数:<span>.*?<");
        matcher = pattern.matcher(result);
        if (matcher.find()) {
            files = matcher.group();
            files = files.substring(9, files.length() - 1);
        }

        pattern = Pattern.compile("日期:<span>.*?<");
        matcher = pattern.matcher(result);
        if (matcher.find()) {
            date = matcher.group();
            date = date.substring(9, date.length() - 1);
        }

        pattern = Pattern.compile("热度:<span>.*?<");
        matcher = pattern.matcher(result);
        if (matcher.find()) {
            hot = matcher.group();
            hot = hot.substring(9, hot.length() - 1);
        }

        pattern = Pattern.compile("magnet[\\D\\d&&[^\"]]*", Pattern.DOTALL);
        matcher = pattern.matcher(result);
        if (matcher.find()) {
            magnet = matcher.group();
        }

        pattern = Pattern.compile("thunder[\\D\\d&&[^\"]]*");
        matcher = pattern.matcher(result);
        if (matcher.find()) {
            thunder = matcher.group();
        }

        return new BTItem(title, null, files, big, date, hot, magnet, thunder);
    }


}
