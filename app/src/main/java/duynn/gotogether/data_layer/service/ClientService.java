package duynn.gotogether.data_layer.service;

import duynn.gotogether.data_layer.model.model.Client;
import duynn.gotogether.data_layer.model.model.Status;
import duynn.gotogether.data_layer.model.model.Transport;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ClientService {
    @GET("client/{id}")
    Call<Client> getClientWithId(@Path("id") Long id);

    @POST("client/{id}/transport")
    Call<Status> addTransport(@Path("id") Long id, @Body Transport transport);
}
