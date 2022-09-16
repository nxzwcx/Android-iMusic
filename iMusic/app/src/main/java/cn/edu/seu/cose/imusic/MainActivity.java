package cn.edu.seu.cose.imusic;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.media.audiofx.Visualizer;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import cn.edu.seu.cose.imusic.databinding.ActivityMainBinding;
import cn.edu.seu.cose.imusic.service.MusicService;
import cn.edu.seu.cose.imusic.ui.home.HomeFragment;
import cn.edu.seu.cose.imusic.ui.music.MusicFragment;
import cn.edu.seu.cose.imusic.util.Music;
import cn.edu.seu.cose.imusic.util.MusicUtil;
import cn.edu.seu.cose.imusic.util.NotificationUtil;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";

    private ActivityMainBinding binding;
    private Visualizer visualizer;
    private HomeFragment homeFragment;
    private MusicFragment musicFragment;

    public MusicService musicService;
    private ServiceConnection serviceConnection;
    private boolean isUnbind =false;//记录服务是否被解绑，默认没有
    /**
     * 请求读文件权限
     */
    private static final int REQUEST_READ_STORAGE = 0x01;
    /**
     * 请求读取文件
     */
    private static final int REQUEST_RECORD_AUDIO = 0X01;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //显示通知栏
        //NotificationUtil.initPlayNotification(this);

   /*     String CHANNEL_ID = "channel_0";
        String CHANNEL_NAME = "channel_0";

        NotificationManager manager = (NotificationManager) this.getSystemService(NOTIFICATION_SERVICE);
        Notification notification = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_LOW);
            manager.createNotificationChannel(channel);
        }
        notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("奖励百万红包")
                .setContentText("你点击查看详情")
                .setAutoCancel(true)
                .setWhen(System.currentTimeMillis())
                .setShowWhen(true)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setDefaults(Notification.DEFAULT_SOUND|Notification.DEFAULT_VIBRATE)
                //.setContentIntent(pi)
                .build();
        manager.notify(1, notification);   //1为通知ID，可随意设置*/

        //NotificationUtil.initPlayNotification(this);
/*       String id = "channel_012";
        String des = "111";

        NotificationManager manager = (NotificationManager) this.getSystemService(NOTIFICATION_SERVICE);
        //NotificationChannel channel = new NotificationChannel(id, des, NotificationManager.IMPORTANCE_MIN);
        //manager.createNotificationChannel(channel);
        //Notification notification = new NotificationCompat.Builder(this, id)
        Notification notification = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            notification = new Notification.Builder(MainActivity.this, id)
                    //Notification notification = new Notification.Builder(MainActivity.this)
                    .setContentTitle("Base Notification View")
                    .setContentText("您有一条新通知")
                    .setSmallIcon(R.drawable.ic_launcher_background)
                    .setStyle(new Notification.MediaStyle())
                    .setAutoCancel(false)
                    .addExtras(new Bundle())
                    .build();
        }
        manager.notify(1, notification);
        Log.i(TAG,"通知栏");*/


/*        NotificationManager manager = (NotificationManager) this.getSystemService(NOTIFICATION_SERVICE);
        //NotificationChannel channel = new NotificationChannel(id, des, NotificationManager.IMPORTANCE_MIN);
        //manager.createNotificationChannel(channel);
        //Notification notification = new Notification.Builder(MainActivity.this, id)
        Notification notification = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            notification = new Notification.Builder(MainActivity.this,id)
                    .setContentTitle("qianqian")
                    .setContentText("您有一条新通知")
                    .setSmallIcon(R.drawable.ic_launcher_background)
                    .setStyle(new Notification.MediaStyle())
                    .setAutoCancel(false)
                    .addExtras(new Bundle())
                    .build();
        }
        manager.notify(1, notification);*/
        /*Notification notification = new NotificationCompat.Builder(MainActivity.this,id)
                .setContentTitle("这是测试通知标题")  //设置标题
                .setContentText("这是测试通知内容") //设置内容
                .setWhen(System.currentTimeMillis())  //设置时间
                .setSmallIcon(R.mipmap.ic_launcher)  //设置小图标
                // .setLargeIcon(R.mipmap.ic_launcher_round)   //设置大图标
                .build();
        manager.notify(1,notification);
*/

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications).build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        //运行时权限处理，动态申请WRITE_EXTERNAL_STORAGE权限
        //PackageManager.PERMISSION_GRANTED 表示有权限， PackageManager.PERMISSION_DENIED 表示无权限
        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
            //这里会调用后面的onRequestPermissionResult
        }else{
            //initMediaPlayer(0);
            Toast.makeText(this, "文件访问已授权！", Toast.LENGTH_SHORT).show();
        }

        if(ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},REQUEST_READ_STORAGE);
            //这里会调用后面的onRequestPermissionResult
        }else{
            //initMediaPlayer(0);
            Toast.makeText(this, "文件读取已授权！", Toast.LENGTH_SHORT).show();
        }

        //调用音乐服务
        //bindMusicService();
        if(musicService==null)
            Log.i(TAG,"m: service空");
        else
            Log.i(TAG,"m: service非空");

    }

    public void onRequestPermissionsResult(int requestCode,String[] permissions,int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //initMediaPlayer(0);
                    Toast.makeText(this, "文件访问已授权！", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "拒绝权限将无法使用", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
              default:
                break;
        }

        //cx add
        if (requestCode == REQUEST_RECORD_AUDIO) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "RECORD_AUDIO已授权！", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "RECORD_AUDIO未授权！", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (serviceConnection != null) {
            //停止服务
            unbindService(serviceConnection);
        }
    }

    //启动服务
    //在Activity中调用 bindService 保持与 Service 的通信：
    public void bindMusicService() {
        Intent intent = new Intent(this, MusicService.class);
        serviceConnection = new MusicServiceConnection();
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
        if(musicService==null)
            Log.i(TAG,"m2: service空");
        else
            Log.i(TAG,"m2: service非空");
    }

    //ServiceConnection与Service建立连接
    private class MusicServiceConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.i(TAG, "ServiceConnection连接成功！");
            MusicService.MusicBinder musicBinder= (MusicService.MusicBinder) iBinder;
            musicService=musicBinder.getService();
            isUnbind=false;//绑定服务
            List<Music> musicList=new ArrayList<>();
            List<Music> rawList= MusicUtil.getMediaFilesFromRaw(getApplicationContext());
            List<Music> localList=MusicUtil.getLocalMedias(getApplicationContext());
            musicList.addAll(rawList);
            musicList.addAll(localList);
            musicService.musicList=musicList;
            Log.i(TAG,"service的音乐列表已加载");
            //onservicebound
//            setupView();
//            updateWeather();
//            controlPanel = new ControlPanel(flPlayBar);
//            naviMenuExecutor = new NaviMenuExecutor(this);
//            AudioPlayer.get().addOnPlayEventListener(controlPanel);
//            QuitTimer.get().setOnTimerListener(this);
//            parseIntent();

            if(musicService==null)
                Log.i(TAG,"m: service空");
            else
                Log.i(TAG,"m: service非空");

        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.i(TAG, "ServiceConnection已断开！");
            musicService=null;
            isUnbind=true;//解绑服务
        }
    }
}