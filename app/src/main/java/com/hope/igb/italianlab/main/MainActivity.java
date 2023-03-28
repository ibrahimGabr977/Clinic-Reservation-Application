package com.hope.igb.italianlab.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.hope.igb.italianlab.R;
import com.hope.igb.italianlab.main.achievements.AchievementsFragment;
import com.hope.igb.italianlab.main.profile.ProfileFragment;
import com.hope.igb.italianlab.main.requests.RequestsFragment;
import com.hope.igb.italianlab.main.schedules.SchedulesFragment;
import com.hope.igb.italianlab.comman.GeneralMethods;
import com.hope.igb.italianlab.comman.SharedData;

public class MainActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener {

    private  TextView fragment_title;
    private ImageView profile_image;
    private GeneralMethods generalMethods;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        generalMethods = new GeneralMethods(this);



//        fragment_title = findViewById(R.id.main_text_title);
        profile_image = findViewById(R.id.main_image_profile);

        BottomNavigationView bnv = findViewById(R.id.bnv);
        bnv.setOnItemSelectedListener(this);

        if (savedInstanceState == null){
            generalMethods.switchToFragment( R.id.mainFrameLayout, new SchedulesFragment());
        }






    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int screenHeight = this.getWindow().getDecorView().getHeight();
        int screenWidth = this.getWindow().getDecorView().getWidth();

        switch (item.getItemId()){
            case R.id.bnv_schedule:
                generalMethods.switchToFragment( R.id.mainFrameLayout, new SchedulesFragment());
//                fragment_title.setText(R.string.schedules_title);
//                fragment_title.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                profile_image.setImageResource(0);
                profile_image.setVisibility(View.GONE);
                break;


            case R.id.bnv_requests:
                generalMethods.switchToFragment( R.id.mainFrameLayout, new RequestsFragment());
//                fragment_title.setText(R.string.requests_title);
//                fragment_title.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                profile_image.setImageResource(0);
                profile_image.setVisibility(View.GONE);
                break;

            case R.id.bnv_achievements:
//                fragment_title.setText(R.string.achievements_title);
                generalMethods.switchToFragment( R.id.mainFrameLayout,
                        new AchievementsFragment());
//                fragment_title.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                profile_image.setImageResource(0);
                profile_image.setVisibility(View.GONE);
                break;



            case R.id.bnv_profile:
                SharedData sharedData = new SharedData(this);
                generalMethods.switchToFragment(R.id.mainFrameLayout, new ProfileFragment(sharedData, screenHeight, screenWidth));
//                fragment_title.setText(sharedData.getName());
//                fragment_title.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.b_main_profile_welcomed, 0);
                profile_image.setImageResource(R.drawable.d_doctor_imaged);
                profile_image.setVisibility(View.VISIBLE);
                break;
        }

        return true;
    }

}