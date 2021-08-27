package de.rinonline.korinrpg.Helper.Gui;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import de.rinonline.korinrpg.ConfigurationMoD;
import de.rinonline.korinrpg.Network.PacketDispatcher;
import de.rinonline.korinrpg.Network.SyncPlayerPropsMessage;
import de.rinonline.korinrpg.Network.sendTextpop;
import de.rinonline.korinrpg.Network.sendVillagePacket;
import de.rinonline.korinrpg.RINMAIN;
import de.rinonline.korinrpg.nbt.RINPlayer;
import java.util.List;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.village.Village;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;

public class RegionEventHandler {
  private static int timer = 0;
  
  @SubscribeEvent
  public void onClientTick(TickEvent.ClientTickEvent event) {
    if (RINPlayer.MapCooldown > 0)
      RINPlayer.MapCooldown--; 
  }
  
  @SubscribeEvent
  public void onEntityConstructing(EntityEvent.EntityConstructing event) {
    if (event.entity instanceof EntityPlayer)
      if (RINPlayer.get((EntityPlayer)event.entity) == null) {
        RINPlayer.register((EntityPlayer)event.entity);
      } else {
        PacketDispatcher.sendTo((IMessage)new SyncPlayerPropsMessage((EntityPlayer)event.entity), (EntityPlayerMP)event.entity);
      }  
  }
  
  @SubscribeEvent
  public void onPlayerLogIn(PlayerEvent.PlayerLoggedInEvent event) {
    if (event.player instanceof EntityPlayer) {
      if (RINPlayer.get(event.player) == null) {
        RINPlayer.register(event.player);
      } else {
        PacketDispatcher.sendTo((IMessage)new SyncPlayerPropsMessage(event.player), (EntityPlayerMP)event.player);
      } 
      PacketDispatcher.sendTo(new sendTextpop("none"),(EntityPlayerMP)event.player);
    	}  
    }
  
  @SubscribeEvent
  public void onPlayerLogOut(PlayerEvent.PlayerLoggedOutEvent event) {
    if (event.player instanceof EntityPlayer)
      PacketDispatcher.sendTo((IMessage)new SyncPlayerPropsMessage(event.player), (EntityPlayerMP)event.player); 
  }
  
  @SubscribeEvent
  public void onJoinWorld(EntityJoinWorldEvent event) {
    if (event.entity instanceof EntityPlayerMP)
      if (RINPlayer.get((EntityPlayer)event.entity) == null)
        RINPlayer.register((EntityPlayer)event.entity);  
  }
  
  @SubscribeEvent
  public void onClonePlayer( net.minecraftforge.event.entity.player.PlayerEvent.Clone event) {
    RINPlayer.get(event.entityPlayer).copy(RINPlayer.get(event.original));
    PacketDispatcher.sendTo((IMessage)new SyncPlayerPropsMessage(event.entityPlayer), (EntityPlayerMP)event.entityPlayer);
  }
  
  @SubscribeEvent
  public void onClientTick(TickEvent.WorldTickEvent event) {
    if (event.side.isServer())
      if (updatetime()) {
        List<Village> allVillages = event.world.villageCollectionObj.getVillageList();
        if (!allVillages.isEmpty()) {
          int a = allVillages.size();
          for (int i = 0; i < a; i++) {
            ChunkCoordinates cords = ((Village)allVillages.get(i)).getCenter();
            List<EntityPlayer> list = event.world.playerEntities;
            if (!list.isEmpty()) {
              int o = list.size();
              for (int k = 0; k < o; k++) {
                EntityPlayer player = list.get(k);
                if (player != null) {
                  boolean api = false;
                  NBTTagCompound nbt = RINMAIN.modInterop.getOrMakeVNInfo(event.world, cords.posX, cords.posY, cords.posZ);
                  if (nbt != null) {
                    cords = new ChunkCoordinates(nbt.getInteger("signX"), nbt.getInteger("signY"), nbt.getInteger("signZ"));
                    api = true;
                  } 
                  if (player.getDistance(cords.posX, cords.posY, cords.posZ) < 50.0D) {
                    int tempID = i;
                    String str = "Empty String";
                    for (; tempID > ConfigurationMoD.TownNames.length; tempID -= ConfigurationMoD.TownNames.length + 1);
                    if (tempID < ConfigurationMoD.TownNames.length)
                      str = ConfigurationMoD.TownNames[tempID]; 
                    if (api)
                      str = nbt.getString("namePrefix") + " " + nbt.getString("nameRoot") + " " + nbt.getString("nameSuffix").trim(); 
                    if (ConfigurationMoD.canTeleportToVillages)
                      RINPlayer.get(player).addTeleportCords(cords, str); 
                    PacketDispatcher.sendTo((IMessage)new sendVillagePacket(str, cords, 120), (EntityPlayerMP)player);
                  } 
                } 
              } 
            } 
          } 
        } 
        List<EntityPlayer> playerlist = event.world.playerEntities;
        if (!playerlist.isEmpty()) {
          int o = playerlist.size();
          for (int k = 0; k < o; k++) {
            EntityPlayer player = playerlist.get(k);
            if (player != null)
              ConfigurationMoD.checkPlayerPos(player); 
          } 
        } 
      }  
  }
  
  private boolean updatetime() {
    timer++;
    if (timer >= 100) {
      timer = 0;
      return true;
    } 
    return false;
  }
}