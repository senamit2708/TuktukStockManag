package smit.aen.tuktukstockmanag.ViewModels;

import android.app.Application;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import javax.annotation.Nullable;

import smit.aen.tuktukstockmanag.Model.ProductM;

public class ProductWithTypeViewM extends AndroidViewModel {

    private static final String TAG = ProductWithTypeViewM.class.getSimpleName();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private MutableLiveData<List<String>> mTypeList;
    private MutableLiveData<List<ProductM>> mProductList;

    private MutableLiveData<ProductM> mProductForEdit = new MutableLiveData<>();

    private ListenerRegistration prodListReg;


    public ProductWithTypeViewM(@NonNull Application application) {
        super(application);
    }


    public LiveData<List<String>> getTypeVariety(String type, int fragType) {
        Log.i(TAG, "inside getype variety");
        mTypeList = new MutableLiveData<>();
        loadTypeVariety(type, fragType);
        return mTypeList;
    }

    private void loadTypeVariety(String type, int fragType) {
        Log.i(TAG, "fragtype value is "+fragType);
        if (fragType==1){
            Log.i(TAG, "fragtype value is "+fragType);
            loadTypeBrand(type);
            return;
        }
        if (fragType==2){
            Log.i(TAG, "fragtype value is "+fragType);
            loadTypeCat(type);
           return;
        }


    }

    private void loadTypeCat(String type) {

        final DocumentReference docRefBrand =
                db.collection("ProCatColl")
                        .document("ProcatDoc")
                        .collection("BrandColl")
                        .document(type);

        docRefBrand.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()){
                            List<String> catList = new ArrayList<>();
                            catList = (List<String>) documentSnapshot.get("cat");
                            mTypeList.setValue(catList);
                        }
                        else {
                            mTypeList.setValue(null);
                        }
                    }
                });

    }

    private void loadTypeBrand(String type) {
        Log.i(TAG, "type name is "+type);

        final DocumentReference docRefCat =
                db.collection("ProCatColl")
                        .document("ProcatDoc")
                        .collection("CatColl")
                        .document(type);
        docRefCat.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()){
                            Log.i(TAG, "load type in if statement");
                            List<String> brandList = new ArrayList<>();
                            brandList = (List<String>) documentSnapshot.get("brand");
                            mTypeList.setValue(brandList);
                        }else {
                            Log.i(TAG, "load type brand in else");
                            mTypeList.setValue(null);
                        }

                    }
                });
    }

    public LiveData<List<ProductM>> getProductList(String type, String selectedItem, int fragType) {
        mProductList = new MutableLiveData<>();
        loadProductList(type, selectedItem, fragType);
        return mProductList;
    }

    private void loadProductList(String type, String selectedItem, int fragType) {
        Query query = null;

         if (fragType==1){
             if ( selectedItem.equals("allProduct")){
                 query = db.collection("ProdColl")
                         .whereEqualTo("cat", type).whereEqualTo("aval", true);
             }else {
                 query = db.collection("ProdColl")
                         .whereEqualTo("cat", type)
                         .whereEqualTo("brand", selectedItem).whereEqualTo("aval", true);
             }

        }
        if (fragType==2){
            if ( selectedItem.equals("allProduct")){
                query = db.collection("ProdColl")
                        .whereEqualTo("brand", type).whereEqualTo("aval", true);
            }else {
                query = db.collection("ProdColl")
                        .whereEqualTo("brand", type)
                        .whereEqualTo("cat", selectedItem).whereEqualTo("aval", true);
            }

        }

        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value,
                                @Nullable FirebaseFirestoreException e) {
                if (e!= null){
                    Log.w(TAG, "listen failed "+e);
                    return;
                }
                if (value.isEmpty()){
                    mProductList.setValue(null);
                }
                List<ProductM> productMList = new ArrayList<>();
                for (QueryDocumentSnapshot doc : value){
                    String name = doc.getString("name");
                    String num = doc.getString("num");
                    String des = doc.getString("des");
                    Log.i(TAG, "product number is "+num);
                    long quan = doc.getLong("quan");
                    double bPrice = doc.getDouble("bPrice");
                    double sPrice = doc.getDouble("sPrice");
                    String cat = doc.getString("cat");
                    String brand = doc.getString("brand");

                    productMList.add(new ProductM(name, num, bPrice, sPrice, des, quan, brand, cat));
                }
                mProductList.setValue(productMList);

            }
        });


//              addSnapshotListener(new EventListener<QuerySnapshot>() {
//            @Override
//            public void onEvent(@Nullable QuerySnapshot value,
//                                @Nullable FirebaseFirestoreException e) {
//                if (e!= null){
//                    Log.w(TAG, "listen failed "+e);
//                    return;
//                }
//                List<ProductM> productMList = new ArrayList<>();
//                for (QueryDocumentSnapshot doc : value){
//                    String name = doc.getString("name");
//                    String num = doc.getString("num");
//                    String des = doc.getString("des");
//                    long quan = doc.getLong("quan");
//                    double bPrice = doc.getDouble("bPrice");
//                    double sPrice = doc.getDouble("sPrice");
//
//                    productMList.add(new ProductM(name, num, bPrice, sPrice, des, quan));
//
//                }
//                mProductList.setValue(productMList);
//            }
//        });

    }



    public void setProductForEdit(ProductM product) {
        mProductForEdit.setValue(product);
    }

    public LiveData<ProductM> getProductForEdit() {
        return mProductForEdit;
    }
}
