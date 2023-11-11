package talloran.autosorter.screen;

import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.util.Identifier;
import talloran.autosorter.Autosorter;

public class ModScreenHandler {
    public static final ScreenHandlerType<AutoSorterScreenHandler> AUTO_SORTER_SCREEN_HANDLER =
            Registry.register(Registries.SCREEN_HANDLER, new Identifier(Autosorter.MOD_ID, "auto_sorter"),
                    new ExtendedScreenHandlerType<>(AutoSorterScreenHandler::new));

    public static void registryScreenHandler(){
        Autosorter.LOGGER.info("registryScreenHandler");
    }
}
