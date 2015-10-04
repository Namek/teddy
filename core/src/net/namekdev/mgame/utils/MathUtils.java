package net.namekdev.mgame.utils;

import org.ode4j.math.DVector3;
import org.ode4j.math.DVector3C;

public class MathUtils {
	public static void limit(DVector3C vect, float maxLen) {
		DVector3 v = (DVector3) vect;
		double len = v.length();

		if (len > maxLen) {
			v.normalize();
			v.scale(maxLen);
		}
	}
}
