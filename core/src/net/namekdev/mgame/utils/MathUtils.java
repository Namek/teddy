package net.namekdev.mgame.utils;

import static com.badlogic.gdx.math.Matrix4.*;

import org.ode4j.math.DMatrix3C;
import org.ode4j.math.DVector3;
import org.ode4j.math.DVector3C;

import com.badlogic.gdx.math.Matrix4;

public class MathUtils {
	public static void limit(DVector3C vect, float maxLen) {
		DVector3 v = (DVector3) vect;
		double len = v.length();

		if (len > maxLen) {
			v.normalize();
			v.scale(maxLen);
		}
	}

	public static void copyMat(DMatrix3C from, Matrix4 to) {
		final float[] m = to.val;
		m[M00] = (float) from.get00();
		m[M01] = (float) from.get01();
		m[M02] = (float) from.get02();
		m[M10] = (float) from.get10();
		m[M11] = (float) from.get11();
		m[M12] = (float) from.get12();
		m[M20] = (float) from.get20();
		m[M21] = (float) from.get21();
		m[M22] = (float) from.get22();
	}
}
