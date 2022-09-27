package duynn.gotogether.ui_layer.activity.home;


import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.util.Log;


import com.google.android.material.bottomnavigation.BottomNavigationView;


import duynn.gotogether.R;
import duynn.gotogether.databinding.ActivityHomeBinding;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ActivityHomeBinding activityHomeBinding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        BottomNavigationView bottomNavigationView = activityHomeBinding.homeBottomNavigationView;


        HomeViewPagerAdapter adapter = new HomeViewPagerAdapter(getSupportFragmentManager(),
                FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);

        activityHomeBinding.homeViewPager.setAdapter(adapter);
        activityHomeBinding.homeViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 1:
                        bottomNavigationView.setSelectedItemId(R.id.home_menu_publish);
                        break;
                    case 2:
                        bottomNavigationView.setSelectedItemId(R.id.home_menu_your_rides);
                        break;
                    case 3:
                        bottomNavigationView.setSelectedItemId(R.id.home_menu_chat);
                        break;
                    case 4:
                        bottomNavigationView.setSelectedItemId(R.id.home_menu_profile);
                        break;
                    default:
                        bottomNavigationView.setSelectedItemId(R.id.home_menu_search);
                        break;
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        bottomNavigationView.setOnItemSelectedListener(item -> {
//            Log.e("onNavigationItemReselected", "onNavigationItemReselected");
            switch (item.getItemId()) {
                case R.id.home_menu_search:
                    activityHomeBinding.homeViewPager.setCurrentItem(0);
                    break;
                case R.id.home_menu_publish:
                    activityHomeBinding.homeViewPager.setCurrentItem(1);
                    break;
                case R.id.home_menu_your_rides:
                    activityHomeBinding.homeViewPager.setCurrentItem(2);
                    break;
                case R.id.home_menu_chat:
                    activityHomeBinding.homeViewPager.setCurrentItem(3);
                    break;
                case R.id.home_menu_profile:
                    activityHomeBinding.homeViewPager.setCurrentItem(4);
                    break;
            }
            return true;
        });
    }
}