package net.namekdev.mgame.enums;

public interface MConstants {
	public static final float DecalScaleToImageSize = 0.02f;
	public static final float DecalDimensionDepth = 0.1f;

	public interface Teddy {
		public static final float Mass = 20f;
		public static final float Acceleration = 160f;
		public static final float MaxWalkSpeed = 6f;
		public static final float MaxRunSpeed = MaxWalkSpeed*2.2f;
		public static final float MaxJumpSpeed = MaxWalkSpeed*2.7f;
		public static final float Friction = 60f;

	}
}
