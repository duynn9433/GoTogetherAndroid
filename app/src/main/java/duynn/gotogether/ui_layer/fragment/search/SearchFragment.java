package duynn.gotogether.ui_layer.fragment.search;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.widget.Toast;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import duynn.gotogether.data_layer.model.dto.request.SearchTripRequest;
import duynn.gotogether.data_layer.model.dto.response.ListTripResponse;
import duynn.gotogether.data_layer.model.model.ClientTrip;
import duynn.gotogether.data_layer.model.model.Place;
import duynn.gotogether.data_layer.model.model.Trip;
import duynn.gotogether.databinding.FragmentSearchBinding;
import duynn.gotogether.domain_layer.common.Constants;
import duynn.gotogether.ui_layer.activity.get_place_goong.GetPlaceGoongActivity;
import duynn.gotogether.ui_layer.activity.search.SearchResultActivity;

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
        searchViewModel.getSearchClientTrip().observe(getViewLifecycleOwner(), clientTrip -> {
            if(clientTrip != null) {
                if(clientTrip.getPickUpPlace() != null){
                    binding.searchLeavingFromEditText.setText(clientTrip.getPickUpPlace().getName());
                }
                if(clientTrip.getDropOffPlace() != null){
                    binding.searchGoingToEditText.setText(clientTrip.getDropOffPlace().getName());
                }
                if(clientTrip.getPickUpTime() != null){
                    binding.searchDateTextView.setText(
                            new SimpleDateFormat("dd-MM-yyyy  HH:mm")
                                    .format(clientTrip.getPickUpTime().getTime()));
                }
                if(clientTrip.getNumOfPeople() != null){
                    binding.searchPeopleEditText.setText(clientTrip.getNumOfPeople() + "");
                }
            }
        });

        searchViewModel.getStatus().observe(getViewLifecycleOwner(), status -> {
            if(status != ""){
                if (Objects.equals(status, Constants.SUCCESS)) {
                    Toast.makeText(getContext(), "Tìm thành công", Toast.LENGTH_SHORT).show();
                    //TODO: chuyển sang màn hình kết quả tìm kiếm
//                    Intent intent = new Intent(getContext(), SearchResultActivity.class);
                    List<Trip> response = searchViewModel.getListTrip().getValue();
                    if(response == null || response.size() == 0 ){
                        Toast.makeText(getContext(), "Không tìm thấy", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent intent = new Intent(getContext(), SearchResultActivity.class);
                        //data
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(Constants.TRIPS, new ArrayList<>(response));
                        bundle.putSerializable(Constants.SEARCH_TRIP_REQUEST, searchViewModel.getSearchClientTrip().getValue());
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
                        ClientTrip searchTripRequest = searchViewModel.getSearchClientTrip().getValue();
                        searchTripRequest.setPickUpTime(calendar);
                        searchViewModel.getSearchClientTrip().setValue(searchTripRequest);
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
                            ClientTrip searchTripRequest = searchViewModel.getSearchClientTrip().getValue();
                            assert searchTripRequest != null;
                            searchTripRequest.setNumOfPeople(Integer.parseInt(s));
                            searchViewModel.getSearchClientTrip().setValue(searchTripRequest);
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
                Place place = (Place) bundle.getSerializable(Constants.PLACE);
                ClientTrip searchTripRequest = searchViewModel.getSearchClientTrip().getValue();
                assert searchTripRequest != null;
                searchTripRequest.setPickUpPlace(place);
                searchViewModel.getSearchClientTrip().setValue(searchTripRequest);
            } else if (requestCode == Constants.GET_END_PLACE_FROM_SEARCH_REQUEST_CODE) {
                Bundle bundle = data.getBundleExtra(Constants.Bundle);
                Place place = (Place) bundle.getSerializable(Constants.PLACE);
                ClientTrip searchTripRequest = searchViewModel.getSearchClientTrip().getValue();
                assert searchTripRequest != null;
                searchTripRequest.setDropOffPlace(place);
                searchViewModel.getSearchClientTrip().setValue(searchTripRequest);
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