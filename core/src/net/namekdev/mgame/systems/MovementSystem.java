package net.namekdev.mgame.systems;

import net.namekdev.mgame.components.Movement;
import net.namekdev.mgame.components.Position;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.math.Vector2;

@Wire
public class MovementSystem extends EntityProcessingSystem {
	ComponentMapper<Movement> mm;
	ComponentMapper<Position> pm;
	
	final Vector2 tmp = new Vector2();
	
	public MovementSystem() {
		super(Aspect.getAspectForAll(Movement.class, Position.class));
	}

	@Override
	protected void process(Entity e) {
		Movement move = mm.get(e);
		Position pos = pm.get(e);
		
		tmp.set(move.speed).scl(world.delta);
		pos.current.add(tmp);
	}
}
