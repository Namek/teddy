package net.namekdev.mgame;

import net.namekdev.mgame.components.AnimationComponent;
import net.namekdev.mgame.components.Movement;
import net.namekdev.mgame.components.Position;
import net.namekdev.mgame.components.Renderable;
import net.namekdev.mgame.components.Teddy;
import net.namekdev.mgame.systems.MovementSystem;
import net.namekdev.mgame.systems.RenderSystem;
import net.namekdev.mgame.systems.TeddyStateSystem;

import com.artemis.EntityEdit;
import com.artemis.World;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;

public class MGame extends ApplicationAdapter {
	World world;

	@Override
	public void create () {
		world = new World();
		world.setSystem(new TeddyStateSystem());
		world.setSystem(new MovementSystem());
		world.setSystem(new RenderSystem());
		world.initialize();
		
		EntityEdit teddy = world.createEntity().edit();
		teddy.create(Teddy.class);
		teddy.create(AnimationComponent.class);
		teddy.create(Renderable.class).type = Renderable.KEYFRAMES;
		teddy.create(Position.class);
		teddy.create(Movement.class);
	}

	@Override
	public void render () {
		world.delta = Gdx.graphics.getDeltaTime();
		world.process();
	}
}
