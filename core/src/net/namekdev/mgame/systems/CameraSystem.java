package net.namekdev.mgame.systems;

import net.namekdev.mgame.components.CameraPOI;
import net.namekdev.mgame.components.base.Transform;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.EntitySystem;
import com.artemis.annotations.Wire;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;

@Wire
public class CameraSystem extends EntitySystem {
	ComponentMapper<CameraPOI> mCameraCenter;
	ComponentMapper<Transform> mTransform;

	RenderSystem renderSystem;

	private final Vector3 tmpPos = new Vector3();

	private static final float minZ = -12;
	private static final float maxZ = -1f;


	public CameraSystem() {
		super(Aspect.all(CameraPOI.class, Transform.class));
	}

	@Override
	protected void initialize() {
		renderSystem.camera.position.set(0, 5, 6);
		renderSystem.camera.direction.set(0, 0, -1);
	}

	@Override
	protected void processSystem() {
		IntBag entities = subscription.getEntities();
		int n = entities.size();

		tmpPos.set(0, 0, 0);

		for (int i = 0; i < n; ++i) {
			int entityId = entities.get(i);
			CameraPOI cameraCenter = mCameraCenter.get(entityId);
			Transform transform = mTransform.get(entityId);

			tmpPos.add(transform.currentPos);
		}

		tmpPos.scl(1f / (float)n);

		renderSystem.camera.position.x = tmpPos.x;
		renderSystem.camera.position.z = MathUtils.clamp(tmpPos.z, minZ, maxZ)+7;
	}

}
