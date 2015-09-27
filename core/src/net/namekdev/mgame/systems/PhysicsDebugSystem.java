package net.namekdev.mgame.systems;

import org.ode4j.math.DVector3C;
import org.ode4j.ode.DAABB;
import org.ode4j.ode.DAABBC;
import org.ode4j.ode.DGeom;

import net.mostlyoriginal.api.plugin.extendedcomponentmapper.M;
import net.namekdev.mgame.components.Physical;

import com.artemis.Aspect.Builder;
import com.artemis.Aspect;
import com.artemis.Entity;
import com.artemis.EntitySystem;
import com.artemis.annotations.Wire;
import com.artemis.systems.EntityProcessingSystem;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;

@Wire
public class PhysicsDebugSystem extends EntitySystem {
	M<Physical> mPhysical;

	PhysicsSystem physicsSystem;
	RenderSystem renderSystem;

	ModelBuilder mb;
	Material material;


	public PhysicsDebugSystem() {
		super(Aspect.all(Physical.class));
	}

	@Override
	public void initialize() {
		mb = new ModelBuilder();
		material = new Material(
			ColorAttribute.createDiffuse(0, 1, 0, 1f),
			new BlendingAttribute(0.5f)
		);
	}

	@Override
	protected void processSystem() {
		renderSystem.modelBatch.begin(renderSystem.camera);

		IntBag ids = subscription.getEntities();
		for (int i = 0, n = ids.size(); i < n; ++i) {
			int id = ids.get(i);

			Physical physical = mPhysical.get(id);
			DVector3C pos = physical.body.getPosition();
			DGeom geom = physical.body.getFirstGeom();
			DAABBC bbox = geom.getAABB();

			Model model = mb.createBox(
				(float)bbox.len0(), (float)bbox.len1(), (float)bbox.len2(),
				material,
				Usage.Position | Usage.Normal
			);

			ModelInstance inst = new ModelInstance(model);
			inst.transform.setTranslation(
				(float)pos.get0() + (float)bbox.len0()/2,
				(float)pos.get1() + (float)bbox.len1()/2,
				(float)pos.get2() + (float)bbox.len2()/2
			);

			renderSystem.modelBatch.render(inst, renderSystem.environment);
		}

		renderSystem.modelBatch.end();
	}


}
