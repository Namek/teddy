package net.namekdev.mgame.systems.base;

import net.namekdev.mgame.components.base.Attached;
import net.namekdev.mgame.components.base.Dimensions;
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
	ComponentMapper<Dimensions> mDimensions;
	ComponentMapper<Transform> mTransform;

	private final Vector3 vect3 = new Vector3();


	public AttachmentSystem() {
		super(Aspect.all(Attached.class));
	}

	@Override
	protected void process(Entity entity) {
		Attached attached = mAttached.get(entity);

		if (attached.entityId == -1) {
			return;
		}

		Transform transform = mTransform.get(entity);
		Transform parentTransform = mTransform.get(attached.entityId);

		vect3.set(parentTransform.currentPos).add(attached.offset);
		transform.xyz(vect3);
	}

	public void attach(int attachedId, int attachedToId) {
		Entity attachedEntity = world.getEntity(attachedId);
		Dimensions teddyDims = mDimensions.get(attachedToId);

		Attached attach = attachedEntity.edit().create(Attached.class);
		attach.entityId = attachedToId;
		attach.offset.x = teddyDims.getWidth();
		attach.offset.y = teddyDims.getHeight()/2;
	}

	public void deattach(int attachedId) {
		Entity attachedEntity = world.getEntity(attachedId);
		attachedEntity.edit().remove(Attached.class);
	}

}
