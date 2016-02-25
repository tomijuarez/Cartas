package view.model;

import javafx.scene.layout.Pane;

/**
 * Created by Gandalf on 13/2/2016.
 */
public abstract class ViewPackage {
    protected Pane container;

    private final String imagesFolder = "file:resources/images/";
    private final String extension    = ".png";

    public Pane getResult() {
        return this.container;
    }

    public String getImagePath(String imageName) {
        return this.imagesFolder + imageName + this.extension;
    }
}
