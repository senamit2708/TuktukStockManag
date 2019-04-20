package smit.aen.tuktukstockmanag.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import smit.aen.tuktukstockmanag.Model.TransactionModel;
import smit.aen.tuktukstockmanag.R;

public class TransactionListAdapter extends RecyclerView.Adapter<TransactionListAdapter.ViewHolder> {

    private static final String TAG = TransactionListAdapter.class.getSimpleName();
    private Context context;

    private List<TransactionModel> transList;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.c_transaction_list, parent, false);
        context = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txtPName.setText(transList.get(position).getName());
        holder.txtPNumber.setText(transList.get(position).getNum());
        holder.txtDate.setText(transList.get(position).getDate());
        holder.txtQuantity.setText("Quantity: "+String.valueOf(transList.get(position).getQuan()));
        if (transList.get(position).getTrans()==1){
            holder.txtType.setText("IN");
            holder.cardView.setCardBackgroundColor(Color.BLUE);
            holder.txtQuantity.setTextColor(Color.BLUE);
        }else {
            holder.txtType.setText("OUT");
            holder.cardView.setCardBackgroundColor(Color.GREEN);
            holder.txtQuantity.setTextColor(Color.GREEN);

        }

    }

    @Override
    public int getItemCount() {
        if (transList!= null){
            return transList.size();
        }else {
            return 0;
        }
    }

    public void setTransList(List<TransactionModel> transList) {
        this.transList = transList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView txtPName;
        TextView txtQuantity;
        TextView txtDate;
        TextView txtType;
        TextView txtPNumber;
        CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtDate = itemView.findViewById(R.id.txtDate);
            txtPName = itemView.findViewById(R.id.txtPName);
            txtPNumber = itemView.findViewById(R.id.txtPNumber);
            txtQuantity = itemView.findViewById(R.id.txtQuantity);
            txtType = itemView.findViewById(R.id.txtType);
            cardView = itemView.findViewById(R.id.cardView);

        }
    }
}
