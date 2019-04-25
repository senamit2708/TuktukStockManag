package smit.aen.tuktukstockmanag.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import smit.aen.tuktukstockmanag.Model.ProductM;
import smit.aen.tuktukstockmanag.Model.TransactionModel;
import smit.aen.tuktukstockmanag.NavMainActivity;
import smit.aen.tuktukstockmanag.R;
import smit.aen.tuktukstockmanag.ViewModels.ProductViewM;
import smit.aen.tuktukstockmanag.ViewModels.TransactionViewM;
import smit.aen.tuktukstockmanag.adapter.TransactionListAdapter;
import smit.aen.tuktukstockmanag.interfaces.TopicIFace;

public class TransactionListFrag extends Fragment implements View.OnClickListener{

    private static final String TAG = TransactionListFrag.class.getSimpleName();
    private Context context;

    private RecyclerView mRecyclerview;
    private TransactionListAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private TransactionViewM mViewModel;
    private ProductViewM mProductViewModel;

    private ImageButton btnDateFrom;
    private ImageButton btnToDate;
    private ImageButton btnProduct;
    private Button btnSubmit;
    private Button btnClear;
    private Switch btnIn;
    private Switch btnOut;

    private static EditText txtFromDate;
    private static EditText txtToDate;
    private static EditText txtProduct;

    private TransactionModel product;
    private String productNumber ="##AMIT##";
    private static long queryFromDate=0;
    private static long queryToDate=0;
    private boolean statusIn = true;
    private boolean statusOut = true;
    private List<TransactionModel> transListFinal;
    //to check submit button is clikced with details..not with previous details
    private boolean statusFromDate=false;
    private boolean statusToDate = false;

