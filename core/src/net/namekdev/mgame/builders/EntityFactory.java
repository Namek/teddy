package net.namekdev.mgame.builders;

import net.namekdev.mgame.components.AnimationComponent;
import net.namekdev.mgame.components.CameraPOI;
import net.namekdev.mgame.components.Carryable;
import net.namekdev.mgame.components.Renderable;
import net.namekdev.mgame.components.Teddy;
import net.namekdev.mgame.components.base.Dimensions;
import net.namekdev.mgame.components.base.Force;
import net.namekdev.mgame.components.base.Transform;
import net.namekdev.mgame.components.base.Velocity;
import net.namekdev.mgame.components.render.DecalComponent;
import net.namekdev.mgame.components.render.ModelSetComponent;
import net.namekdev.mgame.enums.Assets;
import net.namekdev.mgame.enums.CollisionGroups;
import net.namekdev.mgame.enums.MConstants;
import net.namekdev.mgame.enums.RenderLayers;
import net.namekdev.mgame.systems.RenderSystem;
import net.namekdev.mgame.systems.base.collision.Collider;

import com.artemis.BaseSystem;
import com.artemis.Entity;
import com.artemis.EntityEdit;
import com.artemis.annotations.Wire;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.ModelLoader;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.FloatAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.decals.Decal;
import com.badlogic.gdx.graphics.g3d.loader.ObjLoader;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.g3d.utils.TextureDescriptor;
import com.badlogic.gdx.math.Vector3;

@Wire
public class EntityFactory extends BaseSystem {
	RenderSystem renderSystem;
	AssetManager assetManager;

	TextureAtlas toysAtlas;


	@Override
	protected void processSystem() {
		assetManager = new AssetManager();
		toysAtlas = loadAsset(Assets.Textures.Toys, TextureAtlas.class);

		setPassive(true);
	}

	<T> T loadAsset(String filename, Class<T> type) {
		assetManager.load(filename, type);
		assetManager.finishLoading();
		return assetManager.get(filename);
	}

	public void createTeddy(Vector3 position) {
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
		teddyEdit.create(Transform.class).xyz(position);
		teddyEdit.create(Velocity.class)
			.setup(MConstants.Teddy.MaxWalkSpeed, MConstants.Teddy.Friction)
			.maxExtSpeed = MConstants.Teddy.MaxJumpSpeed;
		teddyEdit.create(Force.class);
		teddyEdit.create(CameraPOI.class);
		teddyEdit.create(Collider.class).groups(CollisionGroups.TEDDY);

		setupDecal(teddyEdit, animStandingFrames[0]);
	}

	public Entity createToy(String name, Vector3 position, boolean isCarryable) {
		TextureRegion texture = toysAtlas.findRegion(name);
		EntityEdit edit = world.createEntity().edit();
		edit.create(Transform.class).xyz(position);
		edit.create(Collider.class).groups(CollisionGroups.TOYS);

		if (isCarryable) {
			edit.create(Carryable.class);
		}

		setupDecal(edit, texture);

		return edit.getEntity();
	}

