package com.hamderber.unclaimedchunksregenerate;

import com.hamderber.chunklibrary.ChunkRegenerator;
import com.hamderber.chunklibrary.data.ChunkData;
import com.hamderber.chunklibrary.events.StartLoad;
import com.hamderber.chunklibrary.util.LevelHelper;
import com.hamderber.unclaimedchunksregenerate.config.Config;

import dev.ftb.mods.ftbchunks.api.FTBChunksAPI;
import dev.ftb.mods.ftblibrary.math.ChunkDimPos;
import net.neoforged.bus.api.SubscribeEvent;

public class OldChunkRegenerator {
	@SubscribeEvent
	public void regenChunkIfOld(StartLoad event) {
		String dimensionID = LevelHelper.getDimensionID(event.level);
		
		if (!Config.isDimensionAllowed(dimensionID)) return;
		
		// Skip claimed chunks. Null is returned when the chunk isnt claimed
		if (FTBChunksAPI.api().getManager().getChunk(new ChunkDimPos(LevelHelper.getDimensionKey(dimensionID), event.pos)) != null) return;
		
		if (ChunkData.get(event.level).shouldResetChunk(event.level, event.pos, Config.DIMENSION_REGEN_PERIODS.get(dimensionID).getAsInt())) {
			
			ChunkRegenerator.regenerateChunk(event.level, event.pos);
			
			ChunkData.get(event.level).setLastGeneratedDayNow(event.level, event.pos);
		}
	}
}
