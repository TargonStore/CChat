package net.JaoBalalao.TargonCChat;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import net.md_5.bungee.api.ChatColor;

public class Main extends JavaPlugin implements CommandExecutor {

	private boolean chatMsg;
	private boolean chatAction;
	private int TempoTotal;
	private int TempoAviso;
	private List<String> iraLimparAutomatico = new ArrayList<>();
	private List<String> chatLimpoAutomatico = new ArrayList<>();
	private List<String> iraLimparAdmin = new ArrayList<>();
	private List<String> chatLimpoAdmin = new ArrayList<>();
	private String iraLimparAutomatico_AB;
	private String chatLimpoAutomatico_AB;
	private String iraLimparAdmin_AB;
	private String chatLimpoAdmin_AB;

	private BukkitTask task;

	ActionBar ac = new ActionBar();
	
	@Override
	public void onEnable() {

		if (!getDataFolder().exists())
			getDataFolder().mkdirs();

		saveDefaultConfig();

		chatMsg = getConfig().getBoolean("TargonCChat.Config.useChatMsg");
		chatAction = getConfig().getBoolean("TargonCChat.Config.useActionBar");
		TempoTotal = getConfig().getInt("TargonCChat.Config.tempoTotal");
		TempoAviso = getConfig().getInt("TargonCChat.Config.tempoAviso");
		iraLimparAutomatico = getConfig().getStringList("TargonCChat.Mensagens.iraLimparAutomatico");
		chatLimpoAutomatico = getConfig().getStringList("TargonCChat.Mensagens.chatLimpoAutomatico");
		iraLimparAdmin = getConfig().getStringList("TargonCChat.Mensagens.iraLimparAdmin");
		chatLimpoAdmin = getConfig().getStringList("TargonCChat.Mensagens.chatLimpoAdmin");
		iraLimparAutomatico_AB = getConfig().getString("TargonCChat.Mensagens.iraLimparAutomatico_ActionBar");
		chatLimpoAutomatico_AB = getConfig().getString("TargonCChat.Mensagens.chatLimpoAutomatico_ActionBar");
		iraLimparAdmin_AB = getConfig().getString("TargonCChat.Mensagens.iraLimparAdmin_ActionBar");
		chatLimpoAdmin_AB = getConfig().getString("TargonCChat.Mensagens.chatLimpoAdmin_ActionBar");

		getCommand("clearchat").setExecutor(this);
		
		task = new BukkitRunnable() {

			int Tempo = TempoTotal;

			@Override
			public void run() {

				if (Tempo == TempoAviso) {
					for (Player p : Bukkit.getOnlinePlayers()) {
						if(chatMsg) {
							for (String s : iraLimparAutomatico) {
								p.sendMessage(
										ChatColor.translateAlternateColorCodes('&', s.replace("{tempo}", "" + Tempo)));
							}
						}
						if(chatAction) {
							ac.send(p, ChatColor.translateAlternateColorCodes('&', iraLimparAutomatico_AB.replace("{tempo}", "" + Tempo)));
						
						}
					}
				}

				if (Tempo == 0) {
					for (Player p : Bukkit.getOnlinePlayers()) {
						for (int i = 0; i < 250; i++) {
							p.sendMessage(" ");
						}
						if(chatMsg) {
							for (String s : chatLimpoAutomatico) {
								p.sendMessage(ChatColor.translateAlternateColorCodes('&', s));
							}
						}
						if(chatAction) {
							ac.send(p, ChatColor.translateAlternateColorCodes('&', chatLimpoAutomatico_AB));
						}
					}
					Tempo = TempoTotal;
				}

				Tempo--;

			}
		}.runTaskTimer(this, 0, 20);

	}

	@Override
	public void onDisable() {
		task.cancel();
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		if(args.length > 0) {
			sender.sendMessage("§c* Muitos argumentos!");
		}
		
		String admin;

		if (sender instanceof Player) {
			admin = sender.getName();
		} else {
			admin = "Console";
		}

		if (!sender.hasPermission("TargonCChat.ClearChat")) {
			sender.sendMessage("§cSem permissão.");
			return true;
		}

		sender.sendMessage("§a* Você agendou uma limpeza de chat!");
		
		for (Player p : Bukkit.getOnlinePlayers()) {
			if(chatMsg) {
				for (String s : iraLimparAdmin) {
					p.sendMessage(ChatColor.translateAlternateColorCodes('&',
							s.replace("{tempo}", "" + TempoAviso).replace("{admin}", admin)));
				}
			}
			if(chatAction) {
				ac.send(p, ChatColor.translateAlternateColorCodes('&',
							iraLimparAdmin_AB.replace("{tempo}", "" + TempoAviso).replace("{admin}", admin)));
			}
		}

		new BukkitRunnable() {

			@Override
			public void run() {
				for (Player p : Bukkit.getOnlinePlayers()) {
					for (int i = 0; i < 250; i++) {
						p.sendMessage(" ");
					}
					if(chatMsg) {
						for (String s : chatLimpoAdmin) {
							p.sendMessage(ChatColor.translateAlternateColorCodes('&', s.replace("{admin}", admin)));
						}
					}
					if(chatAction) {
						ac.send(p, ChatColor.translateAlternateColorCodes('&', chatLimpoAdmin_AB.replace("{admin}", admin)));
					}
				}
			}
		}.runTaskLater(this, TempoAviso * 20);

		return false;

	}

}
