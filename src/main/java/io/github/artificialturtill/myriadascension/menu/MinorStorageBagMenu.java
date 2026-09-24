package io.github.artificialturtill.myriadascension.menu;

import io.github.artificialturtill.myriadascension.artifact.MinorStorageBagItem;
import io.github.artificialturtill.myriadascension.registry.ModMenus;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public final class MinorStorageBagMenu extends AbstractContainerMenu {
    private static final int BAG_SLOTS = 9;
    private static final int PLAYER_INV_START = BAG_SLOTS;
    private static final int PLAYER_INV_END = PLAYER_INV_START + 27;
    private static final int HOTBAR_END = PLAYER_INV_END + 9;

    private final Container bag;

    public MinorStorageBagMenu(
            int containerId,
            Inventory playerInventory,
            RegistryFriendlyByteBuf ignored) {
        this(containerId, playerInventory, new SimpleContainer(BAG_SLOTS));
    }

    public MinorStorageBagMenu(
            int containerId,
            Inventory playerInventory,
            Container bag) {
        super(ModMenus.MINOR_STORAGE_BAG.get(), containerId);
        this.bag = bag;

        checkContainerSize(bag, BAG_SLOTS);
        bag.startOpen(playerInventory.player);

        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 3; column++) {
                int index = column + row * 3;
                addSlot(new Slot(bag, index, 62 + column * 18, 17 + row * 18) {
                    @Override
                    public boolean mayPlace(ItemStack stack) {
                        return !(stack.getItem() instanceof MinorStorageBagItem);
                    }
                });
            }
        }

        for (int row = 0; row < 3; row++) {
            for (int column = 0; column < 9; column++) {
                addSlot(new Slot(
                        playerInventory,
                        column + row * 9 + 9,
                        8 + column * 18,
                        84 + row * 18));
            }
        }

        for (int column = 0; column < 9; column++) {
            addSlot(new Slot(
                    playerInventory,
                    column,
                    8 + column * 18,
                    142));
        }
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        Slot slot = slots.get(index);
        if (!slot.hasItem()) {
            return ItemStack.EMPTY;
        }

        ItemStack source = slot.getItem();
        ItemStack copy = source.copy();

        if (index < BAG_SLOTS) {
            if (!moveItemStackTo(source, PLAYER_INV_START, HOTBAR_END, true)) {
                return ItemStack.EMPTY;
            }
        } else {
            if (source.getItem() instanceof MinorStorageBagItem
                    || !moveItemStackTo(source, 0, BAG_SLOTS, false)) {
                return ItemStack.EMPTY;
            }
        }

        if (source.isEmpty()) {
            slot.setByPlayer(ItemStack.EMPTY);
        } else {
            slot.setChanged();
        }

        return copy;
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        bag.stopOpen(player);
        bag.setChanged();
    }
}
