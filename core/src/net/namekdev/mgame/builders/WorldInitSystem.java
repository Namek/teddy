package net.namekdev.mgame.builders;

import com.artemis.BaseSystem;
import com.artemis.annotations.Wire;

@Wire
public class WorldInitSystem extends BaseSystem {
	EntityFactory entityFactory;


	@Override
	protected void processSystem() {
		entityFactory.createTeddy();
		entityFactory.createRoom();

		// TODO create furniture, obstacles, etc.

		setPassive(true);
	}


}
