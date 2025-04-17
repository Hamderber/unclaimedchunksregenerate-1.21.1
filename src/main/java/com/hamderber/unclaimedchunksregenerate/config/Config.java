package com.hamderber.unclaimedchunksregenerate.config;

import java.util.HashMap;
import java.util.Map;

import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.ModConfigSpec.IntValue;

import com.hamderber.chunklibrary.config.ConfigAPI;

public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final Map<String, IntValue> DIMENSION_REGEN_PERIODS = new HashMap<>();

    public static final ModConfigSpec CONFIG;

    static {
        BUILDER.comment("Chunk Regeneration Settings")
            .push("regen_settings");

        registerDimension("minecraft:overworld", 365, true, true, false);
        registerDimension("minecraft:the_nether", 30, true, true, false);
        registerDimension("minecraft:the_end", 30, true, true, false);

        BUILDER.pop();
        CONFIG = BUILDER.build();
    }

    private static void registerDimension(String id, int days, boolean randomOre, boolean randomTree, boolean oreDisabled) {
        DIMENSION_REGEN_PERIODS.put(id, BUILDER.defineInRange(id + ".daysBetweenRegen", days, 1, Integer.MAX_VALUE));
        ConfigAPI.FEATURE_REGEN_PERIODS.put(id, BUILDER.defineInRange(id + ".featureRegenPeriod", days, 1, Integer.MAX_VALUE));
        ConfigAPI.ORE_DISABLED.put(id, BUILDER.define(id + ".oreDisabled", oreDisabled));
        ConfigAPI.RANDOM_ORE_ENABLED.put(id, BUILDER.define(id + ".randomOreEnabled", randomOre));
        ConfigAPI.RANDOM_TREE_ENABLED.put(id, BUILDER.define(id + ".randomTreeEnabled", randomTree));
    }
    
    public static boolean isDimensionAllowed(String dimensionID) {
    	return DIMENSION_REGEN_PERIODS.containsKey(dimensionID);
    }
}

