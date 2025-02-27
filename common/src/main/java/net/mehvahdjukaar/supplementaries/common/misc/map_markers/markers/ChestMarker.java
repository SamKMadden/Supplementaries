package net.mehvahdjukaar.supplementaries.common.misc.map_markers.markers;

import net.mehvahdjukaar.moonlight.api.map.CustomMapDecoration;
import net.mehvahdjukaar.moonlight.api.map.markers.MapBlockMarker;
import net.mehvahdjukaar.supplementaries.common.misc.map_markers.ModMapMarkers;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.ChestBlock;

import org.jetbrains.annotations.Nullable;

public class ChestMarker extends MapBlockMarker<CustomMapDecoration> {

    public ChestMarker() {
        super(ModMapMarkers.CHEST_DECORATION_TYPE);
    }

    public ChestMarker(BlockPos pos) {
        super(ModMapMarkers.CHEST_DECORATION_TYPE, pos);
        this.setPos(pos);
    }

    @Nullable
    public static ChestMarker getFromWorld(BlockGetter world, BlockPos pos) {
        if (world.getBlockState(pos).getBlock() instanceof ChestBlock) {
            return new ChestMarker(pos);
        } else {
            return null;
        }
    }

    @Nullable
    @Override
    public CustomMapDecoration doCreateDecoration(byte mapX, byte mapY, byte rot) {
        return new CustomMapDecoration(this.getType(), mapX, mapY, rot, null);
    }
}
