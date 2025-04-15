package com.hamderber.unclaimedchunksregenerate;

import org.slf4j.Logger;

import com.hamderber.unclaimedchunksregenerate.config.Config;
import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.NeoForge;

@Mod(UnclaimedChunksRegenerate.MODID)
public class UnclaimedChunksRegenerate
{
    public static final String MODID = "unclaimedchunksregenerate";
    public static final Logger LOGGER = LogUtils.getLogger();
   
    public UnclaimedChunksRegenerate(IEventBus modEventBus, ModContainer modContainer)
    {
        LOGGER.info(MODID + " loaded!");
        NeoForge.EVENT_BUS.register(new OldChunkRegenerator());
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.CONFIG);
    }
}
