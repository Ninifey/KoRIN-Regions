package hunternif.mc.atlas.api;

import hunternif.mc.atlas.client.TextureSet;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * API for biome tiles and custom tiles (i.e. dungeons, towns etc.)
 * Texture methods are client side only. Consider registering your own
 * texture set with a unique name: see {@link #registerTextureSet()}
 * @author Hunternf
 */
public interface TileAPI {
	/** Version of Tile API, meaning this particular interface. */
	public static final int VERSION = 5;
	
	/**
	 * Register a texture set with the specified unique name and texture files.
	 * The different textures in the set will be added as variations, and only
	 * the name of the texture set will be saved in the biome/tile config file.
	 * @param	name
	 * @param	textures
	 * @return	the registered texture set
	 */
	@SideOnly(Side.CLIENT)
	TextureSet registerTextureSet(String name, ResourceLocation ... textures);
	
	
	// Biome textures ==========================================================
	
	/**
	 * Assign one or more texture to biome ID, creating a new texture set.
	 * See {@link #registerTextureSet()}
	 */
	@SideOnly(Side.CLIENT)
	void setBiomeTexture(int biomeID, String textureSetName, ResourceLocation ... textures);
	
	/**
	 * Assign one or more texture to biome ID, creating a new texture set.
	 * See {@link #registerTextureSet()}
	 */
	@SideOnly(Side.CLIENT)
	void setBiomeTexture(BiomeGenBase biome, String textureSetName, ResourceLocation ... textures);
	
	/**
	 * Assign one or more texture to biome ID, using an existing texture set.
	 * See {@link #registerTextureSet()}
	 */
	@SideOnly(Side.CLIENT)
	void setBiomeTexture(int biomeID, TextureSet textureSet);
	
	/**
	 * Assign one or more texture to biome ID, using an existing texture set.
	 * See {@link #registerTextureSet()}
	 */
	@SideOnly(Side.CLIENT)
	void setBiomeTexture(BiomeGenBase biome, TextureSet textureSet);
	
	
	// Custom tile textures ====================================================
	
	/**
	 * Assign one or more textures to a unique tile name.
	 * This creates a new texture set with the same name as the tile.
	 * See {@link #registerTextureSet()}
	 */
	@SideOnly(Side.CLIENT)
	void setCustomTileTexture(String uniqueTileName, ResourceLocation ... textures);
	
	/** Assign a texture set to a unique tile name. */
	@SideOnly(Side.CLIENT)
	void setCustomTileTexture(String uniqueTileName, TextureSet textureSet);
	
	
	// Biome tiles =============================================================
	
	/**
	 * Edit the biome ID at the specified chunk in the specified atlas. 
	 * You only need to call this method once for every chunk, after that
	 * the tile will be persisted with the world and loaded when the server
	 * starts up.
	 * <p>
	 * Note that global custom tiles, such as village territory, will override
	 * biome IDs at shared chunks.
	 * </p>
	 * <p>
	 * If calling this method on the client, the player must carry the atlas
	 * in his inventory, to prevent griefing!
	 * </p>
	 * <p>
	 * For setting custom tiles that don't correspond to biomes, see
	 * {@link #putCustomTile}
	 * </p>
	 * @param world		dimension the chunk is located in.
	 * @param atlasID	the ID of the atlas you want to put marker in. Equal
	 * 					to ItemStack damage for ItemAtlas.
	 * @param biomeID	
	 * @param chunkX	x chunk coordinate. (block coordinate >> 4)
	 * @param chunkZ	z chunk coordinate. (block coordinate >> 4)
	 */
	void putBiomeTile(World world, int atlasID, int biomeID, int chunkX, int chunkZ);
	
	/**
	 * Edit the biome at the specified chunk in the specified atlas. 
	 * You only need to call this method once for every chunk, after that
	 * the tile will be persisted with the world and loaded when the server
	 * starts up.
	 * <p>
	 * Note that global custom tiles, such as village territory, will override
	 * biome IDs at shared chunks.
	 * </p>
	 * <p>
	 * If calling this method on the client, the player must carry the atlas
	 * in his inventory, to prevent griefing!
	 * </p>
	 * <p>
	 * For setting custom tiles that don't correspond to biomes, see
	 * {@link #putCustomTile}
	 * </p>
	 * @param world		dimension the chunk is located in.
	 * @param atlasID	the ID of the atlas you want to put marker in. Equal
	 * 					to ItemStack damage for ItemAtlas.
	 * @param biome	
	 * @param chunkX	x chunk coordinate. (block coordinate >> 4)
	 * @param chunkZ	z chunk coordinate. (block coordinate >> 4)
	 */
	void putBiomeTile(World world, int atlasID, BiomeGenBase biome, int chunkX, int chunkZ);
	
	
	// Custom tiles ============================================================
	
	/**
	 * Put the specified custom tile at the specified chunk coordinates
	 * in the specified atlas.
	 * You only need to call this method once for every chunk, after that
	 * the tile will be persisted with the world and loaded when the server
	 * starts up.
	 * <p>
	 * Note that global custom tiles, such as village territory, will override
	 * tiles local to atlas at shared chunks.
	 * </p>
	 * <p>
	 * If calling this method on the client, the player must carry the atlas
	 * in his inventory, to prevent griefing!
	 * </p>
	 * <p>
	 * For custom biomes or for altering biomes at specific chunks, see
	 * {@link #putBiomeTile}
	 * </p>
	 * 
	 * @param world		dimension the chunk is located in.
	 * @param atlasID	the ID of the atlas you want to put marker in. Equal
	 * 					to ItemStack damage for ItemAtlas.
	 * @param tileName	the unique name for your tile type. You must use the
	 * 					same when registering the texture.
	 * @param chunkX	x chunk coordinate. (block coordinate >> 4)
	 * @param chunkZ	z chunk coordinate. (block coordinate >> 4)
	 */
	void putCustomTile(World world, int atlasID, String tileName, int chunkX, int chunkZ);
	
	/**
	 * Put the specified custom tile at the specified chunk coordinates
	 * globally i.e. in every atlas. Therefore this method has to be called
	 * on the <b>server</b> only!
	 * You only need to call this method once for every chunk, after that
	 * the tile will be persisted with the world and loaded when the server
	 * starts up.
	 * 
	 * @param world		dimension the chunk is located in.
	 * @param tileName	the unique name for your tile type. You must use the
	 * 					same when registering the texture.
	 * @param chunkX	x chunk coordinate. (block coordinate >> 4)
	 * @param chunkZ	z chunk coordinate. (block coordinate >> 4)
	 */
	void putCustomGlobalTile(World world, String tileName, int chunkX, int chunkZ);
	
	/**
	 * Delete the global tile at the specified chunk coordinates if a tile has
	 * been previously put there by {@link #putCustomGlobalTile()}.
	 * This method has to be called on the <b>server</b> only!
	 * 
	 * @param world		dimension the chunk is located in.
	 * @param chunkX	x chunk coordinate. (block coordinate >> 4)
	 * @param chunkZ	z chunk coordinate. (block coordinate >> 4)
	 */
	void deleteCustomGlobalTile(World world, int chunkX, int chunkZ);
	//TODO: make it possible to delete local custom tiles as well.
}
