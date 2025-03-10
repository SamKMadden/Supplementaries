package net.mehvahdjukaar.supplementaries.common.utils;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.mehvahdjukaar.moonlight.api.item.additional_placements.AdditionalItemPlacement;
import net.mehvahdjukaar.moonlight.api.item.additional_placements.AdditionalItemPlacementsAPI;
import net.mehvahdjukaar.moonlight.api.item.additional_placements.BlockPlacerItem;
import net.mehvahdjukaar.moonlight.core.Moonlight;
import net.mehvahdjukaar.supplementaries.common.block.tiles.KeyLockableTile;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class ItemsUtil {

    //placeable item stuff

    @Nullable
    public static BlockState getPlacementState(BlockPlaceContext context, Block block) {
        return BlockPlacerItem.get().mimicGetPlacementState(context, block);
    }

    public static InteractionResult place(BlockPlaceContext context, Block blockToPlace) {
        return place(context, blockToPlace, null);
    }

    public static InteractionResult place(BlockPlaceContext context, Block blockToPlace, @Nullable SoundType placeSound) {
        return BlockPlacerItem.get().mimicPlace(context, blockToPlace, placeSound);
    }

    //helper for slingshot. Calls both block item and this in case it as additional behavior
    public static InteractionResult place(Item item, BlockPlaceContext pContext) {
        //this also calls mixin
        //TODO: find better way to do
        if (item instanceof BlockItem bi) return bi.place(pContext);
        var placement = AdditionalItemPlacementsAPI.getBehavior(item);
         if(placement != null) return placement.overridePlace(pContext);
        return InteractionResult.PASS;
    }

    public static void addStackToExisting(Player player, ItemStack stack, boolean avoidHands) {
        var inv = player.getInventory();
        boolean added = false;
        for (int j = 0; j < inv.items.size(); j++) {
            if (inv.getItem(j).is(stack.getItem()) && inv.add(j, stack)) {
                added = true;
                break;
            }
        }
        if (avoidHands && !added) {
            for (int j = 0; j < inv.items.size(); j++) {
                if (inv.getItem(j).isEmpty() && j != inv.selected && inv.add(j, stack)) {
                    added = true;
                    break;
                }
            }
        }
        if (!added && inv.add(stack)) {
            player.drop(stack, false);
        }
    }

    public static boolean tryInteractingWithContainerItem(ItemStack containerStack, ItemStack incoming, Slot slot, ClickAction action, Player player, boolean inSlot) {
        if (action != ClickAction.PRIMARY) {
            //drop content in empty slot
            if (incoming.isEmpty()) {
                if (!inSlot) {
                    return ItemsUtil.extractFromContainerItemIntoSlot(player, containerStack, slot);
                }
            } else if (ItemsUtil.addToContainerItem(player, containerStack, incoming, slot, true, inSlot)) {
                return ItemsUtil.addToContainerItem(player, containerStack, incoming, slot, false, inSlot);
            }
        }
        return false;
    }


    @ExpectPlatform
    public static boolean addToContainerItem(Player player, ItemStack containerStack, ItemStack stack, Slot slot, boolean simulate, boolean inSlot) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static boolean extractFromContainerItemIntoSlot(Player player, ItemStack containerStack, Slot slot) {
        throw new AssertionError();
    }


    @Nullable
    public static BlockEntity loadBlockEntityFromItem(CompoundTag tag, Item item) {
        if (item instanceof BlockItem blockItem) {
            Block block = blockItem.getBlock();
            if (block instanceof EntityBlock entityBlock) {
                BlockEntity te = entityBlock.newBlockEntity(BlockPos.ZERO, block.defaultBlockState());
                if (te != null) te.load(tag);
                return te;
            }
        }
        return null;
    }

    @ExpectPlatform
    public static float getEncumbermentFromInventory(ItemStack stack, ServerPlayer player) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static KeyLockableTile.KeyStatus hasKeyInInventory(Player player, String key) {
        throw new AssertionError();
    }


    //TODO: move to lib
    @ExpectPlatform
    public static ItemStack tryExtractingItem(Level level, @Nullable Direction dir, Object container) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static ItemStack tryAddingItem(ItemStack stack, Level level, @Nullable Direction direction, Object container) {
        throw new AssertionError();
    }

}
