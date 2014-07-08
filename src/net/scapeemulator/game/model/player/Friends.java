package net.scapeemulator.game.model.player;

import java.util.Arrays;
import java.util.List;
import net.scapeemulator.game.model.World;
import net.scapeemulator.game.msg.impl.FriendStatusMessage;
import net.scapeemulator.game.msg.impl.FriendsListStatusMessage;
import net.scapeemulator.game.msg.impl.PrivateChatReceivedMessage;
import net.scapeemulator.game.msg.impl.PrivateChatSentMessage;
import net.scapeemulator.game.msg.impl.SendIgnoresMessage;
import net.scapeemulator.util.ChatUtils;

public class Friends {

	public enum FriendsListStatus {
		LOADING, CONNECTING, CONNECTED
	}

	private final Player player;

	private List<Long> friendsList;
	private List<Long> ignoreList;

	private static final int ON = 0;
	private static final int FRIENDS = 1;
	private static final int OFF = 2;
	private static final int HIDE = 3;

	private int messageTicket;

	public Friends(Player player) {
		this.player = player;
	}

	public void init() {
		player.send(new FriendsListStatusMessage((byte) FriendsListStatus.CONNECTED.ordinal()));
		for(long friend : friendsList) {
			Player other = World.getWorld().getPlayerByLongName(friend);
			if(other != null) {
				if(other.getFriends().canPlayerSeeMe(player)) {
					player.send(new FriendStatusMessage(friend, 1));
					continue;
				}
			}
			player.send(new FriendStatusMessage(friend, 0));
		}
		if (ignoreList.size() > 0) {
			player.send(new SendIgnoresMessage(ignoreList));
		}
		for (Player other : World.getWorld().getPlayers()) {
			if (canPlayerSeeMe(other)) {
				other.getFriends().loggedIn(player);
			}
		}
	}

	public void logout() {
		for (Player other : World.getWorld().getPlayers()) {
			other.getFriends().loggedOut(player);
		}
	}
	
	public void privateChatSent(long name, byte[] packed) {
		if (!friendsList.contains(name)) {
			return;
		}
		// Repack the text to ensure its all valid
		String text = ChatUtils.unpack(packed);
		packed = new byte[256];
		int len = ChatUtils.pack(text, packed);
		packed = Arrays.copyOf(packed, len);
		Player other = World.getWorld().getPlayerByLongName(name);
		if (other != null) {
			if (other.getFriends().canPlayerSeeMe(player)) {
				other.send(new PrivateChatReceivedMessage(player.getLongUsername(), (byte) player.getRights(), packed, other.getFriends().getNextMessageTicket()));
				player.send(new PrivateChatSentMessage(name, packed));
			}
		}

	}

	public void addFriend(long name) {
		if (friendsList.contains(name) || ignoreList.contains(name) || name == player.getLongUsername()) {
			return;
		}
		if (friendsList.size() >= 200) {
			player.sendMessage("Your friends list is full!");
			return;
		}
		friendsList.add(name);
		Player other = World.getWorld().getPlayerByLongName(name);
		if (other != null) {
			if (other.getFriends().canPlayerSeeMe(player)) {
				loggedIn(other);
			}
			if (player.getSettings().getPrivateChat() == FRIENDS) {
				other.getFriends().loggedIn(player);
			}
		}
	}

	public void addIgnore(long name) {
		if (friendsList.contains(name) || ignoreList.contains(name) || name == player.getLongUsername()) {
			return;
		}
		if (ignoreList.size() >= 100) {
			player.sendMessage("Your ignore list is full!");
			return;
		}
		ignoreList.add(name);
		Player other = World.getWorld().getPlayerByLongName(name);
		if (other != null) {
			other.getFriends().loggedOut(player);
		}
	}

	public void removeFriend(long name) {
		if (!friendsList.contains(name)) {
			return;
		}
		Player other = World.getWorld().getPlayerByLongName(name);
		if (other != null) {
			if (player.getSettings().getPrivateChat() == FRIENDS) {
				other.getFriends().loggedOut(player);
			}
		}
		friendsList.remove(name);
	}

	public void removeIgnore(long name) {
		if (!ignoreList.contains(name)) {
			return;
		}
		Player other = World.getWorld().getPlayerByLongName(name);
		if (other != null) {
			if (player.getSettings().getPrivateChat() == ON) {
				other.getFriends().loggedIn(player);
			}
		}
		ignoreList.remove(name);
	}

	public void loggedIn(Player other) {
		if (friendsList.contains(other.getLongUsername())) {
			player.send(new FriendStatusMessage(other.getLongUsername(), 1));
		}
	}

	public void loggedOut(Player other) {
		if(friendsList.contains(other.getLongUsername())) {
			player.send(new FriendStatusMessage(other.getLongUsername(), 0));
		}
	}

	public boolean canPlayerSeeMe(Player other) {
		switch (player.getSettings().getPrivateChat()) {
		case FRIENDS:
			return friendsList.contains(other.getLongUsername());
		case ON:
			return !ignoreList.contains(other.getLongUsername());
		default:
			return false;
		}
	}

	public void refresh() {
		for (Player other : World.getWorld().getPlayers()) {
			if (canPlayerSeeMe(other)) {
				other.getFriends().loggedIn(player);
			} else {
				other.getFriends().loggedOut(player);
			}
		}
	}

	public void changePrivateChatSetting(int status) {
		if (status > 2 || status < 0 || status == player.getSettings().getPrivateChat()) {
			return;
		}
		player.getSettings().setPrivateChat(status);
		refresh();
	}

	public void changePublicChatSetting(int status) {
		if (status > 3 || status < 0 || status == player.getSettings().getPublicChat()) {
			return;
		}
		player.getSettings().setPublicChat(status);
	}

	public void changeTradeRequestsSetting(int status) {
		if (status > 2 || status < 0 || status == player.getSettings().getTradeRequests()) {
			return;
		}
		player.getSettings().setTradeRequests(status);
	}
	
	public void loadedLists(List<Long> friendsList, List<Long> ignoreList) {
		this.friendsList = friendsList;
		this.ignoreList = ignoreList;
	}

	public List<Long> getFriendsList() {
		return friendsList;
	}

	public List<Long> getIgnoreList() {
		return ignoreList;
	}

	public int getNextMessageTicket() {
		return ++messageTicket;
	}

}
