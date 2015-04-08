package net.namekdev.mgame.scene;

import com.badlogic.gdx.Gdx;
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
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.graphics.g3d.utils.TextureDescriptor;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.uwsoft.editor.renderer.Overlap2DStage;

public class GameStage extends Overlap2DStage {
	ModelBatch modelBatch;
	Environment environment;
	
	Model floorModel, wallModel;
	Array<ModelInstance> modelInstances = new Array<ModelInstance>();
	
	Camera cam;
	
	public GameStage(ModelBatch modelBatch) {
		initSceneLoader();
		sceneLoader.loadScene("MainScene");
		addActor(sceneLoader.getRoot());
		
		// 3d stuff
		this.modelBatch = modelBatch;
		cam = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		
		
		environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, 0, -0.7f, -0.4f));
		
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
		modelInstances.add(floor);
		
		// create background wall
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
		float x = -10, h = 30, w = 10, disp = 0.5f;
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
		modelInstances.add(wall);
	}

	@Override
	public void dispose() {
		Disposable[] disposables = new Disposable[] {
			modelBatch, floorModel, wallModel
		};
		
		for (Disposable d : disposables) {
			d.dispose();
		}
		
		super.dispose();
	}

	@Override
	public void draw() {
		cam.position.set(0,6.61f,10f);
		cam.direction.set(0,0,-1);
		cam.far = 300;
		cam.near = 0.1f;
		cam.update();
	
		modelBatch.begin(cam);
		modelBatch.render(modelInstances, environment);
		modelBatch.end();
		
		super.draw();
	}
	
	
}
