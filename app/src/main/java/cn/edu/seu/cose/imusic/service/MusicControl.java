package cn.edu.seu.cose.imusic.service;

import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.media.audiofx.Visualizer;
import android.os.Binder;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import cn.edu.seu.cose.imusic.R;
import cn.edu.seu.cose.imusic.util.Music;
import cn.edu.seu.cose.imusic.util.MusicUtil;

public class MusicControl extends Binder {

    private Fragment currentFragment;
    public boolean isPlay;
    public MediaPlayer mediaPlayer;
    public int index;//当前歌曲序号
    private Visualizer visualizer;//频谱
    public SimpleDateFormat time;
    public boolean isOk;
    private List<Music> rawList;//raw中文件
    private List<Music> localList;//本地文件
    public List<Music> musicList;//音乐文件列表
    //public String[] rawMusic={"林俊杰-豆浆油条", "自由飞翔", "荷塘月色","苏打绿-小情歌", "孤勇者", "错位时空"};

    public MusicControl(Fragment currentFragment){
        this.currentFragment=currentFragment;//用来读取
        isPlay=false;//默认暂停
        isOk=true;
        mediaPlayer=new MediaPlayer();

        //初始化raw文件列表
        rawList= MusicUtil.getMediaFilesFromRaw(this.currentFragment);
        localList=MusicUtil.getLocalMedias(this.currentFragment);
        musicList=new ArrayList<>();
        //musicList.addAll(rawList);
        musicList.addAll(localList);
        File fileDir = Environment.getExternalStorageDirectory();//SD卡根目录
        String srcDir=fileDir+"/_iMusic/";
        String musicFile = fileDir + "/_iMusic/001.mp3";
        time= new SimpleDateFormat("m:ss");
        try {
            mediaPlayer.setDataSource(musicFile);
            mediaPlayer.prepare();
            isOk=true;
        } catch (IOException e) {
            e.printStackTrace();
            isOk=false;
        }
    }

    public void musicPlay(){
        mediaPlayer.start();
        isPlay=true;
    }

    public void musicStop(){
        mediaPlayer.stop();
        isPlay=false;
    }

    public void musicPause(){
        mediaPlayer.pause();
        isPlay=false;
    }

    //计算时长,歌曲是多少分钟多少秒钟
    public String getDuration() {
        int duration=mediaPlayer.getDuration();
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



    //下一首
    public void playNext()
    {
        index=(index+1)%musicList.size();
        Music music=musicList.get(index);
        MusicUtil.initMediaPlayer(mediaPlayer,music);
    }

    //上一首
    public void playPre()
    {
        if(index<=0) index=musicList.size()-1;
        else index=index-1;
        Music music=musicList.get(index);
        MusicUtil.initMediaPlayer(mediaPlayer,music);
    }

    public void rawSelect(int id)
    {
        index=id;
        Log.i("播放ID",id+"");
        Music music=musicList.get(index);
        MusicUtil.initMediaPlayer(mediaPlayer,music);
    }

    public void musicExit()
    {
        mediaPlayer.stop();
        mediaPlayer.release();
        //关闭raw文件读取
        for(Music music:musicList){
            if(music.getFileType()==Music.FileType.rawFile)
            {
                try {
                    music.getAfd().close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
