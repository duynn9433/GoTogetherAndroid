package duynn.gotogether.data_layer.repository;

import androidx.lifecycle.MutableLiveData;
import duynn.gotogether.data_layer.model.model.Client;
import duynn.gotogether.data_layer.model.model.Status;
import duynn.gotogether.data_layer.model.model.Transport;
import duynn.gotogether.data_layer.model.model.TransportWithoutOwner;
import duynn.gotogether.data_layer.retrofit_client.RetrofitClient;
import duynn.gotogether.data_layer.service.ClientService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ClientRepo {
    public static ClientRepo instance;
    private ClientService clientService;


    public static ClientRepo getInstance(String token){
        if(instance == null){
            instance = new ClientRepo(token);
        }
        return instance;
    }

    public ClientRepo(String token){
        clientService = RetrofitClient.getInstance().createServiceWithToken(ClientService.class,token);
    }

    public void getClientWithId(Long id, MutableLiveData<Client> client, MutableLiveData<Status> status) {
        Call<Client> call = clientService.getClientWithId(id);
        call.enqueue(new Callback<Client>() {
            @Override
            public void onResponse(Call<Client> call, Response<Client> response) {
                if (response.isSuccessful()) {
                    client.setValue(response.body());
                    status.setValue(new Status("success", "get client successfully"));
                } else {
                    status.setValue(new Status("fail", "get client fail"));
                }
            }

            @Override
            public void onFailure(Call<Client> call, Throwable t) {
                status.setValue(new Status("fail", "get client fail"));
                t.printStackTrace();
            }
        });
    }

    public void addTransport(Transport transport, MutableLiveData<Status> status, MutableLiveData<Client> client) {
        Call<Status> call = clientService.addTransport(client.getValue().getId(), transport);
        call.enqueue(new Callback<Status>() {
            @Override
            public void onResponse(Call<Status> call, Response<Status> response) {
                if (response.isSuccessful()) {
                    status.setValue(response.body());
                    Client client1 = client.getValue();
                    TransportWithoutOwner transportWithoutOwner = new TransportWithoutOwner(transport);
                    client1.getTransports().add(transportWithoutOwner);
                    client.setValue(client1);
                } else {
                    status.setValue(new Status("fail", "add transport fail"));
                }
            }

            @Override
            public void onFailure(Call<Status> call, Throwable t) {
                status.setValue(new Status("fail", "add transport fail"));
                t.printStackTrace();
            }
        });
    }
}
