package io.github.artificialturtill.myriadascension.artifact;

import io.github.artificialturtill.myriadascension.menu.MinorStorageBagMenu;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.Level;

public final class MinorStorageBagItem extends Item implements ArtifactDescriptor {
    public static final int SLOT_COUNT = 9;

    public MinorStorageBagItem(Properties properties) {
        super(properties);
    }

    @Override
    public ArtifactType artifactType() {
        return ArtifactType.STORAGE;
    }

    @Override
    public ArtifactGrade artifactGrade() {
        return ArtifactGrade.MORTAL;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(
            Level level,
            Player player,
            InteractionHand hand) {

        ItemStack stack = player.getItemInHand(hand);

        if (level.isClientSide) {
            return InteractionResultHolder.success(stack);
        }

        if (!(player instanceof ServerPlayer serverPlayer)) {
            return InteractionResultHolder.pass(stack);
        }

        BagContainer bag = new BagContainer(stack);
        serverPlayer.openMenu(
                new SimpleMenuProvider(
                        (containerId, playerInventory, menuPlayer) ->
                                new MinorStorageBagMenu(containerId, playerInventory, bag),
                        Component.translatable("container.myriad_ascension.minor_storage_bag")),
                buffer -> {});

        return InteractionResultHolder.success(stack);
    }

    public static final class BagContainer extends SimpleContainer {
        private final ItemStack bagStack;
        private boolean loading;

        public BagContainer(ItemStack bagStack) {
            super(SLOT_COUNT);
            this.bagStack = bagStack;
            load();
        }

        private void load() {
            loading = true;

            ItemContainerContents contents = bagStack.getOrDefault(
                    DataComponents.CONTAINER,
                    ItemContainerContents.EMPTY);

            NonNullList<ItemStack> items =
                    NonNullList.withSize(SLOT_COUNT, ItemStack.EMPTY);
            contents.copyInto(items);

            for (int i = 0; i < SLOT_COUNT; i++) {
                super.setItem(i, items.get(i));
            }

            loading = false;
        }

        @Override
        public void setChanged() {
            super.setChanged();
            if (!loading) {
                save();
            }
        }

        private void save() {
            NonNullList<ItemStack> items =
                    NonNullList.withSize(SLOT_COUNT, ItemStack.EMPTY);

            for (int i = 0; i < SLOT_COUNT; i++) {
                items.set(i, getItem(i).copy());
            }

            bagStack.set(
                    DataComponents.CONTAINER,
                    ItemContainerContents.fromItems(items));
        }
    }
}
