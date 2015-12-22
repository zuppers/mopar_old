package net.scapeemulator.game.model.player;

import java.util.EnumMap;
import java.util.Map;

public class PlayerVariables {

    public enum Variable {

        /* @formatter:off */
        
        FIRST_LOGIN(1),
        SLAYER_TASK,
        SLAYER_INFO,
        HOME_LOCATION,
        
        // Construction
        CON_FURN_REMOVE(false),
        HERALDRY_CREST;
        
        /* @formatter:on */

        private final boolean persists;
        private final int defaultValue;

        private Variable() {
            this(0, true);
        }

        private Variable(int defaultValue) {
            this(defaultValue, true);
        }

        private Variable(boolean persists) {
            this(0, persists);
        }

        private Variable(int defaultValue, boolean persists) {
            this.defaultValue = defaultValue;
            this.persists = persists;
        }

        public boolean shouldPersist() {
            return persists;
        }

    }

    private final Map<Variable, Integer> vars;

    PlayerVariables() {
        vars = new EnumMap<Variable, Integer>(Variable.class);
    }

    public boolean hasVar(Variable var) {
        return vars.containsKey(var);
    }

    public int getVar(Variable var) {
        if (vars.containsKey(var)) {
            return vars.get(var);
        }
        setVar(var, var.defaultValue);
        return var.defaultValue;
    }

    public Map<Variable, Integer> getVariables() {
        return vars;
    }

    public void setVar(Variable var, int value) {
        vars.put(var, value);
    }

}
