package net.namekdev.mgame.systems.base;

import net.namekdev.mgame.components.base.Force;
import net.namekdev.mgame.components.base.Gravity;
import net.namekdev.mgame.components.base.PreviousPosition;
import net.namekdev.mgame.components.base.Transform;
import net.namekdev.mgame.components.base.Velocity;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.math.Vector3;

/**
 * <p>System that calculates <b>desired</b> position of moving entity.</p>
 * <p>This system needs a companion system (processing after this one)
 * which will check and modify/copy {@code desiredPos} position into {@code currentPos}.</p>
 * <p>{@link Force} component is optional but preferred for moving entities.
 * Otherwise use {@link Velocity#acceleration} directly.</p>
 * <p>You can also apply a {@link Gravity}.</p>
 *
 * @see Transform
 * @author Namek
 */
@Wire
public class PositionSystem extends EntityProcessingSystem {
	ComponentMapper<Transform> mTransform;
	ComponentMapper<PreviousPosition> mPreviousPos;
	ComponentMapper<Velocity> mVelocity;
	ComponentMapper<Force> mForce;
	ComponentMapper<Gravity> mGravity;

	TimeSystem timeSystem;


	private final Vector3 posDelta = new Vector3();
	private final Vector3 accelDelta = new Vector3();


	public PositionSystem() {
		super(Aspect.all(Transform.class, Velocity.class));
	}

	/**
	 * Velocity Verlet, maxSpeed, friction, gravity
	 */
	@Override
	protected void process(Entity e) {
		Transform transform = mTransform.get(e);
		PreviousPosition previousPosition = mPreviousPos.get(e);
		Velocity velocityComponent = mVelocity.get(e);
		Force force = mForce.get(e);
		Gravity gravity = mGravity.get(e);

		if (previousPosition != null) {
			previousPosition.position.set(transform.currentPos);
		}

		float deltaTime = timeSystem.getDeltaTime(e);

		Vector3 velocity = velocityComponent.velocity;
		Vector3 acceleration = velocityComponent.acceleration;
		Vector3 extVelocity = velocityComponent.extVelocity;
		Vector3 extAcceleration = velocityComponent.extAcceleration;


		if (force != null) {
			acceleration.set(force.accelForce);
			extAcceleration.set(force.extForce);
		}

		// Calculate entity's own velocity
		accelDelta.set(acceleration).scl(deltaTime);
		posDelta.set(accelDelta).scl(0.5f).add(velocity).scl(deltaTime);
		velocity.add(accelDelta);
		transform.desiredPos.add(posDelta);

		if (velocityComponent.maxSpeed >= 0) {
			velocity.limit(velocityComponent.maxSpeed);
		}

		if (velocityComponent.frictionOn) {
			float friction = velocityComponent.friction * deltaTime;
			float speed = velocity.len();

			if (friction < speed) {
				float coeff = friction / speed;
				if (coeff < 0.0001f) {
					coeff = 0;
				}
				velocity.scl(coeff);
			}
			else {
				velocity.set(0, 0, 0);
			}
		}

		// Calculate additional velocity for external forces
		if (force != null) {
			accelDelta.set(extAcceleration).scl(deltaTime);
			posDelta.set(accelDelta).scl(0.5f).add(extVelocity).scl(deltaTime);
			extVelocity.add(accelDelta);
			transform.desiredPos.add(posDelta);

			if (velocityComponent.maxExtSpeed >= 0) {
				extVelocity.limit(velocityComponent.maxExtSpeed);
			}
		}

		// Calculate gravity
		if (gravity != null) {
			accelDelta.set(gravity.force).scl(deltaTime);
			posDelta.set(accelDelta).scl(0.5f).add(gravity.velocity).scl(deltaTime);
			gravity.velocity.add(accelDelta);

			if (gravity.maxSpeed >= 0) {
				gravity.velocity.limit(gravity.maxSpeed);
			}

			transform.desiredPos.add(posDelta);
		}
	}
}