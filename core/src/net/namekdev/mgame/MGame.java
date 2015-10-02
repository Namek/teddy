package net.namekdev.mgame;

import net.mostlyoriginal.api.plugin.extendedcomponentmapper.ExtendedComponentMapperPlugin;

import com.artemis.WorldConfigurationBuilder;

import net.namekdev.mgame.builders.EntityFactory;
import net.namekdev.mgame.builders.WorldInitSystem;
import net.namekdev.mgame.components.Teddy;
import net.namekdev.mgame.systems.CameraSystem;
import net.namekdev.mgame.systems.CollisionSystem;
import net.namekdev.mgame.systems.MovementSystem;
import net.namekdev.mgame.systems.PhysicsDebugSystem;
import net.namekdev.mgame.systems.PhysicsSystem;
import net.namekdev.mgame.systems.RenderSystem;
import net.namekdev.mgame.systems.TeddyStateSystem;
import net.namekdev.mgame.systems.TestSystem;
import net.namekdev.mgame.systems.base.AttachmentSystem;
import net.namekdev.mgame.systems.base.PositionSystem;
import net.namekdev.mgame.systems.base.TimeSystem;
import net.namekdev.mgame.systems.base.events.EventSystem;
import net.namekdev.mgame.systems.render.KeyframedDecalUpdateSystem;

import com.artemis.Aspect;
import com.artemis.Aspect.Builder;
import com.artemis.EntitySystem;
import com.artemis.World;
import com.artemis.WorldConfiguration;
import com.artemis.annotations.Wire;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;

public class MGame extends ApplicationAdapter {
	World world;

	@Override
	public void create () {
		WorldConfiguration cfg = new WorldConfigurationBuilder()
			.with(new ExtendedComponentMapperPlugin())
			.with(new EventSystem())
			.with(new TimeSystem())
			.with(new TeddyStateSystem())
//			.with(new PositionSystem())
			.with(new AttachmentSystem())
//			.with(new CollisionSystem())
//			.with(new MovementSystem())
			.with(new PhysicsSystem())
			.with(new CameraSystem())
			.with(new KeyframedDecalUpdateSystem())
			.with(new RenderSystem())
			.with(new PhysicsDebugSystem())
			.with(new TestSystem())
			.with(new EntityFactory())
			.with(new WorldInitSystem())
			.build();

		world = new World(cfg);
	}

	@Override
	public void render () {
		world.delta = Math.min(1/15f, Gdx.graphics.getDeltaTime());
		world.process();
	}


	@Wire
	public static class TestEntitySystem extends EntitySystem {

		public TestEntitySystem() {
			super(Aspect.all(Teddy.class));
		}
		@Override
		protected boolean checkProcessing() {
			return subscription.getEntities().size() > 0;
		}



		@Override
		protected void processSystem() {
			int a = 5;
		}

	}
}
