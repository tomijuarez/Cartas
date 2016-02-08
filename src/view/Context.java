package view;

import controller.MediableController;
import controller.Mediator;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Created by Tomás on 30/12/2015.
 */
public class Context {
    private MediableController currentController;
    private Stage currentStage;
    private Parent root;

    public Context setStage(Stage currentStage) {
        this.currentStage = currentStage;
        return this;
    }

    public Context setController(MediableController controller) {
        this.currentController = controller;
        return this;
    }

    public void load(String url) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(url));
            loader.setController(this.currentController);
            this.root = loader.load();
        }
        catch(Exception e) {
            System.out.println("No se ha podido cargar la vista.");
            System.out.println(e.getMessage());
        }
    }

    public void show() {
        System.out.println(this.root);
        this.currentStage.setScene(new Scene(this.root));
        this.currentStage.show();
    }

    public MediableController getController() {
        return this.currentController;
    }

    public void setSubWindow(Context subContext, Mediator mediador) {
        MediableController subController = subContext.getController();

        mediador.setControllers(this.currentController, subController);

        /**
         * Agregar el mediador en ambos controladores.
         */

        this.currentController.setMediator(mediador);
        subController.setMediator(mediador);
    }
}
