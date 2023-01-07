package duynn.gotogether.ui_layer.activity.rating;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import duynn.gotogether.R;
import duynn.gotogether.databinding.ActivityDetailRatingBinding;
import duynn.gotogether.domain_layer.ToastUseCase;
import duynn.gotogether.domain_layer.common.Constants;

import java.util.ArrayList;

public class DetailRatingActivity extends AppCompatActivity {
    private ActivityDetailRatingBinding binding;
    private static final String TAG = DetailRatingActivity.class.getSimpleName();
    private DetailRatingViewModel viewModel;
    private CommentItemAdapter commentItemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailRatingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        viewModel = new ViewModelProvider(this).get(DetailRatingViewModel.class);
        Bundle bundle = getIntent().getBundleExtra(Constants.Bundle);
        Long id = bundle.getLong(Constants.ID);
        initRecyclerView();
        viewModel.getCommentListData(id);
        observerData();
    }

    private void observerData() {
        viewModel.getStatus().observe(this, status -> {
            switch (status) {
                case Constants.FAIL:
                    ToastUseCase.showLongToast(this, viewModel.getMessage().getValue()+"");
                    break;
            }
        });
    }

    private void initRecyclerView() {
        commentItemAdapter = new CommentItemAdapter(new ArrayList<>(),this);
        binding.commentList.setAdapter(commentItemAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        binding.commentList.setLayoutManager(linearLayoutManager);
        viewModel.getCommentList().observe(this, comments -> {
            commentItemAdapter.setCommentList(comments);
            commentItemAdapter.notifyDataSetChanged();
        });
    }
}