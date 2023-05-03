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
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.random.Random;
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

public class TyrEntity extends HostileEntity implements IAnimatable {


    private AnimationFactory factory = GeckoLibUtil.createFactory(this);
    private PlayerEntity targetPlayer;

    public TyrEntity(EntityType<? extends HostileEntity> entityType, World world) {


        super(entityType, world);

    }

    //ATRIBUTOS
    public static DefaultAttributeContainer.Builder setAttributes() {
        return HoorEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 200.0D)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 8.0F)
                .add(EntityAttributes.GENERIC_ATTACK_SPEED, 2.0D)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.35F)
                .add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 3.0F)
                .add(EntityAttributes.GENERIC_ATTACK_KNOCKBACK, 8.5F)
                .add(EntityAttributes.GENERIC_FOLLOW_RANGE, 32.0F);

    }

    @Override
    protected void initGoals() {

        this.goalSelector.add(1, new SwimGoal(this));
        this.goalSelector.add(2, new MeleeAttackGoal(this, 1.2D, true));
        this.goalSelector.add(3, new WanderAroundFarGoal(this, 0.75F, 1));
        this.goalSelector.add(4, new LookAtEntityGoal(this, PlayerEntity.class, 12.0F));
        this.goalSelector.add(5, new LookAroundGoal(this));
        this.targetPlayer = null;
        this.targetSelector.add(2, new ActiveTargetGoal<>(this, PlayerEntity.class, true));
        super.initGoals();
    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if (event.isMoving()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.tyr.walk", ILoopType.EDefaultLoopTypes.LOOP));
            return PlayState.CONTINUE;
        }

        event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.tyr.idle", ILoopType.EDefaultLoopTypes.LOOP));
        return PlayState.CONTINUE;
    }

    // Like a wither, this entity apply wither on his attack
    public boolean tryAttack(Entity target) {
        if (!super.tryAttack(target)) {
            return false;
        } else {
            if (target instanceof LivingEntity) {
                ((LivingEntity) target).addStatusEffect(new StatusEffectInstance(StatusEffects.WITHER, 200), this);
                ((LivingEntity) target).addStatusEffect(new StatusEffectInstance(StatusEffects.BLINDNESS, 200), this);
                world.createExplosion(this, target.getX(), target.getY(), target.getZ(), 1.0f, Explosion.DestructionType.BREAK);

            }

            return true;
        }
    }

    public void onCollision(Box collisionBox) {
        BlockPos minPos = new BlockPos(collisionBox.minX, collisionBox.minY, collisionBox.minZ);
        BlockPos maxPos = new BlockPos(collisionBox.maxX, collisionBox.maxY, collisionBox.maxZ);

        for (BlockPos pos : BlockPos.iterate(minPos, maxPos)) {
            BlockState state = this.world.getBlockState(pos);
            if (state.getMaterial().isSolid()) {
                this.world.breakBlock(pos, true);
            }
        }

        this.discard();
    }


    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController<>(this, "controller",
                0, this::predicate));

        animationData.addAnimationController(new AnimationController<TyrEntity>(this, "attackcontroller",
                0, this::attackPredicate));


    }

    private PlayState attackPredicate(AnimationEvent event) {

        if (this.handSwinging && event.getController().getAnimationState().equals(AnimationState.Stopped)) {
            event.getController().markNeedsReload();
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.tyr.attack", ILoopType.EDefaultLoopTypes.PLAY_ONCE));
            this.handSwinging = false;

        }
        return PlayState.CONTINUE;
    }
    // public void attack() {
    //   this.handSwinging = true;
    //}
    // This creates an explosion when attacks the player, in their position
    /*
    @Override
    public boolean tryAttack(Entity target) {
        boolean success = super.tryAttack(target);
        if (success && target instanceof PlayerEntity) {
            // Create a small explosion at the player's position
            Vec3d playerPos = target.getPos();
            World world = this.getEntityWorld();
            world.createExplosion(this, playerPos.x, playerPos.y, playerPos.z, 0.2f, Explosion.DestructionType.DESTROY);
        }
        return success;
    }

     */

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
    @Override
    public void onDeath(DamageSource source) {
        super.onDeath(source);

        if (!this.world.isClient) {
            // Add custom item drops here
            ItemStack customDrop = new ItemStack(Items.ROTTEN_FLESH, 5);
            this.dropStack(customDrop);

            Random random = this.world.random; // Get a random number generator from the world
            if (random.nextFloat() < 0.10f) { // 25% chance to drop a golden apple
                ItemStack goldenApple = new ItemStack(Items.COOKED_BEEF, 4);
                this.dropStack(goldenApple);
            }
        }
    }

    // Name and color name
    private static final Text ENTITY_NAME = Text.of("§cTyr");

    @Override
    public Text getName() {
        return ENTITY_NAME;
    }

}
