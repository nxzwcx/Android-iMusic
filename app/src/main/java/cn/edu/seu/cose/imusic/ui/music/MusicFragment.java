package cn.edu.seu.cose.imusic.ui.music;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import cn.edu.seu.cose.imusic.databinding.FragmentMusicBinding;
import cn.edu.seu.cose.imusic.service.MusicControl;

public class MusicFragment extends Fragment {

    private FragmentMusicBinding binding;

    //cx add

    //主线程创建handler，在子线程中通过handler的post(Runnable)方法更新UI信息。
    private Handler musicHandler = new Handler();
    private Button buttonPre;
    private Button buttonPlay;
    private Button buttonNext;
    private SeekBar musicSeekBar;
    private TextView timeTextView;
    private MusicControl musicControl;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        MusicViewModel musicViewModel =
                new ViewModelProvider(this).get(MusicViewModel.class);

        binding = FragmentMusicBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textMusic;
        musicViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);

        timeTextView = binding.textTime;
        //musicControl=new MusicControl();
        musicControl=new MusicControl(this);
        //歌曲进度条
        musicSeekBar= binding.seekBar;
        musicSeekBar.setMax(musicControl.mediaPlayer.getDuration());
        musicSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean fromUser) {
                //如果不判断是否来自用户操作进度条，会不断执行下面语句块里面的逻辑
                if(fromUser){
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

        buttonPlay= binding.buttonPlayStop;
        buttonPlay.setOnClickListener(new View.OnClickListener(){
            @Override
            //重写onClick函数
            public void onClick(View v) {
                if(!musicControl.isPlay) {
                    Toast.makeText(v.getContext(), "播放音乐", Toast.LENGTH_SHORT).show();
                    musicControl.musicPlay();
                    if(!musicControl.isOk)
                    {
                        Toast.makeText(v.getContext(), "路径_iMusic/001.mp3不存在", Toast.LENGTH_SHORT).show();
                    }
                    buttonPlay.setText("⏸暂停");
                }
                //正在播放，点击暂停
                else{
                    Toast.makeText(v.getContext(), "暂停音乐", Toast.LENGTH_SHORT).show();
                    musicControl.musicPause();
                    buttonPlay.setText("▶播放");
                }

            }
        });

        buttonPre= binding.buttonPlayPre;
        buttonPre.setOnClickListener(new View.OnClickListener(){
            @Override
            //重写onClick函数
            public void onClick(View v) {
                Toast.makeText(v.getContext(),"上一首",Toast.LENGTH_SHORT).show();
            }
        });

        buttonNext= binding.buttonPlayNext;
        buttonNext.setOnClickListener(new View.OnClickListener(){
            @Override
            //重写onClick函数
            public void onClick(View v) {
                Toast.makeText(v.getContext(),"下一首",Toast.LENGTH_SHORT).show();
            }
        });

        musicHandler.post(updateUI);

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        musicHandler.removeCallbacks(updateUI);
        musicControl.musicExit();
        binding = null;
    }

    //更新UI
    private Runnable updateUI = new Runnable() {
        @Override
        public void run() {
            //获取歌曲进度并在进度条上展现
            musicSeekBar.setProgress(musicControl.mediaPlayer.getCurrentPosition());
            //获取播放位置
            timeTextView.setText("当前进度："+musicControl.time.format(musicControl.mediaPlayer.getCurrentPosition()) + "s");
            musicHandler.postDelayed(updateUI,1000);
        }

    };
}