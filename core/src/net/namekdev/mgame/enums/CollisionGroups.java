package net.namekdev.mgame.enums;

public interface CollisionGroups {
	public static final int ROOM = 1 << 0;
	public static final int TEDDY = 1 << 1;
	public static final int TOYS = 1 << 2;
	public static final int EVERYTHING_MOVABLE = TOYS | TEDDY;
}
