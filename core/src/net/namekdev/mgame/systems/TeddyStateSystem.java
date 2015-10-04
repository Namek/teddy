package net.namekdev.mgame.systems;

import static net.namekdev.mgame.components.Teddy.*;
import net.mostlyoriginal.api.event.common.Subscribe;
import net.namekdev.mgame.ToyCollisionEvent;
import net.namekdev.mgame.components.AnimationComponent;
import net.namekdev.mgame.components.Physical;
import net.namekdev.mgame.components.Teddy;
import net.namekdev.mgame.components.base.Dimensions;
import net.namekdev.mgame.components.base.Force;
import net.namekdev.mgame.components.base.Transform;
import net.namekdev.mgame.components.base.Velocity;
import net.namekdev.mgame.enums.MConstants;
import net.namekdev.mgame.systems.base.AttachmentSystem;
import net.namekdev.mgame.systems.base.physics.PhysicsSystem;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;

@Wire
public class TeddyStateSystem extends EntityProcessingSystem {
	ComponentMapper<AnimationComponent> am;
	ComponentMapper<Transform> mTransform;
	ComponentMapper<Velocity> mVelocity;
	ComponentMapper<Force> mForce;
	ComponentMapper<Teddy> tm;
	ComponentMapper<Dimensions> mDimensions;
	ComponentMapper<Physical> mPhysical;

	AttachmentSystem attachmentSystem;
	PhysicsSystem physicsSystem;
	RenderSystem renderSystem;

	static final float floorHeight = 0f;

	public int teddyEntityId;
	public Entity teddyEntity;
	public Teddy state;


	public TeddyStateSystem() {
		super(Aspect.all(Teddy.class));
	}

	@Override
	public void inserted(Entity entity) {
		teddyEntityId = entity.getId();
		teddyEntity = entity;
		state = tm.get(entity);
	}


	@Override
	protected void process(Entity e) {
		AnimationComponent anim = am.get(teddyEntityId);
		Transform transform = mTransform.get(teddyEntityId);
		Velocity velocity = mVelocity.get(teddyEntityId);
		Force force = mForce.get(teddyEntityId);

		boolean hasChangedAnimDirection = false;
		boolean canRun = Gdx.input.isKeyPressed(Keys.SHIFT_LEFT);

		velocity.maxSpeed = canRun ? MConstants.Teddy.MaxRunSpeed : MConstants.Teddy.MaxWalkSpeed;
		velocity.frictionOn = false;
		force.accelForce.set(0, 0, 0);

		if (Gdx.input.isKeyPressed(Keys.RIGHT) || Gdx.input.isKeyPressed(Keys.D)) {
			hasChangedAnimDirection = state.moveHorzDir != RIGHT;
			state.moveHorzDir = RIGHT;
			force.accelForce.add(1, 0, 0);
		}
		else if (Gdx.input.isKeyPressed(Keys.LEFT) || Gdx.input.isKeyPressed(Keys.A)) {
			hasChangedAnimDirection = state.moveHorzDir != LEFT;
			state.moveHorzDir = LEFT;
			force.accelForce.add(-1, 0, 0);
		}
		else {
			state.moveHorzDir = NONE;
		}

		if (Gdx.input.isKeyPressed(Keys.UP) || Gdx.input.isKeyPressed(Keys.W)) {
			hasChangedAnimDirection = state.moveVertDir != UP;
			state.moveVertDir = UP;
			force.accelForce.add(0, 0, -1);
		}
		else if (Gdx.input.isKeyPressed(Keys.DOWN) || Gdx.input.isKeyPressed(Keys.S)) {
			hasChangedAnimDirection = state.moveVertDir != DOWN;
			state.moveVertDir = DOWN;
			force.accelForce.add(0, 0, 1);
		}
		else {
			state.moveVertDir = NONE;
		}

		if (state.moveHorzDir == NONE && state.moveVertDir == NONE) {
			state.animState = STAND;
			anim.animation = state.animStanding;
			force.accelForce.set(0, 0, 0);
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

			force.accelForce.nor();
			force.accelForce.scl(MConstants.Teddy.Acceleration);
		}

		if (Gdx.input.isKeyJustPressed(Keys.SPACE)) {
//			force.extForce.y = transform.currentPos.y > floorHeight ? 20 : 10;
			dropToy();
		}
		else {
//			force.extForce.y = transform.currentPos.y > floorHeight + 1.4f ? -60 : -1;
		}

		if (Gdx.input.isKeyJustPressed(Keys.E)) {
			dropToy();
		}


		if (hasChangedAnimDirection) {
			anim.stateTime = 0;
		}
		else if (transform.currentPos.y <= floorHeight) {
			anim.stateTime += world.delta;
		}
		else {
			anim.stateTime += world.delta*0.4f;
		}


		// ------ new physics system -------

		Physical physical = mPhysical.get(teddyEntityId);
//		physical.body.addForce(force.accelForce.x, force.accelForce.y, force.accelForce.z);
//		physical.body.addForce(force.extForce.x, force.extForce.y, force.extForce.z);


		if (Gdx.input.isKeyJustPressed(Keys.SPACE)) {
			physical.body.setForce(0, 650, 0);
		}
	}

	@Subscribe
	public void onToyCollided(ToyCollisionEvent evt) {
		if (state.carriedEntityId >= 0) {
			return;
		}

		attachToy(evt.toyId);
	}

	public void attachToy(int toyId) {
		if (state.carriedEntityId >= 0) {
			dropToy();
		}

		attachmentSystem.attach(toyId, teddyEntityId);
		state.carriedEntityId = toyId;
	}

	private void dropToy() {
		if (state.carriedEntityId >= 0) {
			int toyId = state.carriedEntityId;
			attachmentSystem.deattach(toyId);
			state.carriedEntityId = -1;
		}
	}
}
