package smit.aen.tuktukstockmanag.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
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
}

