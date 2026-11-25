package pediatriccare;

import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class PediatricCareApp extends Application {
    private Stage stage;
    private final DataStore store = new DataStore();
    private User currentUser;
    private Patient selectedPatient;
    private BorderPane mainLayout;
    private VBox contentContainer;
    private VBox sidebarNav;
    private String activeNav = "";

    @Override
    public void start(Stage stage) {
        this.stage = stage;
        stage.setTitle("PediatricCare – Office Automation System");
        // Default selected patient to Sagar Cheema
        selectedPatient = store.findPatientByName("Sagar", "Cheema");
        showLogin("Patient");
        stage.show();
    }

    // =========================================================================
    // 1. SIGN IN SCREEN (Figures 1, 7, 10)
    // =========================================================================
    private void showLogin(String initialRole) {
        VBox root = new VBox();
        root.setStyle("-fx-background-color: #20557c;");

        // Window Title & Menu Bar
        root.getChildren().add(UIHelper.createAppHeader("PediatricCare – Office Automation System"));

        // Central Login Card
        VBox card = new VBox(14);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(26, 32, 26, 32));
        card.setMaxWidth(420);
        card.setStyle("-fx-background-color: white; -fx-background-radius: 16; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.25), 16, 0, 0, 6);");

        // Centered Brand Logo
        Node logo = LogoUtil.createLogo(76);

        Label title = new Label("Pediatric Care");
        title.setStyle("-fx-font-size: 26px; -fx-font-weight: bold; -fx-text-fill: #19446c;");

        Label subtitle = new Label("Office Automation System");
        subtitle.setStyle("-fx-font-size: 13px; -fx-text-fill: #64748b;");

        // Role Selector Pills (Patient, Nurse, Doctor, Pharmacy, Admin)
        HBox rolePills = new HBox(4);
        rolePills.setAlignment(Pos.CENTER);
        rolePills.setPadding(new Insets(4));
        rolePills.setStyle("-fx-background-color: #e2e8f0; -fx-background-radius: 20;");

        String[] roles = new String[] { "Patient", "Hospital" };
        Button[] pillButtons = new Button[roles.length];

        TextField usernameField = new TextField();
        usernameField.setStyle("-fx-background-radius: 10; -fx-padding: 9; -fx-background-color: #f1f5f9; -fx-border-color: #cbd5e1; -fx-border-radius: 10;");

        PasswordField passwordField = new PasswordField();
        passwordField.setStyle("-fx-background-radius: 10; -fx-padding: 9; -fx-background-color: #f1f5f9; -fx-border-color: #cbd5e1; -fx-border-radius: 10;");

        Label userLabel = new Label("Email Address");
        userLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #1e293b; -fx-font-size: 12px;");

        Label passLabel = new Label("Password");
        passLabel.setStyle("-fx-font-weight: bold; -fx-text-fill: #1e293b; -fx-font-size: 12px;");

        Label message = new Label();
        message.setStyle("-fx-text-fill: #dc2626; -fx-font-size: 12px; -fx-font-weight: bold;");

        Label footerNote = new Label("Authorized personnel only");
        footerNote.setStyle("-fx-font-size: 11px; -fx-text-fill: #64748b;");

        Hyperlink createAccountLink = new Hyperlink("Patient sign up");
        createAccountLink.setStyle("-fx-text-fill: #1b5a8c; -fx-font-weight: bold; -fx-font-size: 12px;");
        createAccountLink.setOnAction(e -> showCreateAccount());

        Hyperlink hospitalSignup = new Hyperlink("Hospital sign up"); hospitalSignup.setOnAction(e -> showHospitalRegistration());
        HBox newPatientRow = new HBox(8, new Label("New here?"), createAccountLink, new Label("·"), hospitalSignup);
        newPatientRow.setAlignment(Pos.CENTER);
        ((Label)newPatientRow.getChildren().get(0)).setStyle("-fx-font-size: 12px; -fx-text-fill: #475569;");

        final String[] currentRoleSelection = new String[] { initialRole };

        Runnable updateRoleUI = () -> {
            String r = currentRoleSelection[0];
            for (int i = 0; i < roles.length; i++) {
                if (roles[i].equalsIgnoreCase(r)) {
                    pillButtons[i].setStyle("-fx-background-color: white; -fx-text-fill: #15456f; -fx-font-weight: bold; -fx-background-radius: 16; -fx-padding: 5 12 5 12;");
                } else {
                    pillButtons[i].setStyle("-fx-background-color: transparent; -fx-text-fill: #475569; -fx-font-weight: normal; -fx-padding: 5 12 5 12;");
                }
            }
            if (r.equalsIgnoreCase("Patient")) {
                subtitle.setText("Office Automation System");
                userLabel.setText("Email Address / Username");
                usernameField.setPromptText("neek@gmail.com or patient");
                usernameField.clear(); passwordField.clear();
                footerNote.setVisible(false);
                footerNote.setManaged(false);
                createAccountLink.setVisible(true); hospitalSignup.setVisible(true);
                newPatientRow.setVisible(true);
                newPatientRow.setManaged(true);
            } else {
                subtitle.setText("Staff Sign In");
                userLabel.setText("Hospital email / username");
                footerNote.setVisible(true);
                footerNote.setManaged(true);
                createAccountLink.setVisible(false); hospitalSignup.setVisible(true);
                newPatientRow.setVisible(true);
                newPatientRow.setManaged(false);

                usernameField.setPromptText("Work email or username");
                usernameField.clear(); passwordField.clear();
            }
        };

        for (int i = 0; i < roles.length; i++) {
            final String roleName = roles[i];
            Button pill = new Button(roleName);
            pillButtons[i] = pill;
            pill.setOnAction(e -> {
                currentRoleSelection[0] = roleName;
                updateRoleUI.run();
            });
            rolePills.getChildren().add(pill);
        }

        updateRoleUI.run();

        Button signInBtn = new Button("Sign in");
        signInBtn.setDefaultButton(true);
        signInBtn.setMaxWidth(Double.MAX_VALUE);
        signInBtn.setStyle("-fx-background-color: #1a608f; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px; -fx-background-radius: 20; -fx-padding: 9; -fx-cursor: hand;");
        signInBtn.setOnAction(e -> {
            String u = usernameField.getText().trim();
            String p = passwordField.getText();
            User authenticated = store.authenticate(u, p);
            if (authenticated == null) { message.setText("Invalid email/username or password."); return; }
            boolean patientMode = currentRoleSelection[0].equalsIgnoreCase("Patient");
            if (patientMode && !List.of("PATIENT", "SUPERADMIN").contains(authenticated.role())) { message.setText("Use Hospital Login for staff and hospital administrators."); return; }
            if (!patientMode && List.of("PATIENT", "SUPERADMIN").contains(authenticated.role())) { message.setText("Use Patient Login for patient and platform-admin access."); return; }
            currentUser = authenticated;
            if (currentUser.role().equals("PATIENT")) {
                Patient pat = store.patientForUsername(currentUser.username());
                if (pat != null) selectedPatient = pat;
            }
            showDashboard();
        });

        VBox userBox = new VBox(4, userLabel, usernameField);
        userBox.setAlignment(Pos.CENTER_LEFT);

        VBox passBox = new VBox(4, passLabel, passwordField);
        passBox.setAlignment(Pos.CENTER_LEFT);

        card.getChildren().addAll(logo, title, subtitle, rolePills, userBox, passBox, signInBtn, message, footerNote, newPatientRow);

        StackPane centerContainer = new StackPane(card);
        centerContainer.setPadding(new Insets(20));
        VBox.setVgrow(centerContainer, Priority.ALWAYS);
        root.getChildren().add(centerContainer);

        stage.setScene(createStyledScene(root, 960, 680));
    }

    // =========================================================================
    // 2. CREATE PATIENT ACCOUNT SCREEN (Figure 2)
    // =========================================================================
    private void showCreateAccount() {
        VBox root = new VBox();
        root.setStyle("-fx-background-color: #20557c;");
        root.getChildren().add(UIHelper.createAppHeader("PediatricCare – Office Automation System"));

        VBox card = new VBox(12);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(24, 32, 24, 32));
        card.setMaxWidth(560);
        card.setStyle("-fx-background-color: white; -fx-background-radius: 16; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.25), 16, 0, 0, 6);");

        Node logo = LogoUtil.createLogo(68);

        Label title = new Label("Create Patient Account");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #19446c;");

        Label subtitle = new Label("Each patient is uniquely identified by first name, last name & date of birth");
        subtitle.setStyle("-fx-font-size: 11px; -fx-text-fill: #64748b;");

        TextField usernameField = new TextField(); usernameField.setPromptText("Choose username");
        TextField firstField = new TextField(); firstField.setPromptText("Child's first name");
        TextField lastField = new TextField(); lastField.setPromptText("Child's last name");
        DatePicker dobPicker = new DatePicker();
        ComboBox<String> genderCombo = new ComboBox<>();
        genderCombo.getItems().addAll("Male", "Female", "Other");
        genderCombo.setValue("Male");
        genderCombo.setMaxWidth(Double.MAX_VALUE);

        TextField emailField = new TextField(); emailField.setPromptText("Parent / guardian email");
        PasswordField passField = new PasswordField();
        passField.setPromptText("Create password");
        PasswordField confirmPassField = new PasswordField();
        confirmPassField.setPromptText("Confirm password");
        TextField phoneField = new TextField(); phoneField.setPromptText("Phone number");
        ComboBox<String> pharmacyCombo = new ComboBox<>();
        pharmacyCombo.getItems().addAll("CVS Pharmacy", "Walgreens", "Rite Aid", "Walmart Pharmacy");
        pharmacyCombo.setValue("CVS Pharmacy");
        pharmacyCombo.setMaxWidth(Double.MAX_VALUE);

        String inputStyle = "-fx-background-radius: 8; -fx-padding: 7; -fx-background-color: #f1f5f9; -fx-border-color: #cbd5e1; -fx-border-radius: 8;";
        firstField.setStyle(inputStyle);
        lastField.setStyle(inputStyle);
        dobPicker.setStyle(inputStyle);
        genderCombo.setStyle(inputStyle);
        emailField.setStyle(inputStyle);
        passField.setStyle(inputStyle);
        confirmPassField.setStyle(inputStyle);
        phoneField.setStyle(inputStyle);
        pharmacyCombo.setStyle(inputStyle);

        GridPane grid = new GridPane();
        grid.setHgap(14);
        grid.setVgap(10);
        grid.setAlignment(Pos.CENTER);

        grid.add(new Label("Username *"), 0, 0); grid.add(usernameField, 0, 1);
        grid.add(new Label("First Name *"), 0, 2); grid.add(firstField, 0, 3);
        grid.add(new Label("Last Name *"), 1, 2); grid.add(lastField, 1, 3);
        grid.add(new Label("Date of Birth *"), 0, 4); grid.add(dobPicker, 0, 5);
        grid.add(new Label("Gender"), 1, 4); grid.add(genderCombo, 1, 5);
        grid.add(new Label("Email Address *"), 0, 6, 2, 1); grid.add(emailField, 0, 7, 2, 1);
        grid.add(new Label("Password *"), 0, 8); grid.add(passField, 0, 9);
        grid.add(new Label("Confirm Password *"), 1, 8); grid.add(confirmPassField, 1, 9);
        grid.add(new Label("Phone Number"), 0, 10); grid.add(phoneField, 0, 11);
        grid.add(new Label("Preferred Pharmacy"), 1, 10); grid.add(pharmacyCombo, 1, 11);

        Label message = new Label();
        message.setStyle("-fx-font-weight: bold; -fx-font-size: 12px;");

        Button createBtn = new Button("Create Account");
        createBtn.setStyle("-fx-background-color: #1a608f; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8; -fx-padding: 8 18 8 18; -fx-cursor: hand;");

        Button backBtn = new Button("Back to Sign In");
        backBtn.setStyle("-fx-background-color: #e2e8f0; -fx-text-fill: #334155; -fx-font-weight: bold; -fx-background-radius: 8; -fx-padding: 8 18 8 18; -fx-cursor: hand;");
        backBtn.setOnAction(e -> showLogin("Patient"));

        createBtn.setOnAction(e -> {
            String f = firstField.getText().trim();
            String l = lastField.getText().trim();
            LocalDate dob = dobPicker.getValue();
            String p1 = passField.getText();
            String p2 = confirmPassField.getText();
            if (usernameField.getText().trim().length()<3 || f.isEmpty() || l.isEmpty() || dob == null || emailField.getText().trim().isEmpty()) {
                message.setText("Please fill out all required fields (*).");
                message.setStyle("-fx-text-fill: #dc2626; -fx-font-weight: bold;");
                return;
            }
            if (p1.length() < 10) { message.setText("Choose a password with at least 10 characters."); message.setStyle("-fx-text-fill:#dc2626;"); return; }
            if (!emailField.getText().trim().matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$")) { message.setText("Enter a valid email address."); message.setStyle("-fx-text-fill:#dc2626;"); return; }
            if (!p1.equals(p2)) {
                message.setText("Passwords do not match.");
                message.setStyle("-fx-text-fill: #dc2626; -fx-font-weight: bold;");
                return;
            }
            try {
                Patient p = store.registerPatient(usernameField.getText().trim(), f, l, dob, genderCombo.getValue(), emailField.getText().trim(), phoneField.getText().trim(), pharmacyCombo.getValue(), p1);
                selectedPatient = p; currentUser = store.authenticate(emailField.getText().trim(), p1); showDashboard();
            } catch (Exception ex) { message.setText(ex.getMessage()); message.setStyle("-fx-text-fill:#dc2626;"); }
        });

        HBox btnRow = new HBox(12, createBtn, backBtn);
        btnRow.setAlignment(Pos.CENTER);

        card.getChildren().addAll(logo, title, subtitle, grid, message, btnRow);

        ScrollPane scroll = new ScrollPane(new StackPane(card));
        scroll.setFitToWidth(true);
        scroll.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        VBox.setVgrow(scroll, Priority.ALWAYS);
        root.getChildren().add(scroll);

        stage.setScene(createStyledScene(root, 960, 720));
    }

    // =========================================================================
    // 3. DASHBOARD MAIN SHELL (Figures 3, 8, 11, 14, 15)
    // =========================================================================
    private void showDashboard() {
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: #f1f5fa;");
        root.setTop(UIHelper.createAppHeader("PediatricCare – Office Automation System"));

        // Left Navigation Sidebar
        VBox sidebar = new VBox(14);
        sidebar.setPrefWidth(220);
        sidebar.setMinWidth(220);
        sidebar.setPadding(new Insets(18, 14, 18, 14));
        sidebar.setStyle("-fx-background-color: #174e76;");

        // Brand at top of sidebar
        HBox brandBox = new HBox(10);
        brandBox.setAlignment(Pos.CENTER_LEFT);
        Node sideLogo = LogoUtil.createLogo(40);

        String consoleTitle = switch (currentUser.role()) {
            case "PATIENT" -> "Patient Portal";
            case "NURSE" -> "Nurse Console";
            case "DOCTOR" -> "Doctor Console";
            case "ADMIN" -> "Admin Console"; // Fixed from Figure 14 mockup typo
            case "PHARMACY" -> "Pharmacy Console"; // Fixed from Figure 15 mockup typo
            default -> "System Console";
        };

        VBox brandText = new VBox(1);
        Label brandName = new Label("PediatricCare");
        brandName.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 16px;");
        Label consoleLbl = new Label(consoleTitle);
        consoleLbl.setStyle("-fx-text-fill: #93c5fd; -fx-font-size: 11px;");
        brandText.getChildren().addAll(brandName, consoleLbl);
        brandBox.getChildren().addAll(sideLogo, brandText);

        sidebarNav = new VBox(6);
        VBox.setVgrow(sidebarNav, Priority.ALWAYS);

        // Populate sidebar buttons based on role
        setupRoleNavigation(sidebarNav);

        // Bottom User Profile Badge
        VBox userBadge = new VBox(2);
        userBadge.setPadding(new Insets(10, 12, 10, 12));
        userBadge.setStyle("-fx-background-color: #206294; -fx-background-radius: 10;");

        Label badgeName = new Label(currentUser.displayName());
        badgeName.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 12px;");

        String badgeRole = switch (currentUser.role()) {
            case "PATIENT" -> "Patient: Age " + (selectedPatient != null ? selectedPatient.age() : 8);
            case "NURSE" -> "Nurse: " + currentUser.staffId();
            case "DOCTOR" -> "Pediatrician . " + currentUser.staffId();
            case "ADMIN" -> "Administrator . " + currentUser.staffId();
            case "PHARMACY" -> "Pharmacy . " + currentUser.staffId();
            default -> currentUser.role();
        };

        Label badgeSub = new Label(badgeRole);
        badgeSub.setStyle("-fx-text-fill: #bfdbfe; -fx-font-size: 11px;");
        userBadge.getChildren().addAll(badgeName, badgeSub);

        // Logout button
        Button logoutBtn = new Button("Log out");
        logoutBtn.setMaxWidth(Double.MAX_VALUE);
        logoutBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #e2e8f0; -fx-border-color: #94a3b8; -fx-border-radius: 6; -fx-cursor: hand; -fx-font-size: 11px;");
        logoutBtn.setOnAction(e -> {
            currentUser = null;
            showLogin("Patient");
        });

        sidebar.getChildren().addAll(brandBox, sidebarNav, userBadge, logoutBtn);
        root.setLeft(sidebar);

        // Center Content Area
        contentContainer = new VBox(16);
        contentContainer.setPadding(new Insets(20));

        ScrollPane centerScroll = new ScrollPane(contentContainer);
        centerScroll.setFitToWidth(true);
        centerScroll.setStyle("-fx-background: transparent; -fx-background-color: transparent; -fx-border-color: transparent;");

        root.setCenter(centerScroll);
        mainLayout = root;

        // Load Default View
        loadDefaultView();

        stage.setScene(createStyledScene(root, 1080, 740));
    }

    private void setupRoleNavigation(VBox navBox) {
        navBox.getChildren().clear();
        String[] navItems = switch (currentUser.role()) {
            case "PATIENT" -> new String[] { "Dashboard", "Book Appointment", "My Appointments", "Medical Records", "Notifications", "Visit History", "Messages", "Medications", "My information" };
            case "NURSE" -> new String[] { "Today's Patients", "Enter Vitals", "Intake Questionnaire", "Messages" };
            case "DOCTOR" -> new String[] { "Today's schedule", "Patient Lookup", "Patient Records", "Examination", "Messages" };
            case "ADMIN", "HOSPITAL_ADMIN" -> new String[] { "Hospital Dashboard", "Manage Staff", "Appointments", "Hospital Activity", "Hospital Information" };
            case "SUPERADMIN" -> new String[] { "Platform Overview", "Hospitals", "Patients", "All Appointments", "System Activity" };
            case "PHARMACY" -> new String[] { "Dashboard", "Prescription Queue", "Inventory", "Reports" };
            default -> new String[] { "Dashboard" };
        };

        for (String item : navItems) {
            Button navBtn = new Button(item);
            navBtn.setMaxWidth(Double.MAX_VALUE);
            navBtn.setAlignment(Pos.CENTER_LEFT);
            navBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #e2e8f0; -fx-font-size: 13px; -fx-padding: 8 12 8 12; -fx-cursor: hand;");

            navBtn.setOnAction(e -> {
                setActiveNavItem(item);
                navigateView(item);
            });
            navBox.getChildren().add(navBtn);
        }
    }

    private void setActiveNavItem(String itemName) {
        activeNav = itemName;
        for (Node n : sidebarNav.getChildren()) {
            if (n instanceof Button btn) {
                if (btn.getText().equalsIgnoreCase(itemName)) {
                    btn.setStyle("-fx-background-color: #2777af; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 13px; -fx-background-radius: 8; -fx-padding: 8 12 8 12;");
                } else {
                    btn.setStyle("-fx-background-color: transparent; -fx-text-fill: #e2e8f0; -fx-font-size: 13px; -fx-padding: 8 12 8 12; -fx-cursor: hand;");
                }
            }
        }
    }

    private void loadDefaultView() {
        String def = switch (currentUser.role()) {
            case "PATIENT" -> "Dashboard";
            case "NURSE" -> "Today's Patients";
            case "DOCTOR" -> "Today's schedule";
            case "ADMIN", "HOSPITAL_ADMIN" -> "Hospital Dashboard";
            case "SUPERADMIN" -> "Platform Overview";
            case "PHARMACY" -> "Dashboard";
            default -> "Dashboard";
        };
        setActiveNavItem(def);
        navigateView(def);
    }

    private void navigateView(String viewName) {
        contentContainer.getChildren().clear();
        String role = currentUser.role();

        if (role.equals("PATIENT")) {
            switch (viewName) {
                case "Dashboard" -> renderPatientDashboard();
                case "Book Appointment" -> renderAppointmentBooking();
                case "My Appointments" -> renderMyAppointments();
                case "Visit History" -> renderVisitSummaryView();
                case "Messages" -> renderMessagingView();
                case "Medical Records" -> renderDatabaseMedicalRecords();
                case "Notifications" -> renderDatabaseNotifications();
                case "Medications" -> { if(currentUser.staffId().endsWith("-DB")) renderDatabaseMedications(); else renderMedicationsView(); }
                case "My information" -> renderMyInformationView();
                default -> renderPatientDashboard();
            }
        } else if (role.equals("NURSE")) {
            switch (viewName) {
                case "Today's Patients" -> renderNurseDashboard();
                case "Enter Vitals", "Intake Questionnaire" -> renderNurseVitalsView();
                case "Messages" -> renderMessagingView();
                default -> renderNurseDashboard();
            }
        } else if (role.equals("DOCTOR")) {
            switch (viewName) {
                case "Today's schedule" -> renderDoctorDashboard();
                case "Patient Lookup", "Patient Records" -> renderPatientRecordsView();
                case "Examination" -> renderDoctorExamView();
                case "Messages" -> renderMessagingView();
                default -> renderDoctorDashboard();
            }
        } else if (role.equals("HOSPITAL_ADMIN")) {
            renderHospitalAdminView(viewName);
        } else if (role.equals("SUPERADMIN")) {
            renderPlatformAdminView(viewName);
        } else if (role.equals("ADMIN")) {
            switch (viewName) {
                case "Dashboard" -> renderAdminDashboard();
                case "User Management", "Staff Roles" -> renderAdminUserManagementView();
                case "System Logs" -> renderAdminLogsView();
                default -> renderAdminDashboard();
            }
        } else if (role.equals("PHARMACY")) {
            switch (viewName) {
                case "Dashboard", "Prescription Queue" -> renderPharmacyDashboard();
                case "Inventory" -> { if(currentUser.staffId().endsWith("-DB")) renderDatabasePharmacyInventory(); else renderPharmacyInventoryView(); }
                case "Reports" -> renderPharmacyReportsView();
                default -> renderPharmacyDashboard();
            }
        }
    }

    // =========================================================================
    // 4. PATIENT PORTAL SCREENS (Figures 3, 4, 5, 6)
    // =========================================================================

    // FIGURE 3 – Patient Dashboard
    private void renderPatientDashboard() {
        if (currentUser.staffId().endsWith("-DB")) { renderDatabasePatientDashboard(); return; }
        Label welcome = new Label("Welcome back, " + (selectedPatient != null ? selectedPatient.firstName() : "Sagar") + "!");
        welcome.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #143e63;");

        // 4 Stat Cards
        HBox stats = new HBox(16);
        stats.getChildren().addAll(
                UIHelper.createMetricCard("6", "Total Visits", null),
                UIHelper.createMetricCard("3", "Active Medications", null),
                UIHelper.createMetricCard("8/10", "Immunizations Complete", null),
                UIHelper.createMetricCard("2", "Unread Messages", null)
        );

        // Recent Visit Summary Table
        VBox recentVisitCard = UIHelper.createCard(16);
        Label recentLbl = new Label("Recent Visit Summary");
        recentLbl.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #1e293b;");

        TableView<String[]> visitTable = new TableView<>();
        visitTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        visitTable.setPrefHeight(130);

        TableColumn<String[], String> colDate = new TableColumn<>("Date");
        colDate.setCellValueFactory(d -> new SimpleStringProperty(d.getValue()[0]));
        TableColumn<String[], String> colDoc = new TableColumn<>("Doctor");
        colDoc.setCellValueFactory(d -> new SimpleStringProperty(d.getValue()[1]));
        TableColumn<String[], String> colReason = new TableColumn<>("Reason");
        colReason.setCellValueFactory(d -> new SimpleStringProperty(d.getValue()[2]));

        visitTable.getColumns().addAll(colDate, colDoc, colReason);
        visitTable.getItems().addAll(
                new String[] { "05/08/26", "Dr Ana", "Annual Checkup" },
                new String[] { "20/08/26", "Dr Aslan", "Vaccination" }
        );
        recentVisitCard.getChildren().addAll(recentLbl, visitTable);

        // Upcoming Appointments Table
        VBox apptCard = UIHelper.createCard(16);
        Label apptLbl = new Label("Upcoming Appointments");
        apptLbl.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #1e293b;");

        TableView<String[]> apptTable = new TableView<>();
        apptTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        apptTable.setPrefHeight(100);

        TableColumn<String[], String> colADate = new TableColumn<>("Date");
        colADate.setCellValueFactory(d -> new SimpleStringProperty(d.getValue()[0]));
        TableColumn<String[], String> colATime = new TableColumn<>("Time");
        colATime.setCellValueFactory(d -> new SimpleStringProperty(d.getValue()[1]));
        TableColumn<String[], String> colADoc = new TableColumn<>("Doctor");
        colADoc.setCellValueFactory(d -> new SimpleStringProperty(d.getValue()[2]));
        TableColumn<String[], String> colAType = new TableColumn<>("Type");
        colAType.setCellValueFactory(d -> new SimpleStringProperty(d.getValue()[3]));

        apptTable.getColumns().addAll(colADate, colATime, colADoc, colAType);
        apptTable.getItems().add(new String[] { "28/08/26", "9:30 AM", "Dr. Ana", "Follow-up" });
        apptCard.getChildren().addAll(apptLbl, apptTable);

        HBox tablesRow = new HBox(16, recentVisitCard, apptCard);
        HBox.setHgrow(recentVisitCard, Priority.ALWAYS);
        HBox.setHgrow(apptCard, Priority.ALWAYS);

        contentContainer.getChildren().addAll(welcome, stats, tablesRow);
    }

    // FIGURE 4 – Visit Summary
    private void renderVisitSummaryView() {
        Label headerTitle = new Label("Visit Summary – August 22, 2026");
        headerTitle.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #16466f;");
        Label headerSub = new Label("Annual Checkup Dr. Ana");
        headerSub.setStyle("-fx-font-size: 13px; -fx-text-fill: #64748b;");

        VBox banner = UIHelper.createPatientBanner(selectedPatient, "penicillin", "02/05/26");

        // Nurse Intake Notes
        VBox nurseCard = UIHelper.createCard(14);
        Label nurseTitle = new Label("Nurse's Intake Notes");
        nurseTitle.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: #1d5f92;");
        Label nurseText = new Label("Parent reports mild cough over the past 3 days, no fever at home. Child has been eating and sleeping normally. "
                + "No known exposures. Up to date immunization. Next HPV dose not due until age 11.");
        nurseText.setWrapText(true);
        nurseText.setStyle("-fx-font-size: 13px; -fx-text-fill: #334155;");
        nurseCard.getChildren().addAll(nurseTitle, nurseText);

        // Doctor's Examination Findings (Fixed typo "Doctor 's")
        VBox docCard = UIHelper.createCard(14);
        Label docTitle = new Label("Doctor's Examination Findings");
        docTitle.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: #1d5f92;");
        Label docText = new Label("Lungs clear, no wheezing. Throat slightly red. Ears clear. "
                + "Overall healthy growth and development consistent with age. No concerns noted during annual well-child exam.");
        docText.setWrapText(true);
        docText.setStyle("-fx-font-size: 13px; -fx-text-fill: #334155;");
        docCard.getChildren().addAll(docTitle, docText);

        // Prescriptions Issued
        VBox rxCard = UIHelper.createCard(14);
        Label rxTitle = new Label("Prescriptions Issued");
        rxTitle.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: #1d5f92;");

        HBox rxItem = new HBox(12);
        rxItem.setAlignment(Pos.CENTER_LEFT);
        rxItem.setPadding(new Insets(10, 14, 10, 14));
        rxItem.setStyle("-fx-background-color: #2b77af; -fx-background-radius: 8;");

        Region greenBar = new Region();
        greenBar.setPrefWidth(6);
        greenBar.setStyle("-fx-background-color: #48bb78;");

        Label medName = new Label("Amoxicillin 250mg");
        medName.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px;");

        Label medInstruct = new Label("1 tablet by mouth, twice daily for 7 days, sent to CVS Pharmacy");
        medInstruct.setStyle("-fx-text-fill: #e2e8f0; -fx-font-size: 13px;");

        rxItem.getChildren().addAll(greenBar, medName, new Region(), medInstruct);
        HBox.setHgrow(rxItem.getChildren().get(2), Priority.ALWAYS);
        rxCard.getChildren().addAll(rxTitle, rxItem);

        contentContainer.getChildren().addAll(headerTitle, headerSub, banner, nurseCard, docCard, rxCard);
    }

    // FIGURE 5 – Patient Messaging (Fixed typos: "relates", "wehn its time", "doesnot", "medictaion")
    private void renderMessagingView() {
        List<String[]> dbContacts = store.messageContacts(currentUser.username());
        if (currentUser.staffId().endsWith("-DB")) { renderDatabaseMessaging(dbContacts); return; }
        Label header = new Label("Messages");
        header.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #16466f;");
        Label sub = new Label("Send health-related questions to your doctor or nurse");
        sub.setStyle("-fx-font-size: 12px; -fx-text-fill: #64748b;");

        HBox split = new HBox(16);
        split.setAlignment(Pos.TOP_LEFT);

        // Left: Inbox List
        VBox inboxCard = UIHelper.createCard(12);
        inboxCard.setPrefWidth(220);
        inboxCard.setMinWidth(200);

        Label inboxLbl = new Label("Inbox (" + store.messages().size() + ")");
        inboxLbl.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-text-fill: #1e293b;");

        VBox thread1 = new VBox(2);
        thread1.setPadding(new Insets(8));
        thread1.setStyle("-fx-background-color: #e2e8f0; -fx-background-radius: 8;");
        Label t1Name = new Label("Dr. Ana");
        t1Name.setStyle("-fx-font-weight: bold; -fx-font-size: 12px;");
        Label t1Preview = new Label("Sagar's medication dosage....");
        t1Preview.setStyle("-fx-font-size: 11px; -fx-text-fill: #64748b;");
        thread1.getChildren().addAll(t1Name, t1Preview);

        VBox thread2 = new VBox(2);
        thread2.setPadding(new Insets(8));
        thread2.setStyle("-fx-background-color: #cbd5e1; -fx-background-radius: 8; -fx-border-color: #1e5a88; -fx-border-radius: 8;");
        Label t2Name = new Label("Dr. Aslan");
        t2Name.setStyle("-fx-font-weight: bold; -fx-font-size: 12px; -fx-text-fill: #0f172a;");
        Label t2Preview = new Label("Flu shot reminder for...");
        t2Preview.setStyle("-fx-font-size: 11px; -fx-text-fill: #334155;");
        thread2.getChildren().addAll(t2Name, t2Preview);

        inboxCard.getChildren().addAll(inboxLbl, thread1, thread2);

        // Right: Active Conversation Thread
        VBox chatCard = UIHelper.createCard(14);
        HBox.setHgrow(chatCard, Priority.ALWAYS);

        HBox partnerHeader = new HBox(10);
        partnerHeader.setAlignment(Pos.CENTER_LEFT);
        StackPane docAvatar = UIHelper.createAvatarBadge("A", 36);
        VBox docInfo = new VBox(2);
        Label partnerName = new Label("Dr. Aslan");
        partnerName.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-text-fill: #1e293b;");
        Label partnerRole = new Label("Pediatrician. Replying to your Question");
        partnerRole.setStyle("-fx-font-size: 11px; -fx-text-fill: #64748b;");
        docInfo.getChildren().addAll(partnerName, partnerRole);
        partnerHeader.getChildren().addAll(docAvatar, docInfo);

        Separator sep = new Separator();

        // Chat bubbles container
        VBox bubbleBox = new VBox(10);
        bubbleBox.setPadding(new Insets(8));

        // Incoming bubble
        VBox bubble1 = new VBox(3);
        bubble1.setAlignment(Pos.CENTER_LEFT);
        Label b1Text = new Label("Hi Dr. Aslan, please remind us when it's time for Sagar's next flu shot. Thanks");
        b1Text.setWrapText(true);
        b1Text.setPadding(new Insets(8, 12, 8, 12));
        b1Text.setStyle("-fx-background-color: #f1f5f9; -fx-background-radius: 12; -fx-text-fill: #1e293b; -fx-font-size: 12px;");
        Label b1Time = new Label("20/05/26 . 9:10 AM");
        b1Time.setStyle("-fx-font-size: 10px; -fx-text-fill: #94a3b8;");
        bubble1.getChildren().addAll(b1Text, b1Time);

        // Outgoing bubble
        VBox bubble2 = new VBox(3);
        bubble2.setAlignment(Pos.CENTER_RIGHT);
        Label b2Text = new Label("Flu shot reminder for Sagar, this Friday at 10 AM. Please make sure he does not have high temperature before the appointment");
        b2Text.setWrapText(true);
        b2Text.setPadding(new Insets(8, 12, 8, 12));
        b2Text.setStyle("-fx-background-color: #1e6093; -fx-background-radius: 12; -fx-text-fill: white; -fx-font-size: 12px;");
        Label b2Time = new Label("23/08/26 . 9:40 AM");
        b2Time.setStyle("-fx-font-size: 10px; -fx-text-fill: #94a3b8;");
        bubble2.getChildren().addAll(b2Text, b2Time);

        bubbleBox.getChildren().addAll(bubble1, bubble2);

        // Interactive Send Input Box
        HBox replyRow = new HBox(8);
        TextField replyField = new TextField();
        replyField.setPromptText("Type here.....");
        replyField.setStyle("-fx-background-radius: 20; -fx-padding: 8 14 8 14; -fx-background-color: #f8fafc; -fx-border-color: #cbd5e1; -fx-border-radius: 20;");
        HBox.setHgrow(replyField, Priority.ALWAYS);

        Button sendBtn = new Button("Send");
        sendBtn.setStyle("-fx-background-color: #1a608f; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 20; -fx-padding: 7 18 7 18; -fx-cursor: hand;");
        sendBtn.setOnAction(e -> {
            String txt = replyField.getText().trim();
            if (!txt.isEmpty()) {
                VBox newBubble = new VBox(3);
                newBubble.setAlignment(Pos.CENTER_RIGHT);
                Label msg = new Label(txt);
                msg.setWrapText(true);
                msg.setPadding(new Insets(8, 12, 8, 12));
                msg.setStyle("-fx-background-color: #1e6093; -fx-background-radius: 12; -fx-text-fill: white; -fx-font-size: 12px;");
                Label t = new Label("Just now");
                t.setStyle("-fx-font-size: 10px; -fx-text-fill: #94a3b8;");
                newBubble.getChildren().addAll(msg, t);
                bubbleBox.getChildren().add(newBubble);
                replyField.clear();
            }
        });
        replyRow.getChildren().addAll(replyField, sendBtn);

        chatCard.getChildren().addAll(partnerHeader, sep, bubbleBox, replyRow);
        split.getChildren().addAll(inboxCard, chatCard);

        // Compose New Message Section at bottom
        VBox composeCard = UIHelper.createCard(12);
        Label composeTitle = new Label("Compose New Message");
        composeTitle.setStyle("-fx-font-weight: bold; -fx-font-size: 13px; -fx-text-fill: #1e293b;");

        GridPane cGrid = new GridPane();
        cGrid.setHgap(12);
        cGrid.setVgap(6);

        TextField toField = new TextField("Dr. Ana");
        toField.setStyle("-fx-background-radius: 8; -fx-padding: 7; -fx-background-color: #f8fafc; -fx-border-color: #cbd5e1; -fx-border-radius: 8;");

        TextField msgField = new TextField();
        msgField.setPromptText("Enter your question or health update...");
        msgField.setStyle("-fx-background-radius: 8; -fx-padding: 7; -fx-background-color: #f8fafc; -fx-border-color: #cbd5e1; -fx-border-radius: 8;");
        GridPane.setHgrow(msgField, Priority.ALWAYS);

        Button composeSend = new Button("Send");
        composeSend.setStyle("-fx-background-color: #1a608f; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8; -fx-padding: 7 16 7 16; -fx-cursor: hand;");
        composeSend.setOnAction(e -> {
            if (!msgField.getText().trim().isEmpty()) {
                store.sendMessage(currentUser.displayName(), toField.getText(), msgField.getText().trim(), false);
                msgField.clear();
                msgField.setPromptText("Message sent successfully!");
            }
        });

        cGrid.add(new Label("To"), 0, 0);
        cGrid.add(toField, 0, 1);
        cGrid.add(new Label("Message"), 1, 0);
        cGrid.add(msgField, 1, 1);
        cGrid.add(composeSend, 2, 1);

        composeCard.getChildren().addAll(composeTitle, cGrid);

        contentContainer.getChildren().addAll(header, sub, split, composeCard);
    }

    // FIGURE 6 – My Information
    private void renderMyInformationView() {
        Label header = new Label("My Information");
        header.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #16466f;");
        Label sub = new Label("View and update your contact, insurance, and pharmacy information");
        sub.setStyle("-fx-font-size: 12px; -fx-text-fill: #64748b;");

        // Sub-tabs
        HBox tabs = new HBox(18);
        tabs.getChildren().addAll(
                new Label("Contact"),
                new Label("Insurance"),
                new Label("Pharmacy"),
                new Label("Account")
        );
        ((Label)tabs.getChildren().get(0)).setStyle("-fx-font-weight: bold; -fx-text-fill: #1a608f; -fx-border-color: #1a608f; -fx-border-width: 0 0 2 0; -fx-padding: 0 4 4 4;");
        for (int i = 1; i < tabs.getChildren().size(); i++) {
            ((Label)tabs.getChildren().get(i)).setStyle("-fx-text-fill: #64748b; -fx-padding: 0 4 4 4;");
        }

        // Contact Information Card
        VBox contactCard = UIHelper.createCard(16);
        Label cTitle = new Label("Contact information");
        cTitle.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: #1e293b;");

        TextField fName = new TextField(selectedPatient.firstName());
        TextField lName = new TextField(selectedPatient.lastName());
        TextField dob = new TextField(selectedPatient.dateOfBirth().toString());
        dob.setEditable(false);
        TextField guardian = new TextField(selectedPatient.guardianName());
        TextField relationship = new TextField(selectedPatient.guardianRelationship());
        TextField email = new TextField(selectedPatient.email());
        TextField phone = new TextField(selectedPatient.phone());

        String fStyle = "-fx-background-radius: 8; -fx-padding: 7; -fx-background-color: #f8fafc; -fx-border-color: #cbd5e1; -fx-border-radius: 8;";
        fName.setStyle(fStyle);
        lName.setStyle(fStyle);
        dob.setStyle(fStyle);
        guardian.setStyle(fStyle);
        relationship.setStyle(fStyle);
        email.setStyle(fStyle);
        phone.setStyle(fStyle);

        GridPane cGrid = new GridPane();
        cGrid.setHgap(16);
        cGrid.setVgap(10);

        cGrid.add(new Label("First Name"), 0, 0);
        cGrid.add(fName, 0, 1);
        cGrid.add(new Label("Last Name"), 1, 0);
        cGrid.add(lName, 1, 1);
        cGrid.add(new Label("Date of Birth"), 2, 0);
        cGrid.add(dob, 2, 1);

        cGrid.add(new Label("Parent/guardian Name"), 0, 2);
        cGrid.add(guardian, 0, 3);
        cGrid.add(new Label("Relationship"), 1, 2);
        cGrid.add(relationship, 1, 3);

        cGrid.add(new Label("Email Address"), 0, 4);
        cGrid.add(email, 0, 5);
        cGrid.add(new Label("Phone Number"), 1, 4);
        cGrid.add(phone, 1, 5);

        Label saveMsg = new Label();
        saveMsg.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: #16a34a;");

        Button saveChangesBtn = UIHelper.createSuccessButton("Save Changes");
        Button editBtn = UIHelper.createPrimaryButton("Edit");

        saveChangesBtn.setOnAction(e -> {
            selectedPatient.setContactInfo(fName.getText().trim(), lName.getText().trim(), guardian.getText().trim(),
                    relationship.getText().trim(), email.getText().trim(), phone.getText().trim());
            saveMsg.setText("Information updated successfully.");
        });

        HBox btnRow = new HBox(10, saveChangesBtn, editBtn, saveMsg);
        btnRow.setAlignment(Pos.CENTER_LEFT);

        contactCard.getChildren().addAll(cTitle, cGrid, btnRow);

        // Insurance Information Card
        VBox insCard = UIHelper.createCard(16);
        Label insTitle = new Label("Insurance Information");
        insTitle.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: #1e293b;");

        TextField provider = new TextField(selectedPatient.insuranceProvider());
        TextField policy = new TextField(selectedPatient.insurancePolicy());
        provider.setStyle(fStyle);
        policy.setStyle(fStyle);

        GridPane insGrid = new GridPane();
        insGrid.setHgap(16);
        insGrid.setVgap(8);
        insGrid.add(new Label("Insurance Provider"), 0, 0);
        insGrid.add(provider, 0, 1);
        insGrid.add(new Label("Policy Number"), 1, 0);
        insGrid.add(policy, 1, 1);

        insCard.getChildren().addAll(insTitle, insGrid);

        contentContainer.getChildren().addAll(header, sub, tabs, contactCard, insCard);
    }

    private void renderMedicationsView() {
        Label header = new Label("Active Medications & Prescriptions");
        header.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #16466f;");

        VBox listCard = UIHelper.createCard(16);
        for (Prescription rx : store.allPrescriptions()) {
            HBox row = new HBox(12);
            row.setAlignment(Pos.CENTER_LEFT);
            row.setPadding(new Insets(10));
            row.setStyle("-fx-background-color: #f8fafc; -fx-background-radius: 8; -fx-border-color: #e2e8f0; -fx-border-radius: 8;");

            Label med = new Label(rx.medication());
            med.setStyle("-fx-font-weight: bold; -fx-font-size: 14px; -fx-text-fill: #1e40af;");
            Label instr = new Label(rx.instructions());
            instr.setStyle("-fx-text-fill: #475569;");
            Label status = UIHelper.createStatusBadge(rx.status());

            Region sp = new Region();
            HBox.setHgrow(sp, Priority.ALWAYS);
            row.getChildren().addAll(med, instr, sp, status);
            listCard.getChildren().add(row);
        }
        contentContainer.getChildren().addAll(header, listCard);
    }

    // =========================================================================
    // 5. NURSE CONSOLE SCREENS (Figures 8, 9)
    // =========================================================================

    // FIGURE 8 – Nurse Dashboard (Fixed typos: "Patlent", "Quick Patlent Lockup")
    private void renderNurseDashboard() {
        List<String[]> dbRows = store.nurseBookings(currentUser.username());
        if (currentUser.staffId().endsWith("-DB")) { renderDatabaseNurseDashboard(dbRows); return; }
        Label header = new Label("Today's Patients");
        header.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #16466f;");
        Label sub = new Label("Saturday, August 22, 2026  |  6 patients scheduled");
        sub.setStyle("-fx-font-size: 12px; -fx-text-fill: #64748b;");

        // 4 Stat Cards
        HBox stats = new HBox(16);
        stats.getChildren().addAll(
                UIHelper.createMetricCard("6", "Scheduled Today", null),
                UIHelper.createMetricCard("2", "Intake Completed", null),
                UIHelper.createMetricCard("3", "Waiting", null),
                UIHelper.createMetricCard("5", "Patient Messages", null)
        );

        // Patient Queue Table
        VBox queueCard = UIHelper.createCard(16);
        Label qTitle = new Label("Patient Queue");
        qTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #1e293b;");

        TableView<Appointment> qTable = new TableView<>();
        qTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        qTable.setPrefHeight(180);

        TableColumn<Appointment, String> colPat = new TableColumn<>("Patient");
        colPat.setCellValueFactory(a -> new SimpleStringProperty(a.getValue().patientName()));

        TableColumn<Appointment, String> colAge = new TableColumn<>("Age");
        colAge.setCellValueFactory(a -> new SimpleStringProperty(String.valueOf(a.getValue().age())));

        TableColumn<Appointment, String> colTime = new TableColumn<>("Time");
        colTime.setCellValueFactory(a -> new SimpleStringProperty(a.getValue().time()));

        TableColumn<Appointment, String> colReason = new TableColumn<>("Reason");
        colReason.setCellValueFactory(a -> new SimpleStringProperty(a.getValue().reason()));

        TableColumn<Appointment, Void> colVitals = new TableColumn<>("Vitals");
        colVitals.setCellFactory(c -> new TableCell<>() {
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) setGraphic(null);
                else {
                    Appointment a = getTableView().getItems().get(getIndex());
                    setGraphic(UIHelper.createStatusBadge(a.vitalsStatus()));
                }
            }
        });

        TableColumn<Appointment, Void> colIntake = new TableColumn<>("Intake");
        colIntake.setCellFactory(c -> new TableCell<>() {
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) setGraphic(null);
                else {
                    Appointment a = getTableView().getItems().get(getIndex());
                    setGraphic(UIHelper.createStatusBadge(a.intakeStatus()));
                }
            }
        });

        TableColumn<Appointment, Void> colAction = new TableColumn<>("Action");
        colAction.setCellFactory(c -> new TableCell<>() {
            private final Button btn = new Button();
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) setGraphic(null);
                else {
                    Appointment a = getTableView().getItems().get(getIndex());
                    if ("Pending".equalsIgnoreCase(a.intakeStatus())) {
                        btn.setText("Start Intake");
                        btn.setStyle("-fx-background-color: #2b77af; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 6; -fx-font-size: 11px;");
                    } else {
                        btn.setText("Open");
                        btn.setStyle("-fx-background-color: #e2e8f0; -fx-text-fill: #334155; -fx-background-radius: 6; -fx-font-size: 11px;");
                    }
                    btn.setOnAction(e -> {
                        Patient p = store.findPatientByName(a.patientName(), "");
                        if (p != null) selectedPatient = p;
                        setActiveNavItem("Enter Vitals");
                        navigateView("Enter Vitals");
                    });
                    setGraphic(btn);
                }
            }
        });

        qTable.getColumns().addAll(colPat, colAge, colTime, colReason, colVitals, colIntake, colAction);
        qTable.getItems().setAll(store.appointments());
        queueCard.getChildren().addAll(qTitle, qTable);

        // Quick Patient Lookup Card
        VBox lookupCard = UIHelper.createCard(16);
        Label lTitle = new Label("Quick Patient Lookup");
        lTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #1e293b;");

        TextField sFirst = new TextField("Sagar");
        TextField sLast = new TextField("Cheema");
        DatePicker sDob = new DatePicker(LocalDate.of(2018, 5, 12));

        String inputStyle = "-fx-background-radius: 8; -fx-padding: 7; -fx-background-color: #f8fafc; -fx-border-color: #cbd5e1; -fx-border-radius: 8;";
        sFirst.setStyle(inputStyle);
        sLast.setStyle(inputStyle);
        sDob.setStyle(inputStyle);

        HBox searchInputs = new HBox(14);
        searchInputs.setAlignment(Pos.CENTER_LEFT);
        searchInputs.getChildren().addAll(
                new VBox(4, new Label("Search by First Name"), sFirst),
                new VBox(4, new Label("Last Name"), sLast),
                new VBox(4, new Label("DOB"), sDob)
        );

        Label searchResult = new Label();
        searchResult.setStyle("-fx-font-size: 12px; -fx-font-weight: bold;");

        Button searchBtn = UIHelper.createPrimaryButton("Search");
        searchBtn.setOnAction(e -> {
            Patient p = store.findPatient(sFirst.getText(), sLast.getText(), sDob.getValue());
            if (p != null) {
                selectedPatient = p;
                searchResult.setText("Found: " + p.fullName() + " (Age " + p.age() + ", " + p.id() + "). Ready for intake.");
                searchResult.setStyle("-fx-text-fill: #16a34a; -fx-font-weight: bold;");
            } else {
                searchResult.setText("No patient record found matching criteria.");
                searchResult.setStyle("-fx-text-fill: #dc2626; -fx-font-weight: bold;");
            }
        });

        lookupCard.getChildren().addAll(lTitle, searchInputs, searchBtn, searchResult);

        contentContainer.getChildren().addAll(header, sub, stats, queueCard, lookupCard);
    }

    // FIGURE 9 – Nurse Intake and Patient History
    private void renderNurseVitalsView() {
        Label header = new Label("Enter Vitals");
        header.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #16466f;");
        Label sub = new Label("Visit on August 22, 2026 . 10:30 AM with Dr. Alan");
        sub.setStyle("-fx-font-size: 12px; -fx-text-fill: #64748b;");

        VBox banner = UIHelper.createPatientBanner(selectedPatient, "penicillin", "02/05/26");

        // Vital Signs Input Form
        VBox vitalsCard = UIHelper.createCard(16);
        Label vTitle = new Label("Vital Signs");
        vTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #1e293b;");

        TextField weight = new TextField("25");
        TextField height = new TextField("128");
        TextField bodyTemp = new TextField("98.4");
        TextField bp = new TextField();
        TextField pulse = new TextField("85");

        if (selectedPatient.age() <= 12) {
            bp.setPromptText("N/A (Age ≤ 12)");
            bp.setText("N/A");
        } else {
            bp.setPromptText("e.g. 118/76");
        }

        String inputStyle = "-fx-background-radius: 8; -fx-padding: 7; -fx-background-color: #f8fafc; -fx-border-color: #cbd5e1; -fx-border-radius: 8;";
        weight.setStyle(inputStyle);
        height.setStyle(inputStyle);
        bodyTemp.setStyle(inputStyle);
        bp.setStyle(inputStyle);
        pulse.setStyle(inputStyle);

        GridPane vGrid = new GridPane();
        vGrid.setHgap(16);
        vGrid.setVgap(10);

        vGrid.add(new Label("Weight (kg)"), 0, 0);
        vGrid.add(weight, 0, 1);
        vGrid.add(new Label("Height (cm)"), 1, 0);
        vGrid.add(height, 1, 1);
        vGrid.add(new Label("Body Temperature (°F)"), 2, 0);
        vGrid.add(bodyTemp, 2, 1);

        vGrid.add(new Label("Blood Pressure (mmHg)"), 0, 2);
        vGrid.add(bp, 0, 3);
        vGrid.add(new Label("Pulse (bpm)"), 1, 2);
        vGrid.add(pulse, 1, 3);

        Label msg = new Label();
        msg.setStyle("-fx-font-weight: bold; -fx-font-size: 12px;");

        Button saveVitalsBtn = UIHelper.createSuccessButton("Save Vitals");
        Button editBtn = UIHelper.createPrimaryButton("Edit");

        saveVitalsBtn.setOnAction(e -> {
            Visit v = store.getOrCreateVisit(selectedPatient, currentUser.username());
            v.setVitals(weight.getText().trim(), height.getText().trim(), bodyTemp.getText().trim(), bp.getText().trim(), pulse.getText().trim());
            v.setIntakeCompleted(true);
            // Update queue
            for (Appointment a : store.appointments()) {
                if (a.patientName().equalsIgnoreCase(selectedPatient.firstName())) {
                    a.setVitalsStatus("Done");
                    a.setIntakeStatus("Done");
                }
            }
            msg.setText("Vitals saved successfully! Patient marked ready for examination.");
            msg.setStyle("-fx-text-fill: #16a34a; -fx-font-weight: bold;");
        });

        HBox btnRow = new HBox(12, saveVitalsBtn, editBtn, msg);
        btnRow.setAlignment(Pos.CENTER_LEFT);

        vitalsCard.getChildren().addAll(vTitle, vGrid, btnRow);

        // Vital History Table
        VBox histCard = UIHelper.createCard(16);
        Label hTitle = new Label("Vital History");
        hTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #1e293b;");

        TableView<String[]> hTable = new TableView<>();
        hTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        hTable.setPrefHeight(120);

        TableColumn<String[], String> colDate = new TableColumn<>("Date");
        colDate.setCellValueFactory(d -> new SimpleStringProperty(d.getValue()[0]));
        TableColumn<String[], String> colW = new TableColumn<>("Weight");
        colW.setCellValueFactory(d -> new SimpleStringProperty(d.getValue()[1]));
        TableColumn<String[], String> colH = new TableColumn<>("Height");
        colH.setCellValueFactory(d -> new SimpleStringProperty(d.getValue()[2]));
        TableColumn<String[], String> colT = new TableColumn<>("Temp");
        colT.setCellValueFactory(d -> new SimpleStringProperty(d.getValue()[3]));
        TableColumn<String[], String> colB = new TableColumn<>("BP");
        colB.setCellValueFactory(d -> new SimpleStringProperty(d.getValue()[4]));

        hTable.getColumns().addAll(colDate, colW, colH, colT, colB);
        hTable.getItems().addAll(
                new String[] { "02/05/26", "25 kg", "127 cm", "98.6", "--" },
                new String[] { "22/08/26", "25 kg", "128 cm", "98.4", "N/A" }
        );

        histCard.getChildren().addAll(hTitle, hTable);

        contentContainer.getChildren().addAll(header, sub, banner, vitalsCard, histCard);
    }

    // =========================================================================
    // 6. DOCTOR CONSOLE SCREENS (Figures 11, 12, 13)
    // =========================================================================

    // FIGURE 11 – Doctor Dashboard
    private void renderDoctorDashboard() {
        List<String[]> dbRows = store.doctorBookings(currentUser.username());
        if (currentUser.staffId().endsWith("-DB")) { renderDatabaseDoctorDashboard(dbRows); return; }
        Label header = new Label("Today's Schedule");
        header.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #16466f;");
        Label sub = new Label("Saturday, August 22, 2026");
        sub.setStyle("-fx-font-size: 12px; -fx-text-fill: #64748b;");

        // 4 Stat Cards
        HBox stats = new HBox(16);
        stats.getChildren().addAll(
                UIHelper.createMetricCard("6", "Appointments", null),
                UIHelper.createMetricCard("2", "Ready for Exam", null),
                UIHelper.createMetricCard("3", "Patient Messages", null),
                UIHelper.createMetricCard("1", "Pending Rx", null)
        );

        // Today's Schedule Table
        VBox scheduleCard = UIHelper.createCard(16);
        Label sTitle = new Label("Today's Schedule");
        sTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #1e293b;");

        TableView<Appointment> sTable = new TableView<>();
        sTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        sTable.setPrefHeight(200);

        TableColumn<Appointment, String> colTime = new TableColumn<>("Time");
        colTime.setCellValueFactory(a -> new SimpleStringProperty(a.getValue().time()));

        TableColumn<Appointment, String> colAge = new TableColumn<>("Age");
        colAge.setCellValueFactory(a -> new SimpleStringProperty(String.valueOf(a.getValue().age())));

        TableColumn<Appointment, String> colPat = new TableColumn<>("Patient");
        colPat.setCellValueFactory(a -> new SimpleStringProperty(a.getValue().patientName()));

        TableColumn<Appointment, String> colReason = new TableColumn<>("Reason");
        colReason.setCellValueFactory(a -> new SimpleStringProperty(a.getValue().reason()));

        TableColumn<Appointment, String> colStatus = new TableColumn<>("Status");
        colStatus.setCellValueFactory(a -> new SimpleStringProperty(a.getValue().status()));

        TableColumn<Appointment, Void> colIntake = new TableColumn<>("Intake");
        colIntake.setCellFactory(c -> new TableCell<>() {
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) setGraphic(null);
                else {
                    Appointment a = getTableView().getItems().get(getIndex());
                    setGraphic(UIHelper.createStatusBadge(a.intakeStatus()));
                }
            }
        });

        TableColumn<Appointment, Void> colAction = new TableColumn<>("Action");
        colAction.setCellFactory(c -> new TableCell<>() {
            private final Button btn = new Button();
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) setGraphic(null);
                else {
                    Appointment a = getTableView().getItems().get(getIndex());
                    btn.setText(a.patientName().equals("Harman") ? "Examine" : (a.patientName().equals("Sagar") ? "Open" : "View"));
                    btn.setStyle("-fx-background-color: #2b77af; -fx-text-fill: white; -fx-background-radius: 6; -fx-font-size: 11px;");
                    btn.setOnAction(e -> {
                        Patient p = store.findPatientByName(a.patientName(), "");
                        if (p != null) selectedPatient = p;
                        setActiveNavItem("Examination");
                        navigateView("Examination");
                    });
                    setGraphic(btn);
                }
            }
        });

        sTable.getColumns().addAll(colTime, colAge, colPat, colReason, colStatus, colIntake, colAction);
        sTable.getItems().setAll(store.appointments());

        scheduleCard.getChildren().addAll(sTitle, sTable);
        contentContainer.getChildren().addAll(header, sub, stats, scheduleCard);
    }

    // FIGURE 12 – Patient History
    private void renderPatientRecordsView() {
        Label header = new Label("Patient Record - " + selectedPatient.fullName());
        header.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #16466f;");
        Label sub = new Label("Complete medical history, medication, and immunization record");
        sub.setStyle("-fx-font-size: 12px; -fx-text-fill: #64748b;");

        VBox banner = UIHelper.createPatientBanner(selectedPatient, "penicillin", "02/05/26");

        // Sub tabs
        HBox tabs = new HBox(20);
        Label tHealth = new Label("Health Issues");
        tHealth.setStyle("-fx-font-weight: bold; -fx-text-fill: #1a608f; -fx-border-color: #1a608f; -fx-border-width: 0 0 2 0; -fx-padding: 0 4 4 4;");
        Label tMeds = new Label("Medications");
        Label tImm = new Label("Immunization");
        Label tVisits = new Label("Visits");
        tMeds.setStyle("-fx-text-fill: #64748b; -fx-padding: 0 4 4 4;");
        tImm.setStyle("-fx-text-fill: #64748b; -fx-padding: 0 4 4 4;");
        tVisits.setStyle("-fx-text-fill: #64748b; -fx-padding: 0 4 4 4;");
        tabs.getChildren().addAll(tHealth, tMeds, tImm, tVisits);

        // Previous Health Issues Table
        VBox issuesCard = UIHelper.createCard(16);
        Label iTitle = new Label("Previous Health Issues");
        iTitle.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: #1e293b;");

        TableView<String[]> issuesTable = new TableView<>();
        issuesTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        issuesTable.setPrefHeight(100);

        TableColumn<String[], String> colDate = new TableColumn<>("Date");
        colDate.setCellValueFactory(d -> new SimpleStringProperty(d.getValue()[0]));
        TableColumn<String[], String> colIssue = new TableColumn<>("Issue");
        colIssue.setCellValueFactory(d -> new SimpleStringProperty(d.getValue()[1]));
        TableColumn<String[], String> colDiag = new TableColumn<>("Diagnosis");
        colDiag.setCellValueFactory(d -> new SimpleStringProperty(d.getValue()[2]));
        TableColumn<String[], String> colTreat = new TableColumn<>("Treatment");
        colTreat.setCellValueFactory(d -> new SimpleStringProperty(d.getValue()[3]));

        issuesTable.getColumns().addAll(colDate, colIssue, colDiag, colTreat);
        issuesTable.getItems().add(new String[] { "02/05/26", "Fever & Cough", "Viral upper respiratory infection", "Rest, fluids, Amoxicillin 250mg" });
        issuesCard.getChildren().addAll(iTitle, issuesTable);

        // Two split cards: Previously Prescribed Medications & Immunization History
        VBox prevMedCard = UIHelper.createCard(14);
        Label pmTitle = new Label("Previously Prescribed Medications");
        pmTitle.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #1e293b;");
        Label pmDetail = new Label("Amoxicillin 250mg\n02/05/26 . Twice daily for 7 Days");
        pmDetail.setStyle("-fx-font-size: 13px; -fx-text-fill: #334155;");
        prevMedCard.getChildren().addAll(pmTitle, pmDetail);

        VBox immCard = UIHelper.createCard(14);
        Label immTitle = new Label("Immunization History");
        immTitle.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #1e293b;");

        TableView<String[]> immTable = new TableView<>();
        immTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        immTable.setPrefHeight(100);

        TableColumn<String[], String> colVac = new TableColumn<>("Vaccine");
        colVac.setCellValueFactory(d -> new SimpleStringProperty(d.getValue()[0]));
        TableColumn<String[], String> colVDate = new TableColumn<>("Date");
        colVDate.setCellValueFactory(d -> new SimpleStringProperty(d.getValue()[1]));
        TableColumn<String[], String> colVStatus = new TableColumn<>("Status");
        colVStatus.setCellValueFactory(d -> new SimpleStringProperty(d.getValue()[2]));

        immTable.getColumns().addAll(colVac, colVDate, colVStatus);
        immTable.getItems().addAll(
                new String[] { "Hepatitis B", "2020", "Complete" },
                new String[] { "MMR (Measles, Mumps, Rubella)", "2023", "Complete" },
                new String[] { "DTaP Booster", "2024", "Complete" }
        );
        immCard.getChildren().addAll(immTitle, immTable);

        HBox split = new HBox(16, prevMedCard, immCard);
        HBox.setHgrow(prevMedCard, Priority.ALWAYS);
        HBox.setHgrow(immCard, Priority.ALWAYS);

        contentContainer.getChildren().addAll(header, sub, banner, tabs, issuesCard, split);
    }

    // FIGURE 13 – Examination Findings and Prescription (Fixes duplicate labels, typos "Apperance", "excudate", "Duration(days)")
    private void renderDoctorExamView() {
        Label header = new Label("Examination & Prescription");
        header.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #16466f;");
        Label sub = new Label("Visit : August 22, 2026  |  " + selectedPatient.fullName() + "  |  Age " + selectedPatient.age());
        sub.setStyle("-fx-font-size: 12px; -fx-text-fill: #64748b;");

        // Banner with Nurse intake checkmark
        VBox banner = UIHelper.createPatientBanner(selectedPatient, "penicillin", "02/05/26");
        Label intakeDone = new Label("Nurse Intake ✓ (Vitals: 25 kg | 128 cm | 98.4 °F | BP: N/A)");
        intakeDone.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: #16a34a; -fx-padding: 4 0 0 0;");
        banner.getChildren().add(intakeDone);

        // Physical Examination Findings Form
        VBox examCard = UIHelper.createCard(16);
        Label eTitle = new Label("Physical Examination Findings");
        eTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #1e293b;");

        ComboBox<String> generalCombo = new ComboBox<>();
        generalCombo.getItems().addAll("Well, alert, cooperative", "Mild distress", "Lethargic", "Fussy / Irritable");
        generalCombo.setValue("Well, alert, cooperative");
        generalCombo.setMaxWidth(Double.MAX_VALUE);

        // Fixed column 2 from duplicated "General Apperance" typo to "Respiratory / Lungs"
        ComboBox<String> respCombo = new ComboBox<>();
        respCombo.getItems().addAll("Clear, no wheezing", "Bilateral air entry equal", "Mild crackles", "Expiratory wheezing");
        respCombo.setValue("Clear, no wheezing");
        respCombo.setMaxWidth(Double.MAX_VALUE);

        ComboBox<String> earsCombo = new ComboBox<>();
        earsCombo.getItems().addAll("Clear bilaterally", "Right tympanic membrane erythema", "Bilateral fluid behind TM", "Normal landmarks visible");
        earsCombo.setValue("Clear bilaterally");
        earsCombo.setMaxWidth(Double.MAX_VALUE);

        // Fixed column 4 from duplicated "Ears (Otoscope)" typo to "Throat & Oral Cavity" and fixed "excudate" -> "exudate"
        ComboBox<String> throatCombo = new ComboBox<>();
        throatCombo.getItems().addAll("Slightly red, no exudate", "Normal pink, moist", "Tonsillar exudate present", "Posterior pharyngeal erythema");
        throatCombo.setValue("Slightly red, no exudate");
        throatCombo.setMaxWidth(Double.MAX_VALUE);

        TextArea examNotes = new TextArea("Healthy growth and development consistent with age. Mild upper respiratory irritation noted, otherwise unremarkable exam. No acute concern.");
        examNotes.setPrefRowCount(2);
        examNotes.setWrapText(true);

        String inputStyle = "-fx-background-radius: 8; -fx-padding: 7; -fx-background-color: #f8fafc; -fx-border-color: #cbd5e1; -fx-border-radius: 8;";
        generalCombo.setStyle(inputStyle);
        respCombo.setStyle(inputStyle);
        earsCombo.setStyle(inputStyle);
        throatCombo.setStyle(inputStyle);
        examNotes.setStyle(inputStyle);

        GridPane eGrid = new GridPane();
        eGrid.setHgap(16);
        eGrid.setVgap(10);

        eGrid.add(new Label("General Appearance"), 0, 0);
        eGrid.add(generalCombo, 0, 1);
        eGrid.add(new Label("Respiratory / Lungs"), 1, 0);
        eGrid.add(respCombo, 1, 1);

        eGrid.add(new Label("Ears (Otoscope)"), 0, 2);
        eGrid.add(earsCombo, 0, 3);
        eGrid.add(new Label("Throat & Oral Cavity"), 1, 2);
        eGrid.add(throatCombo, 1, 3);

        eGrid.add(new Label("Examination Notes"), 0, 4, 2, 1);
        eGrid.add(examNotes, 0, 5, 2, 1);

        examCard.getChildren().addAll(eTitle, eGrid);

        // Prescription Section
        VBox rxCard = UIHelper.createCard(16);
        Label rxTitle = new Label("Prescription");
        rxTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #1e293b;");

        TextField med = new TextField("Amoxicillin 250mg");
        TextField dosage = new TextField("1 tablet");
        TextField frequency = new TextField("Twice daily");
        TextField duration = new TextField("7 days"); // Fixed typo "Duration(days)"
        TextField refills = new TextField("0");

        med.setStyle(inputStyle);
        dosage.setStyle(inputStyle);
        frequency.setStyle(inputStyle);
        duration.setStyle(inputStyle);
        refills.setStyle(inputStyle);

        GridPane rxGrid = new GridPane();
        rxGrid.setHgap(12);
        rxGrid.setVgap(8);

        rxGrid.add(new Label("Medication"), 0, 0);
        rxGrid.add(med, 0, 1);
        rxGrid.add(new Label("Dosage"), 1, 0);
        rxGrid.add(dosage, 1, 1);
        rxGrid.add(new Label("Frequency"), 2, 0);
        rxGrid.add(frequency, 2, 1);
        rxGrid.add(new Label("Duration (days)"), 3, 0);
        rxGrid.add(duration, 3, 1);
        rxGrid.add(new Label("Refills"), 4, 0);
        rxGrid.add(refills, 4, 1);

        Label saveMsg = new Label();
        saveMsg.setStyle("-fx-font-size: 12px; -fx-font-weight: bold;");

        Button submitExamBtn = UIHelper.createSuccessButton("Save Consultation & Send Prescription to Pharmacy");
        submitExamBtn.setOnAction(e -> {
            Visit v = store.getOrCreateVisit(selectedPatient, "Dr. Alan");
            String rxSummary = med.getText().trim() + " - " + dosage.getText().trim() + ", "
                    + frequency.getText().trim() + " for " + duration.getText().trim() + ", sent to " + selectedPatient.preferredPharmacy();
            v.setExamination(generalCombo.getValue(), respCombo.getValue(), earsCombo.getValue(), throatCombo.getValue(),
                    examNotes.getText().trim(), "Complete full course, plenty of warm fluids.", rxSummary);

            // Add prescription to pharmacy queue
            store.addPrescription(selectedPatient.fullName(), med.getText().trim(), dosage.getText().trim(),
                    frequency.getText().trim(), duration.getText().trim(), refills.getText().trim(),
                    currentUser.displayName(), selectedPatient.preferredPharmacy());

            saveMsg.setText("Consultation recorded. Prescription automatically routed to Pharmacy Queue!");
            saveMsg.setStyle("-fx-text-fill: #16a34a; -fx-font-weight: bold;");
        });

        rxCard.getChildren().addAll(rxTitle, rxGrid, submitExamBtn, saveMsg);

        contentContainer.getChildren().addAll(header, sub, banner, examCard, rxCard);
    }

    // =========================================================================
    // 7. ADMINISTRATOR SCREENS (Figure 14)
    // =========================================================================
    private void renderAdminDashboard() {
        Label header = new Label("Admin Dashboard");
        header.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #16466f;");
        Label sub = new Label("Welcome Back, Admin!");
        sub.setStyle("-fx-font-size: 12px; -fx-text-fill: #64748b;");

        // 4 Stat Cards
        HBox stats = new HBox(16);
        stats.getChildren().addAll(
                UIHelper.createMetricCard("1,248", "Total Patients", "View all patients"),
                UIHelper.createMetricCard("32", "Total Staff", "View all Staff"),
                UIHelper.createMetricCard("18", "Today's Appointments", "View schedule"),
                UIHelper.createMetricCard("5", "Unread messages", "View messages")
        );

        // Center split: Recent Appointments & System Notifications
        VBox apptCard = UIHelper.createCard(16);
        Label aTitle = new Label("Recent Appointments");
        aTitle.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: #1e293b;");

        TableView<String[]> aTable = new TableView<>();
        aTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        aTable.setPrefHeight(140);

        TableColumn<String[], String> cName = new TableColumn<>("Patient name");
        cName.setCellValueFactory(d -> new SimpleStringProperty(d.getValue()[0]));
        TableColumn<String[], String> cDate = new TableColumn<>("Date");
        cDate.setCellValueFactory(d -> new SimpleStringProperty(d.getValue()[1]));
        TableColumn<String[], String> cTime = new TableColumn<>("Time");
        cTime.setCellValueFactory(d -> new SimpleStringProperty(d.getValue()[2]));
        TableColumn<String[], String> cStatus = new TableColumn<>("Status");
        cStatus.setCellValueFactory(d -> new SimpleStringProperty(d.getValue()[3]));

        aTable.getColumns().addAll(cName, cDate, cTime, cStatus);
        aTable.getItems().addAll(
                new String[] { "Harman", "20/08/26", "9:00 AM", "Checked in" },
                new String[] { "Sagar", "20/08/26", "10:30 AM", "with doctor" },
                new String[] { "Aish", "20/08/26", "1:15 PM", "scheduled" }
        );
        apptCard.getChildren().addAll(aTitle, aTable);

        // System Notifications Card
        VBox notifCard = UIHelper.createCard(16);
        Label nTitle = new Label("System Notifications");
        nTitle.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: #1e293b;");

        GridPane nGrid = new GridPane();
        nGrid.setHgap(10);
        nGrid.setVgap(8);

        Label log1 = new Label("Database backup completed");
        log1.setStyle("-fx-background-color: #f1f5f9; -fx-padding: 8 12 8 12; -fx-background-radius: 8;");
        Label log2 = new Label("System update completed");
        log2.setStyle("-fx-background-color: #f1f5f9; -fx-padding: 8 12 8 12; -fx-background-radius: 8;");
        Label log3 = new Label("New staff member registered");
        log3.setStyle("-fx-background-color: #f1f5f9; -fx-padding: 8 12 8 12; -fx-background-radius: 8;");
        Label log4 = new Label("Email Server Synced");
        log4.setStyle("-fx-background-color: #f1f5f9; -fx-padding: 8 12 8 12; -fx-background-radius: 8;");

        nGrid.add(log1, 0, 0);
        nGrid.add(log2, 1, 0);
        nGrid.add(log3, 0, 1);
        nGrid.add(log4, 1, 1);

        Button viewNotifs = UIHelper.createPrimaryButton("View all notifications");
        notifCard.getChildren().addAll(nTitle, nGrid, viewNotifs);

        HBox middleRow = new HBox(16, apptCard, notifCard);
        HBox.setHgrow(apptCard, Priority.ALWAYS);
        HBox.setHgrow(notifCard, Priority.ALWAYS);

        // Bottom row: User Management Overview & Quick Actions (Fixed typo "Generate Reoprts" -> "Generate Reports")
        VBox userOverviewCard = UIHelper.createCard(16);
        Label uTitle = new Label("User Management Overview");
        uTitle.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: #1e293b;");

        HBox chartRow = new HBox(20);
        chartRow.setAlignment(Pos.CENTER_LEFT);

        VBox donutMock = new VBox(4);
        donutMock.setAlignment(Pos.CENTER);
        donutMock.setPrefSize(90, 90);
        donutMock.setStyle("-fx-border-color: #1a608f; -fx-border-width: 6; -fx-border-radius: 45; -fx-background-radius: 45;");
        Label totalUsersLbl = new Label("1,278\nUsers");
        totalUsersLbl.setAlignment(Pos.CENTER);
        totalUsersLbl.setStyle("-fx-font-weight: bold; -fx-text-fill: #1a608f; -fx-font-size: 11px;");
        donutMock.getChildren().add(totalUsersLbl);

        VBox legend = new VBox(6);
        legend.getChildren().addAll(
                new Label("■ Patients: 1,248 (78%)"),
                new Label("■ Nurses: 18 (11%)"),
                new Label("■ Doctors: 10 (6%)"),
                new Label("■ Admins: 2 (5%)")
        );
        for (Node l : legend.getChildren()) ((Label)l).setStyle("-fx-font-size: 12px; -fx-text-fill: #475569;");

        chartRow.getChildren().addAll(donutMock, legend);
        userOverviewCard.getChildren().addAll(uTitle, chartRow);

        VBox quickCard = UIHelper.createCard(16);
        Label qTitle = new Label("Quick Actions");
        qTitle.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: #1e293b;");

        GridPane qGrid = new GridPane();
        qGrid.setHgap(10);
        qGrid.setVgap(10);

        Button btn1 = UIHelper.createSecondaryButton("Add New patient");
        Button btn2 = UIHelper.createSecondaryButton("Manage Appointments");
        Button btn3 = UIHelper.createSecondaryButton("Add new staff");
        Button btn4 = UIHelper.createSecondaryButton("Generate Reports"); // Fixed typo "Reoprts"

        btn1.setOnAction(e -> showCreateAccount());
        btn3.setOnAction(e -> {
            setActiveNavItem("User Management");
            navigateView("User Management");
        });

        qGrid.add(btn1, 0, 0);
        qGrid.add(btn2, 1, 0);
        qGrid.add(btn3, 0, 1);
        qGrid.add(btn4, 1, 1);
        quickCard.getChildren().addAll(qTitle, qGrid);

        HBox bottomRow = new HBox(16, userOverviewCard, quickCard);
        HBox.setHgrow(userOverviewCard, Priority.ALWAYS);
        HBox.setHgrow(quickCard, Priority.ALWAYS);

        contentContainer.getChildren().addAll(header, sub, stats, middleRow, bottomRow);
    }

    private void renderAdminUserManagementView() {
        Label header = new Label("Staff Role & User Account Management");
        header.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #16466f;");

        VBox formCard = UIHelper.createCard(16);
        Label fTitle = new Label("Create Staff or User Account");
        fTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #1e293b;");

        TextField uName = new TextField();
        uName.setPromptText("e.g. dr.reena");
        TextField dName = new TextField();
        dName.setPromptText("e.g. Dr. Reena Sharma");
        PasswordField pass = new PasswordField();
        pass.setPromptText("Temporary password");

        ComboBox<String> roleCombo = new ComboBox<>();
        roleCombo.getItems().addAll("NURSE", "DOCTOR", "PHARMACY", "PATIENT", "ADMIN");
        roleCombo.setValue("DOCTOR");

        String inputStyle = "-fx-background-radius: 8; -fx-padding: 7; -fx-background-color: #f8fafc; -fx-border-color: #cbd5e1; -fx-border-radius: 8;";
        uName.setStyle(inputStyle);
        dName.setStyle(inputStyle);
        pass.setStyle(inputStyle);
        roleCombo.setStyle(inputStyle);

        Label msg = new Label();
        msg.setStyle("-fx-font-weight: bold; -fx-font-size: 12px;");

        ListView<String> userList = new ListView<>();
        userList.setPrefHeight(180);
        userList.getItems().setAll(store.userSummaries());

        Button createBtn = UIHelper.createPrimaryButton("Create Account");
        createBtn.setOnAction(e -> {
            String u = uName.getText().trim();
            String p = pass.getText();
            String d = dName.getText().trim();
            if (u.isEmpty() || p.isEmpty()) {
                msg.setText("Please enter username and password.");
                msg.setStyle("-fx-text-fill: #dc2626; -fx-font-weight: bold;");
                return;
            }
            boolean success = store.createUser(u, p, roleCombo.getValue(), d.isEmpty() ? u : d);
            if (success) {
                msg.setText("Account created successfully!");
                msg.setStyle("-fx-text-fill: #16a34a; -fx-font-weight: bold;");
                userList.getItems().setAll(store.userSummaries());
                uName.clear();
                dName.clear();
                pass.clear();
            } else {
                msg.setText("Username already exists.");
                msg.setStyle("-fx-text-fill: #dc2626; -fx-font-weight: bold;");
            }
        });

        GridPane g = new GridPane();
        g.setHgap(14);
        g.setVgap(8);
        g.add(new Label("Username"), 0, 0);
        g.add(uName, 0, 1);
        g.add(new Label("Display Name"), 1, 0);
        g.add(dName, 1, 1);
        g.add(new Label("Temporary Password"), 0, 2);
        g.add(pass, 0, 3);
        g.add(new Label("Assigned Role"), 1, 2);
        g.add(roleCombo, 1, 3);

        formCard.getChildren().addAll(fTitle, g, createBtn, msg);

        VBox listCard = UIHelper.createCard(16);
        Label lTitle = new Label("Existing Accounts & Role Assignments");
        lTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #1e293b;");
        listCard.getChildren().addAll(lTitle, userList);

        contentContainer.getChildren().addAll(header, formCard, listCard);
    }

    private void renderAdminLogsView() {
        Label header = new Label("System Logs & Activity Audit");
        header.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #16466f;");

        VBox logCard = UIHelper.createCard(16);
        ListView<String> logs = new ListView<>();
        logs.setPrefHeight(260);
        logs.getItems().setAll(store.systemLogs());
        logCard.getChildren().addAll(new Label("Recent Events"), logs);

        contentContainer.getChildren().addAll(header, logCard);
    }

    // =========================================================================
    // 8. PHARMACY CONSOLE SCREENS (Figure 15)
    // =========================================================================

    // FIGURE 15 – Pharmacy Dashboard (Fixed typos: "prespricptions", "Fiiled", "Generate Reoprts")
    private void renderPharmacyDashboard() {
        try { List<String[]> dbRows = store.pharmacyQueue(currentUser.username()); if (currentUser.staffId().endsWith("-DB")) { renderDatabasePharmacyDashboard(dbRows); return; } } catch (Exception ignored) { }
        Label header = new Label("Pharmacy Dashboard");
        header.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #16466f;");
        Label sub = new Label("Manage prescriptions and medications"); // Fixed typo "prespricptions"
        sub.setStyle("-fx-font-size: 12px; -fx-text-fill: #64748b;");

        // 4 Stat Cards
        HBox stats = new HBox(16);
        stats.getChildren().addAll(
                UIHelper.createMetricCard("8", "New Prescriptions", "View all"),
                UIHelper.createMetricCard("12", "Ready For Pickup", "View all"),
                UIHelper.createMetricCard("5", "Low Stock Items", "View all"),
                UIHelper.createMetricCard("6", "Refill Requests", "View all")
        );

        // Recent Prescriptions Table
        VBox rxCard = UIHelper.createCard(16);
        Label rTitle = new Label("Recent Prescriptions");
        rTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #1e293b;");

        TableView<Prescription> rxTable = new TableView<>();
        rxTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        rxTable.setPrefHeight(180);

        TableColumn<Prescription, String> colId = new TableColumn<>("Prescription ID");
        colId.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().id()));

        TableColumn<Prescription, String> colName = new TableColumn<>("Name");
        colName.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().patientName()));

        TableColumn<Prescription, String> colDoc = new TableColumn<>("Doctor");
        colDoc.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().prescribedBy()));

        TableColumn<Prescription, String> colMed = new TableColumn<>("Medication");
        colMed.setCellValueFactory(p -> new SimpleStringProperty(p.getValue().medication()));

        TableColumn<Prescription, Void> colStatus = new TableColumn<>("Status");
        colStatus.setCellFactory(c -> new TableCell<>() {
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) setGraphic(null);
                else {
                    Prescription p = getTableView().getItems().get(getIndex());
                    setGraphic(UIHelper.createStatusBadge(p.status()));
                }
            }
        });

        TableColumn<Prescription, Void> colAction = new TableColumn<>("Action");
        colAction.setCellFactory(c -> new TableCell<>() {
            private final Button btn = new Button();
            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) setGraphic(null);
                else {
                    Prescription p = getTableView().getItems().get(getIndex());
                    if ("New".equalsIgnoreCase(p.status())) {
                        btn.setText("Process (Ready)");
                        btn.setStyle("-fx-background-color: #2b77af; -fx-text-fill: white; -fx-font-size: 11px; -fx-background-radius: 6;");
                        btn.setOnAction(e -> {
                            p.setStatus("Ready");
                            rxTable.refresh();
                        });
                    } else if ("Ready".equalsIgnoreCase(p.status())) {
                        btn.setText("Dispense (Filled)"); // Fixed typo "Fiiled"
                        btn.setStyle("-fx-background-color: #2ea066; -fx-text-fill: white; -fx-font-size: 11px; -fx-background-radius: 6;");
                        btn.setOnAction(e -> {
                            p.setStatus("Filled");
                            rxTable.refresh();
                        });
                    } else {
                        btn.setText("Completed");
                        btn.setStyle("-fx-background-color: #e2e8f0; -fx-text-fill: #64748b; -fx-font-size: 11px; -fx-background-radius: 6;");
                        btn.setDisable(true);
                    }
                    setGraphic(btn);
                }
            }
        });

        rxTable.getColumns().addAll(colId, colName, colDoc, colMed, colStatus, colAction);
        rxTable.getItems().setAll(store.allPrescriptions());

        rxCard.getChildren().addAll(rTitle, rxTable);

        // Low Stock Alert Table & Quick Actions
        VBox stockCard = UIHelper.createCard(16);
        Label sTitle = new Label("Low Stock Alert");
        sTitle.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: #1e293b;");

        TableView<InventoryItem> stockTable = new TableView<>();
        stockTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        stockTable.setPrefHeight(130);

        TableColumn<InventoryItem, String> colM = new TableColumn<>("Medication");
        colM.setCellValueFactory(i -> new SimpleStringProperty(i.getValue().medication()));

        TableColumn<InventoryItem, String> colC = new TableColumn<>("Category");
        colC.setCellValueFactory(i -> new SimpleStringProperty(i.getValue().category()));

        TableColumn<InventoryItem, String> colS = new TableColumn<>("Stock left");
        colS.setCellValueFactory(i -> new SimpleStringProperty(String.valueOf(i.getValue().stockLeft())));

        stockTable.getColumns().addAll(colM, colC, colS);
        stockTable.getItems().setAll(store.inventory());

        stockCard.getChildren().addAll(sTitle, stockTable);

        // Quick Actions (Fixed typo "Generate Reoprts" -> "Generate Reports")
        VBox quickCard = UIHelper.createCard(16);
        Label qTitle = new Label("Quick Actions");
        qTitle.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: #1e293b;");

        GridPane qGrid = new GridPane();
        qGrid.setHgap(10);
        qGrid.setVgap(10);

        Button q1 = UIHelper.createSecondaryButton("Add New medication");
        Button q2 = UIHelper.createSecondaryButton("Process prescription");
        Button q3 = UIHelper.createSecondaryButton("Manage Inventory");
        Button q4 = UIHelper.createSecondaryButton("Generate Reports"); // Fixed typo "Reoprts"

        qGrid.add(q1, 0, 0);
        qGrid.add(q2, 1, 0);
        qGrid.add(q3, 0, 1);
        qGrid.add(q4, 1, 1);

        quickCard.getChildren().addAll(qTitle, qGrid);

        HBox splitBottom = new HBox(16, stockCard, quickCard);
        HBox.setHgrow(stockCard, Priority.ALWAYS);
        HBox.setHgrow(quickCard, Priority.ALWAYS);

        contentContainer.getChildren().addAll(header, sub, stats, rxCard, splitBottom);
    }

    private void renderPharmacyInventoryView() {
        Label header = new Label("Medication Inventory Management");
        header.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #16466f;");

        VBox card = UIHelper.createCard(16);
        TableView<InventoryItem> table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setPrefHeight(260);

        TableColumn<InventoryItem, String> colMed = new TableColumn<>("Medication");
        colMed.setCellValueFactory(i -> new SimpleStringProperty(i.getValue().medication()));

        TableColumn<InventoryItem, String> colCat = new TableColumn<>("Category");
        colCat.setCellValueFactory(i -> new SimpleStringProperty(i.getValue().category()));

        TableColumn<InventoryItem, String> colStock = new TableColumn<>("Stock Units Available");
        colStock.setCellValueFactory(i -> new SimpleStringProperty(String.valueOf(i.getValue().stockLeft())));

        table.getColumns().addAll(colMed, colCat, colStock);
        table.getItems().setAll(store.inventory());

        card.getChildren().addAll(new Label("Current Inventory Levels"), table);
        contentContainer.getChildren().addAll(header, card);
    }

    private void renderPharmacyReportsView() {
        Label header = new Label("Pharmacy Dispensing Reports");
        header.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #16466f;");

        VBox card = UIHelper.createCard(16);
        card.getChildren().addAll(
                new Label("Monthly Dispense Volume: 342 prescriptions"),
                new Label("Antibiotic Usage Rate: 42%"),
                new Label("Average Turnaround Time: 18 minutes"),
                UIHelper.createPrimaryButton("Export PDF Summary")
        );
        contentContainer.getChildren().addAll(header, card);
    }

    private void showHospitalRegistration() {
        VBox card = UIHelper.createCard(18); card.setMaxWidth(560);
        Label title = new Label("Register your hospital"); title.setStyle("-fx-font-size:24px;-fx-font-weight:bold;-fx-text-fill:#19446c;");
        TextField name=new TextField();name.setPromptText("Hospital / clinic name"); TextField admin=new TextField();admin.setPromptText("Admin username");
        TextField email=new TextField();email.setPromptText("Admin email"); TextField phone=new TextField();phone.setPromptText("Phone number");
        TextField address=new TextField();address.setPromptText("Full hospital address"); PasswordField pass=new PasswordField();pass.setPromptText("Password (minimum 10 characters)");
        Label msg=new Label();msg.setWrapText(true); Button submit=UIHelper.createPrimaryButton("Create hospital workspace");
        submit.setOnAction(e->{try{if(name.getText().isBlank()||admin.getText().isBlank()||email.getText().isBlank()||phone.getText().isBlank()||address.getText().isBlank()||pass.getText().length()<10)throw new IllegalArgumentException("Complete all fields. Password must be at least 10 characters.");store.registerHospital(name.getText(),admin.getText(),email.getText(),pass.getText(),phone.getText(),address.getText());msg.setStyle("-fx-text-fill:#047857;");msg.setText("Hospital registered. Use Hospital Login with the admin email and password you created.");submit.setDisable(true);}catch(Exception ex){msg.setStyle("-fx-text-fill:#b91c1c;");msg.setText(ex.getMessage());}});
        Button back=new Button("Back to login");back.setOnAction(e->showLogin("Patient")); card.getChildren().addAll(LogoUtil.createLogo(58),title,new Label("Your hospital workspace is created separately."),name,admin,email,phone,address,pass,submit,msg,back);
        StackPane root=new StackPane(card);root.setStyle("-fx-background-color:#20557c;");root.setPadding(new Insets(24));stage.setScene(createStyledScene(root,960,720));
    }

    private void renderAppointmentBooking() {
        Label title=new Label("Find a hospital and book an appointment");title.setStyle("-fx-font-size:24px;-fx-font-weight:bold;-fx-text-fill:#16466f;");
        VBox card=UIHelper.createCard(16); ComboBox<String[]> hospitals=new ComboBox<>();hospitals.getItems().setAll(store.hospitalsDirectory());hospitals.setPromptText("Select registered hospital");hospitals.setMaxWidth(Double.MAX_VALUE);hospitals.setConverter(new javafx.util.StringConverter<>(){public String toString(String[] v){return v==null?"":v[1]+" · "+v[2];}public String[] fromString(String s){return null;}});
        ComboBox<String[]> doctors=new ComboBox<>();doctors.setPromptText("Select doctor");doctors.setMaxWidth(Double.MAX_VALUE);doctors.setConverter(new javafx.util.StringConverter<>(){public String toString(String[] v){return v==null?"":v[1]+" — "+v[2]+" (Treats: "+v[3]+")";}public String[] fromString(String s){return null;}});
        hospitals.valueProperty().addListener((o,a,b)->{doctors.getItems().clear();if(b!=null)doctors.getItems().setAll(store.doctorsDirectory(b[0]));});
        DatePicker date=new DatePicker(LocalDate.now().plusDays(1));date.setDayCellFactory(p->new DateCell(){@Override public void updateItem(LocalDate d,boolean empty){super.updateItem(d,empty);setDisable(empty||d.isBefore(LocalDate.now()));}});
        ComboBox<String> slot=new ComboBox<>();slot.getItems().addAll("09:00 AM","09:30 AM","10:00 AM","10:30 AM","11:00 AM","11:30 AM","12:00 PM","02:00 PM","02:30 PM","03:00 PM","03:30 PM","04:00 PM");slot.setPromptText("Available time slot");
        TextField reason=new TextField();reason.setPromptText("Reason for visit"); Label status=new Label();status.setWrapText(true);
        Button book=UIHelper.createPrimaryButton("Request appointment");book.setOnAction(e->{try{if(hospitals.getValue()==null||doctors.getValue()==null||date.getValue()==null||slot.getValue()==null||reason.getText().isBlank())throw new IllegalArgumentException("Choose a hospital, doctor, date, slot and reason.");store.bookAppointment(currentUser.username(),hospitals.getValue()[0],doctors.getValue()[0],date.getValue().toString(),slot.getValue(),reason.getText());status.setStyle("-fx-text-fill:#047857;");status.setText("Appointment request sent. Status: Pending hospital approval.");}catch(Exception ex){status.setStyle("-fx-text-fill:#b91c1c;");status.setText(ex.getMessage());}});
        for(Control c:List.of(hospitals,doctors,date,slot,reason))c.setMaxWidth(Double.MAX_VALUE);
        GridPane g=new GridPane();g.setHgap(14);g.setVgap(12);g.add(new Label("Hospital"),0,0);g.add(hospitals,0,1,2,1);g.add(new Label("Doctor · Specialization · Treats"),0,2);g.add(doctors,0,3,2,1);g.add(new Label("Date"),0,4);g.add(date,0,5);g.add(new Label("Time"),1,4);g.add(slot,1,5);g.add(new Label("Reason"),0,6);g.add(reason,0,7,2,1);GridPane.setHgrow(hospitals,Priority.ALWAYS);GridPane.setHgrow(doctors,Priority.ALWAYS);GridPane.setHgrow(reason,Priority.ALWAYS);
        card.getChildren().addAll(g,book,status);contentContainer.getChildren().addAll(title,new Label("Choose a registered hospital to see its doctors and their specialties."),card);
    }

    private void renderMyAppointments(){Label title=new Label("My appointments");title.setStyle("-fx-font-size:24px;-fx-font-weight:bold;-fx-text-fill:#16466f;");TableView<String[]> table=new TableView<>();table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);String[] heads={"Hospital","Doctor","Date","Time","Reason","Status"};for(int i=0;i<heads.length;i++){final int n=i+1;TableColumn<String[],String> c=new TableColumn<>(heads[i]);c.setCellValueFactory(v->new SimpleStringProperty(v.getValue()[n]));table.getColumns().add(c);}table.getItems().setAll(store.patientBookings(currentUser.username()));contentContainer.getChildren().addAll(title,new Label("Your appointment status updates as the hospital reviews and your doctor completes each visit."),table);}


    private void renderHospitalAdminView(String view){Label title=new Label(view);title.setStyle("-fx-font-size:24px;-fx-font-weight:bold;-fx-text-fill:#16466f;");VBox card=UIHelper.createCard(16);
        if(currentUser.role().equals("HOSPITAL_ADMIN")){try{String[] m=store.hospitalSummary(currentUser.username());HBox metrics=new HBox(10,UIHelper.createMetricCard(m[0],"Appointments",null),UIHelper.createMetricCard(m[1],"Pending",null),UIHelper.createMetricCard(m[2],"Approved",null),UIHelper.createMetricCard(m[3],"Completed",null),UIHelper.createMetricCard(m[4],"Doctors",null),UIHelper.createMetricCard(m[5],"Nurses",null),UIHelper.createMetricCard(m[6],"Pharmacy staff",null),UIHelper.createMetricCard(m[7],"Patients",null));contentContainer.getChildren().add(metrics);}catch(Exception ex){contentContainer.getChildren().add(new Label(ex.getMessage()));}}
        if(view.equals("Manage Staff")||view.equals("Hospital Dashboard")){TextField name=new TextField();name.setPromptText("Staff full name");TextField username=new TextField();username.setPromptText("Staff username");TextField email=new TextField();email.setPromptText("Staff email");PasswordField password=new PasswordField();password.setPromptText("Temporary password (10+ characters)");ComboBox<String> role=new ComboBox<>();role.getItems().addAll("DOCTOR","NURSE","PHARMACY");role.setPromptText("Staff role");TextField specialty=new TextField();specialty.setPromptText("Doctor specialization (doctors only)");TextField treats=new TextField();treats.setPromptText("Conditions treated / clinical focus");Label status=new Label();Button add=UIHelper.createPrimaryButton("Create staff account");add.setOnAction(e->{try{if(role.getValue()==null)throw new IllegalArgumentException("Select a staff role.");if(password.getText().length()<10)throw new IllegalArgumentException("Use a password of at least 10 characters.");if(role.getValue().equals("DOCTOR")&&(specialty.getText().isBlank()||treats.getText().isBlank()))throw new IllegalArgumentException("Doctors need a specialization and treatment details.");store.createHospitalStaff(currentUser.username(),username.getText(),email.getText(),password.getText(),role.getValue(),name.getText(),specialty.getText(),treats.getText());status.setStyle("-fx-text-fill:#047857;");status.setText("Staff account created. They sign in through Hospital Login.");}catch(Exception ex){status.setStyle("-fx-text-fill:#b91c1c;");status.setText(ex.getMessage());}});card.getChildren().addAll(new Label("Create staff accounts for this hospital only"),name,username,email,password,role,specialty,treats,add,status);
            TableView<String[]> staffTable=new TableView<>();String[] sh={"Username","Email","Role","Name"};for(int i=0;i<sh.length;i++){final int n=i;TableColumn<String[],String> c=new TableColumn<>(sh[i]);c.setCellValueFactory(v->new SimpleStringProperty(v.getValue()[n]));staffTable.getColumns().add(c);}staffTable.getItems().setAll(store.hospitalStaff(currentUser.username()));Button remove=UIHelper.createPrimaryButton("Deactivate selected staff");remove.setOnAction(e->{String[] row=staffTable.getSelectionModel().getSelectedItem();if(row==null)return;try{store.removeHospitalStaff(currentUser.username(),row[0]);staffTable.getItems().setAll(store.hospitalStaff(currentUser.username()));}catch(Exception ex){new Alert(Alert.AlertType.ERROR,ex.getMessage()).showAndWait();}});card.getChildren().addAll(new Label("Active staff in this hospital"),staffTable,remove);}
        if(view.equals("Appointments")||view.equals("Hospital Dashboard")){TableView<String[]> table=new TableView<>();String[] heads={"ID","Patient","Doctor","Date","Slot","Reason","Status"};for(int i=0;i<heads.length;i++){final int n=i;TableColumn<String[],String> c=new TableColumn<>(heads[i]);c.setCellValueFactory(v->new SimpleStringProperty(v.getValue()[n]));table.getColumns().add(c);}table.getItems().setAll(store.hospitalBookings(currentUser.username()));Button approve=UIHelper.createPrimaryButton("Approve selected");Button reject=UIHelper.createSecondaryButton("Reject selected");approve.setOnAction(e->{String[] row=table.getSelectionModel().getSelectedItem();if(row==null)return;try{store.setBookingStatus(currentUser.username(),row[0],"APPROVED");table.getItems().setAll(store.hospitalBookings(currentUser.username()));}catch(Exception ex){new Alert(Alert.AlertType.ERROR,ex.getMessage()).showAndWait();}});reject.setOnAction(e->{String[] row=table.getSelectionModel().getSelectedItem();if(row==null)return;try{store.setBookingStatus(currentUser.username(),row[0],"REJECTED");table.getItems().setAll(store.hospitalBookings(currentUser.username()));}catch(Exception ex){new Alert(Alert.AlertType.ERROR,ex.getMessage()).showAndWait();}});card.getChildren().addAll(new Label("Appointments for your hospital only · approve or reject pending requests"),table,new HBox(10,approve,reject));}
        if(view.equals("Hospital Information")){try{String[] d=store.hospitalDetails(currentUser.username());TextField n=new TextField(d[0]),em=new TextField(d[1]),ph=new TextField(d[2]),ad=new TextField(d[3]);Button save=UIHelper.createPrimaryButton("Save hospital information");Label msg=new Label();save.setOnAction(e->{try{store.updateHospitalDetails(currentUser.username(),n.getText(),em.getText(),ph.getText(),ad.getText());msg.setText("Hospital details saved.");}catch(Exception ex){msg.setText(ex.getMessage());}});card.getChildren().addAll(new Label("Hospital name"),n,new Label("Contact email"),em,new Label("Phone"),ph,new Label("Address"),ad,save,msg);}catch(Exception ex){card.getChildren().add(new Label(ex.getMessage()));}}
        if(view.equals("Hospital Activity")){TableView<String[]> activity=new TableView<>();activity.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);String[] ah={"Time","Account","Activity","Details"};for(int i=0;i<ah.length;i++){final int n=i;TableColumn<String[],String> c=new TableColumn<>(ah[i]);c.setCellValueFactory(v->new SimpleStringProperty(v.getValue()[n]));activity.getColumns().add(c);}activity.getItems().setAll(store.hospitalActivity(currentUser.username()));card.getChildren().addAll(new Label("Recent activity for this hospital only"),activity);}
        contentContainer.getChildren().addAll(title,card);}

    private void renderPlatformAdminView(String view){Label title=new Label("Universal Admin · "+view);title.setStyle("-fx-font-size:24px;-fx-font-weight:bold;-fx-text-fill:#16466f;");VBox card=UIHelper.createCard(16);TableView<String[]> table=new TableView<>();String[] heads={"Hospital ID","Hospital","Admin email","Phone","Address"};for(int i=0;i<heads.length;i++){final int n=i;TableColumn<String[],String> c=new TableColumn<>(heads[i]);c.setCellValueFactory(v->new SimpleStringProperty(v.getValue()[n]));table.getColumns().add(c);}table.getItems().setAll(store.hospitalsDirectory());card.getChildren().addAll(new Label("Registered hospital directory"),table);
        if(view.equals("Patients")||view.equals("Platform Overview")){TableView<String[]> accounts=new TableView<>();String[] ah={"Username","Email","Role","Name","Tenant"};for(int i=0;i<ah.length;i++){final int n=i;TableColumn<String[],String> c=new TableColumn<>(ah[i]);c.setCellValueFactory(v->new SimpleStringProperty(v.getValue()[n]));accounts.getColumns().add(c);}accounts.getItems().setAll(store.platformAccounts());card.getChildren().addAll(new Label("All platform accounts"),accounts);}
        if(view.equals("All Appointments")){TableView<String[]> bookings=new TableView<>();String[] bh={"Hospital","Patient","Doctor","Date","Slot","Reason","Status"};for(int i=0;i<bh.length;i++){final int n=i;TableColumn<String[],String> c=new TableColumn<>(bh[i]);c.setCellValueFactory(v->new SimpleStringProperty(v.getValue()[n]));bookings.getColumns().add(c);}bookings.getItems().setAll(store.platformAppointments());card.getChildren().addAll(new Label("All platform appointments"),bookings);}
        card.getChildren().add(new Label("Universal administrator access is global. Hospital administrators remain restricted to their own tenant."));contentContainer.getChildren().addAll(title,card);}

    private void renderDatabasePharmacyInventory(){Label title=new Label("Medication Inventory");title.setStyle("-fx-font-size:24px;-fx-font-weight:bold;-fx-text-fill:#16466f;");TableView<String[]> t=new TableView<>();t.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);String[] h={"Medicine","Category","Available units","Last updated"};for(int i=0;i<h.length;i++){final int n=i;TableColumn<String[],String> c=new TableColumn<>(h[i]);c.setCellValueFactory(v->new SimpleStringProperty(v.getValue()[n]));t.getColumns().add(c);}t.getItems().setAll(store.pharmacyInventory(currentUser.username()));if(t.getItems().isEmpty())t.setPlaceholder(new Label("No inventory has been entered for this hospital."));TextField medicine=new TextField();medicine.setPromptText("Medicine name (must match prescription)");TextField category=new TextField();category.setPromptText("Category");TextField quantity=new TextField();quantity.setPromptText("Available units");Label msg=new Label();Button save=UIHelper.createPrimaryButton("Save stock");save.setOnAction(e->{try{int q=Integer.parseInt(quantity.getText());store.savePharmacyStock(currentUser.username(),medicine.getText(),category.getText(),q);t.getItems().setAll(store.pharmacyInventory(currentUser.username()));msg.setText("Inventory updated.");}catch(Exception ex){msg.setText(ex.getMessage());}});contentContainer.getChildren().addAll(title,new Label("Stock is stored per hospital. Fulfilling a prescription consumes one unit from the matching medicine record."),t,new Label("Add or update stock"),medicine,category,quantity,save,msg);}

    private void renderDatabasePatientDashboard(){
        String name=currentUser.displayName();Label title=new Label("Welcome, "+name);title.setStyle("-fx-font-size:24px;-fx-font-weight:bold;-fx-text-fill:#16466f;");List<String[]> appts=store.patientBookings(currentUser.username()),records=store.medicalRecords(currentUser.username()),notices=store.notifications(currentUser.username());long pending=appts.stream().filter(a->a[6].equals("PENDING")).count(),approved=appts.stream().filter(a->a[6].equals("APPROVED")).count(),completed=appts.stream().filter(a->a[6].equals("COMPLETED")).count();HBox stats=new HBox(12,UIHelper.createMetricCard(String.valueOf(approved),"Upcoming / approved",null),UIHelper.createMetricCard(String.valueOf(pending),"Pending requests",null),UIHelper.createMetricCard(String.valueOf(completed),"Completed visits",null),UIHelper.createMetricCard(String.valueOf(notices.size()),"Notifications",null));
        TableView<String[]> table=new TableView<>();table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);String[] h={"Hospital","Doctor","Date","Time","Reason","Status"};int[] ix={1,2,3,4,5,6};for(int i=0;i<h.length;i++){final int n=ix[i];TableColumn<String[],String> c=new TableColumn<>(h[i]);c.setCellValueFactory(v->new SimpleStringProperty(v.getValue()[n]));table.getColumns().add(c);}table.getItems().setAll(appts);table.setPrefHeight(220);
        VBox recent=UIHelper.createCard(12);recent.getChildren().add(new Label("Recent medical activity"));for(String[] r:records.stream().limit(4).toList())recent.getChildren().add(new Label(r[0]+" · "+r[2]+(r[4].isBlank()?"":" · "+r[4])));if(records.isEmpty())recent.getChildren().add(new Label("Completed examinations and prescriptions will appear here."));VBox alerts=UIHelper.createCard(12);alerts.getChildren().add(new Label("Notifications"));for(String[] n:notices.stream().limit(4).toList())alerts.getChildren().add(new Label(n[1]));if(notices.isEmpty())alerts.getChildren().add(new Label("You're all caught up."));contentContainer.getChildren().addAll(title,stats,new Label("My appointments"),table,new HBox(12,recent,alerts));
    }

    private void renderDatabaseMessaging(List<String[]> contacts) {
        Label title=new Label("Care Messages");title.setStyle("-fx-font-size:24px;-fx-font-weight:bold;-fx-text-fill:#16466f;");
        HBox layout=new HBox(14);layout.setPrefHeight(520);
        VBox left=UIHelper.createCard(10);left.setPrefWidth(255);left.setMinWidth(225);TextField search=new TextField();search.setPromptText("Search people");ListView<String[]> list=new ListView<>();List<String[]> allContacts=new java.util.ArrayList<>();list.setCellFactory(v->new ListCell<>(){@Override protected void updateItem(String[] x,boolean empty){super.updateItem(x,empty);setText(empty||x==null?null:x[1]+"  ·  "+x[2]+(x.length>3&&!x[3].isBlank()?"\n"+x[3]:"")+(x.length>4&&!x[4].isBlank()?" · "+x[4]:"")+(x.length>5&&Integer.parseInt(x[5])>0?"\n"+x[5]+" unread":""));}});
        ComboBox<String[]> hospitalPicker=new ComboBox<>();hospitalPicker.setMaxWidth(Double.MAX_VALUE);hospitalPicker.setPromptText("Select your hospital");hospitalPicker.setConverter(new javafx.util.StringConverter<>(){public String toString(String[] h){return h==null?"":h[1];}public String[] fromString(String x){return null;}});
        if(currentUser.role().equals("PATIENT")){hospitalPicker.getItems().setAll(store.hospitalsDirectory());hospitalPicker.valueProperty().addListener((o,a,b)->{if(b!=null){try{store.linkPatientHospital(currentUser.username(),b[0]);allContacts.clear();allContacts.addAll(store.messageContacts(currentUser.username(),b[0]));list.getItems().setAll(allContacts);}catch(Exception ex){new Alert(Alert.AlertType.ERROR,ex.getMessage()).showAndWait();}}});}else {allContacts.addAll(contacts);list.getItems().setAll(allContacts);}
        search.textProperty().addListener((o,a,b)->list.getItems().setAll(allContacts.stream().filter(x->(x[1]+x[2]).toLowerCase().contains(b.toLowerCase())).toList()));left.getChildren().addAll(new Label("Conversations"));if(currentUser.role().equals("PATIENT"))left.getChildren().add(hospitalPicker);left.getChildren().addAll(search,list);VBox.setVgrow(list,Priority.ALWAYS);
        VBox chat=UIHelper.createCard(12);HBox.setHgrow(chat,Priority.ALWAYS);Button refreshChat=UIHelper.createSecondaryButton("Refresh");Label partner=new Label("Choose a contact to start messaging");VBox history=new VBox(8);history.setPadding(new Insets(10));ScrollPane scroll=new ScrollPane(history);scroll.setFitToWidth(true);VBox.setVgrow(scroll,Priority.ALWAYS);TextField composer=new TextField();composer.setPromptText("Write a message…");Button send=UIHelper.createPrimaryButton("Send");
        Runnable refresh=()->{String[] c=list.getSelectionModel().getSelectedItem();if(c==null)return;partner.setText(c[1]+" · "+c[2]);history.getChildren().clear();for(String[] m:store.conversation(currentUser.username(),c[0])){boolean mine=m[0].equals(currentUser.username());Label bubble=new Label(m[4]+"\n"+m[5]);bubble.setWrapText(true);bubble.setMaxWidth(420);bubble.setPadding(new Insets(9,12,9,12));bubble.setStyle(mine?"-fx-background-color:#d9fdd3;-fx-background-radius:12;-fx-text-fill:#173b2b;":"-fx-background-color:#f1f5f9;-fx-background-radius:12;-fx-text-fill:#1e293b;");HBox line=new HBox(bubble);line.setAlignment(mine?Pos.CENTER_RIGHT:Pos.CENTER_LEFT);history.getChildren().add(line);}scroll.setVvalue(1.0);};
        list.getSelectionModel().selectedItemProperty().addListener((o,a,b)->refresh.run());refreshChat.setOnAction(e->refresh.run());send.setOnAction(e->{String[] c=list.getSelectionModel().getSelectedItem();if(c==null){new Alert(Alert.AlertType.INFORMATION,"Choose a contact first.").showAndWait();return;}try{store.sendPersistentMessage(currentUser.username(),c[0],composer.getText());composer.clear();refresh.run();}catch(Exception ex){new Alert(Alert.AlertType.ERROR,ex.getMessage()).showAndWait();}});composer.setOnAction(e->send.fire());HBox entry=new HBox(8,composer,send);HBox.setHgrow(composer,Priority.ALWAYS);HBox chatHeader=new HBox(10,partner,refreshChat);chatHeader.setAlignment(Pos.CENTER_LEFT);Region spacer=new Region();HBox.setHgrow(spacer,Priority.ALWAYS);chatHeader.getChildren().add(1,spacer);chat.getChildren().addAll(chatHeader,new Separator(),scroll,entry);layout.getChildren().addAll(left,chat);contentContainer.getChildren().addAll(title,layout);
    }

    private void renderDatabaseDoctorDashboard(List<String[]> rows) {
        Label title=new Label("Doctor Dashboard");title.setStyle("-fx-font-size:24px;-fx-font-weight:bold;-fx-text-fill:#16466f;");long patients=rows.stream().map(r->r[1]).distinct().count(),today=rows.stream().filter(r->r[3].equals(LocalDate.now().toString())).count(),upcoming=rows.stream().filter(r->!r[3].isBlank()&&!java.time.LocalDate.parse(r[3]).isBefore(LocalDate.now())).count(),completed=rows.stream().filter(r->r[6].equals("COMPLETED")).count(),pending=rows.stream().filter(r->r[6].equals("PENDING")).count();HBox metrics=new HBox(12,UIHelper.createMetricCard(String.valueOf(patients),"Total patients",null),UIHelper.createMetricCard(String.valueOf(today),"Today",null),UIHelper.createMetricCard(String.valueOf(upcoming),"Upcoming",null),UIHelper.createMetricCard(String.valueOf(completed),"Completed",null),UIHelper.createMetricCard(String.valueOf(pending),"Pending",null));
        TableView<String[]> table=new TableView<>();table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);String[] heads={"Patient","Date","Time","Reason","Status","Contact"};for(int i=0;i<heads.length;i++){final int n=i==0?2:i==1?3:i==2?4:i==3?5:i==4?6:7;TableColumn<String[],String> c=new TableColumn<>(heads[i]);c.setCellValueFactory(v->new SimpleStringProperty(v.getValue()[n]));table.getColumns().add(c);}table.getItems().setAll(rows);if(rows.isEmpty())table.setPlaceholder(new Label("No appointments are currently assigned to your account."));table.setPrefHeight(280);TextArea patientHistory=new TextArea();patientHistory.setEditable(false);patientHistory.setPrefRowCount(3);patientHistory.setPromptText("Select a patient to review their previous medical records.");table.getSelectionModel().selectedItemProperty().addListener((o,a,b)->{if(b!=null){try{StringBuilder sb=new StringBuilder("Patient: ").append(b[2]).append("\nPhone: ").append(b[7]).append("\n\nPrevious records:\n");for(String[] r:store.staffPatientRecords(currentUser.username(),b[1],"DOCTOR"))sb.append(r[0]).append(" · ").append(r[2]).append(" · ").append(r[4]).append("\n").append(r[3]).append("\n\n");patientHistory.setText(sb.toString());}catch(Exception ex){patientHistory.setText(ex.getMessage());}}});
        TextField diagnosis=new TextField();diagnosis.setPromptText("Diagnosis");TextArea notes=new TextArea();notes.setPromptText("Examination notes");notes.setPrefRowCount(3);TextField medicine=new TextField();medicine.setPromptText("Medication (optional)");TextField dosage=new TextField();dosage.setPromptText("Dosage");TextField instructions=new TextField();instructions.setPromptText("Instructions / duration");Label msg=new Label();Button complete=UIHelper.createPrimaryButton("Save record & mark completed");complete.setOnAction(e->{String[] row=table.getSelectionModel().getSelectedItem();if(row==null)return;try{if(diagnosis.getText().isBlank())throw new IllegalArgumentException("Enter a diagnosis.");store.completeDoctorAppointment(currentUser.username(),row[0],diagnosis.getText(),notes.getText(),medicine.getText(),dosage.getText(),instructions.getText());msg.setText("Examination saved. Appointment marked Completed and prescription sent to pharmacy.");table.getItems().setAll(store.doctorBookings(currentUser.username()));}catch(Exception ex){msg.setText(ex.getMessage());}});
        contentContainer.getChildren().addAll(title,metrics,new Label("Approved appointments and patient details"),table,new Label("Selected patient · contact and previous medical history"),patientHistory,new Label("Complete examination"),diagnosis,notes,medicine,dosage,instructions,complete,msg);
    }

    private void renderDatabaseNurseDashboard(List<String[]> rows) {
        Label title=new Label("Nurse Care Dashboard");title.setStyle("-fx-font-size:24px;-fx-font-weight:bold;-fx-text-fill:#16466f;");long assigned=rows.stream().map(r->r[1]).distinct().count(),today=rows.stream().filter(r->r[3].equals(LocalDate.now().toString())).count(),upcoming=rows.stream().filter(r->!java.time.LocalDate.parse(r[3]).isBefore(LocalDate.now())).count(),tasks=rows.stream().filter(r->r[7].equals("1")).count();HBox metrics=new HBox(12,UIHelper.createMetricCard(String.valueOf(assigned),"Assigned patients",null),UIHelper.createMetricCard(String.valueOf(today),"Today's visits",null),UIHelper.createMetricCard(String.valueOf(upcoming),"Upcoming visits",null),UIHelper.createMetricCard(String.valueOf(tasks),"Pending intake tasks",null));TableView<String[]> table=new TableView<>();table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);String[] heads={"Patient","Date","Time","Reason","Status"};int[] ix={2,3,4,5,6};for(int i=0;i<heads.length;i++){final int n=ix[i];TableColumn<String[],String> c=new TableColumn<>(heads[i]);c.setCellValueFactory(v->new SimpleStringProperty(v.getValue()[n]));table.getColumns().add(c);}table.getItems().setAll(rows);if(rows.isEmpty())table.setPlaceholder(new Label("No approved patient visits are currently assigned."));table.setPrefHeight(280);TextArea patientHistory=new TextArea();patientHistory.setEditable(false);patientHistory.setPrefRowCount(3);patientHistory.setPromptText("Select a patient to view relevant medical history.");table.getSelectionModel().selectedItemProperty().addListener((o,a,b)->{if(b!=null){try{StringBuilder sb=new StringBuilder("Patient: ").append(b[2]).append("\nPrevious doctor records:\n");for(String[] r:store.staffPatientRecords(currentUser.username(),b[1],"NURSE"))sb.append(r[0]).append(" · ").append(r[2]).append(" · ").append(r[4]).append("\n").append(r[3]).append("\n\n");patientHistory.setText(sb.toString());}catch(Exception ex){patientHistory.setText(ex.getMessage());}}});TextField temp=new TextField();temp.setPromptText("Temperature");TextField pulse=new TextField();pulse.setPromptText("Pulse");TextField weight=new TextField();weight.setPromptText("Weight");TextArea notes=new TextArea();notes.setPromptText("Nursing observations and care notes");notes.setPrefRowCount(3);Label msg=new Label();Button save=UIHelper.createPrimaryButton("Save nursing observations");save.setOnAction(e->{String[] row=table.getSelectionModel().getSelectedItem();if(row==null)return;try{store.saveNurseVitals(currentUser.username(),row[0],temp.getText(),pulse.getText(),weight.getText(),notes.getText());msg.setText("Nursing observations saved.");table.getItems().setAll(store.nurseBookings(currentUser.username()));}catch(Exception ex){msg.setText(ex.getMessage());}});contentContainer.getChildren().addAll(title,metrics,new Label("Approved patient visits assigned to this hospital"),table,new Label("Selected patient · relevant medical history"),patientHistory,new Label("Record observations (nurses cannot prescribe or change appointment status)"),temp,pulse,weight,notes,save,msg);
    }

    private void renderDatabaseMedicalRecords(){Label title=new Label("Medical Records");title.setStyle("-fx-font-size:24px;-fx-font-weight:bold;-fx-text-fill:#16466f;");TableView<String[]> t=new TableView<>();t.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);String[] h={"Date","Doctor","Diagnosis","Examination notes","Medication","Dosage"};for(int i=0;i<h.length;i++){final int n=i;TableColumn<String[],String> c=new TableColumn<>(h[i]);c.setCellValueFactory(v->new SimpleStringProperty(v.getValue()[n]));t.getColumns().add(c);}t.getItems().setAll(store.medicalRecords(currentUser.username()));contentContainer.getChildren().addAll(title,t);}
    private void renderDatabaseMedications(){Label title=new Label("My Medications");title.setStyle("-fx-font-size:24px;-fx-font-weight:bold;-fx-text-fill:#16466f;");TableView<String[]> t=new TableView<>();t.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);String[] h={"Date prescribed","Doctor","Medication","Dosage","Instructions"};int[] ix={0,1,4,5,3};for(int i=0;i<h.length;i++){final int n=ix[i];TableColumn<String[],String> c=new TableColumn<>(h[i]);c.setCellValueFactory(v->new SimpleStringProperty(v.getValue()[n]));t.getColumns().add(c);}t.getItems().setAll(store.medicalRecords(currentUser.username()).stream().filter(r->!r[4].isBlank()).toList());contentContainer.getChildren().addAll(title,new Label("Medication history from your completed examinations. Follow the prescribing clinician's instructions."),t);}
    private void renderDatabaseNotifications(){Label title=new Label("Notifications");title.setStyle("-fx-font-size:24px;-fx-font-weight:bold;-fx-text-fill:#16466f;");VBox box=UIHelper.createCard(12);for(String[] n:store.notifications(currentUser.username()))box.getChildren().add(new Label(n[2]+"  ·  "+n[0]+"\n"+n[1]));if(box.getChildren().isEmpty())box.getChildren().add(new Label("No notifications yet."));contentContainer.getChildren().addAll(title,box);}
    private void renderDatabasePharmacyDashboard(List<String[]> rows){Label title=new Label("Pharmacy Prescription Queue");title.setStyle("-fx-font-size:24px;-fx-font-weight:bold;-fx-text-fill:#16466f;");TableView<String[]> t=new TableView<>();t.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);String[] h={"Patient","Medicine","Dosage","Instructions","Status"};int[] ix={1,2,3,4,5};for(int i=0;i<h.length;i++){final int n=ix[i];TableColumn<String[],String> c=new TableColumn<>(h[i]);c.setCellValueFactory(v->new SimpleStringProperty(v.getValue()[n]));t.getColumns().add(c);}t.getItems().setAll(rows);if(rows.isEmpty())t.setPlaceholder(new Label("No prescriptions have been issued for this hospital yet."));ComboBox<String> status=new ComboBox<>();status.getItems().addAll("IN_PROGRESS","READY","FULFILLED");Button update=UIHelper.createPrimaryButton("Update fulfilment");Label msg=new Label();update.setOnAction(e->{String[] r=t.getSelectionModel().getSelectedItem();if(r==null||status.getValue()==null)return;try{store.updateRx(currentUser.username(),r[0],status.getValue());t.getItems().setAll(store.pharmacyQueue(currentUser.username()));msg.setText("Prescription status updated.");}catch(Exception ex){msg.setText(ex.getMessage());}});contentContainer.getChildren().addAll(title,t,status,update,msg);}

    private Scene createStyledScene(Node root,double width,double height){Scene scene=new Scene((javafx.scene.Parent)root,width,height);java.net.URL css=getClass().getResource("/styles/pediatriccare.css");if(css!=null)scene.getStylesheets().add(css.toExternalForm());return scene;}

}
