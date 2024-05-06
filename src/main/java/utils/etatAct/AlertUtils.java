package utils.etatAct;

import org.controlsfx.control.Notifications;

public class AlertUtils {

    public static void makeInformation(String message) {
        Notifications.create()
                .title("Notification")
                .text(message)
                .showInformation();
    }

    public static void makeError(String message) {
        Notifications.create()
                .title("Notification")
                .text(message)
                .showError();
    }

    public static void makeSuccessNotification(String message) {
        Notifications.create()
                .title("Notification")
                .text(message)
                .showInformation();
    }

}
