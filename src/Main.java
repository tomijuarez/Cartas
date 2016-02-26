import javafx.application.Application;
import javafx.stage.Stage;
import controller.*;
import model.Game;
import view.*;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Game game = new Game();
        GameWindow root = new GameWindow("layouts/firstView.fxml", new FirstController(game), primaryStage);
        root.setTitle("MAIN");
        root.printContext();
        root.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}