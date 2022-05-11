package live.myoun.factorizer.listener

import live.myoun.factorizer.generator.PlainChunkGenerator
import net.minestom.server.MinecraftServer
import net.minestom.server.entity.GameMode
import net.minestom.server.entity.ItemEntity
import net.minestom.server.entity.Player
import net.minestom.server.event.Event
import net.minestom.server.event.EventNode
import net.minestom.server.event.item.ItemDropEvent
import net.minestom.server.event.item.PickupItemEvent
import net.minestom.server.event.player.PlayerLoginEvent
import net.minestom.server.instance.InstanceContainer
import net.minestom.server.tag.Tag
import net.minestom.server.utils.NamespaceID
import net.minestom.server.world.DimensionType

class ServerListener(val eventNode: EventNode<Event>) : Runnable {

    lateinit var instance: InstanceContainer

    override fun run() {

        val dim = DimensionType.builder(NamespaceID.from("underworld"))
            .ambientLight(10000.0F)
            .height(512)
            .logicalHeight(256)
            .minY(-64)
            .build()
        MinecraftServer.getDimensionTypeManager().addDimension(dim)
        instance = MinecraftServer.getInstanceManager().createInstanceContainer(dim)

        instance.chunkGenerator = PlainChunkGenerator()

        val dropcount = Tag.Integer("dropcount")


        eventNode.addListener(PlayerLoginEvent::class.java) { event ->
            event.setSpawningInstance(instance)
        }

        eventNode.addListener(ItemDropEvent::class.java) {
            val ie = ItemEntity(it.itemStack)
            ie.setTag(dropcount, 3)
            ie.velocity = it.player.velocity.mul(3.0)

            ie.setInstance(it.instance, it.player.position.add(0.0, it.player.eyeHeight, 0.0).withYaw(it.player.position.yaw))
        }

        eventNode.addListener(PickupItemEvent::class.java) {
            if (it.livingEntity is Player) {
                if (it.itemEntity.hasTag(dropcount)) {
                    if (it.itemEntity.getTag(dropcount)!! > 0) {
                        it.itemEntity.setTag(dropcount, it.itemEntity.getTag(dropcount)!!-1)
                        it.isCancelled = true
                        return@addListener
                    }
                    else it.itemEntity.removeTag(dropcount)
                }
                if ((it.livingEntity as Player).gameMode != GameMode.SPECTATOR) {
                    (it.livingEntity as Player).inventory.addItemStack(it.itemEntity.itemStack)
                } else it.isCancelled = true
            }
        }

    }


}