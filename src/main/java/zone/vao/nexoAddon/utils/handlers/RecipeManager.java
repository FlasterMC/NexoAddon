package zone.vao.nexoAddon.utils.handlers;

import com.nexomc.nexo.api.NexoItems;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.SmithingTransformRecipe;
import zone.vao.nexoAddon.NexoAddon;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class RecipeManager {
    @Getter
    private static final List<NamespacedKey> registeredRecipes = new ArrayList<>();
    @Getter
    @Setter
    private static File recipeFile;
    @Getter
    @Setter
    private static FileConfiguration recipeConfig;


    public static void addSmithingTransformRecipe(String recipeId, FileConfiguration config) {
        ItemStack resultTemplate = parseItem(config, recipeId + ".result");
        RecipeChoice.ExactChoice template = parseRecipeChoice(config, recipeId + ".template");
        RecipeChoice.ExactChoice base = parseRecipeChoice(config, recipeId + ".base");
        RecipeChoice.ExactChoice addition = parseRecipeChoice(config, recipeId + ".addition");

        if (resultTemplate == null || template == null || base == null || addition == null) {
            throw new IllegalArgumentException("Invalid recipe configuration for " + recipeId);
        }

        NamespacedKey key = new NamespacedKey(NexoAddon.instance, recipeId);

        if (NexoAddon.instance.getServer().getRecipe(key) == null) {
            NexoAddon.instance.foliaLib.getScheduler().runNextTick(registerRecipe -> {
                SmithingTransformRecipe recipe = new SmithingTransformRecipe(key, resultTemplate, template, base, addition);
                NexoAddon.instance.getServer().addRecipe(recipe);
                registeredRecipes.add(key);
                NexoAddon.instance.getLogger().info("Registered smithing transform recipe: " + recipeId);
            });
        } else {
            NexoAddon.instance.getLogger().info("Recipe " + recipeId + " already exists, skipping.");
        }
    }

    private static ItemStack parseItem(FileConfiguration config, String path) {
        String nexoItemId = config.getString(path + ".nexo_item");
        if (nexoItemId != null && NexoItems.itemFromId(nexoItemId) != null)
            return NexoItems.itemFromId(nexoItemId).build().clone();

        Object itemObj = config.get(path + ".minecraft_item");
        if (itemObj instanceof String materialName) {
            Material material = Material.matchMaterial(materialName);
            return material != null ? new ItemStack(material).clone() : null;
        }
        if (itemObj instanceof ItemStack) {
            return ((ItemStack) itemObj).clone();
        } else if (itemObj instanceof org.bukkit.configuration.ConfigurationSection) {
            ItemStack item = config.getItemStack(path + ".minecraft_item");
            if (item != null) return item.clone();
        }

        String materialName = config.getString(path + ".minecraft_item");
        if(materialName == null) {
            NexoAddon.instance.getLogger().warning("Wrong item in " + path);
            return null;
        }
        Material material = Material.matchMaterial(materialName);
        return material != null ? new ItemStack(material).clone() : null;
    }

    private static RecipeChoice.ExactChoice parseRecipeChoice(FileConfiguration config, String path) {
        String nexoItemId = config.getString(path + ".nexo_item");
        if (nexoItemId != null && NexoItems.itemFromId(nexoItemId) != null)
            return new RecipeChoice.ExactChoice(NexoItems.itemFromId(nexoItemId).build().clone());

        Object itemObj = config.get(path + ".minecraft_item");
        if (itemObj instanceof String materialName) {
            Material material = Material.matchMaterial(materialName);
            return material != null ? new RecipeChoice.ExactChoice(new ItemStack(material).clone()) : null;
        }
        if (itemObj instanceof ItemStack) {
            return new RecipeChoice.ExactChoice(((ItemStack) itemObj).clone());
        } else if (itemObj instanceof org.bukkit.configuration.ConfigurationSection) {
            ItemStack item = config.getItemStack(path + ".minecraft_item");
            if (item != null) return new RecipeChoice.ExactChoice(item.clone());
        }

        String materialName = config.getString(path + ".minecraft_item");
        if(materialName == null) {
            NexoAddon.instance.getLogger().warning("Wrong item in " + path);
            return null;
        }
        Material material = Material.matchMaterial(materialName);
        return material != null ? new RecipeChoice.ExactChoice(new ItemStack(material).clone()) : null;
    }

    public static void clearRegisteredRecipes() {
        if(registeredRecipes.isEmpty()) return;

        for (NamespacedKey key : registeredRecipes) {
            NexoAddon.instance.getServer().removeRecipe(key);
            NexoAddon.instance.getLogger().info("Removed recipe: " + key.getKey());
        }
        registeredRecipes.clear();
    }
}
