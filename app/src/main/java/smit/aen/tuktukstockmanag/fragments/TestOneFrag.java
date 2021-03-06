package smit.aen.tuktukstockmanag.fragments;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import smit.aen.tuktukstockmanag.Model.TopicMain;
import smit.aen.tuktukstockmanag.NavMainActivity;
import smit.aen.tuktukstockmanag.R;
import smit.aen.tuktukstockmanag.ViewModels.LoginViewM;
import smit.aen.tuktukstockmanag.adapter.TestOneAdap;
import smit.aen.tuktukstockmanag.interfaces.TopicIFace;

public class TestOneFrag extends Fragment implements TopicIFace {

    private static final String TAG = TestOneFrag.class.getSimpleName();
    private static final String ENTRY_CHECK_FRAG = "entryCheck";
    private LoginViewM mViewModel;
    private Context context;
    private ConstraintLayout constraint;

    private RecyclerView mRecyclerview;
    private RecyclerView.LayoutManager mLayoutManager;
    private TestOneAdap mAdapter;

    private long admin=10;


    @Override
    public void onStart() {
        super.onStart();
        //uncomment the below codes
        if (!mViewModel.getLogin()){
            Navigation.findNavController(getActivity(), R.id.btnHide).navigate(R.id.action_testOneFrag_to_signInFrag);
        }else if (FirebaseAuth.getInstance().getCurrentUser()==null){
            Navigation.findNavController(getActivity(), R.id.btnHide).navigate(R.id.action_testOneFrag_to_signInFrag);
        }

        //this is for toolbar color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            ((NavMainActivity)getActivity()).getSupportActionBar().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(context, R.color.colorPrimary)));
            ((NavMainActivity)getActivity()).updateStatusBarColor("#4E0D3A");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = ViewModelProviders.of(getActivity()).get(LoginViewM.class);


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.af_test_onf, container, false);
        context = container.getContext();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindView(view);
        admin = mViewModel.getuType();
        loadTopic();
    }

    private void loadTopic() {
        List<TopicMain> topicName = new ArrayList<>();

        topicName.add(new TopicMain("Notes",R.drawable.ic_event_note_one));
        topicName.add(new TopicMain("Transaction",R.drawable.ic_transaction_three));
        topicName.add(new TopicMain("New Product",R.drawable.ic_product_white));
        topicName.add(new TopicMain("IN / OUT",R.drawable.ic_transaction_white));

        mAdapter.setTopic(topicName);

    }

    private void bindView(View view) {
        mRecyclerview = view.findViewById(R.id.recyclerview);
        mLayoutManager = new GridLayoutManager(context,2);
        mAdapter = new TestOneAdap(context, this);
        mRecyclerview.setLayoutManager(mLayoutManager);
        mRecyclerview.setAdapter(mAdapter);

        constraint = view.findViewById(R.id.constraint);
    }

    @Override
    public void funTopicName(String topic) {
        if (topic.equals("Notes")){
            Navigation.findNavController(getActivity(), R.id.btnHide).navigate(R.id.action_testOneFrag_to_sticknoteFrag);
        }
        if (topic.equals("Transaction")){
            Log.i(TAG, "transaction value of admin is "+admin);
            if (admin==11){
                Navigation.findNavController(getActivity(), R.id.btnHide).navigate(R.id.action_testOneFrag_to_transactionListFrag);
            }else {
                snackAdmin();
            }

        }
        if (topic.equals("New Product")){
            if (admin==11){
                Bundle bundle1 = new Bundle();
                bundle1.putInt(ENTRY_CHECK_FRAG, 1);
                Navigation.findNavController(getActivity(), R.id.btnHide).navigate(R.id.action_testOneFrag_to_productEntryFrag, bundle1);
            }else {
                snackAdmin();
            }

        }
        if (topic.equals("IN / OUT")){
            Navigation.findNavController(getActivity(), R.id.btnHide).navigate(R.id.action_testOneFrag_to_transactionFrag);

        }
    }
    private void snackAdmin() {
        Snackbar.make(constraint, "Unauthorized to open", Snackbar.LENGTH_SHORT).show();
    }
}
