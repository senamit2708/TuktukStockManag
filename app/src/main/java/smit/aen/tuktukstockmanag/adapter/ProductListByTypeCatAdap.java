package smit.aen.tuktukstockmanag.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import smit.aen.tuktukstockmanag.R;
import smit.aen.tuktukstockmanag.interfaces.TopicIFace;

public class ProductListByTypeCatAdap extends RecyclerView.Adapter<ProductListByTypeCatAdap.ViewHolder> {

    private static final String TAG = ProductListByTypeCatAdap.class.getSimpleName();
    private Context context;
    private int clickedItemPos=-1;
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
        if (clickedItemPos==position){
            holder.cardview.setCardBackgroundColor(context.getResources().getColor(R.color.colorYellowTab));
        }else {
            holder.cardview.setCardBackgroundColor(context.getResources().getColor(android.R.color.white));
        }
    }

    @Override
    public int getItemCount() {
        if (typeList!= null){
            return typeList.size();
        }else {
            clickedItemPos=-1;
            return 0;
        }
    }

    public void setType(List<String> typeList) {
        this.typeList = typeList;
        clickedItemPos=-1;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView txtType;
        CardView cardview;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtType = itemView.findViewById(R.id.txtType);
            cardview = itemView.findViewById(R.id.cardview);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            String topic = typeList.get(adapterPosition);
            mInterface.funTopicName(topic);
            clickedItemPos = adapterPosition;
            notifyDataSetChanged();

        }
    }
}
