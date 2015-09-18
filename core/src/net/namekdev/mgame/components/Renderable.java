package net.namekdev.mgame.components;

import com.artemis.PooledComponent;

public class Renderable extends PooledComponent {
	public static final int NONE = 0;
	public static final int DECAL = 2;
	public static final int SPRITE = 4;
	public static final int MODEL = 8;

	/**
     * Layer: higher is in front, lower is behind.
     */
    public int layer = 0;

    /**
     * Mask for combination of renderer types: NONE, DECAL, SPRITE, MODEL.
     */
    public int type = NONE;


	@Override
	protected void reset() {
		type = NONE;
		layer = 0;
	}
}
