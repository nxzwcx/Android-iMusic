package cn.edu.seu.cose.imusic.ui.mv;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import cn.edu.seu.cose.imusic.R;
import cn.edu.seu.cose.imusic.databinding.FragmentMvBinding;

public class MVFragment extends Fragment {

    private FragmentMvBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        //return inflater.inflate(R.layout.fragment_mv, container, false);
        MVViewModel mvViewModel =
                new ViewModelProvider(this).get(MVViewModel.class);

        binding = FragmentMvBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textMv;
        mvViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}