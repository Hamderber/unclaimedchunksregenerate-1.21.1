package com.hamderber.unclaimedchunksregenerate;

import com.hamderber.chunklibrary.ChunkRegenerator;
import com.hamderber.chunklibrary.data.ChunkData;
import com.hamderber.chunklibrary.events.StartLoad;
import com.hamderber.chunklibrary.util.LevelHelper;
import com.hamderber.unclaimedchunksregenerate.config.Config;

import dev.ftb.mods.ftbchunks.api.FTBChunksAPI;
import dev.ftb.mods.ftblibrary.math.ChunkDimPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.ModConfigSpec.IntValue;

public class OldChunkRegenerator {
	@SubscribeEvent
	public void regenChunkIfOld(StartLoad event) {
		String dimensionID = LevelHelper.getDimensionID(event.level);
		
		if (!Config.isDimensionAllowed(dimensionID)) return;
		
		if (isChunkClaimed(LevelHelper.getDimensionKey(dimensionID), event.pos)) return;
		
		if (shouldRegenerate(dimensionID, event.pos)) {
			ChunkRegenerator.regenerateChunk(event.level, event.pos);
			
			ChunkData.get(event.level).setLastGeneratedDayNow(event.level, event.pos);
		}
	}
	
	private boolean isChunkClaimed(ResourceKey<Level> dimKey, ChunkPos chunkPos) {
		boolean claimed = FTBChunksAPI.api().getManager().getChunk(new ChunkDimPos(dimKey, chunkPos)) != null;
	    if (claimed) {
	        UnclaimedChunksRegenerate.LOGGER.debug("Claimed chunk detected: " + dimKey.location() + " @ " + chunkPos);
	    }
	    return claimed;
	}
	
	private boolean shouldRegenerate(String dimensionID, ChunkPos chunkPos) {
		ResourceKey<Level> dimKey = LevelHelper.getDimensionKey(dimensionID);
		
		if (isChunkClaimed(dimKey, chunkPos)) return false;

	    ServerLevel level = LevelHelper.getServerLevel(dimensionID);
	    boolean shouldRegenerate = ChunkData.get(level)
	        .shouldResetChunk(level, chunkPos, Config.DIMENSION_REGEN_PERIODS.get(dimensionID).getAsInt());

	    if (!shouldRegenerate) return false;

	    IntValue value = Config.CLAIM_DISTANCE.get(dimensionID);
	    int distance = value != null ? value.get() : 10;
	    int cx = chunkPos.x;
	    int cz = chunkPos.z;

	    for (int dx = -distance; dx <= distance; dx++) {
	        for (int dz = -distance; dz <= distance; dz++) {
	            ChunkPos nearby = new ChunkPos(cx + dx, cz + dz);
	            if (isChunkClaimed(dimKey, nearby)) {
	                return false;
	            }
	        }
	    }

	    return true;
	}
}
