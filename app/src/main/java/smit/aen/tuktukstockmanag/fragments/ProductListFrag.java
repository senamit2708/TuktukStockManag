package smit.aen.tuktukstockmanag.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import smit.aen.tuktukstockmanag.Model.ProductM;
import smit.aen.tuktukstockmanag.R;
import smit.aen.tuktukstockmanag.ViewModels.ProductViewM;
import smit.aen.tuktukstockmanag.adapter.ProductListAdap;
import smit.aen.tuktukstockmanag.adapter.ProductSearchListAdap;
import smit.aen.tuktukstockmanag.interfaces.ProductIface;

public class ProductListFrag extends Fragment implements View.OnClickListener, ProductIface {

    private static final String TAG = ProductListFrag.class.getSimpleName();
    private static final String PREF_UTYPE="userType";
    private static final String PREFERENCE = "preference";

    private Context context;

    private RecyclerView mRecyclerView;
    private ProductListAdap mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private SharedPreferences mSharedPref;
    private Button btnHide;
    private ProductViewM mViewModel;
    private long uType=0;
    private int adminType=0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = ViewModelProviders.of(getActivity()).get(ProductViewM.class);


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.af_product_list, container, false);
        context = container.getContext();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mSharedPref = context.getSharedPreferences(PREFERENCE, context.MODE_PRIVATE);
        if (mSharedPref.contains(PREF_UTYPE)){
           uType = mSharedPref.getLong(PREF_UTYPE, 11);

        }
        bindView(view);



        mViewModel.getProductList().observe(this, new Observer<List<ProductM>>() {
            @Override
            public void onChanged(List<ProductM> productList) {
                mAdapter.setProductList(productList);
            }
        });
    }

    private void bindView(View view) {
        btnHide = view.findViewById(R.id.btnHide);
        mRecyclerView = view.findViewById(R.id.recyclerview);
        mAdapter = new ProductListAdap(context, this, uType);
        mLayoutManager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void funProduct(ProductM product) {
        mViewModel.setSelectedProduct(product);

    }
}
