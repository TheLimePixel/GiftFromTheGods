package com.emosewapixel.giftfromthegods;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.server.FMLServerAboutToStartEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

@Mod("giftfromthegods")
public class GiftFromTheGods {
	private static List<Item> items;
	protected static int delay;
	
	public GiftFromTheGods() {
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueue);
		
		new ModConfig();
		ForgeConfigSpec CONFIG = ModConfig.COMMON;
		CommentedFileConfig configData = CommentedFileConfig.builder(FMLPaths.CONFIGDIR.get().resolve("giftfromthegods.toml"))
				.writingMode(WritingMode.REPLACE)
				.autosave()
				.sync()
				.build();
		
		configData.load();
		CONFIG.setConfig(configData);
		
		MinecraftForge.EVENT_BUS.register(this);
	}
	
	private void enqueue(final InterModEnqueueEvent e) {
		delay = ModConfig.time.get();
		List<? extends String> blacklist = ModConfig.blackList.get();
		if (ModConfig.invertBlackList.get())
			items = blacklist.stream().map(s -> ForgeRegistries.ITEMS.getValue(new ResourceLocation(s))).collect(Collectors.toList());
		else if (!ModConfig.blackList.get().isEmpty())
			items = ForgeRegistries.ITEMS.getEntries().stream().filter(pair -> !blacklist.contains(pair.getKey().toString())).map(Map.Entry::getValue).collect(Collectors.toList());
		else items = new ArrayList<>(ForgeRegistries.ITEMS.getValues());
	}
	
	@SubscribeEvent
	public void onServerAboutToStart(FMLServerAboutToStartEvent e) {
		new DelayCommand(e.getServer().getCommandManager().getDispatcher());
	}
	
	@SubscribeEvent
	public void onWorldTick(TickEvent.PlayerTickEvent e) {
		World world = e.player.world;
		if (e.phase == TickEvent.Phase.START && !world.isRemote && world.getGameTime() % delay == 0)
			giveItem(items.get((int) (items.size() * new Random().nextFloat())), e.player);
	}
	
	private static void giveItem(Item item, PlayerEntity player) {
		ItemStack stack = new ItemStack(item);
		boolean flag = player.inventory.addItemStackToInventory(stack);
		if (flag) {
			ItemEntity itementity1 = player.dropItem(stack, false);
			if (itementity1 != null)
				itementity1.makeFakeItem();
			
			player.world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.2F, ((player.getRNG().nextFloat() - player.getRNG().nextFloat()) * 0.7F + 1.0F) * 2.0F);
			player.container.detectAndSendChanges();
		} else {
			ItemEntity itementity = player.dropItem(stack, false);
			if (itementity != null) {
				itementity.setNoPickupDelay();
				itementity.setOwnerId(player.getUniqueID());
			}
		}
	}
}