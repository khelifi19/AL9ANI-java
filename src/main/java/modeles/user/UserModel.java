package model.user;

import utils.user.DataUtils;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Objects;

public class UserModel {
    private String username;
    private int userId;
   private String password;
   private String email;

    public UserModel(int idUser, String username, String email, String firstName, String lastName, String password) {
        this.userId=idUser;
        this.username=username;
        this.email=email;
        this.firstName=firstName;
        this.lastName=lastName;
        this.password=password;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    private String firstName;
   private String lastName;

    private Connection connection;



    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public UserModel(String username, int userId, String password) {
        this.username = username;
        this.userId = userId;
        this.password = password;
        try {
            this.connection= DataUtils.getConnection();
            System.out.println("Connexion réussie!");



        } catch (SQLException e) {
            e.printStackTrace();
        }}

    public void setId(int userId) {
        this.userId = userId;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getuserId() {
        return userId;
    }

    public String getPassword() {
        return password;
    }

    public UserModel() {
        this.username="";
        this.password="";
        this.userId=0;
        try {
            this.connection=DataUtils.getConnection();
            System.out.println("Connexion réussie!");



        } catch (SQLException e) {
            e.printStackTrace();
        }}



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserModel userModel = (UserModel) o;
        return userId == userModel.userId && Objects.equals(username, userModel.username) && Objects.equals(password, userModel.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(password);
    }



}
