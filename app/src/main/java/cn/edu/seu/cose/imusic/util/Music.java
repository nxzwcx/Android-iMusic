package cn.edu.seu.cose.imusic.util;

import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;

import java.io.File;
import java.text.SimpleDateFormat;

public class Music {
    private long id;//歌曲ID
    private String name;
    private String album;
    private int albumID;//专辑封面
    private String singer;
    private String Time;
    private long duration;//时长
    private String url;//路径
    private String lrcTitle;//歌词
    private String lrcSize;
    private long fileSize;//文件大小

    public enum FileType{ rawFile, localFile,networkFile};
    //文件类型：自带文件，本地文件，网络文件
    private FileType fileType;//文件类型
    //如果是raw文件，则afd用来读取raw
    private AssetFileDescriptor afd;

    private SimpleDateFormat time = new SimpleDateFormat("m:ss");

    public Music()
    {
        this.name="NA";
        this.singer="NA";
        this.fileType= FileType.localFile;
    }
    public Music(String name,String singer,AssetFileDescriptor afd,long duration)
    {
        this.name=name;
        this.singer=singer;
        this.fileType=FileType.rawFile;
        this.afd=afd;
        this.duration=duration;
    }
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlbum() { return album; }

    public void setAlbum(String album) {
        this.album = album;
    }

    public int getAlbumID() { return albumID; }

    public void setAlbumID(int albumID) { this.albumID = albumID; }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(SimpleDateFormat time) {
        this.time = time;
    }

    public void setTime(String time) {
        this.Time = time;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLrcTitle() {
        return lrcTitle;
    }

    public void setLrcTitle(String lrcTitle) {
        this.lrcTitle = lrcTitle;
    }

    public String getLrcSize() {
        return lrcSize;
    }

    public void setLrcSize(String lrcSize) {
        this.lrcSize = lrcSize;
    }

    public long getFileSize() {
        return fileSize;
    }

    public void setFileSize(long fileSize) {
        this.fileSize = fileSize;
    }
    public FileType getFileType() { return fileType; }

    public void setFileType(FileType fileType) { this.fileType = fileType; }

    public AssetFileDescriptor getAfd() { return afd; }

    public void setAfd(AssetFileDescriptor afd) { this.afd = afd; }

    public String getDurationStr()
    {
        int duration= (int) getDuration();
        int minute=duration/1000/60;
        int second=duration/1000%60;
        String strMinute=null;
        String strSecond=null;
        if(minute<10){//如果歌曲的时间中的分钟小于10
            strMinute="0"+minute;//在分钟的前面加一个0
        }else{
            strMinute=minute+"";
        }
        if (second<10){//如果歌曲中的秒钟小于10
            strSecond="0"+second;//在秒钟前面加一个0
        }else{
            strSecond=second+"";
        }
        return strMinute+":"+strSecond+"s";
    }
}
