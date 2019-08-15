package Models;

import Database.Database;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import enums.UserRights;

import javax.ws.rs.container.ContainerRequestContext;
import javax.xml.bind.ValidationException;
import java.awt.*;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class User {

    @JsonProperty
    String userId;

    @JsonProperty
    String username;

    @JsonProperty
    String password;

    @JsonProperty
    String role;

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public static TokenSession logIn(String username, String password)
            throws ClassNotFoundException, InvalidKeySpecException, SQLException, NoSuchAlgorithmException, ValidationException {

        PreparedStatement ps = Database.prepareStatement("SELECT * FROM users WHERE username = ? LIMIT 1");
        ps.setString(1, username);
        ResultSet rs = ps.executeQuery();
        TokenSession tokenSession = null;

        if (!rs.isBeforeFirst()) {
            System.out.println("if rs is before");
            throw new ValidationException("User does not exist!");
        }

        if (rs.next()) {
            boolean matched = password.equals(rs.getString("password"));
            if (!matched) {
                throw new ValidationException("Invalid credentials");
            }

            tokenSession = TokenSession.generateSessionToken(rs.getString("user_id"));

        }

        return tokenSession;

    }

    public static boolean hasRequiredRights(String userId, UserRights[] allowedRights, String parameter)
            throws ClassNotFoundException, SQLException {

        User user = getUser(userId);
        if (user == null) return false;

        String role = user.getRole();

        PreparedStatement ps = Database.prepareStatement("SELECT r.*, rr.*" +
                "FROM roles r INNER JOIN role_rights rr ON rr.role_id = r.role_id " +
                "WHERE role_name = ? ");
        //INNER JOIN rights rh ON rh.right_name = rr.right_name
        ps.setString(1, role);
        ResultSet rs = ps.executeQuery();
        List<String> userRights = new ArrayList<String>();

        while (rs.next()) {
            userRights.add(rs.getString("right_name"));
            System.out.println(rs.getString("right_name"));
        }
        if(!parameter.equals("0")) {
            System.out.println("udje u if");
            PreparedStatement preparedStatement = Database.prepareStatement("SELECT createdBy FROM PRODUCTS" +
                    " WHERE id = ?");
            preparedStatement.setString(1, parameter);
            ResultSet rs1 = preparedStatement.executeQuery();
            if (rs1.next()) {
                String createdBy = rs1.getString(1);
                if (createdBy.equals(userId)) {
                    userRights.add("modifyProducts");
                }
            }
        }

        for (UserRights allowedRight : allowedRights) {

            if (userRights != null && userRights.contains(allowedRight.name())) {
                return true;
            }

        }
        return false;
    }

    public static boolean hasRequiredRights(String userId, UserRights[] allowedRights)
            throws ClassNotFoundException, SQLException {

        User user = getUser(userId);
        if (user == null) return false;

        String role = user.getRole();

        PreparedStatement ps = Database.prepareStatement("SELECT r.*, rr.*" +
                "FROM roles r INNER JOIN role_rights rr ON rr.role_id = r.role_id " +
                "WHERE role_name = ? ");
        //INNER JOIN rights rh ON rh.right_name = rr.right_name
        ps.setString(1, role);
        ResultSet rs = ps.executeQuery();
        List<String> userRights = new ArrayList<String>();

        while (rs.next()) {
            userRights.add(rs.getString("right_name"));
        }
        for (UserRights allowedRight : allowedRights) {

            if (userRights != null && userRights.contains(allowedRight.name())) {
                return true;
            }

        }
        return false;
    }

    public static User getUser(String userId) throws ClassNotFoundException, SQLException {

        PreparedStatement ps = Database.prepareStatement("SELECT * FROM users WHERE id = ?");
        ps.setString(1, userId);
        ResultSet rs = ps.executeQuery();

        User user = new User();

        if (rs.next()) {
            user.setUserId(rs.getString("id"));
            user.setUsername(rs.getString("username"));
            user.setPassword(rs.getString("password"));
            user.setRole(rs.getString("role"));
        }

        return user;
    }

    public static User addUser(User user) throws ClassNotFoundException, SQLException {
        PreparedStatement ps = Database.prepareStatement("INSERT INTO users (user_id , username, password, role)" +
                " VALUES (?,?,?,?)");
        String id = Database.generatorId(5);
        user.setUserId(id);
        ps.setString(1, user.getUserId());
        ps.setString(2, user.getUsername());
        ps.setString(3, user.getPassword());
        ps.setString(4, user.getRole());

        ps.executeUpdate();

        return user;

    }

    public static List<User> getUsers() throws ClassNotFoundException, SQLException {
        PreparedStatement ps = Database.prepareStatement("SELECT * FROM users");
        ResultSet rs = ps.executeQuery();
        List<User> users = new ArrayList<User>();
        while (rs.next()) {
            User user = new User();
            user.setUserId(rs.getString(1));
            user.setUsername(rs.getString(2));
            user.setPassword(rs.getString(3));
            user.setRole(rs.getString(4));
            users.add(user);
        }
        return users;
    }
}
