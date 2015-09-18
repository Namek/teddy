package net.namekdev.mgame.systems;

import static net.namekdev.mgame.components.Teddy.*;
import static net.namekdev.mgame.components.Teddy.NONE;
import static net.namekdev.mgame.components.Teddy.RIGHT;
import static net.namekdev.mgame.components.Teddy.RUN;
import static net.namekdev.mgame.components.Teddy.STAND;
import static net.namekdev.mgame.components.Teddy.WALK;
import net.namekdev.mgame.components.AnimationComponent;
import net.namekdev.mgame.components.Teddy;
import net.namekdev.mgame.components.base.Transform;
import net.namekdev.mgame.components.base.Velocity;
import net.namekdev.mgame.enums.MConstants;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.EntitySystem;
import com.artemis.annotations.Wire;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;

@Wire
public class TeddyStateSystem extends EntitySystem {
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

		velocity.maxSpeed = canRun ? MConstants.Teddy.MaxRunSpeed : MConstants.Teddy.MaxWalkSpeed;
		velocity.frictionOn = false;
		velocity.acceleration.set(0, 0, 0);

		if (Gdx.input.isKeyPressed(Keys.RIGHT) || Gdx.input.isKeyPressed(Keys.D)) {
			hasChangedAnimDirection = state.lookDir != RIGHT;
			state.moveHorzDir = RIGHT;
			velocity.acceleration.add(1, 0, 0);
		}
		else if (Gdx.input.isKeyPressed(Keys.LEFT) || Gdx.input.isKeyPressed(Keys.A)) {
			hasChangedAnimDirection = state.lookDir != LEFT;
			state.moveHorzDir = LEFT;
			velocity.acceleration.add(-1, 0, 0);
		}
		else {
			state.moveHorzDir = NONE;
		}

		if (Gdx.input.isKeyPressed(Keys.UP) || Gdx.input.isKeyPressed(Keys.W)) {
			hasChangedAnimDirection = state.lookDir != RIGHT;
			state.moveVertDir = UP;
			velocity.acceleration.add(0, 0, -1);
		}
		else if (Gdx.input.isKeyPressed(Keys.DOWN) || Gdx.input.isKeyPressed(Keys.S)) {
			hasChangedAnimDirection = state.lookDir != LEFT;
			state.moveVertDir = DOWN;
			velocity.acceleration.add(0, 0, 1);
		}
		else {
			state.moveVertDir = NONE;
		}

		if (state.moveHorzDir == NONE && state.moveVertDir == NONE) {
			state.animState = STAND;
			anim.animation = state.animStanding;
			velocity.acceleration.set(0, 0, 0);
			velocity.frictionOn = true;
		}
		else {
			state.animState = canRun ? RUN : WALK;
			anim.animation = canRun ? state.animRunning : state.animWalking;

			if (state.moveHorzDir == RIGHT || (state.moveHorzDir == NONE && state.moveVertDir == UP)) {
				state.lookDir = RIGHT;
			}
			else if (state.moveHorzDir == LEFT || (state.moveHorzDir == NONE && state.moveVertDir == DOWN)) {
				state.lookDir = LEFT;
			}

			if (state.lookDir == LEFT) {
				anim.flipHorz = true;
			}
			else if (state.lookDir == RIGHT) {
				anim.flipHorz = false;
			}

			velocity.acceleration.nor();
			velocity.acceleration.scl(MConstants.Teddy.Acceleration);
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
