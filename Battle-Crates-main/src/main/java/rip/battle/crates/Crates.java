package rip.battle.crates;

import cc.stormworth.core.util.command.rCommandHandler;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.Configuration;
import org.bukkit.plugin.java.JavaPlugin;
import rip.battle.crates.airdrop.AirdropListener;
import rip.battle.crates.crate.Crate;
import rip.battle.crates.crate.listeners.CrateListener;
import rip.battle.crates.reward.RewardsListeners;

@Getter
public class Crates extends JavaPlugin {

  @Getter private static Crates instance;

  private MongoDatabase mongoDatabase;

  @Override
  public void onEnable() {
    instance = this;

    saveDefaultConfig();
    loadMongo();

    Crate.load();

    Bukkit.getPluginManager().registerEvents(new CrateListener(), this);
    Bukkit.getPluginManager().registerEvents(new RewardsListeners(), this);
    Bukkit.getPluginManager().registerEvents(new AirdropListener(), this);

    rCommandHandler.registerPackage(this, "rip.battle.crates.crate.commands");
    rCommandHandler.registerPackage(this, "rip.battle.crates.reward.commands");
  }

  @Override
  public void onDisable() {
    Crate.getCrates().values().forEach(Crate::save);
  }

  private void loadMongo() {
    Configuration configuration = getConfig();
    if (configuration.getBoolean("mongo.authentication.enabled")) {
      ServerAddress serverAddress =
          new ServerAddress(
              configuration.getString("mongo.host"), configuration.getInt("mongo.port"));

      MongoCredential credential =
          MongoCredential.createCredential(
              configuration.getString("mongo.authentication.username"),
              "admin",
              configuration.getString("mongo.authentication.password").toCharArray());

      mongoDatabase =
          new MongoClient(serverAddress, credential, MongoClientOptions.builder().build())
              .getDatabase(getConfig().getString("mongo.database"));
    } else {
      mongoDatabase =
          new MongoClient(configuration.getString("mongo.host"), configuration.getInt("mongo.port"))
              .getDatabase(getConfig().getString("mongo.database"));
    }
  }
}