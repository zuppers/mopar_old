package net.scapeemulator.game.model.player.skills.runecrafting;

import net.scapeemulator.game.model.object.GroundObjectList.GroundObject;
import net.scapeemulator.game.model.player.Equipment;
import net.scapeemulator.game.model.player.Item;
import net.scapeemulator.game.model.player.Player;
import net.scapeemulator.game.model.player.action.ReachDistancedAction;

/**
 * @author David Insley
 */
public class RuinsTeleportAction extends ReachDistancedAction {

    public enum Type {
        TALISMAN,
        TIARA,
        PORTAL
    }

    private final RCAltar altar;
    private final Type type;

    public RuinsTeleportAction(Player player, RCAltar altar, GroundObject object, Type type) {
        super(1, false, player, object.getBounds(), 1);
        this.altar = altar;
        this.type = type;
    }

    @Override
    public void executeAction() {
        if (!mob.notWalking()) {
            return;
        }
        if (type == Type.TALISMAN) {
            if (!mob.getInventory().contains(altar.getTalismanId())) {
                stop();
                return;
            }
        } else if (type == Type.TIARA) {
            Item head = mob.getEquipment().get(Equipment.HEAD);
            if (head == null || head.getId() != altar.getTiaraId()) {
                stop();
                return;
            }
        }
        mob.sendMessage(type != Type.PORTAL ? "You feel a powerful force take hold of you..." : "You step through the portal...");
        mob.teleport(type != Type.PORTAL ? altar.getAltarPos() : altar.getRuinsPos());
        stop();
    }

}
