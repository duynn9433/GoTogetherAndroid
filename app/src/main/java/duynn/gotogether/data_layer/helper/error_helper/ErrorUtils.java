package duynn.gotogether.data_layer.helper.error_helper;


import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import duynn.gotogether.data_layer.retrofit_client.RetrofitClient;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Response;

import java.io.IOException;
import java.lang.annotation.Annotation;

public class ErrorUtils {
    public static ApiError parseError(Response<?> response){
        Converter<ResponseBody,ApiError> converter = RetrofitClient
                .getInstance()
                .retrofit
                .responseBodyConverter(ApiError.class,new Annotation[0]);
        ApiError error;
        try {
            error = converter.convert(response.errorBody());
        } catch (Exception e) {
            e.printStackTrace();
            return new ApiError();
        }
        return error;
    }
    public static ApiError parseErrorWithGson(String errorBody){
        try {
            Gson gson = new Gson();
            JsonElement jsonElement = new JsonParser().parse(errorBody);
            return gson.fromJson(jsonElement, ApiError.class);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
