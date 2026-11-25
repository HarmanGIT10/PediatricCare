package pediatriccare;

import java.time.LocalDate;
import java.time.Period;

public class Patient {
    private String id;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String gender;
    private String email;
    private String phone;
    private String guardianName;
    private String guardianRelationship;
    private String insuranceProvider;
    private String insurancePolicy;
    private String preferredPharmacy;
    private String allergies;
    private String username;

    public Patient(String id, String firstName, String lastName, LocalDate dateOfBirth, String gender,
                   String email, String phone, String guardianName, String guardianRelationship,
                   String insuranceProvider, String insurancePolicy, String preferredPharmacy,
                   String allergies, String username) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.email = email;
        this.phone = phone;
        this.guardianName = guardianName;
        this.guardianRelationship = guardianRelationship;
        this.insuranceProvider = insuranceProvider;
        this.insurancePolicy = insurancePolicy;
        this.preferredPharmacy = preferredPharmacy;
        this.allergies = allergies;
        this.username = username;
    }

    public Patient(String firstName, String lastName, LocalDate dateOfBirth, String username) {
        this("PT-00184", firstName, lastName, dateOfBirth, "Male", "neek@gmail.com", "9813973231",
             "Jane Cheema", "Father", "Blue Cross Blue Shield", "BCBS-4827106", "CVS Pharmacy", "penicillin", username);
    }

    public String id() { return id; }
    public String firstName() { return firstName; }
    public String lastName() { return lastName; }
    public LocalDate dateOfBirth() { return dateOfBirth; }
    public String gender() { return gender; }
    public String email() { return email; }
    public String phone() { return phone; }
    public String guardianName() { return guardianName; }
    public String guardianRelationship() { return guardianRelationship; }
    public String insuranceProvider() { return insuranceProvider; }
    public String insurancePolicy() { return insurancePolicy; }
    public String preferredPharmacy() { return preferredPharmacy; }
    public String allergies() { return allergies; }
    public String username() { return username; }

    public void setContactInfo(String firstName, String lastName, String guardianName, String relationship, String email, String phone) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.guardianName = guardianName;
        this.guardianRelationship = relationship;
        this.email = email;
        this.phone = phone;
    }

    public void setInsurance(String provider, String policy) {
        this.insuranceProvider = provider;
        this.insurancePolicy = policy;
    }

    public void setPreferredPharmacy(String pharmacy) {
        this.preferredPharmacy = pharmacy;
    }

    public void setAllergies(String allergies) {
        this.allergies = allergies;
    }

    public String fullName() { return firstName + " " + lastName; }
    public String initials() {
        String f = firstName == null || firstName.isEmpty() ? "" : firstName.substring(0, 1).toUpperCase();
        String l = lastName == null || lastName.isEmpty() ? "" : lastName.substring(0, 1).toUpperCase();
        return f + l;
    }
    public int age() {
        if (dateOfBirth == null) return 0;
        return Period.between(dateOfBirth, LocalDate.now()).getYears();
    }
    public String summary() {
        return fullName() + " | DOB: " + dateOfBirth + " | Age: " + age() + " | ID: " + id;
    }
}
