package pediatriccare;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Visit {
    private final Patient patient;
    private final String doctorName;
    private final String nurseName;
    private final LocalDate date;
    private final String reason;

    // Vitals
    private String weightKg = "25";
    private String heightCm = "128";
    private String bodyTempF = "98.4";
    private String bloodPressure = "N/A";
    private String pulseBpm = "85";
    private boolean vitalsRecorded = false;

    // Intake Notes
    private String nurseNotes = "Parent reports mild cough over the past 3 days, no fever at home. Child has been eating and sleeping normally. No known exposures. Up to date immunization. Next HPV dose not due until age 11.";
    private String allergies = "penicillin";
    private boolean intakeCompleted = false;

    // Doctor Exam
    private String generalAppearance = "Well, alert, cooperative";
    private String respiratoryLungs = "Clear, no wheezing";
    private String earsOtoscope = "Clear bilaterally";
    private String throatOral = "Slightly red, no exudate";
    private String examNotes = "Lungs clear, no wheezing. Throat slightly red. Ears clear. Overall healthy growth and development consistent with age. No concerns noted during annual well-child exam.";
    private String recommendations = "Adequate rest, hydration, finish full antibiotic course as directed.";
    private String prescriptionSummary = "Amoxicillin 250mg - 1 tablet by mouth, twice daily for 7 days, sent to CVS Pharmacy";
    private boolean examCompleted = false;

    public Visit(Patient patient, String doctorName, String nurseName, LocalDate date, String reason) {
        this.patient = patient;
        this.doctorName = doctorName;
        this.nurseName = nurseName;
        this.date = date;
        this.reason = reason;
    }

    public Visit(Patient patient, String staffUsername) {
        this(patient, "Dr. Alan", staffUsername, LocalDate.now(), "Follow up / Checkup");
    }

    public Patient patient() { return patient; }
    public String doctorName() { return doctorName; }
    public String nurseName() { return nurseName; }
    public LocalDate date() { return date; }
    public String reason() { return reason; }

    public String weightKg() { return weightKg; }
    public String heightCm() { return heightCm; }
    public String bodyTempF() { return bodyTempF; }
    public String bloodPressure() { return bloodPressure; }
    public String pulseBpm() { return pulseBpm; }
    public boolean vitalsRecorded() { return vitalsRecorded; }

    public void setVitals(String weight, String height, String temp, String bp, String pulse) {
        this.weightKg = weight;
        this.heightCm = height;
        this.bodyTempF = temp;
        this.bloodPressure = bp;
        this.pulseBpm = pulse;
        this.vitalsRecorded = true;
    }

    public void setVitals(double temperature, int pulse, double weight, double height, String bloodPressure) {
        setVitals(String.valueOf(weight), String.valueOf(height), String.valueOf(temperature), bloodPressure, String.valueOf(pulse));
    }

    public String nurseNotes() { return nurseNotes; }
    public void setNurseNotes(String notes) { this.nurseNotes = notes; }
    public String allergies() { return allergies; }
    public void setAllergies(String allergies) { this.allergies = allergies; }
    public boolean intakeCompleted() { return intakeCompleted; }
    public void setIntakeCompleted(boolean completed) { this.intakeCompleted = completed; }

    public String generalAppearance() { return generalAppearance; }
    public String respiratoryLungs() { return respiratoryLungs; }
    public String earsOtoscope() { return earsOtoscope; }
    public String throatOral() { return throatOral; }
    public String examNotes() { return examNotes; }
    public String recommendations() { return recommendations; }
    public String prescriptionSummary() { return prescriptionSummary; }
    public boolean examCompleted() { return examCompleted; }

    public void setDiagnosis(String diagnosis) {
        this.examNotes = diagnosis;
        this.examCompleted = true;
    }

    public void setExamination(String general, String respiratory, String ears, String throat, String notes, String recommendations, String rxSummary) {
        this.generalAppearance = general;
        this.respiratoryLungs = respiratory;
        this.earsOtoscope = ears;
        this.throatOral = throat;
        this.examNotes = notes;
        this.recommendations = recommendations;
        if (rxSummary != null && !rxSummary.isBlank()) {
            this.prescriptionSummary = rxSummary;
        }
        this.examCompleted = true;
    }

    public String summary() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yy");
        return date.format(fmt) + " | " + patient.fullName()
                + " | Dr. " + doctorName + " | " + reason
                + " | Vitals: " + (vitalsRecorded ? (weightKg + "kg, " + bodyTempF + "°F, BP: " + bloodPressure) : "Pending")
                + " | Exam: " + (examCompleted ? "Completed" : "Pending");
    }
}
