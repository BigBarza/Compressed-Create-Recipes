## Compressed Mysterious Conversions

This mod implements a few "Mysterious Conversion" recipe types for the Compression modpack  
Currently implemented:
- Void Conversion: Toss items in the void and the result floats back up.
- "compressedmystconv:void_proof" tag to make certain items always float back up
- Radiant Conversion: Toss items in an active beacon's beam. No, does not include the alternative method of eating light sources.  

This mod does not include any items.  
Note: This mod soft-depends on Create for the Mysterious Conversion JEI category. It will function without Create, but the recipes will not show up in JEI.

Compression modpack: https://www.curseforge.com/minecraft/modpacks/compression  
Compression Discord: https://discord.gg/dz7UJ7sDmU

### Usage
Void Conversion
```{
 "type": "compressedmystconv:void_conversion",
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
 "type": "compressedmystconv:radiant_conversion",
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
*data/compressedmystconv/tags/items/void_proof.json*
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