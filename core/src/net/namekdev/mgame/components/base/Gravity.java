package net.namekdev.mgame.components.base;

import net.namekdev.mgame.systems.base.PositionSystem;

import com.artemis.PooledComponent;
import com.badlogic.gdx.math.Vector3;

/**
 * An optional component for {@link PositionSystem}.
 *
 * @author Namek
 */
public class Gravity extends PooledComponent {
	public static final float DEFAULT_GRAVITY = 9.80665f;

	public final Vector3 force = new Vector3(0, -DEFAULT_GRAVITY, 0);
	public final Vector3 velocity = new Vector3();
	public float maxSpeed = -1;


	@Override
	protected void reset() {
		force.set(0, -DEFAULT_GRAVITY, 0);
		velocity.set(0, 0, 0);
		maxSpeed = -1;
	}
}
