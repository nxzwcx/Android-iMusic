package cn.edu.seu.cose.imusic.ui.home;

import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public HomeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("你喜欢的音乐在这里");
    }

    public LiveData<String> getText() {
        return mText;
    }

}