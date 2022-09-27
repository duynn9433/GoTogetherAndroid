package duynn.gotogether.ui_layer.fragment.publish;

import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import duynn.gotogether.R;
import duynn.gotogether.databinding.FragmentPublishBinding;
import duynn.gotogether.domain_layer.PermissionsUseCase;
import duynn.gotogether.ui_layer.activity.get_place.GetPlaceActivity;
import duynn.gotogether.ui_layer.activity.publish_route.GetDirectionMapsActivity;
import pub.devrel.easypermissions.EasyPermissions;

public class PublishFragment extends Fragment implements EasyPermissions.PermissionCallbacks {

    private PublishViewModel publishViewModel;
    private FragmentPublishBinding binding;

    public static PublishFragment newInstance() {
        return new PublishFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentPublishBinding.inflate(inflater, container, false);
//        return inflater.inflate(R.layout.fragment_publish, container, false);
        return binding.getRoot();
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        publishViewModel = new ViewModelProvider(this).get(PublishViewModel.class);
        // TODO: Use the ViewModel
        binding.setViewModel(publishViewModel);
        //observeViewModel();

        binding.getRoot().findViewById(R.id.publish_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                publishViewModel.onClickPublish();
                //TODO: publish route
                Intent intent = new Intent(getActivity(), GetDirectionMapsActivity.class);
//                Intent intent = new Intent(getActivity(), GetPlaceActivity.class);
                if(PermissionsUseCase.hasLocationPermission(getActivity())) {
                    startActivity(intent);
                }else{
                    PermissionsUseCase.requestLocationPermission(getActivity());
                }
            }
        });
        binding.getRoot().findViewById(R.id.publish_button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                publishViewModel.onClickPublish();
                //TODO: publish route
//                Intent intent = new Intent(getActivity(), GetDirectionMapsActivity.class);
                Intent intent = new Intent(getActivity(), GetPlaceActivity.class);
                if(PermissionsUseCase.hasLocationPermission(getActivity())) {
                    startActivity(intent);
                }else{
                    PermissionsUseCase.requestLocationPermission(getActivity());
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        Intent intent = new Intent(getActivity(), GetDirectionMapsActivity.class);
        startActivity(intent);
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        PermissionsUseCase.requestLocationPermission(getActivity());
    }
}