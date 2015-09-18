package net.namekdev.mgame.components.base;

import net.namekdev.mgame.systems.base.TimeSystem;

import com.artemis.Entity;
import com.artemis.PooledComponent;

/**
 * @author Namek
 * @see TimeSystem
 */
public class TimeUpdate extends PooledComponent {
	public Updatable updater;
	public boolean dependsOnTimeFactor;
	
	public TimeUpdate setup(Updatable updater, boolean dependsOnTimeFactor) {
		this.updater = updater;
		this.dependsOnTimeFactor = dependsOnTimeFactor;
		return this;
	}
	
	public TimeUpdate setup(Updatable updater) {
		return setup(updater, true);
	}

	@Override
	protected void reset() {
		updater = null;
		dependsOnTimeFactor = false;
	}
	

	public interface Updatable {
		void update(float deltaTime, Entity entity);
	}
}
