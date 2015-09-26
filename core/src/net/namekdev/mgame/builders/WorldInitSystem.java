package net.namekdev.mgame.builders;

import net.namekdev.mgame.enums.CollisionGroups;
import net.namekdev.mgame.systems.base.collision.CollisionDetectionSystem;

import com.artemis.BaseSystem;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.badlogic.gdx.math.Vector3;

@Wire
public class WorldInitSystem extends BaseSystem {
	EntityFactory entityFactory;
	CollisionDetectionSystem collisionSystem;


	@Override
	protected void processSystem() {
		Vector3 vect3 = new Vector3();

		collisionSystem.relations.connectGroups(CollisionGroups.DEFAULT, CollisionGroups.DEFAULT);

		entityFactory.createTeddy(vect3.set(0.5f, 0f, -0.2f));
		entityFactory.createRoom();

		Entity airplane = entityFactory.createToy("airplane", vect3.set(0.5f, 0f, -6.4f));
		Entity alien = entityFactory.createToy("alien", vect3.set(8.5f, 0f, -9.4f));
		Entity workbench = entityFactory.createToy("workbench", vect3.set(18.5f, 0f, -5.4f));

		// TODO create furniture, obstacles, etc.

		setPassive(true);
	}
}
