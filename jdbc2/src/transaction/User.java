package transaction;

import java.sql.Date;

/**
 * @author shkstart
 * @create 2022-05-29-16:15
 */
public class User {
    private String user;
    private String password;
    private String balance;
    private Date birth;

    public User() {
    }

    public User(String user, String password, String balance, Date birth) {
        this.user = user;
        this.password = password;
        this.balance = balance;
        this.birth = birth;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public Date getBirth() {
        return birth;
    }

    public void setBirth(Date birth) {
        this.birth = birth;
    }

    @Override
    public String toString() {
        return "User{" +
                "user='" + user + '\'' +
                ", password='" + password + '\'' +
                ", balance='" + balance + '\'' +
                ", birth=" + birth +
                '}';
    }
}
