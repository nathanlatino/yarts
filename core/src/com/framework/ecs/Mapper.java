package com.framework.ecs;

import com.badlogic.ashley.core.ComponentMapper;
import com.framework.ecs.core.components.*;
import com.framework.ecs.meta.components.ClassComponent;
import com.framework.ecs.meta.components.OwnerComponent;
import com.framework.ecs.meta.components.TypeComponent;
import com.framework.ecs.mouse.components.MultiSelectionComponent;
import com.framework.ecs.mouse.components.SelectionComponent;
import com.framework.ecs.optional.components.LightComponent;
import com.framework.ecs.optional.components.ParticlesComponent;
import com.framework.ecs.rendering.components.AnimationComponent;
import com.framework.ecs.rendering.components.OrientationComponent;
import com.framework.ecs.rendering.components.TextureComponent;
import com.framework.ecs.specialized.components.*;
import com.framework.ecs.states.components.*;

public class Mapper {

    public static final ComponentMapper<TypeComponent> type_m = ComponentMapper.getFor(TypeComponent.class);
    public static final ComponentMapper<ClassComponent> class_m = ComponentMapper.getFor(ClassComponent.class);


    public static final ComponentMapper<BodyComponent> body_m = ComponentMapper.getFor(BodyComponent.class);
    public static final ComponentMapper<BoundsComponent> bounds_m = ComponentMapper.getFor(BoundsComponent.class);
    public static final ComponentMapper<TransformComponent> transform_m = ComponentMapper.getFor(TransformComponent.class);
    public static final ComponentMapper<VelocityComponent> velocity_m = ComponentMapper.getFor(VelocityComponent.class);
    public static final ComponentMapper<OrientationComponent> orientation_m = ComponentMapper.getFor(OrientationComponent.class);
    public static final ComponentMapper<SteeringComponent> steering_m = ComponentMapper.getFor(SteeringComponent.class);
    public static final ComponentMapper<RangeComponent> range_m = ComponentMapper.getFor(RangeComponent.class);



    public static final ComponentMapper<TextureComponent> texture_m = ComponentMapper.getFor(TextureComponent.class);
    public static final ComponentMapper<AnimationComponent> animation_m = ComponentMapper.getFor(AnimationComponent.class);
    public static final ComponentMapper<OwnerComponent> owner_m = ComponentMapper.getFor(OwnerComponent.class);
    public static final ComponentMapper<SelectionComponent> selection_m = ComponentMapper.getFor(SelectionComponent.class);
    public static final ComponentMapper<MultiSelectionComponent> multiSelection_m = ComponentMapper.getFor(MultiSelectionComponent.class);


    public static final ComponentMapper<PassiveComponent> passiveState = ComponentMapper.getFor(PassiveComponent.class);
    public static final ComponentMapper<MovingComponent> movingState = ComponentMapper.getFor(MovingComponent.class);
    public static final ComponentMapper<EngagingComponent> engagingState = ComponentMapper.getFor(EngagingComponent.class);
    public static final ComponentMapper<EngagedComponent> engagedState = ComponentMapper.getFor(EngagedComponent.class);
    public static final ComponentMapper<DisengageComponent> disengageState = ComponentMapper.getFor(DisengageComponent.class);
    public static final ComponentMapper<DeadComponent> deadState = ComponentMapper.getFor(DeadComponent.class);
    public static final ComponentMapper<TargetComponent> target_m = ComponentMapper.getFor(TargetComponent.class);



    public static final ComponentMapper<LightComponent> light_m = ComponentMapper.getFor(LightComponent.class);
    public static final ComponentMapper<ParticlesComponent> particles_m = ComponentMapper.getFor(ParticlesComponent.class);
    public static final ComponentMapper<ProjectileComponent> projectile_m = ComponentMapper.getFor(ProjectileComponent.class);



    public static final ComponentMapper<HealthComponent> health_m = ComponentMapper.getFor(HealthComponent.class);
    public static final ComponentMapper<ConsomableComponent> consomable_m = ComponentMapper.getFor(ConsomableComponent.class);
    public static final ComponentMapper<CarryComponent> carry_m = ComponentMapper.getFor(CarryComponent.class);

    public static final ComponentMapper<DamageComponent> damage_m = ComponentMapper.getFor(DamageComponent.class);
}
