package net.scapeemulator.game.tools;

import org.mindrot.jbcrypt.BCrypt;

public final class BCryptGen {
	
	public static void main(String[] args) {
		System.out.println(BCrypt.gensalt());
		System.out.println(BCrypt.hashpw("123", BCrypt.gensalt()));
	}

}
