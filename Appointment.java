package pediatriccare;

public class Appointment {
    private String time;
    private int age;
    private String patientName;
    private String reason;
    private String doctor;
    private String status; // "In room", "Waiting", "Scheduled", "Checked in", "With doctor"
    private String vitalsStatus; // "Done", "Pending"
    private String intakeStatus; // "Done", "Pending", "Ready"

    public Appointment(String time, int age, String patientName, String reason, String doctor,
                       String status, String vitalsStatus, String intakeStatus) {
        this.time = time;
        this.age = age;
        this.patientName = patientName;
        this.reason = reason;
        this.doctor = doctor;
        this.status = status;
        this.vitalsStatus = vitalsStatus;
        this.intakeStatus = intakeStatus;
    }

    public String time() { return time; }
    public int age() { return age; }
    public String patientName() { return patientName; }
    public String reason() { return reason; }
    public String doctor() { return doctor; }
    public String status() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String vitalsStatus() { return vitalsStatus; }
    public void setVitalsStatus(String vitalsStatus) { this.vitalsStatus = vitalsStatus; }
    public String intakeStatus() { return intakeStatus; }
    public void setIntakeStatus(String intakeStatus) { this.intakeStatus = intakeStatus; }
}

