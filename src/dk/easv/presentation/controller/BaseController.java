package dk.easv.presentation.controller;

import dk.easv.presentation.model.AppModel;

public class BaseController {
    private AppModel model;

    public void setModel(AppModel model) {
        this.model = model;
    }

    public AppModel getModel() {
        return this.model;
    }
}
