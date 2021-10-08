package sample;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws FileNotFoundException {
        Model model = new Model();
        Controller controller = new Controller(model);
        View view = new View(controller, model);
        Image icon = new Image(new FileInputStream(view.debugging+"images/icon.png"));
        primaryStage.getIcons().add(icon);
        Scene scene = new Scene(view.asParent(), 1920, 1080);
        view.startB.addEventHandler(MouseEvent.MOUSE_ENTERED,
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent e) {
                        scene.setCursor(Cursor.HAND);
                    }
                });
        view.startB.addEventHandler(MouseEvent.MOUSE_EXITED,
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent e) {
                        scene.setCursor(Cursor.DEFAULT);
                    }
                });
        view.hitB.addEventHandler(MouseEvent.MOUSE_ENTERED,
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent e) {
                        scene.setCursor(Cursor.HAND);
                    }
                });
        view.hitB.addEventHandler(MouseEvent.MOUSE_EXITED,
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent e) {
                        scene.setCursor(Cursor.DEFAULT);
                    }
                });
        view.standB.addEventHandler(MouseEvent.MOUSE_ENTERED,
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent e) {
                        scene.setCursor(Cursor.HAND);
                    }
                });
        view.standB.addEventHandler(MouseEvent.MOUSE_EXITED,
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent e) {
                        scene.setCursor(Cursor.DEFAULT);
                    }
                });
        view.formulaB.addEventHandler(MouseEvent.MOUSE_ENTERED,
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent e) {
                        scene.setCursor(Cursor.HAND);
                    }
                });
        view.formulaB.addEventHandler(MouseEvent.MOUSE_EXITED,
                new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent e) {
                        scene.setCursor(Cursor.DEFAULT);
                    }
                });
        primaryStage.setTitle("BlackjackAI");
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}