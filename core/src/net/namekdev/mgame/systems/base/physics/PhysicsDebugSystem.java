package net.namekdev.mgame.systems.base.physics;

import static net.namekdev.mgame.utils.MathUtils.*;

import net.mostlyoriginal.api.plugin.extendedcomponentmapper.M;
import net.namekdev.mgame.components.Physical;
import net.namekdev.mgame.systems.RenderSystem;

import org.ode4j.math.DMatrix3C;
import org.ode4j.math.DVector3C;
import org.ode4j.ode.DAABBC;
import org.ode4j.ode.DBox;
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
	ModelInstance box;
	Material material;
	ColorAttribute color;


	public PhysicsDebugSystem() {
		super(Aspect.all(Physical.class));
	}

	@Override
	public void initialize() {
		mb = new ModelBuilder();
		color = ColorAttribute.createDiffuse(0, 1, 0, 1f);
		material = new Material(
			color,
			new BlendingAttribute(0.5f)
		);

		Model model = mb.createBox(1, 1, 1, material, Usage.Position);
		box = new ModelInstance(model);
	}

	@Override
	protected void begin() {
		renderSystem.modelBatch.begin(renderSystem.camera);
	}

	@Override
	protected void process(Entity e) {
		Physical physical = mPhysical.get(e);

		DVector3C pos = physical.body.getPosition();
		DMatrix3C rot = physical.body.getRotation();
		DGeom geom = physical.body.getFirstGeom();

		while (geom != null) {
			renderGeom(pos, rot, geom);
			geom = physical.body.getNextGeom(geom);
		}
	}

	private void renderGeom(DVector3C pos, DMatrix3C rot, DGeom geom) {
		DAABBC bbox = geom.getAABB();

		final DBox gbox = (DBox) geom;
		DVector3C scale = gbox.getLengths();

		// set rotation
		box.transform.idt();
		copyMat(rot, box.transform);

		box.transform.scale((float)scale.get0(), (float)scale.get1(), (float)scale.get2());
		box.transform.setTranslation(
			(float)pos.get0(),
			(float)pos.get1(),
			(float)pos.get2()+0.05f
		);

		color.color.r = (float) Math.random();
		color.color.g = (float) Math.random();
		color.color.b = 1f;
		material.set(color);
		renderSystem.modelBatch.render(box, renderSystem.environment);
		renderSystem.modelBatch.flush();
	}

	@Override
	protected void end() {
		renderSystem.modelBatch.end();
	}

}
