package mercari.victorlsn.mercari.ui.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by victorlsn on 23/07/18.
 */

public class PageFragmentAdapter extends FragmentPagerAdapter {

    private final List<Fragment> mFragments = new ArrayList<>();
    private final List<String> mFragmentTitles = new ArrayList<>();

    public PageFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    public List<Fragment> getmFragments() {
        return mFragments;
    }

    public List<String> getmFragmentTitles() {
        return mFragmentTitles;
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

    public String getTitle(int position) {
        return mFragmentTitles.get(position);
    }

    /**
     * This method override is needed to ensure the fragments are reutilized on screen orientation change
     */
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Object ret = super.instantiateItem(container, position);
        mFragments.set(position, (Fragment) ret);
        return ret;
    }
}
