package live.myoun.factorizer

import net.minestom.server.tag.Tag

enum class FactoryItem {

    CONVEYOR_BELT;

    companion object {
        val conveyorBeltTag = Tag.String("conveyor-belt")
    }

}