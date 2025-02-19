package net.satisfy.nethervinery.client;

import dev.architectury.registry.client.rendering.ColorHandlerRegistry;
import dev.architectury.registry.client.rendering.RenderTypeRegistry;
import dev.architectury.registry.menu.MenuRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.RenderType;
import net.satisfy.nethervinery.client.gui.AgingBarrelGui;
import net.satisfy.nethervinery.client.gui.ApplePressGui;
import net.satisfy.nethervinery.registry.NetherObjectRegistry;
import net.satisfy.nethervinery.registry.NetherScreenHandlerTypes;

@Environment(EnvType.CLIENT)
public class NetherVineryClient {

    public static void onInitializeClient() {

        RenderTypeRegistry.register(RenderType.cutout(),
                NetherObjectRegistry.BLAZEWINE_PINOT.get(), NetherObjectRegistry.NETHER_FIZZ.get(), NetherObjectRegistry.GHASTLY_GRENACHE.get(),
                NetherObjectRegistry.IMPROVED_LAVA_FIZZ.get(), NetherObjectRegistry.LAVA_FIZZ.get(), NetherObjectRegistry.IMPROVED_LAVA_FIZZ.get(),
                NetherObjectRegistry.NETHERITE_NECTAR.get(), NetherObjectRegistry.CRIMSON_WINE_RACK_MID.get(), NetherObjectRegistry.WARPED_GRAPE_BUSH.get(),
                NetherObjectRegistry.WARPED_WINE_RACK_MID.get(), NetherObjectRegistry.CRIMSON_GRAPE_BUSH.get(), NetherObjectRegistry.IMPROVED_NETHER_FIZZ.get(),
                NetherObjectRegistry.OBSIDIAN_STEM.get(), NetherObjectRegistry.WARPED_LATTICE.get(), NetherObjectRegistry.CRIMSON_LATTICE.get()
                );

        ColorHandlerRegistry.registerBlockColors((state, world, pos, tintIndex)->{
                    if(world== null || pos == null){
                        return -1;
                    }
                    return BiomeColors.getAverageFoliageColor(world,pos);
                }, NetherObjectRegistry.OBSIDIAN_STEM.get()
        );

        MenuRegistry.registerScreenFactory(NetherScreenHandlerTypes.APPLE_PRESS_GUI_HANDLER.get(), ApplePressGui::new);
        MenuRegistry.registerScreenFactory(NetherScreenHandlerTypes.FERMENTATION_BARREL_GUI_HANDLER.get(), AgingBarrelGui::new);
    }
}