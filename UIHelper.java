package pediatriccare;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class UIHelper {

    public static final String COLOR_PRIMARY = "#1e5b88";
    public static final String COLOR_PRIMARY_DARK = "#154264";
    public static final String COLOR_PRIMARY_LIGHT = "#2a77af";
    public static final String COLOR_BG_LIGHT = "#f1f5fa";
    public static final String COLOR_CARD_BG = "#ffffff";
    public static final String COLOR_BORDER = "#dbe5f0";
    public static final String COLOR_TEXT_MAIN = "#142c44";
    public static final String COLOR_TEXT_MUTED = "#64748b";
    public static final String COLOR_GREEN = "#2e9e66";
    public static final String COLOR_YELLOW = "#d97706";
    public static final String COLOR_BLUE = "#2563eb";

    // Application window top chrome (mimicking desktop title bar and macOS dots)
    public static VBox createAppHeader(String windowTitle) {
        HBox bar = new HBox(10);
        bar.setAlignment(Pos.CENTER_LEFT);
        bar.setPadding(new Insets(8, 14, 8, 14));
        bar.setStyle("-fx-background-color: #174266;");

        Label title = new Label(windowTitle);
        title.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 13px;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // macOS-like window control dots
        Circle dotRed = new Circle(5, Color.web("#ef4444"));
        Circle dotYellow = new Circle(5, Color.web("#f59e0b"));
        Circle dotGreen = new Circle(5, Color.web("#10b981"));
        HBox dots = new HBox(6, dotRed, dotYellow, dotGreen);
        dots.setAlignment(Pos.CENTER);

        bar.getChildren().addAll(title, spacer, dots);

        // Desktop App Menu Bar (File, Edit, View, Help)
        MenuBar menuBar = new MenuBar();
        menuBar.setStyle("-fx-background-color: #ffffff; -fx-border-color: #e2e8f0; -fx-border-width: 0 0 1 0;");
        menuBar.getMenus().addAll(
                new Menu("File"),
                new Menu("Edit"),
                new Menu("View"),
                new Menu("Help")
        );

        return new VBox(bar, menuBar);
    }

    // Summary / Metric Card (Big bold number with label)
    public static VBox createMetricCard(String number, String label, String subAction) {
        VBox card = new VBox(4);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(14, 18, 14, 18));
        card.setPrefWidth(180);
        card.setMinWidth(140);
        card.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-border-color: #dbe3ed; -fx-border-radius: 10;");

        Label numLbl = new Label(number);
        numLbl.setStyle("-fx-font-size: 26px; -fx-font-weight: bold; -fx-text-fill: #1d6199;");

        Label textLbl = new Label(label);
        textLbl.setStyle("-fx-font-size: 13px; -fx-text-fill: #334155; -fx-font-weight: bold;");

        card.getChildren().addAll(numLbl, textLbl);

        if (subAction != null && !subAction.isBlank()) {
            Label sub = new Label(subAction);
            sub.setStyle("-fx-font-size: 11px; -fx-text-fill: #64748b;");
            card.getChildren().add(sub);
        }
        return card;
    }

    // Styled patient avatar circle badge (e.g. "SC" in circle)
    public static StackPane createAvatarBadge(String initials, double size) {
        StackPane badge = new StackPane();
        badge.setPrefSize(size, size);
        badge.setMaxSize(size, size);
        badge.setMinSize(size, size);

        Circle circle = new Circle(size / 2.0, Color.web("#1e5a88"));
        Label label = new Label(initials);
        label.setStyle("-fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: " + (size * 0.42) + "px;");

        badge.getChildren().addAll(circle, label);
        return badge;
    }

    // Styled Status Pill Badge (e.g. "Done", "Pending", "Ready", "New", "Filled")
    public static Label createStatusBadge(String text) {
        Label lbl = new Label(text);
        lbl.setPadding(new Insets(3, 10, 3, 10));
        String t = text.toLowerCase();
        if (t.contains("done") || t.contains("ready") || t.contains("filled") || t.contains("complete")) {
            lbl.setStyle("-fx-background-color: #d1fae5; -fx-text-fill: #065f46; -fx-background-radius: 12; -fx-font-weight: bold; -fx-font-size: 11px;");
        } else if (t.contains("pending") || t.contains("waiting")) {
            lbl.setStyle("-fx-background-color: #fef3c7; -fx-text-fill: #92400e; -fx-background-radius: 12; -fx-font-weight: bold; -fx-font-size: 11px;");
        } else if (t.contains("new") || t.contains("room")) {
            lbl.setStyle("-fx-background-color: #dbeafe; -fx-text-fill: #1e40af; -fx-background-radius: 12; -fx-font-weight: bold; -fx-font-size: 11px;");
        } else {
            lbl.setStyle("-fx-background-color: #e2e8f0; -fx-text-fill: #334155; -fx-background-radius: 12; -fx-font-weight: bold; -fx-font-size: 11px;");
        }
        return lbl;
    }

    // Styled Primary Action Button
    public static Button createPrimaryButton(String text) {
        Button btn = new Button(text);
        btn.setStyle("-fx-background-color: #1e5e8c; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8; -fx-padding: 8 16 8 16; -fx-cursor: hand;");
        btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-color: #174b70; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8; -fx-padding: 8 16 8 16; -fx-cursor: hand;"));
        btn.setOnMouseExited(e -> btn.setStyle("-fx-background-color: #1e5e8c; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8; -fx-padding: 8 16 8 16; -fx-cursor: hand;"));
        return btn;
    }

    // Styled Secondary Action Button
    public static Button createSecondaryButton(String text) {
        Button btn = new Button(text);
        btn.setStyle("-fx-background-color: #e2e8f0; -fx-text-fill: #334155; -fx-font-weight: bold; -fx-background-radius: 8; -fx-padding: 8 16 8 16; -fx-cursor: hand;");
        btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-color: #cbd5e1; -fx-text-fill: #1e293b; -fx-font-weight: bold; -fx-background-radius: 8; -fx-padding: 8 16 8 16; -fx-cursor: hand;"));
        btn.setOnMouseExited(e -> btn.setStyle("-fx-background-color: #e2e8f0; -fx-text-fill: #334155; -fx-font-weight: bold; -fx-background-radius: 8; -fx-padding: 8 16 8 16; -fx-cursor: hand;"));
        return btn;
    }

    // Styled Success Green Button
    public static Button createSuccessButton(String text) {
        Button btn = new Button(text);
        btn.setStyle("-fx-background-color: #2ea066; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8; -fx-padding: 8 16 8 16; -fx-cursor: hand;");
        btn.setOnMouseEntered(e -> btn.setStyle("-fx-background-color: #248252; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8; -fx-padding: 8 16 8 16; -fx-cursor: hand;"));
        btn.setOnMouseExited(e -> btn.setStyle("-fx-background-color: #2ea066; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 8; -fx-padding: 8 16 8 16; -fx-cursor: hand;"));
        return btn;
    }

    // Standard card panel
    public static VBox createCard(double padding) {
        VBox card = new VBox(12);
        card.setPadding(new Insets(padding));
        card.setStyle("-fx-background-color: white; -fx-background-radius: 12; -fx-border-color: #dbe4ef; -fx-border-radius: 12;");
        return card;
    }

    // Patient Banner Card (Used across Visit Summary, Nurse Intake, Doctor Exam)
    public static VBox createPatientBanner(Patient p, String allergiesOverride, String lastVisitDate) {
        VBox banner = new VBox(10);
        banner.setPadding(new Insets(14, 18, 14, 18));
        banner.setStyle("-fx-background-color: #e9eef5; -fx-background-radius: 12; -fx-border-color: #d5e0ee; -fx-border-radius: 12;");

        HBox topRow = new HBox(15);
        topRow.setAlignment(Pos.CENTER_LEFT);

        StackPane avatar = createAvatarBadge(p.initials(), 50);

        VBox nameBox = new VBox(3);
        Label name = new Label(p.fullName());
        name.setStyle("-fx-font-size: 19px; -fx-font-weight: bold; -fx-text-fill: #164e79;");

        Label meta = new Label("DOB: " + p.dateOfBirth() + "  |  " + p.gender() + "  |  Age " + p.age() + "  |  Patient ID: " + p.id());
        meta.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: #334155;");
        nameBox.getChildren().addAll(name, meta);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        topRow.getChildren().addAll(avatar, nameBox, spacer);

        // Detail strip
        HBox details = new HBox(25);
        details.setAlignment(Pos.CENTER_LEFT);

        String allergy = allergiesOverride != null ? allergiesOverride : p.allergies();
        VBox allergyBox = new VBox(2, new Label("Allergies:"), new Label(allergy));
        ((Label)allergyBox.getChildren().get(0)).setStyle("-fx-font-size: 11px; -fx-text-fill: #64748b;");
        ((Label)allergyBox.getChildren().get(1)).setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: #dc2626;");

        VBox guardianBox = new VBox(2, new Label("Guardian:"), new Label(p.guardianName() + " (" + p.guardianRelationship() + ")"));
        ((Label)guardianBox.getChildren().get(0)).setStyle("-fx-font-size: 11px; -fx-text-fill: #64748b;");
        ((Label)guardianBox.getChildren().get(1)).setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: #1e293b;");

        VBox pharmacyBox = new VBox(2, new Label("Pharmacy:"), new Label(p.preferredPharmacy()));
        ((Label)pharmacyBox.getChildren().get(0)).setStyle("-fx-font-size: 11px; -fx-text-fill: #64748b;");
        ((Label)pharmacyBox.getChildren().get(1)).setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: #1e293b;");

        VBox lastVisitBox = new VBox(2, new Label("Last Visit:"), new Label(lastVisitDate != null ? lastVisitDate : "05/08/26"));
        ((Label)lastVisitBox.getChildren().get(0)).setStyle("-fx-font-size: 11px; -fx-text-fill: #64748b;");
        ((Label)lastVisitBox.getChildren().get(1)).setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: #1e293b;");

        details.getChildren().addAll(allergyBox, guardianBox, pharmacyBox, lastVisitBox);
        banner.getChildren().addAll(topRow, details);

        return banner;
    }
}

