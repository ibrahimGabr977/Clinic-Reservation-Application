package com.hope.igb.italianlab.main.schedules;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.google.android.material.tabs.TabLayout;
import com.hope.igb.italianlab.R;
import com.hope.igb.italianlab.comman.GeneralMethods;
import com.hope.igb.italianlab.comman.SharedData;
import com.hope.igb.italianlab.networking.firebase.MyFirebaseBuilderImpl;

public class SchedulesFragment extends Fragment implements TabLayout.OnTabSelectedListener {

    private GeneralMethods generalMethods;
    private MyFirebaseBuilderImpl firebaseBuilder;
    private SharedData sharedData;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View viewRoot = inflater.inflate(R.layout.schedules_fragment, container, false);

        TabLayout tabLayout = viewRoot.findViewById(R.id.schedules_tabLayout);
        tabLayout.addOnTabSelectedListener(this);
        generalMethods = new GeneralMethods(requireActivity());
        sharedData = new SharedData(getContext());

        firebaseBuilder = new MyFirebaseBuilderImpl(requireActivity());

        if (savedInstanceState == null){
            generalMethods.switchToFragment(R.id.schedulesFrameLayout,
                    new UpcomingFragment(firebaseBuilder, sharedData.getHeight(),
                            sharedData.getWidth()));
        }


        return viewRoot;
    }


    @Override
    public void onTabSelected(TabLayout.Tab tab) {

        switch (tab.getPosition()){
            case 0:
                generalMethods.switchToFragment(R.id.schedulesFrameLayout,
                        new UpcomingFragment(firebaseBuilder, sharedData.getHeight(),
                                sharedData.getWidth()));
                break;
            case 1:
                generalMethods.switchToFragment(R.id.schedulesFrameLayout, new CompletedFragment(firebaseBuilder));
                break;
            case 2:
                generalMethods.switchToFragment(R.id.schedulesFrameLayout, new CanceledFragment(firebaseBuilder));
                break;
        }

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}
