package cn.edu.seu.cose.imusic.ui.home;

import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;

import androidx.fragment.app.Fragment;
import cn.edu.seu.cose.imusic.R;
import cn.edu.seu.cose.imusic.util.Music;

public class MusicListAdapter extends BaseAdapter {

    public String[] songs = {"林俊杰-豆浆油条", "自由飞翔", "荷塘月色","苏打绿-小情歌", "孤勇者", "错位时空"};
    private Fragment fragment;
    private List<Music> musicList;

    public MusicListAdapter(Fragment fragment,List<Music> musicList)
    {
        //this.count=count;
        this.fragment=fragment;
        this.musicList=musicList;
    }
    @Override
    public int getCount() {
        return musicList.size();
    }

    @Override
    public Object getItem(int i) {
        return musicList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        //绑定好VIew，然后绑定控件
        view=View.inflate(fragment.getContext(), R.layout.music_item,null);
        TextView tv_name=view.findViewById(R.id.item_name);
        ImageView iv=view.findViewById(R.id.iv);
        TextView tv_singer=view.findViewById(R.id.item_singer);
        TextView tv_url=view.findViewById(R.id.item_url);
        TextView tv_duration=view.findViewById(R.id.item_duration);
        //设置控件显示的内容，就是获取的歌曲名和歌手图片
        //tv_name.setText(countryArray[i]);
        Music music=musicList.get(i);
        tv_name.setText(musicList.get(i).getName()+"——"+musicList.get(i).getSinger());
        //tv_singer.setText(musicList.get(i).getSinger());
        tv_url.setText(musicList.get(i).getUrl());
        //tv_duration.setText(musicList.get(i).getDurationStr());
        //iv.setImageResource(icons[i]);
        return view;
    }
}
