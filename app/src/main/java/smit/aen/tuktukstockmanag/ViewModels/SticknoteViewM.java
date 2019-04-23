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
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import smit.aen.tuktukstockmanag.Model.StickModel;

public class SticknoteViewM extends AndroidViewModel {

    private static final String TAG = SticknoteViewM.class.getSimpleName();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private MutableLiveData<List<StickModel>> mStickLive;

    private ListenerRegistration mListenerStickList;


    public SticknoteViewM(@NonNull Application application) {
        super(application);
    }

    public LiveData<List<StickModel>> getStickNotes(String mUId) {
        if (mStickLive!= null){
            return mStickLive;
        }
        mStickLive = new MutableLiveData<>();
        loadSticknoteList(mUId);
        return mStickLive;
    }

    private void loadSticknoteList(String mUId) {
     mListenerStickList =    db.collection("UserColl")
                .document(mUId)
                .collection("StickColl")
             .orderBy("date", Query.Direction.DESCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e!= null){
                            Log.w(TAG, "listen failed "+e);
                            return;
                        }
                        List<StickModel> stickyList = new ArrayList<>();
                        for (QueryDocumentSnapshot doc : value){
                            String docName = doc.getId();
                            String note = doc.getString("note");
                            long code = doc.getLong("code");

                            stickyList.add(new StickModel(note, code, docName));
                        }
                        mStickLive.setValue(stickyList);
                    }
                });
    }
}
