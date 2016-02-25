package controller.events;

/**
 * Created by Gandalf on 21/2/2016.
 */
public interface GameEventAcceptor {
    void accept(GameEventVisitor visitor);
}
