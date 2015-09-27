package net.namekdev.mgame.components.base;

import com.artemis.PooledComponent;
import com.badlogic.gdx.math.Vector3;

/**
 * Usage: set {@link #acceleration} OR {@link Force} and let {@link PositionSystem} calculate velocity.
 *
 * <p>Set value of {@link #friction} to configure how entity slows down when {@link #frictionOn} {@code = true}.</p>
 *
 * @author Namek
 * @see PositionSystem
 */
public class Velocity extends PooledComponent {
	public final Vector3 velocity = new Vector3();
	public final Vector3 acceleration = new Vector3();

	public final Vector3 extVelocity = new Vector3();
	public final Vector3 extAcceleration = new Vector3();


	/**
	 * Maximum speed. Length of {@link #velocity} will be limited by this value.
	 * Won't work when it's less than zero.
	 */
	public float maxSpeed = -1;

	/**
	 * Maximum speed for external velocity ({@link #extVelocity}).
	 * Won't work when it's less than zero.
	 */
	public float maxExtSpeed = -1;

	/**
	 * Describes how much of speed ({@link #velocity}) to decrease during one second.
	 *
	 * <p>
	 * When friction is greater than current speed then object stops moving.
	 * When friction is lower than current speed then object just moves slower.
	 * </p>
	 *
	 * <p>To enable friction, set {@code frictionOn = true}</p>
	 *
	 * @see #maxSpeed
	 */
	public float friction;

	/**
	 * @see #friction
	 */
	public boolean frictionOn;


	public float extFriction;
	public boolean extFrictionOn;


	public Velocity setup(float maxSpeed) {
		this.maxSpeed = maxSpeed;
		this.friction = 0;
		this.frictionOn = false;
		return this;
	}

	public Velocity setup(float maxSpeed, float friction) {
		this.maxSpeed = maxSpeed;
		this.friction = friction;
		this.frictionOn = true;
		return this;
	}

	public Velocity friction(float friction) {
		this.frictionOn = true;
		this.friction = friction;
		return this;
	}

	public Velocity extFriction(float friction) {
		this.extFrictionOn = true;
		this.extFriction = friction;
		return this;
	}

	public float getCurrentSpeed() {
		return velocity.len();
	}

	@Override
	protected void reset() {
		velocity.set(0, 0, 0);
		acceleration.set(0, 0, 0);
		extVelocity.set(0, 0, 0);
		extAcceleration.set(0, 0, 0);
		maxSpeed = -1;
		maxExtSpeed = -1;
		friction = 0;
		frictionOn = false;
		extFriction = 0;
		extFrictionOn = false;
	}
}