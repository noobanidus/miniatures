package noobanidus.mods.miniatures.commands;

import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.util.StringUtil;
import net.minecraftforge.fml.loading.FMLPaths;
import noobanidus.mods.miniatures.Miniatures;
import noobanidus.mods.miniatures.entity.MiniMeEntity;
import noobanidus.mods.miniatures.util.NullProfileCache;
import noobanidus.mods.miniatures.util.ProfileCache;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

public class CommandMiniatures {
  public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
    dispatcher.register(Commands.literal("minis").requires(p -> p.hasPermission(2))
        .executes(c -> {
          c.getSource().sendSuccess(new TextComponent("minis cache | minis validate"), false);
          return 1;
        })
        .then(cacheBuilder(Commands.literal("cache")))
        .then(validateBuilder(Commands.literal("validate"))));
  }

  public static LiteralArgumentBuilder<CommandSourceStack> validateBuilder(LiteralArgumentBuilder<CommandSourceStack> builder) {
    builder.executes(c -> {

      return 1;
    });
    return builder;
  }

  public static LiteralArgumentBuilder<CommandSourceStack> cacheBuilder(LiteralArgumentBuilder<CommandSourceStack> builder) {
    builder.executes(c -> {
      c.getSource().sendSuccess(new TextComponent("minis cache add <username> (add a user to the cache) | minis cache preload (preloads cached usernames that don't exist) | cache save <filename> (saves cache to filename) | minis cache load <filename> (loads cache from filename, root directory) | minis cache reset (resets cache)"), false);
      return 1;
    });
    builder.then(Commands.literal("add").then(Commands.argument("username", StringArgumentType.word()).executes(c -> {
      String username = StringArgumentType.getString(c, "username");
      ProfileCache.cache(username);
      c.getSource().sendSuccess(new TextComponent("Added " + username + " to the user name cache."), false);
      return 1;
    })));
    builder.then(Commands.literal("preload").executes(c -> {
      c.getSource().sendSuccess(new TextComponent("Beginning pre-load, this could take some time..."), false);
      int counter = 0;
      for (String name : ProfileCache.cache()) {
        if (StringUtil.isNullOrEmpty(name)) {
          continue;
        }
        if (NullProfileCache.isCachedNull(name, null)) {
          continue;
        }

        GameProfile profile = new GameProfile(null, name);
        MiniMeEntity.updateGameProfile(profile);
        counter++;
      }
      c.getSource().sendSuccess(new TextComponent("Pre-loaded " + counter + " skins."), false);
      return 1;
    }));
    builder.then(Commands.literal("save").then(Commands.argument("filename", StringArgumentType.word()).executes(c -> {
      String filename = StringArgumentType.getString(c, "filename");
      Path savefile = FMLPaths.GAMEDIR.get().resolve(filename);
      if (Files.exists(savefile)) {
        c.getSource().sendFailure(new TextComponent("Over-writing file named " + filename));
      }
      try {
        BufferedWriter buffer = new BufferedWriter(new FileWriter(savefile.toFile()));
        for (String name : ProfileCache.cache()) {
          buffer.write(name);
          buffer.newLine();
        }
        buffer.close();
        c.getSource().sendSuccess(new TextComponent("Saved cache to " + filename), false);
      } catch (IOException e) {
        c.getSource().sendFailure(new TextComponent("Failed to write to file named " + filename + ". See log for error."));
        Miniatures.LOG.error("Failed to write to file named " + filename, e);
        return -1;
      }
      return 1;
    })));
    builder.then(Commands.literal("load").then(Commands.argument("filename", StringArgumentType.word()).executes(c -> {
      String filename = StringArgumentType.getString(c, "filename");
      Path savefile = FMLPaths.GAMEDIR.get().resolve(filename);
      if (!Files.exists(savefile)) {
        c.getSource().sendFailure(new TextComponent("File doesn't exist to load: " + filename));
        return -1;
      }

      try {
        BufferedReader buffer = new BufferedReader(new FileReader(savefile.toFile()));
        Set<String> cache = new HashSet<>();
        buffer.lines().forEach(o -> {
          if (!StringUtil.isNullOrEmpty(o.trim())) {
            cache.add(o.trim());
          }
        });
        ProfileCache.cache(cache);
        c.getSource().sendSuccess(new TextComponent("Loaded " + cache.size() + " names from " + filename), false);
      } catch (IOException e) {
        c.getSource().sendFailure(new TextComponent("Failed to load names from " + filename));
        return -1;
      }
      return 1;
    })));
    builder.then(Commands.literal("reset").executes(c -> {
      ProfileCache.clear();
      c.getSource().sendSuccess(new TextComponent("Reset the profile cache."), false);
      return 1;
    }));
    return builder;
  }
}

