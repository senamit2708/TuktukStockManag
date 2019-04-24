package smit.aen.tuktukstockmanag.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import smit.aen.tuktukstockmanag.NavMainActivity;
import smit.aen.tuktukstockmanag.R;
import smit.aen.tuktukstockmanag.ViewModels.LoginViewM;

public class SignInFrag extends Fragment {

    private static final String TAG = SignInFrag.class.getSimpleName();
    private Context context;

    private FirebaseAuth mAuth;
    private EditText txtUserName;
    private EditText txtPassword;
    private Button btnSubmit;
//    private Button btnSignUP;
    private LoginViewM mViewModel;
    private SharedPreferences mSharedPref;
    private FirebaseFirestore db;

    private static final String PREFERENCE = "preference";
    private static final String PREF_UID = "userId";
    private static final String PREF_RESULT = "result";
    private static final String PREF_CHECK = "check";
    private static final String PREF_UTYPE="userType";

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        mViewModel = ViewModelProviders.of(getActivity()).get(LoginViewM.class);
        db = FirebaseFirestore.getInstance();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.af_signin, container, false);
        context=container.getContext();
        mSharedPref = context.getSharedPreferences(PREFERENCE, context.MODE_PRIVATE);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        bindView(view);
        loadUid();

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((NavMainActivity)getActivity()).hideSoftKeyboard(v);
                SignIn(txtUserName.getText().toString().trim(), txtPassword.getText().toString().trim());
            }
        });
    }

    private void loadUid() {
        Log.i(TAG, "inside loadUid method");
        if (mSharedPref.contains(PREF_UID)){
            Log.i(TAG, "shared pref have uId");
            txtUserName.setText(mSharedPref.getString(PREF_UID,"12345"));
        }
    }

    private void SignIn(String uId, final String password) {
        Log.i(TAG, "inside signin fun");
        btnSubmit.setEnabled(false);
        ViewCompat.setBackgroundTintList(btnSubmit, ContextCompat.getColorStateList(context, R.color.colorGray));


        if (!validateForm()){
            btnSubmit.setEnabled(true);
            ViewCompat.setBackgroundTintList(btnSubmit, ContextCompat.getColorStateList(context, R.color.colorPrimaryDark));
            return;
        }
        if (mSharedPref.contains(PREF_RESULT)) {
            Log.i(TAG, "inside password check ");
            if (password.equals(mSharedPref.getString(PREF_RESULT,null))){
                mViewModel.setLogin(true);
                Log.i(TAG, "password is correct");
                Log.i(TAG, "firebase auth is "+FirebaseAuth.getInstance().getCurrentUser());
                if (FirebaseAuth.getInstance().getCurrentUser()!= null){
                    Log.i(TAG, "password matched ");
                    extractFromSharedPref();
                    Navigation.findNavController(getActivity(), R.id.btnSubmit).popBackStack();
                }else {
                    Toast.makeText(context, "Correct password, wait to load mobile number", Toast.LENGTH_SHORT).show();
                    loadDataViewM(uId, password);
                }
            }else {
                Toast.makeText(context, "Incorrect password ", Toast.LENGTH_SHORT).show();
                ViewCompat.setBackgroundTintList(btnSubmit, ContextCompat.getColorStateList(context, R.color.colorPrimaryDark));
                btnSubmit.setEnabled(true);
            }
        }
        else {
            Log.i(TAG, "inside no shared pref found method");
            getPassFirebase(uId, password);
        }
    }

    private void extractFromSharedPref() {
        String uId = mSharedPref.getString(PREF_UID, "1000");
        long uType = mSharedPref.getLong(PREF_UTYPE, 10);
        mViewModel.setuType(uType);
        mViewModel.setuId(uId);
    }

    private void loadDataViewM(String uId, final String password) {
        db.collection("UidColl")
                .document(uId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()){
                            //details to be kept in LoginViewM for futher reference
//                            String className = documentSnapshot.getString("class");
                            String mob = documentSnapshot.getString("mob");
//                            String roll = documentSnapshot.getString("roll");
                            String uId = documentSnapshot.getString("uId");
                            String name = documentSnapshot.getString("name");
                            String uId_pass = documentSnapshot.getString("pass");
//                            mViewModel.setStudentDetail(className, mob, roll, uId, name, uId_pass);
                            long user_type = documentSnapshot.getLong("admin");
                            mViewModel.setUserDetail(mob, uId, name, uId_pass, user_type);

                            Navigation.findNavController(getActivity(), R.id.btnSubmit).navigate(R.id.action_signInFrag_to_mobileSignInFrag);

                        }
                    }
                });

    }

    private void getPassFirebase(String uId, final String password) {

        db.collection("UidColl")
                .document(uId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()){
                            String pass = documentSnapshot.getString("pass");
                            if (password.equals(pass)){
                                mViewModel.setLogin(true);
                                Toast.makeText(context, "Login Successful", Toast.LENGTH_SHORT).show();
                                //details to be kept in LoginViewM for futher reference
                                String mob = documentSnapshot.getString("mob");
                                String uId = documentSnapshot.getString("uId");
                                String name = documentSnapshot.getString("name");
                                String uId_pass = documentSnapshot.getString("pass");
                                long user_type = documentSnapshot.getLong("admin");
                                mViewModel.setUserDetail(mob, uId, name, uId_pass, user_type);
                                if (FirebaseAuth.getInstance().getCurrentUser()!= null){
                                    Navigation.findNavController(getActivity(), R.id.btnSubmit).popBackStack();
                                }else {
                                    Navigation.findNavController(getActivity(), R.id.btnSubmit).navigate(R.id.action_signInFrag_to_mobileSignInFrag);

                                }

                            }else {
                                Toast.makeText(context, "Incorrect password", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    private boolean validateForm() {
        boolean valid = true;
        if (TextUtils.isEmpty(txtUserName.getText().toString())){
            txtUserName.setError("Required");
            valid= false;
        }
        if (TextUtils.isEmpty(txtPassword.getText().toString())){
            txtPassword.setError("Required");
            valid = false;
        }
        return valid;
    }

    private void bindView(View view) {
        txtUserName = view.findViewById(R.id.txtUserName);
        txtPassword = view.findViewById(R.id.txtPassword);
        btnSubmit = view.findViewById(R.id.btnSubmit);
        ViewCompat.setBackgroundTintList(btnSubmit, ContextCompat.getColorStateList(context, R.color.colorPrimaryDark));

//        btnSignUP = view.findViewById(R.id.btnSignUP);
//        btnSubmit.setBackground(ContextCompat.getDrawable(context, R.drawable.login_button_draw));

    }

    @Override
    public void onResume() {
        super.onResume();
        ((AppCompatActivity)getActivity()).getSupportActionBar().hide();

    }

    @Override
    public void onStop() {
        super.onStop();
        ((AppCompatActivity)getActivity()).getSupportActionBar().show();

    }
}
