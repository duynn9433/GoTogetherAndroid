package duynn.gotogether.data_layer.repository;

import androidx.lifecycle.MutableLiveData;
import duynn.gotogether.data_layer.helper.error_helper.ApiError;
import duynn.gotogether.data_layer.helper.error_helper.ErrorUtils;
import duynn.gotogether.data_layer.model.dto.firebase.UpdateTokenRequest;
import duynn.gotogether.data_layer.model.dto.request.CommentRequest;
import duynn.gotogether.data_layer.model.model.*;
import duynn.gotogether.data_layer.retrofit_client.RetrofitClient;
import duynn.gotogether.data_layer.service.ClientService;
import duynn.gotogether.data_layer.service.CommentService;
import duynn.gotogether.domain_layer.common.Constants;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.IOException;
import java.util.List;

public class CommentRepo {
    public static CommentRepo instance;
    private CommentService commentService;


    public static CommentRepo getInstance(String token){
        if(instance == null){
            instance = new CommentRepo(token);
        }
        return instance;
    }

    public CommentRepo(String token){
        commentService = RetrofitClient.getInstance().createServiceWithToken(CommentService.class,token);
    }

    public void submitRating(Comment comment,
                             MutableLiveData<String> status,
                             MutableLiveData<String> message) {
        CommentRequest commentRequest = new CommentRequest(comment);
        commentRequest.setCommentDTO(comment);
        Call<Status> call = commentService.createComment(commentRequest);
        call.enqueue(new Callback<Status>() {
            @Override
            public void onResponse(Call<Status> call, Response<Status> response) {
                if(response.isSuccessful()){
                    Status statusResponse = response.body();
                    if(statusResponse != null){
                        status.setValue(statusResponse.getStatus());
                        message.setValue(statusResponse.getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<Status> call, Throwable t) {
                status.setValue(Constants.FAIL);
                message.setValue(t.getMessage());
            }
        });
    }

    public void getCommentList(Long id,
                               MutableLiveData<String> status,
                               MutableLiveData<String> message,
                               MutableLiveData<List<Comment>> commentList) {
        Call<List<Comment>> call = commentService.getAllReceivedComment(id);
        call.enqueue(new Callback<List<Comment>>() {
            @Override
            public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
                if(response.isSuccessful()){
                    List<Comment> commentListResponse = response.body();
                    if(commentListResponse != null){
                        commentList.setValue(commentListResponse);
                        status.setValue(Constants.SUCCESS);
                    }
                }
                else {
                    ApiError apiError = null;
                    try {
                        String errorBody = response.errorBody().string();
                        apiError = ErrorUtils.parseErrorWithGson(errorBody);
                    } catch (IOException e) {
                        apiError = new ApiError();
                        throw new RuntimeException(e);
                    }
                    message.postValue(apiError.getMessage());
                    status.postValue(Constants.FAIL);
                }
            }

            @Override
            public void onFailure(Call<List<Comment>> call, Throwable t) {
                status.setValue(Constants.FAIL);
                message.setValue(t.getMessage());
            }
        });
    }
}
