package net.scapeemulator.game.model.player.skills;

import net.scapeemulator.game.model.player.interfaces.ComponentListener;
import net.scapeemulator.game.model.player.interfaces.InterfaceSet.Component;
import net.scapeemulator.game.model.player.Item;
import net.scapeemulator.game.model.player.Player;
import net.scapeemulator.game.model.player.ScriptInputListener;
import net.scapeemulator.game.msg.impl.inter.InterfaceItemMessage;

/**
 * @author David Insley
 */
public class MakeItemInterface {

    private static final int COOK_ITEM_INTERFACE = 307;
    private static final int MAKE_ITEM_INTERFACE = 309; // 1 5 X ALL
    private static final int CHOOSE_ITEM_INTERFACE_OFFSET = 301; // 1 5 10 X

    public static abstract class MakeItemInterfaceListener {

        /**
         * Alerts that the 'make all' option was selected for the recipe.
         */
        public abstract void makeAllSelected();

        /**
         * Alerts that one of the constant amount options or the make-x option was selected for the recipes. If the make-x option was selected, it has
         * already been queried and the amount given is what was input.
         */
        public abstract void makeAmountSelected(int amount);

        /**
         * Alerts that the make item interface was cancelled.
         */
        public abstract void cancelled();
    }

    public static abstract class ChooseItemInterfaceListener<T extends SkillRecipe> {

        /**
         * Alerts that one of the constant amount options or the make-x option was selected for one of the recipes. If the make-x option was selected,
         * it has already been queried and the amount given is what was input.
         * 
         * @param selected the recipe that was selected
         */
        public abstract void makeAmountSelected(T selected, int amount);

        /**
         * Alerts that the make item interface was cancelled.
         */
        public abstract void cancelled();
    }
    
    public static class ChooseItemInterfaceListenerAdapter<T extends SkillRecipe> extends ChooseItemInterfaceListener<T> {

        @Override
        public void makeAmountSelected(T selected, int amount) {
        }

        @Override
        public void cancelled() {
        }
    }

    public static void showMakeItemInterface(Player player, MakeItemInterfaceListener listener, Item item, boolean cook) {
        int interfaceId = cook ? COOK_ITEM_INTERFACE : MAKE_ITEM_INTERFACE;
        player.send(new InterfaceItemMessage(interfaceId, 2, 150, item.getId()));
        player.setInterfaceText(interfaceId, 6, "<br><br><br><br><br>" + item.getDefinition().getName());
        player.getInterfaceSet().openChatbox(interfaceId);
        player.getInterfaceSet().getChatbox().setListener(new SingleItemComponentListener(player, listener));
    }

    public static <T extends SkillRecipe> void showChooseItemInterface(Player player, ChooseItemInterfaceListener<T> listener, T[] options) {
        if (options.length == 1) {
            MakeItemInterfaceListener newListener = new MakeItemInterfaceListener() {
                @Override
                public void makeAllSelected() {
                    listener.makeAmountSelected(options[0], 1000);
                }

                @Override
                public void makeAmountSelected(int amount) {
                    listener.makeAmountSelected(options[0], amount);
                }

                @Override
                public void cancelled() {
                    listener.cancelled();
                }

            };
            showMakeItemInterface(player, newListener, options[0].getProduct(), false);
            return;
        }
        int id = CHOOSE_ITEM_INTERFACE_OFFSET + options.length;
        for (int i = 0; i < options.length; i++) {
            player.send(new InterfaceItemMessage(id, i + 2, 200, options[i].getProduct().getId()));
            int string_index = 7 + (4 * i) + options.length - 2;
            player.setInterfaceText(id, string_index, "<br><br><br><br><br>" + options[i].getProduct().getDefinition().getName());
        }
        player.getInterfaceSet().openChatbox(id, new ChooseItemComponentListener<T>(player, options, listener));
    }

    private static class ChooseItemComponentListener<T extends SkillRecipe> extends ComponentListener {

        private final Player player;
        private final T[] recipes;
        private final ChooseItemInterfaceListener<T> listener;

        private ChooseItemComponentListener(Player player, T[] recipes, ChooseItemInterfaceListener<T> listener) {
            this.player = player;
            this.recipes = recipes;
            this.listener = listener;
        }

        @Override
        public void inputPressed(Component component, int componentId, int dynamicId) {
            player.getInterfaceSet().getChatbox().removeListener();
            player.getInterfaceSet().getChatbox().reset();
            int recipeIndex = componentId / 4;
            if (recipeIndex < 0 || recipeIndex >= recipes.length) {
                listener.cancelled();
                return;
            }
            T selected = recipes[recipeIndex];
            int option = 3 - ((componentId - 3) % 4);
            switch (option) {
                case 0:
                    listener.makeAmountSelected(selected, 1);
                    break;
                case 1:
                    listener.makeAmountSelected(selected, 5);
                    break;
                case 2:
                    listener.makeAmountSelected(selected, 10);
                    break;
                case 3:
                    player.getScriptInput().showIntegerScriptInput("Enter amount:", new ScriptInputListener() {

                        @Override
                        public void intInputReceived(int value) {
                            listener.makeAmountSelected(selected, value);
                        }

                        @Override
                        public void usernameInputReceived(long value) {
                            listener.cancelled();
                        }
                    });
                    break;
            }
        }

        @Override
        public void componentClosed(Component component) {
            listener.cancelled();
        }

        @Override
        public boolean componentChanged(Component component, int oldId) {
            listener.cancelled();
            return false;
        }

    }

    private static class SingleItemComponentListener extends ComponentListener {

        private final Player player;
        private final MakeItemInterfaceListener listener;

        private SingleItemComponentListener(Player player, MakeItemInterfaceListener listener) {
            this.player = player;
            this.listener = listener;
        }

        @Override
        public void inputPressed(Component component, int componentId, int dynamicId) {
            player.getInterfaceSet().getChatbox().removeListener();
            player.getInterfaceSet().getChatbox().reset();
            int option = 3 - ((componentId - 3) % 4);
            switch (option) {
                case 0:
                    listener.makeAmountSelected(1);
                    break;
                case 1:
                    listener.makeAmountSelected(5);
                    break;
                case 2:
                    player.getScriptInput().showIntegerScriptInput("Enter amount:", new ScriptInputListener() {
                        @Override
                        public void intInputReceived(int value) {
                            listener.makeAmountSelected(value);
                        }

                        @Override
                        public void usernameInputReceived(long value) {
                            listener.cancelled();
                        }
                    });
                    break;
                case 3:
                    listener.makeAllSelected();
                    break;
            }
        }

        @Override
        public void componentClosed(Component component) {
            listener.cancelled();
        }

        @Override
        public boolean componentChanged(Component component, int oldId) {
            listener.cancelled();
            return false;
        }

    }

}
