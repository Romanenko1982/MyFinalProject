package model;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class Movie {

    private static int counter;
    private int id;
    private String name;
    private Calendar dateSeance;
    private List<Ticket> ticketsList;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.y");

    public Movie(String name, Calendar dateSeance, List<Ticket> ticketsList) {
        id = ++counter;
        this.name = name;
        this.dateSeance = dateSeance;
        this.ticketsList = ticketsList;
        for (Ticket ticket : ticketsList) {
            ticket.setMovieName(name);
        }
    }

    public Movie(int id, String name, Calendar dateSeance, List<Ticket> ticketsList) {
        this.id = id;
        this.name = name;
        this.dateSeance = dateSeance;
        this.ticketsList = ticketsList;
        for (Ticket ticket : ticketsList) {
            ticket.setMovieName(name);
        }
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDateAsString() {
        return simpleDateFormat.format(dateSeance.getTime());
    }

    public List<Ticket> getTicketsList() {
        return ticketsList;
    }

    public void setDateSeance(Calendar dateSeance) {
        this.dateSeance = dateSeance;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("id=").append(id);
        sb.append("; name=").append(name);
        sb.append("; dateSeance=").append(dateSeance);
        sb.append("; ticketsList=").append(ticketsList);
        return sb.toString();
    }
}
