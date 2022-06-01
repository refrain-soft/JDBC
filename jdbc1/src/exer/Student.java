package exer;

/**
 * @author shkstart
 * @create 2022-05-26-19:01
 */
public class Student {
    private int FlowID;
    private int Type;
    private String IDCard;
    private String ExamCard;
    private String name;
    private String Location;
    private int Grade;

    public Student() {
    }

    public Student(int flowID, int type, String IDCard, String examCard, String name, String location, int grade) {
        FlowID = flowID;
        Type = type;
        this.IDCard = IDCard;
        ExamCard = examCard;
        this.name = name;
        Location = location;
        Grade = grade;
    }

    public int getFlowID() {
        return FlowID;
    }

    public void setFlowID(int flowID) {
        FlowID = flowID;
    }

    public int getType() {
        return Type;
    }

    public void setType(int type) {
        Type = type;
    }

    public String getIDCard() {
        return IDCard;
    }

    public void setIDCard(String IDCard) {
        this.IDCard = IDCard;
    }

    public String getExamCard() {
        return ExamCard;
    }

    public void setExamCard(String examCard) {
        ExamCard = examCard;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public int getGrade() {
        return Grade;
    }

    public void setGrade(int grade) {
        Grade = grade;
    }

    @Override
    public String toString() {
        System.out.println("==========查询结果=========");
        return "Student{" + '\n' +
                "FlowID=" + FlowID + '\n'+
                ", Type=" + Type + '\n'+
                ", IDCard='" + IDCard + '\n' +
                ", ExamCard='" + ExamCard + '\n' +
                ", name='" + name + '\n' +
                ", Location='" + Location + '\n' +
                ", Grade=" + Grade +
                '}';
    }
}
