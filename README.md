## Compressed Create Recipes

This mod implements a few change to Create or Create-like recipes for the Compression modpack  
Currently implemented:
- Void Conversion: Toss items in the void and the result floats back up.
- "compressed_create_recipes:void_proof" tag to make certain items always float back up
- Radiant Conversion: Toss items in an active beacon's beam. No, does not include the alternative method of eating light sources. 
- Changes to the Sequenced Assembly JEI category
  - Random salvage is not random salvage anymore, but is shown in detail underneath the recipe
  - A new "isProcessing" element can be added to sequenced assembly recipe jsons.
  - Recipes marked as "processing" indicate that there is no one "main" or "correct" output and instead display all outputs together at the bottom.
- Configs to change the basin's capacity and use speed of sandpaper
- (1.19.2 only) Backports a fix for a dupe bug with basins and buckets/tools that remain in the crafting grid

Disclaimer: This mod does not include Shadow Steel, Refined Radiance, Chromatic compound, or any other content related to them.
Compression Modpack: https://www.curseforge.com/minecraft/modpacks/compression  
Compression Discord: https://discord.gg/dz7UJ7sDmU

### Usage
Void Conversion
```{
 "type": "compressed_create_recipes:void_conversion",
 "input": {
    "item":"minecraft:iron_ingot"
    "count": 5
  },
 "output": {
    "item":"minecraft:gold_ingot"
    "count": 3
  }
}
```
Radiant Conversion (Level refers to the minimum beacon level to process the recipe)
```{
 "type": "compressed_create_recipes:radiant_conversion",
 "input": {
    "item":"minecraft:gold_ingot"
    "count": 10
  },
 "output": {
    "item":"minecraft:diamond"
    "count": 2
  },
  "level": 3
}
```
Void Proofing items  
*data/compressed_create_recipes/tags/items/void_proof.json*
```{
  "replace": false,
  "values": [
    "minecraft:nether_star",
    "minecraft:dragon_egg"
  ]
}
```
### FAQ: (Not really, but i feel like these might come up.)

- *Q: Will you support other Minecraft versions or loaders?*
  - A: No, this mod is made with the needs of the Compression modpack first. Feel free to open a pull request though.
- *Q: Will you make a standalone version with the chromatic materials?*
  - A: Maybe. If i get around to doing that, i'll put up a link here.