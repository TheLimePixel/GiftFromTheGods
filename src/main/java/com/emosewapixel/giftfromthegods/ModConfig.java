package com.emosewapixel.giftfromthegods;

import net.minecraftforge.common.ForgeConfigSpec;

import java.util.Collections;
import java.util.List;

public class ModConfig {
	public static final ForgeConfigSpec COMMON;
	public static final ForgeConfigSpec.IntValue time;
	public static final ForgeConfigSpec.ConfigValue<List<? extends String>> blackList;
	public static final ForgeConfigSpec.BooleanValue invertBlackList;
	
	static {
		ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
		time = builder.comment("Sets how much time (in ticks) is needed for players to get their next random item").defineInRange("Delay", 200, 1, Integer.MAX_VALUE);
		blackList = builder.comment("List of items that won't given to players").defineList("Blacklist", Collections.emptyList(), s -> s instanceof String);
		invertBlackList = builder.comment("When enabled, the blacklist becomes a whitelist").define("Invert Blacklist", false);
		COMMON = builder.build();
	}
}
