package duynn.gotogether.ui_layer.fragment.inbox;

import android.content.Intent;
import android.net.Uri;
import androidx.lifecycle.ViewModelProvider;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import duynn.gotogether.R;
import duynn.gotogether.databinding.FragmentInboxBinding;
import duynn.gotogether.databinding.FragmentSearchBinding;
import duynn.gotogether.domain_layer.GoogleMapUrlUseCase;
import duynn.gotogether.ui_layer.activity.execute_route.Direc2pointActivity;
import duynn.gotogether.ui_layer.activity.execute_route.MapsTestActivity;
import duynn.gotogether.ui_layer.fragment.search.SearchViewModel;

public class InboxFragment extends Fragment {

    public static InboxFragment newInstance() {
        return new InboxFragment();
    }

    private InboxViewModel mViewModel;

    public FragmentInboxBinding binding;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentInboxBinding.inflate(inflater, container, false);
//        searchViewModel = new ViewModelProvider(this).get(SearchViewModel.class);
        View view = binding.getRoot();
        binding.test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                binding.test.setText("test");
                Intent intent = new Intent(getActivity(), Direc2pointActivity.class);
                startActivity(intent);
            }
        });
        binding.bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //20.9809035,105.7852492 buu chinh
                //20.9889776,105.7924895 dh hanoi
                //21.0046809,105.8427335 bk
                //21.232800, 105.706457 my loca
                String uri = GoogleMapUrlUseCase.getGoogleMapUrlWithWaypoints(
                        "21.232800, 105.706457",
                        "21.0046809,105.8427335",
                        "driving",
                        "20.9889776,105.7924895");
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                startActivity(intent);
            }
        });

        return view;


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(InboxViewModel.class);
        // TODO: Use the ViewModel
    }

}