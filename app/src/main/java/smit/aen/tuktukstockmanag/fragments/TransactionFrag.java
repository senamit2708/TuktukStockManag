package smit.aen.tuktukstockmanag.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import smit.aen.tuktukstockmanag.Model.ProductM;
import smit.aen.tuktukstockmanag.NavMainActivity;
import smit.aen.tuktukstockmanag.R;
import smit.aen.tuktukstockmanag.ViewModels.ProductViewM;

public class TransactionFrag extends Fragment implements View.OnClickListener{

    private static final String TAG = TransactionFrag.class.getSimpleName();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private Context context;
    private ProductViewM mViewModel;

    private Button btnHome;
    private Button btnSubmit;
    private Button btnIn;
    private Button btnOut;
    private ImageButton btnDate;

    private static EditText txtDate;
    private EditText txtPName;
    private TextView txtStock;
    private EditText txtQuantity;
    private EditText txtRemarks;
    private ConstraintLayout mConsLayout;

    private int mTransType = 0;
    private String prodNumber = null;
    private long availableQuan;
    private long totalQuan;
    private static long queryDate;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = ViewModelProviders.of(getActivity()).get(ProductViewM.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.af_transaction, container, false);
        context = container.getContext();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindView(view);

        txtPName.setFocusable(false);
        txtPName.setClickable(true);
        txtDate.setFocusable(false);
        txtPName.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
        btnOut.setOnClickListener(this);
        btnIn.setOnClickListener(this);
        btnDate.setOnClickListener(this);
        btnHome.setOnClickListener(this);

