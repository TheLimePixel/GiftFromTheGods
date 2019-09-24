package com.emosewapixel.giftfromthegods;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.StringTextComponent;

public class DelayCommand {
	public DelayCommand(CommandDispatcher<CommandSource> dispatches) {
		dispatches.register(Commands
				.literal("givedelay")
				.requires(c -> c.hasPermissionLevel(2))
				.then(Commands.argument("amount", IntegerArgumentType.integer(1))
						.executes(c -> {
							GiftFromTheGods.delay = IntegerArgumentType.getInteger(c, "amount");
							c.getSource().sendFeedback(new StringTextComponent("Set give delay to " + GiftFromTheGods.delay + " ticks"), false);
							return 0;
						}))
				.executes(c -> {
					c.getSource().sendFeedback(new StringTextComponent("The give delay is currently " + GiftFromTheGods.delay + " ticks"), false);
					return 0;
				}));
	}
}