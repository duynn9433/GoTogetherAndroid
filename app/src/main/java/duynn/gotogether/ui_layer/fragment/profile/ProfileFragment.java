package duynn.gotogether.ui_layer.fragment.profile;

import android.app.Activity;
import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.LinearLayoutManager;
import duynn.gotogether.R;
import duynn.gotogether.data_layer.model.model.Transport;
import duynn.gotogether.databinding.FragmentProfileBinding;
import duynn.gotogether.domain_layer.common.Constants;
import duynn.gotogether.ui_layer.activity.profile.AddTransportActivity;

import static android.app.Activity.RESULT_OK;

public class ProfileFragment extends Fragment {

    private ProfileViewModel mViewModel;
    private FragmentProfileBinding binding;

    private TransportRVAdapder transportRVAdapder;
    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        mViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        View view = binding.getRoot();
        initRecyclerView();
        observeData();
        initAddTransport();
        return view;
    }

    private void initAddTransport() {
        binding.btnAddVehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddTransportActivity.class);
                startActivityForResult(intent, Constants.ADD_TRANSPORT_REQUEST_CODE);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if(requestCode == Constants.ADD_TRANSPORT_REQUEST_CODE){
                Bundle bundle = data.getBundleExtra(Constants.Bundle);
                Transport transport = (Transport) bundle.getSerializable(Constants.TRANSPORT);

                mViewModel.addTransport(transport);

            }
        }
    }

    private void observeData() {
        mViewModel.getClient().observe(getViewLifecycleOwner(), client -> {
            transportRVAdapder.setListItem(client.getTransports());
            binding.name.setText(client.getFullNameString());
            binding.phone.setText(client.getContactInfomation().getPhoneNumber());
            binding.email.setText(client.getContactInfomation().getEmail());
//            binding.avatar
        });
    }

    private void initRecyclerView() {
        binding.rvTransport.setLayoutManager(new LinearLayoutManager(getContext()));
        transportRVAdapder = new TransportRVAdapder();
        binding.rvTransport.setAdapter(transportRVAdapder);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        // TODO: Use the ViewModel
//        transportRVAdapder.setListItem(mViewModel.getUser().);
    }

}