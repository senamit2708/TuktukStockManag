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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import smit.aen.tuktukstockmanag.NavMainActivity;
import smit.aen.tuktukstockmanag.R;
import smit.aen.tuktukstockmanag.ViewModels.ProductTypeViewM;

public class ProductEntryFrag extends Fragment implements View.OnClickListener{

    private static final String TAG = ProductEntryFrag.class.getSimpleName();
    private static final String FRAG_CHECK = "fragCheck";
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private Context context;
    private ProductTypeViewM mViewModel;

    private EditText txtPName;
    private EditText txtPNumber;
    private EditText txtBRate;
    private EditText txtSPrice;
    private EditText txtDes;
    private EditText txtCategory;
    private EditText txtBrand;
    private Button btnSubmit;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = ViewModelProviders.of(getActivity()).get(ProductTypeViewM.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.af_product_entry, container, false);
        context = container.getContext();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindView(view);

        txtBrand.setClickable(true);
        txtCategory.setClickable(true);
        txtBrand.setFocusable(false);
        txtCategory.setFocusable(false);
        btnSubmit.setOnClickListener(this);
        txtBrand.setOnClickListener(this);
        txtCategory.setOnClickListener(this);

        mViewModel.getEnterCategory().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String category) {
                txtCategory.setText(category);
            }
        });
        mViewModel.getEnterBrand().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String brand) {
                txtBrand.setText(brand);
            }
        });

    }

    private void bindView(View view) {
        txtPName = view.findViewById(R.id.txtPName);
        txtPNumber =view.findViewById(R.id.txtPNumber);
        txtBRate = view.findViewById(R.id.txtBRate);
        txtSPrice = view.findViewById(R.id.txtSPrice);
        txtDes = view.findViewById(R.id.txtDes);
        txtCategory = view.findViewById(R.id.txtCategory);
        txtBrand = view.findViewById(R.id.txtBrand);
        btnSubmit = view.findViewById(R.id.btnSubmit);

        btnSubmit.setBackground(ContextCompat.getDrawable(context,R.drawable.submit_button_selector));

    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.btnSubmit){
            ((NavMainActivity)getActivity()).hideSoftKeyboard(v);
            if (!validate()){
                return;
            }
            btnSubmit.setEnabled(false);
            loadToFirebase();
        }
        if (v==txtBrand){
            Bundle bundle = new Bundle();
            bundle.putInt(FRAG_CHECK, 1);
            Navigation.findNavController(v).navigate(R.id.action_productEntryFrag_to_brandListFrag, bundle);
            return;
        }
        if (v==txtCategory){
            Bundle bundle1 = new Bundle();
            bundle1.putInt(FRAG_CHECK, 1);
            Navigation.findNavController(v).navigate(R.id.action_productEntryFrag_to_categotyListFrag, bundle1);
        return;
        }
    }

    private void loadToFirebase() {
//        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault()).format(new Date());
        String name = txtPName.getText().toString();
        String num = txtPNumber.getText().toString();
        double bPrice = Double.parseDouble(txtBRate.getText().toString());
        double sPrice = Double.parseDouble(txtSPrice.getText().toString());
        String description;
        if (TextUtils.isEmpty(txtDes.getText().toString())){
            description = null;
        }else {
            description = txtDes.getText().toString();
        }
        final String brnad = txtBrand.getText().toString();
        final String cat = txtCategory.getText().toString();
        Map<String, Object> mainMap = new HashMap<>();
        mainMap.put("name", txtPName.getText().toString());
        mainMap.put("num", num);
        mainMap.put("bPrice", bPrice);
        mainMap.put("sPrice", sPrice);
        mainMap.put("des", description);
        mainMap.put("quan",0);
        mainMap.put("aval", true);
        mainMap.put("brand", brnad);
        mainMap.put("cat", cat);


        final DocumentReference docRefBrand =
                db.collection("ProCatColl")
                        .document("ProcatDoc")
                        .collection("BrandColl")
                        .document(brnad);
        final DocumentReference docRefCat =
                db.collection("ProCatColl")
                        .document("ProcatDoc")
                        .collection("CatColl")
                        .document(cat);

        db.collection("ProdColl")
                .document(num)
                .set(mainMap, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        docRefBrand.update("cat", FieldValue.arrayUnion(cat))
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.i(TAG, "brand updated successfully");
                                    }
                                });
                        docRefCat.update("brand", FieldValue.arrayUnion(brnad))
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Log.i(TAG, "category updated succeessfully");
                                    }
                                });
                        Toast.makeText(context, "Product Entered Successfully ", Toast.LENGTH_SHORT).show();
                        clearDetails();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "Some error occured ", Toast.LENGTH_SHORT).show();
                    }
                });



    }

    private void clearDetails() {
        txtPName.setText("");
        txtPNumber.setText("");
        txtSPrice.setText("");
        txtBRate.setText("");
        txtDes.setText("");
        mViewModel.setEnterCategory(null);
        mViewModel.setEnterBrand(null);
        btnSubmit.setEnabled(true);
    }

    private boolean validate() {
        Boolean status = true;
        if (TextUtils.isEmpty(txtPName.getText().toString())){
            txtPName.setError("REQUIRED");
            status = false;
        }
        if (TextUtils.isEmpty(txtPNumber.getText().toString())){
            txtPNumber.setError("REQUIRED");
            status = false;
        }
        if (TextUtils.isEmpty(txtBRate.getText().toString())){
            txtBRate.setError("REQUIRED");
            status = false;
        }
        if (TextUtils.isEmpty(txtSPrice.getText().toString())){
            txtSPrice.setError("REQUIRED");
            status = false;
        }
        return status;
    }
}
