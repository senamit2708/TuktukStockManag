package smit.aen.tuktukstockmanag.fragments;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import smit.aen.tuktukstockmanag.NavMainActivity;
import smit.aen.tuktukstockmanag.R;
import smit.aen.tuktukstockmanag.ViewModels.ProductTypeViewM;
import smit.aen.tuktukstockmanag.adapter.ProductBrandAdap;
import smit.aen.tuktukstockmanag.interfaces.TopicIFace;

public class BrandListFrag extends Fragment implements View.OnClickListener, TopicIFace {

    private static final String TAG = BrandListFrag.class.getSimpleName();
    private static final String CATEG = "proCategory";
    private static final String FRAG_CHECK = "fragCheck";
    private static final String SELECT_TYPE = "selectType";
    private static final String FRAG_TAG = "fragTag";

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private Context context;
    private ProductTypeViewM mViewModel;

    private RecyclerView mRecyclerView;
    private ProductBrandAdap mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private FloatingActionButton fabAdd;
    private EditText txtType;
    private Button btnAdd;
    private CardView proAddCard;
    private String type;
    private int mFragCall=0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = ViewModelProviders.of(getActivity()).get(ProductTypeViewM.class);
        if (getArguments()!= null){
            mFragCall = getArguments().getInt(FRAG_CHECK,0);
            Log.i(TAG, "value of fragcall in oncreate is "+mFragCall);
        }else {
            Log.i(TAG, "inside else statmenet of saved instance");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.af_brand_list, container, false);
        context = container.getContext();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindView(view);

        fabAdd.setOnClickListener(this);
        btnAdd.setOnClickListener(this);

        mViewModel.getBrandList().observe(this, new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> typeList) {
                mAdapter.setTypeList(typeList);
            }
        });
    }

    private void bindView(View view) {
        mRecyclerView = view.findViewById(R.id.recyclerview);
        mAdapter = new ProductBrandAdap(context, this);
        mLayoutManager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        fabAdd = view.findViewById(R.id.fabAdd);
        txtType = view.findViewById(R.id.txtType);
        btnAdd = view.findViewById(R.id.btnAdd);
        proAddCard = view.findViewById(R.id.proAddCard);
    }

    @Override
    public void onClick(View v) {
        if (v==fabAdd){
            loadCard();
        }
        if (v==btnAdd){
            ((NavMainActivity)getActivity()).hideSoftKeyboard(v);
            getDetails();
        }
    }

    private void getDetails() {

        if (TextUtils.isEmpty(txtType.getText().toString())){
            txtType.setError("REQUIRED");
            return;
        }
        //@TODO: TRIM THE TEXT TO REMOVE BLANK SPACE FROM LAST AND STARTING, ALSO CONVERT FIRST LETTER INTO CAPITAL

        type = txtType.getText().toString();
        loadToFirebase();
    }

    private void loadCard() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        proAddCard.setVisibility(View.VISIBLE);
    }

    private void loadToFirebase() {




        final Map<String, Object> mainmap = new HashMap<>();

        mainmap.put("type", type);

        final DocumentReference docRefBrand =
                db.collection("ProCatColl")
                        .document("ProcatDoc")
                        .collection("BrandColl")
                        .document(type);

        db.collection("ProCatColl")
                .document("brandDoc")
                .update("brand", FieldValue.arrayUnion(type))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        docRefBrand.set(mainmap)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(context,"Category added successfully",Toast.LENGTH_SHORT).show();
                                        proAddCard.setVisibility(View.INVISIBLE);
                                        mRecyclerView.setVisibility(View.VISIBLE);
                                    }
                                });

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "Some error occured "+e, Toast.LENGTH_SHORT).show();
                    }
                });


    }
    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "inside onresume method");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i(TAG, "inside onpause method");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i(TAG, "inside onstop method");
    }

    @Override
    public void funTopicName(String topic) {
        Log.i(TAG, "mFragCall value is "+mFragCall);
        if (mFragCall==1){
            mViewModel.setEnterBrand(topic);
            Navigation.findNavController(getActivity(), R.id.fabAdd).popBackStack();
        }
        if (mFragCall==0){
            Bundle bundle = new Bundle();
            bundle.putString(SELECT_TYPE, topic);
            bundle.putInt(FRAG_TAG, 2);
            Navigation.findNavController(getActivity(), R.id.fabAdd).navigate(R.id.action_productTypeTabFrag_to_productListByType, bundle);

        }
    }
}