	public void createRoom() {
		Model floorModel, wallModel, tableModel;

		// create floor
        Texture floorTexture = new Texture("enviro/floor2.png");
        ModelBuilder mb = new ModelBuilder();
        TextureAttribute textureAttr = TextureAttribute.createDiffuse(floorTexture);
        textureAttr.textureDescription.uWrap = TextureWrap.Repeat;
        textureAttr.textureDescription.vWrap = TextureWrap.Repeat;
        textureAttr.textureDescription.magFilter = TextureFilter.Linear;
        textureAttr.textureDescription.minFilter = TextureFilter.Linear;
        textureAttr.scaleU = 3;
        textureAttr.scaleV = 30;

        Material floorMat = new Material(
    		textureAttr
        );

		floorModel = mb.createRect(
			-10, 0, 0,
			-10, 0, 20,
			100, 0, 20,
			100, 0, 0,
			0, 1, 0,
			floorMat, Usage.Position | Usage.Normal | Usage.TextureCoordinates
		);
		floorModel.manageDisposable(floorTexture);

		ModelInstance floor = new ModelInstance(floorModel);
		floor.transform.translate(0, 0, -20);


        // background wall
		mb = new ModelBuilder();
		mb.begin();
		Texture wallTexture1 = new Texture("enviro/wall1.png");
		Texture wallTexture2 = new Texture("enviro/wall2.png");
		TextureAttribute[] wallTexturesAttrs = new TextureAttribute[] {
			TextureAttribute.createDiffuse(wallTexture1),
			TextureAttribute.createDiffuse(wallTexture2)
		};
		for (int i = 0; i < wallTexturesAttrs.length; ++i) {
			textureAttr = wallTexturesAttrs[i];
			textureAttr.scaleU = 5f;
			textureAttr.scaleV = 1f;
			TextureDescriptor<Texture> desc = textureAttr.textureDescription;
			desc.magFilter = TextureFilter.Linear;
			desc.minFilter = TextureFilter.Linear;
			desc.uWrap = TextureWrap.Repeat;
			desc.vWrap = TextureWrap.Repeat;
		}

		Material[] wallTextureMats = new Material[wallTexturesAttrs.length];
		for (int i = 0; i < wallTextureMats.length; ++i) {
			wallTextureMats[i] = new Material(
				wallTexturesAttrs[i],
	    		ColorAttribute.createSpecular(1,1,1,1), FloatAttribute.createShininess(8f)
	    	);
			mb.manage(wallTexturesAttrs[i].textureDescription.texture);
		}
		float x = -10, h = 30, w = 10, disp = 0f;
		for (int i = 0; i < 11; ++i) {
			MeshPartBuilder mpb = mb.part(
				"wall" + i, GL20.GL_TRIANGLES, Usage.Position | Usage.Normal | Usage.TextureCoordinates, wallTextureMats[i%2]
			);
			int flip = i%2 == 0 ? 1 : -1;
			mpb.rect(
				x, h, -10 + flip*disp + disp,
				x, 0, -10 + flip*disp + disp,
				x+w, 0, -10 - flip*disp + disp,
				x+w, h, -10 - flip*disp + disp,
				0, 0, 1
			);

			x += w;
		}
		wallModel = mb.end();
		ModelInstance wall = new ModelInstance(wallModel);
		wall.transform.translate(0, 0, -10);


		// table
		ModelLoader loader = new ObjLoader();
		tableModel = loader.loadModel(Gdx.files.internal("furniture/table.obj"));
		ModelInstance table = new ModelInstance(tableModel);
		table.transform.translate(10, 0, -4);


		// now create entities
		EntityEdit edit = world.createEntity().edit();
		edit.create(Renderable.class).type = Renderable.MODEL;
		edit.create(ModelSetComponent.class).instances = new ModelInstance[] {
			floor, wall
		};
		renderSystem.registerToModelRenderer(edit.getEntity());

		edit = world.createEntity().edit();
		edit.create(Renderable.class).type = Renderable.MODEL;
		edit.create(ModelSetComponent.class).instances = new ModelInstance[] {
//			table //TODO correct table model file by removing wall
		};
		renderSystem.registerToModelRenderer(edit.getEntity());
	}

	public void setupDecal(EntityEdit edit, TextureRegion texture) {
		float scale = MConstants.DecalScaleToImageSize;
		float width = texture.getRegionWidth() * scale;
		float height = texture.getRegionHeight() * scale;

		Decal decal = Decal.newDecal(width, height, texture, true);
		DecalComponent decalComponent = edit.create(DecalComponent.class);
		decalComponent.decal = decal;
		decalComponent.lookAtCamera = false;

		edit.create(Dimensions.class).set(width, height, MConstants.DecalDimensionDepth);
		edit.create(Renderable.class).setup(Renderable.DECAL, RenderLayers.FRONT);

		renderSystem.registerToDecalRenderer(edit.getEntity());
	}
}
