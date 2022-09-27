package duynn.gotogether.ui_layer.activity.home;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import duynn.gotogether.ui_layer.fragment.inbox.InboxFragment;
import duynn.gotogether.ui_layer.fragment.profile.ProfileFragment;
import duynn.gotogether.ui_layer.fragment.publish.PublishFragment;
import duynn.gotogether.ui_layer.fragment.search.SearchFragment;
import duynn.gotogether.ui_layer.fragment.your_rides.YourRidesFragment;

public class HomeViewPagerAdapter extends FragmentStatePagerAdapter {
    public HomeViewPagerAdapter(FragmentManager supportFragmentManager, int behaviorResumeOnlyCurrentFragment) {
        super(supportFragmentManager,behaviorResumeOnlyCurrentFragment);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 1:
                return new PublishFragment();
            case 2:
                return new YourRidesFragment();
            case 3:
                return new InboxFragment();
            case 4:
                return new ProfileFragment();
            default:
                return new SearchFragment();
        }
    }

    @Override
    public int getCount() {
        return 5;
    }
}
