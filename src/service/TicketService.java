package service;

import model.Movie;
import model.Ticket;
import model.User;
import model.UserType;

import javax.jws.soap.SOAPBinding;
import java.io.*;
import java.util.*;

import static java.lang.String.format;

public class TicketService {

    private List<Ticket> ticketsMovieMap;
    private File ticketsDirectory = new File("Tickets");
    private List<Ticket> purchaseTicketsUser;
    private UserService userService = new UserService();
    private Scanner scanner = new Scanner(System.in);
    private static final String USER_PURCHASE_TICKET_LIST_PATH = "Users/%s_purchaseTickets.txt";


    public List<Ticket> getTicketListByMovie(String movieName) {
        File file = new File(format("Tickets/%s_tickets.txt", movieName));
        if (file.exists()) {
            try (FileInputStream fis = new FileInputStream(file)) {
                ObjectInputStream ois = new ObjectInputStream(fis);
                return (List<Ticket>) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            createFile(movieName, createEmptyTicketList());
            return createEmptyTicketList();
        }
        return null;
    }

    private void createFile(String movieName, List<Ticket> ticketsList) {
        try (FileOutputStream fos = new FileOutputStream(format("%s/%s_tickets.txt", ticketsDirectory, movieName))) {
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(ticketsList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<Ticket> createEmptyTicketList() {
        List<Ticket> tickets = new LinkedList<>();
        for (int i = 0; i < 10; i++) {
            tickets.add(new Ticket(i + 1));
        }
        return tickets;
    }

    public void showFreeTicketsForMovie(String movieName) {
        System.out.print(" Свободные места:");
        for (Ticket ticket : getTicketListByMovie(movieName)) {
            if (ticket.isAvailable()) {
                System.out.print(" " + ticket.getNumberOfPlaces());
            }
        }
    }

    public void purchaseTicketByRequest(String request, MovieService movieService) {
        String[] array = request.trim().split(" +");
        int idMovie = Integer.parseInt(array[0]);
        int numberOfPlaces = Integer.parseInt(array[1]) - 1;
        User user;
        if (array.length == 3 && userService.getUser().getUserType() != UserType.COMMON_USER) {
            user = userService.getUserByLogin(array[2]);
        } else {
            user = userService.getUser();
        }
        Ticket ticket = movieService.getMovieMap()
                .get(idMovie)
                .getTicketsList()
                .get(numberOfPlaces);
        if (ticket.isAvailable()) {
            ticket.setUser(user);
            ticket.setAvailable(false);
            putPurchaseTicket(user, ticket);
            createFile(ticket.getMovieName(), changeStatusTicketById(ticket, movieService, idMovie));
        } else {
            System.out.println("Данный билет уже куплен.");
        }
    }

    private List<Ticket> changeStatusTicketById(Ticket ticket, MovieService movieService, int id) {
        List<Ticket> ticketsOfMovie = movieService.getMovieMap().get(id).getTicketsList();
        ticketsOfMovie.set(ticket.getNumberOfPlaces() - 1, ticket);
        return ticketsOfMovie;
    }

    public void purchaseTicket(MovieService movieService) {
        System.out.println("Введите id фильма и номер места через пробел.");
        purchaseTicketByRequest(scanner.nextLine(), movieService);
    }

    private void putPurchaseTicket(User user, Ticket ticket) {
        if (purchaseTicketsUser == null) {
            purchaseTicketsUser = new LinkedList<>();
        }
        purchaseTicketsUser.add(ticket);
        saveChangePurchaseTicketsForUser(user, purchaseTicketsUser);
    }

    private void saveChangePurchaseTicketsForUser(User user, List<Ticket> tickets) {
        File file = new File(format(USER_PURCHASE_TICKET_LIST_PATH, user.getLogin()));
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        serializationPurchaseTicketsFile(tickets, file);
    }

    public void showPurchaseTicketForUser() {
        showPurchaseTicketByUser(userService.getUser());
    }

    private void showPurchaseTicketByUser(User user) {
        if (getDeserializationPurchaseTicketForUser(user).isEmpty()) {
            System.out.println("Купленных билетов нет!");
        } else {
            for (Ticket ticket : getDeserializationPurchaseTicketForUser(user)) {
                System.out.printf("id ticket - %s, movie - %s, place - %s%n",
                        ticket.getId(), ticket.getMovieName(), ticket.getNumberOfPlaces());
            }
        }
    }

    public void showPurchaseTicketForManagerByLogin() {
        System.out.println("Введите login пользователя");
        User user = userService.getUserByLogin(scanner.nextLine().trim());
        showPurchaseTicketByUser(user);
    }

    private List<Ticket> getDeserializationPurchaseTicketForUser(User user) {
        File file = new File(format(USER_PURCHASE_TICKET_LIST_PATH, user.getLogin()));
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try (FileInputStream fis = new FileInputStream(file)) {
                ObjectInputStream ois = new ObjectInputStream(fis);
                return (List<Ticket>) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return Collections.emptyList();
    }

    private void serializationPurchaseTicketsFile(List<Ticket> tickets, File file) {
        try (FileOutputStream fos = new FileOutputStream(file)) {
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(tickets);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void returnTicket(MovieService movieService) {
        System.out.println("Ввудите id билета, который хотите вернуть.");
        returnTicketByRequest(scanner.nextLine(), movieService);
    }

    private void returnTicketByRequest(String request, MovieService movieService) {
        String array[] = request.trim().split(" +");
        User user;
        if (array.length == 2 && userService.getUser().getUserType() != UserType.COMMON_USER) {
            user = userService.getUserByLogin(array[1]);
        } else {
            user = userService.getUser();
        }
        List<Ticket> list = getDeserializationPurchaseTicketForUser(user);
        List<Ticket> newList = new LinkedList<>();
        int id = Integer.parseInt(array[0]);
        for (Ticket ticket : list) {
            if (id != ticket.getId()) {
                newList.add(ticket);
            } else {
                ticket.setAvailable(true);
                ticket.setUser(null);
            }
            saveChangePurchaseTicketsForUser(user, newList);
            createFile(ticket.getMovieName(),
                    changeStatusTicketById(ticket, movieService, getIdMovieByName(ticket.getMovieName(), movieService)));
        }
    }

    private int getIdMovieByName(String movieName, MovieService movieService) {
        for (Movie movie : movieService.getMovieMap().values()) {
            if (movie.getName().equals(movieName)) {
                return movie.getId();
            }
        }
        return 0;
    }

    public void purchaseTicketForManager(MovieService movieService) {
        System.out.println("Введите id фильма, номер места и login пользователя через пробел.");
        purchaseTicketByRequest(scanner.nextLine(), movieService);

    }

    public void removePurchasedTicketForManager(MovieService movieService) {
        System.out.println("Введите id билета и login пользователя, у которого хотите отмениь покупку билета.");
        returnTicketByRequest(scanner.nextLine(), movieService);
    }
}
