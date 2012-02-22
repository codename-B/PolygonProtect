package net.betterverse.protect;

import java.util.List;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockDispenseEvent;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.block.BlockFormEvent;
import org.bukkit.event.block.BlockFromToEvent;
import org.bukkit.event.block.BlockIgniteEvent;
import org.bukkit.event.block.BlockListener;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.BlockRedstoneEvent;
import org.bukkit.event.block.BlockSpreadEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import net.betterverse.protect.utils.ProtectedPolygon;

public class PolygonBlock extends BlockListener {
	
	public PolygonBlock(PluginManager pm, JavaPlugin plugin) {
		pm.registerEvent(Event.Type.BLOCK_BREAK, this, Priority.Normal, plugin);
		pm.registerEvent(Event.Type.BLOCK_BURN, this, Priority.Normal, plugin);
		pm.registerEvent(Event.Type.BLOCK_DISPENSE, this, Priority.Normal, plugin);
		pm.registerEvent(Event.Type.BLOCK_FADE, this, Priority.Normal, plugin);
		pm.registerEvent(Event.Type.BLOCK_FORM, this, Priority.Normal, plugin);
		pm.registerEvent(Event.Type.BLOCK_FROMTO, this, Priority.Normal, plugin);
		pm.registerEvent(Event.Type.BLOCK_IGNITE, this, Priority.Normal, plugin);
		pm.registerEvent(Event.Type.BLOCK_PISTON_EXTEND, this, Priority.Normal, plugin);
		pm.registerEvent(Event.Type.BLOCK_PISTON_RETRACT, this, Priority.Normal, plugin);
		pm.registerEvent(Event.Type.BLOCK_PLACE, this, Priority.Normal, plugin);
		pm.registerEvent(Event.Type.BLOCK_SPREAD, this, Priority.Normal, plugin);
		pm.registerEvent(Event.Type.LEAVES_DECAY, this, Priority.Normal, plugin);
	}
	
	PolygonManager pm = PolygonManager.getInstance();
	
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
	public void onBlockBreak(BlockBreakEvent event) {
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
		if(!build)
		event.setCancelled(true);
	}
	
	@Override
	public void onBlockPlace(BlockPlaceEvent event) {
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
		if(!build)
		event.setCancelled(true);
	}

	@Override
	public void onBlockBurn(BlockBurnEvent event) {
				List<ProtectedPolygon> poly = pm.getList(event.getBlock().getLocation());
				eventCheck(event, poly);
	}

	@Override
	public void onBlockDispense(BlockDispenseEvent event) {
				List<ProtectedPolygon> poly = pm.getList(event.getBlock().getLocation());
				eventCheck(event, poly);
	}

	@Override
	public void onBlockFade(BlockFadeEvent event) {
				List<ProtectedPolygon> poly = pm.getList(event.getBlock().getLocation());
				eventCheck(event, poly);
	}

	@Override
	public void onBlockForm(BlockFormEvent event) {
				List<ProtectedPolygon> poly = pm.getList(event.getBlock().getLocation());
				eventCheck(event, poly);
	}

	@Override
	public void onBlockFromTo(BlockFromToEvent event) {
				List<ProtectedPolygon> poly = pm.getList(event.getBlock().getLocation());
				eventCheck(event, poly);
	}

	@Override
	public void onBlockIgnite(BlockIgniteEvent event) {
				List<ProtectedPolygon> poly = pm.getList(event.getBlock().getLocation());
				eventCheck(event, poly);
	}

	@Override
	public void onBlockPistonExtend(BlockPistonExtendEvent event) {
				List<ProtectedPolygon> poly = pm.getList(event.getBlock().getLocation());
				eventCheck(event, poly);
	}

	@Override
	public void onBlockPistonRetract(BlockPistonRetractEvent event) {
				List<ProtectedPolygon> poly = pm.getList(event.getBlock().getLocation());
				eventCheck(event, poly);
	}

	@Override
	public void onBlockRedstoneChange(BlockRedstoneEvent event) {
				List<ProtectedPolygon> poly = pm.getList(event.getBlock().getLocation());
				eventCheck(event, poly);
	}

	@Override
	public void onBlockSpread(BlockSpreadEvent event) {
				List<ProtectedPolygon> poly = pm.getList(event.getBlock().getLocation());
				eventCheck(event, poly);
	}

	@Override
	public void onLeavesDecay(LeavesDecayEvent event) {
				List<ProtectedPolygon> poly = pm.getList(event.getBlock().getLocation());
				eventCheck(event, poly);
	}

}
