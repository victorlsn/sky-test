package mercari.victorlsn.mercari.ui.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by victorlsn on 23/07/18.
 */

public class PageFragmentAdapter extends FragmentPagerAdapter {
    public List<Fragment> getmFragments() {
        return mFragments;
    }

    public List<String> getmFragmentTitles() {
        return mFragmentTitles;
    }

    private final List<Fragment> mFragments = new ArrayList<>();
    private final List<String> mFragmentTitles = new ArrayList<>();

    public PageFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    public void addFragment(Fragment fragment, String title) {
        mFragments.add(fragment);
        mFragmentTitles.add(title);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }


    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return null;
    }

    public String getTitle(int position){
        return mFragmentTitles.get(position);
    }
}
