package duynn.gotogether.ui_layer.activity.rating;

import android.app.Application;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import duynn.gotogether.data_layer.model.model.Comment;
import duynn.gotogether.data_layer.repository.ClientTripRepo;
import duynn.gotogether.data_layer.repository.CommentRepo;
import duynn.gotogether.data_layer.repository.SessionManager;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

@Getter
@Setter
public class RatingViewModel extends AndroidViewModel {
    private static final String TAG = RatingViewModel.class.getSimpleName();
    private MutableLiveData<String> message;
    private MutableLiveData<String> status;
    private MutableLiveData<Comment> comment;
    private SessionManager sessionManager;
    private CommentRepo commentRepo;
    private boolean isPassenger;
    private int position;

    public RatingViewModel(@NonNull @NotNull Application application) {
        super(application);
        message = new MutableLiveData<>();
        status = new MutableLiveData<>();
        comment = new MutableLiveData<>();
        message.setValue("");
        status.setValue("");
        comment.setValue(new Comment());
        sessionManager = SessionManager.getInstance(application);
        commentRepo = CommentRepo.getInstance(sessionManager.getToken());
        isPassenger = false;
    }


    public void setRating(Float rating) {
        Comment temp = comment.getValue();
        if(temp !=null){
            temp.setRating(Math.round(rating));
            comment.setValue(temp);
        }
    }
    public void setCommentContent(String content) {
        Comment temp = comment.getValue();
        if(temp !=null){
            temp.setContent(content);
            comment.setValue(temp);
        }
    }

    public void submitRating() {
        commentRepo.submitRating(comment.getValue(), status, message);
    }


}
