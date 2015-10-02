package net.namekdev.mgame.components;

import net.namekdev.mgame.systems.base.collision.CollisionGroupsRelations;

import org.ode4j.ode.DBody;

import com.artemis.PooledComponent;
import com.badlogic.gdx.math.Vector3;

public class Physical extends PooledComponent {
	/** Body that can be {@code null} if entity is going to be static. */
	public DBody body;

	/** Bitset of groups that this entity will collide with.
	 * @see CollisionGroupsRelations */
	public long collisionGroups;



	public Physical position(Vector3 position) {
		body.setPosition(position.x, position.y, position.z);
		return this;
	}

	public Physical groups(long groups) {
		collisionGroups = groups;
		return this;
	}

	@Override
	protected void reset() {
	}
}
