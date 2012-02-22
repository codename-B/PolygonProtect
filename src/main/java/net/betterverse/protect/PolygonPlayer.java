package net.betterverse.protect;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.bukkit.event.player.PlayerBucketEmptyEvent;
import org.bukkit.event.player.PlayerBucketFillEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerEggThrowEvent;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerInventoryEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.event.player.PlayerToggleSprintEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import net.betterverse.protect.utils.ProtectedPolygon;

public class PolygonPlayer extends PlayerListener {
	
	PolygonManager pm = PolygonManager.getInstance();
	
	public PolygonPlayer(PluginManager pm, JavaPlugin plugin) {
		pm.registerEvent(Event.Type.PLAYER_BUCKET_EMPTY, this, Priority.Normal, plugin);
		pm.registerEvent(Event.Type.PLAYER_BUCKET_FILL, this, Priority.Normal, plugin);
		pm.registerEvent(Event.Type.PLAYER_BED_ENTER, this, Priority.Normal, plugin);
		pm.registerEvent(Event.Type.PLAYER_BED_LEAVE, this, Priority.Normal, plugin);
		pm.registerEvent(Event.Type.PLAYER_CHAT, this, Priority.Normal, plugin);
		pm.registerEvent(Event.Type.PLAYER_DROP_ITEM, this, Priority.Normal, plugin);
		pm.registerEvent(Event.Type.PLAYER_EGG_THROW, this, Priority.Normal, plugin);
		pm.registerEvent(Event.Type.PLAYER_FISH, this, Priority.Normal, plugin);
		pm.registerEvent(Event.Type.PLAYER_INTERACT, this, Priority.Normal, plugin);
		pm.registerEvent(Event.Type.PLAYER_INTERACT_ENTITY, this, Priority.Normal, plugin);
		pm.registerEvent(Event.Type.PLAYER_TOGGLE_SNEAK, this, Priority.Normal, plugin);
		pm.registerEvent(Event.Type.PLAYER_TOGGLE_SPRINT, this, Priority.Normal, plugin);
	}

	@Override
	public void onPlayerBucketFill(PlayerBucketFillEvent event) {
		Player player = event.getPlayer();
		boolean build = true;
		List<ProtectedPolygon> poly = pm.getList(event.getBlockClicked().getLocation());
		
		if(poly.size() == 0)
			return;
		
		for(ProtectedPolygon p : poly) {
			if(p.canOverride(player))
				return;
			if(!p.canBuild(player))
				build = false;
		}
		event.setCancelled(!build);	
		PolygonDebug.log(event, (Cancellable) event);
	}
	
	@Override
	public void onPlayerBucketEmpty(PlayerBucketEmptyEvent event) {
		Player player = event.getPlayer();
		boolean build = true;
		List<ProtectedPolygon> poly = pm.getList(event.getBlockClicked().getLocation());
		
		if(poly.size() == 0)
			return;
		
		for(ProtectedPolygon p : poly) {
			if(p.canOverride(player))
				return;
			if(!p.canBuild(player))
				build = false;
		}
		event.setCancelled(!build);	
		PolygonDebug.log(event, (Cancellable) event);
	}
	
	public void eventCheck(Player player, Event event, List<ProtectedPolygon> poly) {
		if(poly.size() == 0)
			return;
		Cancellable cancel = null;
		if(event instanceof Cancellable)
			cancel = (Cancellable) event;
		else
			return;
		String flag = event.getType().name();
		boolean cancelled = false;
		for(ProtectedPolygon p : poly) {
			if(p.canOverride(player))
				return;
			boolean fl = p.getFlag(flag);
			if(fl) {
				cancelled = true;
			}
		}
		if(cancelled)
			cancel.setCancelled(true);

		PolygonDebug.log(event, (Cancellable) event);
	}

	@Override
	public void onInventoryOpen(PlayerInventoryEvent event) {
				List<ProtectedPolygon> poly = pm.getList(event.getPlayer().getLocation());
				eventCheck(event.getPlayer(), event, poly);
	}

	@Override
	public void onItemHeldChange(PlayerItemHeldEvent event) {
				List<ProtectedPolygon> poly = pm.getList(event.getPlayer().getLocation());
				eventCheck(event.getPlayer(), event, poly);
	}

	@Override
	public void onPlayerBedEnter(PlayerBedEnterEvent event) {
				List<ProtectedPolygon> poly = pm.getList(event.getBed().getLocation());
				eventCheck(event.getPlayer(), event, poly);
	}

	@Override
	public void onPlayerBedLeave(PlayerBedLeaveEvent event) {
				List<ProtectedPolygon> poly = pm.getList(event.getBed().getLocation());
				eventCheck(event.getPlayer(), event, poly);
	}

	@Override
	public void onPlayerChat(PlayerChatEvent event) {
				List<ProtectedPolygon> poly = pm.getList(event.getPlayer().getLocation());
				eventCheck(event.getPlayer(), event, poly);
	}

	@Override
	public void onPlayerDropItem(PlayerDropItemEvent event) {
				List<ProtectedPolygon> poly = pm.getList(event.getPlayer().getLocation());
				eventCheck(event.getPlayer(), event, poly);
	}

	@Override
	public void onPlayerEggThrow(PlayerEggThrowEvent event) {
				List<ProtectedPolygon> poly = pm.getList(event.getPlayer().getLocation());
				eventCheck(event.getPlayer(), event, poly);
	}

	@Override
	public void onPlayerFish(PlayerFishEvent event) {
				List<ProtectedPolygon> poly = pm.getList(event.getPlayer().getLocation());
				eventCheck(event.getPlayer(), event, poly);
	}

	@Override
	public void onPlayerInteract(PlayerInteractEvent event) {
		if(event.getClickedBlock() == null)
			return;
		if(event.getClickedBlock().getLocation() == null)
			return;
	
		
		Player player = event.getPlayer();
		boolean cancel = false;
		
		List<ProtectedPolygon> poly = pm.getList(event.getClickedBlock().getLocation());
		
		if(poly.size() == 0)
			return;
		
		String flag = event.getType().name()+"_"+event.getClickedBlock().getType().name();
		String gFlag = event.getType().name();
		
		for(ProtectedPolygon p : poly) {
			if(p.canOverride(player))
				return;
			
			if((p.getFlag(flag) || p.getFlag(gFlag)))
				cancel = true;
		}
		if(cancel)
		event.setCancelled(true);
		PolygonDebug.log(event, (Cancellable) event);
	}

	@Override
	public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
				List<ProtectedPolygon> poly = pm.getList(event.getRightClicked().getLocation());
				eventCheck(event.getPlayer(), event, poly);
	}

	@Override
	public void onPlayerToggleSneak(PlayerToggleSneakEvent event) {
				List<ProtectedPolygon> poly = pm.getList(event.getPlayer().getLocation());
				eventCheck(event.getPlayer(), event, poly);
	}

	@Override
	public void onPlayerToggleSprint(PlayerToggleSprintEvent event) {
				List<ProtectedPolygon> poly = pm.getList(event.getPlayer().getLocation());
				eventCheck(event.getPlayer(), event, poly);
	}

}
