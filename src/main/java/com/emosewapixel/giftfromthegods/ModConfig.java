package com.emosewapixel.giftfromthegods;

import net.minecraftforge.common.config.Config;

@Config(modid = GiftFromTheGods.MODID)
public class ModConfig {
	@Config.Comment("Sets how much time (in ticks) is needed for players to get their next random item")
	@Config.Name("Delay")
	@Config.RangeInt(min = 1)
	public static int time = 200;
	
	@Config.Comment("List of items that won't given to players")
	@Config.Name("Blacklist")
	public static String[] blackList = new String[0];
	
	@Config.Comment("When enabled, the blacklist becomes a whitelist")
	@Config.Name("Invert Blacklist")
	public static boolean invertBlackList = false;
}