package talloran.autosorter;

import net.fabricmc.api.ModInitializer;

import net.minecraft.client.gui.screen.ingame.HandledScreens;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import talloran.autosorter.custom.block.ModBlock;
import talloran.autosorter.custom.blockentity.ModBlockEntity;
import talloran.autosorter.custom.item.ModItems;
import talloran.autosorter.screen.AutoSorterScreen;
import talloran.autosorter.screen.ModScreenHandler;

public class Autosorter implements ModInitializer {
	public static final String MOD_ID = "autosorter";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModItems.registerModItem();
		ModBlock.RegisterModBlock();
		ModBlockEntity.registryBlockEntity();
		ModScreenHandler.registryScreenHandler();


		HandledScreens.register(ModScreenHandler.AUTO_SORTER_SCREEN_HANDLER, AutoSorterScreen::new);

		LOGGER.info("AUTO_SORTER_MOD on Initialize");
	}
}