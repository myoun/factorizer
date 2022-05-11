package live.myoun.factorizer.generator

import net.minestom.server.instance.Chunk
import net.minestom.server.instance.ChunkGenerator
import net.minestom.server.instance.ChunkPopulator
import net.minestom.server.instance.batch.ChunkBatch
import net.minestom.server.instance.block.Block

class PlainChunkGenerator : ChunkGenerator {

    override fun generateChunkData(batch: ChunkBatch, chunkX: Int, chunkZ: Int) {
        for (x in 0..Chunk.CHUNK_SIZE_X) {
            for (z in 0..Chunk.CHUNK_SIZE_Z) {
                for (y in 0..40) {
                    if (y == 0) {
                        batch.setBlock(x, y, z, Block.BEDROCK)
                    } else if (y <= 35) {
                        batch.setBlock(x, y, z, Block.STONE)
                    } else if (y < 40) {
                        batch.setBlock(x, y, z, Block.DIRT)
                    } else if (y == 40) {
                        batch.setBlock(x, y, z, Block.GRASS_BLOCK)
                    }
                }
            }
        }
    }

    override fun getPopulators(): MutableList<ChunkPopulator>? {
        return null
    }
}