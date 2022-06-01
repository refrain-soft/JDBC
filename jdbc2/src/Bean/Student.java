package Bean;

import java.util.Date;

/**
 * @author shkstart
 * @create 2022-05-25-18:21
 */
public class Student {
    private int id;
    private String name;
    private Date birth;
    private float grade;


    public Student() {
    }

    public Student(int id, String name, Date birth, float grade) {
        this.id = id;
        this.name = name;
        this.birth = birth;
        this.grade = grade;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getGrade() {
        return grade;
    }

    public void setGrade(float grade) {
        this.grade = grade;
    }

    public Date getBirth() {
        return birth;
    }

    public void setBirth(Date birth) {
        this.birth = birth;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", birth=" + birth +
                ", grade=" + grade +
                '}';
    }
}
