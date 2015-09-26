package net.namekdev.mgame.systems.base.collision.messaging;

public interface CollisionEnterListener {
	void onCollisionEnter(int entityId, int otherEntityId);
}
