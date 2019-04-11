package smit.aen.tuktukstockmanag.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import smit.aen.tuktukstockmanag.R;

public class AccountFrag extends Fragment implements View.OnClickListener{

    private static final String TAG = AccountFrag.class.getSimpleName();
    private Context context;

//    private TextView txtName;
//    private TextView txtRollNo;
//    private TextView txtEmailId;
//    private TextView txtUserId;
//    private TextView txtClass;
//    private TextView txtClassTeacher;
    private Button btnLogout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.af_account, container, false);
        context = container.getContext();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bindView(view);

        btnLogout.setOnClickListener(this);
    }

    private void bindView(View view) {
//        txtName = view.findViewById(R.id.txtName);
//        txtClass = view.findViewById(R.id.txtClass);
//        txtClassTeacher = view.findViewById(R.id.txtClassTeacher);
//        txtEmailId = view.findViewById(R.id.txtEmailId);
//        txtRollNo = view.findViewById(R.id.txtRollNo);
//        txtUserId = view.findViewById(R.id.txtUserId);
        btnLogout = view.findViewById(R.id.btnLogout);
    }

    @Override
    public void onClick(View v) {
        if (v.getId()== R.id.btnLogout){
            signOutDevice();
        }
    }

    private void signOutDevice() {
        FirebaseAuth.getInstance().signOut();
        Navigation.findNavController(getActivity(), R.id.btnLogout).popBackStack(R.id.testOneFrag, false);
    }
}

