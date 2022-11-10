package io.ejekta.bountiful.bounty

import io.ejekta.bountiful.Bountiful
import io.ejekta.bountiful.config.BountifulIO
import io.ejekta.bountiful.util.GameTime
import io.ejekta.kambrik.serial.ItemDataJson
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import net.minecraft.client.MinecraftClient
import net.minecraft.item.ItemStack
import net.minecraft.text.MutableText
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.util.Identifier
import net.minecraft.world.World
import kotlin.math.max

@Serializable
class BountyInfo(
    var tooltip: List<@Contextual MutableText> = listOf(Text.literal("DOOT")),
    var rarity: BountyRarity = BountyRarity.COMMON,
    var timeStarted: Long = -1L,
    var timeToComplete: Long = -1L,
    var objectiveFlags: Set<@Contextual Identifier> = emptySet()
) {

    fun timeLeft(world: World): Long {
        return when (BountifulIO.configData.shouldBountiesHaveTimersAndExpire) {
            true -> max(timeStarted - world.time + timeToComplete, 0L)
            false -> 1L
        }
    }

    // ### Formatting ### //

    fun formattedTimeLeft(world: World): Text {
        return GameTime.formatTimeExpirable(timeLeft(world) / 20)
    }

    private fun genTooltip(fromData: BountyData): List<MutableText> {
        val localPlayer = MinecraftClient.getInstance().player ?: return emptyList()
        return buildList {
            add(Text.translatable("bountiful.tooltip.required").formatted(Formatting.GOLD).append(":"))
            addAll(fromData.objectives.map {
                it.textSummary(fromData, localPlayer, true)
            })
            add(Text.translatable("bountiful.tooltip.rewards").formatted(Formatting.GOLD).append(":"))
            addAll(fromData.rewards.map {
                it.textSummary(fromData, localPlayer, false)
            })
        }
    }

    fun update(data: BountyData): BountyInfo {
        objectiveFlags = data.objectives.map { it.logicId }.toSet()
        tooltip = genTooltip(data)
        return this
    }

    @Suppress("RemoveRedundantQualifierName")
    companion object : ItemDataJson<BountyInfo>() {
        override val identifier = Bountiful.id("bounty_info")
        override val ser = BountyInfo.serializer()
        override val default: () -> BountyInfo = { BountyInfo() }
    }

}