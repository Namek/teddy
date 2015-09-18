package net.namekdev.mgame.systems;

import net.namekdev.mgame.components.AnimationComponent;
import net.namekdev.mgame.components.Position;
import net.namekdev.mgame.components.Renderable;
import net.namekdev.mgame.scene.GameStage;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.EntitySystem;
import com.artemis.annotations.Wire;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

@Wire
public class RenderSystem extends EntitySystem {
	ComponentMapper<AnimationComponent> am;
	ComponentMapper<Position> pm;
	ComponentMapper<Renderable> rm;

	SpriteBatch batch;
	ModelBatch batch3d;
	Camera camera2d, camera3d;
	GameStage stage;

	public RenderSystem() {
		super(Aspect.all(Renderable.class));
	}

	@Override
	protected void initialize() {
		batch = new SpriteBatch();
		batch3d = new ModelBatch();
		camera2d = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		camera3d = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

		stage = new GameStage(batch3d, camera3d);
	}

	@Override
	protected void processSystem() {
		final IntBag actives = subscription.getEntities();

		Gdx.gl.glClearColor(0.1f, 0, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

		camera3d.position.x = camera2d.position.x*0.05f;
		camera3d.update();

		// draw floor, walls, furniture and so on
		stage.act();
		stage.draw();

		// draw teddy bears
		camera2d.update();
		batch.setProjectionMatrix(camera2d.combined);
		batch.begin();

		for (int i = 0, n = actives.size(); i < n; ++i) {
			int entityId = actives.get(i);

			Renderable renderable = rm.get(entityId);

			switch (renderable.type) {
				case Renderable.KEYFRAMES: {
					AnimationComponent anim = am.get(entityId);
					TextureRegion frame = anim.animation.getKeyFrame(anim.stateTime, true);
					Position position = pm.get(entityId);

					float hw = Gdx.graphics.getWidth()/2;
					float hh = Gdx.graphics.getHeight()/2;
					float w = frame.getRegionWidth();
					float scaleX = anim.flipHorz ? -1 : 1;
					float x = position.current.x;

					batch.draw(frame, x*2-hw, position.current.y-hh, w/2, 0, w, frame.getRegionHeight(), scaleX, 1, 0);

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
