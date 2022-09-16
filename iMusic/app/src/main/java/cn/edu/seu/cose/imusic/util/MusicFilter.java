package cn.edu.seu.cose.imusic.util;

//过滤文件，判断是否为音乐文件

public class MusicFilter {
    public static boolean isMusic(String fileName) {
        return fileName.matches(("^.*?\\.(mp3|wma|flac)$"));  //正则表达式判断MP3文件
    }
}