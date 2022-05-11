package live.myoun.factorizer.listener

import live.myoun.factorizer.FactoryItem
import live.myoun.factorizer.handler.ConveyorHandler
import net.minestom.server.entity.GameMode
import net.minestom.server.entity.Player
import net.minestom.server.event.Event
import net.minestom.server.event.EventNode
import net.minestom.server.event.item.PickupItemEvent
import net.minestom.server.event.player.PlayerBlockInteractEvent
import net.minestom.server.event.player.PlayerPreEatEvent
import net.minestom.server.instance.block.Block
import net.minestom.server.item.ItemStack
import net.minestom.server.tag.Tag

class ConveyorBeltListener(val eventNode: EventNode<Event>) : Runnable {

    override fun run() {
        eventNode.addListener(PlayerBlockInteractEvent::class.java) {
            if (!it.player.itemInMainHand.hasTag(FactoryItem.conveyorBeltTag)) {
                return@addListener
            }
            // Conveyor Belt
            val newBlockPosition = it.blockPosition.relative(it.blockFace)

            val faceTag = Tag.String("direction")

            val block = Block.MAGENTA_GLAZED_TERRACOTTA
                .withHandler(ConveyorHandler())

            if (it.instance.getBlock(newBlockPosition) == Block.AIR) {
                it.instance.setBlock(newBlockPosition, block)

                if (it.player.gameMode != GameMode.CREATIVE && it.player.gameMode != GameMode.SPECTATOR) {
                    if (it.player.itemInMainHand.amount == 1)
                        it.player.itemInMainHand = ItemStack.AIR
                    else it.player.itemInMainHand = it.player.itemInMainHand.withAmount(it.player.itemInMainHand.amount-1)
                }
            }
        }

        eventNode.addListener(PlayerPreEatEvent::class.java) {
            if (!it.player.itemInMainHand.hasTag(FactoryItem.conveyorBeltTag)) {
                return@addListener
            }
            it.isCancelled = true
        }


    }


}