package duynn.gotogether.ui_layer.activity.search;

import android.view.View;
import android.widget.AdapterView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import duynn.gotogether.R;
import duynn.gotogether.data_layer.model.model.Trip;
import duynn.gotogether.databinding.ActivitySearchResultBinding;
import duynn.gotogether.domain_layer.ToastUseCase;
import duynn.gotogether.domain_layer.common.Constants;
import duynn.gotogether.ui_layer.activity.get_place_goong.GetPlaceGoongAdapter;

import java.util.ArrayList;
import java.util.List;

public class SearchResultActivity extends AppCompatActivity {
    private static final String TAG = SearchResultActivity.class.getSimpleName();
    private ActivitySearchResultBinding binding;
    private SearchItemRecyclerViewAdapter searchItemRecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySearchResultBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initBackButton();
        initRecyclerView();
    }

    private void initRecyclerView() {
        Bundle bundle = getIntent().getBundleExtra(Constants.Bundle);
        if (bundle != null) {
            List<Trip> trips = (ArrayList<Trip>) bundle.getSerializable(Constants.Trips);
            searchItemRecyclerViewAdapter = new SearchItemRecyclerViewAdapter(trips, this);
            binding.searchResultRecyclerView.setAdapter(searchItemRecyclerViewAdapter);
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
            binding.searchResultRecyclerView.setLayoutManager(layoutManager);

            searchItemRecyclerViewAdapter.setItemClickListener(new SearchItemRecyclerViewAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    ToastUseCase.showLongToast(getApplicationContext(), "Click on item " + position);
                }

                @Override
                public void onItemRegisterClick(View view, int position) {
                    ToastUseCase.showLongToast(getApplicationContext(), "Click on register " + position);
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