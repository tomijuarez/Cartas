package view;

import controller.MediableController;
import controller.Mediator;
import javafx.scene.media.Media;
import javafx.stage.Stage;

import javax.sound.midi.SysexMessage;
import java.util.UUID;

/**
 * Created by Gandalf on 4/2/2016.
 */
public class GameWindow {

    String id;
    Context root;
    String title;

    public GameWindow(String view, MediableController controller, Stage stage){
        System.out.println("cree una nueva ventana de juego");
        this.root = new Context();
        this.root.setStage(stage).setController(controller);
        this.root.load(view);
        controller.setContext(this);
    }

    public void show() {
        this.root.show();
    }

    public void close() {
        this.root.getStage().close();
        System.out.println("pase por el gamewindow close");
    }

    public Stage getStage() {
        return this.root.getStage();
    }

    public Context getContext() {
        return this.root;
    }

    public void setChild(GameWindow subWindow, Mediator mediator) {
        this.root.setSubWindow(subWindow.getContext(),mediator);
        subWindow.show();
    }

    public String getId() {
        return this.id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void printContext() {
        System.out.println(this.id + " --> " + this.title);
    }
}
