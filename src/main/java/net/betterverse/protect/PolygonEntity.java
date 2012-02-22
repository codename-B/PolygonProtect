package net.betterverse.protect;

import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreeperPowerEvent;
import org.bukkit.event.entity.EndermanPickupEvent;
import org.bukkit.event.entity.EndermanPlaceEvent;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntityListener;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.entity.ItemSpawnEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.SlimeSplitEvent;
import org.bukkit.event.painting.PaintingBreakByEntityEvent;
import org.bukkit.event.painting.PaintingBreakEvent;
import org.bukkit.event.painting.PaintingPlaceEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import net.betterverse.protect.utils.ProtectedPolygon;

public class PolygonEntity extends EntityListener {
	
	PolygonManager pm = PolygonManager.getInstance();
	
	public PolygonEntity(PluginManager pm, JavaPlugin plugin) {
		pm.registerEvent(Event.Type.CREATURE_SPAWN, this, Priority.Normal, plugin);
		pm.registerEvent(Event.Type.CREEPER_POWER, this, Priority.Normal, plugin);
		pm.registerEvent(Event.Type.ENDERMAN_PICKUP, this, Priority.Normal, plugin);
		pm.registerEvent(Event.Type.ENDERMAN_PLACE, this, Priority.Normal, plugin);
		pm.registerEvent(Event.Type.ENTITY_COMBUST, this, Priority.Normal, plugin);
		pm.registerEvent(Event.Type.ENTITY_DAMAGE, this, Priority.Normal, plugin);
		pm.registerEvent(Event.Type.ENTITY_EXPLODE, this, Priority.Normal, plugin);
		pm.registerEvent(Event.Type.ENTITY_TARGET, this, Priority.Normal, plugin);
		pm.registerEvent(Event.Type.EXPLOSION_PRIME, this, Priority.Normal, plugin);
		pm.registerEvent(Event.Type.ITEM_SPAWN, this, Priority.Normal, plugin);
		pm.registerEvent(Event.Type.PAINTING_BREAK, this, Priority.Normal, plugin);
		pm.registerEvent(Event.Type.PAINTING_PLACE, this, Priority.Normal, plugin);
		pm.registerEvent(Event.Type.PROJECTILE_HIT, this, Priority.Normal, plugin);
		pm.registerEvent(Event.Type.SLIME_SPLIT, this, Priority.Normal, plugin);
	}
	
	public void eventCheck(Event event, List<ProtectedPolygon> poly) {
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
	public void onCreatureSpawn(CreatureSpawnEvent event) {
				List<ProtectedPolygon> poly = pm.getList(event.getEntity().getLocation());
				eventCheck(event, poly);
	}

	@Override
	public void onCreeperPower(CreeperPowerEvent event) {
				List<ProtectedPolygon> poly = pm.getList(event.getEntity().getLocation());
				eventCheck(event, poly);
	}

	@Override
	public void onEndermanPickup(EndermanPickupEvent event) {
				List<ProtectedPolygon> poly = pm.getList(event.getBlock().getLocation());
				eventCheck(event, poly);
	}

	@Override
	public void onEndermanPlace(EndermanPlaceEvent event) {
				List<ProtectedPolygon> poly = pm.getList(event.getEntity().getLocation());
				eventCheck(event, poly);
	}

	@Override
	public void onEntityCombust(EntityCombustEvent event) {
				List<ProtectedPolygon> poly = pm.getList(event.getEntity().getLocation());
				eventCheck(event, poly);
	}

	@Override
	public void onEntityDamage(EntityDamageEvent event) {
				if(event.getEntity() instanceof Player) {
				List<ProtectedPolygon> poly = pm.getList(event.getEntity().getLocation());
				if(event instanceof EntityDamageByEntityEvent) {
					EntityDamageByEntityEvent ev = (EntityDamageByEntityEvent) event;
					if(ev.getDamager() instanceof Player) {
						if(poly.size() == 0)
							return;
						String flag = "PLAYER_DAMAGE_PVP";
						boolean cancelled = false;
						for(ProtectedPolygon p : poly) {
							boolean fl = p.getFlag(flag);
							if(fl) {
								cancelled = true;
							}
						}
						if(cancelled) {
							event.setCancelled(true);
						((Player)ev.getDamager()).sendMessage(ChatColor.RED+flag+" is disabled");
						}
						return;
					}
				} else {
				if(poly.size() == 0)
					return;
				String flag = "PLAYER_DAMAGE";
				boolean cancelled = false;
				for(ProtectedPolygon p : poly) {
					boolean fl = p.getFlag(flag);
					if(fl) {
						cancelled = true;
					}
				}
				if(cancelled)
					event.setCancelled(true);
				return;
				}
				}
	}

	@Override
	public void onEntityExplode(EntityExplodeEvent event) {
				List<ProtectedPolygon> poly = pm.getList(event.getLocation());
				eventCheck(event, poly);
	}

	@Override
	public void onEntityTarget(EntityTargetEvent event) {
				List<ProtectedPolygon> poly = pm.getList(event.getEntity().getLocation());
				eventCheck(event, poly);
	}

	@Override
	public void onExplosionPrime(ExplosionPrimeEvent event) {
				List<ProtectedPolygon> poly = pm.getList(event.getEntity().getLocation());
				eventCheck(event, poly);
	}

	@Override
	public void onItemSpawn(ItemSpawnEvent event) {
				List<ProtectedPolygon> poly = pm.getList(event.getEntity().getLocation());
				eventCheck(event, poly);
	}

	@Override
	public void onPaintingPlace(PaintingPlaceEvent event) {
		Player player = event.getPlayer();
		boolean build = true;
		List<ProtectedPolygon> poly = pm.getList(event.getBlock().getLocation());
		
		if(poly.size() == 0)
			return;
		
		for(ProtectedPolygon p : poly) {
			if(p.canOverride(player))
				return;
			if(!p.canBuild(player))
				build = false;
		}
		event.setCancelled(!build);	
	}

	@Override
	public void onPaintingBreak(PaintingBreakEvent ev) {
		if(ev instanceof PaintingBreakByEntityEvent) {
		PaintingBreakByEntityEvent event = (PaintingBreakByEntityEvent) ev;
		if(!(event.getRemover() instanceof Player))
			return;
		
		Player player = (Player) event.getRemover();
		boolean build = true;
		List<ProtectedPolygon> poly = pm.getList(event.getPainting().getLocation());
		
		if(poly.size() == 0)
			return;
		
		for(ProtectedPolygon p : poly) {
			if(p.canOverride(player))
				return;
			if(!p.canBuild(player))
				build = false;
		}
		event.setCancelled(!build);	
		}
	}

	@Override
	public void onProjectileHit(ProjectileHitEvent event) {
				List<ProtectedPolygon> poly = pm.getList(event.getEntity().getLocation());
				eventCheck(event, poly);
	}

	@Override
	public void onSlimeSplit(SlimeSplitEvent event) {
				List<ProtectedPolygon> poly = pm.getList(event.getEntity().getLocation());
				eventCheck(event, poly);
	}

}
