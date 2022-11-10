package duynn.gotogether.ui_layer.fragment.profile;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import duynn.gotogether.R;
import duynn.gotogether.data_layer.model.model.Transport;
import duynn.gotogether.data_layer.model.model.TransportType;
import duynn.gotogether.data_layer.model.model.TransportWithoutOwner;
import duynn.gotogether.ui_layer.activity.publish_route.StopPlaceRecyclerViewAdapter;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TransportRVAdapder extends RecyclerView.Adapter<TransportRVAdapder.TransportViewHolder> {

    private StopPlaceRecyclerViewAdapter.ItemClickListener itemClickListener;
    private List<TransportWithoutOwner> listItem;

    @NonNull
    @Override
    public TransportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.transport_item, parent, false);
        return new TransportViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransportViewHolder holder, int position) {
        holder.name.setText(listItem.get(position).getName());
        holder.licensePlate.setText(listItem.get(position).getLicensePlate());
        TransportType type = listItem.get(position).getTransportType();
        switch (type){
            case BIKE:
                holder.type.setImageResource(R.drawable.electric_bike);
                break;
            case CAR:
                holder.type.setImageResource(R.drawable.directions_car);
                break;
            case MOTORCYCLE:
                holder.type.setImageResource(R.drawable.two_wheeler);
                break;
        }
    }

    @Override
    public int getItemCount() {
        if (listItem == null) {
            return 0;
        }
        return listItem.size();
    }

    public class TransportViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView name,licensePlate;
        ImageView type;

        public TransportViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.transport_name);
            licensePlate = itemView.findViewById(R.id.transport_number);
            type = itemView.findViewById(R.id.transport_type);
        }

        @Override
        public void onClick(View v) {
            itemClickListener.onItemClick(v, getAdapterPosition());
        }
    }
}
