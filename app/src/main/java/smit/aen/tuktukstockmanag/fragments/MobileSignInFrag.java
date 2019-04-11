package smit.aen.tuktukstockmanag.fragments;

import android.app.Activity;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.auth.User;

import java.util.concurrent.TimeUnit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import smit.aen.tuktukstockmanag.NavMainActivity;
import smit.aen.tuktukstockmanag.R;
import smit.aen.tuktukstockmanag.ViewModels.LoginViewM;

public class MobileSignInFrag extends Fragment implements View.OnClickListener {

    private static final String TAG = MobileSignInFrag.class.getSimpleName();
    private static final String PREFERENCE = "preference";
    private static final String PREF_UID = "userId";
    private static final String PREF_RESULT = "result";
    private static final String PREF_UTYPE="userType";
    private static final String PREF_CHECK = "check";
    private Context context;

    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback;
    private boolean mVerificationInProgress = false;
    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;
//    private DatabaseReference mDatabase;

    private EditText mPhoneNumberField;
    private EditText mPhoneOtp;
    private TextView mTxtResendOtp;
    private TextView mTxtSignInHeadline;
    private Button btnPhoneNumber;
    private Button btnSubmit;
    private Button btnResendOtp;
    private TextInputLayout txtStyleEditPhoneNumber;
    private TextInputLayout txtStylePhoneOtp;

    private LoginViewM mViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mViewModel = ViewModelProviders.of(getActivity()).get(LoginViewM.class);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mobile_signin, container, false);
        context = container.getContext();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindView(view);

        mPhoneNumberField.setText(mViewModel.getMobileNo());

        btnPhoneNumber.setOnClickListener(this);
        btnSubmit.setOnClickListener(this);
        btnResendOtp.setOnClickListener(this);

        mFirebaseAuth = FirebaseAuth.getInstance();
//        mDatabase = FirebaseDatabase.getInstance().getReference();

//        Typeface customFont = Typeface.createFromAsset(getAssets(), "fonts/Rubik-Bold.ttf");

//        mTxtSignInHeadline.setTypeface(customFont);
        //7437810643

        mCallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                Log.i(TAG, "inside onverification completed method");
