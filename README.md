## Compressed Mysterious Conversions

This mod implements a few "Mysterious Conversion" recipe types for the Compression modpack  
Currently implemented:
- Void Conversion: Toss items in the void and the result floats back up.
- "compressedmystconv:void_proof" tag to make certain items always float back up
- Radiant Conversion: Toss items in an active beacon's beam. No, does not include the alternative method of eating light sources.  

This mod does not include any items.  

Compression modpack: https://www.curseforge.com/minecraft/modpacks/compression

Compression Discord: https://discord.gg/dz7UJ7sDmU

### Usage
```
{
 "type": "compressedmystconv:void_conversion",
 "input": {
    "item":"minecraft:gold_ingot"
    "count": 10
  },
 "output": {
    "item":"minecraft:diamond"
    "count": 2
  }
}
```
### FAQ: (Not really, but i feel like these might come up.)

- *Q: Will you support other Minecraft versions or loaders?*
  - A: No, this mod is made with the needs of the Compression modpack first. Feel free to open a pull request though.
- *Q: Will you make a standalone version with the chromatic materials?*
  - A: Maybe. If i get around to doing that, i'll put up a link here.