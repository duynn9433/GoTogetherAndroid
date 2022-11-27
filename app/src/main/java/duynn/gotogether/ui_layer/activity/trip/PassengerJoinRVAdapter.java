package duynn.gotogether.ui_layer.activity.trip;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import duynn.gotogether.data_layer.model.model.ClientTrip;
import duynn.gotogether.data_layer.model.model.Trip;
import duynn.gotogether.databinding.PassengerJoinItemBinding;
import duynn.gotogether.databinding.SearchTripItemBinding;
import duynn.gotogether.domain_layer.CalendarConvertUseCase;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Getter
@Setter
public class PassengerJoinRVAdapter extends RecyclerView.Adapter<PassengerJoinRVAdapter.PassengerJoinItem> {
    private List<ClientTrip> listClientTrip;
    private Context context;

    public PassengerJoinRVAdapter(List<ClientTrip> listClientTrip, Context context) {
        this.listClientTrip = listClientTrip;
        this.context = context;
    }

    @NonNull
    @NotNull
    @Override
    public PassengerJoinItem onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        PassengerJoinItemBinding binding = PassengerJoinItemBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false);
        return new PassengerJoinItem(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull PassengerJoinItem holder, int position) {
        holder.bind(listClientTrip.get(position));
    }

    @Override
    public int getItemCount() {
        if (listClientTrip == null) {
            return 0;
        }
        return listClientTrip.size();
    }

    public class PassengerJoinItem extends RecyclerView.ViewHolder{
        PassengerJoinItemBinding binding;
        public PassengerJoinItem(@NonNull @NotNull PassengerJoinItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
        public void bind(ClientTrip clientTrip){
            if(clientTrip.getClient() != null){
                binding.passengerName.setText("Tên: "+clientTrip.getClient().getFullNameString());
            }
            if(clientTrip.getPickUpPlace() != null){
                binding.from.setText("Điểm đón: "+clientTrip.getPickUpPlace().getName());
            }
            if(clientTrip.getDropOffPlace() != null){
                binding.to.setText("Điểm trả: "+clientTrip.getDropOffPlace().getName());
            }
            if(clientTrip.getPricePerKmForOnePeople() != null){
                binding.price.setText("Giá: "+clientTrip.getPricePerKmForOnePeople() + " VND");
            }
            if(clientTrip.getPickUpTime() != null){
                binding.time.setText("Thời gian: "+CalendarConvertUseCase.fromCalendarToString(clientTrip.getPickUpTime()));
            }
            if(clientTrip.getNumOfPeople() != null){
                binding.numOfSeat.setText("Số người: "+clientTrip.getNumOfPeople());
            }
        }
    }

}
