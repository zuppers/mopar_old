package net.scapeemulator.game.dialogue;

import net.scapeemulator.game.model.definition.NPCDefinitions;
import net.scapeemulator.game.model.player.interfaces.InterfaceSet.Component;
import net.scapeemulator.game.model.player.Player;
import net.scapeemulator.game.msg.impl.inter.InterfaceAnimationMessage;
import net.scapeemulator.game.msg.impl.inter.InterfaceNPCHeadMessage;
import net.scapeemulator.game.msg.impl.inter.InterfacePlayerHeadMessage;

import org.codehaus.plexus.util.StringUtils;

/**
 * @author Hadyn Richard
 */
public final class DialogueContext {

    private static final int TEXT_OFFSET = 210;
    private static final int TEXT_OFFSET_NO_INPUT = 214;
    private static final int OPTION_OFFSET = 228;
    private static final int RIGHT_CONVERSATION_OFFSET = 64;
    private static final int RIGHT_CONVERSATION_OFFSET_NO_INPUT = 68;
    private static final int LEFT_CONVERSATION_OFFSET = 241;
    private static final int LEFT_CONVERSATION_OFFSET_NO_INPUT = 245;

    private static final int LESS_OPTIONS = 1;
    private static final int MORE_OPTIONS = 5;

    private final Player player;
    private final Dialogue dialogue;
    private DialogueType displayedType;
    private final DialogueContextListener listener;
    private String[] options;
    private boolean inputDisplayed;
    private boolean overflowDisplayed;
    private int stage;
    private int interfaceId = -2;
    private boolean isStopped;

    private enum DialogueType {

        /* The enumeration for each conversation dialogue */
        PLAYER_CONVERSATION, NPC_CONVERSATION,

        /* The enumeration for each option dialogue */
        TWO_OPTION, THREE_OPTION, FOUR_OPTION, FIVE_OPTION, SIX_OPTION, SEVEN_OPTION, EIGHT_OPTION, NINE_OPTION,

        /* The enumeration for the text dialogue */
        TEXT
    }

    public DialogueContext(Player player, Dialogue dialogue) {
        this.player = player;
        this.dialogue = dialogue;
        listener = new DialogueContextListener(this);
        init();
    }

    private void init() {
        dialogue.initialize(this);
    }

    public void handleInput(int id, int componentId) {

        /* Dont handle input if there is no input displayed */
        if (!inputDisplayed) {
            return;
        }

        switch (displayedType) {
        case PLAYER_CONVERSATION:
        case NPC_CONVERSATION:
        case TEXT:
            dialogue.handleOption(this, DialogueOption.CONTINUE);
            break;

        case TWO_OPTION:
        case THREE_OPTION:
        case FOUR_OPTION:
        case FIVE_OPTION:
        case SIX_OPTION:
        case SEVEN_OPTION:
        case EIGHT_OPTION:
        case NINE_OPTION:
            int offset = componentId - 1;
            int count = getAmountOptions(displayedType);
            if (count > 5) {

                /* Swap from overflow to option chatbox */
                if (offset == LESS_OPTIONS && overflowDisplayed) {
                    interfaceId = getOptionInterfaceId(count);
                    player.setInterfaceText(interfaceId, 6, "More Options...");
                    for (int i = 0; i < 4; i++) {
                        player.setInterfaceText(interfaceId, i + 2, options[i]);
                    }
                    player.getInterfaceSet().openChatbox(interfaceId);
                    overflowDisplayed = false;
                    return;
                }

                /* Swap from overflow to option chatbox */
                if (offset == MORE_OPTIONS && !overflowDisplayed) {
                    interfaceId = getOverflowInterfaceId(count);
                    player.setInterfaceText(interfaceId, 2, "Less Options...");
                    for (int i = 4; i < count; i++) {
                        player.setInterfaceText(interfaceId, i - 1, options[i]);
                    }
                    player.getInterfaceSet().getChatbox().removeListener();
                    player.getInterfaceSet().openChatbox(interfaceId);
                    player.getInterfaceSet().getChatbox().setListener(new DialogueContextListener(this));
                    overflowDisplayed = true;
                    return;
                }
            }

            if (overflowDisplayed) {
                offset += 4;
            }

            dialogue.handleOption(this, DialogueOption.forId(offset));
            break;
        }
    }

    public void openPlayerConversationDialogue(String text, HeadAnimation animation, boolean displayInput) {
        String[] chunks = split(text);
        if (chunks.length >= 5) {
            throw new IllegalArgumentException();
        }
        interfaceId = (displayInput ? RIGHT_CONVERSATION_OFFSET : RIGHT_CONVERSATION_OFFSET_NO_INPUT) + chunks.length - 1;
        for (int i = 0; i < chunks.length; i++) {
            player.setInterfaceText(interfaceId, i + 4, chunks[i]);
        }
        player.setInterfaceText(interfaceId, 3, StringUtils.capitalise(player.getDisplayName()));
        player.send(new InterfaceAnimationMessage(interfaceId, 2, animation.getAnimationId()));
        player.send(new InterfacePlayerHeadMessage(interfaceId, 2));
        player.getInterfaceSet().openChatbox(interfaceId);
        player.getInterfaceSet().getChatbox().setListener(listener);
        displayedType = DialogueType.PLAYER_CONVERSATION;
        inputDisplayed = displayInput;
    }

