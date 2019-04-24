package smit.aen.tuktukstockmanag.fragments;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import smit.aen.tuktukstockmanag.NavMainActivity;
import smit.aen.tuktukstockmanag.R;
import smit.aen.tuktukstockmanag.ViewModels.ProductTypeViewM;
import smit.aen.tuktukstockmanag.adapter.ProductTypeAdap;
import smit.aen.tuktukstockmanag.interfaces.TopicIFace;

public class CategotyListFrag extends Fragment implements View.OnClickListener, TopicIFace {
    private static final String TAG = CategotyListFrag.class.getSimpleName();
    private static final String CATEG = "proCategory";
    private static final String FRAG_CHECK = "fragCheck";
    private static final String SELECT_TYPE = "selectType";
    private static final String FRAG_TAG = "fragTag";

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private Context context;
    private ProductTypeViewM mViewModel;

    private RecyclerView mRecyclerView;
    private ProductTypeAdap mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private FloatingActionButton fabAdd;
    private EditText txtType;
    private Button btnAdd;
    private Button btnCross;
    private CardView proAddCard;
    private ConstraintLayout mConsLayout;

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
        View view = inflater.inflate(R.layout.af_category_list, container, false);
        context = container.getContext();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindView(view);
        fabAdd.setOnClickListener(this);
        btnAdd.setOnClickListener(this);
        btnCross.setOnClickListener(this);


        mViewModel.getTypeList().observe(this, new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> typeList) {
                mAdapter.setTypeList(typeList);
            }
        });
    }

    private void bindView(View view) {
        mRecyclerView = view.findViewById(R.id.recyclerview);
        mAdapter = new ProductTypeAdap(context, this);
        mLayoutManager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        fabAdd = view.findViewById(R.id.fabAdd);
        txtType = view.findViewById(R.id.txtType);
        btnAdd = view.findViewById(R.id.btnAdd);
        btnCross = view.findViewById(R.id.btnCross);
        proAddCard = view.findViewById(R.id.proAddCard);

        mConsLayout = view.findViewById(R.id.constraintLayout);
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
        if (v==btnCross){
            ((NavMainActivity)getActivity()).hideSoftKeyboard(v);
            reLoadDetails();
        }
    }

    private void reLoadDetails() {

        txtType.setText("");
        btnAdd.setEnabled(true);
        ViewCompat.setBackgroundTintList(btnAdd, ContextCompat.getColorStateList(context, R.color.colorYellowTab));
        proAddCard.setVisibility(View.INVISIBLE);
        mRecyclerView.setVisibility(View.VISIBLE);
        btnCross.setVisibility(View.INVISIBLE);
        fabAdd.show();
    }

    private void getDetails() {

        if (TextUtils.isEmpty(txtType.getText().toString())){
            txtType.setError("REQUIRED");
            return;
        }
        ViewCompat.setBackgroundTintList(btnAdd, ContextCompat.getColorStateList(context, R.color.colorGray));
        btnAdd.setEnabled(false);
        String firstString = txtType.getText().toString().trim();
        type = Character.toUpperCase(firstString.charAt(0)) + firstString.substring(1);
        Log.i(TAG, "the value is "+type);

        loadToFirebase();
    }

    private void loadCard() {
//        fabAdd.setEnabled(false);
        mRecyclerView.setVisibility(View.INVISIBLE);
        proAddCard.setVisibility(View.VISIBLE);
        btnCross.setVisibility(View.VISIBLE);
        fabAdd.hide();

        ViewCompat.setBackgroundTintList(btnAdd, ContextCompat.getColorStateList(context, R.color.colorYellowTab));

    }

    private void loadToFirebase() {

        final Map<String, Object> mainmap = new HashMap<>();

        mainmap.put("type", type);
        final DocumentReference docRefCat =
                db.collection("ProCatColl")
                        .document("ProcatDoc")
                        .collection("CatColl")
                        .document(type);

//        db.collection("ProCatColl")
//                .document("ProcatDoc")
//                .collection("CatColl")
//                .document(type)
//                .set(mainmap, SetOptions.merge())
        db.collection("ProCatColl")
                .document("categDoc")
                .update("cat", FieldValue.arrayUnion(type))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        docRefCat.set(mainmap, SetOptions.merge())
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        snackBarShow();
                                      reLoadDetails();
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

    private void snackBarShow() {
        Snackbar snackbar = Snackbar.make(mConsLayout, "Category Entered Successfully ", Snackbar.LENGTH_SHORT);
//        snackbar.setActionTextColor(context.getResources().getColor(R.color.colorYellowTab));
        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(context.getResources().getColor(R.color.colorBlackForToolbar));
//        TextView textView = snackbarView.findViewById(android.su)
        snackbar.show();
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
    public void onStart() {
        super.onStart();
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
            mViewModel.setEnterCategory(topic);
            Navigation.findNavController(getActivity(), R.id.fabAdd).popBackStack();
        }
        if (mFragCall==0){
            Bundle bundle = new Bundle();
            bundle.putString(SELECT_TYPE, topic);
            bundle.putInt(FRAG_TAG, 1);
            Navigation.findNavController(getActivity(), R.id.fabAdd).navigate(R.id.action_productTypeTabFrag_to_productListByType, bundle);

        }
    }
}
