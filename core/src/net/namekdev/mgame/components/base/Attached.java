package net.namekdev.mgame.components.base;

import com.artemis.Entity;
import com.artemis.PooledComponent;
import com.badlogic.gdx.math.Vector3;

public class Attached extends PooledComponent {
	public Entity entity;
	public final Vector3 offset = new Vector3();


	@Override
	protected void reset() {
		entity = null;
		offset.set(0, 0, 0);
	}
}
