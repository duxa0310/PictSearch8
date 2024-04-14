package com.group8;

import com.group8.commands.BotCommonCommands;
import com.group8.getcources.GetCources;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.BotSession;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

public class Main {

    public static void main(String[] args) throws TelegramApiException {
        Bot.list = new GetCources().getCources();
        TelegramBotsApi api = new TelegramBotsApi(DefaultBotSession.class);
        Bot bot = new Bot();
        BotCommonCommands.bot = bot;
        BotSession botSession = api.registerBot(bot);
    }

}
