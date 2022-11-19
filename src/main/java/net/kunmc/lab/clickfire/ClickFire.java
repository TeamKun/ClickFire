package net.kunmc.lab.clickfire;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.type.Fire;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class ClickFire extends JavaPlugin implements Listener, CommandExecutor, TabCompleter {

    public static boolean GAME = false;

    @Override
    public void onEnable() {
        // Plugin startup logic
        Objects.requireNonNull(this.getCommand("cf")).setExecutor(this);
        Objects.requireNonNull(this.getCommand("cf")).setTabCompleter(this);

        getServer().getPluginManager().registerEvents(this, this);
    }

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command cmd, String label, String[] args) {
        if (cmd.getName().equals("cf")) {
            if(args.length == 1){
                if(args[0].equals("on")){
                    if(GAME){
                        sender.sendMessage(ChatColor.RED + "プラグインはすでにonです");
                    }else{
                        GAME = true;
                        sender.sendMessage(ChatColor.GREEN + "プラグインをonにしました");
                    }
                }else if(args[0].equals("off")){
                    if(GAME){
                        GAME = false;
                        sender.sendMessage(ChatColor.GREEN + "プラグインをoffにしました");
                    }else{
                        sender.sendMessage(ChatColor.RED + "プラグインはすでにoffです");
                    }
                }else if(args[0].equals("help")){
                    sender.sendMessage(ChatColor.GOLD + "-------------コマンド一覧-------------");
                    sender.sendMessage("/cf on   : プラグインのon");
                    sender.sendMessage("/cf off  : プラグインのoff");
                    sender.sendMessage("/cf help : プラグインのコマンド一覧");
                    sender.sendMessage(ChatColor.GOLD + "-------------コマンド一覧-------------");
                }else{
                    sender.sendMessage(ChatColor.RED + "引数が異なります./cf help");
                }
            }else{
                sender.sendMessage(ChatColor.RED + "引数が異なります./cf help");
            }
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equals("cf")) {
            if (args.length == 1) {
                return (sender.hasPermission("cf")
                        ? Stream.of("on","off","help")
                        : Stream.of("on","off","help"))
                        .filter(e -> e.startsWith(args[0])).collect(Collectors.toList());
            }
        }
        return new ArrayList<>();
    }

    @EventHandler
    public void onClick(PlayerInteractEvent e){
        Player p = e.getPlayer();
        Action a = e.getAction();
        if(a == Action.LEFT_CLICK_BLOCK){
            Block block = e.getClickedBlock();
            BlockFace blockFace = p.rayTraceBlocks(6).getHitBlockFace();
            Block fire = block.getRelative(blockFace);
            fire.setType(Material.FIRE);

        }
    }
}
