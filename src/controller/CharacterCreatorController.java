package controller;

import controller.utils.AlertUtils;
import controller.utils.TextFieldUtils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import javafx.stage.FileChooser;
import model.Game;
import view.model.CardView;
import view.model.ViewPackage;

import javax.xml.bind.annotation.XmlAnyAttribute;
import javax.xml.soap.Text;
import java.io.File;
import java.net.URL;
import java.util.*;

/**
 * Created by Gandalf on 25/2/2016.
 */
public class CharacterCreatorController extends MediableController implements Initializable {

    ViewPackage imageManager = new ViewPackage();

    TextFieldUtils textFieldUtils = new TextFieldUtils();
    AlertUtils alertUtils = new AlertUtils();

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
    @FXML
    private TextField inputRealName;

    private String characterName;
    private File selectedImage;

    Map<TextField, ToggleButton> attributesToCreate = new HashMap<>();

    private List<String> attributes;
    private Map<String, Boolean> createdAttributes = new HashMap<>();
    private List<String> selectedAttributes = new Vector<>();
    private Game game;

    public CharacterCreatorController(Game game, List<String> attributes) {
        this.game = game;
        this.attributes = attributes;
    }

    private void setToggleButtonOnAction(ToggleButton button) {
        button.setOnAction((event)-> {
            button.setText("comparación por <");
        });
    }

    private boolean attributeExists(String attrName) {
        HBox row;
        for(int i = 0; i < this.attributesContainer.getChildren().size(); i++) {
            row = (HBox) this.attributesContainer.getChildren().get(i);
            TextField attribute = (TextField) row.getChildren().get(0);

            if (attribute.getText().toLowerCase().equals(attrName.toLowerCase()))
                return true;
        }

        return false;
    }

    private Map<String, String> getSelectedAttributes() {
        Map<String, String> attributes = new HashMap<>();
        HBox row;
        for(int i = 0; i < this.attributesContainer.getChildren().size(); i++) {
            row = (HBox) this.attributesContainer.getChildren().get(i);
            TextField attribute = (TextField) row.getChildren().get(0);
            TextField attributeValue = (TextField) row.getChildren().get(1);
            attributes.put(attribute.getText(), attributeValue.getText());
        }
        return attributes;
    }

    private void setAttributesSelectorEvents() {
        this.addAttributeButton.getSelectionModel().selectedItemProperty().addListener((o, oldValue, newValue) -> {
            if (!this.attributeExists((String) newValue))
                this.addAttributeCreatorItem((String) newValue);
        });
    }

    private void addAttributeCreatorItem() {
        TextField attributeName = new TextField();
        TextField attributeValue = new TextField("0.00");

        textFieldUtils.setTextFieldEvent(attributeName);
        textFieldUtils.setTextFieldEvent(attributeValue);

        HBox horizontalElements = new HBox();
        horizontalElements.setPrefSize(330,40);
        horizontalElements.getChildren().addAll(attributeName,attributeValue);
        horizontalElements.setSpacing(10);
        this.attributesContainer.getChildren().add(horizontalElements);
    }

    private void addAttributeCreatorItem(String attrName) {
        TextField attributeName = new TextField(attrName);
        attributeName.setDisable(true);
        TextField attributeValue = new TextField("0.00");

        textFieldUtils.setTextFieldEvent(attributeValue);

        HBox horizontalElements = new HBox();
        horizontalElements.setPrefSize(330,40);
        horizontalElements.getChildren().addAll(attributeName,attributeValue);
        horizontalElements.setSpacing(10);
        this.attributesContainer.getChildren().add(horizontalElements);
    }

    private boolean verifyInputs() {
        HBox row;
        boolean passed = true;
        boolean attributeFault = false;

        if(this.textFieldUtils.stringIsEmpty(this.inputName))
            passed = false;

        if(this.textFieldUtils.stringIsEmpty(this.inputRealName))
            passed = false;

        for(int i = 0; i < this.attributesContainer.getChildren().size(); i++) {
            row = (HBox)this.attributesContainer.getChildren().get(i);
            TextField attribute = (TextField)row.getChildren().get(0);
            TextField attributeValue = (TextField)row.getChildren().get(1);

            if(textFieldUtils.stringIsEmpty(attribute) || !textFieldUtils.hasDoubleValue(attributeValue) ) {
                passed = false;
                attributeFault = true;
            }
        }

        if(!passed)
            if(attributeFault)
                alertUtils.throwUIError("Revisar los atributos ingresados. Los valores deben ser numéricos y no pueden existir campos vacíos.");
            else
                alertUtils.throwUIError("El nombre real y el nombre ficticio del personaje deben ser ingresados.");

        return passed;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        this.addAttributeButton.setItems(FXCollections.observableArrayList(this.attributes));
        this.setAttributesSelectorEvents();
        this.textFieldUtils.setTextFieldEvent(this.inputName);
        this.textFieldUtils.setTextFieldEvent(this.inputRealName);

        createAttributeButton.setOnAction((event)->{
            this.addAttributeCreatorItem();
        });

        selectImageButton.setOnAction((event)->{
            FileChooser imageSelector = new FileChooser();
            imageSelector.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG images","*.png"));
            imageSelector.setTitle("Elegir foto del personaje.");
            this.selectedImage = imageSelector.showOpenDialog(this.context.getStage());
            this.showImage(this.selectedImage);
        });

        continueButton.setOnAction((event)-> {
                Map<String, String> selectedAttributes = this.getSelectedAttributes();
                if (this.verifyInputs()) {
                    if (selectedAttributes.size() >= 5) {
                        if (selectedImage != null) {
                            this.imageManager.copyImage(this.selectedImage, this.inputName.getText());
                            this.mediator.createCharacter(this.inputName.getText(), this.inputRealName.getText(), selectedAttributes);

                        }
                        else
                            alertUtils.throwUIError("Debe seleccionarse una imagen.");
                    }
                    else {
                        alertUtils.throwUIError("Deben elegirse 5 atributos como mínimo.");
                    }
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
