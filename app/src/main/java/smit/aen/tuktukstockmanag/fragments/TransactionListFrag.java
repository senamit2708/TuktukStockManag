package smit.aen.tuktukstockmanag.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import smit.aen.tuktukstockmanag.Model.TransactionModel;
import smit.aen.tuktukstockmanag.R;
import smit.aen.tuktukstockmanag.ViewModels.TransactionViewM;
import smit.aen.tuktukstockmanag.adapter.TransactionListAdapter;

public class TransactionListFrag extends Fragment {

    private static final String TAG = TransactionListFrag.class.getSimpleName();
    private Context context;

    private RecyclerView mRecyclerview;
    private TransactionListAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private TransactionViewM mViewModel;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = ViewModelProviders.of(getActivity()).get(TransactionViewM.class);
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

        mViewModel.getCompleteTransList().observe(this, new Observer<List<TransactionModel>>() {
            @Override
            public void onChanged(List<TransactionModel> transList) {
                mAdapter.setTransList(transList);
            }
        });
    }

    private void bindView(View view) {
        mRecyclerview = view.findViewById(R.id.recyclerview);
        mAdapter = new TransactionListAdapter();
        mLayoutManager = new LinearLayoutManager(context);
        mRecyclerview.setLayoutManager(mLayoutManager);
        mRecyclerview.setAdapter(mAdapter);
    }
}
