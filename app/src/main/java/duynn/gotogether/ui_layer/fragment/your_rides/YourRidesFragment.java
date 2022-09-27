package duynn.gotogether.ui_layer.fragment.your_rides;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import duynn.gotogether.R;

public class YourRidesFragment extends Fragment {

    private YourRidesViewModel mViewModel;

    public static YourRidesFragment newInstance() {
        return new YourRidesFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_your_rides, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(YourRidesViewModel.class);
        // TODO: Use the ViewModel
    }

}