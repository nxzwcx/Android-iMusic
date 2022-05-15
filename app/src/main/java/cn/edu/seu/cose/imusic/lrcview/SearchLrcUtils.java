package cn.edu.seu.cose.imusic.lrcview;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.edu.seu.cose.imusic.util.Music;

//搜索本地歌词
//搜索网络歌词
public class SearchLrcUtils {
    private static final String APPDIR="/_iMusic";
    private static final String MP3 = ".mp3";
    private static final String FLAC = ".flac";
    private static final String LRC = ".lrc";

    private static String getAppDir() {
        return Environment.getExternalStorageDirectory() + APPDIR;
    }

    public static String getMusicDir() {
        String dir = getAppDir() + "/Music/";
        return mkdirs(dir);
    }

    public static String getLrcDir() {
        String dir = getAppDir() + "/Lyric/";
        return mkdirs(dir);
    }

    private static String mkdirs(String dir) {
        File file = new File(dir);
        if (!file.exists()) {
            file.mkdirs();
        }
        return dir;
    }

    private static boolean exists(String path) {
        File file = new File(path);
        return file.exists();
    }

    public static String getFileName(String artist, String title) {
        artist = stringFilter(artist);
        title = stringFilter(title);
        if (TextUtils.isEmpty(artist)) {
            artist = "未知";
        }
        if (TextUtils.isEmpty(title)) {
            title = "未知";
        }
        return artist + " - " + title;
    }

    /**
     * 过滤特殊字符(\/:*?"<>|)
     */
    private static String stringFilter(String str) {
        if (str == null) {
            return null;
        }
        String regEx = "[\\/:*?\"<>|]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }

    public static String getLrcFileName(String artist, String title) {
        return getFileName(artist, title) + LRC;
    }
    /**
     * 获取歌词路径<br>
     * 先从已下载文件夹中查找，如果不存在，则从歌曲文件所在文件夹查找。
     *
     * @return 如果存在返回路径，否则返回null
     */
    public static String getLrcFilePath(Music music) {
        if (music == null) {
            return null;
        }

        String lrcFilePath = getLrcDir() + getLrcFileName(music.getSinger(), music.getName());
        if (!exists(lrcFilePath)) {
            lrcFilePath = music.getUrl().replace(MP3, LRC).replace(FLAC,LRC);
            if (!exists(lrcFilePath)) {
                lrcFilePath = null;
            }
        }
        System.out.println("LRC："+lrcFilePath);
        return lrcFilePath;
    }

    public static String getLrcTextFromAssets(Context context, String fileName) {
        String lrcText = null;
        try {
            InputStream is = context.getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            lrcText = new String(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lrcText;
    }

}
