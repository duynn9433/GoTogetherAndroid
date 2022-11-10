package duynn.gotogether.ui_layer.fragment.search;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Toast;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import duynn.gotogether.R;
import duynn.gotogether.data_layer.model.dto.request.SearchTripRequest;
import duynn.gotogether.data_layer.model.dto.response.GoongMaps.PlaceDetail.GoongPlaceDetailResult;
import duynn.gotogether.data_layer.model.dto.response.GoongMaps.PlaceDetail.Place;
import duynn.gotogether.data_layer.model.dto.response.SearchTripResponse;
import duynn.gotogether.data_layer.model.model.Trip;
import duynn.gotogether.databinding.FragmentProfileBinding;
import duynn.gotogether.databinding.FragmentSearchBinding;
import duynn.gotogether.domain_layer.common.Constants;
import duynn.gotogether.ui_layer.activity.get_place_goong.GetPlaceGoongActivity;
import duynn.gotogether.ui_layer.activity.publish_route.PublishActivity;
import duynn.gotogether.ui_layer.activity.search.SearchResultActivity;
import duynn.gotogether.ui_layer.fragment.profile.ProfileViewModel;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public class SearchFragment extends Fragment {

    private SearchViewModel mViewModel;
    private FragmentSearchBinding binding;
    private SearchViewModel searchViewModel;

    public static SearchFragment newInstance() {
        return new SearchFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentSearchBinding.inflate(inflater, container, false);
        searchViewModel = new ViewModelProvider(this).get(SearchViewModel.class);
        View view = binding.getRoot();
        initElement();
        observeData();
        return view;
    }

    private void observeData() {
        searchViewModel.getSearchTripRequest().observe(getViewLifecycleOwner(), searchTripRequest -> {
            if(searchTripRequest != null) {
                if(searchTripRequest.getStartPlace() != null){
                    binding.searchLeavingFromEditText.setText(searchTripRequest.getStartPlace().getName());
                }
                if(searchTripRequest.getEndPlace() != null){
                    binding.searchGoingToEditText.setText(searchTripRequest.getEndPlace().getName());
                }
                if(searchTripRequest.getStartTime() != null){
                    binding.searchDateTextView.setText(
                            new SimpleDateFormat("dd-MM-yyyy  HH:mm")
                                    .format(searchTripRequest.getStartTime().getTime()));
                }
                if(searchTripRequest.getNumOfSeat() != null){
                    binding.searchPeopleEditText.setText(searchTripRequest.getNumOfSeat() + "");
                }
            }
        });

        searchViewModel.getStatus().observe(getViewLifecycleOwner(), status -> {
            if(status != ""){
                if (Objects.equals(status, Constants.SUCCESS)) {
                    Toast.makeText(getContext(), "Tìm thành công", Toast.LENGTH_SHORT).show();
                    //TODO: chuyển sang màn hình kết quả tìm kiếm
//                    Intent intent = new Intent(getContext(), SearchResultActivity.class);
                    SearchTripResponse response = searchViewModel.getSearchTripResponse().getValue();
                    if(response == null
                            || response.getTrips() == null || response.getTrips().size() == 0 ){
                        Toast.makeText(getContext(), "Không tìm thấy", Toast.LENGTH_SHORT).show();
                    } else if (response.getStatus().equals(Constants.FAIL)) {
                        Toast.makeText(getContext(), response.getMessage(), Toast.LENGTH_SHORT).show();
                    } else{
                        Intent intent = new Intent(getContext(), SearchResultActivity.class);
                        //data
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(Constants.Trips, new ArrayList<>(response.getTrips()));
                        intent.putExtra(Constants.Bundle, bundle);
                        //go to search result activity
                        startActivity(intent);
                    }

                } else if (Objects.equals(status, Constants.FAIL)) {
                    Toast.makeText(getContext(), "Không tìm thấy", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initElement() {
        //init search place
        binding.searchLeavingFromEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), GetPlaceGoongActivity.class);
                startActivityForResult(intent, Constants.GET_START_PLACE_FROM_SEARCH_REQUEST_CODE);
            }
        });
        binding.searchGoingToEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), GetPlaceGoongActivity.class);
                startActivityForResult(intent, Constants.GET_END_PLACE_FROM_SEARCH_REQUEST_CODE);
            }
        });
        //start date
        binding.searchDateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                DatePickerDialog.OnDateSetListener dateSetListener = (dateView, year, month, dayOfMonth) -> {
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, month);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    TimePickerDialog.OnTimeSetListener timeSetListener = (timeView, hourOfDay, minute) -> {
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);
                        //set new time
                        SearchTripRequest searchTripRequest = searchViewModel.getSearchTripRequest().getValue();
                        searchTripRequest.setStartTime(calendar);
                        searchViewModel.getSearchTripRequest().setValue(searchTripRequest);
                    };
                    new TimePickerDialog(getContext(), timeSetListener,
                            calendar.get(Calendar.HOUR_OF_DAY),
                            calendar.get(Calendar.MINUTE),
                            true).show();
                };
                new DatePickerDialog(getContext(), dateSetListener,
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        //num of people
        binding.searchPeopleEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    String s = binding.searchPeopleEditText.getText().toString();
                    if (s.isEmpty()) {
//                    binding.emptySeat.setError("Không được để trống");
                    } else {
                        try {
                            SearchTripRequest searchTripRequest = searchViewModel.getSearchTripRequest().getValue();
                            assert searchTripRequest != null;
                            searchTripRequest.setNumOfSeat(Integer.parseInt(s));
                            searchViewModel.getSearchTripRequest().setValue(searchTripRequest);
                        }catch (NumberFormatException e){
                            binding.searchPeopleEditText.setError("Số ghế trống phải là số");
                        }
                    }
                }
            }
        });
        //search button
        binding.searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.searchPeopleEditText.clearFocus();
                searchViewModel.searchTrip(getContext());
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Constants.GET_START_PLACE_FROM_SEARCH_REQUEST_CODE) {
                Bundle bundle = data.getBundleExtra(Constants.Bundle);
                GoongPlaceDetailResult goongPlaceDetailResult = (GoongPlaceDetailResult) bundle.getSerializable(Constants.GOONG_PLACE_DETAIL_RESULT);
                Place place = goongPlaceDetailResult.getResult();
                SearchTripRequest searchTripRequest = searchViewModel.getSearchTripRequest().getValue();
                assert searchTripRequest != null;
                searchTripRequest.setStartPlace(place);
                searchViewModel.getSearchTripRequest().setValue(searchTripRequest);
            } else if (requestCode == Constants.GET_END_PLACE_FROM_SEARCH_REQUEST_CODE) {
                Bundle bundle = data.getBundleExtra(Constants.Bundle);
                GoongPlaceDetailResult goongPlaceDetailResult = (GoongPlaceDetailResult) bundle.getSerializable(Constants.GOONG_PLACE_DETAIL_RESULT);
                Place place = goongPlaceDetailResult.getResult();
                SearchTripRequest searchTripRequest = searchViewModel.getSearchTripRequest().getValue();
                assert searchTripRequest != null;
                searchTripRequest.setEndPlace(place);
                searchViewModel.getSearchTripRequest().setValue(searchTripRequest);
            }
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(SearchViewModel.class);
        // TODO: Use the ViewModel
    }

}