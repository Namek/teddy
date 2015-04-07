package net.namekdev.mgame.components;

import com.artemis.PooledComponent;
import com.badlogic.gdx.math.Vector2;

public class Movement extends PooledComponent {
	public final Vector2 speed = new Vector2();

	@Override
	protected void reset() {
		speed.set(0, 0);
	}

}
