package io.github.schntgaispock.gastronomicon.core.items.seeds;

import java.util.List;

import javax.annotation.ParametersAreNonnullByDefault;

import org.bukkit.block.Block;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import io.github.schntgaispock.gastronomicon.core.slimefun.GastroGroups;
import io.github.schntgaispock.gastronomicon.core.slimefun.GastroRecipes.GastroRecipeType;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItemStack;
import io.github.thebusybiscuit.slimefun4.core.handlers.BlockBreakHandler;

public abstract class AbstractGastroSeed extends SlimefunItem {

    @ParametersAreNonnullByDefault
    public AbstractGastroSeed(SlimefunItemStack item, ItemStack[] harvestSources) {
        super(GastroGroups.RAW_INGREDIENTS, item, GastroRecipeType.BREAK, harvestSources);
    }

    @Override
    public void preRegister() {
        addItemHandler((BlockBreakHandler) new BlockBreakHandler(false, false) {
            @Override
            public void onPlayerBreak(BlockBreakEvent e, ItemStack item, List<ItemStack> drops) {
                drops.clear();
                // TODO: Check if its fully grown first, probably in the AbstractGastroSeed.onHarvest() method instead
                onHarvest(e, item);
            }
        });
    }

    public abstract void onHarvest(BlockBreakEvent e, ItemStack item);

    public abstract boolean isMature(Block b);

    public abstract void tick(Block b);

    // TODO: Pull out sickle checking code

    // TODO: On right click, check for farmland, etc...

}
