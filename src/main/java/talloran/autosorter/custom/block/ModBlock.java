package talloran.autosorter.custom.block;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import talloran.autosorter.Autosorter;

public class ModBlock {
    public static final Block AUTO_SORTER_STATE_BLOCK = registerBlock("auto_sorter_state_block", new AutoSorterStationBlock(FabricBlockSettings.copyOf(Blocks.SPRUCE_PLANKS).sounds(BlockSoundGroup.STONE)));
    public static final Block MAIN_AUTO_SORTER_STATE_BLOCK = registerBlock("main_auto_sorter_state_block", new MainAutoSorterStationBlock(FabricBlockSettings.copyOf(Blocks.SPRUCE_PLANKS).sounds(BlockSoundGroup.STONE)));

    private static Block registerBlock(String name, Block block){
        registerBlockItem(name, block);
        return Registry.register(Registries.BLOCK, new Identifier(Autosorter.MOD_ID, name), block);
    }
    private static Item registerBlockItem(String name, Block block){
        return Registry.register(Registries.ITEM, new Identifier(Autosorter.MOD_ID, name), new BlockItem(block, new FabricItemSettings()));
    }
    public static void RegisterModBlock(){
        Autosorter.LOGGER.info("Registering Mod Block for " + Autosorter.MOD_ID);
    }
}
