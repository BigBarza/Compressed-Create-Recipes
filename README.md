## Compressed Engineering

This mod contains tweaks to Immersive Engineering for the Compression modpack.
Currently, it allows the coke oven to have custom fluid outputs instead of just the hardcoded creosote oil. It adds no recipes on its own.

Curseforge link: https://legacy.curseforge.com/minecraft/mc-mods/compressed-engineering

Compression modpack: https://www.curseforge.com/minecraft/modpacks/compression

Compression Discord: https://discord.gg/dz7UJ7sDmU

### Usage
```
{
 "type": "immersiveengineering:coke_oven",
 "fluid": {
    "amount":1000,
    "fluid":"immersiveengineering:ethanol"
  },
 "input": {
    "item":"minecraft:gold_ingot"
  },
 "result": {
    "item":"minecraft:diamond"
  },
 "time": 100
}
```
### FAQ: (Not really, but i feel like these might come up.)

- *Q: Will you support other Minecraft versions or loaders?*
  - A: No, this mod is made with the needs of the Compression modpack first. Feel free to open a pull request though.

- *Q: What happens to the default recipes?*
  - A: Nothing. If there's no custom fluid but a creosote value, the coke oven will output creosote oil as usual.

- *Q: What happens if there's a different fluid in the output?*
  - A: The recipe simply won't start.

- *Q: These mixins look cursed.*
  - A: Mixins are like dark and forbidden magic. Nothing to be concerned about
