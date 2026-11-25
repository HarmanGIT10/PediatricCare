package pediatriccare;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Prescription {
    private final String id;
    private final String patientName;
    private final String medication;
    private final String dosage;
    private final String frequency;
    private final String duration;
    private final String refills;
    private final String prescribedBy;
    private final String pharmacy;
    private String status; // "New", "Ready", "Filled"
    private final LocalDateTime prescribedAt;

    public Prescription(String id, String patientName, String medication, String dosage,
                        String frequency, String duration, String refills,
                        String prescribedBy, String pharmacy, String status, LocalDateTime prescribedAt) {
        this.id = id;
        this.patientName = patientName;
        this.medication = medication;
        this.dosage = dosage;
        this.frequency = frequency;
        this.duration = duration;
        this.refills = refills;
        this.prescribedBy = prescribedBy;
        this.pharmacy = pharmacy;
        this.status = status;
        this.prescribedAt = prescribedAt;
    }

    public Prescription(String patientName, String medicine, String dosage, String prescribedBy, LocalDateTime prescribedAt) {
        this("RX" + (1000 + (int)(Math.random() * 9000)), patientName, medicine, dosage, "Twice daily", "7 days", "0",
             prescribedBy, "CVS Pharmacy", "New", prescribedAt);
    }

    public String id() { return id; }
    public String patientName() { return patientName; }
    public String medication() { return medication; }
    public String medicine() { return medication; }
    public String dosage() { return dosage; }
    public String frequency() { return frequency; }
    public String duration() { return duration; }
    public String refills() { return refills; }
    public String prescribedBy() { return prescribedBy; }
    public String pharmacy() { return pharmacy; }
    public String status() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime prescribedAt() { return prescribedAt; }

    public String instructions() {
        StringBuilder sb = new StringBuilder();
        if (dosage != null && !dosage.isBlank()) sb.append(dosage);
        if (frequency != null && !frequency.isBlank()) {
            if (sb.length() > 0) sb.append(", ");
            sb.append(frequency);
        }
        if (duration != null && !duration.isBlank()) {
            if (sb.length() > 0) sb.append(", for ");
            sb.append(duration);
        }
        if (pharmacy != null && !pharmacy.isBlank()) {
            if (sb.length() > 0) sb.append(", sent to ");
            sb.append(pharmacy);
        }
        return sb.toString();
    }

    public String summary() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yy");
        return id + " | " + patientName + " | " + medication + " | " + dosage
                + " | Dr. " + prescribedBy + " | " + prescribedAt.format(fmt) + " | Status: " + status;
    }
}
