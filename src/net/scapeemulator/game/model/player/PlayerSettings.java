package net.scapeemulator.game.model.player;

import net.scapeemulator.game.msg.impl.ConfigMessage;
import net.scapeemulator.game.msg.impl.PrivacySettingsUpdateMessage;
import net.scapeemulator.game.msg.impl.ScriptMessage;

public final class PlayerSettings {

	private final Player player;

	private int attackStyle;
	private int privateChat;
	private int publicChat;
	private int tradeRequests;
	private boolean autoRetaliating = true;
	private boolean running = false; /* TODO move to Player as it isn't saved */
	private boolean twoButtonMouse = true;
	private boolean chatFancy = true;
	private boolean privateChatSplit = false;
	private boolean acceptingAid = false;

	public PlayerSettings(Player player) {
		this.player = player;
	}

	public void setRunning(boolean running) {
		this.running = running;
		refreshRunning();
	}

	public boolean isRunning() {
		return running;
	}

	public void toggleRunning() {
		running = !running;
		refreshRunning();
	}

	public void setAttackStyle(int attackStyle) {
		this.attackStyle = attackStyle;
		refreshAttackStyle();
	}

	public int getAttackStyle() {
		return attackStyle;
	}

	public int getPrivateChat() {
		return privateChat;
	}
	
	public int getPublicChat() {
		return publicChat;
	}
	
	public int getTradeRequests() {
		return tradeRequests;
	}
	
	public void setPrivateChat(int privateChat) {
		setPrivateChat(privateChat, true);
	}

	public void setPublicChat(int publicChat) {
		setPublicChat(publicChat, true);
	}

	public void setTradeRequests(int tradeRequests) {
		setTradeRequests(tradeRequests, true);
	}
	
	public void setPrivateChat(int privateChat, boolean refresh) {
		this.privateChat = privateChat;
		if(refresh)
		refreshPrivacy();
	}

	public void setPublicChat(int publicChat, boolean refresh) {
		this.publicChat = publicChat;
		if(refresh)
		refreshPrivacy();
	}

	public void setTradeRequests(int tradeRequests, boolean refresh) {
		this.tradeRequests = tradeRequests;
		if(refresh)
		refreshPrivacy();
	}
	
	public void toggleAutoRetaliating() {
		autoRetaliating = !autoRetaliating;
		refreshAutoRetaliating();
	}

	public void setAutoRetaliating(boolean autoRetaliating) {
		this.autoRetaliating = autoRetaliating;
		refreshAutoRetaliating();
	}

	public boolean isAutoRetaliating() {
		return autoRetaliating;
	}

	public void setTwoButtonMouse(boolean twoButtonMouse) {
		this.twoButtonMouse = twoButtonMouse;
		refreshTwoButtonMouse();
	}

	public void toggleTwoButtonMouse() {
		twoButtonMouse = !twoButtonMouse;
		refreshTwoButtonMouse();
	}

	public boolean isTwoButtonMouse() {
		return twoButtonMouse;
	}

	public void setChatFancy(boolean chatFancy) {
		this.chatFancy = chatFancy;
		refreshChatFancy();
	}

	public void toggleChatFancy() {
		chatFancy = !chatFancy;
		refreshChatFancy();
	}

	public boolean isChatFancy() {
		return chatFancy;
	}

	public void setPrivateChatSplit(boolean privateChatSplit) {
		this.privateChatSplit = privateChatSplit;
		refreshPrivateChatSplit();
	}

	public void togglePrivateChatSplit() {
		privateChatSplit = !privateChatSplit;
		refreshPrivateChatSplit();
	}

	public boolean isPrivateChatSplit() {
		return privateChatSplit;
	}

	public void setAcceptingAid(boolean acceptingAid) {
		this.acceptingAid = acceptingAid;
		refreshAcceptingAid();
	}

	public void toggleAcceptingAid() {
		acceptingAid = !acceptingAid;
		refreshAcceptingAid();
	}

	public boolean isAcceptingAid() {
		return acceptingAid;
	}

	public Player getPlayer() {
		return player;
	}

	public void refresh() {
		refreshRunning();
		refreshAttackStyle();
		refreshAutoRetaliating();
		refreshTwoButtonMouse();
		refreshChatFancy();
		refreshPrivateChatSplit();
		refreshAcceptingAid();
		refreshPrivacy();
	}

	private void refreshRunning() {
		player.send(new ConfigMessage(173, running ? 1 : 0));
	}

	private void refreshAttackStyle() {
		player.send(new ConfigMessage(43, attackStyle));
	}

	private void refreshPrivacy() {
		player.send(new PrivacySettingsUpdateMessage(publicChat, privateChat, tradeRequests));
	}

	private void refreshAutoRetaliating() {
		player.send(new ConfigMessage(172, autoRetaliating ? 0 : 1));
	}

	private void refreshTwoButtonMouse() {
		player.send(new ConfigMessage(170, twoButtonMouse ? 0 : 1));
	}

	private void refreshChatFancy() {
		player.send(new ConfigMessage(171, chatFancy ? 0 : 1));
	}

	private void refreshPrivateChatSplit() {
		player.send(new ConfigMessage(287, privateChatSplit ? 1 : 0));
		if (privateChatSplit) {
			player.send(new ScriptMessage(83, "s"));
		}
	}

	private void refreshAcceptingAid() {
		player.send(new ConfigMessage(427, acceptingAid ? 1 : 0));
	}

}
