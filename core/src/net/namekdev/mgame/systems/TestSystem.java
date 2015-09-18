package net.namekdev.mgame.systems;

import com.artemis.BaseSystem;
import com.artemis.annotations.Wire;

@Wire
public class TestSystem extends BaseSystem {
	RenderSystem renderSystem;

	@Override
	protected void processSystem() {
//		renderSystem.camera.position.x = 5;
	}

}
