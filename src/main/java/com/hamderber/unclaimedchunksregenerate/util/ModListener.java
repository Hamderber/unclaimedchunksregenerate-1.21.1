package com.hamderber.unclaimedchunksregenerate.util;

import com.hamderber.unclaimedchunksregenerate.UnclaimedChunksRegenerate;
import net.neoforged.fml.ModList;

import java.util.Arrays;
import java.util.List;

public class ModListener {
    public static boolean FTB_CHUNKS_INSTALLED = false;
    private static final String FTB_CHUNKS_MODID = "ftbchunks";
    private static final String FTB_LIBRARY_MODID = "ftblibrary";

    public static boolean MINECOLONIES_INSTALLED = false;
    private static final String MINECOLONIES_MODID = "minecolonies";

    public static boolean OPEN_PARTIES_AND_CLAIMS_INSTALLED = false;
    private static final String OPEN_PARTIES_AND_CLAIMS_MODID = "openpartiesandclaims";

    public static final List<String> SUPPORTED_MODIDS = Arrays.asList(
        FTB_CHUNKS_MODID,
        MINECOLONIES_MODID,
        OPEN_PARTIES_AND_CLAIMS_MODID);

    public static void init() {
        ModList modList = ModList.get();
        FTB_CHUNKS_INSTALLED = modList.isLoaded(FTB_CHUNKS_MODID) && modList.isLoaded(FTB_LIBRARY_MODID);
        MINECOLONIES_INSTALLED = modList.isLoaded(MINECOLONIES_MODID);
        OPEN_PARTIES_AND_CLAIMS_INSTALLED = modList.isLoaded(OPEN_PARTIES_AND_CLAIMS_MODID);

        if (noChunkClaimModsInstalled()) {
            UnclaimedChunksRegenerate
                .LOGGER
                .warn("There are no chunk claiming mods installed! Supported mods: {}", SUPPORTED_MODIDS);
        }
      }

    public static boolean noChunkClaimModsInstalled() {
        return !FTB_CHUNKS_INSTALLED && !MINECOLONIES_INSTALLED && !OPEN_PARTIES_AND_CLAIMS_INSTALLED;
    }
}
