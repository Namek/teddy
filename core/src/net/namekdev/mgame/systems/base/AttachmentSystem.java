package net.namekdev.mgame.systems.base;

import net.namekdev.mgame.components.base.Attached;
import net.namekdev.mgame.components.base.Transform;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.math.Vector3;

@Wire
public class AttachmentSystem extends EntityProcessingSystem {
	ComponentMapper<Attached> mAttached;
	ComponentMapper<Transform> mTransform;

	private final Vector3 vect3 = new Vector3();


	public AttachmentSystem() {
		super(Aspect.all(Attached.class));
	}

	@Override
	protected void process(Entity entity) {
		Attached attached = mAttached.get(entity);

		if (attached.entity == null) {
			return;
		}

		Transform transform = mTransform.get(entity);
		Transform parentTransform = mTransform.get(attached.entity);

		vect3.set(parentTransform.currentPos).add(attached.offset);
		transform.xyz(vect3);
	}

}
