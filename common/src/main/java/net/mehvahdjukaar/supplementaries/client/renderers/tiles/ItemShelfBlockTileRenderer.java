package net.mehvahdjukaar.supplementaries.client.renderers.tiles;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.world.item.ItemDisplayContext;
import org.joml.Vector3f;
import net.mehvahdjukaar.supplementaries.common.block.tiles.ItemShelfBlockTile;
import net.mehvahdjukaar.supplementaries.common.utils.MiscUtils;
import net.mehvahdjukaar.supplementaries.configs.ClientConfigs;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;


public class ItemShelfBlockTileRenderer implements BlockEntityRenderer<ItemShelfBlockTile> {
    protected final ItemRenderer itemRenderer;

    public ItemShelfBlockTileRenderer(BlockEntityRendererProvider.Context context) {
        itemRenderer = Minecraft.getInstance().getItemRenderer();
    }

    protected boolean canRenderName(ItemShelfBlockTile tile) {
        if (Minecraft.renderNames() && tile.getItem(0).hasCustomHoverName()) {
            double d0 = Minecraft.getInstance().getEntityRenderDispatcher().distanceToSqr(tile.getBlockPos().getX() + 0.5, tile.getBlockPos().getY() + 0.5, tile.getBlockPos().getZ() + 0.5);
            return d0 < 16;
        }
        return false;
    }

    @Override
    public void render(ItemShelfBlockTile tile, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int combinedLightIn,
                       int combinedOverlayIn) {

        if (!tile.isEmpty()) {

            matrixStackIn.pushPose();
            matrixStackIn.translate(0.5, 0.5, 0.5);
            matrixStackIn.scale(0.5f, 0.5f, 0.5f);
            float yaw = tile.getYaw();
            matrixStackIn.mulPose(Axis.YP.rotationDegrees(yaw));
            matrixStackIn.translate(0, 0, 0.8125);

            if (this.canRenderName(tile)) {
                matrixStackIn.pushPose();
                matrixStackIn.mulPose(Axis.YP.rotationDegrees(-yaw));
                Component name = tile.getItem(0).getHoverName();

                PedestalBlockTileRenderer.renderName(name,0.625f, matrixStackIn, bufferIn, combinedLightIn);
                matrixStackIn.popPose();
            }

            ItemStack stack = tile.getDisplayedItem();
            if (MiscUtils.FESTIVITY.isAprilsFool()) stack = new ItemStack(Items.SALMON);
            BakedModel model = itemRenderer.getModel(stack, tile.getLevel(), null, 0);
            if (model.usesBlockLight() && ClientConfigs.Blocks.SHELF_TRANSLATE.get()) matrixStackIn.translate(0, -0.25, 0);


            itemRenderer.render(stack, ItemDisplayContext.FIXED, true, matrixStackIn,
                    bufferIn, combinedLightIn, combinedOverlayIn, model);

            matrixStackIn.popPose();
        }

    }

}