package net.namekdev.mgame.components;

import com.artemis.Component;
import com.badlogic.gdx.graphics.g2d.Animation;

public class Teddy extends Component {
	public static final int STAND = 1;
	public static final int WALK = 2;
	public static final int RUN = 3;

	public static final int LEFT = -1;
	public static final int RIGHT = 1;
	public static final int UP = 1;
	public static final int DOWN = -1;
	public static final int NONE = 0;


	public int animState = STAND;
	public int lookDir = RIGHT;
	public int moveHorzDir = NONE;
	public int moveVertDir = NONE;
	public Animation animStanding, animRunning, animWalking;

	public int carriedEntityId = -1;
}
