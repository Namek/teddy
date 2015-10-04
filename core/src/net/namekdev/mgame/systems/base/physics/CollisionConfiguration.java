package net.namekdev.mgame.systems.base.physics;

import org.ode4j.ode.DContact.DSurfaceParameters;

import net.namekdev.mgame.systems.base.collision.messaging.CollisionEvent;

/**
 * Determines parameters for collision between physical
 * bodies belonging to two distinct collision groups.
 *
 * @author Namek
 * @see CollisionGroupsRelations
 * @see PhysicsSystem
 */
public class CollisionConfiguration {
	public boolean collide = true;

	/** @see CollisionEvent */
	public boolean triggerEnterEvent = false;

//	public DSurfaceParameters parameters = new DSurfaceParameters();
}