    @Override
    public void onStart() {
        super.onStart();
        //this is for toolbar color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            ((NavMainActivity)getActivity()).getSupportActionBar().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(context, R.color.colorPrimary)));
            ((NavMainActivity)getActivity()).updateStatusBarColor("#4E0D3A");
        }
    }

    private void bindView(View view) {

        btnDateFrom = view.findViewById(R.id.btnDateFrom);
        btnToDate = view.findViewById(R.id.btnToDate);
        btnProduct = view.findViewById(R.id.btnProduct);
        btnSubmit = view.findViewById(R.id.btnSubmit);
        btnClear = view.findViewById(R.id.btnClear);
        txtFromDate = view.findViewById(R.id.txtFromDate);
        txtToDate = view.findViewById(R.id.txtToDate);
        txtProduct = view.findViewById(R.id.txtProduct);
        btnIn = view.findViewById(R.id.btnIn);
        btnOut = view.findViewById(R.id.btnOut);

        mRecyclerview = view.findViewById(R.id.recyclerview);
        mAdapter = new TransactionListAdapter();
        mLayoutManager = new LinearLayoutManager(context);
        mRecyclerview.setLayoutManager(mLayoutManager);
        mRecyclerview.setAdapter(mAdapter);


    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = ViewModelProviders.of(getActivity()).get(TransactionViewM.class);
        mProductViewModel = ViewModelProviders.of(getActivity()).get(ProductViewM.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.af_transaction_list, container, false);
        context = container.getContext();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindView(view);

        btnProduct.setOnClickListener(this);
        btnToDate.setOnClickListener(this);
        btnDateFrom.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
        btnClear.setOnClickListener(this);
        txtFromDate.setOnClickListener(this);
        txtToDate.setOnClickListener(this);
        txtProduct.setOnClickListener(this);


        txtFromDate.setFocusable(false);
        txtToDate.setFocusable(false);
        txtProduct.setFocusable(false);
        txtFromDate.setClickable(true);
        txtToDate.setClickable(true);
        txtProduct.setClickable(true);



        //this i have used to load all the transactions
//        mViewModel.getCompleteTransList().observe(this, new Observer<List<TransactionModel>>() {
//            @Override
//            public void onChanged(List<TransactionModel> transList) {
//                mAdapter.setTransList(transList);
//            }
//        });

        btnIn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    Log.i(TAG, "inside onchecked method");
                    statusIn=true;
                    loadTransTypeList();
                }else {
                    statusIn = false;
                    loadTransTypeList();
                    Log.i(TAG, "unchecked done ");
                }
            }
        });

        btnOut.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    statusOut= true;
                    loadTransTypeList();
                }else {
                    statusOut=false;
                    loadTransTypeList();
                }
            }
        });

        mProductViewModel.getSelectedProduct().observe(this, new Observer<ProductM>() {
            @Override
            public void onChanged(ProductM product) {
                if (product!= null){
                    txtProduct.setText(product.getName());
                    productNumber = product.getNum();
                }
            }
        });
    }

    private void loadTransTypeList() {
        Log.i(TAG, "statusIn is "+statusIn);
        Log.i(TAG, "status out is "+statusOut);
       if (statusIn==true && statusOut==true){
           Log.i(TAG, "inside both true statement");
           mAdapter.setTransList(transListFinal);
       }
       else if (statusIn==true){
           loadStatusInList(1);

       }else if (statusOut==true){
           loadStatusInList(2);
       }else {
           loadStatusInList(3);
           Toast.makeText(context, "Do changes in IN/OUT switch", Toast.LENGTH_SHORT).show();
       }
    }

    private void loadStatusInList(int i) {
        int checkTrans=0;
        if (i==1){
             checkTrans = 1;
        }else if (i==2){
            checkTrans =2;
        }else {
            mAdapter.setTransList(null);
            return;
        }
        List<TransactionModel> newTransList = new ArrayList<>();
        if (transListFinal!= null){
            int size = transListFinal.size();
            for (int j=0; j<size; j++){
                if (transListFinal.get(j).getTrans()==checkTrans){
                    newTransList.add(transListFinal.get(j));
                }
            }
            mAdapter.setTransList(newTransList);
        }

    }


    @Override
    public void onClick(View v) {
        if (v==btnDateFrom){
            showDatePickerDialog();
        }
        if (v==btnToDate){
            showDateToPickerDialog();
        }
        if (v==btnProduct){
            selectProduct(v);
        }
        if (v==btnSubmit){
//           if (!validate()){
//               return;
//           }
            transactionListLoad();
        }
        if (v==btnClear){
            clearData();
        }
        if (v==txtFromDate){
            showDatePickerDialog();
        }
        if (v==txtToDate){
            showDateToPickerDialog();
        }
        if (v==txtProduct){
            selectProduct(v);
        }
    }

    private void selectProduct(View v) {
        mProductViewModel.setProValForSearch(2);
        Navigation.findNavController(v).navigate(R.id.action_transactionListFrag_to_productTypeTabFrag);
    }

    private void clearData() {
        txtToDate.setText("");
        txtFromDate.setText("");
        txtProduct.setText("");
        txtProduct.setHint("Product(optional)");
        queryFromDate = 0;
        queryToDate=0;
        productNumber ="##AMIT##";
        mProductViewModel.setSelectedProduct(null);
        mAdapter.setTransList(null);
    }

    private void transactionListLoad() {
        mAdapter.setTransList(null);
        if (queryFromDate==0 || queryToDate==0){
            Toast.makeText(context, "Date should not be null", Toast.LENGTH_SHORT).show();
            return;
        }
        if ((txtFromDate.getText().toString()).equals("Select From Date") || (txtToDate.getText().toString()).equals("Select To Date")){
            Toast.makeText(context, "Date should not be null", Toast.LENGTH_SHORT).show();
            return;
        }
        if (queryToDate<queryFromDate){
            Toast.makeText(context, "Change 'TO Date' ", Toast.LENGTH_SHORT).show();
            return;
        }

        mViewModel.getSelectedTransList(queryFromDate, queryToDate, productNumber).observe(this, new Observer<List<TransactionModel>>() {
            @Override
            public void onChanged(List<TransactionModel> transList) {
                if (transList!= null){
                    transListFinal = new ArrayList<>();
                    transListFinal.addAll(transList);
                    loadTransTypeList();
                }else {
                    mAdapter.setTransList(null);
                    Toast.makeText(context,"No transaction available", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }

    private boolean validate() {
        boolean status = true;
        if (txtFromDate.getText().equals("Select From Date")){
            status = false;
        }
        if (txtToDate.getText().equals("Select To Date")){
            status = false;
        }
        return status;

    }

    private void showDateToPickerDialog() {
        DialogFragment newFragment = new DatePickerFragTwo();
        newFragment.show(getActivity().getSupportFragmentManager(), "datePickerTwo");
    }

    private void showDatePickerDialog() {
        DialogFragment newFragment = new DatePickerFragOne();
        newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
    }

    @Override
    public void onStop() {
        super.onStop();
        mProductViewModel.setSelectedProduct(null);
    }

    public static class DatePickerFragOne extends DialogFragment implements DatePickerDialog.OnDateSetListener{

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
            txtFromDate.setText(date);
            long queryyear = year*10000;
            long querymonth = (month+1)*100;

            queryFromDate = queryyear+querymonth+dayOfMonth;

        }
    }


    public static class DatePickerFragTwo extends DialogFragment implements DatePickerDialog.OnDateSetListener{
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
            txtToDate.setText(date);
            long queryyear = year*10000;
            long querymonth = (month+1)*100;

            queryToDate = queryyear+querymonth+dayOfMonth;
        }
    }
}
