package live.myoun.factorizer.command

import live.myoun.factorizer.FactoryItem
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.minestom.server.command.builder.Command
import net.minestom.server.command.builder.arguments.ArgumentType
import net.minestom.server.entity.Player
import net.minestom.server.item.ItemMetaBuilder
import net.minestom.server.item.ItemStack
import net.minestom.server.item.Material
import net.minestom.server.item.StackingRule
import java.util.function.IntUnaryOperator

class FactoryItemCommand : Command("factoryitem", "fi") {

    init {
        setDefaultExecutor { sender, _ ->
            sender.sendMessage(Component.text("사용법 : /factoryitem <itemname>").color(NamedTextColor.RED))
        }

        val itemArgument = ArgumentType.Enum("factoryitem", FactoryItem::class.java)

        addSyntax({ sender, argument ->
            if (sender !is Player) {
                sender.sendMessage(Component.text("이 명령어는 플레이어만 사용할 수 있습니다.").color(NamedTextColor.RED))
                return@addSyntax
            }

            val factoryItem = argument.get(itemArgument)

            val itemstack = itemGenerator(factoryItem)

            sender.inventory.addItemStack(itemstack)

        }, itemArgument)
    }

    companion object {
        val itemGenerator: (FactoryItem) -> ItemStack = {
            when (it) {
                FactoryItem.CONVEYOR_BELT -> {
                    ItemStack
                        .builder(Material.DRIED_KELP)
                        .displayName(Component.text("컨베이어 벨트"))
                        .lore(
                            Component.text("가장 기본적인 컨베이어 벨트"),
                            Component.text("설치할 수 있다.")
                        )
                        .build()
                        .withTag(FactoryItem.conveyorBeltTag, "conveyor-belt")

                }
            }
        }
    }
}