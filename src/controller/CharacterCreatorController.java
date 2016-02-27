package controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.TilePane;
import javafx.stage.FileChooser;
import view.model.CardView;
import view.model.ViewPackage;

import javax.xml.bind.annotation.XmlAnyAttribute;
import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by Gandalf on 25/2/2016.
 */
public class CharacterCreatorController extends MediableController implements Initializable {

    ViewPackage imageManager = new ViewPackage();

    CharacterCreatorMediator mediator;

    @FXML
    private TextField inputName;
    @FXML
    private Button selectImageButton;
    @FXML
    private Button createAttributeButton;
    @FXML
    private ComboBox addAttributeButton;
    @FXML
    private TilePane attributesContainer;
    @FXML
    private Button continueButton;
    @FXML
    private ImageView imageContainer;

    private String characterName;
    private File selectedImage;

    private List<String> attributes;

    public CharacterCreatorController(List<String> attributes) {
        this.attributes = attributes;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        selectImageButton.setOnAction((event)->{
            FileChooser imageSelector = new FileChooser();
            imageSelector.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG images","*.png"));
            imageSelector.setTitle("Elegir foto del personaje.");
            this.selectedImage = imageSelector.showOpenDialog(this.context.getStage());
            this.showImage(this.selectedImage);
        });

        continueButton.setOnAction((event)->{
            if (selectedImage != null && this.inputName.getText() != null) {
                this.imageManager.copyImage(this.selectedImage, this.inputName.getText());
            }
        });
    }

    private void showImage(File selectedFile) {
        if(selectedFile != null) {
            Image image = new Image(this.imageManager.getImageAbsolutePath(selectedFile.getPath()));
            this.imageContainer.setImage(image);
        }
    }

    @Override
    public void setMediator(Mediator mediator) {
        this.mediator = (CharacterCreatorMediator) mediator;
    }

}
