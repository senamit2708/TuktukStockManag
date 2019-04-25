package smit.aen.tuktukstockmanag.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import smit.aen.tuktukstockmanag.Model.TopicMain;
import smit.aen.tuktukstockmanag.R;
import smit.aen.tuktukstockmanag.interfaces.TopicIFace;

public class TestOneAdap extends RecyclerView.Adapter<TestOneAdap.ViewHolder> {

    private Context context;
    private TopicIFace mInterface;
    private List<TopicMain> topicName;

    public TestOneAdap(Context context, TopicIFace mInterface) {
        this.context = context;
        this.mInterface = mInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.c_test_one, parent,false );
        context = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txtTitle.setText(topicName.get(position).getTopicName());
        holder.txtImageView.setImageResource(topicName.get(position).getImage());
        if (position==0 || position==3){
            holder.txtTitle.setTextColor(context.getResources().getColor(R.color.colorPrimaryDark));
            holder.cardview.setCardBackgroundColor(context.getResources().getColor(R.color.colorGray));
        }
    }

    @Override
    public int getItemCount() {
        if (topicName!= null){
            return topicName.size();
        }else{
            return 0;
        }
    }

    public void setTopic(List<TopicMain> topicName) {
        this.topicName = topicName;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView txtTitle;
        ImageView txtImageView;
        CardView cardview;
        ConstraintLayout constraint;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txtRecyclerTitle);
            txtImageView = itemView.findViewById(R.id.txtImageView);
            cardview = itemView.findViewById(R.id.cardview);
            constraint = itemView.findViewById(R.id.constraint);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            String topic = topicName.get(adapterPosition).getTopicName();
            mInterface.funTopicName(topic);
        }
    }
}
