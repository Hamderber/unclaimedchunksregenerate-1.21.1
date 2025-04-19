package com.hamderber.unclaimedchunksregenerate.config;

import java.util.HashMap;
import java.util.Map;

import com.hamderber.chunklibrary.config.ConfigAPI;

import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.ModConfigSpec.ConfigValue;
import net.neoforged.neoforge.common.ModConfigSpec.DoubleValue;
import net.neoforged.neoforge.common.ModConfigSpec.IntValue;


public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final Map<String, IntValue> DIMENSION_REGEN_PERIODS = new HashMap<>();
    public static final Map<String, IntValue> CLAIM_DISTANCE = new HashMap<>();

    public static final ModConfigSpec CONFIG;

    static {
        BUILDER.comment("Chunk Regeneration Settings")
            .push("regen_settings");
        
        ConfigAPI.CHUNK_SCAN_FLAGGING_CHANCE_MODULO = BUILDER
    	        .comment(" Factor impacting how likely a chunk is to be flagged for a scan of block changes. Bigger number is less scanning.")
    	        .defineInRange("chunkScanFlaggingModulo", 7, 1, 512); // default 7
        
        ConfigAPI.SKIP_CHUNK_SCAN_BELOW_TPS = BUILDER
    	        .comment(" Restricts the chunk scanning to be limited by server TPS. If the TPS is below this value, the chunk scan won't happen.")
    	        .defineInRange("skipScanBelowTPS", 15.0, 1, 20); // default 15
        
        ConfigAPI.TICKS_BETWEEN_CHUNK_SCAN_BATCH = BUILDER
    	        .comment(" Number of ticks to wait before each chunk scan.")
    	        .defineInRange("ticksBetweenChunkScanBatch", 100, 1, Integer.MAX_VALUE); // default 100
        
        ConfigAPI.MAX_CHUNK_SCANS_PER_BATCH = BUILDER
    	        .comment(" Number of chunks to scan per batch. This is the highest impact setting of them all!")
    	        .defineInRange("maxChunkScansPerBatch", 3, 1, Integer.MAX_VALUE); // default 3
        
        registerDimension("minecraft:overworld", 365, 3, 25, 0.5, true, true, false, true);
        registerDimension("minecraft:the_nether", 30, 1, 50, 0.8, true, true, false, true);
        registerDimension("minecraft:the_end", 30, 0, 10, 1.0, true, true, false, true);
        registerDimension("allthemodium:mining", 7, 50, 0, 0.3, true, true, false, true);
        registerDimension("allthemodium:the_other", 30, 1, 50, 0.8, true, true, false, true);
        registerDimension("allthemodium:the_beyond", 365, 5, 5, 1.0, true, true, false, true);
        registerDimension("lostcities:lostcity", 180, 2, 25, 0.5, true, true, false, true);
        registerDimension("twilightforest:twilight_forest", 180, 3, 25, 0.5, true, true, false, true);
        registerDimension("aether:the_aether", 30, 3, 25, 1.0, true, true, false, true);
        registerDimension("the_bumblezone:the_bumblezone", 30, 2, 25, 0.5, true, true, false, true);
        
        BUILDER.pop();
        CONFIG = BUILDER.build();
    }

    private static void registerDimension(String id, int days, int distanceFromClaim, int airDelta, double yPercent, boolean randomOre, boolean randomTree, boolean oreDisabled, boolean randomMob) {
    	IntValue regenPeriod = BUILDER
	        .comment(" How many tick-based game days before unclaimed chunks in " + id + " are eligible for regeneration", 
	        		" Using /time will NOT impact this. '/chunklibrary age..' will.")
	        .defineInRange(id + ".daysBetweenRegen", days, 1, Integer.MAX_VALUE);
    	
    	DIMENSION_REGEN_PERIODS.put(id, regenPeriod);
	    ConfigAPI.FEATURE_REGEN_PERIODS.put(id, regenPeriod);
    	
    	IntValue distance = BUILDER
    	        .comment(" The cubic radius around a claimed chunk in " + id + " where unclaimed chunks won't regenerate.")
    	        .defineInRange(id + ".distanceFromClaim", distanceFromClaim, 0, 10);
    	CLAIM_DISTANCE.put(id, distance);
	    

	     IntValue airDeltaSetting = BUILDER
            .comment(" The minimum +/- change in estimated air blocks for " + id + " required for a chunk to be elligble for regeneration.",
         		" Defaults are based on potential chunk changes during generation, such as water flowing from a spawned source "
         		+ "or vertical plants growing. ",
         		" Less than one (< 1) to disable this feature.")
    		.defineInRange(id + ".airDeltaAllowed", airDelta, Integer.MIN_VALUE, Integer.MAX_VALUE);
	     ConfigAPI.AIR_DELTA_ALLOWED.put(id, airDeltaSetting);
	     
	     DoubleValue percentOfChunkToScan = BUILDER
	             .comment(" The lower percent of the chunk to scan for changes. Note that most of a chunk is just air on top, so this value may be deceptively low.")
	     		.defineInRange(id + ".percentOfChunkToScan", yPercent, 0.01, 1.0);
	 	     ConfigAPI.DIMENSION_SCAN_FACTORS.put(id, percentOfChunkToScan);
	    
        ConfigAPI.ORE_DISABLED.put(id, BUILDER
            .comment(" Regenerated chunks in " + id + " will not have minecraft:ore features.")
            .define(id + ".oreDisabled", oreDisabled));

        ConfigAPI.RANDOM_ORE_ENABLED.put(id, BUILDER
            .comment(" Regenerated chunks in " + id + " will have their ores generated from a random seed.")
            .define(id + ".randomOreEnabled", randomOre));

        ConfigAPI.RANDOM_TREE_ENABLED.put(id, BUILDER
            .comment(" Regenerated chunks in " + id + " will have their trees generated from a random seed.")
            .define(id + ".randomTreeEnabled", randomTree));

        ConfigAPI.RANDOM_MOB_ENABLED.put(id, BUILDER
            .comment(" Regenerated chunks in " + id + " will have their initial entities generated from a random seed.",
            		" This will only change WHAT entity spawns, not WHERE the entity spawns.",
            		" Ex: a chunk first spawns a cow at x,y,z and then a horse on regeneration.")
            .define(id + ".randomMobEnabled", randomMob));
    }
    
    public static boolean isDimensionAllowed(String dimensionID) {
    	return DIMENSION_REGEN_PERIODS.containsKey(dimensionID);
    }
}

