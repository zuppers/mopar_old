package net.scapeemulator.game.io.jdbc;

import java.io.IOException;
import java.sql.SQLException;

public abstract class Table<E> {

	public abstract void load(E e) throws SQLException, IOException;

	public abstract void save(E e) throws SQLException, IOException;

}
