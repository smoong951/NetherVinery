package net.satisfy.nethervinery.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.satisfy.nethervinery.block.entity.ApplePressBlockEntity;
import net.satisfy.nethervinery.registry.NetherBlockEntityTypes;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("deprecation")
public class ApplePressBlock extends BaseEntityBlock {

	public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

	public ApplePressBlock(Properties settings) {
		super(settings);
		this.registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.NORTH));
	}

	@Override
	public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean moved) {
		if (state.getBlock() != newState.getBlock()) {
			BlockEntity blockEntity = world.getBlockEntity(pos);
			if (blockEntity instanceof ApplePressBlockEntity) {
				Containers.dropContents(world, pos, (ApplePressBlockEntity)blockEntity);
				world.updateNeighbourForOutputSignal(pos,this);
			}
			super.onRemove(state, world, pos, newState, moved);
		}
	}

	@Override
	public @NotNull InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
		if (!world.isClientSide) {
			MenuProvider screenHandlerFactory = state.getMenuProvider(world, pos);
			if (screenHandlerFactory != null) {
				player.openMenu(screenHandlerFactory);
			}
		}
		return InteractionResult.SUCCESS;
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new ApplePressBlockEntity(pos, state);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
		return createTickerHelper(type, NetherBlockEntityTypes.APPLE_PRESS_BLOCK_ENTITY.get(), ApplePressBlockEntity::tick);
	}


	protected static final VoxelShape SHAPE_WE = makeShapeWE();
	protected static final VoxelShape SHAPE_NS = makeShapeNS();

	@Override
	public @NotNull VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
		return state.getValue(FACING) == Direction.SOUTH || state.getValue(FACING) == Direction.NORTH ? SHAPE_WE : SHAPE_NS;
	}


	public static VoxelShape makeShapeWE() {
		VoxelShape shape = Shapes.empty();
	shape = Shapes.joinUnoptimized(shape, Shapes.box(0, 0, 0.09375, 0.125, 0.125, 0.90625), BooleanOp.OR);
	shape = Shapes.joinUnoptimized(shape, Shapes.box(0.875, 0, 0.09375, 1, 0.125, 0.90625), BooleanOp.OR);
	shape = Shapes.joinUnoptimized(shape, Shapes.box(0.46875, 1.46875, 0.475, 0.53125, 1.90625, 0.5375), BooleanOp.OR);
	shape = Shapes.joinUnoptimized(shape, Shapes.box(0.46875, 1.03125, 0.475, 0.53125, 1.46875, 0.5375), BooleanOp.OR);
	shape = Shapes.joinUnoptimized(shape, Shapes.box(0.375, 1.90625, 0.375, 0.625, 1.96875, 0.625), BooleanOp.OR);
	shape = Shapes.joinUnoptimized(shape, Shapes.box(0, 0.125, 0.40625, 0.125, 1.0625, 0.59375), BooleanOp.OR);
	shape = Shapes.joinUnoptimized(shape, Shapes.box(0.875, 0.125, 0.40625, 1, 1.0625, 0.59375), BooleanOp.OR);
	shape = Shapes.joinUnoptimized(shape, Shapes.box(0.1875, 0.4375, 0.8125, 0.8125, 0.5625, 0.875), BooleanOp.OR);
	shape = Shapes.joinUnoptimized(shape, Shapes.box(0.1875, 0.4375, 0.125, 0.8125, 0.5625, 0.1875), BooleanOp.OR);
	shape = Shapes.joinUnoptimized(shape, Shapes.box(0.1875, 0.4375, 0.1875, 0.8125, 1.1875, 0.8125), BooleanOp.OR);
	shape = Shapes.joinUnoptimized(shape, Shapes.box(0.125, 0.4375, 0.125, 0.1875, 0.5625, 0.875), BooleanOp.OR);
	shape = Shapes.joinUnoptimized(shape, Shapes.box(0.8125, 0.4375, 0.125, 0.875, 0.5625, 0.875), BooleanOp.OR);
	shape = Shapes.joinUnoptimized(shape, Shapes.box(0, 1.0625, 0.40625, 0.125, 1.5, 0.59375), BooleanOp.OR);
	shape = Shapes.joinUnoptimized(shape, Shapes.box(0.875, 1.0625, 0.40625, 1, 1.5, 0.59375), BooleanOp.OR);
	shape = Shapes.joinUnoptimized(shape, Shapes.box(0.125, 0.3125, 0.46875, 0.875, 0.4375, 0.53125), BooleanOp.OR);
	shape = Shapes.joinUnoptimized(shape, Shapes.box(0.125, 1.375, 0.40625, 0.875, 1.5, 0.59375), BooleanOp.OR);
		return shape;
	}

	public static VoxelShape makeShapeNS() {
		VoxelShape shape = Shapes.empty();
		shape = Shapes.joinUnoptimized(shape, Shapes.box(0.09375, 0, 0, 0.90625, 0.125, 0.125), BooleanOp.OR);
		shape = Shapes.joinUnoptimized(shape, Shapes.box(0.15625, 0, 0.875, 0.90625, 0.125, 1), BooleanOp.OR);
		shape = Shapes.joinUnoptimized(shape, Shapes.box(0.4625, 1.46875, 0.46875, 0.525, 1.90625, 0.53125), BooleanOp.OR);
		shape = Shapes.joinUnoptimized(shape, Shapes.box(0.4625, 1.03125, 0.46875, 0.525, 1.46875, 0.53125), BooleanOp.OR);
		shape = Shapes.joinUnoptimized(shape, Shapes.box(0.375, 1.90625, 0.375, 0.625, 1.96875, 0.625), BooleanOp.OR);
		shape = Shapes.joinUnoptimized(shape, Shapes.box(0.40625, 0.125, 0, 0.59375, 1.0625, 0.125), BooleanOp.OR);
		shape = Shapes.joinUnoptimized(shape, Shapes.box(0.40625, 0.125, 0.875, 0.59375, 1.0625, 1), BooleanOp.OR);
		shape = Shapes.joinUnoptimized(shape, Shapes.box(0.125, 0.4375, 0.1875, 0.1875, 0.5625, 0.8125), BooleanOp.OR);
		shape = Shapes.joinUnoptimized(shape, Shapes.box(0.8125, 0.4375, 0.1875, 0.875, 0.5625, 0.8125), BooleanOp.OR);
		shape = Shapes.joinUnoptimized(shape, Shapes.box(0.1875, 0.4375, 0.1875, 0.8125, 1.1875, 0.8125), BooleanOp.OR);
		shape = Shapes.joinUnoptimized(shape, Shapes.box(0.125, 0.4375, 0.125, 0.875, 0.5625, 0.1875), BooleanOp.OR);
		shape = Shapes.joinUnoptimized(shape, Shapes.box(0.125, 0.4375, 0.8125, 0.875, 0.5625, 0.875), BooleanOp.OR);
		shape = Shapes.joinUnoptimized(shape, Shapes.box(0.40625, 1.0625, 0, 0.59375, 1.5, 0.125), BooleanOp.OR);
		shape = Shapes.joinUnoptimized(shape, Shapes.box(0.40625, 1.0625, 0.875, 0.59375, 1.5, 1), BooleanOp.OR);
		shape = Shapes.joinUnoptimized(shape, Shapes.box(0.46875, 0.3125, 0.125, 0.53125, 0.4375, 0.875), BooleanOp.OR);
		shape = Shapes.joinUnoptimized(shape, Shapes.box(0.40625, 1.375, 0.125, 0.59375, 1.5, 0.875), BooleanOp.OR);

		return shape;
	}

	@Override
	public @NotNull RenderShape getRenderShape(BlockState state) {
		return RenderShape.MODEL;
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		return this.defaultBlockState().setValue(FACING, ctx.getHorizontalDirection().getOpposite());
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING);
	}

	public @NotNull BlockState rotate(BlockState state, Rotation rotation) {
		return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
	}

	public @NotNull BlockState mirror(BlockState state, Mirror mirror) {
		return state.rotate(mirror.getRotation(state.getValue(FACING)));
	}
}