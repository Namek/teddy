package net.namekdev.mgame.systems;

import net.namekdev.mgame.components.AnimationComponent;
import net.namekdev.mgame.components.Movement;
import net.namekdev.mgame.components.Position;
import net.namekdev.mgame.components.Teddy;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.EntitySystem;
import com.artemis.annotations.Wire;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import static net.namekdev.mgame.components.Teddy.*;

@Wire
public class TeddyStateSystem extends EntitySystem {
	static final float WALK_SPEED = 250f;
	static final float RUN_SPEED = 550f;

	ComponentMapper<AnimationComponent> am;
	ComponentMapper<Movement> mm;
	ComponentMapper<Position> pm;
	ComponentMapper<Teddy> tm;

	RenderSystemOld renderer;

	Texture animStandingTexture, animRunningTexture, animWalkingTexture;
	TextureRegion[] animStandingFrames, animRunningFrames, animWalkingFrames;
	Animation animStanding, animRunning, animWalking;


	public TeddyStateSystem() {
		super(Aspect.all(Teddy.class));
	}

	@Override
	protected void initialize() {
		animStandingTexture = new Texture("characters/tbear_standing.png");
		animStandingFrames = TextureRegion.split(animStandingTexture, 90, 110)[0];
		animStanding = new Animation(0.1f, animStandingFrames);

		animRunningTexture = new Texture("characters/tbear_running.png");
		animRunningFrames = TextureRegion.split(animRunningTexture, 90, 110)[0];
		animRunning = new Animation(0.1f, animRunningFrames);

		animWalkingTexture = new Texture("characters/tbear_walking.png");
		animWalkingFrames = TextureRegion.split(animWalkingTexture, 90, 110)[0];
		animWalking = new Animation(0.1f, animWalkingFrames);
	}

	@Override
	protected void processSystem() {
		int teddyEntityId = subscription.getEntities().get(0);
		Teddy state = tm.get(teddyEntityId);
		AnimationComponent anim = am.get(teddyEntityId);
		Movement move = mm.get(teddyEntityId);

		boolean hasChangedAnimDirection = false;
		boolean canRun = Gdx.input.isKeyPressed(Keys.SHIFT_LEFT);

		if (Gdx.input.isKeyPressed(Keys.RIGHT) || Gdx.input.isKeyPressed(Keys.D)) {
			hasChangedAnimDirection = state.lookDir != RIGHT;
			state.lookDir = state.moveDir = RIGHT;
			state.animState = canRun ? RUN : WALK;
			anim.animation = canRun ? animRunning : animWalking;
			anim.flipHorz = false;
			move.speed.set(canRun ? RUN_SPEED : WALK_SPEED, 0);
		}
		else if (Gdx.input.isKeyPressed(Keys.LEFT) || Gdx.input.isKeyPressed(Keys.A)) {
			hasChangedAnimDirection = state.lookDir != LEFT;
			state.lookDir = state.moveDir = LEFT;
			state.animState = canRun ? RUN : WALK;
			anim.animation = canRun ? animRunning : animWalking;
			anim.flipHorz = true;
			move.speed.set(canRun ? -RUN_SPEED : -WALK_SPEED, 0);
		}
		else {
			state.moveDir = NONE;
			state.animState = STAND;
			move.speed.set(0, 0);
			anim.animation = animStanding;
		}


		if (hasChangedAnimDirection) {
			anim.stateTime = 0;
		}
		else {
			anim.stateTime += world.delta;
		}

		Position pos = pm.get(teddyEntityId);
		renderer.camera2d.position.x = pos.current.x;
	}

}
