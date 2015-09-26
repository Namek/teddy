package net.namekdev.mgame.components.base;

import com.artemis.PooledComponent;
import com.badlogic.gdx.math.Vector3;

public class Attached extends PooledComponent {
	public int entityId = -1;
	public final Vector3 offset = new Vector3();


	@Override
	protected void reset() {
		entityId = -1;
		offset.set(0, 0, 0);
	}
}
