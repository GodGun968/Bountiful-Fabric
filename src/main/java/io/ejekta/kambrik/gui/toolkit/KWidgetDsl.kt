package io.ejekta.kambrik.gui.toolkit

import net.minecraft.client.gui.Element
import net.minecraft.client.gui.ParentElement

open class KWidgetDsl(
    var drawFunc: KambrikGuiDsl.() -> Unit = {},
    open val width: Int,
    open val height: Int,
) : ParentElement {

    fun onDraw(func: KambrikGuiDsl.() -> Unit) {
        drawFunc = func
    }

    val children = mutableListOf<Element>()

    override fun children(): MutableList<out Element> {
        return children
    }

    override fun isDragging(): Boolean {
        return false
    }

    override fun setDragging(dragging: Boolean) {
        //
    }

    override fun getFocused(): Element? {
        return null
    }

    override fun setFocused(focused: Element?) {
        //
    }

}