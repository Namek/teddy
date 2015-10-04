package net.namekdev.mgame.systems.base.physics;


/**
 * <p>Manages groups between all groups, supporting up to 31 groups.</p>
 * <p>Relation is always two-sided (if group A is connected to group B, then B is connected to A).</p>
 *
 * @see PhysicsSystem
 * @see CollisionConfiguration
 * @author Namek
 */
public final class CollisionGroupsRelations {
	public static final int MAX_GROUPS = 31;

	/** Maps groupIndex to groupsToCollide bitset. */
	private final long[] relations = new long[MAX_GROUPS];

	/** Maps bitset of two groups to collision configuration. */
	private final CollisionConfiguration[] collisions = new CollisionConfiguration[MAX_GROUPS*2];
	{
		for (int i = 0; i < collisions.length; ++i) {
			collisions[i] = new CollisionConfiguration();
		}
	}


	public CollisionConfiguration getCollisionConfiguration(long group1, long group2) {
		assert(group1 > 0);
		assert(group2 > 0);
		assert(Long.numberOfLeadingZeros(group1) + Long.numberOfTrailingZeros(group1) == MAX_GROUPS - 1);
		assert(Long.numberOfLeadingZeros(group2) + Long.numberOfTrailingZeros(group2) == MAX_GROUPS - 1);

		if (group2 < group1) {
			long tmp = group1;
			group1 = group2;
			group2 = tmp;
		}

		int index = bitsetToIndex(group1) | (bitsetToIndex(group2) << 32);
		return collisions[index];
	}

	/**
	 * Makes groups between all groups for each of two different sets. Relation is two-sided.
	 */
	public void connectGroups(long group, long groups) {
		assert(group > 0);
		assert(groups > 0);
		assert(Long.numberOfLeadingZeros(group) + Long.numberOfTrailingZeros(group) == MAX_GROUPS - 1);

		// add all groups to this group
		int groupIndex = bitsetToIndex(group);
		relations[groupIndex] |= groups;

		// now add this group to all other groups
		groupIndex = 0;
		while (groups > 0) {
			if ((groups & 1) == 1) {
				relations[groupIndex] |= group;
			}

			groups >>= 1;
			++groupIndex;
		}
	}

	/**
	 * Disconnect {@code groups} from {@code group}.
	 */
	public void disconnectGroups(long group, long groups) {
		assert(group > 0);
		assert(groups > 0);
		assert(Long.numberOfLeadingZeros(group) + Long.numberOfTrailingZeros(group) == MAX_GROUPS - 1);

		// turn off all groups for group
		int groupIndex = bitsetToIndex(group);
		relations[groupIndex] &= ~groups;

		// now turn off the reverse side
		groupIndex = 0;
		while (groups > 0) {
			if ((groups & 1) == 1) {
				relations[groupIndex] &= ~(group);
			}

			groups >>= 1;
			++groupIndex;
		}
	}

	/**
	 * Resets all @{code group} groups by making only groups to given {@code groups}.
	 */
	public void setupGroup(long group, long groups) {
		assert(group > 0);
		assert(groups >= 0);
		assert(Long.numberOfLeadingZeros(group) + Long.numberOfTrailingZeros(group) == MAX_GROUPS - 1);

		// overwrite groups for this group
		int groupIndex = bitsetToIndex(group);
		long oldGroups = relations[groupIndex];
		relations[groupIndex] = groups;

		// now set connections to this group for all other groups
		groupIndex = 0;
		long allGroups = groups | oldGroups;
		while (allGroups > 0) {
			long bitset = relations[groupIndex];

			if ((groups & 1) == 1) {
				bitset |= group;
			}
			else {
				bitset &= ~(group);
			}
			relations[groupIndex] = bitset;

			allGroups >>= 1;
			groups >>= 1;
			++groupIndex;
		}
	}

	/**
	 * Clear all {@code groups} groups.
	 * @param group
	 */
	public void clearGroupRelations(long group) {
		setupGroup(group, 0);
	}

	/**
	 * Checks if there exists a relation between any two groups of two different sets.
	 *
	 * @param groups1 - bitset
	 * @param groups2 - bitset
	 */
	public boolean anyRelationExists(long groups1, long groups2) {
		long leftGroups = groups1;
		while (leftGroups > 0) {
			int groupIndex = bitsetToIndex(leftGroups);
			leftGroups &= ~(1 << groupIndex);

			if ((relations[groupIndex] & groups2) > 0) {
				return true;
			}
		}

		return false;
	}

	/** Looks looks for rightmost bit in given {@code bitset}. */
	private static int bitsetToIndex(long bitset) {
		return Long.numberOfTrailingZeros(bitset);
	}
}
