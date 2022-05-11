package live.myoun.factorizer.handler

import live.myoun.factorizer.FactoryItem
import live.myoun.factorizer.command.FactoryItemCommand
import net.kyori.adventure.text.TextComponent
import net.minestom.server.coordinate.Vec
import net.minestom.server.entity.EntityType
import net.minestom.server.entity.ItemEntity
import net.minestom.server.entity.Player
import net.minestom.server.instance.block.Block
import net.minestom.server.instance.block.BlockHandler
import net.minestom.server.tag.Tag
import net.minestom.server.utils.NamespaceID

class ConveyorHandler : BlockHandler {

    override fun onDestroy(destroy: BlockHandler.Destroy) {
        val itemEntity = ItemEntity(FactoryItemCommand.itemGenerator(FactoryItem.CONVEYOR_BELT))
        if (destroy.instance.getBlock(destroy.blockPosition).isAir)
            itemEntity.setInstance(destroy.instance, destroy.blockPosition)
    }

    override fun onInteract(interaction: BlockHandler.Interaction): Boolean {

        if (interaction.player.isSneaking) {
            interaction.instance.setBlock(interaction.blockPosition, Block.AIR)
        } else if (!interaction.player.itemInMainHand.isAir) {
            // TODO
        } else {
            val now = interaction.block.getProperty("facing")
            val next = nextFace(now!!)
            interaction.instance.setBlock(interaction.blockPosition, interaction.block.withProperty("facing", next).withHandler(this))
        }
        return true
    }

    override fun isTickable(): Boolean {
        return true
    }

    override fun tick(tick: BlockHandler.Tick) {
        val entities = tick.instance.getNearbyEntities(tick.blockPosition, 2.0)
            .filter { it.entityType == EntityType.ITEM }
            .filter {
                val bp = tick.blockPosition.add(0.0, 1.0, 0.0)
                val ep = it.position
                bp.blockX() == ep.blockX() && bp.blockY() == ep.blockY() && bp.blockZ() == ep.blockZ()
            }

        val facing = tick.block.getProperty("facing")!!
        entities.forEach {
            it.scheduleNextTick { sce ->
                val applyPos = nextPosByFace(facing)
                val nextPos = sce.position.add(applyPos)

                sce.refreshPosition(nextPos)
            }
        }
    }

    override fun getNamespaceId(): NamespaceID {
        return NamespaceID.from("factorizer:conveyor_belt")
    }

    private fun nextFace(now : String): String {
        return when (now) {
            "north" -> "east"
            "east" -> "south"
            "south" -> "west"
            "west" -> "north"
            else -> throw Exception("Unexpected Direction")
        }
    }

    private fun nextPosByFace(face: String) : Vec {
        return when (face) {
            "east" -> Vec.ZERO.add(-0.125,0.0,0.0)
            "south" -> Vec.ZERO.add(0.0,0.0,-0.125)
            "west" -> Vec.ZERO.add(0.125,0.0,0.0)
            "north" -> Vec.ZERO.add(0.0,0.0,0.125)
            else -> throw Exception("Unexpected Direction")
        }
    }
}