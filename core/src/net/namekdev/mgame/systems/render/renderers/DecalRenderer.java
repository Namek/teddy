package net.namekdev.mgame.systems.render.renderers;

import net.namekdev.mgame.components.Renderable;
import net.namekdev.mgame.components.base.Transform;
import net.namekdev.mgame.components.render.DecalComponent;
import net.namekdev.mgame.systems.render.RenderBatchingSystem.EntityProcessAgent;
import net.namekdev.mgame.systems.render.BaseRenderSystem;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.World;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;

public class DecalRenderer implements EntityProcessAgent  {
	private DecalBatch batch;
	private BaseRenderSystem renderSystem;
	private ComponentMapper<DecalComponent> mDecal;
	private ComponentMapper<Transform> mTransform;

	public DecalRenderer(BaseRenderSystem renderSystem, World world, DecalBatch batch) {
		this.renderSystem = renderSystem;
		this.batch = batch;
		mDecal = world.getMapper(DecalComponent.class);
		mTransform = world.getMapper(Transform.class);
	}

	@Override
	public void begin() {
	}

	@Override
	public void process(Entity e) {
		Camera camera = renderSystem.camera;
		DecalComponent decalComponent = mDecal.get(e);
		Decal decal = decalComponent.decal;
		Transform transform = mTransform.get(e);

		decal.setPosition(transform.currentPos);

		if (decalComponent.lookAtCamera) {
			decal.lookAt(camera.position, camera.up);
		}
		else {
			decal.lookAt(transform.direction.add(camera.position), transform.up);
			//throw new UnsupportedOperationException("Todo: set up vector based on orientation");
		}
		batch.add(decal);
	}

	@Override
	public void end() {
		batch.flush();
	}

	@Override
	public int getType() {
		return Renderable.DECAL;
	}

}
