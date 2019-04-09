package smit.aen.tuktukstockmanag.ViewModels;

import android.app.Application;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import smit.aen.tuktukstockmanag.Model.ProductM;


public class ProductViewM extends AndroidViewModel {

    private static final String TAG = ProductViewM.class.getSimpleName();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private MutableLiveData<List<ProductM>> mProductLive;

    private MutableLiveData<ProductM> mProductSelectedLive= new MutableLiveData<>();

    public ProductViewM(@NonNull Application application) {
        super(application);
    }


    public MutableLiveData<List<ProductM>> getProductList() {
        if (mProductLive!= null){
            return mProductLive;
        }
        mProductLive = new MutableLiveData();
        getLiveProductList();
        return mProductLive;
    }

    private void getLiveProductList() {
        db.collection("ProdColl")
               .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            List<ProductM> productList = new ArrayList<>();
                            for (QueryDocumentSnapshot doc : task.getResult()){
                                ProductM product =doc.toObject(ProductM.class);
                                productList.add(product);
                                Log.i(TAG, "product number is "+product.getName());
                            }
                            mProductLive.setValue(productList);
                        }else {
                            //nothing to do here....
                        }
                    }
                });
    }

    public void setSelectedProduct(ProductM product) {
//       mProductSelectedLive = new MutableLiveData<>();
       mProductSelectedLive.setValue(product);

    }

    public MutableLiveData<ProductM> getSelectedProduct(){
        return mProductSelectedLive;
    }
}
