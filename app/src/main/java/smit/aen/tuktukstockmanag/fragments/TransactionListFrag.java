package smit.aen.tuktukstockmanag.fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.Calendar;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import smit.aen.tuktukstockmanag.Model.ProductM;
import smit.aen.tuktukstockmanag.Model.TransactionModel;
import smit.aen.tuktukstockmanag.R;
import smit.aen.tuktukstockmanag.ViewModels.ProductViewM;
import smit.aen.tuktukstockmanag.ViewModels.TransactionViewM;
import smit.aen.tuktukstockmanag.adapter.TransactionListAdapter;

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

    private static TextView txtFromDate;
    private static TextView txtToDate;
    private static TextView txtProduct;

    private void bindView(View view) {

        btnDateFrom = view.findViewById(R.id.btnDateFrom);
        btnToDate = view.findViewById(R.id.btnToDate);
        btnProduct = view.findViewById(R.id.btnProduct);
        txtFromDate = view.findViewById(R.id.txtFromDate);
        txtToDate = view.findViewById(R.id.txtToDate);
        txtProduct = view.findViewById(R.id.txtProduct);

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

        mViewModel.getCompleteTransList().observe(this, new Observer<List<TransactionModel>>() {
            @Override
            public void onChanged(List<TransactionModel> transList) {
                mAdapter.setTransList(transList);
            }
        });

        mProductViewModel.getSelectedProduct().observe(this, new Observer<ProductM>() {
            @Override
            public void onChanged(ProductM product) {
                txtProduct.setText(product.getName());
                //TODO: to do changes here
            }
        });
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
            Navigation.findNavController(v).navigate(R.id.action_transactionListFrag_to_productSearchListFrag);
        }
    }

    private void showDateToPickerDialog() {
        DialogFragment newFragment = new DatePickerFragTwo();
        newFragment.show(getActivity().getSupportFragmentManager(), "datePickerTwo");
    }

    private void showDatePickerDialog() {
        DialogFragment newFragment = new DatePickerFragOne();
        newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
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
        }
    }
}
