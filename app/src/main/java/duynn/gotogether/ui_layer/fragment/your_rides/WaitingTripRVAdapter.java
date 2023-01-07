package duynn.gotogether.ui_layer.fragment.your_rides;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import duynn.gotogether.data_layer.model.model.Trip;
import duynn.gotogether.databinding.WaitingTripItemBinding;
import duynn.gotogether.domain_layer.CalendarConvertUseCase;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Getter
@Setter
public class WaitingTripRVAdapter extends RecyclerView.Adapter<WaitingTripRVAdapter.WaitingTripViewHolder> {
    private OnItemClickListener onItemClickListener;
    private List<Trip> listTrip;
    private Context context;

    public WaitingTripRVAdapter(List<Trip> listTrip, Context context) {
        this.listTrip = listTrip;
        this.context = context;
    }

    @NonNull
    @Override
    public WaitingTripViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        WaitingTripItemBinding binding = WaitingTripItemBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false);
        return new WaitingTripViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull WaitingTripViewHolder holder, int position) {
        holder.bind(listTrip.get(position));
    }

    @Override
    public int getItemCount() {
        if(listTrip == null){
            return 0;
        }
        return listTrip.size();
    }

    public class WaitingTripViewHolder extends RecyclerView.ViewHolder{
        WaitingTripItemBinding binding;
        public WaitingTripViewHolder(@NonNull WaitingTripItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            this.binding.waitingCall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    onItemClickListener.onCallClick(v,position);
                }
            });
            this.binding.waitingMessage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    onItemClickListener.onMessageClick(v,position);
                }
            });
            this.binding.waitingDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    onItemClickListener.onDeleteClick(v,position);
                }
            });
        }
        public void bind(Trip trip){
            if(trip!=null){
                binding.waitingItemTripName.setText("Chuyến đi số "+trip.getId());
                if(trip.getDriver()!=null){
                    binding.waitingItemDriverName.setText(trip.getDriver().getFullNameString());
                    binding.waitingItemRatingBar.setRating(trip.getDriver().getRate().floatValue());
                }
                if(trip.getStartPlace()!=null){
                    binding.waitingItemFrom.setText(trip.getStartPlace().getName());
                }
                if(trip.getEndPlace()!=null){
                    binding.waitingItemTo.setText(trip.getEndPlace().getName());
                }
                if(trip.getStartTime()!=null){
                    binding.waitingItemTime.setText(CalendarConvertUseCase.fromCalendarToString(trip.getStartTime()));
                }
                if(trip.getPricePerKm()!=null){
                    binding.waitingItemPrice.setText(trip.getPricePerKm().toString());
                }
            }
        }
    }

    public interface OnItemClickListener{
        void onCallClick(View view, int position);
        void onMessageClick(View view, int position);
        void onDeleteClick(View view, int position);
    }
}
