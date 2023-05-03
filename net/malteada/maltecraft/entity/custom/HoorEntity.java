package net.malteada.maltecraft.entity.custom;

import net.minecraft.block.*;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
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


public class HoorEntity extends HostileEntity implements IAnimatable {


    // INICIALIZADOR DE ANIMACIONES
    private AnimationFactory factory = GeckoLibUtil.createFactory(this);
    public HoorEntity(EntityType<? extends HostileEntity> entityType, World world) {
        super(entityType, world);
    }

    private PlayerEntity targetPlayer;
    private double breakRange = 5.0;

    //ATRIBUTOS
     public static DefaultAttributeContainer.Builder setAttributes(){
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

   /* @Override
    public void tick() {
        super.tick();

        // Get the nearest player
        PlayerEntity player = world.getClosestPlayer(this, 10);
        if (player != null) {
            // Get the position of the block the player is standing on
            BlockPos playerPos = new BlockPos(player.getPos().getX(), player.getPos().getY() - 1, player.getPos().getZ());

            // Break the block if it is within reach of the zombie
            if (canBreakBlock(playerPos)) {
                world.breakBlock(playerPos, true);
            }
        }
    }
*/
    // Funcion pra romper el bloque
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
    }


    // ANIMACIONES  <>
    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if (event.isMoving()){
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.hoor.walk", ILoopType.EDefaultLoopTypes.LOOP));
            return PlayState.CONTINUE;
        }

        event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.hoor.walk", ILoopType.EDefaultLoopTypes.LOOP));
        return PlayState.CONTINUE;
    }


     //STUFF I DON'T GET IT
    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController(this, "controller",
                0, this::predicate));
        animationData.addAnimationController(new AnimationController<HoorEntity>(this, "attackcontroller",
                0, this::attackPredicate));


    }

    private PlayState attackPredicate(AnimationEvent event) {

        if(this.handSwinging && event.getController().getAnimationState().equals(AnimationState.Stopped)){
            event.getController().markNeedsReload();
            event.getController().setAnimation(new AnimationBuilder().addAnimation("animation.hoor.attack", ILoopType.EDefaultLoopTypes.PLAY_ONCE));
            this.handSwinging = false;

        }
        return PlayState.CONTINUE;
    }



    @Override
    public AnimationFactory getFactory() {
        return factory;
    }
    // SONIDOS DE LA ENTIDAD
    @Override
    protected SoundEvent getAmbientSound(){return SoundEvents.ENTITY_ZOMBIE_AMBIENT;}

    @Override
    protected SoundEvent getHurtSound(DamageSource source){return SoundEvents.ENTITY_ZOMBIE_HURT;}
    @Override
    protected SoundEvent getDeathSound(){return SoundEvents.ENTITY_ZOMBIE_DEATH;}
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

    @Override
    public void onDeath(DamageSource source) {
        super.onDeath(source);

        if (!this.world.isClient) {
            // Add custom item drops here
            ItemStack customDrop = new ItemStack(Items.ROTTEN_FLESH, 2);
            this.dropStack(customDrop);

            Random random = this.world.random; // Get a random number generator from the world
            if (random.nextFloat() < 0.50f) { // 25% chance to drop a golden apple
                ItemStack goldenApple = new ItemStack(Items.BONE, 10);
                this.dropStack(goldenApple);
            }
        }
    }
    // Name and color name
    private static final Text ENTITY_NAME = Text.of("§cHoor");

    @Override
    public Text getName() {
        return ENTITY_NAME;
    }

}
