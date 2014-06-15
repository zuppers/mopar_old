package net.scapeemulator.game.model.player;

import net.scapeemulator.game.msg.impl.ScriptMessage;

/**
 * Written by David Insley
 */
public final class ScriptInput {
    
	private final Player player;
	
	IntegerScriptInputListener intListener;
	//StringScriptInputListener stringListener;
	
	public ScriptInput(Player player) {
		this.player = player;
	}
	
	public void showIntegerScriptInput() {
		showIntegerScriptInput("Enter amount:");
	}
	
	public void showIntegerScriptInput(String prompt) {
		player.send(new ScriptMessage(108, "s", prompt));
	}
	
    public IntegerScriptInputListener getIntegerInputListener() {
		return intListener;
	}

	public void setIntegerInputListener(IntegerScriptInputListener listener) {
    	intListener = listener;
    }
	
	public void reset() {
		//TODO reset when moving at all, not just walking
		intListener = null;
		//stringListener = null;
	}
    
}
