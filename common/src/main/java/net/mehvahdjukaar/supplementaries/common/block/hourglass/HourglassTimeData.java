package net.mehvahdjukaar.supplementaries.common.block.hourglass;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;


public class HourglassTimeData {

    public static final HourglassTimeData EMPTY = new HourglassTimeData(HolderSet.direct(), 0, 0, Optional.empty(), 99);

    public static final Codec<HourglassTimeData> REGISTRY_CODEC = RecordCodecBuilder.create(instance -> instance.group(
            RegistryCodecs.homogeneousList(Registries.ITEM).fieldOf("items").forGetter(p -> p.dusts),
            ExtraCodecs.POSITIVE_INT.fieldOf("duration").forGetter(p -> p.duration),
            Codec.intRange(0, 15).optionalFieldOf("light_level", 0).forGetter(p -> p.light),
            ResourceLocation.CODEC.optionalFieldOf("texture").forGetter(p -> p.texture),
            ExtraCodecs.POSITIVE_INT.optionalFieldOf("ordering", 0).forGetter(p -> p.ordering)
    ).apply(instance, HourglassTimeData::new));

    public static final Codec<HourglassTimeData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ResourceLocation.CODEC.listOf().fieldOf("items").forGetter(p -> p.dusts.stream().map(h -> h.unwrapKey().get().location()).toList()),
            ExtraCodecs.POSITIVE_INT.fieldOf("duration").forGetter(p -> p.duration),
            Codec.intRange(0, 15).optionalFieldOf("light_level", 0).forGetter(p -> p.light),
            ResourceLocation.CODEC.optionalFieldOf("texture").forGetter(p -> p.texture),
            ExtraCodecs.POSITIVE_INT.optionalFieldOf("ordering", 0).forGetter(p -> p.ordering)
    ).apply(instance, HourglassTimeData::createSafe));

    private static HourglassTimeData createSafe(List<ResourceLocation> dusts, int dur, int light, Optional<ResourceLocation> texture, int order) {
        List<Holder<Item>> holders = new ArrayList<>();
        dusts.forEach(r -> BuiltInRegistries.ITEM.getHolder(ResourceKey.create(Registries.ITEM, r)).ifPresent(holders::add));
        return new HourglassTimeData(HolderSet.direct(holders), dur, light, texture, order);
    }

    private final HolderSet<Item> dusts;
    private final int duration;
    private final int light;
    private final Optional<ResourceLocation> texture;
    private final int ordering;

    public HourglassTimeData(HolderSet<Item> owner, int duration, int light, Optional<ResourceLocation> texture, int priority) {
        this.dusts = owner;
        this.duration = duration;
        this.light = light;
        this.texture = texture;
        this.ordering = priority;
    }

    public ResourceLocation computeTexture(ItemStack i, Level world) {
        Minecraft mc = Minecraft.getInstance();
        if (this.texture.isEmpty()) {
            ItemRenderer itemRenderer = mc.getItemRenderer();
            BakedModel model = itemRenderer.getModel(i, world, null, 0);
            return model.getParticleIcon().contents().name();

        }
        return this.texture.get();
    }

    public Stream<Holder<Item>> getItems() {
        return dusts.stream();
    }

    public int getLight() {
        return light;
    }

    public int getOrdering() {
        return ordering;
    }

    public boolean isEmpty() {
        return this == EMPTY;
    }

    public float getIncrement() {
        return 1f / duration;
    }
}
