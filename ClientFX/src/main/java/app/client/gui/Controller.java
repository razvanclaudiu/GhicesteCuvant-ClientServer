package app.client.gui;

import app.server.implementation.DefaultAppServer;
import app.services.AppServices;
import javafx.stage.Stage;

public class Controller {

    protected AppServices services;
    protected Stage stage;

    public void setServer(AppServices services, Stage stage) {
        this.services = services;
        this.stage = stage;
    }
}
