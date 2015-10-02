package net.namekdev.mgame.enums;

public interface CollisionGroups {
	public static final int ROOM = 1;
	public static final int TEDDY = 2;
	public static final int TOYS = 4;
	public static final int EVERYTHING_MOVABLE = TOYS | TEDDY;
}
