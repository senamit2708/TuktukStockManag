package smit.aen.tuktukstockmanag.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import smit.aen.tuktukstockmanag.Model.ProductM;

public class ProductListByTypeAdap extends RecyclerView.Adapter<ProductListByTypeAdap.ViewHolder> {

    private static final String TAG = ProductListByTypeAdap.class.getSimpleName();
    private Context context;
    private List<ProductM> productList;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        if (productList!= null){
            return productList.size();
        }else {
            return 0;
        }
    }

    public void setProductList(List<ProductM> productList) {
        this.productList = productList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
