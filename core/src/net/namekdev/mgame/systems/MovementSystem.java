package net.namekdev.mgame.systems;

import net.namekdev.mgame.components.base.Transform;

import com.artemis.Aspect;
import com.artemis.Aspect.Builder;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.systems.EntityProcessingSystem;

@Wire
public class MovementSystem extends EntityProcessingSystem {
	ComponentMapper<Transform> mTransform;


	public MovementSystem() {
		super(Aspect.all(Transform.class));
	}

	@Override
	protected void process(Entity entity) {
		Transform transform = mTransform.get(entity);

		transform.currentPos.set(transform.desiredPos);
	}

}
