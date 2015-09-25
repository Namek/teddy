package net.namekdev.mgame.components.render;

import net.namekdev.mgame.components.base.Transform;

import com.artemis.PooledComponent;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.math.Vector2;

public class DecalComponent extends PooledComponent {
	public Decal decal;

	/** Ignore {@link Transform#orientation} by looking at camera. */
	public boolean lookAtCamera = true;

	/** Displace rendering position by multiplying the factor with decal width and height. */
	public Vector2 posDisplacementFactor = new Vector2(0, 0.5f);


	@Override
	protected void reset() {
		decal = null;
		lookAtCamera = true;
		posDisplacementFactor.set(0, 0.5f);
	}
}
