package io.github.schntgaispock.gastronomicon.core.items.seeds;

import java.util.Map;

import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import io.github.schntgaispock.gastronomicon.util.GastroUtil;
import io.github.schntgaispock.gastronomicon.util.RecipeShapes;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import lombok.Getter;

public class CropGastroSeed extends SimpleGastroSeed {

    private final @Getter Map<ItemStack, Double> grownCrops;

    @ParametersAreNonnullByDefault
    public CropGastroSeed(SlimefunItemStack item, ItemStack[] harvestSources,
            Map<ItemStack, Double> grownCrops) {
        super(item, harvestSources);

        this.grownCrops = grownCrops;
    }

    @ParametersAreNonnullByDefault
    public CropGastroSeed(SlimefunItemStack item, ItemStack[] harvestSources,
            ItemStack grownCrop) {
        this(item, harvestSources, GastroUtil.toMap(grownCrop, 1.0));
    }

    @ParametersAreNonnullByDefault
    public CropGastroSeed(SlimefunItemStack item, SlimefunItemStack harvestSource) {
        this(item, RecipeShapes.singleCenter(harvestSource), harvestSource);
    }

    @Override
    public void onHarvest(BlockBreakEvent e, ItemStack item) {
        int sickleTier = 0;
        final Location location = e.getBlock().getLocation();
        final World world = location.getWorld();

        final SlimefunItem sfItem = SlimefunItem.getByItem(item);
        if (sfItem != null) {
            sickleTier = switch (sfItem.getId()) {
                case "WOODEN_SICKLE" -> 1;
                case "STEEL_SICKLE" -> 2;
                case "REINFORCED_SICKLE" -> 3;
                default -> 0;
            };
        }

        final double _fortune_factor = Math.sqrt(item.getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS) + 1);

        final ItemStack seed = this.getItem().clone();
        seed.setAmount(GastroUtil.randomRound((sickleTier + 1) * (_fortune_factor + 1) / 2));
        world.dropItemNaturally(location, seed);

        for (Map.Entry<ItemStack, Double> grownCropsEntry : getGrownCrops().entrySet()) {
            final ItemStack drop = grownCropsEntry.getKey().clone();
            drop.setAmount(GastroUtil.randomRound(grownCropsEntry.getValue() * _fortune_factor));
            world.dropItemNaturally(location, drop);
        }
    }
}
