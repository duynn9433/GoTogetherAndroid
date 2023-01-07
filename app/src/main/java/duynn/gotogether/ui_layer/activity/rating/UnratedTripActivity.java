package duynn.gotogether.ui_layer.activity.rating;

import android.content.Intent;
import android.os.Handler;
import android.view.View;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import duynn.gotogether.R;
import duynn.gotogether.databinding.ActivityUnratedTripBinding;
import duynn.gotogether.domain_layer.common.Constants;

import java.util.ArrayList;

public class UnratedTripActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{
    private static final String TAG = UnratedTripActivity.class.getSimpleName();
    private UnratedTripViewModel viewModel;
    private ActivityUnratedTripBinding binding;
    private UnratedClientTripRVAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUnratedTripBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        viewModel = new ViewModelProvider(this).get(UnratedTripViewModel.class);

        initRecyclerView();
        observeData();
        binding.unratedTripSwipeRefresh.setOnRefreshListener(this);
        viewModel.getUnratedClientTrip();

    }

    private void initRecyclerView() {
        adapter = new UnratedClientTripRVAdapter(new ArrayList<>(), this);
        binding.unratedClientTripList.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        binding.unratedClientTripList.setLayoutManager(layoutManager);
        adapter.setOnItemClickListener(new UnratedClientTripRVAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                /**
                 * call activity rating
                 * parameter:
                 *  clienttrip
                 *  position
                 * */
                Intent intent = new Intent(UnratedTripActivity.this, RatingActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constants.CLIENT_TRIP, adapter.getClientTripList().get(position));
                bundle.putInt(Constants.POSITION, position);
                intent.putExtra(Constants.Bundle,bundle);
                startActivityForResult(intent, Constants.RATING_REQUEST_CODE);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == Constants.RATING_REQUEST_CODE && resultCode == RESULT_OK){
            Bundle bundle = data.getBundleExtra(Constants.Bundle);
            int position = bundle.getInt(Constants.POSITION);
            adapter.getClientTripList().remove(position);
            adapter.notifyItemRemoved(position);
        }
    }

    private void observeData() {
        viewModel.getClientTripList().observe(this, clientTrips -> {
            if (clientTrips != null && clientTrips.size() > 0) {
                adapter.setClientTripList(clientTrips);
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onRefresh() {
        viewModel.getUnratedClientTrip();
        Handler handler = new Handler();
        handler.postDelayed(() -> binding.unratedTripSwipeRefresh.setRefreshing(false), 2000);
    }
}