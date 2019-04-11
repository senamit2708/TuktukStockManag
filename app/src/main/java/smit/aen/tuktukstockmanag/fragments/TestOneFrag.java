package smit.aen.tuktukstockmanag.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import smit.aen.tuktukstockmanag.Model.TopicMain;
import smit.aen.tuktukstockmanag.R;
import smit.aen.tuktukstockmanag.ViewModels.LoginViewM;
import smit.aen.tuktukstockmanag.adapter.TestOneAdap;
import smit.aen.tuktukstockmanag.interfaces.TopicIFace;

public class TestOneFrag extends Fragment implements TopicIFace {

    private static final String TAG = TestOneFrag.class.getSimpleName();
    private LoginViewM mViewModel;
    private Context context;

    private RecyclerView mRecyclerview;
    private RecyclerView.LayoutManager mLayoutManager;
    private TestOneAdap mAdapter;


    @Override
    public void onStart() {
        super.onStart();
        //uncomment the below codes
//        if (!mViewModel.getLogin()){
//            Navigation.findNavController(getActivity(), R.id.btnHide).navigate(R.id.action_testOneFrag_to_signInFrag);
//        }else if (FirebaseAuth.getInstance().getCurrentUser()==null){
//            Navigation.findNavController(getActivity(), R.id.btnHide).navigate(R.id.action_testOneFrag_to_signInFrag);
//        }
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

        loadTopic();
    }

    private void loadTopic() {
        List<TopicMain> topicName = new ArrayList<>();

        topicName.add(new TopicMain("Product",R.drawable.ic_product_three));
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
        mRecyclerview.setAdapter(mAdapter)  ;
    }

    @Override
    public void funTopicName(String topic) {
        if (topic.equals("Product")){
            Navigation.findNavController(getActivity(), R.id.btnHide).navigate(R.id.action_testOneFrag_to_productListFrag);
        }
        if (topic.equals("Transaction")){
            Navigation.findNavController(getActivity(), R.id.btnHide).navigate(R.id.action_testOneFrag_to_transactionListFrag);

        }
        if (topic.equals("New Product")){
            Navigation.findNavController(getActivity(), R.id.btnHide).navigate(R.id.action_testOneFrag_to_productEntryFrag);

        }
        if (topic.equals("IN / OUT")){
            Navigation.findNavController(getActivity(), R.id.btnHide).navigate(R.id.action_testOneFrag_to_transactionFrag);

        }
    }
}
