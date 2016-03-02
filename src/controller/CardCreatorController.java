package controller;

import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import model.AbstractCharacter;

import java.net.URL;
import java.util.List;
import java.util.Observable;
import java.util.ResourceBundle;
import java.util.Vector;

/**
 * Created by Gandalf on 29/2/2016.
 */
public class CardCreatorController extends MediableController implements Initializable {

    private CardCreatorMediator mediator;

    @FXML
    private ComboBox selectCharacterButton;
    @FXML
    private ListView attributesListView;
    @FXML
    private ListView selectedAttributes;
    @FXML
    private Button continueButton;

    private static final int MAX_ATTRIBUTES_SELECTION = 5;

    private List<AbstractCharacter> characters;
    private List<String> charactersName = new Vector<>();
    private AbstractCharacter selectedCharacter;

    public CardCreatorController(List<AbstractCharacter> characters) {
        this.characters = characters;
        for(int i = 0; i < characters.size(); i++)
            this.charactersName.add(characters.get(i).getFictitiousName());
    }

    public AbstractCharacter getCharacter(String characterNick) {
        for(AbstractCharacter character: this.characters) {
            if (character.getFictitiousName() == characterNick)
                return character;
        }
        return null;
    }

    private void setToggleEvents(ToggleButton toggleButton, String attributeName) {
        toggleButton.setOnAction((event)->{
            if(toggleButton.isSelected()) {
                if (this.selectedAttributes.getItems().size() < this.MAX_ATTRIBUTES_SELECTION) {
                    toggleButton.setText("eliminar");
                    this.selectedAttributes.getItems().add(attributeName);
                }
                else
                    toggleButton.setSelected(false);
            }
            else {
                toggleButton.setText("seleccionar");
                this.selectedAttributes.getItems().remove(attributeName);
            }
        });
    }

    public void showAttributes(AbstractCharacter character) {
        this.attributesListView.getItems().clear();
        this.selectedAttributes.getItems().clear();

        List<HBox> rows = new Vector<>();
        for (String attribute: character.getAttributes()) {
            HBox row = new HBox();
            row.setSpacing(10);
            Text attributeName = new Text(attribute);
            ToggleButton addButton = new ToggleButton("seleccionar");
            this.setToggleEvents(addButton, attribute);
            row.getChildren().addAll(attributeName,addButton);
            rows.add(row);
        }

        this.attributesListView.setItems(FXCollections.observableArrayList(rows));
    }

    public void setComboBoxEvent() {
        this.selectCharacterButton.getSelectionModel().selectedItemProperty().addListener((o, oldValue, newValue) -> {
            this.selectedCharacter = this.getCharacter((String)o.getValue());
            this.showAttributes(this.selectedCharacter);
        });
    }

    @Override
    public void setMediator(Mediator mediator) {
        this.mediator = (CardCreatorMediator)mediator;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.selectCharacterButton.setItems(FXCollections.observableArrayList(this.charactersName));
        this.setComboBoxEvent();

        this.continueButton.setOnAction((event)-> {
            if ( this.selectedAttributes.getItems().size() == 5 ) {
                this.mediator.createCard(this.selectedCharacter, new Vector<>((List<String>) this.selectedAttributes.getItems()));
                this.context.close();
            }
        });
    }
}
