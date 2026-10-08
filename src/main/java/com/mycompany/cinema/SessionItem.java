package com.mycompany.cinema;

public class SessionItem {

    private int id;
    private String movieName;
    private String roomName;
    private String time;
    private String priceType;

    public SessionItem(
            int id,
            String movieName,
            String roomName,
            String time,
            String priceType) {

        this.id = id;
        this.movieName = movieName;
        this.roomName = roomName;
        this.time = time;
        this.priceType = priceType;
    }

    public int getId() {
        return id;
    }

    public String getMovieName() {
        return movieName;
    }

    public String getRoomName() {
        return roomName;
    }

    public String getTime() {
        return time;
    }

    public String getPriceType() {
        return priceType;
    }

    @Override
    public String toString() {
        return movieName
                + " - "
                + roomName
                + " - "
                + time
                + " - "
                + priceType;
    }
}