
Unclaimed Chunks Regenerate
=======

Chunks lacking claims regenerate with configurable dimensions and periodicities. The Overworld is defaulted to 365 Minecraft days and the End and Nether are at 30. When a chunk's age has surpassed the configured threshold, it is flagged for regeneration. The next time that a flagged chunk is loaded, its blocks are overwritten as if the chunk was just created for the first time. This means that structures, ores, loot chests, original entities (ex: cows and pigs), etc. will all be respawned. The underlying library mod supports randomized trees, ores, and animals during chunk regeneration. Those features are all configured through this mod.

Supported chunk claim mods:
- FTB Chunks 
- MineColonies
- Open Parties and Claims
 
KEY FEATURES:
- Chunks are reloaded as if brand new once they reach the configured age.
- Configurable range around claimed chunks to skip the regeneration process. Chunks actually claimed will never regenerate. 
- Random trees, ores, animals when a chunk is regenerated. 
- Protects entities from suffocation within a regenerated chunk for 100 ticks after it is re-created and teleports them to the next highest safe location. 
- The following dimensions are independently configurable:
  - Overworld 
  - Nether 
  - End 
  - ATM Mining Dimension 
  - ATM The Other 
  - ATM The Beyond 
  - The Twilight Forest 
  - The Bumblezone 
  - Lost Cities 
  - The Aether