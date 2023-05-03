package net.malteada.maltecraft.entity.custom;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;
import software.bernie.geckolib3.core.AnimationState;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;
import java.util.List;


public class BragiEntity extends HostileEntity implements IAnimatable {


    private AnimationFactory factory = GeckoLibUtil.createFactory(this);


    public BragiEntity(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
    }

    private PlayerEntity targetPlayer;

    //ATRIBUTOS
    public static DefaultAttributeContainer.Builder setAttributes() {
        return HoorEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 20.0D)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 8.0F)
                .add(EntityAttributes.GENERIC_ATTACK_SPEED, 2.0D)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.4F);
    }

    // OBJETIVOS DE LA ENTIDAD GOALS S


    @Override
    protected void initGoals() {

        this.goalSelector.add(1, new SwimGoal(this));
        this.goalSelector.add(2, new MeleeAttackGoal(this, 1.2D, true));
        this.goalSelector.add(3, new WanderAroundFarGoal(this, 0.75F, 1));
        this.goalSelector.add(4, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.add(5, new LookAroundGoal(this));
        this.targetPlayer = null;
        this.targetSelector.add(2, new ActiveTargetGoal<>(this, PlayerEntity.class, true));
        super.initGoals();
    }

    // ANIMACIONES  <>
    private <E extends Entity & IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if (event.isMoving()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.bragi.walk", ILoopType.EDefaultLoopTypes.LOOP));
            return PlayState.CONTINUE;
        }

        event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.bragi.idle", ILoopType.EDefaultLoopTypes.LOOP));
        return PlayState.CONTINUE;


    }


    //STUFF I DON'T GET IT
    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController(this, "controller",
                0, this::predicate));

        animationData.addAnimationController(new AnimationController(this, "explodecontroller",
                0, this::explodepredicate));


    }

    private PlayState explodepredicate(AnimationEvent event) {
        if (this.handSwinging && event.getController().getAnimationState().equals(AnimationState.Stopped)) {
            event.getController().markNeedsReload();

        }
        return PlayState.CONTINUE;
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    // SONIDOS DE LA ENTIDAD
    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_ZOMBIE_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return SoundEvents.ENTITY_ZOMBIE_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_ZOMBIE_DEATH;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(SoundEvents.ENTITY_ZOMBIE_VILLAGER_STEP, 0.15F, 1.0F);
    }


    // This is for explode like a creeper
    private int explosionRadius = 3;

    @Override
    public void tick() {
        super.tick();
        // Apply a glowing effect to the entity
        // this.addStatusEffect(new StatusEffectInstance(StatusEffects.GLOWING, 10, 0));
        /*BlockPos pos = this.getBlockPos();
        this.world.getLightLevel(LightType.BLOCK, pos); // refresh the light level cache
        this.world.setBlockState(pos, this.world.getBlockState(pos).with(LightType.BLOCK, 2.0D));
        this.world.updateListeners(pos, this.world.getBlockState(pos), this.world.getBlockState(pos), 3);*/


        // Get the closest player within a radius of 10 blocks
        Entity closestPlayer = world.getClosestPlayer(this, 10);
        if (closestPlayer != null && closestPlayer.isAlive()) {
            // Check if the player is within 2 blocks of the entity
            double distanceSq = this.squaredDistanceTo(closestPlayer);
            if (distanceSq < 4.0D) {
                this.explode();
            }
        }

    }


    // This is for explodes like a creeper
    private void explode() {

        if (!this.world.isClient) {
            // This create the explosion at the position of the entity
            this.world.createExplosion(this, this.getX(), this.getY(), this.getZ(), this.explosionRadius, Explosion.DestructionType.DESTROY);
            this.discard();
            ServerWorld serverWorld = (ServerWorld) this.world;
            // Spawn some particles to show the explosion and poison cloud
            serverWorld.spawnParticles(ParticleTypes.EXPLOSION, this.getX(), this.getY(), this.getZ(), 1, 0, 0, 0, 0.1);
            serverWorld.spawnParticles(ParticleTypes.CRIMSON_SPORE, this.getX(), this.getY() + 0.5, this.getZ(), 40, 0.5, 0.5, 0.5, 0.1);

            // Apply a PotionEffect to all nearby living entities
            double radius = 3.0;
            List<Entity> entities = serverWorld.getEntitiesByClass(Entity.class, new Box(getPos().add(-radius, -radius, -radius), getPos().add(radius, radius, radius)), (entity) -> entity instanceof LivingEntity);
            for (Entity entity : entities) {
                ((LivingEntity) entity).addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, 200));
            }
        }
    }

     /*  public void explodeWithDelay() {
        BlockPos pos = new BlockPos(this.getX(), this.getY(), this.getZ());
        new Thread(() -> {
            try {
                Thread.sleep(2000); // Sleep for 2 seconds (2000 milliseconds)
                this.world.createExplosion(this, pos.getX(), pos.getY(), pos.getZ(), this.explosionRadius, Explosion.DestructionType.DESTROY);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }*/
    // This is for explodes like a creeper
        /*    private void explode() {

        if (!this.world.isClient) {



            // This create the explosion at the position of the entity
            this.world.createExplosion(this, this.getX(), this.getY(), this.getZ(), this.explosionRadius, Explosion.DestructionType.DESTROY);
            this.discard();
            ServerWorld serverWorld = (ServerWorld) this.world;

            // Spawn some particles to show the explosion and poison cloud
            serverWorld.spawnParticles(ParticleTypes.EXPLOSION, this.getX(), this.getY(), this.getZ(), 1, 0, 0, 0, 0.1);
            serverWorld.spawnParticles(ParticleTypes.CRIMSON_SPORE, this.getX(), this.getY() + 0.5, this.getZ(), 40, 0.5, 0.5, 0.5, 0.1);

            // Apply a PotionEffect to all nearby living entities
            double radius = 3.0;
            List<Entity> entities = serverWorld.getEntitiesByClass(Entity.class, new Box(getPos().add(-radius, -radius, -radius), getPos().add(radius, radius, radius)), (entity) -> entity instanceof LivingEntity);
            for (Entity entity : entities) {
                ((LivingEntity) entity).addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, 200));
            }



            }


        }
*/


    // This an experimental blood effect, I don't know what I'm doing
    @Override
    public boolean damage(DamageSource source, float amount) {
        boolean wasAlive = this.isAlive();
        if (!wasAlive) {
            return false;
        } else {
            boolean isCreative = source.getAttacker() instanceof ServerPlayerEntity && ((ServerPlayerEntity) source.getAttacker()).interactionManager.isCreative();
            if (!isCreative) {
                BlockPos pos = new BlockPos(this.getX(), this.getY(), this.getZ());
                World world = this.getEntityWorld();
                BlockState blockstate = Blocks.REDSTONE_BLOCK.getDefaultState();
                for (int i = 0; i < 20; ++i) {
                    double d0 = this.random.nextGaussian() * 0.02D;
                    double d1 = this.random.nextGaussian() * 0.02D;
                    double d2 = this.random.nextGaussian() * 0.02D;
                    double d3 = 10.0D;
                    ParticleEffect particle = new BlockStateParticleEffect(ParticleTypes.BLOCK, blockstate);
                    world.addParticle(particle, this.getX() + (double) (this.random.nextFloat() * this.getWidth() * 2.0F) - (double) this.getWidth(), this.getY() + (double) (this.random.nextFloat() * this.getHeight()), this.getZ() + (double) (this.random.nextFloat() * this.getWidth() * 2.0F) - (double) this.getWidth(), d0, d1, d2);
                }
            }
            return super.damage(source, amount);
        }
    }
    // Name and color name
    private static final Text ENTITY_NAME = Text.of("§cBragi");

    @Override
    public Text getName() {
        return ENTITY_NAME;
    }
}







