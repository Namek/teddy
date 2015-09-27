package net.namekdev.mgame.components.base;

import net.namekdev.mgame.systems.base.PositionSystem;

import com.artemis.PooledComponent;
import com.badlogic.gdx.math.Vector3;

/**
 * Forces that are used by {@link PositionSystem} to move the entity.
 * <p>To limit maximum speed, set up {@link Velocity}.</p>
 *
 * @author Namek
 */
public class Force extends PooledComponent {
	/** Own force used to move entity, can be limited by {@link Velocity#maxSpeed}. */
	public final Vector3 accelForce = new Vector3();

	/**
	 * Additional force that doesn't have to be limited by the same value as {@link #accelForce}.
	 * If you want to limit it anyway, then set up {@link Velocity#maxExtSpeed}.
	 */
	public final Vector3 extForce = new Vector3();

	public final Vector3 impulseForce = new Vector3();
	public float impulseTime = 0;

	public void impulse(Vector3 force, float time) {
		impulseForce.set(force);
		impulseTime = time;
	}

	public void applyImpulse(float x, float y, float z, float time) {
		impulseForce.set(x, y, z);
		impulseTime = time;
	}

	@Override
	protected void reset() {
		accelForce.set(0, 0, 0);
		extForce.set(0, 0, 0);
		impulseForce.set(0, 0, 0);
		impulseTime = 0;
	}
}
