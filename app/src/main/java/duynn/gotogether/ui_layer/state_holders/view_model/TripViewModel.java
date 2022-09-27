package duynn.gotogether.ui_layer.state_holders.view_model;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import duynn.gotogether.data_layer.model.model.Position;

@HiltViewModel
public class TripViewModel extends ViewModel {

    private MutableLiveData<Position> startLocation = new MutableLiveData<>();
    private MutableLiveData<Position> endLocation = new MutableLiveData<>();
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

    public MutableLiveData<Position> getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(Position startLocation) {
        this.startLocation.postValue(startLocation);
        this.startLocation.setValue(startLocation);
    }

    public MutableLiveData<Position> getEndLocation() {
        return endLocation;
    }

    public void setEndLocation(Position endLocation) {
        this.endLocation.postValue(endLocation);
    }

    public MutableLiveData<Long> getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime.postValue(startTime);
    }

    public MutableLiveData<Integer> getTotalSeat() {
        return totalSeat;
    }

    public void setTotalSeat(Integer totalSeat) {
        this.totalSeat.postValue(totalSeat);
    }

    public MutableLiveData<Integer> getEmptySeat() {
        return emptySeat;
    }

    public void setEmptySeat(Integer emptySeat) {
        this.emptySeat.postValue(emptySeat);
    }

    public MutableLiveData<Double> getPricePerKm() {
        return pricePerKm;
    }

    public void setPricePerKm(Double pricePerKm) {
        this.pricePerKm.postValue(pricePerKm);
    }

    public MutableLiveData<String> getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description.postValue(description);
    }

    public MutableLiveData<Double> getDistancePlus() {
        return distancePlus;
    }

    public void setDistancePlus(Double distancePlus) {
        this.distancePlus.postValue(distancePlus);
    }

    public MutableLiveData<Boolean> getIsFinished() {
        return isFinished;
    }

    public void setIsFinished(Boolean isFinished) {
        this.isFinished.postValue(isFinished);
    }

    public MutableLiveData<Boolean> getIsCanceled() {
        return isCanceled;
    }

    public void setIsCanceled(Boolean isCanceled) {
        this.isCanceled.postValue(isCanceled);
    }

    public MutableLiveData<Boolean> getIsStarted() {
        return isStarted;
    }

    public void setIsStarted(Boolean isStarted) {
        this.isStarted.postValue(isStarted);
    }
}
