package smit.aen.tuktukstockmanag.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import smit.aen.tuktukstockmanag.Model.ProductM;
import smit.aen.tuktukstockmanag.R;
import smit.aen.tuktukstockmanag.fragments.ProductSearchListFrag;
import smit.aen.tuktukstockmanag.interfaces.ProductIface;

public class ProductSearchListAdap extends RecyclerView.Adapter<ProductSearchListAdap.ViewHolder> {

    private static final String TAG = ProductSearchListAdap.class.getSimpleName();
    private Context context;

    private List<ProductM> productList;
    private ProductIface mInterface;

    public ProductSearchListAdap(Context context, ProductIface mInterface) {
        this.context = context;
        this.mInterface = mInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.c_product_search_list, parent, false);
        context = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txtPName.setText(productList.get(position).getName());
        holder.txtPNumber.setText(productList.get(position).getNum());
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

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView txtPName;
        TextView txtPNumber;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtPName = itemView.findViewById(R.id.txtPName);
            txtPNumber = itemView.findViewById(R.id.txtPNumber);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            ProductM product = productList.get(adapterPosition);
            mInterface.funProduct(product);

        }
    }
}
