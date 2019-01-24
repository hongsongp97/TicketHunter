package com.example.macbookpro.ticketapp.views.fragments.profilescreen;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import com.example.macbookpro.ticketapp.R;
import com.example.macbookpro.ticketapp.databinding.FragmentNavProfileBinding;
import com.example.macbookpro.ticketapp.models.User;
import com.example.macbookpro.ticketapp.views.base.BindingFragment;
import com.example.macbookpro.ticketapp.views.base.NavigationFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class NavProfileFragment extends NavigationFragment {

    FragmentNavProfileBinding binding;

    public static NavProfileFragment newInstance() {

        Bundle args = new Bundle();

        NavProfileFragment fragment = new NavProfileFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding = (FragmentNavProfileBinding) getViewBinding();
        binding.setUser(new User("asdasdas","Ice Tea", ""));
    }

    @Override
    protected BindingFragment onCreateMainFragment() {
        return ProfileFragment.newInstance();
    }

    @Override
    protected int getContentViewId() {
        return R.id.navigation_profile;
    }

    @Override
    protected int layoutRes() {
        return R.layout.fragment_nav_profile;
    }
}
