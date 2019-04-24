package smit.aen.tuktukstockmanag.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import smit.aen.tuktukstockmanag.Model.ProductM;
import smit.aen.tuktukstockmanag.R;
import smit.aen.tuktukstockmanag.interfaces.ProEditIface;
import smit.aen.tuktukstockmanag.interfaces.ProductIface;

public class ProductListAdap extends RecyclerView.Adapter<ProductListAdap.ViewHolder> {

    private static final String TAG = ProductListAdap.class.getSimpleName();
    private Context context;
    private SharedPreferences mSharedPref;


    private List<ProductM> productList;
    private ProductIface mInterface;
    private ProEditIface mEditIface;
    private long uType = 0;

    public ProductListAdap(Context context, ProductIface mInterface,ProEditIface mEditIface, long uType) {
        this.context = context;
        this.mInterface = mInterface;
        this.mEditIface = mEditIface;
        this.uType = uType;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.c_product_list, parent, false);
        context = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txtQuantity.setText("Quantity: "+String.valueOf(productList.get(position).getQuan()));
        holder.txtPNumber.setText("Number: "+productList.get(position).getNum());
        holder.txtPName.setText(productList.get(position).getName());
        holder.txtSPrice.setText("Sell Price: "+String.valueOf(productList.get(position).getsPrice()));
        if (uType==10){
            holder.txtBPrice.setText("Buy Price: "+String.valueOf(productList.get(position).getbPrice()));
        }
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

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView txtPName;
        TextView txtPNumber;
        TextView txtBPrice;
        TextView txtSPrice;
        TextView txtQuantity;
        ImageButton btnDel;
        ImageButton btnEdit;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtBPrice = itemView.findViewById(R.id.txtBPrice);
            txtPName = itemView.findViewById(R.id.txtPName);
            txtPNumber = itemView.findViewById(R.id.txtPNumber);
            txtSPrice = itemView.findViewById(R.id.txtSPrice);
            txtQuantity = itemView.findViewById(R.id.txtQuantity);
            btnDel = itemView.findViewById(R.id.btnDelete);
            btnEdit = itemView.findViewById(R.id.btnEdit);

            itemView.setOnClickListener(this);
            btnDel.setOnClickListener(this);
            btnEdit.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            ProductM product = productList.get(adapterPosition);

            if (v==itemView){
                Log.i(TAG, "inside itemview ");
                mEditIface.funSearchPro(product);
            }
            if (v==btnDel){
                    mInterface.funProduct(product);
            }
            if (v==btnEdit){
                mEditIface.funEditPro(product);
            }
        }
    }
}
