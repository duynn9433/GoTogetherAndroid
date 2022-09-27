package duynn.gotogether.ui_layer.activity.publish_route;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import duynn.gotogether.R;
import duynn.gotogether.data_layer.model.dto.response.GoongMaps.PlaceDetail.Place;

public class StopPlaceRecyclerViewAdapter extends RecyclerView.Adapter<StopPlaceRecyclerViewAdapter.StopPlaceViewHolder> {

    private ItemClickListener itemClickListener;
    private List<Place> listItem;

    public ItemClickListener getItemClickListener() {
        return itemClickListener;
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public List<Place> getListItem() {
        return listItem;
    }

    public void setListItem(List<Place> listItem) {
        this.listItem = listItem;
    }

    @NonNull
    @Override
    public StopPlaceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.stop_place_item, parent, false);
        return new StopPlaceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StopPlaceViewHolder holder, int position) {
        holder.title.setText(listItem.get(position).getName());
        holder.description.setText(listItem.get(position).getFormattedAddress());
    }

    @Override
    public int getItemCount() {
        if(listItem == null){
            return 0;
        }
        return listItem.size();
    }

    public class StopPlaceViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView title, description;
        ImageView delete;

        public StopPlaceViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            description = itemView.findViewById(R.id.description);
            delete = itemView.findViewById(R.id.delete_btn);
            delete.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            itemClickListener.onItemClick(v, getAdapterPosition());
        }
    }

    public interface ItemClickListener {
        void onItemClick(View v, int position);
    }
}
