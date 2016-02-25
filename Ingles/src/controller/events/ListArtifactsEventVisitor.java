package controller.events;

/**
 * Created by Gandalf on 21/2/2016.
 */
public interface ListArtifactsEventVisitor {
    public void visit(ListDecks event);
    public void visit(ListCards event);
}
