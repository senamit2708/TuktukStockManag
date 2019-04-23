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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import smit.aen.tuktukstockmanag.NavMainActivity;
import smit.aen.tuktukstockmanag.R;
import smit.aen.tuktukstockmanag.ViewModels.LoginViewM;

public class StickyNotesAdd extends Fragment implements View.OnClickListener{

    private static final String TAG = StickyNotesAdd.class.getSimpleName();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private Context context;
    private LoginViewM mLoginViewModel;

    private EditText txtNote;
    private Button btnSubmit;
    private CardView cardOne;
    private CardView cardTwo;
    private CardView cardThree;
    private TextInputLayout textInput;

    private String mUId = null;
    private long colorCode = 1;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLoginViewModel = ViewModelProviders.of(getActivity()).get(LoginViewM.class);
        mUId = mLoginViewModel.getUserId();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.af_sticky_notes_add, container, false);
        context = container.getContext();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindView(view);

        btnSubmit.setOnClickListener(this);
        cardThree.setOnClickListener(this);
        cardOne.setOnClickListener(this);
        cardTwo.setOnClickListener(this);
    }

    private void bindView(View view) {
        txtNote = view.findViewById(R.id.txtNote);
        btnSubmit = view.findViewById(R.id.btnSubmit);
        cardOne = view.findViewById(R.id.cardOne);
        cardTwo = view.findViewById(R.id.cardTwo);
        cardThree = view.findViewById(R.id.cardThree);
        textInput = view.findViewById(R.id.textInput);
        textInput.setBoxBackgroundColor(context.getResources().getColor(R.color.textColorThree));

    }

    @Override
    public void onClick(View v) {
        if (v==btnSubmit){
           if (!validate()){
               return;
           }
            ((NavMainActivity)getActivity()).hideSoftKeyboard(v);
           loadToFirebase();
        }
        if (v==cardOne){
            Log.i(TAG, "card one is clicked");
            colorCode =1;
            textInput.setBoxBackgroundColor(context.getResources().getColor(R.color.textColorThree));
        }
        if (v==cardTwo){
            colorCode=2;
            textInput.setBoxBackgroundColor(context.getResources().getColor(R.color.textColorFour));
        }
        if (v==cardThree){
            colorCode=3;
            textInput.setBoxBackgroundColor(context.getResources().getColor(R.color.colorMainPage));
        }
    }

    private void loadToFirebase() {

        String note = txtNote.getText().toString();
        String dateold = new SimpleDateFormat("yyMMddHHmm", Locale.getDefault()).format(new Date());
        long date = Long.parseLong(dateold);

        Map<String, Object> mainMap = new HashMap<>();
        mainMap.put("note", note);
        mainMap.put("code", colorCode);
        mainMap.put("date", date);

        db.collection("UserColl")
                .document(mUId)
                .collection("StickColl")
                .add(mainMap)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(context, "Notes Added Successfully", Toast.LENGTH_SHORT).show();
                        clearData();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "Some error occured", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void clearData() {
        txtNote.setText("");
        Navigation.findNavController(getActivity(), R.id.btnSubmit).popBackStack();
    }

    private boolean validate() {
        boolean status = true;
        if (TextUtils.isEmpty(txtNote.getText().toString())){
            status = false;
            txtNote.setError("REQUIRED");
        }
        return status;
    }
}
