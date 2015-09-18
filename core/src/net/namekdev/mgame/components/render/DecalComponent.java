package net.namekdev.mgame.components.render;

import net.namekdev.mgame.components.base.Transform;

import com.artemis.PooledComponent;
import com.badlogic.gdx.graphics.g3d.decals.Decal;

public class DecalComponent extends PooledComponent {
	public Decal decal;
	
	/** Ignore {@link Transform#orientation} by looking at camera. */
	public boolean lookAtCamera = true;
	
	@Override
	protected void reset() {
		decal = null;
		lookAtCamera = true;
	}
}
