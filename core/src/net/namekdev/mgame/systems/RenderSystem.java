package net.namekdev.mgame.systems;

import net.namekdev.mgame.components.AnimationComponent;
import net.namekdev.mgame.components.Position;
import net.namekdev.mgame.components.Renderable;

import com.artemis.Aspect.Builder;
import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.EntitySystem;
import com.artemis.annotations.Wire;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

@Wire
public class RenderSystem extends EntitySystem {
	ComponentMapper<AnimationComponent> am;
	ComponentMapper<Position> pm;
	ComponentMapper<Renderable> rm;
	
	SpriteBatch batch;

	public RenderSystem() {
		super(Aspect.all(Renderable.class));
	}

	@Override
	protected void initialize() {
		batch = new SpriteBatch();
	}

	@Override
	protected void processSystem() {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.begin();

		for (int i = 0, n = actives.size(); i < n; ++i) {
			flyweight.id = actives.get(i);
			
			Renderable renderable = rm.get(flyweight);
			
			switch (renderable.type) {
				case Renderable.KEYFRAMES: {
					AnimationComponent anim = am.get(flyweight);
					TextureRegion frame = anim.animation.getKeyFrame(anim.stateTime, true);
					Position position = pm.get(flyweight);

					float w = frame.getRegionWidth();
					float scaleX = anim.flipHorz ? -1 : 1;
					float x = position.current.x;

					batch.draw(frame, x, position.current.y, w/2, 0, w, frame.getRegionHeight(), scaleX, 1, 0);

					break;
				}
				case Renderable.TEXTURE: {
					break;
				}
			}
		}
		
		batch.end();
	}

}
