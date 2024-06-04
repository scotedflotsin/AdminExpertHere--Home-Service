package www.experthere.adminexperthere.dataModel;

public class StatisticsResponse {


    boolean success;
    String message;

    Statistics statistics;

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public Statistics getStatistics() {
        return statistics;
    }
}
