package net.namekdev.mgame.systems.base.physics;

import net.mostlyoriginal.api.plugin.extendedcomponentmapper.M;
import net.namekdev.mgame.components.Physical;
import net.namekdev.mgame.systems.RenderSystem;

import org.ode4j.math.DVector3C;
import org.ode4j.ode.DAABBC;
import org.ode4j.ode.DGeom;

import com.artemis.Aspect;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.graphics.VertexAttributes.Usage;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;

@Wire
public class PhysicsDebugSystem extends EntityProcessingSystem {
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
	protected void begin() {
		renderSystem.modelBatch.begin(renderSystem.camera);
	}

	@Override
	protected void process(Entity e) {
		Physical physical = mPhysical.get(e);

		DVector3C pos = physical.body.getPosition();
		DGeom geom = physical.body.getFirstGeom();

		while (geom != null) {
			renderGeom(pos, geom);
			geom = physical.body.getNextGeom(geom);
		}
	}

	private void renderGeom(DVector3C pos, DGeom geom) {
		DAABBC bbox = geom.getAABB();

		Model model = mb.createBox(
			(float)bbox.len0(), (float)bbox.len1(), (float)bbox.len2(),
			material,
			Usage.Position | Usage.Normal
		);

		ModelInstance inst = new ModelInstance(model);
		inst.transform.setTranslation(
			(float)pos.get0(),
			(float)pos.get1(),
			(float)pos.get2()
		);

		renderSystem.modelBatch.render(inst, renderSystem.environment);
	}

	@Override
	protected void end() {
		renderSystem.modelBatch.end();
	}

}
