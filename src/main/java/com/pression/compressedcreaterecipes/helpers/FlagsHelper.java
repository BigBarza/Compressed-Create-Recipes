package com.pression.compressedcreaterecipes.helpers;

import com.pression.compressedcreaterecipes.ClientConfig;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModList;

import java.util.Optional;

//This is just a small thing to account for differences between Create 5.x and 6.x
public class FlagsHelper {
    public static boolean enableSalvage = false;

    static {
        //Compat with Create: Cyber Goggles. Disable the salvage display if that mod is present
        Optional<? extends ModContainer> cyberGogglesMod = ModList.get().getModContainerById("create_cyber_goggles");
        if(!cyberGogglesMod.isPresent() && ClientConfig.ENABLE_SALVAGE_DISPLAY.get()) enableSalvage = true;
    }
}
