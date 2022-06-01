package Bean;

import java.sql.Date;

/**
 * @author shkstart
 * @create 2022-05-25-15:35
 */

/*
ORM(object relational mapping)编程思想: 对象关系映射
  java       与         sql 对应数据类型转换表
boolean                BIT
byte                   TINYINT
short                  SMALLINT
int                    INTEGER
long                   BIGINT
String                 CHAR, VARCHAR, LONGVARCHAR
byte array             BINARY, VAR BINARY
java.sql.Date          DATE
java.sql.Time          TIME
java.sql.Timestamp     TIMESTAMP


一个数据表对应一个Java类
表中的一条记录对应Java类的一个对象
表中的一个字段对应java类中的一个属性


 */
public class user_table {

     private String user;
     private String password;
     private String balance;
     private Date birth;

    public user_table() {
    }

    public user_table(String user, String password, String balance, Date birth) {
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
        return "user_table{" +
                "user='" + user + '\'' +
                ", password='" + password + '\'' +
                ", balance='" + balance + '\'' +
                ", birth=" + birth +
                '}';
    }
}
