package com.hamderber.unclaimedftbchunksregenerate.config;

import java.util.HashMap;
import java.util.Map;

import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.ModConfigSpec.IntValue;

public class Config {
	public static final Map<String, IntValue> REGEN_PERIODS = new HashMap<>();
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static ModConfigSpec CONFIG;

    static {
        BUILDER.comment("Unclaimed Chunk Regeneration Settings")
            .push("regen");

        addRegenDimension("minecraft:overworld", 365);
        addRegenDimension("minecraft:the_nether", 30);
        addRegenDimension("minecraft:the_end", 30);

        BUILDER.pop();
        CONFIG = BUILDER.build();
    }

    private static void addRegenDimension(String dimensionID, int defaultDays) {
        IntValue value = BUILDER.comment("Days between regen for dimension: " + dimensionID)
            .defineInRange(dimensionID + ".daysBetweenRegen", defaultDays, 1, Integer.MAX_VALUE);
        REGEN_PERIODS.put(dimensionID, value);
    }

    public static int getDaysBetween(String dimensionID) {
        IntValue val = REGEN_PERIODS.get(dimensionID);
        return val != null ? val.get() : -1;
    }
    
    public static boolean isDimensionAllowed(String dimensionID) {
    	return REGEN_PERIODS.containsKey(dimensionID);
    }
}
