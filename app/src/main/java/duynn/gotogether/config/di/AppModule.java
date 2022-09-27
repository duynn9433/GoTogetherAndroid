package duynn.gotogether.config.di;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import duynn.gotogether.data_layer.service.GoongPlaceService;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
@InstallIn(SingletonComponent.class)
public class AppModule {
    String goongBaseUrl = "https://rsapi.goong.io/";

    @Singleton
    @Provides
    public Retrofit getGoongRetrofit(){
        return new Retrofit.Builder()
                .baseUrl(goongBaseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @Singleton
    @Provides
    public GoongPlaceService getGoongPlaceService(Retrofit goongRetrofit){
        return goongRetrofit.create(GoongPlaceService.class);
    }
}
