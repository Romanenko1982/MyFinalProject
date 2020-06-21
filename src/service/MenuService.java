package service;

import model.Menu;
import model.UserType;

import java.util.Scanner;

public class MenuService {

    private Scanner scanner = new Scanner(System.in);
    private UserService userService = new UserService();
    private Menu menu = new Menu();
    private MovieService movieService = new MovieService();
    private TicketService ticketService = new TicketService();

    public void startWelcomeMenu() {
        userService.createFileUsers();
        menu.printTheWelcomeMenuView();
        startMenu();
    }

    public void startMenu() {
        if (scanner.hasNextInt()) {
            int var = scanner.nextInt();
            switch (var) {
                case 1:
                    userService.userRegistration();
                    break;
                case 2:
                    userService.userLogin();
                    break;
                case 3:
                    exit();
                    break;
                default:
                    System.out.println("Неверно введенные данные, попробуйте снова\n");
                    startWelcomeMenu();
            }
            subMenu();
        } else {
            System.out.println("Неверно введенные данные, попробуйте снова\n");
            scanner.next();
            startWelcomeMenu();
        }
    }

    public void subMenu() {
        UserType type = userService.getUser().getUserType();
        switch (type) {
            case COMMON_USER:
                startMenuCommon();
                break;
            case MANAGER:
                startMenuManager();
                break;
            case ADMINISTRATOR:
                startMenuAdministrator();
                break;
        }
    }

    public void startMenuCommon() {
        menu.printTheCommonMenuView();
        if (scanner.hasNextInt()) {
            int var = scanner.nextInt();
            switch (var) {
                case 1:
                    movieService.showMovieList();
                    break;
                case 2:
                    ticketService.purchaseTicket(movieService);
                    break;
                case 3:
                    ticketService.showPurchaseTicketForUser();
                    break;
                case 4:
                    ticketService.returnTicket(movieService);
                    break;
                case 5:
                    exit();
                    break;
                default:
                    System.out.println("Неверно введенные данные, попробуйте снова\n");
            }
            startMenuCommon();
        }
    }

    private void exit() {
        userService.rewriteUsers();
        movieService.rewriteMovieDataBase();
        scanner.close();
        System.exit(0);
    }

    public void startMenuManager() {
        menu.printManagerEditorMenu();
        if (scanner.hasNextInt()) {
            int var = scanner.nextInt();
            switch (var) {
                case 1:
                    movieService.showMovieList();
                    break;
                case 2:
                    movieService.changeTheDateShowMovie();
                    break;
                case 3:
                    userService.printHashMapUsers();
                    break;
                case 4:
                    ticketService.purchaseTicketForManager(movieService);
                    break;
                case 5:
                    ticketService.removePurchasedTicketForManager(movieService);
                    break;
                case 6:
                    ticketService.showPurchaseTicketForManagerByLogin();
                    break;
                case 7:
                    exit();
                    break;
                default:
                    System.out.println("Неверно введенные данные, попробуйте снова\n");
                    startMenuManager();
            }
        }
        startMenuManager();
    }

    public void startMenuAdministrator() {
        menu.printTheAdministratorMenuView();
        if (scanner.hasNextInt()) {
            int var = scanner.nextInt();
            switch (var) {
                case 1:
                    userService.printHashMapUsers();
                    break;
                case 2:
                    administratorEditMenu();
                    break;
                case 3:
                    userService.removeUser();
                    break;
                case 4:
                    movieService.addMovie();
                    break;
                case 5:
                    movieService.removeMovie();
                    break;
                case 6:
                    exit();
                    break;
                default:
                    System.out.println("Неверно введенные данные, попробуйте снова\n");
                    startMenuAdministrator();
            }
            startMenuAdministrator();
        }
    }

    private void administratorEditMenu() {
        menu.printEditUserMenuView();
        if (scanner.hasNextInt()) {
            int var = scanner.nextInt();
            switch (var) {
                case 1:
                    userService.changeLoginUser();
                    break;
                case 2:
                    userService.changePasswordUser();
                    break;
                case 3:
                    userService.changeTypeUser();
                    break;
                default:
                    administratorEditMenu();
            }
            startMenuAdministrator();
        }
    }
}
