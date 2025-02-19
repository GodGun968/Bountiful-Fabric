package io.ejekta.bountiful.client

import io.ejekta.bountiful.Bountiful
import io.ejekta.bountiful.bounty.BountyRarity
import io.ejekta.bountiful.content.board.BoardBlockEntity
import io.ejekta.bountiful.content.gui.BoardScreenHandler
import io.ejekta.bountiful.content.gui.widgets.BountyLongButton
import io.ejekta.kambrik.KambrikHandledScreen
import io.ejekta.kambrik.gui.KSpriteGrid
import io.ejekta.kambrik.gui.widgets.KListWidget
import io.ejekta.kambrik.gui.widgets.KScrollbarVertical
import net.minecraft.client.MinecraftClient
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.EntityType
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerInventory
import net.minecraft.screen.ScreenHandler
import net.minecraft.text.Text
import net.minecraft.util.Formatting
import net.minecraft.util.Identifier
import net.minecraft.util.registry.Registry
import org.spongepowered.asm.util.Quantifier.SINGLE


class BoardScreen(handler: ScreenHandler, inventory: PlayerInventory, title: Text) :
    KambrikHandledScreen<ScreenHandler>(handler, inventory, title) {

    val boardHandler: BoardScreenHandler
        get() = handler as BoardScreenHandler

    init {
        sizeToSprite(BOARD_BG)
    }

    private val bgGui = kambrikGui {
        sprite(BOARD_BG)
    }

    private val buttons = (0 until 21).map { BountyLongButton(this, it) }

    private val validButtons: List<BountyLongButton>
        get() = buttons.filter { it.getBountyData().objectives.isNotEmpty() }

    private val scroller = KScrollbarVertical(120, SLIDER, 0x0)

    private val buttonList = object : KListWidget<BountyLongButton>(
        { validButtons }, 160, 20, 6, Orientation.VERTICAL, Mode.SINGLE,
        { listWidget, item, selected ->
            widget(item)
        }
    ) {
        override fun canClickThrough() = true
    }.apply {
        attachScrollbar(scroller)
    }

    val fgGui = kambrikGui {
        val levelData = BoardBlockEntity.levelProgress(boardHandler.totalDone)
        val percentDone = (levelData.second.toDouble() / levelData.third * 100).toInt()

        // Reputation Bar (background, foreground, label)
        offset(240, 56) {
            sprite(BAR_BG)
            sprite(BAR_FG, w = percentDone + 1)
            textCentered(94, -10) {
                color(0xabff7a)
                addLiteral(levelData.first.toString()) {
                    format(BountyRarity.forReputation(levelData.first).color)
                }
            }
            offset(85, -19) {
                if (isHovered(18, 18)) {
                    tooltip {
                        addLiteral("Reputation ") {
                            addLiteral("(${levelData.first})") {
                                format(BountyRarity.forReputation(levelData.first).color)
                            }
                        }
                    }
                }
            }
        }

        // GUI Title
        textCentered(titleX - 53, titleY + 1) {
            color = 0xEADAB5
            add(title)
        }

        widget(buttonList, 5, 18)

        if (validButtons.isEmpty()) {
            textCentered(85, 78) {
                color = 0xEADAB5
                addLiteral("It's Empty! Check back soon!")
            }
        } else {
            // Scroll bar
            widget(scroller, 166, 18)
        }

    }

    override fun onDrawBackground(matrices: MatrixStack, mouseX: Int, mouseY: Int, delta: Float) {
        bgGui.draw(matrices, mouseX, mouseY, delta)
    }

    override fun onDrawForeground(matrices: MatrixStack, mouseX: Int, mouseY: Int, delta: Float) {
        fgGui.draw(matrices, mouseX, mouseY, delta)
    }

    override fun init() {
        super.init()
        titleX = (backgroundWidth - textRenderer.getWidth(title)) / 2
    }

    companion object {
        private val TEXTURE = Bountiful.id("textures/gui/container/new_board.png")
        private val WANDER = Identifier("textures/gui/container/villager2.png")

        private val BOARD_SHEET = KSpriteGrid(TEXTURE, texWidth = 512, texHeight = 256)
        private val BOARD_BG = BOARD_SHEET.Sprite(0f, 0f, 348, 146)

        private val WANDER_SHEET = KSpriteGrid(WANDER, texWidth = 512, texHeight = 256)
        private val BAR_BG = WANDER_SHEET.Sprite(0f, 186f, 102, 5)
        private val BAR_FG = WANDER_SHEET.Sprite(0f, 191f, 102, 5)
        private val SLIDER = WANDER_SHEET.Sprite(0f, 199f, 6, 26)
    }
}

