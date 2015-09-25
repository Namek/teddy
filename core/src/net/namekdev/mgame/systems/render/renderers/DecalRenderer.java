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
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class DecalRenderer implements EntityProcessAgent  {
	private DecalBatch batch;
	private BaseRenderSystem renderSystem;
	private ComponentMapper<DecalComponent> mDecal;
	private ComponentMapper<Transform> mTransform;

	private final Vector3 currentPos = new Vector3();
	private final Vector3 tmpVect3 = new Vector3();


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

		Vector2 pivot = decalComponent.posDisplacementFactor;
		currentPos.set(transform.currentPos).add(
			pivot.x * decal.getWidth(),
			pivot.y * decal.getHeight(),
			0
		);
		decal.setPosition(currentPos);

		if (decalComponent.lookAtCamera) {
			decal.lookAt(camera.position, camera.up);
		}
		else {
			tmpVect3.set(camera.direction).scl(-1).add(currentPos);
			decal.lookAt(tmpVect3, camera.up);
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
