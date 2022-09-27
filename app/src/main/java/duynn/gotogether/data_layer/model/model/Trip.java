package duynn.gotogether.data_layer.model.model;


import java.io.Serializable;
import java.util.*;

import duynn.gotogether.data_layer.model.dto.response.GoongMaps.PlaceDetail.Place;

public class Trip implements Serializable {
    private static final long serialVersionUID = 9L;

    private Long id;

    private Place startPlace;
    private Place endPlace;
    private List<Place> listStopPlace;

    private Position startPosition;

    private Position endPosition;

    private Calendar startTime;

    private Integer totalSeat;

    private Integer emptySeat;

    private Double pricePerKm;

    private String description;

    private Double distancePlus;
    private Transport transport;

    private boolean isFinished;

    private boolean isStarted;

    private boolean isCanceled;

    private Client driver;

    public Trip() {
        startPlace = new Place();
        endPlace = new Place();
        listStopPlace = new ArrayList<>();
        startTime = Calendar.getInstance();
        totalSeat = 0;
        emptySeat = 0;
        pricePerKm = 0.0;
        description = "";
        distancePlus = 0.0;
        transport = new Transport();
        driver = new Client();
    }

    public Place getStartPlace() {
        return startPlace;
    }

    public void setStartPlace(Place startPlace) {
        this.startPlace = startPlace;
    }

    public Place getEndPlace() {
        return endPlace;
    }

    public void setEndPlace(Place endPlace) {
        this.endPlace = endPlace;
    }

    public List<Place> getListStopPlace() {
        return listStopPlace;
    }

    public void setListStopPlace(List<Place> listStopPlace) {
        this.listStopPlace = listStopPlace;
    }

    public Position getStartPosition() {
        return startPosition;
    }

    public void setStartPosition(Position startPosition) {
        this.startPosition = startPosition;
    }

    public Position getEndPosition() {
        return endPosition;
    }

    public void setEndPosition(Position endPosition) {
        this.endPosition = endPosition;
    }

    public Calendar getStartTime() {
        return startTime;
    }

    public void setStartTime(Calendar startTime) {
        this.startTime = startTime;
    }

    public Integer getTotalSeat() {
        return totalSeat;
    }

    public void setTotalSeat(Integer totalSeat) {
        this.totalSeat = totalSeat;
    }

    public Integer getEmptySeat() {
        return emptySeat;
    }

    public void setEmptySeat(Integer emptySeat) {
        this.emptySeat = emptySeat;
    }

    public Double getPricePerKm() {
        return pricePerKm;
    }

    public void setPricePerKm(Double pricePerKm) {
        this.pricePerKm = pricePerKm;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getDistancePlus() {
        return distancePlus;
    }

    public void setDistancePlus(Double distancePlus) {
        this.distancePlus = distancePlus;
    }

    public Transport getTransport() {
        return transport;
    }

    public void setTransport(Transport transport) {
        this.transport = transport;
    }

    public boolean isFinished() {
        return isFinished;
    }

    public void setFinished(boolean finished) {
        isFinished = finished;
    }

    public boolean isStarted() {
        return isStarted;
    }

    public void setStarted(boolean started) {
        isStarted = started;
    }

    public boolean isCanceled() {
        return isCanceled;
    }

    public void setCanceled(boolean canceled) {
        isCanceled = canceled;
    }

    public Client getDriver() {
        return driver;
    }

    public void setDriver(Client driver) {
        driver = driver;
    }

    @Override
    public String toString() {
        return "Trip{" +
                "id=" + id +
                ", startPlace=" + startPlace +
                ", endPlace=" + endPlace +
                ", listStopPlace=" + listStopPlace +
                ", startPosition=" + startPosition +
                ", endPosition=" + endPosition +
                ", startTime=" + startTime +
                ", totalSeat=" + totalSeat +
                ", emptySeat=" + emptySeat +
                ", pricePerKm=" + pricePerKm +
                ", description='" + description + '\'' +
                ", distancePlus=" + distancePlus +
                ", transport=" + transport +
                ", isFinished=" + isFinished +
                ", isStarted=" + isStarted +
                ", isCanceled=" + isCanceled +
                ", driver=" + driver +
                '}';
    }
}
