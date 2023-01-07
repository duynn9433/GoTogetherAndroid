package duynn.gotogether.data_layer.service;

import duynn.gotogether.data_layer.model.dto.firebase.UpdateTokenRequest;
import duynn.gotogether.data_layer.model.dto.request.CommentRequest;
import duynn.gotogether.data_layer.model.dto.response.ListCommentResponse;
import duynn.gotogether.data_layer.model.model.Client;
import duynn.gotogether.data_layer.model.model.Comment;
import duynn.gotogether.data_layer.model.model.Status;
import duynn.gotogether.data_layer.model.model.Transport;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

import java.util.List;

public interface CommentService {
    @GET("comment/get-all-by-receiver-id/{id}")
    Call<ListCommentResponse> getAllByReceiverId(@Path("id") Long id);

    @GET("comment/get-all-received-comment/{id}")
    Call<List<Comment>> getAllReceivedComment(@Path("id") Long id);
    @GET("comment/get-all-send-comment/{id}")
    Call<List<Comment>> getAllSendComment(@Path("id") Long id);
    @POST("comment/create")
    Call<Status> createComment(@Body CommentRequest commmentRequest);

}
