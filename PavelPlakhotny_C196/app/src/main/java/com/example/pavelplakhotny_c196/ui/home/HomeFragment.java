package com.example.pavelplakhotny_c196.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.example.pavelplakhotny_c196.R;
import com.example.pavelplakhotny_c196.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {
    private HomeViewModel viewModel = new HomeViewModel();
    private FragmentHomeBinding binding;


    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_home, container, false);
        binding.setLifecycleOwner(getViewLifecycleOwner());
        ;
        binding.textHome.setText(viewModel.getText().getValue());
        return binding.getRoot();
    }
}
