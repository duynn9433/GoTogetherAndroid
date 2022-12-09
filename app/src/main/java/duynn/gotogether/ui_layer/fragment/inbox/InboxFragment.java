package duynn.gotogether.ui_layer.fragment.inbox;

import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
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
import duynn.gotogether.data_layer.model.chat.Client;
import duynn.gotogether.databinding.FragmentInboxBinding;
import duynn.gotogether.databinding.FragmentSearchBinding;
import duynn.gotogether.domain_layer.GoogleMapUrlUseCase;
import duynn.gotogether.ui_layer.activity.execute_route.Direc2pointActivity;
import duynn.gotogether.ui_layer.activity.execute_route.MapsTestActivity;
import duynn.gotogether.ui_layer.fragment.search.SearchViewModel;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class InboxFragment extends Fragment {

    public static InboxFragment newInstance() {
        return new InboxFragment();
    }

    private InboxViewModel mViewModel;
    private UserAdapter userAdapter;

    public FragmentInboxBinding binding;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentInboxBinding.inflate(inflater, container, false);
//        searchViewModel = new ViewModelProvider(this).get(SearchViewModel.class);
        View view = binding.getRoot();
        mViewModel = new ViewModelProvider(this).get(InboxViewModel.class);
        mViewModel.getClientsFromRoomDB();

        binding.swipeRefershLayout.setOnRefreshListener(() -> {
            mViewModel.getClientsFromRoomDB();
            Handler handler = new Handler();
            handler.postDelayed(() -> binding.swipeRefershLayout.setRefreshing(false), 2000);
        });

        initRecyclerView();
        observeData();

        return view;
    }

    private void observeData() {
        mViewModel.getClients().observe(getViewLifecycleOwner(), clients -> {
            userAdapter.setClients(clients);
            userAdapter.notifyDataSetChanged();
        });
    }

    private void initRecyclerView() {
        List<Client> data = new ArrayList<>();
//        data.add(Client.builder()
//                        .id(1L)
//                        .name("Duy")
//                        .phone("0123456789")
//                        .build());
        userAdapter = new UserAdapter(getContext(), data);
        binding.userRecyclerView.setAdapter(userAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        binding.userRecyclerView.setLayoutManager(linearLayoutManager);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(InboxViewModel.class);
        // TODO: Use the ViewModel
    }

}