package duynn.gotogether.ui_layer.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import duynn.gotogether.R;
import duynn.gotogether.data_layer.model.model.TransportWithoutOwner;

import java.util.List;

public class TransportSpinnerAdapter extends BaseAdapter {
    private List<TransportWithoutOwner> transportList;
    private Context context;

    public List<TransportWithoutOwner> getTransportList() {
        return transportList;
    }

    public void setTransportList(List<TransportWithoutOwner> transportList) {
        this.transportList = transportList;
    }

    public TransportSpinnerAdapter(Context context) {
        this.context = context;
    }

    public TransportSpinnerAdapter(List<TransportWithoutOwner> transportList, Context context) {
        this.transportList = transportList;
        this.context = context;
    }

    @Override
    public int getCount() {
        if (transportList == null) {
            return 0;
        }
        return transportList.size();
    }

    @Override
    public Object getItem(int position) {
        if (transportList == null) {
            return null;
        }
        if(position < 0 || position >= transportList.size()) {
            return null;
        }
        return transportList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View item = LayoutInflater.from(context).inflate(R.layout.transport_item, parent, false);
        TextView transportName = item.findViewById(R.id.transport_name);
        TextView transportLicensePlate = item.findViewById(R.id.transport_number);
        ImageView transportImage = item.findViewById(R.id.transport_type);
        TransportWithoutOwner transport = transportList.get(position);
        transportName.setText(transport.getName());
        transportLicensePlate.setText(transport.getLicensePlate());
        switch (transport.getTransportType()) {
            case CAR:
                transportImage.setImageResource(R.drawable.directions_car);
                break;
            case MOTORCYCLE:
                transportImage.setImageResource(R.drawable.two_wheeler);
                break;
            case BIKE:
                transportImage.setImageResource(R.drawable.electric_bike);
                break;
            case BUS:
                transportImage.setImageResource(R.drawable.directions_car);
                break;
            case OTHER:
                transportImage.setImageResource(R.drawable.directions_car);
                break;
        }

        return item;
    }
}
