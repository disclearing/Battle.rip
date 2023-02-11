package rip.battle.crates.crate;

import cc.stormworth.core.CorePlugin;
import cc.stormworth.core.kt.util.ItemBuilder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bson.Document;
import org.bukkit.inventory.ItemStack;

@Getter
@AllArgsConstructor
public class CratePlaceholder {

  private ItemStack item;
  private int slot;

  public CratePlaceholder(Document document) {
    this.item = CorePlugin.GSON.fromJson(document.getString("item"), ItemStack.class);
    this.slot = document.getInteger("slot");
  }

  public ItemStack getPreview() {
    return new ItemBuilder(item.clone())
        .name(" ")
        .build();
  }

  public Document serialize(){
    return new Document("item", CorePlugin.GSON.toJson(item))
        .append("slot", slot);
  }
}