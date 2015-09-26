package net.namekdev.mgame.systems.base.collision.messaging;

public interface CollisionExitListener {
	void onCollisionExit(int entityId, int otherEntityId);
}
