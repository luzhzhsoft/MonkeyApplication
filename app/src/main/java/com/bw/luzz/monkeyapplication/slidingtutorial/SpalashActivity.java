package com.bw.luzz.monkeyapplication.slidingtutorial;

import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.bw.luzz.monkeyapplication.R;
import com.bw.luzz.monkeyapplication.slidingtutorial.CustomPresentationPagerFragment;

/**
 * 闪屏
 */
public class SpalashActivity extends AppCompatActivity  implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spalash);

        findViewById(R.id.bRetry).setOnClickListener(this);

        if (savedInstanceState == null) {
            replaceTutorialFragment();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bRetry:
                replaceTutorialFragment();
                break;
        }
    }

    public void replaceTutorialFragment() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.container, new CustomPresentationPagerFragment());
        fragmentTransaction.commit();
    }
}
