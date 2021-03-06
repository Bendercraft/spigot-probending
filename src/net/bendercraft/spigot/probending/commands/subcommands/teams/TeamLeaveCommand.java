package net.bendercraft.spigot.probending.commands.subcommands.teams;

import net.bendercraft.spigot.probending.commands.ProbendingCommand;
import net.bendercraft.spigot.probending.data.Container;
import net.bendercraft.spigot.probending.exceptions.ProbendingException;
import net.bendercraft.spigot.probending.exceptions.ProbendingPlayerCommandException;
import net.bendercraft.spigot.probending.models.ProbendingTeam;
import net.bendercraft.spigot.probending.utils.Messages;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * Created by Nokorbis on 6/04/2016.
 */
public class TeamLeaveCommand extends ProbendingCommand {

    public TeamLeaveCommand() {
        super();
        this.command = "leave";
        this.aliases.add("le");
    }

    public boolean execute(CommandSender sender, List<String> args) throws ProbendingException {
        if (!(sender instanceof Player)) {
            throw new ProbendingPlayerCommandException();
        }
        if (args.isEmpty()) {
            throw new ProbendingException("error.command.argument.more");
        }

        String teamName = args.remove(0);
        ProbendingTeam team = Container.getInstance().getTeam(teamName);
        if (team == null) {
            throw new ProbendingException("error.team.unexisting");
        }
        Player player = (Player) sender;
        if (!team.isMember(player)) {
            throw new ProbendingException("error.team.notmember");
        }

        team.removeMember(player);

        String msg = Messages.get("probending.team.you.left");
        msg = msg.replace("{TEAM}", team.getName());
        player.sendMessage(ChatColor.AQUA + msg);
        msg = Messages.get("probending.team.left.you");
        msg = msg.replace("{PLAYER}", player.getName());
        msg = msg.replace("{TEAM}", team.getName());
        for (Player member : team.getMembers()) {
            if (member.isOnline() && !member.equals(player)) {
                member.sendMessage(ChatColor.AQUA + msg);
            }
        }
        return true;
    }

    @Override
    public void printUsage(CommandSender sender) {
        sender.sendMessage(ChatColor.AQUA + "/probending team invite <TEAM_NAME> <PLAYER_NAME>");
    }
}
