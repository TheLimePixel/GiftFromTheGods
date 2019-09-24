package com.emosewapixel.giftfromthegods;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.util.*;

@Mod(modid = GiftFromTheGods.MODID, name = GiftFromTheGods.NAME, version = GiftFromTheGods.VERSION)
public class GiftFromTheGods {
	private static NonNullList<ItemStack> items = NonNullList.create();
	
	public static final String MODID = "giftfromthegods";
	public static final String NAME = "Gift From The Gosa";
	public static final String VERSION = "1.0.1";
	
	@Mod.EventHandler
	public static void init(FMLInitializationEvent e) {
		List<String> blackList = Arrays.asList(ModConfig.blackList);
		if (ModConfig.invertBlackList)
			blackList.stream().map(s -> ForgeRegistries.ITEMS.getValue(new ResourceLocation(s))).filter(Objects::nonNull).forEach(i -> i.getSubItems(CreativeTabs.SEARCH, items));
		else if (!blackList.isEmpty())
			ForgeRegistries.ITEMS.getEntries().stream().filter(pair -> !blackList.contains(pair.getKey().toString())).map(Map.Entry::getValue).forEach(i -> i.getSubItems(CreativeTabs.SEARCH, items));
		else ForgeRegistries.ITEMS.getValues().forEach(i -> i.getSubItems(CreativeTabs.SEARCH, items));
	}
	
	@Mod.EventBusSubscriber(modid = GiftFromTheGods.MODID)
	public static class Events {
		@SubscribeEvent
		public static void onWorldTick(TickEvent.PlayerTickEvent e) {
			World world = e.player.world;
			if (e.phase == TickEvent.Phase.START && !world.isRemote && world.getTotalWorldTime() % ModConfig.time == 0)
				giveItem(items.get((int) (items.size() * new Random().nextFloat())), e.player);
		}
	}
	
	private static void giveItem(ItemStack item, EntityPlayer player) {
		ItemStack stack = item.copy();
		boolean flag = player.inventory.addItemStackToInventory(stack);
		if (flag) {
			EntityItem itementity1 = player.dropItem(stack, false);
			if (itementity1 != null)
				itementity1.makeFakeItem();
			
			player.world.playSound(null, player.posX, player.posY, player.posZ, SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 0.2F, ((player.getRNG().nextFloat() - player.getRNG().nextFloat()) * 0.7F + 1.0F) * 2.0F);
			player.openContainer.detectAndSendChanges();
		} else {
			EntityItem itementity = player.dropItem(stack, false);
			if (itementity != null) {
				itementity.setNoPickupDelay();
				itementity.setOwner(player.getName());
			}
		}
	}
}