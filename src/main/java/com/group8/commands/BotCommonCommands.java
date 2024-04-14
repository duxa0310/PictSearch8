package com.group8.commands;

import com.group8.Bot;
import com.group8.getcources.Cource;
import com.group8.getcources.GetUser;
import com.group8.getcources.User;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.group8.Bot.list;

public class BotCommonCommands {

    public static Bot bot;

    @AppBotCommand(name = "/start", description = "start", showInHelp = false)
    String start() {
        bot.start();
        return "Здравствуйте, я - бот PictSearch.";
    }

    @AppBotCommand(name = "/list", description = "Full List", showInHelp = false)
    public String fullList() {
        List<Cource> list = Bot.list;
        switch (bot.filter) {
            case "по длительности" : {
                list.sort(Comparator.comparingInt(e -> Integer.parseInt(e.length.split("\\s")[0])));
                break;
            }
            case "по рейтингу" : {
                list.sort(Comparator.comparingDouble(e -> Double.parseDouble(e.rate)));
                break;
            }
            case "по цене" : {
                list.sort(Comparator.comparingInt(e -> Integer.parseInt(e.cost.split("\\s")[1])));
                break;
            }
            case "обычный" : {
                break;
            }
        }
        List<Cource> res = new ArrayList<>();
        for (Cource c : list) {
            if (containsAnyWords(c.name, bot.keywords)) {
                res.add(c);
            }
        }

        bot.printList(res);
        return null;
    }



    public static User user; // в файл Bot

    @AppBotCommand(name = "/auth", description = "Creatnig acc", showInHelp = false)
    String auth() {
        // считываем имя, желаемого времени, цeны и рейтинга курсов
        String name = "Vanay", wantedTime = "1 месяц", wantedPrice = "1000  за курс", wantedRating = "5";
        // указывается считанные данные

        user = new GetUser().getUser(name);
        user.required_price = wantedPrice;
        user.required_time = wantedTime;
        user.required_rate = wantedRating;
        return "Вы успешно прошли тест";
        // выполнить /lk
    }

    /*@AppBotCommand(name = "/addcouces", description = "Adding cources", showInHelp = false)
    String add() {
        // вывод сообщения "Выберите номер курса, который вы хотите добавить к себе"
        // ввод номера желаемого курса
        int wantedNumber = 1; // <- ввод от пользователя
        user.userCourcesAndProgress.put(list[wantedNumber - 1], "0");
        return "Вы успешно добавили курс" + list[wantedNumber - 1].name.substring(7, list[wantedNumber - 1].name.length()) + " к себе";
    }

    @AppBotCommand(name = "/deletecource", description = "Deleting cources", showInHelp = false)
    String delete() {
        // вывод сообщения "Выберите номер курса, который вы хотите удалить к себе"
        // ввод номера желаемого курса
        int wantedNumber = 1; // <- ввод от пользователя
        user.userCourcesAndProgress.remove(list[wantedNumber - 1]);
        return "Вы успешно удалили курс " + list[wantedNumber - 1].name.substring(7, list[wantedNumber - 1].name.length());
    }

    @AppBotCommand(name = "/updatecourceprogress", description = "Deleting cources", showInHelp = false)
    String updateprogress() {
        // вывод сообщения "Выберите номер курса, прогресс по которому вы хотите обновить"
        // ввод номера желаемого курса
        int wantedNumber = 1; // <- ввод от пользователя
        int pp = Integer.parseInt(user.userCourcesAndProgress.get(wantedNumber - 1));
        if(pp <= 90 ) {
            user.userCourcesAndProgress.put(list[wantedNumber - 1], Integer.toString(pp));
            return "Вы успешно обновили прогресс по курсу " + list[wantedNumber - 1].name.substring(7, list[wantedNumber - 1].name.length());

        }
        else{
            return "Курс уже пройден";
        }
        // возврат на главную
    }*/

    @AppBotCommand(name = "/compare", description = "Compare cources", showInHelp = false)
    String Compare (){ // потом -> public string
        int p = 0;
        Cource[] neededCources = new Cource[3];
        for (int i = 0; i < user.compareArray.length; i++) {
            if (user.compareArray[i] != null) {
                neededCources[p] = user.compareArray[i];
                p++;
            }
        }
        if(p < 2){
            return ("Добавьте в сравнение хотя бы 2 элемента");
        }
        else{
            String s = "";
            s += "Табллица сравнения \n Название:\t";
            for (int i = 0; i < p; i++) {
                s += neededCources[i].name + "\t";
            }
            s += "\nСтоимость:\t";
            for (int i = 0; i < p; i++) {
                s += neededCources[i].cost + "\t";
            }
            s+="\nПродолжительнось\t";
            for (int i = 0; i < p; i++) {
                s += neededCources[i].length + "\t";
            }
            s+="\nРейтинг\t";
            for (int i = 0; i < p; i++) {
                s += neededCources[i].rate + "\t";
            }
            // вывод всех neededCources до курса с индексом p (на самом деле всегда  p = 3 или p = 4)
            // вывод в формате таблицы где столбцы - все (/ почти все)
            return s;
        }

    }

    @AppBotCommand(name = "/lk", description = "LK", showInHelp = false)
    void lk() {
        // вывод кнопок /auth /mycources /домой
    }
    @AppBotCommand(name = "/mycouces", description = "mycouces", showInHelp = false)
    String mycources() {
        String s = "Ваши курсы: ";
        if(user.userCourcesAndProgress.isEmpty())
            return "У вас пока нету курсов";
        for(Cource r: user.userCourcesAndProgress.keySet()){
            s += user.userCourcesAndProgress.get(r);
        }
        return s;
        // вывод кнопок /addcouces /updatecourceprogress /deletecource
    }

    public static boolean containsAnyWords(String word, List<String> keywords) {
        for (String k : keywords)
            if (word.contains(k)) return true;
        return false;
    }

    @AppBotCommand(name = "/filter", description = "Filter", showInHelp = false)
    String filter() {
        bot.filter();
        return null;
    }

    @AppBotCommand(name = "/add", description = "Add keywords", showInHelp = false)
    String suggest() {
        bot.addKeywords();
        return null;
    }

    @AppBotCommand(name = "/clear", description = "Clear keywords", showInHelp = false)
    String clear() {
        bot.keywords.clear();
        return null;
    }

    @AppBotCommand(name = "/help", description = "when request help", showInKeyBoard = false)
    String help() {
        return "Вы воспользовались командой /help. В дальнейшем в данном разделе могут быть доступные команды бота.";
    }

}
