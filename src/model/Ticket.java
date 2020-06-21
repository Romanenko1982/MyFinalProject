package model;

import java.io.Serializable;

public class Ticket implements Serializable {

    public static final long serialVersionUID = 0;
    private static int counter = 0;
    private int id;
    private User user;
    private String movieName;
    private int numberOfPlaces;
    private boolean isAvailable = true;

    public Ticket(int numberOfPlaces) {
        id = ++counter;
        this.numberOfPlaces = numberOfPlaces;
    }

    public void setMovieName(String movieName) {
        this.movieName = movieName;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public int getNumberOfPlaces() {
        return numberOfPlaces;
    }

    public int getId() {
        return id;
    }

    public String getMovieName() {
        return movieName;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }
}
