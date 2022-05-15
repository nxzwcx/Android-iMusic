package cn.edu.seu.cose.imusic.ui.music;

import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MusicViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public MusicViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("你喜欢的音乐在这里");
    }

    public LiveData<String> getText() {
        return mText;
    }

}