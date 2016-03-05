package controller.utils;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;

/**
 * Created by Gandalf on 1/3/2016.
 */
public class TextFieldUtils {

    public void setTextFieldEvent(TextField field) {
        field.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                setNormalStateInput(field);
            }
        });
    }

    public void setNormalStateInput(TextField input) {
        input.getStyleClass().clear();
        input.getStyleClass().add("defaultStateTextInput");
    }

    public void setErrorStateInput(TextField input) {
        input.getStyleClass().clear();
        input.getStyleClass().add("errorStateTextInput");
    }

    public boolean hasDoubleValue(TextField input) {
        try {
            Double d = Double.parseDouble(input.getText());
            return true;
        }
        catch (Exception e) {
            this.setErrorStateInput(input);
            return false;
        }
    }

    public boolean stringIsEmpty(TextField input) {
        if (!input.getText().isEmpty())
            return false;
        else {
            System.out.println("ERRRRORRR");
            this.setErrorStateInput(input);
            return true;
        }
    }
}
