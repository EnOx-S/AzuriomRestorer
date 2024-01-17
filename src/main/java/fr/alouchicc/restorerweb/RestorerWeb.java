package fr.alouchicc.restorerweb;

import net.skinsrestorer.api.SkinsRestorer;
import net.skinsrestorer.api.exception.DataRequestException;
import net.skinsrestorer.api.exception.MineSkinException;
import net.skinsrestorer.api.property.InputDataResult;
import net.skinsrestorer.api.storage.PlayerStorage;
import net.skinsrestorer.api.storage.SkinStorage;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

public final class RestorerWeb extends JavaPlugin implements Listener {

    private SkinsRestorer skinsRestorerAPI;
    public File jobFile;

    @Override
    public void onEnable() {
        System.out.println("§7Plugin RestorerWeb by §3AlouchiCC §7fix by §3EnOx_S");
        getServer().getPluginManager().registerEvents(this, this);

        createFile("config");
        FileConfiguration config = YamlConfiguration.loadConfiguration(getFile("config"));
        try {
            config.save(getFile("config"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private void createFile(String fileName){
        if(!getDataFolder().exists()){
            getDataFolder().mkdir();
        }
        File file = new File(getDataFolder(), fileName + ".yml");

        if(!file.exists()){
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
    public File getFile(String fileName){
        return new File(getDataFolder(), fileName + ".yml");
    }

    @EventHandler
    public void Skins(PlayerJoinEvent event) {
        try {
            Player player = event.getPlayer();
            SkinStorage skinStorage = skinsRestorerAPI.getSkinStorage();
            Optional<InputDataResult> result = skinStorage.findOrCreateSkinData(getConfig().getString("url") + player.getName());
            PlayerStorage playerStorage = skinsRestorerAPI.getPlayerStorage();
            playerStorage.setSkinIdOfPlayer(player.getUniqueId(), result.get().getIdentifier());
            skinsRestorerAPI.getSkinApplier(Player.class).applySkin(player);
        } catch (DataRequestException | MineSkinException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onDisable() {
        System.out.println("§7Plugin RestorerWeb by §3AlouchiCC §7fix by §3EnOx_S");
    }
}