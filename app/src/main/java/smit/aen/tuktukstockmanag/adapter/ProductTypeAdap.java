package smit.aen.tuktukstockmanag.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import smit.aen.tuktukstockmanag.R;
import smit.aen.tuktukstockmanag.interfaces.TopicIFace;

public class ProductTypeAdap extends RecyclerView.Adapter<ProductTypeAdap.ViewHolder> {

    private static final String TAG = ProductTypeAdap.class.getSimpleName();
    private Context context;
    private TopicIFace mInterface;
    private List<String> typeList;

    public ProductTypeAdap(Context context, TopicIFace mInterface) {
        this.context = context;
        this.mInterface = mInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.c_product_type, parent, false);
        context = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txtName.setText(typeList.get(position));
    }

    @Override
    public int getItemCount() {
       if (typeList!= null){
           return typeList.size();
       }else {
           return 0;
       }
    }

    public void setTypeList(List<String> typeList) {
        this.typeList = typeList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView txtName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtName);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            String typeName = typeList.get(adapterPosition);
            mInterface.funTopicName(typeName);
        }
    }
}
