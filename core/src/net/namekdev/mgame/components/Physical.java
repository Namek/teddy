package net.namekdev.mgame.components;

import org.ode4j.ode.DBody;

import com.artemis.PooledComponent;
import com.badlogic.gdx.math.Vector3;

public class Physical extends PooledComponent {
	public DBody body;

	/**
	 * can't be moved by another entity
	 */
	public boolean isStatic = false;


	public Physical position(Vector3 position) {
		body.setPosition(position.x, position.y, position.z);
		return this;
	}

	@Override
	protected void reset() {
	}
}