        mViewModel.getSelectedProduct().observe(this, new Observer<ProductM>() {
            @Override
            public void onChanged(ProductM product) {
                if (product!= null){
                    txtPName.setText(product.getName());
                    txtStock.setText(String.valueOf(product.getQuan()));
                    availableQuan = product.getQuan();
                    prodNumber = product.getNum();
                }


            }
        });
    }

    private void bindView(View view) {
        btnHome = view.findViewById(R.id.btnHome);
        btnSubmit =view.findViewById(R.id.btnSubmit);
        btnIn = view.findViewById(R.id.btnIn);
        btnOut = view.findViewById(R.id.btnOut);
        btnDate = view.findViewById(R.id.btnDate);

        txtDate = view.findViewById(R.id.txtDate);
        txtPName = view.findViewById(R.id.txtPName);
        txtStock = view.findViewById(R.id.txtStock);
        txtQuantity = view.findViewById(R.id.txtQuantity);
        txtRemarks = view.findViewById(R.id.txtRemarks);
        mConsLayout = view.findViewById(R.id.constraintLayout);
        ViewCompat.setBackgroundTintList(btnSubmit, ContextCompat.getColorStateList(context, R.color.colorPrimaryDark));

    }

    @Override
    public void onClick(View v) {
        if (v == btnSubmit){
            ((NavMainActivity)getActivity()).hideSoftKeyboard(v);
            if (!validate()){
                return;
            }
            ViewCompat.setBackgroundTintList(btnSubmit, ContextCompat.getColorStateList(context, R.color.colorGray));
            btnSubmit.setEnabled(false);
            loadToFirebase();
        }
        if (v==btnIn){
            mTransType = 1;
            ViewCompat.setBackgroundTintList(btnIn, ContextCompat.getColorStateList(context, R.color.colorBlue));
            ViewCompat.setBackgroundTintList(btnOut, ContextCompat.getColorStateList(context, R.color.colorWhite));


            btnIn.setTextColor(Color.WHITE);
            btnOut.setTextColor(Color.BLACK);
        }
        if (v==btnOut){
            mTransType = 2;
            ViewCompat.setBackgroundTintList(btnIn, ContextCompat.getColorStateList(context, R.color.colorWhite));
            ViewCompat.setBackgroundTintList(btnOut, ContextCompat.getColorStateList(context, R.color.colorGreen));
            btnOut.setTextColor(Color.WHITE);
            btnIn.setTextColor(Color.BLACK);

        }

        if (v==btnDate){
            showDatePickerDialog();
        }
        if (v==btnHome){
            clearData();
            Navigation.findNavController(getActivity(), R.id.btnHome).popBackStack();
        }
        if (v==txtPName){
            mViewModel.setProValForSearch(1);
            Navigation.findNavController(getActivity(), R.id.txtPName).navigate(R.id.action_transactionFrag_to_productTypeTabFrag);
        }
    }

    private void showDatePickerDialog() {
        DialogFragment newFragment = new DatePickerFrag();
        newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");


    }

    private void loadToFirebase() {
        final String name = txtPName.getText().toString();
        final long quan = Long.parseLong(txtQuantity.getText().toString());
        //to check out quantity is less than available quantity
        if (mTransType==2){
            if (quan>availableQuan){
                Toast.makeText(context, "Quantity can't be greater than current stock", Toast.LENGTH_SHORT).show();
                btnSubmit.setEnabled(true);
                ViewCompat.setBackgroundTintList(btnSubmit, ContextCompat.getColorStateList(context, R.color.colorPrimaryDark));

                return;
            }
            else {
                totalQuan= availableQuan-quan;
            }
        }
        else {
            totalQuan = quan + availableQuan;
        }

        String date = txtDate.getText().toString();
        String remark= null;
        if (!(TextUtils.isEmpty(txtRemarks.getText().toString()))){
            remark = txtRemarks.getText().toString();
        }

        Map<String, Object> mainMap = new HashMap<>();

        mainMap.put("name", name);
        mainMap.put("quan", quan);
        mainMap.put("remark", remark);
        mainMap.put("trans", mTransType);
        mainMap.put("date", date);
        mainMap.put("num", prodNumber);
        mainMap.put("queryDate", queryDate);

        db.collection("stockColl")
                .add(mainMap)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        db.collection("ProdColl")
                                .document(prodNumber)
                                .update("quan", totalQuan)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        snackBarShow();
                                        clearData();
                                    }
                                });

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "Some error occured ", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void snackBarShow() {
        Snackbar snackbar = Snackbar.make(mConsLayout, "Transaction done ", Snackbar.LENGTH_SHORT);
//        snackbar.setActionTextColor(context.getResources().getColor(R.color.colorYellowTab));
        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundColor(context.getResources().getColor(R.color.colorBlackForToolbar));
//        TextView textView = snackbarView.findViewById(android.su)
        snackbar.show();
    }

    private void clearData() {
        btnSubmit.setEnabled(true);
        ViewCompat.setBackgroundTintList(btnSubmit, ContextCompat.getColorStateList(context, R.color.colorPrimaryDark));
        mViewModel.setSelectedProduct(null);
        txtQuantity.setText("");
        txtRemarks.setText("");
        txtDate.setText("");
        txtStock.setText("");
        txtPName.setText("");
        mTransType = 0;
        availableQuan = 0;
        prodNumber = null;
        ViewCompat.setBackgroundTintList(btnIn, ContextCompat.getColorStateList(context, R.color.colorWhite));
        ViewCompat.setBackgroundTintList(btnOut, ContextCompat.getColorStateList(context, R.color.colorWhite));

//        btnIn.setBackgroundColor(Color.WHITE);
        btnIn.setTextColor(Color.BLACK);
//        btnOut.setBackgroundColor(Color.WHITE);
        btnOut.setTextColor(Color.BLACK);
    }

    private boolean validate() {
        boolean status = true;
        if (TextUtils.isEmpty(txtDate.getText().toString())){
            status = false;
            txtDate.setError("REQUIRED");
        }
        if (TextUtils.isEmpty(txtPName.getText().toString())){
            status = false;
            txtPName.setError("REQUIRED");
        }
        if (TextUtils.isEmpty(txtQuantity.getText().toString())){
            status = false;
            txtQuantity.setError("REQUIRED");
        }
        if(mTransType==0){
            status = false;
            Toast.makeText(context, "please select IN/OUT", Toast.LENGTH_SHORT).show();
        }
        return status;
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i(TAG, "inside on stop method");
        mViewModel.setSelectedProduct(null);
    }

    public static class DatePickerFrag extends DialogFragment implements DatePickerDialog.OnDateSetListener{

        @NonNull
        @Override
        public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
            //use the current date as the default date in the picker..
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            //create new instance of Datepicker dialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            String date = dayOfMonth+"-"+(month+1)+"-"+year;

            long queryyear = year*10000;
            long querymonth = (month+1)*100;

            queryDate = queryyear+querymonth+dayOfMonth;
            txtDate.setText(date);
        }
    }
}
