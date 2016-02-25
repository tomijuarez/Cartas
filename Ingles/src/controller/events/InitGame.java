package controller.events;

/**
 * Created by Gandalf on 21/2/2016.
 */
public class InitGame implements GameEventAcceptor {

    @Override
    public void accept(GameEventVisitor visitor) {
        visitor.visit(this);
    }
}
