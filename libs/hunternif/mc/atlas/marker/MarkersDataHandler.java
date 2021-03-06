package hunternif.mc.atlas.marker;

import hunternif.mc.atlas.AntiqueAtlasMod;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent.ClientConnectedToServerEvent;

/**
 * Provides access to {@link MarkersData}. Maintains a cache on the client side,
 * because WorldClient is reset along with all WorldSavedData when the player
 * changes dimension (fixes #67).
 * @author Hunternif
 */
public class MarkersDataHandler {
	protected static final String MARKERS_DATA_PREFIX = "aaMarkers_";
	
	private final Map<String, MarkersData> markersDataClientCache = new ConcurrentHashMap<String, MarkersData>();
	
	/** Loads data for the given atlas or creates a new one. */
	public MarkersData getMarkersData(ItemStack stack, World world) {
		if (stack.getItem() == AntiqueAtlasMod.itemAtlas) {
			return getMarkersData(stack.getItemDamage(), world);
		} else {
			return null;
		}
	}
	
	/** Loads data for the given atlas ID or creates a new one. */
	public MarkersData getMarkersData(int atlasID, World world) {
		String key = getMarkersDataKey(atlasID);
		MarkersData data = null;
		if (world.isRemote) {
			// Since atlas data doesn't really belong to a single world-dimension,
			// it can be cached. This should fix #67
			data = markersDataClientCache.get(key);
		}
		if (data == null) {
			data = (MarkersData) world.loadItemData(MarkersData.class, key);
			if (data == null) {
				data = new MarkersData(key);
				world.setItemData(key, data);
			}
			if (world.isRemote) markersDataClientCache.put(key, data);
		}
		return data;
	}
	
	protected String getMarkersDataKey(int atlasID) {
		return MARKERS_DATA_PREFIX + atlasID;
	}
	
	/**
	 * This method resets the cache when the client loads a new world.
	 * It is required in order that old markers data is not
	 * transferred from a previous world the client visited.
	 * <p>
	 * Using a "connect" event instead of "disconnect" because according to a
	 * form post, the latter event isn't actually fired on the client.
	 * </p>
	 */
	@SubscribeEvent
	public void onClientConnectedToServer(ClientConnectedToServerEvent event) {
		markersDataClientCache.clear();
	}
}
