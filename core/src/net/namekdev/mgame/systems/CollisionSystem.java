package net.namekdev.mgame.systems;

import com.artemis.annotations.Wire;

import net.namekdev.mgame.systems.base.collision.Collider;
import net.namekdev.mgame.systems.base.collision.CollisionDetectionSystem;

@Wire(injectInherited=true)
public class CollisionSystem extends CollisionDetectionSystem {

	@Override
	public void onCollisionEnter(int entity1Id, Collider collider1, int entity2Id, Collider collider2) {
		super.onCollisionEnter(entity1Id, collider1, entity2Id, collider2);
	}

	@Override
	public void onCollisionExit(int entity1Id, Collider collider1, int entity2Id, Collider collider2) {
		super.onCollisionExit(entity1Id, collider1, entity2Id, collider2);
	}

}
