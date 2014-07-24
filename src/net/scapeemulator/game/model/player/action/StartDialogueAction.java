package net.scapeemulator.game.model.player.action;

import net.scapeemulator.game.dialogue.Dialogue;
import net.scapeemulator.game.dialogue.DialogueContext;
import net.scapeemulator.game.model.mob.action.MobInteractionAction;
import net.scapeemulator.game.model.npc.NPC;
import net.scapeemulator.game.model.player.Player;

/**
 * Written by Hadyn Richard
 */
public final class StartDialogueAction extends MobInteractionAction<Player, NPC> {
    
    private final Dialogue dialogue;
    private DialogueContext context;
    private boolean dialogueDisplayed;
    
    public StartDialogueAction(Player player, NPC target, Dialogue dialogue) {
        super(player, target, 1);
        this.dialogue = dialogue;
        this.target = target;
    }

    @Override
    public void executeAction() {
        if(!dialogueDisplayed) {
            context = dialogue.displayTo(mob);
            mob.turnToTarget(target);
            target.turnToTarget(mob);
            dialogueDisplayed = true;
            return;
        }
        
        if(context.isStopped()) {
            mob.resetTurnToTarget();
            if(target.getTurnToTarget().equals(mob)) {
                target.resetTurnToTarget();
            }
            stop();
        }
    }
}
