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
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import smit.aen.tuktukstockmanag.Model.StickModel;
import smit.aen.tuktukstockmanag.R;
import smit.aen.tuktukstockmanag.ViewModels.LoginViewM;
import smit.aen.tuktukstockmanag.ViewModels.SticknoteViewM;
import smit.aen.tuktukstockmanag.adapter.SticknoteAdap;
import smit.aen.tuktukstockmanag.interfaces.TopicIFace;

public class SticknoteFrag extends Fragment implements View.OnClickListener, TopicIFace {

    private static final String TAG = SticknoteFrag.class.getSimpleName();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private LoginViewM mLoginViewModel;
    private SticknoteViewM mViewModel;

    private Context context;
    private String mUId = null;

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManger;
    private SticknoteAdap mAdapter;

    private FloatingActionButton fabAdd;
    private CoordinatorLayout coordinator;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mLoginViewModel = ViewModelProviders.of(getActivity()).get(LoginViewM.class);
        mViewModel = ViewModelProviders.of(getActivity()).get(SticknoteViewM.class);
        mUId = mLoginViewModel.getUserId();
        Log.i(TAG, "the value of uid is "+mUId);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.af_stickynote, container, false);
    context = container.getContext();
    return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindView(view);

        fabAdd.setOnClickListener(this);

        mViewModel.getStickNotes(mUId).observe(this, new Observer<List<StickModel>>() {
            @Override
            public void onChanged(List<StickModel> stickList) {
                mAdapter.setStickList(stickList);
            }
        });
    }

    private void bindView(View view) {
        mRecyclerView = view.findViewById(R.id.recyclerview);
        mLayoutManger = new GridLayoutManager(context, 2);
        ((GridLayoutManager) mLayoutManger).setOrientation(LinearLayoutManager.HORIZONTAL);
        mAdapter = new SticknoteAdap(context, this);
        mRecyclerView.setLayoutManager(mLayoutManger);
        mRecyclerView.setAdapter(mAdapter);

        fabAdd = view.findViewById(R.id.fabAdd);
        coordinator = view.findViewById(R.id.coordinator);
    }

    @Override
    public void onClick(View v) {
        if (v == fabAdd){
            Navigation.findNavController(v).navigate(R.id.action_sticknoteFrag_to_stickyNotesAdd);
        }
    }

    @Override
    public void funTopicName(String topic) {
        loadSnack(topic);

    }

    private void loadSnack(final String topic) {
        Snackbar snackbar = Snackbar.make(coordinator, "Sure want to delete", Snackbar.LENGTH_SHORT);
        snackbar.setAction(R.string.done_string, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteDoc(topic);
            }
        });
        snackbar.show();
    }

    private void deleteDoc(String topic) {
        db.collection("UserColl")
                .document(mUId)
                .collection("StickColl")
                .document(topic)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(context, "Note deleted successfully ", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "Some error occured", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
