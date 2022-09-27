package duynn.gotogether.ui_layer.activity.get_place_goong;

import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import duynn.gotogether.R;
import duynn.gotogether.data_layer.model.dto.response.GoongMaps.PlaceAutocomple.Prediction;

public class GetPlaceGoongAdapter extends RecyclerView.Adapter<GetPlaceGoongAdapter.PlacePredictionViewHolder> {

    private List<Prediction> listItem = new ArrayList<>();
    private ItemClickListener itemClickListener;

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public List<Prediction> getListItem() {
        return listItem;
    }

    public void setListItem(List<Prediction> listItem) {
        this.listItem = listItem;
    }

    @NonNull
    @Override
    public PlacePredictionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.place_autocomplete_item, parent, false);
        return new PlacePredictionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlacePredictionViewHolder holder, int position) {
        holder.primaryText.setText(listItem.get(position).getStructuredFormatting().getMainText());
        holder.description.setText(listItem.get(position).getDescription());
    }

    @Override
    public int getItemCount() {
        if(listItem == null){
            return 0;
        }
        return listItem.size();
    }

    public class PlacePredictionViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView primaryText, description;
        LinearLayout autocompleteItem;

        public PlacePredictionViewHolder(@NonNull View itemView) {
            super(itemView);
            primaryText = itemView.findViewById(R.id.primary_text);
            description = itemView.findViewById(R.id.description);
            autocompleteItem = itemView.findViewById(R.id.autocomplete_layout);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(itemClickListener != null){
                itemClickListener.onItemClick(v, getAdapterPosition());
            }
        }
    }

    public interface ItemClickListener {
        void onItemClick(View v, int position);
    }
}
