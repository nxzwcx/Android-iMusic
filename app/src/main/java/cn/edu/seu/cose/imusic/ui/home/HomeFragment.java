package cn.edu.seu.cose.imusic.ui.home;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.os.Bundle;
import android.os.Handler;
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
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;
import cn.edu.seu.cose.imusic.R;
import cn.edu.seu.cose.imusic.databinding.FragmentHomeBinding;

import cn.edu.seu.cose.imusic.lrcview.LrcUtils;
import cn.edu.seu.cose.imusic.lrcview.LrcView;
import cn.edu.seu.cose.imusic.lrcview.SearchLrcUtils;
import cn.edu.seu.cose.imusic.service.MusicService;
import cn.edu.seu.cose.imusic.ui.music.MusicFragment;
import cn.edu.seu.cose.imusic.ui.play.PlayLRCFragment;
import cn.edu.seu.cose.imusic.util.Music;
import cn.edu.seu.cose.imusic.service.MusicControl;
import cn.edu.seu.cose.imusic.util.MusicFrequencyView;

public class HomeFragment extends Fragment {

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
    private MusicControl musicControl;
    private MusicFrequencyView musicFrequencyView;
    private View musicListView;//自定义列表，使用布局文件music_listview.xml
    private Switch aSwitch;
    private MusicFragment musicFragment;
    private HomeFragment homeFragment;
    private PlayLRCFragment playLRCFragment;
    private ViewPager playViewPager;
    private View playLRCView;
    private LrcView lrcView;
    private boolean lrcIsEmpty;

