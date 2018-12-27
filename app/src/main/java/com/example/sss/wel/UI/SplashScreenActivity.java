package com.example.sss.wel.UI;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.sss.wel.Adapters.MySplashScreenAdapter;
import com.example.sss.wel.R;
import com.example.sss.wel.Utils.SharedPreferenceConfig;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import me.relex.circleindicator.CircleIndicator;

public class SplashScreenActivity extends AppCompatActivity {
    private static ViewPager mPager;
    private static int currentPage = 0;
    private static final Integer[] slideImages= {R.drawable.batman,R.drawable.spiderman,R.drawable.batman,R.drawable.spiderman};
    private ArrayList<Integer> slidImagesArray = new ArrayList<Integer>();
    private Button btnGetStarted;
    private SharedPreferenceConfig sharedPreferenceConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        sharedPreferenceConfig=new SharedPreferenceConfig(this);
        init();
    }
    private void init() {
        for(int i=0;i<slideImages.length;i++)
            slidImagesArray.add(slideImages[i]);

        mPager = (ViewPager) findViewById(R.id.pager);
        btnGetStarted = (Button) findViewById(R.id.btnGetStarted);
        mPager.setAdapter(new MySplashScreenAdapter(SplashScreenActivity.this,slidImagesArray));
        CircleIndicator indicator = (CircleIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(mPager);




        btnGetStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mainActivity=new Intent(SplashScreenActivity.this,MainActivity.class);
                startActivity(mainActivity);
                finish();

            }
        });

        // Auto start of viewpager
        final Handler handler = new Handler();
        final Runnable Update = new Runnable() {
            public void run() {
                if (currentPage == slideImages.length) {
                    currentPage = 0;
                }
                mPager.setCurrentItem(currentPage++, true);
            }
        };
        Timer swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.post(Update);
            }
        }, 2500, 2500);

    }
}
