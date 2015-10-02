package net.namekdev.mgame.systems;

import net.mostlyoriginal.api.plugin.extendedcomponentmapper.M;
import net.namekdev.mgame.components.Physical;
import net.namekdev.mgame.components.base.Dimensions;
import net.namekdev.mgame.components.base.Transform;
import net.namekdev.mgame.systems.base.TimeSystem;
import net.namekdev.mgame.systems.base.collision.CollisionGroupsRelations;

import org.ode4j.ode.DBox;
import org.ode4j.ode.DContact;
import org.ode4j.ode.DContactBuffer;
import org.ode4j.ode.DGeom;
import org.ode4j.ode.DGeom.DNearCallback;
import org.ode4j.ode.DJoint;
import org.ode4j.ode.DJointGroup;
import org.ode4j.ode.DSpace;
import org.ode4j.ode.DWorld;
import org.ode4j.ode.OdeConstants;
import org.ode4j.ode.OdeHelper;

import com.artemis.Aspect;
import com.artemis.Entity;
import com.artemis.EntityEdit;
import com.artemis.annotations.Wire;
import com.artemis.systems.EntityProcessingSystem;

@Wire
public class PhysicsSystem extends EntityProcessingSystem {
	M<Physical> mPhysical;
	M<Transform> mTransform;

	TimeSystem timeSystem;

	public final CollisionGroupsRelations relations = new CollisionGroupsRelations();
	public int fps = 60;

	public DWorld physics;
	public DSpace space;
	DJointGroup contactGroup;


	public PhysicsSystem() {
		super(Aspect.all(Physical.class));
	}

	@Override
	public void initialize() {
		OdeHelper.initODE2(0);
		physics = OdeHelper.createWorld();
		physics.setGravity(0, -9.81, 0);
		physics.setLinearDamping(0.01);
		space = OdeHelper.createSimpleSpace(null);
		contactGroup = OdeHelper.createJointGroup();
	}

	@Override
	protected void dispose() {
		contactGroup.destroy();
		space.destroy();
		physics.destroy();
		physics = null;
		OdeHelper.closeODE();
	}

	@Override
	protected void begin() {
		float deltaTime = 1/(float)fps;

		// TODO fixed physics update: update only one frame ahead of graphics

		///
		// TODO optimize: by #relations use this:
		// "dSpaceCollide2 determines which geoms from one space may potentially intersect with geoms from another space, and calls a callback function with each candidate pair."
		///

		space.collide(null, onCollisionCallback);
		physics.step(deltaTime);
		contactGroup.empty();
	}

	@Override
	protected void process(Entity e) {
		Physical physical = mPhysical.get(e);
		Transform transform = mTransform.get(e);

		transform.xyz(physical.body.getPosition());
	}

	public Physical initEntity(EntityEdit edit) {
		Physical physical = edit.create(Physical.class);
		physical.body = OdeHelper.createBody(physics);
		physical.body.setData(physical);

		return physical;
	}

	public Physical initEntity(EntityEdit edit, Dimensions dims) {
		Physical physical = initEntity(edit);
		DBox box = OdeHelper.createBox(dims.getWidth(), dims.getHeight(), dims.getDepth());
		box.setBody(physical.body);

		addToSpace(box);

		return physical;
	}

	public void addToSpace(DGeom geom) {
		space.add(geom);
	}


	private DNearCallback onCollisionCallback = new DNearCallback() {
		final int N = 100;
		DContactBuffer contacts = new DContactBuffer(N);

		@Override
		public void call(Object data, DGeom o1, DGeom o2) {
			long g1 = ((Physical) o1.getBody().getData()).collisionGroups;
			long g2 = ((Physical) o2.getBody().getData()).collisionGroups;

			if (!relations.anyRelationExists(g1, g2)) {
				return;
			}

			int n = OdeHelper.collide(o1, o2, N, contacts.getGeomBuffer());

			for (int i = 0; i < n; ++i) {
				DContact contact = contacts.get(i);
				contact.surface.mode = OdeConstants.dContactBounce;
				contact.surface.mu = 1;
				contact.surface.bounce = 0.3;
				contact.surface.bounce_vel = 6;
				DJoint c = OdeHelper.createContactJoint(physics, contactGroup, contact);
				c.attach(contact.geom.g1.getBody(), contact.geom.g2.getBody());
			}
		}
	};
}
