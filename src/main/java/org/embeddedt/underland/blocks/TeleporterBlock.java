package org.embeddedt.underland.blocks;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.TicketType;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.util.ITeleporter;
import org.embeddedt.underland.Underland;
import org.embeddedt.underland.config.UnderlandConfig;
import org.embeddedt.underland.dimensions.Dimensions;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

public class TeleporterBlock extends Block implements EntityBlock {
    private static final VoxelShape SHAPE = Shapes.box(0, 0, 0, 1, .8, 1);

    public TeleporterBlock(Properties p_49795_) {
        super(p_49795_);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    @Override
    public void onPlace(BlockState pState, Level pLevel, BlockPos pPos, BlockState pOldState, boolean pIsMoving) {
        super.onPlace(pState, pLevel, pPos, pOldState, pIsMoving);
        if(!pLevel.isClientSide) {
            ResourceKey<Level> targetLevel;
            if (pLevel.dimension().equals(Dimensions.UNDERLAND)) {
                targetLevel = Level.OVERWORLD;
            } else
                targetLevel = Dimensions.UNDERLAND;
            if(pLevel.getBlockEntity(pPos) instanceof TeleporterBlockEntity tbe)
                tbe.setTargetDimension(targetLevel.location());
        }
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
        if(!level.isClientSide) {
            MinecraftServer server = ((ServerLevel)level).getServer();
            ServerLevel beneathLevel = server.getLevel(Dimensions.UNDERLAND);
            if(pos.getY() <= (beneathLevel.getMinBuildHeight() + 1) || pos.getY() >= (beneathLevel.getMaxBuildHeight() - 5)) {
                player.displayClientMessage(Component.translatable("underland.teleporter_out_of_range").withStyle(ChatFormatting.RED), true);
            } else if(level.getBlockEntity(pos) instanceof TeleporterBlockEntity tbe) {
                teleportTo((ServerPlayer)player, pos, ResourceKey.create(Registry.DIMENSION_REGISTRY, tbe.getTargetDimension()));
            }
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    private void teleportTo(ServerPlayer player, BlockPos pos, ResourceKey<Level> id) {
        teleport(player, player.getServer().getLevel(id), pos);
    }

    public static void teleport(ServerPlayer entity, ServerLevel destination, BlockPos pos) {
        entity.changeDimension(destination, new ITeleporter() {
            @Override
            public Entity placeEntity(Entity entity, ServerLevel currentWorld, ServerLevel destWorld, float yaw, Function<Boolean, Entity> repositionEntity) {
                entity = repositionEntity.apply(false);
                if(destWorld.getBlockState(pos) != Underland.TELEPORTER.get().defaultBlockState()) {
                    BlockPos bottomLeft = pos.offset(-2, 0, -2);
                    BlockPos ceilingTopRight = pos.offset(2, 3, 2);
                    for(BlockPos pos : BlockPos.betweenClosed(bottomLeft, ceilingTopRight)) {
                        destWorld.setBlock(pos.immutable(), Blocks.AIR.defaultBlockState(), Block.UPDATE_CLIENTS);
                    }
                    BlockPos floorRight = pos.offset(2, 0, 2);
                    for(BlockPos newPos : BlockPos.betweenClosed(bottomLeft, floorRight)) {
                        BlockState desiredState = newPos.equals(pos) ? Underland.TELEPORTER.get().defaultBlockState() : Blocks.COBBLESTONE.defaultBlockState();
                        destWorld.setBlock(newPos, desiredState, Block.UPDATE_CLIENTS | Block.UPDATE_NEIGHBORS);
                        if(newPos.equals(pos) && destWorld.getBlockEntity(newPos) instanceof TeleporterBlockEntity tbe) {
                            tbe.setTargetDimension(UnderlandConfig.ADDITIONAL_DIMENSION_TELEPORT.get() ? currentWorld.dimension().location() : Level.OVERWORLD.location());
                        }
                    }
                    if(UnderlandConfig.TELEPORTER_TORCHES.get()) {
                        BlockPos torchPos = pos.above();
                        destWorld.setBlock(torchPos.offset(-2, 0, -2), Blocks.TORCH.defaultBlockState(), Block.UPDATE_CLIENTS | Block.UPDATE_NEIGHBORS);
                        destWorld.setBlock(torchPos.offset(-2, 0, 2), Blocks.TORCH.defaultBlockState(), Block.UPDATE_CLIENTS | Block.UPDATE_NEIGHBORS);
                        destWorld.setBlock(torchPos.offset(2, 0, -2), Blocks.TORCH.defaultBlockState(), Block.UPDATE_CLIENTS | Block.UPDATE_NEIGHBORS);
                        destWorld.setBlock(torchPos.offset(2, 0, 2), Blocks.TORCH.defaultBlockState(), Block.UPDATE_CLIENTS | Block.UPDATE_NEIGHBORS);
                    }
                }

                int y = pos.getY();
                entity.teleportTo(pos.getX() + 0.5, y + 1, pos.getZ() + 0.5);
                destWorld.getChunkSource().addRegionTicket(TicketType.PORTAL, new ChunkPos(pos), 3, pos);
                return entity;
            }
        });
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new TeleporterBlockEntity(pPos, pState);
    }
}
