package net.namekdev.mgame.systems;

import com.artemis.annotations.Wire;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

@Wire(injectInherited=true)
public class RenderSystem extends net.namekdev.mgame.systems.render.BaseRenderSystem {
	public RenderSystem() {
	}

	@Override
	protected void processSystem() {
		Gdx.gl.glClearColor(0.1f, 0, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
		Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);

		super.processSystem();
	}

}
