package comp3111.examsystem.entity;

public class ExamResult extends Entity {
    private String studentID;
    private String examID;
    private int score;
    private int totalScore;
    private String passStatus;

    public ExamResult(String id, String studentID, String examID, int score, int totalScore, String passStatus) {
        this.setId(id);
        this.studentID = studentID;
        this.examID = examID;
        this.score = score;
        this.totalScore = totalScore;
        this.passStatus = passStatus;
    }

    public String getStudentID() { return studentID; }
    public void setStudentID(String studentID) { this.studentID = studentID; }

    public String getExamID() { return examID; }
    public void setExamID(String examID) { this.examID = examID; }

    public int getScore() { return score; }
    public void setScore(int score) { this.score = score; }

    public int getTotalScore() { return totalScore; }
    public void setTotalScore(int totalScore) { this.totalScore = totalScore; }

    public String getPassStatus() { return passStatus; }
    public void setPassStatus(String passStatus) { this.passStatus = passStatus; }

    @Override
    public String toString() {
        return "ExamResult{" +
                "id=" + getId() +
                ", studentID='" + studentID + '\'' +
                ", examID='" + examID + '\'' +
                ", score=" + score +
                ", totalScore=" + totalScore +
                ", passStatus='" + passStatus + '\'' +
                '}';
    }
}
