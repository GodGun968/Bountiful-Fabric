package io.ejekta.bountiful.mixin;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(NbtCompound.class)
public interface MutableNbtCompound {
    @Accessor("entries")
    Map<String, NbtElement> getAllTags();

}
