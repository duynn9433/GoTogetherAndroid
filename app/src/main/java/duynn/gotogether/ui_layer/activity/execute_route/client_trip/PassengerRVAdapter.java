package duynn.gotogether.ui_layer.activity.execute_route.client_trip;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import duynn.gotogether.data_layer.model.model.ClientTrip;
import duynn.gotogether.databinding.PassengerItemBinding;
import duynn.gotogether.domain_layer.CalendarConvertUseCase;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.util.List;
@Getter
@Setter
public class PassengerRVAdapter extends RecyclerView.Adapter<PassengerRVAdapter.PassengerItem> {
    private OnItemClickListener onItemClickListener;
    private List<ClientTrip> listClientTrip;
    private Context context;

    public PassengerRVAdapter(List<ClientTrip> listClientTrip, Context context) {
        this.listClientTrip = listClientTrip;
        this.context = context;
    }

    @NonNull
    @Override
    public PassengerItem onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        PassengerItemBinding binding = PassengerItemBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false);
        return new PassengerItem(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull PassengerItem holder, int position) {
        holder.bind(listClientTrip.get(position));
    }

    @Override
    public int getItemCount() {
        if (listClientTrip == null) {
            return 0;
        }
        return listClientTrip.size();
    }


    public class PassengerItem extends RecyclerView.ViewHolder{
        PassengerItemBinding binding;
        public PassengerItem(@NonNull PassengerItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.passengerCall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onCallClick(v,getAdapterPosition());
                }
            });
            binding.passengerFinish.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onFinishClick(v,getAdapterPosition());
                }
            });
        }

        public void bind(ClientTrip clientTrip){
            if(clientTrip.getClient() != null){
                if(clientTrip.getClient().getFullname()!=null){
                    binding.passengerName.setText("Tên: "+clientTrip.getClient().getFullNameString());
                }
                if(clientTrip.getClient().getContactInfomation()!=null){
                    binding.passengerPhone.setText("Số điện thoại: "+clientTrip.getClient().getContactInfomation().getPhoneNumber());
                }
            }
            if(clientTrip.getPickUpPlace() != null){
                binding.searchItemFrom.setText(clientTrip.getPickUpPlace().getName());
            }
            if(clientTrip.getDropOffPlace() != null){
                binding.searchItemTo.setText(clientTrip.getDropOffPlace().getName());
            }
        }
    }

    public interface OnItemClickListener{
        void onFinishClick(View view, int position);
        void onCallClick(View view, int position);
    }
}
