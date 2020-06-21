package service;

import model.User;
import model.UserType;

import java.io.*;
import java.util.HashMap;
import java.util.Scanner;

public class UserService {

    private File userDataBase = new File("Users/userDataBase.txt");
    private Scanner scanner = new Scanner(System.in);
    private HashMap<String, User> usersMap;
    private static User currentUser;

    public void userRegistration() {
        String login;
        String password;
        login = checkLogin();
        password = checkPassword();
        currentUser = new User(login, password, UserType.COMMON_USER);
        usersMap.put(currentUser.getLogin(), currentUser);
        System.out.println("Пользователь создан успешно.");
    }

    private String checkLogin() {
        System.out.println("Введите login:");
        String login = scanner.nextLine();
        if (login.isEmpty()) {
            System.out.println("Вы ввели пустую строку, повторите снова");
            checkLogin();
        } else if (getUsersHashMap().containsKey(login)) {
            System.out.println(
                    "Пользователь " + login + " уже существует, придумайте другой login и повторите снова");
            checkLogin();
        } else {
            return login;
        }
        return null;
    }

    private String checkPassword() {
        System.out.println("Введите пароль:");
        String password = scanner.nextLine();
        if (password.isEmpty()) {
            System.out.println("Вы ввели пустую строку, повторите снова");
            checkPassword();
        } else {
            return password;
        }
        return null;
    }

    public User userLogin() {
        System.out.println("Введите login:");
        String login = scanner.nextLine();
        if (getUsersHashMap().containsKey(login)) {
            currentUser = usersMap.get(login);
            System.out.println("Введите пароль:");
            String password = scanner.nextLine();
            if (password.equals(currentUser.getPassword())) {
                System.out.println("Вход выполнен успешно");
            } else {
                System.out.println("Неправильно введены данные, повторите снова");
                userLogin();
            }
        } else {
            System.out.println("Неправильно введены данные, повторите снова");
            userLogin();
        }
        System.out.println();
//        User user1 = new User("admin", "123", UserType.ADMINISTRATOR);
//        usersMap.put(user1.getLogin(), user1);
        return currentUser;
    }

    public void createFileUsers() {
        if (!userDataBase.exists()) {
            try {
                userDataBase.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public HashMap<String, User> getUsersHashMap() {
        if (usersMap == null) {
            usersMap = getDeserializationUsersMap();
        }
        return usersMap;
    }

    public void printHashMapUsers() {
        if (currentUser.getUserType() == UserType.MANAGER) {
            for (User user : getUsersHashMap().values()) {
                if (user.getUserType().equals(UserType.COMMON_USER)) {
                    System.out.printf("id=%s, login=%s%n", user.getId(), user.getLogin());
                }
            }
        } else {
            for (User user : getUsersHashMap().values()) {
                if (!user.getUserType().equals(UserType.ADMINISTRATOR))
                System.out.printf("id=%s, login=%s%n", user.getId(), user.getLogin());
            }
        }
    }

    public void rewriteUsers() {
        try (FileOutputStream fos = new FileOutputStream(userDataBase)) {
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(usersMap);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public HashMap<String, User> getDeserializationUsersMap() {
        try (FileInputStream fis = new FileInputStream(userDataBase)) {
            ObjectInputStream ois = new ObjectInputStream(fis);
            try {
                return (HashMap<String, User>) ois.readObject();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            return new HashMap<String, User>();
        }
        return null;
    }

    public User getUser() {
        return currentUser;
    }

    public User getUserByLogin(String login) {
        return getUsersHashMap().get(login);
    }

    public void changeLoginUser() {
        System.out.println("Введите login пользователя у которого хотите поменять login.");
        User user = getUsersHashMap().remove(scanner.nextLine());
        System.out.println("Введите новый login");
        user.setLogin(scanner.nextLine());
        getUsersHashMap().put(user.getLogin(), user);
    }

    public void changePasswordUser() {
        System.out.println("Введите login пользователя у которого хотите поменять password.");
        User user = getUserByLogin(scanner.nextLine());
        System.out.println("Введите новый password");
        user.setPassword(scanner.nextLine());
    }

    public void changeTypeUser() {
        System.out.println("Введите login пользователя у которого хотите поменять тип.");
        User user = getUserByLogin(scanner.nextLine());
        System.out.println("Введите новый тип");
        user.setUserType(UserType.valueOf(scanner.nextLine().toUpperCase()));
    }

    public  void removeUser() {
        System.out.println("Введите login пользователя, которого хотите удалить.");
        getUsersHashMap().remove(scanner.nextLine());
    }
}
