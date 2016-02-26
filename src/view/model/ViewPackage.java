package view.model;

import javafx.scene.layout.Pane;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/**
 * Created by Gandalf on 13/2/2016.
 */
public class ViewPackage {
    protected Pane container;

    private final String protocol       = "file:";
    private final String imagePath      = "resources/images/";
    private final String extension      = ".png";

    public Pane getResult() {
        return this.container;
    }

    public String getImagePath(String imageName) {
        return this.protocol + this.imagePath + imageName + this.extension;
    }

    public String getImageAbsolutePath(String imagePath) {
        return this.protocol + imagePath;
    }

    public void copyImage(File imagePath, String imageName) {
        Path destiny = Paths.get(this.imagePath + imageName + this.extension);
        Path from = Paths.get(imagePath.getAbsolutePath());
        try {
            Files.copy(from, destiny, StandardCopyOption.REPLACE_EXISTING);
        }
        catch (IOException e) {
                System.out.println(e.getMessage());
        }
    }
}
