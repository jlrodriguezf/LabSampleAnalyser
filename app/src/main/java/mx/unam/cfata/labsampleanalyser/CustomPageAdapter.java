package mx.unam.cfata.labsampleanalyser;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by José Luis Rodríguez Fragoso.
 */

public class CustomPageAdapter extends FragmentPagerAdapter {
    private static final String TAG = CustomPageAdapter.class.getSimpleName();
    private String[] tabTitles = new String[]{"J. L. Rodríguez", "B. E. Millán", "UNAM"};

    public CustomPageAdapter(FragmentManager fm){
        super(fm);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabTitles[position];
    }

    @Override
    public Fragment getItem(int position) {
        switch(position) {
            case 0:
                return new Author1();
            case 1:
                return new Author2();
            case 2:
                return new Author3();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabTitles.length;
    }
}
