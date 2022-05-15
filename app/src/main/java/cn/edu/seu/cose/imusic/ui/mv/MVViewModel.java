package cn.edu.seu.cose.imusic.ui.mv;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MVViewModel extends ViewModel {
    private final MutableLiveData<String> mText;

    public MVViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("你喜欢的MV在这里");
    }

    public LiveData<String> getText() {
        return mText;
    }
}