//                mVerificationInProgress = false;
                signInWithPhoneAuthCredential(phoneAuthCredential);

            }
            @Override
            public void onVerificationFailed(FirebaseException e) {

                Log.i(TAG, "inside onverificationfailed method");
                mVerificationInProgress = false;
                if (e instanceof FirebaseAuthInvalidCredentialsException){
                    mPhoneNumberField.setError("Invalid phone number  "+((FirebaseAuthInvalidCredentialsException) e).getErrorCode());
                    Log.i(TAG, "inside the exception one of verification failed");
                }else if (e instanceof FirebaseTooManyRequestsException){
                    Log.i(TAG, "inside the exception two of verification failed "+e.getMessage());

//                    Toast.makeText(SignInActivity.this, "Please try another way of login", Toast.LENGTH_SHORT).show();
                }
                Log.i(TAG, "inside onverification failed last portion  "+e.getMessage());
            }

            @Override
            public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(verificationId, forceResendingToken);
                Log.d(TAG, "the code sent is "+verificationId);
                mVerificationId = verificationId;
                mResendToken = forceResendingToken;

            }
        };
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential phoneAuthCredential) {

        mFirebaseAuth.signInWithCredential(phoneAuthCredential)
                .addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            Log.d(TAG, "login credential successful");
                            FirebaseUser user = task.getResult().getUser();
                            Log.i(TAG, "the user is "+user);
                            onAuthSuccess(user);
                        }
                        else {
                            mPhoneOtp.setError("Invalid OTP number");
                            Log.i(TAG, "login failed inside signInwithphoneauthcredential");
                        }
                    }
                });


    }

    private void onAuthSuccess(FirebaseUser user) {
        String phoneNumber = mPhoneNumberField.getText().toString();
        saveToSharedPref();
        writeNewUser(user.getUid(), phoneNumber);
        Toast.makeText(context, "Authentication successful", Toast.LENGTH_SHORT).show();
        Navigation.findNavController(getActivity(), R.id.btn_sign_in).popBackStack(R.id.testOneFrag, false);
        //TODO: AFTER AUTH SUCCESS WHTAT TO DO
//        finish();
    }

    private void saveToSharedPref() {
        //i am not using default sharedprefernce here
        // @TODO i think its good to add default shared preferne ...   https://stackoverflow.com/questions/7549802/save-login-detailspreferences-android

        SharedPreferences mSharedPref = context.getSharedPreferences(PREFERENCE, context.MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPref.edit();
        mEditor.putString(PREF_UID, mViewModel.getUserId());
        mEditor.putString(PREF_RESULT, mViewModel.getResult());
        mEditor.putInt(PREF_CHECK, 1);
        mEditor.putLong(PREF_UTYPE, mViewModel.getuType());
        mEditor.apply();
    }

    private void writeNewUser(String uid, String phoneNumber) {
        User user = new User(phoneNumber.trim());
        //TODO: NOw where in document i want to write this mobile number
    }

    private void bindView(View view) {
        mPhoneNumberField = view.findViewById(R.id.edt_phone_number);
        mPhoneOtp = view.findViewById(R.id.edt_phone_otp);
        mTxtResendOtp = view.findViewById(R.id.txtResendOtp);
//        mTxtSignInHeadline = view.findViewById(R.id.txtSignInHeadline);
        btnPhoneNumber = view.findViewById(R.id.btn_phone_number_enter);
        btnSubmit = view.findViewById(R.id.btn_sign_in);
        btnResendOtp = view.findViewById(R.id.btn_resend_otp);
//        txtStyleEditPhoneNumber = view.findViewById(R.id.txtStyleEditPhoneNumber);
//        txtStylePhoneOtp = view.findViewById(R.id.txtStylePhoneOtp);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.btn_phone_number_enter:
                if(!validatePhoneNumber()){
                    Log.i(TAG, "inside the if statement");
                    return;
                }
                Log.i(TAG, "inside the else statement ");
                String phoneNumberField = mPhoneNumberField.getText().toString();
                String countryCode = "+91-";
                String phoneNumber = countryCode +phoneNumberField;
                Log.d(TAG, "phone number is "+phoneNumber);
                startPhoneNumberVerification(phoneNumber);
//                txtStyleEditPhoneNumber.setVisibility(View.GONE);
//                btnPhoneNumber.setVisibility(View.GONE);
//                txtStylePhoneOtp.setVisibility(View.VISIBLE);
//                btnSubmit.setVisibility(View.VISIBLE);
//                mTxtResendOtp.setVisibility(View.VISIBLE);
//                btnResendOtp.setVisibility(View.VISIBLE);

                break;
            case R.id.btn_sign_in:
                ((NavMainActivity)getActivity()).hideSoftKeyboard(v);
                String code = mPhoneOtp.getText().toString();
                if (TextUtils.isEmpty(code)) {
                    mPhoneOtp.setError("Cannot be empty.");
                    return;
                }
                verifyPhoneNumberWithCode(mVerificationId, code);

                break;
            case R.id.btn_resend_otp:
                Log.i(TAG, "inside resend otp method");
                resendVerificationCode(mPhoneNumberField.getText().toString(), mResendToken);
                break;
            default:
                Log.i(TAG, "no button found of such type");

        }

    }

    private void resendVerificationCode(String phoneNumber, PhoneAuthProvider.ForceResendingToken token) {
        Log.i(TAG, "inside resend verification code method");
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,
                60,
                TimeUnit.SECONDS,
                (Activity) context,
                mCallback,
                token);
        mVerificationInProgress = true;
    }

    private void verifyPhoneNumberWithCode(String mVerificationId, String code) {
        PhoneAuthCredential credential= PhoneAuthProvider.getCredential(mVerificationId,code);
        signInWithPhoneAuthCredential(credential);
    }

    private void startPhoneNumberVerification(String phoneNumber) {
        Log.i(TAG, "inside start phone number verification method");
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,
                60,
                TimeUnit.SECONDS,
                (Activity) context,
                mCallback);
        mVerificationInProgress = true;
    }

    private boolean validatePhoneNumber() {
        String phoneNumber = mPhoneNumberField.getText().toString();
        Log.i(TAG, "the phone number is "+phoneNumber);
        if (TextUtils.isEmpty(phoneNumber)){
            mPhoneNumberField.setError("invalid mobile number");
            Log.i(TAG, "inside validate phone number");
            Toast.makeText(context, "invalid phone number", Toast.LENGTH_SHORT).show();
            return false;
        }
        //for now i have commented it...i dont think its needed in this app
//        if(!getContryCode(phoneNumber)){
//            Log.i(TAG, "countrycode here issue");
//            return false;
//        }
        Log.i(TAG, "final step of validate phone number");
        return true;
    }

//    private boolean getContryCode(String phoneNumber) {
//        String countryCode;
//        TelephonyManager tm = (TelephonyManager)context.getSystemService(context.TELEPHONY_SERVICE);
//        String countryCodeValue = tm.getSimCountryIso();
//        if(countryCodeValue.equals("in")){
//            Log.i(TAG, "the country code is india");
//            return true;
//        }
//        Log.i(TAG, "inside get contry code method and we didnt get any code here  "+countryCodeValue);
//        return false;
//    }


}
