package model;

import java.io.Serializable;

public class User implements Serializable {

    private static final long serialVersionUID = 0;
    private int id;
    private String login;
    private String password;
    private UserType userType;
    private static int counter;

    public User(int id, String login, String password, UserType userType) {
        this.id = id;
        this.login = login;
        this.password = password;
        this.userType = userType;
    }

    public User(String login, String password, UserType userType) {
        this(++counter, login, password,userType);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("id=").append(id);
        sb.append("; Login=").append(login);
        sb.append("; Password=").append(password);
        sb.append("; UserType=").append(userType);
        return sb.toString();
    }


    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public int getId() {
        return id;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }
}
