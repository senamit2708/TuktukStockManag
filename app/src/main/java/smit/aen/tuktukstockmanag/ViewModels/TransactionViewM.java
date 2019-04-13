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
    private MutableLiveData<List<TransactionModel>> mTransSelectedList;

    ListenerRegistration transListRegistration;
    ListenerRegistration transSelectedListRegis;




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

    public MutableLiveData<List<TransactionModel>> getSelectedTransList(long fromDate, long toDate, String prodSelected) {
//        if (mTransSelectedList!= null){
//            return mTransSelectedList;
//        }
        mTransSelectedList = new MutableLiveData<>();
        loadSelectedTransList(fromDate, toDate, prodSelected);
        return mTransSelectedList;
    }

    private void loadSelectedTransList(long fromDate, long toDate, String prodSelected) {
        Query query;
        if (prodSelected.equals("##AMIT##")){
             query = db.collection("stockColl")
                    .whereLessThanOrEqualTo("queryDate", toDate)
                    .whereGreaterThanOrEqualTo("queryDate", fromDate)
                    .orderBy("queryDate", Query.Direction.DESCENDING);
        }
        else {
             query = db.collection("stockColl")
                    .whereLessThanOrEqualTo("queryDate", toDate)
                    .whereGreaterThanOrEqualTo("queryDate", fromDate)
                    .whereEqualTo("num", prodSelected)
                    .orderBy("queryDate", Query.Direction.DESCENDING);
        }



        transSelectedListRegis = query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value,
                                @Nullable FirebaseFirestoreException e) {
                if (e!= null){
                    Log.i(TAG, "exception is "+e);
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
                mTransSelectedList.setValue(transList);

            }
        });
    }
}