    private MusicService musicService;
    private MusicService.MusicServiceConnection conn;
    //记录服务是否被解绑，默认没有
    private boolean isUnbind =false;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        homeFragment=this;

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //musicControl=new MusicControl();
        musicControl = new MusicControl(this);
        // Array of strings...
        musicListView = inflater.inflate(R.layout.music_list, null);
        ListView listView=musicListView.findViewById(R.id.listview_songs);
        //ListView listView = binding.listviewSongs;
        MusicListAdapter adapter = new MusicListAdapter(this,musicControl.musicList);
        listView.setAdapter(adapter);
        //列表元素的点击监听器
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Toast.makeText(view.getContext(), "开始播放"+position, Toast.LENGTH_SHORT).show();
                //musicInfoTextView.setText("正在播放："+musicControl.rawMusic[position]);
                musicControl.rawSelect(position);
                buttonPlay.setText("⏸暂停");
                musicInfoTextView.setText("正在播放：" + musicControl.musicList.get(position).getName());
                onChangeMusicUI(musicControl.musicList.get(musicControl.index));
            }
        });

        //final TextView textView = binding.textHome;
        //homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        musicInfoTextView = binding.textMusicInfo;
        stateTextView = binding.textMusicState;
        voiceTextView = binding.textMusicVoice;

        timeTextView = binding.textTime;
        totaltimeTextView = binding.textTotaltime;
        totaltimeTextView.setText(musicControl.getDuration());

        //频谱显示
        musicFrequencyView = binding.viewMusicFrequency;
        musicFrequencyView.setMediaPlayer(musicControl.mediaPlayer);
        //歌曲进度条
        musicSeekBar = binding.seekBar;
        musicSeekBar.setMax(musicControl.mediaPlayer.getDuration());
        musicSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean fromUser) {
                //如果不判断是否来自用户操作进度条，会不断执行下面语句块里面的逻辑
                if (fromUser) {
                    musicControl.mediaPlayer.start();
                    buttonPlay.setText("⏸暂停");
                    musicControl.mediaPlayer.seekTo(seekBar.getProgress());
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        buttonPlay = binding.buttonPlayStop;
        buttonPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            //重写onClick函数
            public void onClick(View v) {
                if (!musicControl.isPlay) {
                    //Toast.makeText(v.getContext(), "播放音乐", Toast.LENGTH_SHORT).show();
                    musicControl.musicPlay();
                    //if(musicControl.musicList.size()>0) musicControl.isOk=true;
                    onChangeMusicUI(musicControl.musicList.get(musicControl.index));
                    if (!musicControl.isOk) {
                        Toast.makeText(v.getContext(), "路径_iMusic/001.mp3不存在,播放测试音乐：林俊杰-豆浆油条", Toast.LENGTH_SHORT).show();
                        AssetFileDescriptor afd = getResources().openRawResourceFd(R.raw.doujiangyoutiao);
                        try {
                            musicControl.mediaPlayer.reset();
                            musicControl.mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength());
                            musicControl.mediaPlayer.prepare();
                            musicControl.mediaPlayer.start();
                            musicControl.isOk = true;

                            onChangeMusicUI(musicControl.musicList.get(musicControl.index));
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
                    buttonPlay.setText("⏸暂停");
                }
                //正在播放，点击暂停
                else {
                    //Toast.makeText(v.getContext(), "暂停音乐", Toast.LENGTH_SHORT).show();
                    musicControl.musicPause();
                    buttonPlay.setText("▶播放");
                }

            }
        });

        buttonPre = binding.buttonPlayPre;
        buttonPre.setOnClickListener(new View.OnClickListener() {
            @Override
            //重写onClick函数
            public void onClick(View v) {
                //Toast.makeText(v.getContext(),"上一首",Toast.LENGTH_SHORT).show();
                musicControl.playPre();
                buttonPlay.setText("⏸暂停");
                onChangeMusicUI(musicControl.musicList.get(musicControl.index));
            }
        });

        buttonNext = binding.buttonPlayNext;
        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            //重写onClick函数
            public void onClick(View v) {
                //Toast.makeText(v.getContext(),"下一首",Toast.LENGTH_SHORT).show();
                musicControl.playNext();
                //musicSeekBar.setMax(musicControl.mediaPlayer.getDuration());
                //totaltimeTextView.setText(musicControl.getDuration());
                buttonPlay.setText("⏸暂停");
                onChangeMusicUI(musicControl.musicList.get(musicControl.index));
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

        //绑定Service
        initService();

        return root;
        //return musicListView;
    }

    private void initService()
    {
        Intent intent=new Intent(getContext(),MusicService.class);
        conn= new MusicService.MusicServiceConnection();//创建服务连接对象
        getActivity().bindService(intent,conn, Context.BIND_AUTO_CREATE);//绑定服务
    }

    private void unbindService(boolean isUnbind)
    {
        //如果解绑了
        if(!isUnbind){
            musicControl.musicStop();//音乐暂停播放
            //getActivity().unbindService(conn);//解绑服务
            getActivity().unbindService(conn);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        //musicHandler.removeCallbacks(updateUI);
        //musicControl.musicExit();
        binding = null;
        unbindService(isUnbind);//解绑服务
    }

    //更新UI
    private Runnable updateUI = new Runnable() {
        @Override
        public void run() {

            musicSeekBar.setMax(musicControl.mediaPlayer.getDuration());//可加可不加
            musicSeekBar.setProgress(musicControl.mediaPlayer.getCurrentPosition()); //获取歌曲进度并在进度条上展现
            //获取播放位置
            timeTextView.setText(musicControl.time.format(musicControl.mediaPlayer.getCurrentPosition()) + "s");
            if(musicControl.mediaPlayer.isPlaying())
            {
                buttonPlay.setText("⏸暂停");
                stateTextView.setText("状态：播放");
            }
            else
            {
                buttonPlay.setText("▶播放");
                stateTextView.setText("状态：暂停");
            }

            lrcView.updateTime(musicControl.mediaPlayer.getCurrentPosition());

            //musicHandler.postDelayed(updateUI,1000);
            musicHandler.postDelayed(updateUI,300);
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
        setLrc(music);
        TextView lrcUrl=playLRCView.findViewById(R.id.text_lrcUrl);
        if(lrcIsEmpty)lrcUrl.setText("歌词url:");
        else{
            lrcUrl.setText("歌词url:"+music.getUrl().toLowerCase()
                    .replace(".mp3",".lrc")
                    .replace(".flac",".lrc"));}
        //刷新进度条，不然点击进度条后闪退
        musicSeekBar.setMax(musicControl.mediaPlayer.getDuration());
        totaltimeTextView.setText(musicControl.getDuration());
        musicInfoTextView.setText("正在播放："+musicControl.musicList.get(musicControl.index).getName());
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
}