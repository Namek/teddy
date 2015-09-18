package net.namekdev.mgame.components;

import com.artemis.PooledComponent;
import com.badlogic.gdx.graphics.g2d.Animation;

public class AnimationComponent extends PooledComponent {
	public Animation animation;
	public float stateTime;
	public boolean flipHorz;


	@Override
	protected void reset() {
		animation = null;
		stateTime = 0;
		flipHorz = false;
	}
}
