import javafx.application.Application;
import javafx.stage.Stage;
import controller.*;
import view.*;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        GameWindow root = new GameWindow("layouts/firstView.fxml", new FirstController(), primaryStage);
        root.setTitle("MAIN");
        root.printContext();
        root.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}