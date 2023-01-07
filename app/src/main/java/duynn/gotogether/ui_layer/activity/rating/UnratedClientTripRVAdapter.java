package duynn.gotogether.ui_layer.activity.rating;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import duynn.gotogether.data_layer.model.model.ClientTrip;
import duynn.gotogether.data_layer.model.model.Trip;
import duynn.gotogether.data_layer.repository.SessionManager;
import duynn.gotogether.databinding.UnratedClientTripItemBinding;
import duynn.gotogether.databinding.WaitingTripItemBinding;
import duynn.gotogether.domain_layer.CalendarConvertUseCase;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UnratedClientTripRVAdapter extends RecyclerView.Adapter<UnratedClientTripRVAdapter.UnratedClientTripViewHolder> {
    private OnItemClickListener onItemClickListener;
    private List<ClientTrip> clientTripList;
    private Context context;
    private SessionManager sessionManager;
    private Long myId;

    public UnratedClientTripRVAdapter(List<ClientTrip> clientTripList, Context context) {
        this.clientTripList = clientTripList;
        this.context = context;
        sessionManager = SessionManager.getInstance(context);
        myId = sessionManager.getClient().getId();
    }

    @NonNull
    @Override
    public UnratedClientTripViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        UnratedClientTripItemBinding binding = UnratedClientTripItemBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false);
        return new UnratedClientTripViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull UnratedClientTripViewHolder holder, int position) {
        holder.bind(clientTripList.get(position));
    }

    @Override
    public int getItemCount() {
        if(clientTripList == null){
            return 0;
        }
        return clientTripList.size();
    }

    public class UnratedClientTripViewHolder extends RecyclerView.ViewHolder{
        UnratedClientTripItemBinding binding;
        public UnratedClientTripViewHolder(@NonNull UnratedClientTripItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            this.binding.unratedClientTripItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    onItemClickListener.onItemClick(v,position);
                }
            });
        }
        public void bind(ClientTrip clientTrip){
            if(clientTrip!=null){
                if(clientTrip.getTrip()!=null){
                    binding.tripNumber.setText("Chuyến số: "+clientTrip.getTrip().getId());
                }
                if(clientTrip.getClient()!=null){
                    //myid == passenger id --> receiver is driver
                    if(myId.equals(clientTrip.getClient().getId())){
                        binding.receiver.setText("Người nhận: "+clientTrip.getTrip().getDriver().getFullNameString());
                    }else{
                        binding.receiver.setText("Người nhận: "+clientTrip.getClient().getFullNameString());
                    }
                }
                if(clientTrip.getPickUpPlace()!=null){
                    binding.from.setText("Điểm đón: "+clientTrip.getPickUpPlace().getName());
                }
                if(clientTrip.getDropOffPlace()!=null){
                    binding.to.setText("Điểm trả: "+clientTrip.getDropOffPlace().getName());
                }
            }
        }
    }

    public interface OnItemClickListener{
        void onItemClick(View view, int position);
    }
}
