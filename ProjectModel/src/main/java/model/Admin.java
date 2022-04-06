package model;

import java.io.Serializable;

public class Admin implements Identifiable<Integer>, Serializable {
    private int id;
    private String username;
    private String password;

    public Admin() {
    }

    public Admin(int id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    @Override
    public Integer getID() {
        return id;
    }

    @Override
    public void setID(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "Admin{" +
                "id=" + id +
                ", username=" + username +
                ", password=" + password +
                '}';
    }
}