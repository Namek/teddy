package net.namekdev.mgame.systems;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.badlogic.gdx.math.Vector3;

import net.namekdev.mgame.components.Carryable;
import net.namekdev.mgame.components.base.Attached;
import net.namekdev.mgame.components.base.Dimensions;
import net.namekdev.mgame.systems.base.collision.Collider;
import net.namekdev.mgame.systems.base.collision.CollisionDetectionSystem;
import net.namekdev.mgame.systems.base.collision.messaging.CollisionEnterListener;

@Wire(injectInherited=true)
public class CollisionSystem extends CollisionDetectionSystem {
	ComponentMapper<Attached> mAttached;
	ComponentMapper<Carryable> mCarryable;
	ComponentMapper<Dimensions> mDimensions;


	@Override
	protected void initialize() {

	}

	@Override
	protected void inserted(int entityId) {
		if (mCarryable.has(entityId)) {
			Collider collider = mCollider.get(entityId);
			collider.enterListener = toyCollisionListener;
		}
	}

	private CollisionEnterListener toyCollisionListener = new CollisionEnterListener() {
		@Override
		public void onCollisionEnter(int toyId, int otherEntityId) {
			Entity toy = world.getEntity(toyId);
			Dimensions dims = mDimensions.get(otherEntityId);
			Attached toyAttach = toy.edit().create(Attached.class);
			toyAttach.entity = world.getEntity(otherEntityId);
			toyAttach.offset.x = dims.getWidth();
			toyAttach.offset.y = dims.getHeight()/2;
		}
	};

}
