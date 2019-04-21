package smit.aen.tuktukstockmanag.fragments;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import smit.aen.tuktukstockmanag.NavMainActivity;
import smit.aen.tuktukstockmanag.R;
import smit.aen.tuktukstockmanag.adapter.SimpleFragPagerAdap;

public class ProductTypeTabFrag extends Fragment {

    private static final String TAG = ProductTypeTabFrag.class.getSimpleName();

    private Context context;

    private ViewPager mViewPager;
    private SimpleFragPagerAdap mAdapter;
    private TabLayout tabLayout;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.af_product_type_tab, container, false);
        context = container.getContext();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        bindView(view);


        mViewPager = view.findViewById(R.id.viewPager);
        tabLayout = view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        mAdapter = new SimpleFragPagerAdap(getChildFragmentManager());
        mAdapter.addFragment(new CategotyListFrag(), "CATEGORY");
        mAdapter.addFragment(new BrandListFrag(), "BRAND");
        mAdapter.addFragment(new SellerListFrag(), "SELLER");
        mViewPager.setAdapter(mAdapter);
    }

    private void bindView(View view) {
        mViewPager = view.findViewById(R.id.viewPager);
        tabLayout = view.findViewById(R.id.tabs);
//        mAdapter = new SimpleFragPagerAdap(context, getActivity().getSupportFragmentManager());
        mViewPager.setAdapter(mAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i(TAG, "inside on start ");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            ((NavMainActivity)getActivity()).getSupportActionBar().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(context, R.color.colorBlackForToolbar)));
            ((NavMainActivity)getActivity()).updateStatusBarColor("#0B0B0B");
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i(TAG, "inside on pause method");
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
//            ((NavMainActivity)getActivity()).getSupportActionBar().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(context, R.color.colorPrimary)));
//            ((NavMainActivity)getActivity()).updateStatusBarColor("#4E0D3A");
//        }
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i(TAG, "inside onstop");
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
//            ((NavMainActivity)getActivity()).getSupportActionBar().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(context, R.color.colorPrimary)));
//            ((NavMainActivity)getActivity()).updateStatusBarColor("#4E0D3A");
//        }
    }
}

