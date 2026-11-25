package pediatriccare;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

public class LogoUtil {

    public static Node createLogo(double size) {
        // First try to load the extracted logo image
        try {
            InputStream stream = LogoUtil.class.getResourceAsStream("/images/logo.png");
            if (stream == null) {
                File[] candidates = new File[] {
                    new File("src/main/resources/images/logo.png"),
                    new File("PediatricCarePrototype/src/main/resources/images/logo.png"),
                    new File("target/classes/images/logo.png"),
                    new File("PediatricCarePrototype/target/classes/images/logo.png")
                };
                for (File candidate : candidates) {
                    if (candidate.exists()) {
                        stream = new FileInputStream(candidate);
                        break;
                    }
                }
            }

            if (stream != null) {
                Image img = new Image(stream);
                if (!img.isError() && img.getWidth() > 0) {
                    ImageView view = new ImageView(img);
                    view.setFitWidth(size);
                    view.setFitHeight(size);
                    view.setPreserveRatio(true);
                    view.setSmooth(true);
                    // Add subtle soft drop shadow matching modern UI
                    DropShadow shadow = new DropShadow();
                    shadow.setColor(Color.rgb(0, 0, 0, 0.15));
                    shadow.setRadius(size * 0.1);
                    shadow.setOffsetY(size * 0.04);
                    view.setEffect(shadow);
                    return view;
                }
            }
        } catch (Throwable ignored) {
            // Fall through to crisp vector rendering
        }

        // Programmatic vector fallback - pixel-perfect rendering of the PediatricCare gradient cross badge
        return createVectorLogo(size);
    }

    public static StackPane createVectorLogo(double size) {
        StackPane pane = new StackPane();
        pane.setPrefSize(size, size);
        pane.setMaxSize(size, size);
        pane.setMinSize(size, size);
        pane.setAlignment(Pos.CENTER);

        double radius = size / 2.0;

        // Circular badge with teal-to-blue-green gradient
        Stop[] stops = new Stop[] {
            new Stop(0.0, Color.web("#42b883")),
            new Stop(0.5, Color.web("#2b988f")),
            new Stop(1.0, Color.web("#21618c"))
        };
        LinearGradient gradient = new LinearGradient(0.2, 0.0, 0.8, 1.0, true, CycleMethod.NO_CYCLE, stops);

        Circle circle = new Circle(radius, gradient);
        circle.setCenterX(radius);
        circle.setCenterY(radius);

        DropShadow shadow = new DropShadow();
        shadow.setColor(Color.rgb(18, 50, 75, 0.35));
        shadow.setRadius(size * 0.12);
        shadow.setOffsetY(size * 0.05);
        circle.setEffect(shadow);

        // Medical white plus cross
        double barLength = size * 0.52;
        double barThickness = size * 0.19;
        double cornerRadius = barThickness * 0.18;

        Rectangle vBar = new Rectangle(barThickness, barLength, Color.WHITE);
        vBar.setArcWidth(cornerRadius);
        vBar.setArcHeight(cornerRadius);

        Rectangle hBar = new Rectangle(barLength, barThickness, Color.WHITE);
        hBar.setArcWidth(cornerRadius);
        hBar.setArcHeight(cornerRadius);

        pane.getChildren().addAll(circle, vBar, hBar);
        return pane;
    }
}

