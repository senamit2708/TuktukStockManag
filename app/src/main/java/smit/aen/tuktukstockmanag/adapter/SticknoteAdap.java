package smit.aen.tuktukstockmanag.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import smit.aen.tuktukstockmanag.Model.StickModel;
import smit.aen.tuktukstockmanag.R;
import smit.aen.tuktukstockmanag.fragments.SticknoteFrag;
import smit.aen.tuktukstockmanag.interfaces.TopicIFace;

public class SticknoteAdap extends RecyclerView.Adapter<SticknoteAdap.ViewHolder> {

    private static final String TAG = SticknoteAdap.class.getSimpleName();
    private Context context;
    private TopicIFace mInterface;

    private List<StickModel> stickList;

    public SticknoteAdap(Context context, TopicIFace mInterface) {
        this.context = context;
        this.mInterface = mInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.c_sticknote, parent, false);
       context = parent.getContext();
       return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
       holder.txtNote.setText(stickList.get(position).getNote());
       holder.btnDelete.setBackgroundColor(Color.TRANSPARENT);
       long code = stickList.get(position).getCode();
       Log.i(TAG, "the code value is "+code);
       if (code==1){
           Log.i(TAG, "inside code 1");
           holder.cardview.setCardBackgroundColor(context.getResources().getColor(R.color.textColorThree));
       }else if (code==2){
           Log.i(TAG, "inside code 2");
           holder.cardview.setCardBackgroundColor(context.getResources().getColor(R.color.textColorFour));
       }else {
           Log.i(TAG, "inside code 3");
           holder.cardview.setCardBackgroundColor(context.getResources().getColor(R.color.colorMainPage));
       }
    }

    @Override
    public int getItemCount() {
       if (stickList!= null){
           return stickList.size();
       }else{
           return 0;
       }
    }

    public void setStickList(List<StickModel> stickList) {
        Log.i(TAG,"the size of sticklist is "+stickList.size());
        this.stickList = stickList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView txtNote;
        CardView cardview;
        ImageButton btnDelete;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
          txtNote = itemView.findViewById(R.id.txtNote);
          cardview = itemView.findViewById(R.id.cardview);
          btnDelete = itemView.findViewById(R.id.btnDelete);

          btnDelete.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            int adapterPosition = getAdapterPosition();
            if (v==btnDelete){
                String docName = stickList.get(adapterPosition).getDocName();
                mInterface.funTopicName(docName);
            }
        }
    }
}
