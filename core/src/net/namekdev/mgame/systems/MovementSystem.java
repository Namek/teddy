package net.namekdev.mgame.systems;

import net.namekdev.mgame.components.base.Transform;
import net.namekdev.mgame.components.base.Velocity;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.systems.EntityProcessingSystem;

@Wire
public class MovementSystem extends EntityProcessingSystem {
	ComponentMapper<Transform> mTransform;
	ComponentMapper<Velocity> mVelocity;

	public MovementSystem() {
		super(Aspect.all(Transform.class, Velocity.class));
	}

	@Override
	protected void process(Entity entity) {
		Transform transform = mTransform.get(entity);
		Velocity velocity = mVelocity.get(entity);

		transform.currentPos.set(transform.desiredPos);

		if (transform.desiredPos.y <= 0) {
			transform.desiredPos.y = transform.currentPos.y = 0;
			velocity.extVelocity.y = 0;
		}

		if (transform.desiredPos.z >= 0f) {
			transform.desiredPos.z = 0f;
			velocity.velocity.z = 0;
		}
	}

}
