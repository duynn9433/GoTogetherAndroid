package duynn.gotogether.ui_layer.activity.search;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import duynn.gotogether.R;
import duynn.gotogether.data_layer.model.dto.request.SearchTripRequest;
import duynn.gotogether.data_layer.model.model.Client;
import duynn.gotogether.data_layer.model.model.ClientTrip;
import duynn.gotogether.data_layer.model.model.Trip;
import duynn.gotogether.data_layer.repository.SessionManager;
import duynn.gotogether.databinding.ActivitySearchResultBinding;
import duynn.gotogether.domain_layer.ToastUseCase;
import duynn.gotogether.domain_layer.common.Constants;
import duynn.gotogether.ui_layer.activity.trip.PassengerTripDetailActivity;

import java.util.ArrayList;
import java.util.List;

public class SearchResultActivity extends AppCompatActivity {
    private static final String TAG = SearchResultActivity.class.getSimpleName();
    private ActivitySearchResultBinding binding;
    private SearchItemRecyclerViewAdapter searchItemRecyclerViewAdapter;
    private SearchResultViewModel viewModel;
    private List<Trip> trips = new ArrayList<>();
    private ClientTrip searchTripRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySearchResultBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        viewModel = new ViewModelProvider(this).get(SearchResultViewModel.class);
        initBackButton();
        initRecyclerView();
        observerData();
    }


    private void observerData() {
        viewModel.status.observe(this, status -> {
            switch (status) {
                case Constants.SUCCESS:
                    ToastUseCase.showLongToast(this, "Đăng kí tham gia chuyến đi thành công");
                    finish();
                    break;
                case Constants.FAIL:
                    ToastUseCase.showLongToast(this, viewModel.message.getValue()+"");
                    break;
            }
        });
    }

    private void initRecyclerView() {
        Bundle bundle = getIntent().getBundleExtra(Constants.Bundle);
        if (bundle != null) {
            trips = (ArrayList<Trip>) bundle.getSerializable(Constants.TRIPS);
            searchTripRequest = (ClientTrip) bundle.getSerializable(Constants.SEARCH_TRIP_REQUEST);
            searchItemRecyclerViewAdapter = new SearchItemRecyclerViewAdapter(trips, this, viewModel.getEstimatedDistance(searchTripRequest));
            binding.searchResultRecyclerView.setAdapter(searchItemRecyclerViewAdapter);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
            binding.searchResultRecyclerView.setLayoutManager(layoutManager);

            searchItemRecyclerViewAdapter.setItemClickListener(new SearchItemRecyclerViewAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    ToastUseCase.showLongToast(getApplicationContext(), "Click on item " + position);
                    Intent intent = new Intent(getApplicationContext(), PassengerTripDetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(Constants.TRIP, trips.get(position));
                    intent.putExtra(Constants.Bundle, bundle);
                    startActivity(intent);
                }

                @Override
                public void onItemRegisterClick(View view, int position) {
//                    ToastUseCase.showLongToast(getApplicationContext(), "Click on register " + position);
                    //hien thong bao ...
                    // Use the Builder class for convenient dialog construction
                    AlertDialog.Builder builder = new AlertDialog.Builder(SearchResultActivity.this);
                    builder.setMessage(R.string.regiter_trip)
                            .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // regit
                                    Long tripId = trips.get(position).getId();
                                    SessionManager sessionManager = SessionManager.getInstance(getApplicationContext());
                                    Client client = sessionManager.getClient();
                                    viewModel.regitTrip(tripId, client.getId(), searchTripRequest);
                                }
                            })
                            .setNegativeButton(R.string.cancel, null);
                    // Create the AlertDialog object and show
                    AlertDialog dialog = builder.create();
                    dialog.show();

                }

                @Override
                public void onItemCallClick(View view, int position) {
                    Trip trip = searchItemRecyclerViewAdapter.getListTrip().get(position);
                    //TODO: call
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" +
                            trip.getDriver().getContactInfomation().getPhoneNumber()));
                    startActivity(intent);
                }

                @Override
                public void onItemMessageClick(View view, int position) {
                    //TODO: message
                    Trip trip = searchItemRecyclerViewAdapter.getListTrip().get(position);

                }
            });
        }
    }
    private void initBackButton() {
        binding.searchResultBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}