package com.pression.compressedmystconv;

import com.mojang.logging.LogUtils;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(CompressedMystConv.MODID)
public class CompressedMystConv
{
    public static final String MODID = "compressedmystconv";
    private static final Logger LOGGER = LogUtils.getLogger();
    public CompressedMystConv(){
        LOGGER.info("Hexagons are the bestagons!");
    }
}