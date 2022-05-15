package cn.edu.seu.cose.imusic.service;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import java.io.IOException;
import java.util.List;

import androidx.fragment.app.Fragment;
import cn.edu.seu.cose.imusic.util.Music;

public class MusicService extends Service {

    private static final String TAG = "Service";
    private Fragment currentFragment;
    public MediaPlayer mediaPlayer;
    public List<Music> musicList;//音乐文件列表

    public class MusicBinder extends Binder {
        public MusicService getService() { return MusicService.this; }
    }

    //创建handler，在子线程中通过handler的post(Runnable)方法更新UI信息。
    private Handler serviceHandler = new Handler();

    public MusicService()
    {
    }
    @Override
    public IBinder onBind(Intent intent) {
        return new MusicBinder();
    }

    @Override
    public void onCreate(){
        super.onCreate();
        //创建音乐播放器对象
        mediaPlayer=new MediaPlayer();
        serviceHandler.post(runnable);
    }

    //销毁多媒体播放器
    @Override
    public void onDestroy(){
        if(mediaPlayer==null) return;
        mediaPlayer.stop();
        mediaPlayer.release();
        //关闭raw文件读取
        if(musicList!=null) {
            for (Music music : musicList) {
                if (music.getFileType() == Music.FileType.rawFile) {
                    try {
                        music.getAfd().close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        super.onDestroy();
    }


    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            if(mediaPlayer==null) return;
            if(!mediaPlayer.isPlaying()) return;
            int duration=mediaPlayer.getDuration();//获取歌曲总时长
            int currentPosition=mediaPlayer.getCurrentPosition();//获取播放进度
            Message msg= serviceHandler.obtainMessage();//创建消息对象
            //将音乐的总时长和播放进度封装至bundle中
            Bundle bundle=new Bundle();
            bundle.putInt("duration",duration);
            bundle.putInt("currentPosition",currentPosition);
            //再将bundle封装到msg消息对象中
            msg.setData(bundle);
            serviceHandler.postDelayed(runnable,300);
        }

    };

    public static class MusicServiceConnection implements ServiceConnection {

        private MusicService.MusicBinder musicBinder;

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            musicBinder= (MusicBinder) iBinder;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    }
}


