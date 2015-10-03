package net.namekdev.mgame.systems;

import net.namekdev.mgame.ToyCollisionEvent;
import net.namekdev.mgame.components.Carryable;
import net.namekdev.mgame.components.base.Attached;
import net.namekdev.mgame.systems.base.collision.Collider;
import net.namekdev.mgame.systems.base.collision.CollisionDetectionSystem;
import net.namekdev.mgame.systems.base.collision.messaging.CollisionEnterListener;
import net.namekdev.mgame.systems.base.events.EventSystem;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;

@Wire(injectInherited=true)
public class CollisionSystem extends CollisionDetectionSystem {
	ComponentMapper<Attached> mAttached;
	ComponentMapper<Carryable> mCarryable;

	EventSystem events;
	TeddyStateSystem teddySystem;


	@Override
	protected void initialize() {
		eventDispatchingEnabled = true;
	}

	@Override
	public void inserted(Entity entity) {
		if (mCarryable.has(entity.getId())) {
			Collider collider = mCollider.get(entity.getId());
			collider.enterListener = toyCollisionListener;
		}
	}

	private CollisionEnterListener toyCollisionListener = new CollisionEnterListener() {
		@Override
		public void onCollisionEnter(int toyId, int otherEntityId) {
			assert otherEntityId == teddySystem.teddyEntityId;
			events.dispatch(ToyCollisionEvent.class).toyId = toyId;
		}
	};

}
