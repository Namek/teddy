package net.namekdev.mgame.builders;

import net.namekdev.mgame.components.AnimationComponent;
import net.namekdev.mgame.components.Renderable;
import net.namekdev.mgame.components.Teddy;
import net.namekdev.mgame.components.base.Dimensions;
import net.namekdev.mgame.components.base.Transform;
import net.namekdev.mgame.components.base.Velocity;
import net.namekdev.mgame.components.render.DecalComponent;
import net.namekdev.mgame.enums.MConstants;
import net.namekdev.mgame.systems.RenderSystem;

import com.artemis.BaseSystem;
import com.artemis.EntityEdit;
import com.artemis.annotations.Wire;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.decals.Decal;

@Wire
public class WorldInitSystem extends BaseSystem {
	RenderSystem renderSystem;


	@Override
	protected void processSystem() {
		renderSystem.camera.position.set(0, 1.5f, 6);
		renderSystem.camera.direction.set(0, 0, -1);

		createTeddy();

		// TODO create floor, walls, furniture, obstacles, etc.
	}

	private void createTeddy() {
		EntityEdit teddyEdit = world.createEntity().edit();
		Teddy teddy = teddyEdit.create(Teddy.class);

		Texture animStandingTexture, animRunningTexture, animWalkingTexture;
		TextureRegion[] animStandingFrames, animRunningFrames, animWalkingFrames;

		animStandingTexture = new Texture("characters/tbear_standing.png");
		animStandingFrames = TextureRegion.split(animStandingTexture, 90, 110)[0];
		teddy.animStanding = new Animation(0.1f, animStandingFrames);

		animRunningTexture = new Texture("characters/tbear_running.png");
		animRunningFrames = TextureRegion.split(animRunningTexture, 90, 110)[0];
		teddy.animRunning = new Animation(0.1f, animRunningFrames);

		animWalkingTexture = new Texture("characters/tbear_walking.png");
		animWalkingFrames = TextureRegion.split(animWalkingTexture, 90, 110)[0];
		teddy.animWalking = new Animation(0.1f, animWalkingFrames);

		teddyEdit.create(AnimationComponent.class);
		teddyEdit.create(Renderable.class).type = Renderable.DECAL;
		teddyEdit.create(Transform.class).xyz(0.5f, 0.5f, -0.2f);
		teddyEdit.create(Velocity.class);
		setupDecal(teddyEdit, animStandingFrames[0]);
		renderSystem.registerToDecalRenderer(teddyEdit.getEntity());

		setPassive(true);
	}

	private void setupDecal(EntityEdit edit, TextureRegion texture) {
		float scale = MConstants.DecalScaleToImageSize;
		float width = texture.getRegionWidth() * scale;
		float height = texture.getRegionHeight() * scale;

		Decal decal = Decal.newDecal(width, height, texture, true);
		edit.create(DecalComponent.class).decal = decal;
		edit.create(Dimensions.class).set(width, height, MConstants.DecalDimensionDepth);
	}
}
