package net.namekdev.mgame;

import net.namekdev.mgame.builders.EntityFactory;
import net.namekdev.mgame.builders.WorldInitSystem;
import net.namekdev.mgame.systems.CameraSystem;
import net.namekdev.mgame.systems.MovementSystem;
import net.namekdev.mgame.systems.RenderSystem;
import net.namekdev.mgame.systems.TeddyStateSystem;
import net.namekdev.mgame.systems.TestSystem;
import net.namekdev.mgame.systems.base.PositionSystem;
import net.namekdev.mgame.systems.base.TimeSystem;
import net.namekdev.mgame.systems.render.KeyframedDecalUpdateSystem;

import com.artemis.World;
import com.artemis.WorldConfiguration;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;

public class MGame extends ApplicationAdapter {
	World world;

	@Override
	public void create () {
		WorldConfiguration cfg = new WorldConfiguration();
		cfg.setSystem(new EntityFactory());
		cfg.setSystem(new WorldInitSystem());
		cfg.setSystem(new TimeSystem());
		cfg.setSystem(new TeddyStateSystem());
		cfg.setSystem(new PositionSystem());
		cfg.setSystem(new MovementSystem());
		cfg.setSystem(new CameraSystem());
		cfg.setSystem(new KeyframedDecalUpdateSystem());
		cfg.setSystem(new RenderSystem());
		cfg.setSystem(new TestSystem());
		world = new World(cfg);
	}

	@Override
	public void render () {
		world.delta = Math.min(1/15f, Gdx.graphics.getDeltaTime());
		world.process();
	}
}
