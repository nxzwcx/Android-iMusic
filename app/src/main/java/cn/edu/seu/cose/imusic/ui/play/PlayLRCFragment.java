package cn.edu.seu.cose.imusic.ui.play;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import cn.edu.seu.cose.imusic.R;
import cn.edu.seu.cose.imusic.lrcview.LrcView;

public class PlayLRCFragment extends Fragment {

    private PlayLRCViewModel mViewModel;
    private LrcView lrcView;

    public static PlayLRCFragment newInstance() {
        return new PlayLRCFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_play_lrc, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(PlayLRCViewModel.class);
        // TODO: Use the ViewModel
    }

}