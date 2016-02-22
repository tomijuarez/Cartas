import javafx.application.Application;
import javafx.stage.Stage;
import controller.*;
import model.Juego;
import view.*;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Juego game = new Juego();
        GameWindow root = new GameWindow("layouts/firstView.fxml", new FirstController(game), primaryStage);
        root.setTitle("MAIN");
        root.printContext();
        root.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}