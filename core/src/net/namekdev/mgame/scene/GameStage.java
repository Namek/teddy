package net.namekdev.mgame.scene;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.ModelLoader;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.FloatAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.loader.ObjLoader;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.g3d.utils.TextureDescriptor;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class GameStage  {
	Camera camera3d;
	ModelBatch modelBatch;
	Environment environment;


	Array<ModelInstance> modelInstances = new Array<ModelInstance>();


	public GameStage(ModelBatch modelBatch, Camera cam3d) {

		environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, 0, -0.7f, -0.4f));

	}

//	@Override
//	public void dispose() {
//		Disposable[] disposables = new Disposable[] {
//			modelBatch, floorModel, wallModel, tableModel
//		};
//
//		for (Disposable d : disposables) {
//			d.dispose();
//		}
//
//		super.dispose();
//	}

//	@Override
//	public void draw() {
//		camera3d.update();
//		modelBatch.begin(camera3d);
//		modelBatch.render(modelInstances, environment);
//		modelBatch.end();
//
//		Camera camera = getCamera();
//		camera.position.x = camera3d.position.x*39;
//		camera.update();
//
////		super.draw();
//	}


}
