package smit.aen.tuktukstockmanag.ViewModels;

import android.app.Application;
import android.util.Log;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class ProductTypeViewM extends AndroidViewModel {

    private static final String TAG = ProductTypeViewM.class.getSimpleName();

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private MutableLiveData<List<String>> mCategoryLiveD;
    private MutableLiveData<List<String>> mBrandLiveD;
    private MutableLiveData<List<String>> mSupplierLiveD;

    private MutableLiveData<String> mEnterCategory = new MutableLiveData<>();
    private MutableLiveData<String> mEnterBrand = new MutableLiveData<>();

    ListenerRegistration mCategoryListener;
    ListenerRegistration mBrandListener;
    ListenerRegistration mSupplierListener;



    public ProductTypeViewM(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<List<String>> getTypeList() {
        if (mCategoryLiveD!= null){
            return mCategoryLiveD;
        }
        mCategoryLiveD = new MutableLiveData<>();
//        loadCategoryList();
        loadCategory();
        return mCategoryLiveD;
    }

//    private void loadCategoryList() {
//        mCategoryListener =  db.collection("ProCatColl")
//                .document("ProcatDoc")
//                .collection("CatColl")
//                .addSnapshotListener(new EventListener<QuerySnapshot>() {
//                    @Override
//                    public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots,
//                                        @Nullable FirebaseFirestoreException e) {
//                        if (e!= null){
//                            Log.w(TAG, "listen failed "+e);
//                            return;
//                        }
//
//
//                    }
//                });
//    }

    private void loadCategory() {
        mCategoryListener = db.collection("ProCatColl")
                .document("categDoc")
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e!= null){
                            Log.w(TAG, "listen failed "+e);
                            return;
                        }
                        List<String> categoryList = new ArrayList<>();
                        categoryList = (List<String>) documentSnapshot.get("cat");
                        Log.i(TAG,"the size of category list is "+categoryList);
                        mCategoryLiveD.setValue(categoryList);
                    }
                });

    }

    public LiveData<List<String>> getBrandList() {
        if (mBrandLiveD!= null){
            return mBrandLiveD;
        }
        mBrandLiveD = new MutableLiveData<>();
        loadBrandList();
        return mBrandLiveD;
    }

    private void loadBrandList() {
        mBrandListener =  db.collection("ProCatColl")
                .document("brandDoc")
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot,
                                        @Nullable FirebaseFirestoreException e) {
                        if (e!= null){
                            Log.w(TAG, "listen failed "+e);
                            return;
                        }
                        List<String> brandList = new ArrayList<>();
                        brandList = (List<String>) documentSnapshot.get("brand");
                        Log.i(TAG,"the size of brand list is "+brandList);
                        mBrandLiveD.setValue(brandList);
                    }
                });
    }


    public void setEnterCategory(String topic) {
        mEnterCategory.setValue(topic);
    }

    public LiveData<String> getEnterCategory() {
        return mEnterCategory;
    }

    public void setEnterBrand(String topic) {
        mEnterBrand.setValue(topic);
    }
    public LiveData<String> getEnterBrand() {
        return mEnterBrand;
    }
}
