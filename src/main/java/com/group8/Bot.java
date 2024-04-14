package com.group8;

import com.group8.commands.AppBotCommand;
import com.group8.commands.BotCommonCommands;
import com.group8.getcources.Cource;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;


public class Bot extends TelegramLongPollingBot {

    List<Message> messages = new LinkedList<>();

    String lastCommand = "/start";
    long chatId = -1L;
    public String filter = "обычный";

    public List<String> keywords = new LinkedList<>();

    @Override
    public String getBotUsername() {
        return "pict_search_8_bot";
    }

    @Override
    public String getBotToken() {
        return "6832937102:AAHSLa9giwk4aF05pugj2R11MZ1emdr7tj8";
    }

    public static List<Cource> list;

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            Message message = update.getMessage();
            messages.add(message);
            this.chatId = message.getChatId();
            if (message.getText().startsWith("/")) {
                lastCommand = message.getText();
                try {
                    execute(runCommonCommand(message));
                } catch (TelegramApiException | InvocationTargetException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            } else {
                messages.add(message);
                if (message.getText().startsWith("по ")) {
                    filter = message.getText();
                    new BotCommonCommands().fullList();
                }
                if (message.getText().contains("Готово")) {
                }
            }
        } else {

        }
    }

    public void getKeywords() {

    }

    public void printList(List<Cource> list) {
        for (Cource c : Bot.list) {
            SendMessage msg  = new SendMessage();
            msg.setChatId(chatId);

            StringBuilder s = new StringBuilder("");
            s.append(c.name).append("\n");
            s.append(c.URL).append("\n");
            s.append(c.length).append("\n");
            s.append(c.rate).append("\n");
            s.append(c.cost);

            msg.setText(s.toString());
            msg.disableWebPagePreview();

            KeyboardRow row1 = new KeyboardRow();
            row1.add(new KeyboardButton("/filter"));

            msg.setReplyMarkup(new ReplyKeyboardMarkup(Collections.singletonList(row1)));

            try {
                execute(msg);
            } catch (TelegramApiException e) {
                throw new RuntimeException(e);
            }
        }
    }


    public void start() {
        SendMessage msg  = new SendMessage();
        msg.setChatId(chatId);
        msg.setText("Нажмите, чтобы показать список курсов");

        KeyboardRow row1 = new KeyboardRow();
        row1.add(new KeyboardButton("/list"));

        msg.setReplyMarkup(new ReplyKeyboardMarkup(Collections.singletonList(row1)));

        try {
            execute(msg);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    public void filter() {
        SendMessage msg = new SendMessage();
        msg.setChatId(chatId);
        msg.setText("Выберите фильтры:");
        List<KeyboardRow> rows = new LinkedList<>();
        KeyboardRow row1 = new KeyboardRow();
        row1.add(new KeyboardButton("по длительности"));
        row1.add(new KeyboardButton("по рейтингу"));
        KeyboardRow row2 = new KeyboardRow();
        row2.add(new KeyboardButton("по цене"));
        row2.add(new KeyboardButton("сброс"));
        rows.add(row1);
        rows.add(row2);
        msg.setReplyMarkup(new ReplyKeyboardMarkup(rows));
        try {
            execute(msg);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    public void addKeywords() {
        SendMessage msg = new SendMessage();
        msg.setChatId(chatId);
        msg.setText("Введите ключевые слова:");

        List<KeyboardRow> rows = new LinkedList<>();
        KeyboardRow row1 = new KeyboardRow();
        row1.add(new KeyboardButton("Готово"));
        rows.add(row1);
        msg.setReplyMarkup(new ReplyKeyboardMarkup(rows));

        try {
            execute(msg);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }


    private SendMessage runCommonCommand(Message message) throws InvocationTargetException, IllegalAccessException {
        String text = message.getText();
        BotCommonCommands commands = new BotCommonCommands();
        Method[] classMethods = commands.getClass().getDeclaredMethods();
        for (Method method : classMethods) {
            if (method.isAnnotationPresent(AppBotCommand.class)) {
                AppBotCommand command = method.getAnnotation(AppBotCommand.class);
                if (command.name().equals(text)) {
                    method.setAccessible(true);
                    String responseText = (String) method.invoke(commands);
                    if (responseText != null) {
                        SendMessage sendMessage = new SendMessage();
                        sendMessage.setChatId(message.getChatId().toString());
                        sendMessage.setText(responseText);
                        return sendMessage;
                    }
                }
            }
        }
        return null;
    }



    private static ArrayList<KeyboardRow> getKeyboardRows(Class someClass) {
        Method[] classMethods = someClass.getDeclaredMethods();
        ArrayList<AppBotCommand> commands = new ArrayList<>();
        for (Method method : classMethods) {
            if (method.isAnnotationPresent(AppBotCommand.class)) {
                commands.add(method.getAnnotation(AppBotCommand.class));
            }
        }
        ArrayList<KeyboardRow> keyboardRows = new ArrayList<>();
        int columCount = 3;
        int rowCount = commands.size() / columCount + ((commands.size() % columCount == 0) ? 0 : 1);
        for (int rowIndex = 0; rowIndex < rowCount; rowIndex++) {
            KeyboardRow row = new KeyboardRow();
            for (int columIndex = 0; columIndex < columCount; columIndex++) {
                int index = rowIndex * columCount + columIndex;
                if (index >= commands.size()) continue;
                AppBotCommand command = commands.get(rowIndex * columCount + columIndex);
                KeyboardButton keyboardButton = new KeyboardButton(command.name());
                row.add(keyboardButton);
            }
            keyboardRows.add(row);
        }
        return keyboardRows;
    }
}

