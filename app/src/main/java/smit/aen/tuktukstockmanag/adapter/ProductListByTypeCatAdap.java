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

public class ProductListByTypeCatAdap extends RecyclerView.Adapter<ProductListByTypeCatAdap.ViewHolder> {

    private static final String TAG = ProductListByTypeCatAdap.class.getSimpleName();
    private Context context;
    private TopicIFace mInterface;
    private List<String> typeList;

    public ProductListByTypeCatAdap(Context context, TopicIFace mInterface) {
        this.context = context;
        this.mInterface = mInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.c_pro_type_cat, parent, false);
        context = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txtType.setText(typeList.get(position));
    }

    @Override
    public int getItemCount() {
        if (typeList!= null){
            return typeList.size();
        }else {
            return 0;
        }
    }

    public void setType(List<String> typeList) {
        this.typeList = typeList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView txtType;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtType = itemView.findViewById(R.id.txtType);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            String topic = typeList.get(adapterPosition);
            mInterface.funTopicName(topic);

        }
    }
}
