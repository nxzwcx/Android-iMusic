package cn.edu.seu.cose.imusic.ui.home;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.PagerAdapter;

public class ViewPagerAdapter extends PagerAdapter {

    private HomeFragment homeFragment;
    private List<View> pageList;
    public ViewPagerAdapter(HomeFragment homeFragment,List<View> pageList) {
        this.homeFragment=homeFragment;
        this.pageList=pageList;
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==object;
    }

    @NonNull
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        container.addView(pageList.get(position));
        return pageList.get(position);
    }
}
