package app.bean;

/**
 * @Author zmy
 * 2017/10/26.
 */
public class MessageResult {
    private String statusCode;
    private String message;
    private String report;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public MessageResult(String statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }

    public MessageResult() {
    }

    public String getReport() {
        return report;
    }

    public void setReport(String report) {
        this.report = report;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    @Override
    public String toString() {
        return "MessageResult{" +
                "statusCode='" + statusCode + '\'' +
                ", message='" + message + '\'' +
                ", report='" + report + '\'' +
                '}';
    }
}
