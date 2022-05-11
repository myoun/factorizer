package live.myoun.factorizer.command

import live.myoun.factorizer.util.toMini
import net.minestom.server.command.builder.Command
import net.minestom.server.command.builder.arguments.ArgumentType
import net.minestom.server.entity.GameMode
import net.minestom.server.entity.Player

class GamemodeCommand : Command("gamemode", "gm") {

    init {
        setDefaultExecutor { sender, _ ->
            sender.sendMessage("<red>사용법 : /gamemode <gamemode></red>".toMini())
        }
        val gamemode = ArgumentType.Enum("gamemode", GameMode::class.java)

        addSyntax({sender, argument ->
            val gamemode = argument.get(gamemode)

            if (sender !is Player) {
                return@addSyntax
            }
            sender.gameMode = gamemode
        }, gamemode)
    }
}