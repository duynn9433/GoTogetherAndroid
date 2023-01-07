package duynn.gotogether.ui_layer.activity.rating;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import duynn.gotogether.data_layer.model.model.Comment;
import duynn.gotogether.data_layer.repository.CommentRepo;
import duynn.gotogether.data_layer.repository.SessionManager;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class DetailRatingViewModel extends AndroidViewModel {
    private static final String TAG = DetailRatingViewModel.class.getSimpleName();
    private MutableLiveData<List<Comment>> commentList;
    private MutableLiveData<String> status;
    private MutableLiveData<String> message;
    private SessionManager sessionManager;
    private CommentRepo commentRepo;
    public DetailRatingViewModel(@NonNull @NotNull Application application) {
        super(application);
        commentList = new MutableLiveData<>();
        status = new MutableLiveData<>();
        message = new MutableLiveData<>();
        commentList.setValue(new ArrayList<>());
        status.setValue("");
        message.setValue("");
        sessionManager = SessionManager.getInstance(application);
        commentRepo = CommentRepo.getInstance(sessionManager.getToken());
    }

    public void getCommentListData(Long id) {
        commentRepo.getCommentList(id, status, message, commentList);
    }
}
