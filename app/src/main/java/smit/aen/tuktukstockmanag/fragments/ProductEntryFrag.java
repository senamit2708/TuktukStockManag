package smit.aen.tuktukstockmanag.fragments;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import smit.aen.tuktukstockmanag.NavMainActivity;
import smit.aen.tuktukstockmanag.R;

public class ProductEntryFrag extends Fragment implements View.OnClickListener{

    private static final String TAG = ProductEntryFrag.class.getSimpleName();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private Context context;

    private EditText txtPName;
    private EditText txtPNumber;
    private EditText txtBRate;
    private EditText txtSPrice;
    private EditText txtDes;
    private EditText txtQuantity;
    private Button btnSubmit;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        btnSubmit.setOnClickListener(this);
    }

    private void bindView(View view) {
        txtPName = view.findViewById(R.id.txtPName);
        txtPNumber =view.findViewById(R.id.txtPNumber);
        txtBRate = view.findViewById(R.id.txtBRate);
        txtSPrice = view.findViewById(R.id.txtSPrice);
        txtDes = view.findViewById(R.id.txtDes);
        txtQuantity = view.findViewById(R.id.txtQuantity);
        btnSubmit = view.findViewById(R.id.btnSubmit);
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.btnSubmit){
            ((NavMainActivity)getActivity()).hideSoftKeyboard(v);
            if (!validate()){
                return;
            }
            loadToFirebase();
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
        Map<String, Object> mainMap = new HashMap<>();
        mainMap.put("name", txtPName.getText().toString());
        mainMap.put("num", num);
        mainMap.put("bPrice", bPrice);
        mainMap.put("sPrice", sPrice);
        mainMap.put("des", description);
        mainMap.put("quan",0);

        db.collection("ProdColl")
                .document(num)
                .set(mainMap, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(context, "Product Entered Successfully ", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "Some error occured ", Toast.LENGTH_SHORT).show();
                    }
                });



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
