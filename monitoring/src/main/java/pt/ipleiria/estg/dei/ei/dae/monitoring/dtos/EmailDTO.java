package pt.ipleiria.estg.dei.ei.dae.monitoring.dtos;
public class EmailDTO {
    private String subject;
    private String body;

    // Constructor
    public EmailDTO(String subject, String body) {
        this.subject = subject;
        this.body = body;
    }

    // Default constructor (if needed)
    public EmailDTO() {}


    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "EmailDTO{" +
                "subject='" + subject + '\'' +
                ", body='" + body + '\'' +
                '}';
    }
}

