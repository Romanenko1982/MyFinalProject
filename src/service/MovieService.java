package service;

import model.Movie;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Scanner;

public class MovieService {

    private File movieDataBase = new File("resources/Movies/MoviesDataBase.txt");
    private HashMap<Integer, Movie> movieMap;
    private TicketService ticketService = new TicketService();
    private Scanner scanner = new Scanner(System.in);

    public void createMovieFile() {
        if (!movieDataBase.exists()) {
            try {
                movieDataBase.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public HashMap<Integer, Movie> getMovieMap() {
        if (movieMap == null) {
            movieMap = new HashMap<>();
            try (BufferedReader br = new BufferedReader(new FileReader(movieDataBase))) {
                String str;
                while ((str = br.readLine()) != null) {
                    String[] array = str.trim().split("; ");
                    Movie movie = new Movie(getFieldValue(array[1]),
                            convertToDate(getFieldValue(array[2])),
                            ticketService.getTicketListByMovie(getFieldValue(array[1])));
                    movieMap.put(movie.getId(), movie);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return movieMap;
    }

    private String getFieldValue(String str) {
        int index = str.indexOf("=");
        return str.substring(index + 1);
    }

    private Calendar convertToDate(String str) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.y");
        try {
            return new Calendar.Builder().setInstant(simpleDateFormat.parse(str)).build();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }


    public void showMovieList() {
        for (Movie movie : getMovieMap().values()) {
            System.out.printf("id - %s, фильм - %s, дата - %s", movie.getId(), movie.getName(), movie.getDateAsString());
            ticketService.showFreeTicketsForMovie(movie.getName());
            System.out.println();
        }
    }

    public void changeTheDateShowMovie() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.y");
        System.out.println("Введите id фильма, который хотите отредактировать");
        if (scanner.hasNextInt()) {
            int var = scanner.nextInt();
            if (getMovieMap().containsKey(var)) {
                Movie movie = getMovieMap().get(var);
                System.out.println("Введите новую дату просмотра фильма в формате dd.MM.y");
                String date = new Scanner(System.in).nextLine();
                try {
                    movie.setDateSeance(toCalendar(simpleDateFormat.parse(date)));
                } catch (ParseException e) {
                    System.out.println("Вы ввели в неверном формате дату");
                    changeTheDateShowMovie();
                }
            }
        }
    }

    private Calendar toCalendar(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return cal;
    }

    public void addMovie() {
        System.out.println("Введите название нового фильма и его дату через пробел.");
        String arr[] = scanner.nextLine().split(" +");
        Movie movie = new Movie(100,arr[0], convertToDate(arr[1]), ticketService.getTicketListByMovie(getFieldValue(arr[0])));
        getMovieMap().put(movie.getId(), movie);
    }

    public void removeMovie() {
        System.out.println("Введите id фильма который хотите удалить.");
        getMovieMap().remove(scanner.nextInt());
    }

    public void rewriteMovieDataBase() {
        HashMap<Integer, Movie> var = getMovieMap();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(movieDataBase))) {
            for (Movie movie : var.values()) {
                bw.write(String
                        .format("id=%s; name=%s; date=%s%n", movie.getId(), movie.getName(), movie.getDateAsString()));
            }
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