    public void openNpcConversationDialogue(String text, int npcType, HeadAnimation animation, boolean displayInput) {
        String[] chunks = split(text);
        if (chunks.length >= 5) {
            throw new IllegalArgumentException();
        }
        interfaceId = (displayInput ? LEFT_CONVERSATION_OFFSET : LEFT_CONVERSATION_OFFSET_NO_INPUT) + chunks.length - 1;
        for (int i = 0; i < chunks.length; i++) {
            player.setInterfaceText(interfaceId, i + 4, chunks[i]);
        }

        player.setInterfaceText(interfaceId, 3, StringUtils.capitalise(NPCDefinitions.forId(npcType).getName()));
        player.send(new InterfaceAnimationMessage(interfaceId, 2, animation.getAnimationId()));
        player.send(new InterfaceNPCHeadMessage(interfaceId, 2, npcType));
        player.getInterfaceSet().openChatbox(interfaceId);
        player.getInterfaceSet().getChatbox().setListener(listener);
        displayedType = DialogueType.PLAYER_CONVERSATION;
        inputDisplayed = displayInput;
    }

    public void openTextDialogue(String text, boolean displayInput) {
        String[] chunks = split(text);
        if (chunks.length >= 5) {
            throw new IllegalArgumentException();
        }
        interfaceId = (displayInput ? TEXT_OFFSET : TEXT_OFFSET_NO_INPUT) + chunks.length - 1;
        for (int i = 1; i <= chunks.length; i++) {
            player.setInterfaceText(interfaceId, i, chunks[i - 1]);
        }
        player.getInterfaceSet().openChatbox(interfaceId);
        player.getInterfaceSet().getChatbox().setListener(listener);
        displayedType = DialogueType.TEXT;
        inputDisplayed = displayInput;
    }

    public void openOptionDialogue(String... opts) {
        if (opts.length >= 10 || opts.length <= 1) {
            throw new IllegalArgumentException();
        }

        int amountOptions = opts.length;
        interfaceId = getOptionInterfaceId(amountOptions);

        /* If overflow is present do not send the fifth option yet */
        for (int i = 0; i < (amountOptions > 5 ? 4 : amountOptions); i++) {
            player.setInterfaceText(interfaceId, i + 2, opts[i]);
        }

        /* Send the overflow junk */
        if (amountOptions > 5) {
            player.setInterfaceText(interfaceId, 6, "More Options...");
        }

        player.getInterfaceSet().openChatbox(interfaceId);
        player.getInterfaceSet().getChatbox().setListener(listener);
        displayedType = getOptionDialogue(amountOptions);
        inputDisplayed = true;
        overflowDisplayed = false;
        options = opts;
    }

    private static int getOptionInterfaceId(int count) {
        /* Limit the count if nessecary */
        if (count > 5) {
            count = 5;
        }
        return OPTION_OFFSET + 2 * (count - 2);
    }

    private static int getOverflowInterfaceId(int count) {
        return getOptionInterfaceId(count - 4);
    }

    private static int getAmountOptions(DialogueType type) {
        switch (type) {
        case TWO_OPTION:
            return 2;
        case THREE_OPTION:
            return 3;
        case FOUR_OPTION:
            return 4;
        case FIVE_OPTION:
            return 5;
        case SIX_OPTION:
            return 6;
        case SEVEN_OPTION:
            return 7;
        case EIGHT_OPTION:
            return 8;
        case NINE_OPTION:
            return 9;

        default:
            break;
        }
        throw new IllegalArgumentException();
    }

    private static DialogueType getOptionDialogue(int amount) {
        switch (amount) {
        case 2:
            return DialogueType.TWO_OPTION;
        case 3:
            return DialogueType.THREE_OPTION;
        case 4:
            return DialogueType.FOUR_OPTION;
        case 5:
            return DialogueType.FIVE_OPTION;
        case 6:
            return DialogueType.SIX_OPTION;
        case 7:
            return DialogueType.SEVEN_OPTION;
        case 8:
            return DialogueType.EIGHT_OPTION;
        case 9:
            return DialogueType.NINE_OPTION;
        }
        throw new IllegalArgumentException();
    }

    public void stop() {
        if (!isStopped) {
            Component component = player.getInterfaceSet().getChatbox();
            component.removeListener();
            component.reset();
            isStopped = true;
        }
    }

    public boolean isStopped() {
        return isStopped;
    }

    private static String[] split(String str) {
        StringBuilder builder = new StringBuilder();
        int counter = 0;
        for (char c : str.toCharArray()) {
            if (counter >= 40 && c == ' ') {
                builder.append("\n");
                counter = 0;
                continue;
            }
            builder.append(c);
            counter++;
        }
        return builder.toString().split("\n");
    }

    public int getStage() {
        return stage;
    }
    
    public void setStage(int stage) {
        this.stage = stage;
    }
    
    public int getInterfaceId() {
        return interfaceId;
    }
    
    public Player getPlayer() {
        return player;
    }

}