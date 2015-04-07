package net.namekdev.mgame.components;

import com.artemis.PooledComponent;

public class Renderable extends PooledComponent {
	public static final int TEXTURE = 14213;
	public static final int KEYFRAMES = 14214;
	
	public int type;

	
	@Override
	protected void reset() {
		type = 0;
	}
}
