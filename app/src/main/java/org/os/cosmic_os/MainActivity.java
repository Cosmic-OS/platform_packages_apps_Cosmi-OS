package org.os.cosmic_os;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationAdapter;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationViewPager;

public class MainActivity extends AppCompatActivity {

    AHBottomNavigation ahBottomNavigation;
    AHBottomNavigationAdapter ahBottomNavigationAdapter;
    AHBottomNavigationViewPager ahBottomNavigationViewPager;
    FloatingActionButton g_fab;
    int[] tabColors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        g_fab = findViewById(R.id.g_fab);
        //setup the AHViewPager
        ahBottomNavigationViewPager = findViewById(R.id.viewPager);
        ahBottomNavigationViewPager.setAdapter(new MyPageAdapter(getSupportFragmentManager()));
        ahBottomNavigationViewPager.setPageTransformer(false, new FadePageTransform() );

        //setup the bottom navigation tab
        ahBottomNavigation = findViewById(R.id.bottom_navigation);
        ahBottomNavigationAdapter =
                new AHBottomNavigationAdapter(this,R.menu.bottom_bar_menu);
        tabColors = getApplicationContext().getResources().getIntArray(R.array.tab_colors);
        ahBottomNavigationAdapter.setupWithBottomNavigation(ahBottomNavigation,tabColors);
        ahBottomNavigation.setColored(true);
        ahBottomNavigation.setTranslucentNavigationEnabled(true);
        ahBottomNavigation.setBehaviorTranslationEnabled(true);
        ahBottomNavigation.manageFloatingActionButtonBehavior(g_fab);
        g_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gplusIntent = new Intent(Intent.ACTION_VIEW);
                gplusIntent.setData(Uri.parse("https://plus.google.com/communities/116339021564888810193"));
                startActivity(gplusIntent);
            }
        });
        ahBottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public boolean onTabSelected(int position, boolean wasSelected) {
                if(!wasSelected)
                    ahBottomNavigationViewPager.setCurrentItem(position);
                return true;
            }
        });

    }

    public class MyPageAdapter extends FragmentStatePagerAdapter {
        MyPageAdapter(FragmentManager fm) {
            super(fm);
        }

        //Handle to create fragment instances
        @Override
        public Fragment getItem(int position) {
            switch (position)
            {
                case 0 : return HomeFragment.newInstance();
                case 1 : return ChangelogFragment.newInstance();
                case 2 : return TeamFragment.newInstance();
                case 3 : return DonateFragment.newInstance();
            }
            return HomeFragment.newInstance();
        }

        @Override
        public int getCount() {
            return 4;
        }

    }

    //FadeOut and FadeIn animation for ViewPager
    public class FadePageTransform implements ViewPager.PageTransformer{

        @Override
        public void transformPage(@NonNull View page, float position) {
            if(position <= -1.0F || position >= 1.0F) {
                page.setTranslationX(page.getWidth() * position);
                page.setAlpha(0.0F);
            } else if( position == 0.0F ) {
                page.setTranslationX(page.getWidth() * position);
                page.setAlpha(1.0F);
            } else {
                // position is between -1.0F & 0.0F OR 0.0F & 1.0F
                page.setTranslationX(page.getWidth() * -position);
                page.setAlpha(1.0F - Math.abs(position));
            }
        }
    }

}
