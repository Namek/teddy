package net.namekdev.mgame.builders;

import net.namekdev.mgame.enums.CollisionGroups;
import net.namekdev.mgame.systems.base.collision.CollisionDetectionSystem;

import com.artemis.Entity;
import com.artemis.Manager;
import com.artemis.annotations.Wire;
import com.badlogic.gdx.math.Vector3;

@Wire
public class WorldInitSystem extends Manager {
	EntityFactory entityFactory;
	CollisionDetectionSystem collisionSystem;


	@Override
	protected void initialize() {
		Vector3 vect3 = new Vector3();

		collisionSystem.relations.connectGroups(CollisionGroups.TEDDY, CollisionGroups.TOYS);

		entityFactory.createTeddy(vect3.set(0.5f, 0f, -0.2f));
		entityFactory.createRoom();

		Entity airplane = entityFactory.createToy("airplane", vect3.set(0.5f, 0f, -6.4f), true);
		Entity alien = entityFactory.createToy("alien", vect3.set(8.5f, 0f, -9.4f), true);
		Entity workbench = entityFactory.createToy("workbench", vect3.set(18.5f, 0f, -5.4f), false);

		// TODO create furniture, obstacles, etc.
	}
}
