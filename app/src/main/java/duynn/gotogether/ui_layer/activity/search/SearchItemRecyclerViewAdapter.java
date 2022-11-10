package duynn.gotogether.ui_layer.activity.search;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import duynn.gotogether.data_layer.model.model.Trip;
import duynn.gotogether.databinding.SearchTripItemBinding;
import duynn.gotogether.domain_layer.CalendarConvertUseCase;
import duynn.gotogether.ui_layer.activity.get_place_goong.GetPlaceGoongAdapter;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SearchItemRecyclerViewAdapter extends RecyclerView.Adapter<SearchItemRecyclerViewAdapter.SearchItemViewHolder> {
    private List<Trip> listTrip;
    private Context context;
    private OnItemClickListener itemClickListener;

    public SearchItemRecyclerViewAdapter(List<Trip> listTrip, Context context) {
        this.listTrip = listTrip;
        this.context = context;
    }

    @NonNull
    @Override
    public SearchItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        SearchTripItemBinding binding = SearchTripItemBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false);
        return new SearchItemViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchItemViewHolder holder, int position) {
        holder.bind(listTrip.get(position));
    }

    @Override
    public int getItemCount() {
        if (listTrip == null) {
            return 0;
        }
        return listTrip.size();
    }

    public class SearchItemViewHolder extends RecyclerView.ViewHolder{
        SearchTripItemBinding binding;

        public SearchItemViewHolder(@NonNull SearchTripItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.searchItemLl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    itemClickListener.onItemClick(v, position);
                }
            });
            binding.searchItemRegister.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    itemClickListener.onItemRegisterClick(v, position);
                }
            });
        }

        public void bind(Trip trip) {
            binding.searchItemDriverName.setText(trip.getDriver().getFullNameString());
            binding.searchItemFrom.setText(trip.getStartPlace().getName());
            binding.searchItemTo.setText(trip.getEndPlace().getName());
            binding.searchItemTime.setText("Thời gian: "+ CalendarConvertUseCase.fromCalendarToString(trip.getStartTime()));
            binding.searchItemPrice.setText("Giá: "+trip.getPricePerKm().toString()+" VND/km");
        }
    }

    public interface OnItemClickListener{
        void onItemClick(View view, int position);
        void onItemRegisterClick(View view, int position);
    }

}
