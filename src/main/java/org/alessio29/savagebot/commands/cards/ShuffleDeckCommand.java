package org.alessio29.savagebot.commands.cards;

import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.alessio29.savagebot.cards.Deck;
import org.alessio29.savagebot.cards.Decks;
import org.alessio29.savagebot.cards.Hands;
import org.alessio29.savagebot.commands.Category;
import org.alessio29.savagebot.commands.ICommand;
import org.alessio29.savagebot.internal.CommandExecutionResult;

import java.util.List;
import java.util.stream.Collectors;


public class ShuffleDeckCommand implements ICommand {

	@Override
	public String getName() {
		return "shuffle";
	}

	@Override
	public String[] getAliases() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public Category getCategory() {
		return Category.CARDS;
	}

	@Override
	public String getDescription() {
		return "Shuffles current deck, resets secret cards dealt to all users in this channel";
	}

	@Override
	public String[] getArguments() {
		return null;
	}

	@Override
	public CommandExecutionResult execute(MessageReceivedEvent event, String[] args) throws Exception {
		Deck deck = Decks.getDeck(event.getGuild(), event.getChannel());
		deck.shuffle();	
		Guild guild = event.getGuild();

		List<User> users = event.getGuild().getMembers().stream().
				filter(m -> m.hasPermission(event.getTextChannel(), Permission.MESSAGE_READ)).
				filter(m->m.getOnlineStatus()==OnlineStatus.ONLINE).map(m -> m.getUser()).collect(Collectors.toList());
		for (User user : users) {
			Hands.getHand(guild, user).clear();
		}
		return new CommandExecutionResult("\nShuffled...\n", 1);
	}


}
