package talloran.autosorter.custom.item;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import talloran.autosorter.Autosorter;
import talloran.autosorter.custom.block.ModBlock;

public class ModItems {

    private static void addItemToIngredientItemGroup(FabricItemGroupEntries entries){
        entries.add(ModBlock.AUTO_SORTER_STATE_BLOCK);
        entries.add(ModBlock.MAIN_AUTO_SORTER_STATE_BLOCK);
    }


    public static void registerModItem(){
        Autosorter.LOGGER.info("Registring Mod Item for " + Autosorter.MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.REDSTONE).register(ModItems::addItemToIngredientItemGroup);
    }
}
