package net.namekdev.mgame.components;

import com.artemis.PooledComponent;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class AnimationComponent extends PooledComponent {
	public Animation<TextureRegion> animation;
	public float stateTime;
	public boolean flipHorz;


	@Override
	protected void reset() {
		animation = null;
		stateTime = 0;
		flipHorz = false;
	}
}
