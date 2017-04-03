package com.wei.btsearch.btengine;

/**
 * Created by wei on 17-3-6.
 */
public class BTItem {
    public String title;
    public String fileShortcut;
    public String fileCount;
    public String filesSize;
    public String createTime;
    public String hotIndex;
    public String magnetUrl;
    public String thunderUrl;

    public BTItem(String title, String fileShortcut, String fileCount, String filesSize, String createTime, String hotIndex, String magnetUrl, String thunderUrl) {
        this.title = title;
        this.fileShortcut = fileShortcut;
        this.fileCount = fileCount;
        this.filesSize = filesSize;
        this.createTime = createTime;
        this.hotIndex = hotIndex;
        this.magnetUrl = magnetUrl;
        this.thunderUrl = thunderUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFileShortcut() {
        return fileShortcut;
    }

    public void setFileShortcut(String fileShortcut) {
        this.fileShortcut = fileShortcut;
    }

    public String getFileCount() {
        return fileCount;
    }

    public void setFileCount(String fileCount) {
        this.fileCount = fileCount;
    }

    public String getFilesSize() {
        return filesSize;
    }

    public void setFilesSize(String filesSize) {
        this.filesSize = filesSize;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getHotIndex() {
        return hotIndex;
    }

    public void setHotIndex(String hotIndex) {
        this.hotIndex = hotIndex;
    }

    public String getMagnetUrl() {
        return magnetUrl;
    }

    public void setMagnetUrl(String magnetUrl) {
        this.magnetUrl = magnetUrl;
    }

    public String getThunderUrl() {
        return thunderUrl;
    }

    public void setThunderUrl(String thunderUrl) {
        this.thunderUrl = thunderUrl;
    }

    public static void printItem(BTItem item) {
        System.out.println(item.title);
        System.out.println("▶" + item.fileCount + "  ▶" + item.filesSize + "  ▶" + item.hotIndex + "  ▶" + item.createTime);
        System.out.println(item.magnetUrl);
    }
}
