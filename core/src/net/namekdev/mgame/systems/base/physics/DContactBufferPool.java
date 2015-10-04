package net.namekdev.mgame.systems.base.physics;

import org.ode4j.ode.DContact;
import org.ode4j.ode.DContact.DSurfaceParameters;
import org.ode4j.ode.DContactBuffer;
import org.ode4j.ode.DContactGeom;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;

class DContactBufferPool {
	private Array<ContactBuffer> toBeFreed = new Array<>(ContactBuffer.class);

	private Pool<ContactBuffer> pool = new Pool<ContactBuffer>() {
		@Override
		protected ContactBuffer newObject() {
			return new ContactBuffer(bufferSize);
		}
	};

	public final int bufferSize;

	public DContactBufferPool(int bufferSize) {
		this.bufferSize = bufferSize;
	}

	public final ContactBuffer getNext() {
		ContactBuffer buffer = pool.obtain();
		toBeFreed.add(buffer);
		return buffer;
	}

	public final void freeAll() {
		for (int i = 0, n = toBeFreed.size; i < n; ++i) {
			ContactBuffer buffer = toBeFreed.items[i];
			buffer.nullify();
		}

		pool.freeAll(toBeFreed);
		toBeFreed.size = 0;
	}


	public static class ContactBuffer {
		public final DContactBuffer buffer;
		public int lastUsedContacts;

		public ContactBuffer(int bufferSize) {
			buffer = new DContactBuffer(bufferSize);
		}

		public void nullify() {
			for (int i = 0, n = lastUsedContacts; i < n; ++i) {
				final DContact c = buffer.get(i);
				c.fdir1.setZero();

				DContactGeom g = c.geom;
				g.g1 = g.g2 = null;
				g.side1 = g.side2 = 0;
				g.depth = 0;
				g.normal.setZero();
				g.pos.setZero();

				final DSurfaceParameters p = c.surface;
				p.mode = 0;
				p.mu = 0;
				p.mu2 = 0;
				p.rho = 0;
				p.rho2 = 0;
				p.rhoN = 0;
				p.bounce = 0;
				p.bounce_vel = 0;
				p.soft_erp = 0;
				p.soft_cfm = 0;
				p.motion1 = p.motion2 = p.motionN = 0;
				p.slip1 = p.slip2 = 0;
			}

			lastUsedContacts = 0;
		}
	}
}
