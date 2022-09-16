package cn.edu.seu.cose.imusic.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.widget.RemoteViews;

import cn.edu.seu.cose.imusic.service.MusicService;
import cn.edu.seu.cose.imusic.util.NotificationUtil;


//广播接收器
public class MusicReceiver extends BroadcastReceiver {

    public static final String TAG = "MusicReceiver";

    public static final String ACTION_STATUS_BAR = "music.Notification_ACTIONS";
    public static final String EXTRA = "play";//前缀
    public static final String EXTRA_NEXT = "play_next";//必须前缀一致
    public static final String EXTRA_PREV = "play_prev";//必须前缀一致
    public static final String EXTRA_PLAY_PAUSE = "play_pause";//必须前缀一致
    public static final String EXTRA_EXIT = "play_exit";//必须前缀一致

    private MusicService musicService;

    private RemoteViews remoteViews=null;

    public void setMusicService(MusicService musicService) {
        this.musicService = musicService;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        //throw new UnsupportedOperationException("Not yet implemented");
        if (intent == null || TextUtils.isEmpty(intent.getAction())) {
            return;
        }

        if(musicService==null) {
            Log.d(TAG, "广播：service空");
            return;
        }

        String extra = intent.getStringExtra(EXTRA);
        if (TextUtils.equals(extra, EXTRA_NEXT)) {
            //AudioPlayer.get().next();
            musicService.playNext();
            Log.d(TAG,"广播：NEXT");
        } else if (TextUtils.equals(extra, EXTRA_PLAY_PAUSE)) {
            musicService.musicPlay();
            Log.d(TAG,"广播：PLAY_PAUSE");
        } else if(TextUtils.equals(extra,EXTRA_PREV)){
            musicService.playPre();
            Log.d(TAG,"广播：PREV");
        } else if(TextUtils.equals(extra,EXTRA_EXIT)){
            Log.d(TAG,"广播：EXIT");
            //关闭通知，退出该APP
            NotificationUtil.closePlayNotification(context.getApplicationContext());
        }
    }
}