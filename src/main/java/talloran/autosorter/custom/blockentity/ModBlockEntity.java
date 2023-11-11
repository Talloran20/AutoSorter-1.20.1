package talloran.autosorter.custom.blockentity;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import talloran.autosorter.Autosorter;
import talloran.autosorter.custom.block.ModBlock;

public class ModBlockEntity {
    public static final BlockEntityType<AutoSorterBlockEntity> AUTO_SORTER_BLOCK_ENTITY =
            Registry.register(Registries.BLOCK_ENTITY_TYPE, new Identifier(Autosorter.MOD_ID, "auto_sorter_entity"),
                    FabricBlockEntityTypeBuilder.create(AutoSorterBlockEntity::new,
                            ModBlock.AUTO_SORTER_STATE_BLOCK).build());

    public static final BlockEntityType<MainAutoSorterBlockEntity> MAIN_AUTO_SORTER_BLOCK_ENTITY =
            Registry.register(Registries.BLOCK_ENTITY_TYPE, new Identifier(Autosorter.MOD_ID, "main_auto_sorter_entity"),
                    FabricBlockEntityTypeBuilder.create(MainAutoSorterBlockEntity::new,
                            ModBlock.MAIN_AUTO_SORTER_STATE_BLOCK).build());

    public static void registryBlockEntity(){
        Autosorter.LOGGER.info("registryBlockEntity");
    }


}
