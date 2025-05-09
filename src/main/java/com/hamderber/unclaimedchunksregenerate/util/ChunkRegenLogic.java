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
import com.minecolonies.api.util.ColonyUtils;
import xaero.pac.OpenPartiesAndClaims;
import xaero.pac.common.claims.player.IPlayerChunkClaim;
import xaero.pac.common.claims.player.PlayerChunkClaim;
import xaero.pac.common.server.api.OpenPACServerAPI;
import xaero.pac.common.server.claims.api.IServerClaimsManagerAPI;

public class ChunkRegenLogic {
    public static boolean isChunkClaimed(ServerLevel level, ResourceKey<Level> dimKey, ChunkPos chunkPos) {
        // Early exit and return true if no chunk claim mod is installed. Because claimed chunks are ignored,
        // this will prevent inadvertently resetting a world when none of the compatible options are included.
        if (!ModListener.chunkClaimModInstalled()) return true;

        boolean claimed = false;
        // Check if a chunk is claimed only if it hasn't yet been determined as 'yes' while only checking against
        // mods that are actually installed.
        if (ModListener.FTB_CHUNKS_INSTALLED) {
            claimed = FTBChunksAPI.api().getManager().getChunk(new ChunkDimPos(dimKey, chunkPos)) != null;
            if (claimed) {
                UnclaimedChunksRegenerate.LOGGER.info("Chunk at {} is claimed by {}", chunkPos, "FTB_CHUNKS");
            }
        }

        if (ModListener.MINECOLONIES_INSTALLED && !claimed) {
            claimed = IColonyManager.getInstance().getClaimData(dimKey, chunkPos) != null;
            if (claimed) {
                UnclaimedChunksRegenerate.LOGGER.info("Chunk at {} is claimed by {}", chunkPos, "MINECOLONIES");
            }
        }

        if (ModListener.OPEN_PARTIES_AND_CLAIMS_INSTALLED && !claimed) {
            claimed = OpenPACServerAPI
                    .get(level.getServer())
                    .getServerClaimsManager()
                    .get(dimKey.location(), chunkPos.x, chunkPos.z) != null;

            if (claimed) {
                UnclaimedChunksRegenerate.LOGGER.info("Chunk at {} is claimed by {}", chunkPos, "OPAC");
            }
        }

        return claimed;
    }

    public static boolean shouldRegenerate(ServerLevel level, String dimensionID, ChunkPos chunkPos) {
        ResourceKey<Level> dimKey = LevelHelper.getDimensionKey(dimensionID);

        if (isChunkClaimed(level, dimKey, chunkPos)) return false;

        boolean shouldRegenerate = ChunkData.get(level)
                .shouldResetChunk(level, chunkPos, Config.DIMENSION_REGEN_PERIODS.get(dimensionID).getAsInt());

        if (!shouldRegenerate) return false;

        ModConfigSpec.IntValue value = Config.CLAIM_DISTANCE.get(dimensionID);
        int distance = value != null ? value.get() : 10;
        int cx = chunkPos.x;
        int cz = chunkPos.z;

        for (int dx = -distance; dx <= distance; dx++) {
            for (int dz = -distance; dz <= distance; dz++) {
                ChunkPos nearby = new ChunkPos(cx + dx, cz + dz);
                if (isChunkClaimed(level, dimKey, nearby)) {
                    return false;
                }
            }
        }

        return true;
    }
}
