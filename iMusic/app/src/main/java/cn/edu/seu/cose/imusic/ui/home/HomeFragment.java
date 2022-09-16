package cn.edu.seu.cose.imusic.ui.home;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;
import cn.edu.seu.cose.imusic.MainActivity;
import cn.edu.seu.cose.imusic.R;
import cn.edu.seu.cose.imusic.databinding.FragmentHomeBinding;

import cn.edu.seu.cose.imusic.lrcview.LrcUtils;
import cn.edu.seu.cose.imusic.lrcview.LrcView;
import cn.edu.seu.cose.imusic.lrcview.SearchLrcUtils;
import cn.edu.seu.cose.imusic.service.MusicService;
import cn.edu.seu.cose.imusic.ui.play.PlayLRCFragment;
import cn.edu.seu.cose.imusic.util.Music;
import cn.edu.seu.cose.imusic.util.MusicFrequencyView;
import cn.edu.seu.cose.imusic.util.MusicUtil;
import cn.edu.seu.cose.imusic.util.NotificationUtil;

import static android.content.Context.AUDIO_SERVICE;

public class HomeFragment extends Fragment {

    public static final String TAG = "HomeFragment";

    private MainActivity mActivity;
    private FragmentHomeBinding binding;
    //cx add
    //主线程创建handler，在子线程中通过handler的post(Runnable)方法更新UI信息。
    private Handler musicHandler = new Handler();
    private Button buttonPre;
    private Button buttonPlay;
    private Button buttonNext;
    private SeekBar musicSeekBar;
    private TextView musicInfoTextView;
    private TextView stateTextView;
    private TextView voiceTextView;
    private TextView timeTextView;
    private TextView totaltimeTextView;
    private MusicFrequencyView musicFrequencyView;
    private View musicListView;//自定义列表，使用布局文件music_listview.xml
    private Switch aSwitch;
    private PlayLRCFragment playLRCFragment;
    private ViewPager playViewPager;
    private View playLRCView;
    private LrcView lrcView;
    private boolean lrcIsEmpty;

    private MusicService musicService;
    private MusicService.MusicBinder musicBinder;
    private ServiceConnection serviceConnection;
    private  boolean isUnbind=false;//是否解绑
    private List<Music> musicList;

    @Nullable
    private Bundle savedInstanceState;


