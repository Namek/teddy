package net.namekdev.mgame.systems.render.renderers;

import net.namekdev.mgame.components.Renderable;
import net.namekdev.mgame.components.render.SpriteComponent;
import net.namekdev.mgame.systems.render.RenderBatchingSystem.EntityProcessAgent;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class SpriteRenderer implements EntityProcessAgent {
	private SpriteBatch batch;
	private ComponentMapper<SpriteComponent> sm;


	public SpriteRenderer(World world, SpriteBatch batch) {
		this.batch = batch;
		sm = world.getMapper(SpriteComponent.class);
	}

	@Override
	public void begin() {
		batch.begin();
	}

	@Override
	public void end() {
		batch.end();
	}

	@Override
	public void process(Entity e) {
		SpriteComponent sprite = sm.get(e);

		batch.setBlendFunction(sprite.blendSrcFunc, sprite.blendDestFunc);
		sprite.sprite.draw(batch);
	}

	@Override
	public int getType() {
		return Renderable.SPRITE;
	}
}