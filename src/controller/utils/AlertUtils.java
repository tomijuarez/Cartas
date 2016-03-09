package controller.utils;

import javafx.scene.control.Alert;

/**
 * Created by Gandalf on 1/3/2016.
 */
public class AlertUtils {

    private static String errorHeader = "Lo sentimos, ocurrió un error.";
    private static String infoHeader = "Información";
    private static String warningHeader = "Cuidado!";

    public void showAlert(Alert.AlertType context, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(errorHeader);
        alert.setContentText(content);
        alert.show();
    }

    public void throwUIError(String errorMsg) {
        this.showAlert(Alert.AlertType.ERROR,this.errorHeader, errorMsg);
    }

    public void throwUINotice(String infoMsg) {
        this.showAlert(Alert.AlertType.INFORMATION,this.infoHeader, infoMsg);
    }

    public void throwUIWarning(String warnMsg) {
        this.showAlert(Alert.AlertType.WARNING, this.warningHeader, warnMsg);
    }
}
