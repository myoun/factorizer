package live.myoun.factorizer

import live.myoun.factorizer.command.FactoryItemCommand
import live.myoun.factorizer.command.GamemodeCommand
import live.myoun.factorizer.listener.ConveyorBeltListener
import live.myoun.factorizer.listener.ServerListener
import net.minestom.server.MinecraftServer
import net.minestom.server.extensions.Extension


class FactorizerExtension : Extension() {

    override fun initialize(): LoadStatus {
        logger().info("Hello, World!")
        ConveyorBeltListener(eventNode()).run()
        ServerListener(eventNode()).run()

        MinecraftServer.getCommandManager().register(FactoryItemCommand())
        MinecraftServer.getCommandManager().register(GamemodeCommand())

        return LoadStatus.SUCCESS
    }

    override fun terminate() {

    }


}