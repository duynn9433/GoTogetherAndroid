package duynn.gotogether.ui_layer.activity.profile;

import android.content.Intent;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.databinding.DataBindingUtil;
import duynn.gotogether.R;
import duynn.gotogether.data_layer.model.model.Transport;
import duynn.gotogether.data_layer.model.model.TransportType;
import duynn.gotogether.databinding.ActivityAddTransportBinding;
import duynn.gotogether.domain_layer.common.Constants;

import java.util.Arrays;

public class AddTransportActivity extends AppCompatActivity {
    private static final String TAG = AddTransportActivity.class.getSimpleName();
    private ActivityAddTransportBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddTransportBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initAddButton();
        initCancelButton();

    }

    private void initCancelButton() {
        binding.btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                setResult(RESULT_CANCELED, intent);
                finish();
            }
        });
    }

    private void initAddButton() {
        binding.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: transport create
                Transport transport = new Transport();
                transport.setName(binding.name.getText().toString());
                transport.setLicensePlate(binding.licensePlate.getText().toString());
                transport.setDescription(binding.description.getText().toString());
                String[] transportArray = getResources().getStringArray(R.array.transport_array);
                switch(binding.transportType.getSelectedItemPosition()) {
                    case 0:
                        transport.setTransportType(TransportType.CAR);
                        break;
                    case 1:
                        transport.setTransportType(TransportType.BUS);
                        break;
                    case 2:
                        transport.setTransportType(TransportType.BIKE);
                        break;
                    case 3:
                        transport.setTransportType(TransportType.MOTORCYCLE);
                        break;
                    default:
                        throw new IllegalStateException("Unexpected value: " + binding.transportType.getSelectedItem().toString());
                }

                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constants.TRANSPORT, transport);
                intent.putExtra(Constants.Bundle, bundle);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }
}