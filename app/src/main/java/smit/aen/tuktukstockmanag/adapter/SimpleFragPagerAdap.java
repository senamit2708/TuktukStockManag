package smit.aen.tuktukstockmanag.adapter;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class SimpleFragPagerAdap extends FragmentPagerAdapter {

    private static final String TAG = SimpleFragPagerAdap.class.getSimpleName();
    private Context mContext;
    private int tabCount = 3;

    List<Fragment> fragmentList = new ArrayList<Fragment>();
    List<String> fragmentTitle = new ArrayList<String>();

    public SimpleFragPagerAdap(FragmentManager fm) {
        super(fm);
    }


//    public SimpleFragPagerAdap(Context context, FragmentManager fm) {
//        super(fm);
//        mContext = context;
//    }




    @Override
    public Fragment getItem(int position) {
//        if (position==0){
//            Log.i(TAG, "inside postion 0 of get item");
//            return new TokenListFrag();
//        }else if (position==1){
//            Log.i(TAG, "inside postion 1 of get item");
//            return new TokenListCompFrag();
//        }else if (position==2){
//            Log.i(TAG, "inside postion 2 of get item");
//            return new TokenListCloseFrag();
//        }else {
//            Log.i(TAG, "inside postion 3 of get item");
//            return null;
//        }
        Log.i(TAG, "the item position is "+fragmentList.get(position));
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
//        return tabCount;
        Log.i(TAG, "the count of item is "+fragmentList.size());
        return fragmentList.size();
    }

    public void addFragment(Fragment fragment, String name) {
        fragmentList.add(fragment);
        fragmentTitle.add(name);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
//        switch (position){
//            case 0:
//                Log.i(TAG, "inside postion 0 of getPageTitle");
//                return mContext.getString(R.string.tab_a_label);
//            case 1:
//                Log.i(TAG, "inside postion 0 of getPageTitle");
//                return mContext.getString(R.string.tab_b_label);
//            case 2:
//                Log.i(TAG, "inside postion 0 of getPageTitle");
//                return mContext.getString(R.string.tab_c_label);
//             default:
//                 Log.i(TAG, "inside postion 0 of getPageTitle");
//                 return null;
//        }
        Log.i(TAG, "returned item is "+fragmentTitle.get(position));
        return fragmentTitle.get(position);
    }
}
