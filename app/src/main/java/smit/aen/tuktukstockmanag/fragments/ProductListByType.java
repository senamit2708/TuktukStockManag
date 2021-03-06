package smit.aen.tuktukstockmanag.fragments;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import smit.aen.tuktukstockmanag.Model.ProductM;
import smit.aen.tuktukstockmanag.NavMainActivity;
import smit.aen.tuktukstockmanag.R;
import smit.aen.tuktukstockmanag.ViewModels.LoginViewM;
import smit.aen.tuktukstockmanag.ViewModels.ProductViewM;
import smit.aen.tuktukstockmanag.ViewModels.ProductWithTypeViewM;
import smit.aen.tuktukstockmanag.adapter.ProductListAdap;
import smit.aen.tuktukstockmanag.adapter.ProductListByTypeAdap;
import smit.aen.tuktukstockmanag.adapter.ProductListByTypeCatAdap;
import smit.aen.tuktukstockmanag.interfaces.ProEditIface;
import smit.aen.tuktukstockmanag.interfaces.ProductIface;
import smit.aen.tuktukstockmanag.interfaces.TopicIFace;

public class ProductListByType extends Fragment implements TopicIFace, ProductIface, ProEditIface, View.OnClickListener {

    private static final String TAG = ProductListByType.class.getSimpleName();
    private static final String SELECT_TYPE = "selectType";
    private static final String FRAG_TAG = "fragTag";
    private static final String ENTRY_CHECK_FRAG = "entryCheck";

    private FirebaseFirestore db = FirebaseFirestore.getInstance();


    private Context context;

    private ProductWithTypeViewM mViewModel;
    private ProductViewM mProductViewModel;
    private LoginViewM mLoginViewModel;

    private RecyclerView mRecyclerType;
    private ProductListByTypeCatAdap mCatAdapter;
    private RecyclerView.LayoutManager mCatLayoutManager;

    private RecyclerView mRecyclerview;
    private ProductListAdap mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private CardView cardViewAll;
    private ConstraintLayout constraint;


    private String type=null;
    private int fragType=0;
    private String selectedItem= "none";
    private long admin = 10;
    private String mAllProd ="allProduct";
    private List<String> typeListForProduct;

    private void bindView(View view) {
        mRecyclerType = view.findViewById(R.id.recyclerType);
        mCatAdapter = new ProductListByTypeCatAdap(context, this);
        mCatLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        mRecyclerType.setLayoutManager(mCatLayoutManager);
        mRecyclerType.setAdapter(mCatAdapter);

        mRecyclerview = view.findViewById(R.id.recyclerview);
        mAdapter = new ProductListAdap(context, this,this,admin );
        mLayoutManager = new LinearLayoutManager(context);
        mRecyclerview.setLayoutManager(mLayoutManager);
        mRecyclerview.setAdapter(mAdapter);

        constraint = view.findViewById(R.id.constraint);
        cardViewAll = view.findViewById(R.id.cardviewAll);
        cardViewAll.setBackground(context.getResources().getDrawable(R.drawable.all_select_button_draw));
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = ViewModelProviders.of(getActivity()).get(ProductWithTypeViewM.class);
        mProductViewModel = ViewModelProviders.of(getActivity()).get(ProductViewM.class);
        mLoginViewModel = ViewModelProviders.of(getActivity()).get(LoginViewM.class);
        type = getArguments().getString(SELECT_TYPE, "none");
        fragType = getArguments().getInt(FRAG_TAG, 0);
        admin = mLoginViewModel.getuType();
        Log.i(TAG, "the value of frag tag is "+fragType);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.af_product_list_by_type, container, false);
        context = container.getContext();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindView(view);


        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(type);

        cardViewAll.setOnClickListener(this);

        mViewModel.getTypeVariety(type, fragType).observe(this, new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> typeList) {
                typeListForProduct = new ArrayList<>();
                if (typeList!= null){
                    typeListForProduct.addAll(typeList);
                }
                mCatAdapter.setType(typeList);
            }
        });

//        loadProductList(type, selectedItem, fragType);

    }

    private void loadProductList(String type, String selectedItem, int fragType) {
        mAdapter.setProductList(null);
       mViewModel.getProductList(type, selectedItem, fragType).observe(this, new Observer<List<ProductM>>() {
           @Override
           public void onChanged(List<ProductM> productList) {
               if (productList!= null){
                   mAdapter.setProductList(productList);
               }
               else{
                   mAdapter.setProductList(null);
                   Log.i(TAG, "inside null of productlist");
                   Toast.makeText(context, "no product available", Toast.LENGTH_SHORT).show();
               }
           }
       });
    }


    @Override
    public void funTopicName(String topic) {
        cardViewAll.setBackground(context.getResources().getDrawable(R.drawable.all_select_button_draw));
        loadProductList(type, topic, fragType);
    }

    @Override
    public void funProduct(ProductM product) {
        if (admin==11){
            loadSnack(product);
        }else {
            snackAdmin();
        }
    }

    private void loadSnack(final ProductM product) {
        Snackbar snackbar = Snackbar.make(constraint, "Sure want to delete",Snackbar.LENGTH_SHORT);
        snackbar.setAction("YES", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteProduct(product);
            }
        });
        snackbar.show();
    }

    private void deleteProduct(ProductM product) {
        db.collection("ProdColl")
                .document(product.getNum())
                .update("aval", false)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Snackbar.make(constraint,"Product deleted successfully", Snackbar.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Snackbar.make(constraint,"Some error occured", Snackbar.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public void onStart() {
        super.onStart();
        //this is for toolbar color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            ((NavMainActivity)getActivity()).getSupportActionBar().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(context, R.color.colorPrimary)));
            ((NavMainActivity)getActivity()).updateStatusBarColor("#4E0D3A");
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i(TAG, "inside onstop method");
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
//            ((NavMainActivity)getActivity()).getSupportActionBar().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(context, R.color.colorPrimary)));
//            ((NavMainActivity)getActivity()).updateStatusBarColor("#4E0D3A");
//        }
    }

    @Override
    public void onClick(View v) {
        if (v==cardViewAll){

            cardViewAll.setBackground(context.getResources().getDrawable(R.drawable.all_select_button_clicked_draw));
            mCatAdapter.setType(typeListForProduct);
            loadProductList(type, mAllProd, fragType);
        }
    }

    @Override
    public void funEditPro(ProductM product) {
        if (admin==11){
            mViewModel.setProductForEdit(product);
            Bundle bundle = new Bundle();
            bundle.putInt(ENTRY_CHECK_FRAG, 2);
            Navigation.findNavController(getActivity(), R.id.cardviewAll).navigate(R.id.action_productListByType_to_productEntryFrag, bundle);
        }else {
            snackAdmin();
        }

    }

    private void snackAdmin() {
        Snackbar.make(constraint, "Unauthorized to make changes", Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void funSearchPro(ProductM productM) {
        Log.i(TAG, "inside funserach pro  "+mProductViewModel.getProValForSearch() );
        if (mProductViewModel.getProValForSearch()==2){
            mProductViewModel.setSelectedProduct(productM);
            Navigation.findNavController(getActivity(), R.id.cardviewAll).popBackStack(R.id.transactionListFrag, false);
        }
        if (mProductViewModel.getProValForSearch()==1){
            mProductViewModel.setSelectedProduct(productM);
            Navigation.findNavController(getActivity(), R.id.cardviewAll).popBackStack(R.id.transactionFrag, false);
        }
    }
}
