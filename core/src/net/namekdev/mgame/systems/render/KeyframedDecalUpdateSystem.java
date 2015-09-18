package net.namekdev.mgame.systems.render;

import net.namekdev.mgame.components.AnimationComponent;
import net.namekdev.mgame.components.render.DecalComponent;

import com.artemis.Aspect;
import com.artemis.ComponentMapper;
import com.artemis.Entity;
import com.artemis.annotations.Wire;
import com.artemis.systems.EntityProcessingSystem;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

@Wire
public class KeyframedDecalUpdateSystem extends EntityProcessingSystem {
	ComponentMapper<AnimationComponent> mAnim;
	ComponentMapper<DecalComponent> mDecal;


	public KeyframedDecalUpdateSystem() {
		super(Aspect.all(DecalComponent.class, AnimationComponent.class));
	}

	@Override
	protected void process(Entity entity) {
		AnimationComponent anim = mAnim.get(entity);
		DecalComponent decal = mDecal.get(entity);

		TextureRegion frame = anim.animation.getKeyFrame(anim.stateTime, true);
		decal.decal.setScaleX(anim.flipHorz ? -1 : 1);
		decal.decal.setTextureRegion(frame);
	}

}
