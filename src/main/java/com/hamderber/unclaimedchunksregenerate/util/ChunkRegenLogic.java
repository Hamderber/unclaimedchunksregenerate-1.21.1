package com.hamderber.unclaimedchunksregenerate.util;

import com.hamderber.chunklibrary.data.ChunkData;
import com.hamderber.chunklibrary.util.LevelHelper;
import com.hamderber.unclaimedchunksregenerate.UnclaimedChunksRegenerate;
import com.hamderber.unclaimedchunksregenerate.config.Config;
import com.minecolonies.api.colony.IColonyManager;
import dev.ftb.mods.ftbchunks.api.FTBChunksAPI;
import dev.ftb.mods.ftblibrary.math.ChunkDimPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.ModConfigSpec;
import xaero.pac.common.server.api.OpenPACServerAPI;

import java.util.ArrayList;
import java.util.List;

public class ChunkRegenLogic {
    public static boolean isChunkClaimed(ServerLevel level, ResourceKey<Level> dimKey, ChunkPos chunkPos) {
        // Early exit and return true if no chunk claim mod is installed. Because claimed chunks are ignored,
        // this will prevent inadvertently resetting a world when none of the compatible options are included.
        if (ModListener.noChunkClaimModsInstalled()) return true;

        List<String> claimedBy = new ArrayList<>();

        boolean claimed;

        if (ModListener.FTB_CHUNKS_INSTALLED) {
            claimed = FTBChunksAPI.api().getManager().getChunk(new ChunkDimPos(dimKey, chunkPos)) != null;
            if (claimed) {
                claimedBy.add("FTB CHUNKS");
            }
        }

        if (ModListener.MINECOLONIES_INSTALLED) {
            claimed = IColonyManager.getInstance().getClaimData(dimKey, chunkPos) != null;
            if (claimed) {
                claimedBy.add("MINECOLONIES");
            }
        }

        if (ModListener.OPEN_PARTIES_AND_CLAIMS_INSTALLED) {
            claimed = OpenPACServerAPI
                    .get(level.getServer())
                    .getServerClaimsManager()
                    .get(dimKey.location(), chunkPos.x, chunkPos.z) != null;

            if (claimed) {
                claimedBy.add("OPEN PARTIES AND CLAIMS");
            }
        }

        if (!claimedBy.isEmpty()) {
            debugMessage(level, chunkPos, claimedBy);
            return true;
        }
        else{
            return false;
        }
    }

    public static boolean shouldRegenerate(ServerLevel level, String dimensionID, ChunkPos chunkPos) {
        ResourceKey<Level> dimKey = LevelHelper.getDimensionKey(dimensionID);

        if (isChunkClaimed(level, dimKey, chunkPos)) return false;

        boolean shouldRegenerate = ChunkData.get(level)
                .shouldResetChunk(level, chunkPos, Config.DIMENSION_REGEN_PERIODS.get(dimensionID).getAsInt());

        if (!shouldRegenerate) return false;

        ModConfigSpec.IntValue value = Config.CLAIM_DISTANCE.get(dimensionID);
        int distance = value != null ? value.get() : Config.MAX_CLAIM_DISTANCE;

        for (int dx = -distance; dx <= distance; dx++) {
            for (int dz = -distance; dz <= distance; dz++) {
                ChunkPos nearby = new ChunkPos(chunkPos.x + dx, chunkPos.z + dz);
                if (isChunkClaimed(level, dimKey, nearby)) {
                    return false;
                }
            }
        }

        return true;
    }

    private static void debugMessage(ServerLevel level, ChunkPos chunkPos, List<String> claimedBy) {
        String claimedByString = claimedBy.toString();
        UnclaimedChunksRegenerate
            .LOGGER
            .debug("Chunk at {} in {} is claimed by {}", LevelHelper.getDimensionID(level), chunkPos, claimedByString);
    }
}
