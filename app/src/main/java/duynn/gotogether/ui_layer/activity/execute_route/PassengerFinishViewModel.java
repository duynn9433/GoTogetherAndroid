package duynn.gotogether.ui_layer.activity.execute_route;

import android.app.Application;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import duynn.gotogether.data_layer.model.dto.request.PassengerFinishRequest;
import duynn.gotogether.data_layer.model.model.Client;
import duynn.gotogether.data_layer.model.model.ClientTrip;
import duynn.gotogether.data_layer.model.model.Comment;
import duynn.gotogether.data_layer.repository.ClientTripRepo;
import duynn.gotogether.data_layer.repository.SessionManager;
import duynn.gotogether.data_layer.retrofit_client.RetrofitClient;
import org.jetbrains.annotations.NotNull;

public class PassengerFinishViewModel extends AndroidViewModel {
    MutableLiveData<String> message;
    MutableLiveData<String> status;
    MutableLiveData<Comment> comment;
    MutableLiveData<ClientTrip> clientTrip;
    ClientTripRepo clientTripRepo;
    SessionManager sessionManager;
    public PassengerFinishViewModel(@NonNull @NotNull Application application) {
        super(application);
        message = new MutableLiveData<>();
        status = new MutableLiveData<>();
        comment = new MutableLiveData<>();
        clientTrip = new MutableLiveData<>();
        sessionManager = SessionManager.getInstance(application);
        clientTripRepo = ClientTripRepo.getInstance(sessionManager.getToken());
        message.setValue("");
        status.setValue("");
        Comment data = Comment.builder().rating(4).content("Tuyệt vời").build();
        comment.setValue(new Comment());
        clientTrip.setValue(new ClientTrip());
    }

    public void finishTrip(Comment comment) {
        clientTripRepo.finishTrip(comment, status, message);
    }

    public void updateRating(int rating) {
        Comment data = comment.getValue();
        assert data != null;
        data.setRating(rating);
        comment.setValue(data);
    }

    public void updateComment(String toString) {
        Comment data = comment.getValue();
        assert data != null;
        data.setContent(toString);
        comment.setValue(data);
    }
}
