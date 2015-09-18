package net.namekdev.mgame.systems;

import static net.namekdev.mgame.components.Teddy.LEFT;
import static net.namekdev.mgame.components.Teddy.NONE;
import static net.namekdev.mgame.components.Teddy.RIGHT;
import static net.namekdev.mgame.components.Teddy.RUN;
import static net.namekdev.mgame.components.Teddy.STAND;
import static net.namekdev.mgame.components.Teddy.WALK;
import net.namekdev.mgame.components.AnimationComponent;
import net.namekdev.mgame.components.Teddy;
import net.namekdev.mgame.components.base.Transform;
import net.namekdev.mgame.components.base.Velocity;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.EntitySystem;
import com.artemis.annotations.Wire;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;

@Wire
public class TeddyStateSystem extends EntitySystem {
	static final float WALK_SPEED = 250f;
	static final float RUN_SPEED = 550f;

	ComponentMapper<AnimationComponent> am;
	ComponentMapper<Transform> mTransform;
	ComponentMapper<Velocity> mVelocity;
	ComponentMapper<Teddy> tm;


	public TeddyStateSystem() {
		super(Aspect.all(Teddy.class));
	}

	@Override
	protected void initialize() {

	}

	@Override
	protected void processSystem() {
		int teddyEntityId = subscription.getEntities().get(0);
		Teddy state = tm.get(teddyEntityId);
		AnimationComponent anim = am.get(teddyEntityId);
		Transform transform = mTransform.get(teddyEntityId);
		Velocity velocity = mVelocity.get(teddyEntityId);

		boolean hasChangedAnimDirection = false;
		boolean canRun = Gdx.input.isKeyPressed(Keys.SHIFT_LEFT);

		velocity.frictionOn = false;

		if (Gdx.input.isKeyPressed(Keys.RIGHT) || Gdx.input.isKeyPressed(Keys.D)) {
			hasChangedAnimDirection = state.lookDir != RIGHT;
			state.lookDir = state.moveDir = RIGHT;
			state.animState = canRun ? RUN : WALK;
			anim.animation = canRun ? state.animRunning : state.animWalking;
			anim.flipHorz = false;
			velocity.acceleration.set(1, 0, 0).scl(canRun ? RUN_SPEED : WALK_SPEED);
		}
		else if (Gdx.input.isKeyPressed(Keys.LEFT) || Gdx.input.isKeyPressed(Keys.A)) {
			hasChangedAnimDirection = state.lookDir != LEFT;
			state.lookDir = state.moveDir = LEFT;
			state.animState = canRun ? RUN : WALK;
			anim.animation = canRun ? state.animRunning : state.animWalking;
			anim.flipHorz = true;
			velocity.acceleration.set(-1, 0, 0).scl(canRun ? RUN_SPEED : WALK_SPEED);
		}
		// TODO Keys.UP, Keys.DOWN
		else {
			state.moveDir = NONE;
			state.animState = STAND;
			anim.animation = state.animStanding;
			velocity.acceleration.set(0, 0, 0);
			velocity.frictionOn = true;
		}


		if (hasChangedAnimDirection) {
			anim.stateTime = 0;
		}
		else {
			anim.stateTime += world.delta;
		}


		// TODO CameraCenter component
//		renderer.camera2d.position.x = pos.current.x;
	}

}
