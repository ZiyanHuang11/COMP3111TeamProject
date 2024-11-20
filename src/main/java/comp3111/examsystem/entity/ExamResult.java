package comp3111.examsystem.entity;

public class ExamResult {
    private String courseID;   // 课程代码
    private String examName;   // 考试名称
    private int totalScore;    // 总分
    private int score;         // 得分
    private String passStatus; // 是否通过

    public ExamResult(String courseID, String examName, int totalScore, int score, String passStatus) {
        this.courseID = courseID;
        this.examName = examName;
        this.totalScore = totalScore;
        this.score = score;
        this.passStatus = passStatus;
    }

    public String getCourseID() {
        return courseID;
    }

    public String getExamName() {
        return examName;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public int getScore() {
        return score;
    }

    public String getPassStatus() {
        return passStatus;
    }
}
