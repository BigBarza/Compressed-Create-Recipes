package com.pression.compressedcreaterecipes.helpers;

import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModList;

import java.util.Optional;

//This is just a small thing to account for differences between Create 5.x and 6.x
public class VersionHelper {
    public static boolean isV6 = false;

    static {
        Optional<? extends ModContainer> createMod = ModList.get().getModContainerById("create");
        if(createMod.isPresent()) {
            int majorVersion = createMod.get().getModInfo().getVersion().getMajorVersion();
            isV6 = (majorVersion == 6);
        }
    }
}
