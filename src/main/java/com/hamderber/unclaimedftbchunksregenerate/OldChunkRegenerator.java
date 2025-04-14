package com.hamderber.unclaimedftbchunksregenerate;

import com.hamderber.chunklibrary.ChunkRegenerator;
import com.hamderber.chunklibrary.data.ChunkAgeData;
import com.hamderber.chunklibrary.events.ChunkEvent;
import com.hamderber.chunklibrary.util.LevelHelper;
import com.hamderber.chunklibrary.util.TimeHelper;
import com.hamderber.unclaimedftbchunksregenerate.config.Config;

import dev.ftb.mods.ftbchunks.api.FTBChunksAPI;
import dev.ftb.mods.ftblibrary.math.ChunkDimPos;
import net.neoforged.bus.api.SubscribeEvent;

public class OldChunkRegenerator {
	@SubscribeEvent
	private void regenChunkIfOld(ChunkEvent.StartLoad event) {
		String dimensionID = LevelHelper.getDimensionID(event.level);
		if (!Config.isDimensionAllowed(dimensionID)) return;
		// Skip claimed chunks
		if (FTBChunksAPI.api().getManager().getChunk(new ChunkDimPos(LevelHelper.getDimensionKey(dimensionID), event.pos)) != null) return;
		
		long currentDay = TimeHelper.getCurrentDay(event.level);
		long lastGeneratedDay = ChunkAgeData.get(event.level).getLastGeneratedDay(event.level, event.pos);
		long age = currentDay - lastGeneratedDay;
		
		if (age >= Config.REGEN_PERIODS.get(dimensionID).getAsInt()) {
			UnclaimedFTBChunksRegenerate.LOGGER.debug("Chunk at " + event.pos.toString() + " has an age of " + age + 
					" days. Current day: " + currentDay + " Generated day: " + lastGeneratedDay);
			
			ChunkRegenerator.regenerateChunk(event.level, event.pos);
			
			ChunkAgeData.get(event.level).setLastGeneratedDay(event.level, event.pos, currentDay);
		}
	}
}
