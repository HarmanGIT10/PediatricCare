package pediatriccare;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class DataStore {
    private final Map<String, User> users = new LinkedHashMap<>();
    private final List<Patient> patients = new ArrayList<>();
    private final List<Visit> visits = new ArrayList<>();
    private final List<Prescription> prescriptions = new ArrayList<>();
    private final List<Appointment> appointments = new ArrayList<>();
    private final List<InventoryItem> inventory = new ArrayList<>();
    private final List<ChatMessage> messages = new ArrayList<>();
    private final List<String> systemLogs = new ArrayList<>();

    public DataStore() {
        try { ClinicDatabase.get().ensureUniversalAdmin(); } catch (Exception e) { throw new IllegalStateException(e); }
        seedUsers();
        seedPatients();
        seedAppointments();
        seedVisits();
        seedPrescriptions();
        seedInventory();
        seedMessages();
        seedSystemLogs();
    }

    private void seedUsers() {
        users.put("patient", new User("patient", "patient123", "PATIENT", "Sagar Cheema", "PT-00184"));
        users.put("nurse", new User("nurse", "nurse123", "NURSE", "Olivia Park, RN", "NR-0042"));
        users.put("doctor", new User("doctor", "doctor123", "DOCTOR", "Dr. Alan", "DR-0118"));
        users.put("pharmacy", new User("pharmacy", "pharm123", "PHARMACY", "James Miller, RPh", "PH-004"));
        users.put("admin", new User("admin", "admin123", "ADMIN", "Administrator", "AD-001"));
        // Additional mock accounts
        users.put("harman", new User("harman", "pass123", "PATIENT", "Harman", "PT-00185"));
        users.put("aish", new User("aish", "pass123", "PATIENT", "Aish", "PT-00186"));
    }

    private void seedPatients() {
        Patient sagar = new Patient("PT-00184", "Sagar", "Cheema", LocalDate.of(2018, 5, 12), "Male",
                "neek@gmail.com", "9813973231", "Jane Cheema", "Father",
                "Blue Cross Blue Shield", "BCBS-4827106", "CVS Pharmacy", "penicillin", "patient");
        Patient harman = new Patient("PT-00185", "Harman", "Singh", LocalDate.of(2021, 3, 10), "Male",
                "harman.family@gmail.com", "9813971100", "Gurpreet Singh", "Mother",
                "Aetna Health", "AET-993812", "CVS Pharmacy", "None", "harman");
        Patient aish = new Patient("PT-00186", "Aish", "Patel", LocalDate.of(2016, 9, 18), "Female",
                "aish.parents@gmail.com", "9813974422", "Priya Patel", "Mother",
                "UnitedHealthcare", "UHC-771829", "Walgreens", "None", "aish");

        patients.add(sagar);
        patients.add(harman);
        patients.add(aish);
    }

    private void seedAppointments() {
        appointments.add(new Appointment("9:00 AM", 5, "Harman", "Well-child check", "Dr. Ana", "In room", "Done", "Done"));
        appointments.add(new Appointment("10:30 AM", 8, "Sagar", "Follow up", "Dr. Alan", "Waiting", "Pending", "Pending"));
        appointments.add(new Appointment("1:15 PM", 10, "Aish", "Vaccination", "Dr. Aslan", "Scheduled", "Pending", "Pending"));
        appointments.add(new Appointment("2:30 PM", 3, "Lucas Smith", "Ear pain check", "Dr. Alan", "Scheduled", "Pending", "Pending"));
        appointments.add(new Appointment("3:15 PM", 7, "Maya Lin", "Allergy consultation", "Dr. Ana", "Scheduled", "Pending", "Pending"));
        appointments.add(new Appointment("4:00 PM", 12, "Leo Rossi", "Annual physical", "Dr. Aslan", "Scheduled", "Pending", "Pending"));
    }

    private void seedVisits() {
        Patient sagar = patients.get(0);
        // Completed previous visit
        Visit pastVisit1 = new Visit(sagar, "Dr. Ana", "Olivia Park, RN", LocalDate.of(2026, 8, 5), "Annual Checkup");
        pastVisit1.setVitals("25", "127", "98.6", "N/A", "82");
        pastVisit1.setIntakeCompleted(true);
        pastVisit1.setExamination("Well, alert, cooperative", "Clear, normal breath sounds", "Clear bilaterally",
                "Normal, pink", "Healthy growth and development consistent with age.", "Maintain balanced diet and hydration.", "Multivitamins 1 daily");
        visits.add(pastVisit1);

        Visit pastVisit2 = new Visit(sagar, "Dr. Aslan", "Olivia Park, RN", LocalDate.of(2026, 8, 20), "Vaccination");
        pastVisit2.setVitals("25", "128", "98.4", "N/A", "84");
        pastVisit2.setIntakeCompleted(true);
        pastVisit2.setExamination("Alert, smiling", "Clear", "Clear bilaterally", "Normal", "Routine immunization administered.", "Ice pack at injection site if tender.", null);
        visits.add(pastVisit2);

        // Current visit (August 22, 2026)
        Visit currentVisit = new Visit(sagar, "Dr. Alan", "Olivia Park, RN", LocalDate.of(2026, 8, 22), "Annual Checkup / Follow up");
        currentVisit.setVitals("25", "128", "98.4", "N/A", "85");
        currentVisit.setIntakeCompleted(true);
        currentVisit.setExamination("Well, alert, cooperative", "Clear, no wheezing", "Clear bilaterally", "Slightly red, no exudate",
                "Lungs clear, no wheezing. Throat slightly red. Ears clear. Overall healthy growth and development consistent with age. No concerns noted during annual well-child exam.",
                "Drink warm fluids, rest, return if fever develops.",
                "Amoxicillin 250mg - 1 tablet by mouth, twice daily for 7 days, sent to CVS Pharmacy");
        visits.add(currentVisit);
    }

    private void seedPrescriptions() {
        prescriptions.add(new Prescription("RX1001", "Harman", "Amoxicillin 125mg", "1 tsp", "Twice daily", "5 days", "0",
                "Dr. Alan", "CVS Pharmacy", "New", LocalDateTime.of(2026, 8, 20, 9, 30)));
        prescriptions.add(new Prescription("RX1002", "Sagar", "Amoxicillin 250mg", "1 tablet by mouth", "Twice daily", "7 days", "0",
                "Dr. Alan", "CVS Pharmacy", "Ready", LocalDateTime.of(2026, 8, 20, 10, 45)));
        prescriptions.add(new Prescription("RX1003", "Aish", "Paracetamol 250mg", "1 tablet", "As needed", "3 days", "1",
                "Dr. Reena", "Walgreens", "Filled", LocalDateTime.of(2026, 8, 20, 11, 15)));
    }

    private void seedInventory() {
        inventory.add(new InventoryItem("Amoxicillin 250mg", "Antibiotic", 12));
        inventory.add(new InventoryItem("Paracetamol 250mg", "Analgesic", 8));
        inventory.add(new InventoryItem("Ibuprofen 100mg", "Analgesic", 7));
        inventory.add(new InventoryItem("Cetirizine 5mg", "Antihistamine", 24));
        inventory.add(new InventoryItem("Albuterol Inhaler", "Bronchodilator", 15));
    }

    private void seedMessages() {
        messages.add(new ChatMessage("Sagar Cheema", "Dr. Aslan", "Hi Dr. Aslan, please remind us when it's time for Sagar's next flu shot. Thanks", "20/05/26 . 9:10 AM", false));
        messages.add(new ChatMessage("Dr. Aslan", "Sagar Cheema", "Flu shot reminder for Sagar, this Friday at 10 AM. Please make sure he does not have high temperature before the appointment", "23/08/26 . 9:40 AM", true));
        messages.add(new ChatMessage("Dr. Ana", "Sagar Cheema", "Sagar's medication dosage: Take Amoxicillin 250mg twice daily with food.", "21/08/26 . 11:00 AM", true));
    }

    private void seedSystemLogs() {
        systemLogs.add("Database backup completed - 08:30 AM");
        systemLogs.add("System update completed - 07:15 AM");
        systemLogs.add("New staff member registered - Yesterday");
        systemLogs.add("Email Server Synced - 09:00 AM");
    }

    // Authentication
    public User authenticate(String username, String password) {
        if (username == null || password == null) return null;
        try {
            String[] account = ClinicDatabase.get().accountForLogin(username.trim());
            if (account != null && ClinicDatabase.get().authenticate(username.trim(), password, null))
                if (account[1].equals("PATIENT") && patientForUsername(account[0]) == null) {
                    String[] profile = ClinicDatabase.get().patientProfile(account[0]);
                    if (profile != null) patients.add(new Patient(profile[0],profile[1],profile[2],LocalDate.parse(profile[3]),profile[4],profile[5],profile[6],"Parent/Guardian","Parent","Pending Insurance","N/A",profile[7],"None reported",account[0]));
                }
                return new User(account[0], password, account[1], account[2], account[1] + "-DB");
        } catch (Exception ignored) { }
        User user = users.get(username.toLowerCase().trim());
        if (user != null && user.password().equals(password)) {
            return user;
        }
        // Also check by staffId or patient id
        for (User u : users.values()) {
            if ((u.staffId().equalsIgnoreCase(username.trim()) || u.username().equalsIgnoreCase(username.trim()))
                    && u.password().equals(password)) {
                return u;
            }
        }
        return null;
    }

    // Patient lookup
    public Patient findPatient(String first, String last, LocalDate dob) {
        if (first == null || last == null || dob == null) return null;
        return patients.stream().filter(p ->
                p.firstName().equalsIgnoreCase(first.trim())
                && p.lastName().equalsIgnoreCase(last.trim())
                && p.dateOfBirth().equals(dob)).findFirst().orElse(null);
    }

    public Patient findPatientByName(String first, String last) {
        if (first == null) return null;
        return patients.stream().filter(p ->
                p.firstName().equalsIgnoreCase(first.trim())
                && (last == null || last.isBlank() || p.lastName().equalsIgnoreCase(last.trim())))
                .findFirst().orElse(null);
    }

    public Patient patientForUsername(String username) {
        if (username == null) return null;
        return patients.stream().filter(p -> p.username().equalsIgnoreCase(username.trim())).findFirst().orElse(null);
    }

    public List<Patient> allPatients() {
        return Collections.unmodifiableList(patients);
    }

    public Patient registerPatient(String first, String last, LocalDate dob, String gender, String email,
                                   String phone, String pharmacy, String password) {
        return registerPatient(first.toLowerCase().replaceAll("[^a-z0-9]", "") + (patients.size()+1), first,last,dob,gender,email,phone,pharmacy,password);
    }
    public Patient registerPatient(String username, String first, String last, LocalDate dob, String gender, String email,
                                   String phone, String pharmacy, String password) {
        String id = "PT-" + String.format("%05d", patients.size() + 185);
        Patient p = new Patient(id, first, last, dob, gender, email, phone,
                "Parent/Guardian", "Parent", "Pending Insurance", "N/A", pharmacy, "None reported", username);
        try { ClinicDatabase.get().registerPatient(username, email, password, p.fullName(),id,first,last,dob.toString(),gender,phone,pharmacy); }
        catch (Exception e) { throw new IllegalArgumentException("Could not create account: " + e.getMessage()); }
        patients.add(p);
        users.put(username.toLowerCase(), new User(username, password, "PATIENT", p.fullName(), id));
        return p;
    }


    public String registerHospital(String name,String admin,String email,String password,String phone,String address) {
        try { return ClinicDatabase.get().registerHospital(name,admin,email,password,phone,address); }
        catch(Exception e){ throw new IllegalArgumentException(e.getMessage()); }
    }
    public void createHospitalStaff(String admin,String username,String email,String password,String role,String name,String specialization,String treats) {
        try { ClinicDatabase.get().createStaff(admin,username,email,password,role,name,specialization,treats); }
        catch(Exception e){ throw new IllegalArgumentException(e.getMessage()); }
    }
    public List<String[]> hospitalsDirectory(){ try{return ClinicDatabase.get().hospitals();}catch(Exception e){return List.of();} }
    public List<String[]> doctorsDirectory(String hospital){ try{return ClinicDatabase.get().doctors(hospital);}catch(Exception e){return List.of();} }
    public void bookAppointment(String patient,String hospital,String doctor,String date,String slot,String reason){try{ClinicDatabase.get().book(patient,hospital,doctor,date,slot,reason);}catch(Exception e){throw new IllegalArgumentException(e.getMessage());}}
    public List<String[]> patientBookings(String patient){try{return ClinicDatabase.get().appointmentsForPatient(patient);}catch(Exception e){return List.of();}}
    public List<String[]> hospitalBookings(String admin){try{return ClinicDatabase.get().hospitalAppointments(admin);}catch(Exception e){return List.of();}}
    public void setBookingStatus(String admin,String id,String status){try{ClinicDatabase.get().updateAppointmentStatus(admin,id,status);}catch(Exception e){throw new IllegalArgumentException(e.getMessage());}}
    public List<String[]> hospitalStaff(String admin){try{return ClinicDatabase.get().staffForHospital(admin);}catch(Exception e){return List.of();}}
    public void removeHospitalStaff(String admin,String username){try{ClinicDatabase.get().deactivateStaff(admin,username);}catch(Exception e){throw new IllegalArgumentException(e.getMessage());}}
    public List<String[]> platformAccounts(){try{return ClinicDatabase.get().platformAccounts();}catch(Exception e){return List.of();}}
    public List<String[]> platformAppointments(){try{return ClinicDatabase.get().platformAppointments();}catch(Exception e){return List.of();}}
    public String[] hospitalSummary(String admin){try{return ClinicDatabase.get().hospitalSummary(admin);}catch(Exception e){throw new IllegalArgumentException(e.getMessage());}}
    public String[] hospitalDetails(String admin){try{return ClinicDatabase.get().hospitalDetails(admin);}catch(Exception e){throw new IllegalArgumentException(e.getMessage());}}
    public void updateHospitalDetails(String admin,String name,String email,String phone,String address){try{ClinicDatabase.get().updateHospitalDetails(admin,name,email,phone,address);}catch(Exception e){throw new IllegalArgumentException(e.getMessage());}}
    public List<String[]> hospitalActivity(String admin){try{return ClinicDatabase.get().hospitalActivity(admin);}catch(Exception e){throw new IllegalArgumentException(e.getMessage());}}
    public List<String[]> doctorBookings(String doctor){try{return ClinicDatabase.get().doctorAppointments(doctor);}catch(Exception e){throw new IllegalArgumentException(e.getMessage());}}
    public void completeDoctorAppointment(String doctor,String id,String diagnosis,String notes,String medicine,String dosage,String instructions){try{ClinicDatabase.get().completeAppointment(doctor,id,diagnosis,notes,medicine,dosage,instructions);}catch(Exception e){throw new IllegalArgumentException(e.getMessage());}}
    public List<String[]> medicalRecords(String patient){try{return ClinicDatabase.get().patientRecords(patient);}catch(Exception e){throw new IllegalArgumentException(e.getMessage());}}
    public List<String[]> staffPatientRecords(String staff,String patient,String role){try{return ClinicDatabase.get().staffPatientRecords(staff,patient,role);}catch(Exception e){throw new IllegalArgumentException(e.getMessage());}}
    public List<String[]> messageContacts(String username){try{return ClinicDatabase.get().contacts(username);}catch(Exception e){throw new IllegalArgumentException(e.getMessage());}}
    public void linkPatientHospital(String patient,String hospital){try{ClinicDatabase.get().linkPatientHospital(patient,hospital);}catch(Exception e){throw new IllegalArgumentException(e.getMessage());}}
    public List<String[]> messageContacts(String username,String hospital){try{return ClinicDatabase.get().contactsForHospital(username,hospital);}catch(Exception e){throw new IllegalArgumentException(e.getMessage());}}
    public List<String[]> conversation(String username,String other){try{return ClinicDatabase.get().messages(username,other);}catch(Exception e){throw new IllegalArgumentException(e.getMessage());}}
    public void sendPersistentMessage(String sender,String receiver,String body){try{ClinicDatabase.get().sendMessage(sender,receiver,body);}catch(Exception e){throw new IllegalArgumentException(e.getMessage());}}
    public List<String[]> notifications(String username){try{return ClinicDatabase.get().notifications(username);}catch(Exception e){throw new IllegalArgumentException(e.getMessage());}}
    public List<String[]> pharmacyQueue(String username){try{return ClinicDatabase.get().pharmacyPrescriptions(username);}catch(Exception e){throw new IllegalArgumentException(e.getMessage());}}
    public void updateRx(String username,String id,String status){try{ClinicDatabase.get().updatePrescription(username,id,status);}catch(Exception e){throw new IllegalArgumentException(e.getMessage());}}
    public List<String[]> pharmacyInventory(String username){try{return ClinicDatabase.get().pharmacyInventory(username);}catch(Exception e){throw new IllegalArgumentException(e.getMessage());}}
    public void savePharmacyStock(String username,String medicine,String category,int quantity){try{ClinicDatabase.get().savePharmacyStock(username,medicine,category,quantity);}catch(Exception e){throw new IllegalArgumentException(e.getMessage());}}
    public List<String[]> nurseBookings(String username){try{return ClinicDatabase.get().nurseAppointments(username);}catch(Exception e){throw new IllegalArgumentException(e.getMessage());}}
    public void saveNurseVitals(String username,String id,String temp,String pulse,String weight,String notes){try{ClinicDatabase.get().saveNursingNote(username,id,temp,pulse,weight,notes);}catch(Exception e){throw new IllegalArgumentException(e.getMessage());}}


    // Visits
    public Visit getOrCreateVisit(Patient patient, String staffUsername) {
        for (Visit v : visits) {
            if (v.patient().equals(patient) && v.date().equals(LocalDate.now())) {
                return v;
            }
        }
        Visit v = new Visit(patient, "Dr. Alan", staffUsername, LocalDate.now(), "Consultation");
        visits.add(v);
        return v;
    }

    public List<Visit> visitsForPatient(Patient patient) {
        return visits.stream().filter(v -> v.patient().equals(patient)).toList();
    }

    public List<String> visitSummaries(Patient patient) {
        return visits.stream().filter(v -> v.patient().equals(patient)).map(Visit::summary).toList();
    }

    public List<String> allVisitSummaries() {
        return visits.stream().map(Visit::summary).toList();
    }

    public List<Visit> allVisits() {
        return Collections.unmodifiableList(visits);
    }

    // Prescriptions
    public Prescription addPrescription(Patient patient, String medicine, String dosage, String doctor) {
        return addPrescription(patient.fullName(), medicine, dosage, "Twice daily", "7 days", "0", doctor, patient.preferredPharmacy());
    }

    public Prescription addPrescription(String patientName, String medicine, String dosage,
                                        String frequency, String duration, String refills,
                                        String doctor, String pharmacy) {
        String id = "RX" + (1000 + prescriptions.size() + 1);
        Prescription rx = new Prescription(id, patientName, medicine, dosage, frequency, duration, refills,
                doctor, pharmacy, "New", LocalDateTime.now());
        prescriptions.add(rx);
        return rx;
    }

    public List<Prescription> allPrescriptions() {
        return Collections.unmodifiableList(prescriptions);
    }

    public List<String> prescriptionSummaries() {
        return prescriptions.stream().map(Prescription::summary).toList();
    }

    // Appointments Queue
    public List<Appointment> appointments() {
        return appointments;
    }

    // Inventory
    public List<InventoryItem> inventory() {
        return inventory;
    }

    // Messaging
    public List<ChatMessage> messages() {
        return messages;
    }

    public void sendMessage(String sender, String recipient, String content, boolean isStaff) {
        String ts = LocalDateTime.now().toLocalDate() + " . "
                + String.format("%02d:%02d", LocalDateTime.now().getHour(), LocalDateTime.now().getMinute());
        messages.add(new ChatMessage(sender, recipient, content, ts, isStaff));
    }

    // Admin Users
    public boolean createUser(String username, String password, String role) {
        return createUser(username, password, role, username);
    }

    public boolean createUser(String username, String password, String role, String displayName) {
        String key = username.toLowerCase().trim();
        if (users.containsKey(key)) return false;
        String idPrefix = switch (role.toUpperCase()) {
            case "NURSE" -> "NR-";
            case "DOCTOR" -> "DR-";
            case "PHARMACY" -> "PH-";
            case "PATIENT" -> "PT-";
            default -> "AD-";
        };
        String staffId = idPrefix + String.format("%04d", users.size() + 10);
        users.put(key, new User(username, password, role.toUpperCase(), displayName, staffId));
        return true;
    }

    public List<User> allUsers() {
        return new ArrayList<>(users.values());
    }

    public List<String> userSummaries() {
        return users.values().stream().map(u -> u.staffId() + "  |  " + u.displayName() + " (" + u.username() + ")  |  " + u.role()).toList();
    }

    public List<String> systemLogs() {
        return systemLogs;
    }
}
