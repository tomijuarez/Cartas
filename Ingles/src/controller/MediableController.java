package controller;


import view.Context;
import view.GameWindow;

/**
 * Created by Tomás on 30/12/2015.
 */
public abstract class MediableController {
    protected GameWindow context;

    public abstract void setMediator(Mediator mediator);

    public void setContext(GameWindow context) {
        this.context = context;
    }

    public GameWindow getContext() {
        return this.context;
    }
}