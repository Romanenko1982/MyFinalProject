package service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Scanner;
import model.User;
import model.UserType;

public class UserService {

  private static User currentUser;
  private File userDataBase = new File("resources/Users/userDataBase.txt");
  private Scanner scanner = new Scanner(System.in);
  private HashMap<String, User> usersMap;

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
    return currentUser;
  }

  public void createFileUsers() {
    if (!userDataBase.exists()) {
      try {
        userDataBase.createNewFile();
        usersMap = new HashMap<>();
        User admin = new User("admin", "123", UserType.ADMINISTRATOR);
        User manager = new User("manager", "123", UserType.MANAGER);
        usersMap.put(admin.getLogin(), admin);
        usersMap.put(manager.getLogin(), manager);
        rewriteUsers();
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
        if (!user.getUserType().equals(UserType.ADMINISTRATOR)) {
          System.out.printf("id=%s, login=%s%n", user.getId(), user.getLogin());
        }
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
    HashMap<String, User> map = null;
    try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(userDataBase))) {
      map = (HashMap<String, User>) ois.readObject();
      map.values().forEach(user->{
        if(user.getId()>User.getCounter())User.setCounter(user.getId());
      });
    } catch (ClassNotFoundException | IOException e) {
      e.printStackTrace();
    }
    return map;
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

  public void removeUser() {
    System.out.println("Введите login пользователя, которого хотите удалить.");
    getUsersHashMap().remove(scanner.nextLine());
  }
}
