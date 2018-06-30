package com.su.core.game;

/**
 * 可匹配的牌桌
 */
public class ClassicTable extends MatchTable {
	
	public ClassicTable(ClassicSite room) {
		super(room);
		match = room;
	}
}
