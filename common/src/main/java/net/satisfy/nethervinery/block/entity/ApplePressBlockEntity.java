package net.satisfy.nethervinery.block.entity;

import de.cristelknight.doapi.common.world.ImplementedInventory;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.satisfy.nethervinery.client.gui.handler.ApplePressGuiHandler;
import net.satisfy.nethervinery.registry.NetherBlockEntityTypes;
import net.satisfy.vinery.registry.ObjectRegistry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public class ApplePressBlockEntity extends BlockEntity implements MenuProvider, ImplementedInventory {
    private final NonNullList<ItemStack> inventory = NonNullList.withSize(2, ItemStack.EMPTY);

    protected final ContainerData propertyDelegate;
    private int progress = 0;
    private int maxProgress = 72;

    public ApplePressBlockEntity(BlockPos pos, BlockState state) {
        super(NetherBlockEntityTypes.APPLE_PRESS_BLOCK_ENTITY.get(), pos, state);
        this.propertyDelegate = new ContainerData() {
            public int get(int index) {
                return switch (index) {
                    case 0 -> ApplePressBlockEntity.this.progress;
                    case 1 -> ApplePressBlockEntity.this.maxProgress;
                    default -> 0;
                };
            }

            public void set(int index, int value) {
                switch (index) {
                    case 0 -> ApplePressBlockEntity.this.progress = value;
                    case 1 -> ApplePressBlockEntity.this.maxProgress = value;
                }
            }

            public int getCount() {
                return 2;
            }
        };
    }

    @Override
    public NonNullList<ItemStack> getItems() {
        return this.inventory;
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatable(this.getBlockState().getBlock().getDescriptionId());
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int syncId, Inventory inv, Player player) {
        return new ApplePressGuiHandler(syncId, inv, this, this.propertyDelegate);
    }

    @Override
    public boolean stillValid(Player player) {
        assert this.level != null;
        if (this.level.getBlockEntity(this.worldPosition) != this) {
            return false;
        } else {
            return player.distanceToSqr((double)this.worldPosition.getX() + 0.5, (double)this.worldPosition.getY() + 0.5, (double)this.worldPosition.getZ() + 0.5) <= 64.0;
        }
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        super.saveAdditional(nbt);
        ContainerHelper.saveAllItems(nbt, inventory);
        nbt.putInt("apple_press.progress", progress);
    }

    @Override
    public void load(CompoundTag nbt) {
        ContainerHelper.loadAllItems(nbt, inventory);
        progress = nbt.getInt("apple_press.progress");
        super.load(nbt);

    }

    private void resetProgress() {
        this.progress = 0;
    }

    public static void tick(Level world, BlockPos blockPos, BlockState state, ApplePressBlockEntity entity) {
        if(world.isClientSide()) {
            return;
        }

        if(hasRecipe(entity)) {
            entity.progress++;
            setChanged(world, blockPos, state);
            if(entity.progress >= entity.maxProgress) {
                craftItem(entity);
            }
        } else {
            entity.resetProgress();
            setChanged(world, blockPos, state);
        }
    }

    private static void craftItem(ApplePressBlockEntity entity) {
        SimpleContainer inventory = new SimpleContainer(entity.getContainerSize());
        for (int i = 0; i < entity.getContainerSize(); i++) {
            inventory.setItem(i, entity.getItem(i));
        }

        if(hasRecipe(entity)) {
            entity.removeItem(0, 1);

            entity.setItem(1, new ItemStack(ObjectRegistry.APPLE_MASH.get(),
                    entity.getItem(1).getCount() + 1));

            entity.resetProgress();
        }
    }

    private static boolean hasRecipe(ApplePressBlockEntity entity) {
        SimpleContainer inventory = new SimpleContainer(entity.getContainerSize());
        for (int i = 0; i < entity.getContainerSize(); i++) {
            inventory.setItem(i, entity.getItem(i));
        }

        boolean hasAppleInFirstSlot = entity.getItem(0).getItem() == Items.APPLE;

        return hasAppleInFirstSlot && canInsertAmountIntoOutputSlot(inventory)
                && canInsertItemIntoOutputSlot(inventory, ObjectRegistry.APPLE_MASH.get());
    }

    private static boolean canInsertItemIntoOutputSlot(SimpleContainer inventory, Item output) {
        return inventory.getItem(1).getItem() == output || inventory.getItem(1).isEmpty();
    }

    private static boolean canInsertAmountIntoOutputSlot(SimpleContainer inventory) {
        return inventory.getItem(1).getMaxStackSize() > inventory.getItem(1).getCount();
    }
}
