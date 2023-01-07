package duynn.gotogether.ui_layer.activity.trip;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import duynn.gotogether.data_layer.model.model.ClientTrip;
import duynn.gotogether.databinding.PassengerWaitItemBinding;
import duynn.gotogether.domain_layer.CalendarConvertUseCase;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;


import java.util.List;

@Getter
@Setter
public class PassengerWaitRVAdapter extends RecyclerView.Adapter<PassengerWaitRVAdapter.PassengerWaitItem> {
    private List<ClientTrip> listClientTrip;
    private Context context;
    private OnItemClickListener onItemClickListener;

    public PassengerWaitRVAdapter(List<ClientTrip> listClientTrip, Context context) {
        this.listClientTrip = listClientTrip;
        this.context = context;
    }

    @NonNull
    @NotNull
    @Override
    public PassengerWaitItem onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        PassengerWaitItemBinding binding = PassengerWaitItemBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false);
        return new PassengerWaitItem(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull PassengerWaitItem holder, int position) {
        holder.bind(listClientTrip.get(position));
    }

    @Override
    public int getItemCount() {
        if (listClientTrip == null) {
            return 0;
        }
        return listClientTrip.size();
    }


    public class PassengerWaitItem extends RecyclerView.ViewHolder{
        PassengerWaitItemBinding binding;
        public PassengerWaitItem(@NonNull PassengerWaitItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            this.binding.accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    onItemClickListener.onAcceptClick(v,position);
                }
            });
            this.binding.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    onItemClickListener.onDeleteClick(v,position);
                }
            });
            this.binding.detailRating.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    onItemClickListener.onRatingDetail(v,position);
                }
            });
        }
        public void bind(ClientTrip clientTrip){
            if(clientTrip.getClient() != null){
                binding.passengerName.setText("Tên: "+clientTrip.getClient().getFullNameString());
                binding.ratingBar.setRating(clientTrip.getClient().getRate().floatValue());
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

    public interface OnItemClickListener{
        void onAcceptClick(View view, int position);
        void onDeleteClick(View view, int position);
        void onRatingDetail(View view, int position);
    }
}
