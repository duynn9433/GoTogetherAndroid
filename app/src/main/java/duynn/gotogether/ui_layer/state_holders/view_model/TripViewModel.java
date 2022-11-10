package duynn.gotogether.ui_layer.state_holders.view_model;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import lombok.Getter;

@HiltViewModel
@Getter
public class TripViewModel extends ViewModel {

    private MutableLiveData<Long> startTime = new MutableLiveData<>();
    private MutableLiveData<Integer> totalSeat = new MutableLiveData<>();
    private MutableLiveData<Integer> emptySeat = new MutableLiveData<>();
    private MutableLiveData<Double> pricePerKm = new MutableLiveData<>();
    private MutableLiveData<String> description = new MutableLiveData<>();
    private MutableLiveData<Double> distancePlus = new MutableLiveData<>();
    private MutableLiveData<Boolean> isFinished = new MutableLiveData<>();
    private MutableLiveData<Boolean> isCanceled = new MutableLiveData<>();
    private MutableLiveData<Boolean> isStarted = new MutableLiveData<>();

    @Inject
    public TripViewModel() {
    }

}
