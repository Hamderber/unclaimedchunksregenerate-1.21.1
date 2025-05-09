package com.hamderber.unclaimedchunksregenerate;

import com.hamderber.chunklibrary.ChunkRegenerator;
import com.hamderber.chunklibrary.data.ChunkData;
import com.hamderber.chunklibrary.events.StartLoad;
import com.hamderber.chunklibrary.util.LevelHelper;
import com.hamderber.unclaimedchunksregenerate.config.Config;

import com.hamderber.unclaimedchunksregenerate.util.ChunkRegenLogic;
import net.neoforged.bus.api.SubscribeEvent;

public class OldChunkRegenerator {
	@SubscribeEvent
	public void regenChunkIfOld(StartLoad event) {
		String dimensionID = LevelHelper.getDimensionID(event.level);
		
		if (!Config.isDimensionAllowed(dimensionID)) return;

		if (ChunkRegenLogic.shouldRegenerate(event.level, dimensionID, event.pos)) {
			ChunkRegenerator.regenerateChunk(event.level, event.pos);
			
			ChunkData.get(event.level).setLastGeneratedDayNow(event.level, event.pos);
		}
	}
}