    //通过该方法获取MainActivity，从而获取service
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = (MainActivity) activity;
    }


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);


        if(mActivity.musicService==null)
            Log.i(TAG,"oncreate view service空");
        else
            Log.i(TAG,"oncreate view service非空");
        //结果是空，只能在ServiceConnection中获取的service才非空

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        //初始化raw文件列表
        musicList=new ArrayList<>();
        List<Music> rawList=null;
        List<Music> localList=MusicUtil.getLocalMedias(this);
        if(localList.size()>=0) {
            rawList = MusicUtil.getMediaFilesFromRaw(this);
            musicList.addAll(rawList);
        }
        musicList.addAll(localList);

        musicListView = inflater.inflate(R.layout.music_list, null);
        //ListView listView = binding.listviewSongs;


        //final TextView textView = binding.textHome;
        //homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        musicInfoTextView = binding.textMusicInfo;
        stateTextView = binding.textMusicState;
        voiceTextView = binding.textMusicVoice;

        timeTextView = binding.textTime;
        totaltimeTextView = binding.textTotaltime;
        //totaltimeTextView.setText(musicControl.getDuration());
        //totaltimeTextView.setText(musicService.getDuration());

        //频谱显示
        musicFrequencyView = binding.viewMusicFrequency;
        //musicFrequencyView.setMediaPlayer(musicControl.mediaPlayer);
        //musicFrequencyView.setMediaPlayer(mActivity.musicService.mediaPlayer);
        //歌曲进度条
        musicSeekBar = binding.seekBar;
        //musicSeekBar.setMax(musicControl.mediaPlayer.getDuration());
        //musicSeekBar.setMax(musicService.mediaPlayer.getDuration());
        musicSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean fromUser) {
                //如果不判断是否来自用户操作进度条，会不断执行下面语句块里面的逻辑
                if (fromUser) {
                    //musicControl.mediaPlayer.start();
                    musicService.mediaPlayer.start();
                    buttonPlay.setText("⏸暂停");
                    //musicControl.mediaPlayer.seekTo(seekBar.getProgress());
                    musicService.mediaPlayer.seekTo(seekBar.getProgress());
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });



        aSwitch = binding.switchPage;
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                //控制开关字体颜色
                if (b) {
                    //aSwitch.setSwitchTextAppearance(MainActivity.this,R.style.s_true);
                    Log.i("info","列表页面");
                    Toast.makeText(getContext(), "列表页面", Toast.LENGTH_SHORT).show();
                    //MusicFragment musicFragment=new MusicFragment();;
                    /*getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_music,new MusicFragment(),null)
                            .addToBackStack(null)
                            .commit();*/
                    /*FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.fragment_music, musicFragment);
                    ft.commitAllowingStateLoss();*/

                   /* FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    if (playLRCFragment == null) {
                        playLRCFragment = new PlayLRCFragment();
                        //ft.replace(android.R.id.content, musicFragment);
                        //ft.replace(android.R.id.content, playLRCFragment);
                        ft.replace(R.id.music_lrc, playLRCFragment);
                    } else {
                        ft.show(playLRCFragment);
                    }
                    ft.commitAllowingStateLoss();*/

                    //原来采用Fragment跳转，但原来内容不消失，不知道问题出在哪里。只能采用ViewPage方案
                    playViewPager.setCurrentItem(1);

                } else {
                    //aSwitch.setSwitchTextAppearance(MainActivity.this,R.style.x
                    // 1);
                    Log.i("info","播放页面");
                    Toast.makeText(getContext(), "播放页面", Toast.LENGTH_SHORT).show();
                    //getActivity().getSupportFragmentManager()
                   /* getActivity().getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_home,new HomeFragment(),null)
                            .addToBackStack(null)
                            .commit();*/
                   /* FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                    ft.hide(playLRCFragment);
                    ft.commitAllowingStateLoss();*/

                    playViewPager.setCurrentItem(0);
                }
            }
        });

        musicHandler.post(updateUI);

        //滑动
        List<View> pageList=new ArrayList<>();
        pageList.add(musicListView);
        //View playLRCView = inflater.inflate(R.layout.view_play_lrc, null);
        //PlayLRCFragment playLRCFragment=new PlayLRCFragment();
        playLRCView = inflater.inflate(R.layout.view_play_lrc, null);
        lrcView=playLRCView.findViewById(R.id.lrc_view);
        // 加载歌词文本
        String mainLrcText = LrcUtils.getLrcText(getContext(),"send_it_en.lrc");
        String secondLrcText = LrcUtils.getLrcText(getContext(),"send_it_cn.lrc");
        lrcView.loadLrc(mainLrcText, secondLrcText);
        pageList.add(playLRCView);
        //pageList.add(playLRCFragment);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(this,pageList);
        playViewPager=binding.viewpager;
        playViewPager.setAdapter(viewPagerAdapter);
        return root;
        //return musicListView;
    }


    @Override
    public void onStart() {
        super.onStart();
        //**启动服务
        //mActivity.bindMusicService();
        bindMusicService();
        if(mActivity.musicService==null)
            Log.i(TAG,"onstart service空");
        else
            Log.i(TAG,"onstart service非空");
        //结果是空，说明尚未建立连接

        //setListener();//监听按钮事件

    }

    //启动服务
    //在Activity中调用 bindService 保持与 Service 的通信：
    public void bindMusicService() {
        Intent intent = new Intent(getActivity(), MusicService.class);
        serviceConnection = new MusicServiceConnection();
        getActivity().bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
        if(musicService==null)
            Log.i(TAG,"fragment绑定服务: service空");
        else
            Log.i(TAG,"fragment绑定服务: service非空");
    }

    private void setListener() {

        //播放按钮监听事件
        buttonPlay = binding.buttonPlayStop;

        ListView listView=musicListView.findViewById(R.id.listview_songs);
        MusicListAdapter adapter = new MusicListAdapter(this,musicService.musicList);
        listView.setAdapter(adapter);
        //列表元素的点击监听器
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(view.getContext(), "开始播放"+position, Toast.LENGTH_SHORT).show();
                //musicInfoTextView.setText("正在播放："+musicControl.rawMusic[position]);
                //musicControl.rawSelect(position);
                musicService.rawSelect(position);
                buttonPlay.setText("⏸暂停");
                //musicInfoTextView.setText("正在播放：" + musicControl.musicList.get(position).getName());
                musicInfoTextView.setText("正在播放：" + musicService.musicList.get(position).getName());
                //totaltimeTextView.setText(musicService.getDuration());
                //onChangeMusicUI(musicControl.musicList.get(musicControl.index));
                onChangeMusicUI(musicService.musicList.get(musicService.index));
            }
        });

        buttonPre = binding.buttonPlayPre;
        buttonPre.setOnClickListener(new View.OnClickListener() {
            @Override
            //重写onClick函数
            public void onClick(View v) {
                //Toast.makeText(v.getContext(),"上一首",Toast.LENGTH_SHORT).show();
                musicService.playPre();
                buttonPlay.setText("⏸暂停");
                //totaltimeTextView.setText(musicService.getDuration());
                //onChangeMusicUI(musicService.musicList.get(musicService.index));

            }
        });

        buttonNext = binding.buttonPlayNext;
        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            //重写onClick函数
            public void onClick(View v) {
                //Toast.makeText(v.getContext(),"下一首",Toast.LENGTH_SHORT).show();
                musicService.playNext();
                //musicSeekBar.setMax(musicService.mediaPlayer.getDuration());
                //totaltimeTextView.setText(musicService.getDuration());
                buttonPlay.setText("⏸暂停");
                onChangeMusicUI(musicService.musicList.get(musicService.index));
            }
        });

        buttonPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            //重写onClick函数
            public void onClick(View v) {
                if (!musicService.mediaPlayer.isPlaying()) {
                    Log.i(TAG,"点击播放按钮,播放状态：停止");
                    Toast.makeText(v.getContext(), "播放音乐", Toast.LENGTH_SHORT).show();
                    //musicControl.musicPlay();
                    musicService.musicPlay();
                    if(musicService.musicList.size()<=0) return;
                    Log.i(TAG,musicService.musicList.size()+"歌曲数量");

                    onChangeMusicUI(musicService.musicList.get(musicService.index));
                    if (!musicService.isOk) {
                        Log.i(TAG,"点击播放按钮,isOk:false");
                        Toast.makeText(v.getContext(), "路径_iMusic/001.mp3不存在,播放测试音乐：林俊杰-豆浆油条", Toast.LENGTH_SHORT).show();
                        AssetFileDescriptor afd = getResources().openRawResourceFd(R.raw.doujiangyoutiao);
                        try {
                            musicService.mediaPlayer.reset();
                            musicService.mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
                            musicService.mediaPlayer.prepare();
                            musicService.mediaPlayer.start();

                            onChangeMusicUI(musicService.musicList.get(musicService.index));
                            String testLrc=SearchLrcUtils.getLrcTextFromAssets(getContext(),"doujiangyoutiao.lrc");
                            System.out.println(testLrc);
                            lrcView.loadLrc(testLrc);
                            musicInfoTextView.setText("正在播放测试歌曲：豆浆油条.mp3");
                            TextView lrcUrl=playLRCView.findViewById(R.id.text_lrcUrl);
                            lrcUrl.setText("歌词url: assets/doujiangyoutiao.lrc");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    Log.i(TAG,"点击播放按钮,isOk:true");
                    buttonPlay.setText("⏸暂停");

                }
                //正在播放，点击暂停
                else {
                    Log.i(TAG,"播放状态：进行");
                    //Toast.makeText(v.getContext(), "暂停音乐", Toast.LENGTH_SHORT).show();
                    musicService.musicPause();
                    buttonPlay.setText("▶播放");
                }
                //Log.i(TAG,"播放状态：异常");

            }
        });

        if(musicService.mediaPlayer.isPlaying())
        {
            buttonPlay.setText("⏸暂停");
            stateTextView.setText("状态：播放");
        }
        else
        {
            buttonPlay.setText("▶播放");
            stateTextView.setText("状态：暂停");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        musicHandler.removeCallbacks(updateUI);
        //mActivity.musicService.musicExit();
    }

    //更新UI
    private Runnable updateUI = new Runnable() {
        @Override
        public void run() {
            if(musicService!=null) {
                //musicFrequencyView.setMediaPlayer(mActivity.musicService.mediaPlayer);
                musicSeekBar.setMax(musicService.mediaPlayer.getDuration());//可加可不加
                musicSeekBar.setProgress(musicService.mediaPlayer.getCurrentPosition()); //获取歌曲进度并在进度条上展现
                //获取播放位置
                timeTextView.setText(musicService.time.format(musicService.mediaPlayer.getCurrentPosition()) + "s");
                totaltimeTextView.setText(musicService.getDuration());
                musicInfoTextView.setText("正在播放："+musicService.musicList.get(musicService.index).getName());
                if (musicService.mediaPlayer.isPlaying()) {
                    buttonPlay.setText("⏸暂停");
                    stateTextView.setText("状态：播放");
                } else {
                    buttonPlay.setText("▶播放");
                    stateTextView.setText("状态：暂停");
                }

                lrcView.updateTime(musicService.mediaPlayer.getCurrentPosition());
            }
            //musicHandler.postDelayed(updateUI,1000);
            musicHandler.postDelayed(updateUI,300);
            //Log.i(TAG,"update...");
        }

    };

    private void setLrc(final Music music) {
        if (music.getFileType()== Music.FileType.localFile) {
            String lrcPath = SearchLrcUtils.getLrcFilePath(music);
            if (!TextUtils.isEmpty(lrcPath)) {
                lrcView.loadLrc(new File(lrcPath));
                Toast.makeText(getContext(), "本地歌词匹配成功", Toast.LENGTH_SHORT).show();
                lrcIsEmpty=false;
            } else {
                lrcView.loadLrc("");
                lrcView.setLabel("暂无歌词@-@");
                lrcIsEmpty=true;
               /* new SearchLrc(music.getArtist(), music.getTitle()) {
                    @Override
                    public void onPrepare() {
                        // 设置tag防止歌词下载完成后已切换歌曲
                        mLrcView.setTag(music);

                        loadLrc("");
                        setLrcLabel("正在搜索歌词");
                    }

                    @Override
                    public void onExecuteSuccess(@NonNull String lrcPath) {
                        if (mLrcView.getTag() != music) {
                            return;
                        }

                        // 清除tag
                        mLrcView.setTag(null);

                        loadLrc(lrcPath);
                        setLrcLabel("暂无歌词");
                    }

                    @Override
                    public void onExecuteFail(Exception e) {
                        if (mLrcView.getTag() != music) {
                            return;
                        }

                        // 清除tag
                        mLrcView.setTag(null);

                        setLrcLabel("暂无歌词");
                    }
                }.execute();*/
                Toast.makeText(getContext(), "网络歌词匹配成功", Toast.LENGTH_SHORT).show();
            }
        } else {
            String lrcPath = SearchLrcUtils.getLrcDir() + SearchLrcUtils.getLrcFileName(music.getSinger(), music.getName());
            lrcView.loadLrc(new File(lrcPath));
        }
    }

    //每次切换歌曲时，更新UI内容
    private void onChangeMusicUI(Music music)
    {
        if(music==null) return;

        //此函数必须在mediaPlayer.play()之后才能执行，不然会报错！！！
        musicFrequencyView.setMediaPlayer(musicService.mediaPlayer);

        setLrc(music);
        TextView lrcUrl=playLRCView.findViewById(R.id.text_lrcUrl);
        if(lrcIsEmpty)lrcUrl.setText("歌词url:");
        else{
            lrcUrl.setText("歌词url:"+music.getUrl().toLowerCase()
                    .replace(".mp3",".lrc")
                    .replace(".flac",".lrc"));}
        //刷新进度条，不然点击进度条后闪退
        musicSeekBar.setMax(musicService.mediaPlayer.getDuration());
        timeTextView.setText(musicService.time.format(musicService.mediaPlayer.getCurrentPosition()) + "s");
        totaltimeTextView.setText(musicService.getDuration());
        musicInfoTextView.setText("正在播放："+musicService.musicList.get(musicService.index).getName());
    }

    //handler机制，可以理解为线程间的通信，我获取到一个信息，然后把这个信息告诉你，就这么简单
    public static Handler handler=new Handler() {//创建消息处理器对象
        // 在主线程中处理从子线程发送过来的消息
        public void handleMessage(Message msg) {
            Bundle bundle=msg.getData();//获取从子线程发送过来的音乐播放进度
            //获取当前进度currentPosition和总时长duration
            int duration=bundle.getInt("duration");
            int currentPosition=bundle.getInt("currentPosition");

        }
    };


    //ServiceConnection与Service建立连接
    private class MusicServiceConnection implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.i(TAG, "ServiceConnection连接成功！");
            MusicService.MusicBinder musicBinder= (MusicService.MusicBinder) iBinder;
            musicService=musicBinder.getService();
            isUnbind=false;//绑定服务
            musicService.musicList=musicList;
            Log.i(TAG,"service的音乐列表已加载");
            if(musicService==null)
                Log.i(TAG,"fragment conn: service空");
            else
                Log.i(TAG,"fragment conn: service非空");

            //musicFrequencyView.visualizer.setEnabled(true);
            //musicFrequencyView.visualizer.setEnabled(false);
            //musicFrequencyView.setMediaPlayer(musicService.mediaPlayer);
            //onChangeMusicUI(musicList.get(musicService.index));
            setListener();

            //NotificationUtil.initPlayNotification(getActivity());
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.i(TAG, "ServiceConnection已断开！");
            musicService=null;
            musicFrequencyView.setMediaPlayer(null);
            isUnbind=true;//解绑服务
        }
    }

}