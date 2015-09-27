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
import org.ode4j.ode.DContactGeomBuffer;
import org.ode4j.ode.DGeom.DNearCallback;
import org.ode4j.ode.DGeom;
import org.ode4j.ode.DJoint;
import org.ode4j.ode.DJointGroup;
import org.ode4j.ode.DSpace;
import org.ode4j.ode.DWorld;
import org.ode4j.ode.OdeConstants;
import org.ode4j.ode.OdeHelper;

import com.artemis.Aspect;
import com.artemis.EntityEdit;
import com.artemis.EntitySystem;
import com.artemis.annotations.Wire;
import com.artemis.utils.IntBag;

@Wire
public class PhysicsSystem extends EntitySystem {
	M<Physical> mPhysical;
	M<Transform> mTransform;

	TimeSystem timeSystem;

	public final CollisionGroupsRelations relations = new CollisionGroupsRelations();

	public DWorld physics;
	public DSpace space;
	DJointGroup contactGroup;

	public int fps = 60;


	public PhysicsSystem() {
		super(Aspect.all(Physical.class));
	}

	@Override
	public void initialize() {
		OdeHelper.initODE2(0);
		physics = OdeHelper.createWorld();
		physics.setGravity(0, -9.81, 0);
		space = OdeHelper.createHashSpace(null);
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
	protected void processSystem() {
		float deltaTime = 1/(float)fps;

		///
		// TODO optimize: by #relations use this:
		// "dSpaceCollide2 determines which geoms from one space may potentially intersect with geoms from another space, and calls a callback function with each candidate pair."
		///

		space.collide(null, onCollisionCallback);
		physics.step(deltaTime);
		contactGroup.empty();

		IntBag ids = subscription.getEntities();
		for (int i = 0, n = ids.size(); i < n; ++i) {
			int entityId = ids.get(i);

			Physical physical = mPhysical.get(entityId);
			Transform transform = mTransform.get(entityId);

			transform.xyz(physical.body.getPosition());
		}
	}

	public Physical initEntity(EntityEdit edit) {
		Physical physical = edit.create(Physical.class);
		physical.body = OdeHelper.createBody(physics);

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
			int n = OdeHelper.collide(o1, o2, N, contacts.getGeomBuffer());

			for (int i = 0; i < n; ++i) {
				DContact contact = contacts.get(i);
				contact.surface.mode = OdeConstants.dContactSoftCFM | OdeConstants.dContactApprox1;
				contact.surface.mu = 0.5;
				contact.surface.soft_cfm = 0;
				DJoint c = OdeHelper.createContactJoint(physics, contactGroup, contact);
				c.attach(contact.geom.g1.getBody(), contact.geom.g2.getBody());
			}
		}
	};
}
