package net.malteada.maltecraft.entity.custom;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
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
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
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

public class ForsetiEntity extends HostileEntity implements IAnimatable {
    public ForsetiEntity(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
    }

    // INICIALIZADOR DE ANIMACIONES
    private AnimationFactory factory = GeckoLibUtil.createFactory(this);

    //ATRIBUTOS
    public static DefaultAttributeContainer.Builder setAttributes(){
        return HoorEntity.createMobAttributes()
                .add(EntityAttributes.GENERIC_MAX_HEALTH, 70.0D)
                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 8.0F)
                .add(EntityAttributes.GENERIC_ATTACK_SPEED, 2.0D)
                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.4F);
    }

    // OBJETIVOS DE LA ENTIDAD GOALS S

    private PlayerEntity targetPlayer;
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
    // Funcion para romper el bloque
    private boolean canBreakBlock(BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        if (state.getHardness(world, pos) == -1.0f) {
            // Unbreakable blocks (such as bedrock) cannot be broken
            return false;
        }
        Material material = state.getMaterial();
        if (material.isLiquid() || material.isReplaceable()) {
            // Liquids and replaceable blocks (such as air) cannot be broken
            return false;
        }
        Vec3d zombiePos = new Vec3d(getX(), getY(), getZ());
        Vec3d blockPos = new Vec3d(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
        double distanceSquared = zombiePos.squaredDistanceTo(blockPos);
        return distanceSquared <= (BREAK_RANGE * BREAK_RANGE);
    }

    private static final int BREAK_RANGE = 1;
    @Override
    public void tick() {
        super.tick();

        // Get the player target, even if they are not directly visible
        LivingEntity target = getTarget();
        if (target == null || !(target instanceof PlayerEntity)) {
            return;
        }
        PlayerEntity player = (PlayerEntity) target;

        // Search for blocks to break around the player
        BlockPos playerPos = new BlockPos(player.getPos().getX(), player.getPos().getY() - 1, player.getPos().getZ());
        for (BlockPos pos : BlockPos.iterate(playerPos.add(-BREAK_RANGE, -BREAK_RANGE, -BREAK_RANGE), playerPos.add(BREAK_RANGE, BREAK_RANGE, BREAK_RANGE))) {
            if (canBreakBlock(pos)) {
                world.breakBlock(pos, true);
            }
        }

        // Move towards the player
        getNavigation().startMovingTo(player, 1.0);

        if (!this.world.isClient) {
            this.setClimbingWall(this.horizontalCollision);
        }
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }
    // ANIMACIONES  <>
    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if (event.isMoving()){
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.forseti.walk", ILoopType.EDefaultLoopTypes.LOOP));
            return PlayState.CONTINUE;
        }

        event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.forseti.idle", ILoopType.EDefaultLoopTypes.LOOP));
        return PlayState.CONTINUE;
    }

    private PlayState attackPredicate(AnimationEvent event) {
        if(this.handSwinging && event.getController().getAnimationState().equals(AnimationState.Stopped)) {
            event.getController().markNeedsReload();

            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.forseti.attack", ILoopType.EDefaultLoopTypes.PLAY_ONCE));
            this.handSwinging = false;

        }
        return PlayState.CONTINUE;
    }



    //STUFF I DON'T GET IT
    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController(this, "controller",
                0, this::predicate));
        animationData.addAnimationController(new AnimationController(this, "attackcontroller",
                0, this::attackPredicate));




    }
    // SONIDOS DE LA ENTIDAD
    @Override
    protected SoundEvent getAmbientSound(){return SoundEvents.ENTITY_GHAST_HURT;}

    @Override
    protected SoundEvent getHurtSound(DamageSource source){return SoundEvents.ENTITY_GHAST_HURT;}
    @Override
    protected SoundEvent getDeathSound(){return SoundEvents.ENTITY_GHAST_DEATH;}
    @Override
    protected void playStepSound(BlockPos pos, BlockState state){
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
                    world.addParticle(particle, this.getX() + (double)(this.random.nextFloat() * this.getWidth() * 2.0F) - (double)this.getWidth(), this.getY() + (double)(this.random.nextFloat() * this.getHeight()), this.getZ() + (double)(this.random.nextFloat() * this.getWidth() * 2.0F) - (double)this.getWidth(), d0, d1, d2);
                }
            }
            return super.damage(source, amount);
        }
    }
    // Like a spider, this entity can climb walls
    private static final TrackedData<Byte> FORSETI_FLAGS;
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(FORSETI_FLAGS, (byte)0);
    }

    public boolean isClimbing() {
        return this.isClimbingWall();
    }
    public boolean isClimbingWall() {
        return ((Byte)this.dataTracker.get(FORSETI_FLAGS) & 1) != 0;
    }

    public void setClimbingWall(boolean climbing) {
        byte b = (Byte)this.dataTracker.get(FORSETI_FLAGS);
        if (climbing) {
            b = (byte)(b | 1);
        } else {
            b &= -2;
        }

        this.dataTracker.set(FORSETI_FLAGS, b);
    }
    static {
        FORSETI_FLAGS = DataTracker.registerData(ForsetiEntity.class, TrackedDataHandlerRegistry.BYTE);
    }
    // CUSTOM DROPS
    @Override
    public void onDeath(DamageSource source) {
        super.onDeath(source);

        if (!this.world.isClient) {
            // Add custom item drops here
            ItemStack customDrop = new ItemStack(Items.ROTTEN_FLESH, 3);
            this.dropStack(customDrop);

            Random random = this.world.random; // Get a random number generator from the world
            if (random.nextFloat() < 0.05f) { // 25% chance to drop a golden apple
                ItemStack randomDrop = new ItemStack(Items.GHAST_TEAR, 1);
                this.dropStack(randomDrop);
            }
        }
    }
    // Name and color name
    private static final Text ENTITY_NAME = Text.of("§cForseti");

    @Override
    public Text getName() {
        return ENTITY_NAME;
    }
}

