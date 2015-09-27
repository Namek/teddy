package net.namekdev.mgame.systems.render;

import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.RenderableProvider;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.decals.CameraGroupStrategy;
import com.badlogic.gdx.graphics.g3d.decals.DecalBatch;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.shaders.DefaultShader.Config;

import net.namekdev.mgame.components.Renderable;
import net.namekdev.mgame.components.base.Dimensions;
import net.namekdev.mgame.components.base.Transform;
import net.namekdev.mgame.components.render.DecalComponent;
import net.namekdev.mgame.components.render.ModelSetComponent;
import net.namekdev.mgame.components.render.Shaders;
import net.namekdev.mgame.systems.render.renderers.DecalRenderer;
import net.namekdev.mgame.systems.render.renderers.SpriteRenderer;

@Wire(injectInherited=true)
public class BaseRenderSystem extends RenderBatchingSystem {
	ComponentMapper<DecalComponent> mDecal;
	ComponentMapper<Dimensions> mDimensions;
	ComponentMapper<ModelSetComponent> mModelSet;
	ComponentMapper<Renderable> mRenderable;
	ComponentMapper<Shaders> mShaders;

	public DecalBatch decalBatch;
	public SpriteBatch spriteBatch;
	public ModelBatch modelBatch;
	public PerspectiveCamera camera = new PerspectiveCamera(80, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

	DecalRenderer decalRenderer;
	SpriteRenderer spriteRenderer;
	ModelRenderer modelRenderer;

	public Environment environment;
	DirectionalLight directionalLight;

	@Override
	protected void initialize() {
		super.initialize();

		decalBatch = new DecalBatch(new CameraGroupStrategy(camera));
		spriteBatch = new SpriteBatch();

		decalRenderer = new DecalRenderer(this, world, decalBatch);
		spriteRenderer = new SpriteRenderer(world, spriteBatch);
		modelRenderer = new ModelRenderer();

		camera.near = 0.1f;
		camera.far = 300f;

		Config shaderConfig = new Config();
		shaderConfig.defaultCullFace = 0;

		modelBatch = new ModelBatch();

		environment = new Environment();
		environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
		environment.set(new ColorAttribute(ColorAttribute.Fog, 0, 0, 0, 1f));
		directionalLight = new DirectionalLight().set(1f, 1f, 1f, -1f, -0.8f, -0.2f);
		environment.add(directionalLight);
	}

	public void registerToDecalRenderer(Entity entity) {
		registerAgent(entity, decalRenderer);
	}

	public void unregisterToDecalRenderer(Entity entity) {
		unregisterAgent(entity, decalRenderer);
	}

	public void registerToSpriteRenderer(Entity entity) {
		registerAgent(entity, spriteRenderer);
	}

	public void unregisterToSpriteRenderer(Entity entity) {
		unregisterAgent(entity, spriteRenderer);
	}

	public void registerToModelRenderer(Entity entity) {
		registerAgent(entity, modelRenderer);
	}

	public void unregisterToModelRenderer(Entity entity) {
		unregisterAgent(entity, modelRenderer);
	}

	@Override
	protected void processSystem() {
		camera.update();
		super.processSystem();
//		shaderProvider.updateAll(world.getDelta());
	}


	class ModelRenderer implements EntityProcessAgent {
		@Override
		public void begin() {
			modelBatch.begin(camera);
		}

		@Override
		public void process(Entity e) {
			ModelSetComponent models = mModelSet.get(e);
//			Transform transform = mTransform.get(e);

			if (models != null) {
				Shaders shaders = mShaders.get(e);

				for (int i = 0; i < models.instances.length; ++i) {
					RenderableProvider model = models.instances[i];

					if (shaders == null) {
						modelBatch.render(model, environment);
					}
					else {
						if (shaders.useDefaultShader) {
							modelBatch.render(model, environment);
						}

						for (int j = 0, n = shaders.shaders.length; j < n; ++j) {
							modelBatch.render(model, environment, shaders.shaders[j]);
						}
					}
				}
			}
		}

		@Override
		public void end() {
			modelBatch.end();
		}

		@Override
		public int getType() {
			return Renderable.MODEL;
		}
	}
}
