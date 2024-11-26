package comp3111.examsystem.entity;

public class GradeRecord {
    private final String course;
    private final String exam;
    private final int score;
    private final int fullScore;
    private final int time; // 用时（秒）

    public GradeRecord(String course, String exam, int score, int fullScore, int time) {
        this.course = course;
        this.exam = exam;
        this.score = score;
        this.fullScore = fullScore;
        this.time = time;
    }

    public String getCourse() {
        return course;
    }

    public String getExam() {
        return exam;
    }

    public int getScore() {
        return score;
    }

    public int getFullScore() {
        return fullScore;
    }

    public int getTime() {
        return time;
    }
}
