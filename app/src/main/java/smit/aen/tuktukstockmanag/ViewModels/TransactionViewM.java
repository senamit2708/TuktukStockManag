package smit.aen.tuktukstockmanag.ViewModels;

import android.app.Application;
import android.util.Log;

import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import smit.aen.tuktukstockmanag.Model.TransactionModel;

public class TransactionViewM extends AndroidViewModel {

    private static final String TAG = TransactionViewM.class.getSimpleName();

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private MutableLiveData<List<TransactionModel>> mTransCompleteList;
    ListenerRegistration transListRegistration;



    public TransactionViewM(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<List<TransactionModel>> getCompleteTransList() {
        if (mTransCompleteList!= null){
            return mTransCompleteList;
        }
        mTransCompleteList = new MutableLiveData<>();
        loadCompleteTransList();
        return mTransCompleteList;

    }

    private void loadCompleteTransList() {
        Query query = db.collection("stockColl");
        transListRegistration = query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value,
                                @Nullable FirebaseFirestoreException e) {
                if (e!= null){
                    return;
                }
                List<TransactionModel> transList = new ArrayList<>();
                for (QueryDocumentSnapshot doc : value){
                    String name = doc.getString("name");
                    String num = doc.getString("num");
                    long trans = doc.getLong("trans");
                    Log.i(TAG, "quantity is "+doc.getLong("quan"));
                    long quantity = doc.getLong("quan");
                    String date = doc.getString("date");
                    String remark = doc.getString("remark");


                    transList.add(new TransactionModel(name, num, date, quantity, remark, trans));
                }
                mTransCompleteList.setValue(transList);

            }
        });
    }
}
