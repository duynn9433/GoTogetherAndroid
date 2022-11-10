package duynn.gotogether.data_layer.service;

import duynn.gotogether.data_layer.model.dto.request.Authen.LoginReq;
import duynn.gotogether.data_layer.model.dto.response.Authen.LoginResp;
import duynn.gotogether.data_layer.model.dto.response.Authen.RegisterRes;
import duynn.gotogether.data_layer.model.model.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthenService {
    @POST("authenticate")
    Call<LoginResp> authenticate(@Body LoginReq loginReq);

    @POST("login")
    Call<LoginResp> login(@Body LoginReq loginReq);

    @POST("register")
    Call<RegisterRes> register(@Body User user);
}
