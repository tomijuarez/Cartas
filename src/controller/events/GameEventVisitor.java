package controller.events;

/**
 * Created by Gandalf on 21/2/2016.
 */
public interface GameEventVisitor {
    public void visit(ConfrontationEvent event);
    public void visit(CardsSelection event);
    public void visit(TieBreakCardsSelection event);
    public void visit(ShiftTurn event);
    public void visit(DeadHeatRound event);
    public void visit(WinRound event);
}
