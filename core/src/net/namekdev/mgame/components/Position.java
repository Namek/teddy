package net.namekdev.mgame.components;

import com.artemis.PooledComponent;
import com.badlogic.gdx.math.Vector2;

public class Position extends PooledComponent {
	public final Vector2 current = new Vector2();
	
	@Override
	protected void reset() {
		current.set(0, 0);
	}
}
