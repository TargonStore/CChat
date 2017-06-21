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

	private int TempoTotal;
	private int TempoAviso;
	private List<String> iraLimparAutomatico = new ArrayList<>();
	private List<String> chatLimpoAutomatico = new ArrayList<>();
	private List<String> iraLimparAdmin = new ArrayList<>();
	private List<String> chatLimpoAdmin = new ArrayList<>();

	private BukkitTask task;

	@Override
	public void onEnable() {

		if (!getDataFolder().exists())
			getDataFolder().mkdirs();

		saveDefaultConfig();

		TempoTotal = getConfig().getInt("TargonCChat.Config.tempoTotal");
		TempoAviso = getConfig().getInt("TargonCChat.Config.tempoAviso");
		iraLimparAutomatico = getConfig().getStringList("TargonCChat.Mensagens.iraLimparAutomatico");
		chatLimpoAutomatico = getConfig().getStringList("TargonCChat.Mensagens.chatLimpoAutomatico");
		iraLimparAdmin = getConfig().getStringList("TargonCChat.Mensagens.iraLimparAdmin");
		chatLimpoAdmin = getConfig().getStringList("TargonCChat.Mensagens.chatLimpoAdmin");

		getCommand("clearchat").setExecutor(this);
		
		task = new BukkitRunnable() {

			int Tempo = TempoTotal;

			@Override
			public void run() {

				if (Tempo == TempoAviso) {
					for (Player p : Bukkit.getOnlinePlayers()) {
						for (String s : iraLimparAutomatico) {
							p.sendMessage(
									ChatColor.translateAlternateColorCodes('&', s.replace("{tempo}", "" + Tempo)));
						}
					}
				}

				if (Tempo == 0) {
					for (Player p : Bukkit.getOnlinePlayers()) {
						for (int i = 0; i < 250; i++) {
							p.sendMessage(" ");
						}
						for (String s : chatLimpoAutomatico) {
							p.sendMessage(ChatColor.translateAlternateColorCodes('&', s));
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
			for (String s : iraLimparAdmin) {
				p.sendMessage(ChatColor.translateAlternateColorCodes('&',
						s.replace("{tempo}", "" + TempoAviso).replace("{admin}", admin)));
			}
		}

		new BukkitRunnable() {

			@Override
			public void run() {
				for (Player p : Bukkit.getOnlinePlayers()) {
					for (int i = 0; i < 250; i++) {
						p.sendMessage(" ");
					}
					for (String s : chatLimpoAdmin) {
						p.sendMessage(ChatColor.translateAlternateColorCodes('&', s.replace("{admin}", admin)));
					}
				}
			}
		}.runTaskLater(this, TempoAviso * 20);

		return false;

	}

}
