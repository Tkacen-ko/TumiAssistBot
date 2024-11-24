package com.tkachenko.BasicTelegramBot.service.mainServiceBlocks.psychologicalHealth;

import com.tkachenko.BasicTelegramBot.dto.tg.Intermediate;
import com.tkachenko.BasicTelegramBot.dto.tg.messages.BasicInformationMessage;
import com.tkachenko.BasicTelegramBot.dto.tg.messages.MessagesHistoryData;
import com.tkachenko.BasicTelegramBot.model.UserTelegram;
import com.tkachenko.BasicTelegramBot.model.psychologicalHealth.Emotion;
import com.tkachenko.BasicTelegramBot.model.psychologicalHealth.ThoughtUser;
import com.tkachenko.BasicTelegramBot.model.psychologicalHealth.UserEmotion;
import com.tkachenko.BasicTelegramBot.repository.UserRepository;
import com.tkachenko.BasicTelegramBot.repository.psychologicalHealth.EmotionRepository;
import com.tkachenko.BasicTelegramBot.repository.psychologicalHealth.ThoughtUserRepository;
import com.tkachenko.BasicTelegramBot.service.tg.respondent.buttons.ButtonsBuilder;
import com.tkachenko.BasicTelegramBot.service.tg.respondent.commands.constantElementsCommands.ButtonConstant;
import com.tkachenko.BasicTelegramBot.service.tg.respondent.commands.constantElementsCommands.TitleButtonConstant;
import com.tkachenko.BasicTelegramBot.service.tg.respondent.utils.LimitedSizeMessageList;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class FillingThoughtUser {

    private final UserRepository userRepository;
    private final ButtonsBuilder buttonsBuilder;
    private final ThoughtUserRepository thoughtUserRepository;
    private final EmotionRepository emotionRepository;

    public void fillThought(BasicInformationMessage basicInformationMessage,
                            SendMessage sendMessage,
                            Map<String, Intermediate> intermediateData)
    {
        Long chatId = basicInformationMessage.getUserTelegram().getChatId();
        String textMessage = basicInformationMessage.getMessageText();

        ThoughtUser thoughtUser = new ThoughtUser(basicInformationMessage.getHistoryReceivedMessages().get(1).getMessageText());
        intermediateData.get(chatId.toString()).setThoughtUser(thoughtUser);


        thoughtUser.setTags(generateHashtags(thoughtUser.getTitle()));

        String textAnswer = "Для упрощения поиска для вашей мысли " +
                "автоматически сгенерированы следующие теги:_\n";
        textAnswer += String.join(", ", thoughtUser.getTags());
        textAnswer += "_\n\nЕсли вы желаете установить пользовательский набор тегов для " +
                "мысли или хотите добавить какую либо эмоцию к мысли, в ответ на это сообщение " +
                "отправьте теги в виде \"#тегМысли\" а мысли в виде \"&эмоцияМысли\".\n" +
                "Все слова с символом \"#\" вначале будут определены как теги а " +
                "слова с \"&\" как эмоции. Либо если вы хотите просто сохранить мысль с " +
                "дефолтным набором тегов, нажмите \"Сохранить мысль\".";
        sendMessage.setText(textAnswer);
        Map<String, String> buttonData = new LinkedHashMap<>();
        buttonData.putAll(ButtonConstant.SAVE_THOUGHT_DEFAULT_TAGS);
        buttonData.putAll(ButtonConstant.CANCEL_CREATION);
        sendMessage.setReplyMarkup(buttonsBuilder.createMessageWithButtons(buttonData));
    }

    public void saveThought(BasicInformationMessage basicInformationMessage,
                            SendMessage sendMessage,
                            Map<String, Intermediate> intermediateData)
    {
        Long chatId = basicInformationMessage.getUserTelegram().getChatId();
        ThoughtUser thoughtUser = intermediateData.get(chatId.toString()).getThoughtUser();
        thoughtUser.setUser(userRepository.findByChatId(chatId).get());
        intermediateData.get(chatId.toString()).setThoughtUser(thoughtUserRepository.save(thoughtUser));
    }

    public void addTagsAndEmotions(BasicInformationMessage basicInformationMessage,
                                   SendMessage sendMessage,
                                   Map<String, Intermediate> intermediateData)
    {
        Long chatId = basicInformationMessage.getUserTelegram().getChatId();
        ThoughtUser thoughtUser = intermediateData.get(chatId.toString()).getThoughtUser();
        String textMessage = basicInformationMessage.getMessageText();
        String textAnswer = "";

        Set<String >userTags = thoughtUser.getTags();
        userTags.clear();
        Set<String >newUserTags = Arrays.stream(textMessage.split("\\s+"))
                .filter(word -> word.startsWith("#")) // Выбираем слова с #
                .map(word -> word.substring(1).toLowerCase()) // Убираем # и приводим к нижнему регистру
                .limit(5)
                .collect(Collectors.toSet());
        if(!newUserTags.isEmpty())
        {
            userTags.clear();
            thoughtUser.setTags(newUserTags);
            textAnswer += "Для вашего сообщения установлен новый набор тегов:\n_";
            textAnswer += String.join(", ", newUserTags) + "_\n\n";
        }
        UserTelegram user = userRepository.findByChatId(chatId).get();

        List<Emotion> emotions = Arrays.stream(textMessage.split("\\s+"))
                .filter(word -> word.startsWith("&"))
                .map(word -> word.substring(1).toLowerCase())
                .map(word -> new Emotion(null, word))
                .collect(Collectors.toList());

        if(!emotions.isEmpty())
        {
            List<UserEmotion> userEmotion = emotions.stream()
                    .map(emotion -> new UserEmotion(user, emotion))
                    .collect(Collectors.toList());
            intermediateData.get(chatId.toString()).setEmotions(emotions);
            intermediateData.get(chatId.toString()).setUserEmotions(userEmotion);
            textAnswer += "Для вашего сообщения добавлены следующие эмоции:\n_";
            textAnswer += emotions.stream().map(emotion -> emotion.getTitle()) // Преобразуем объекты в их title
                    .collect(Collectors.joining(", "))  + "_\n\n";
        }
        if(textAnswer.isEmpty())
        {
            textAnswer += "В переданной сообщение не обнаруженно тегов или эмоций в указанном выше формате " +
                    "вы можете установить пользовательский набор тегов для " +
                    "мысли или хотите добавить какую либо эмоцию к мысли, в ответ на это сообщение " +
                    "отправьте теги в виде \"#тегМысли\" а мысли в виде \"&эмоцияМысли\".\n" +
                    "Все слова с символом \"#\" вначале будут определены как теги а " +
                    "слова с \"&\" как эмоции. Либо если вы хотите просто сохранить мысль с " +
                    "дефолтным набором тегов, нажмите \"Сохранить мысль\".";
        }
        else
        {
            textAnswer += "Вы можете сохранить сообщение либо нажать \"Отмена\" если передумали";
        }
        Map<String, String> buttonData = new LinkedHashMap<>();
        buttonData.put(TitleButtonConstant.SAVE_THOUGHT_DEFAULT, ButtonConstant.SAVE_THOUGHT_DEFAULT_BUTTON_CODE);
        buttonData.putAll(ButtonConstant.CANCEL_CREATION);
        sendMessage.setReplyMarkup(buttonsBuilder.createMessageWithButtons(buttonData));

        sendMessage.setText(textAnswer);
    }

    public void thereIsUnsavedThought(BasicInformationMessage basicInformationMessage,
                                      SendMessage sendMessage,
                                      Map<String, Intermediate> intermediateData)
    {
        String textAnswer = "Вы не сохранили мысль. Сохранить или отменить процесс?";
        sendMessage.setText(textAnswer);
        Map<String, String> buttonData = new LinkedHashMap<>();
        buttonData.putAll(ButtonConstant.SAVE_THOUGHT_DEFAULT_TAGS);
        buttonData.putAll(ButtonConstant.CANCEL_CREATION);
        sendMessage.setReplyMarkup(buttonsBuilder.createMessageWithButtons(buttonData));
    }

    public static Set<String> generateHashtags(String message) {
        Set<String> stopWords = Set.of(
                "это", "того", "такой", "который", "другой", "данный",
                "тот", "эта", "их", "его", "моя", "ваша", "своя", "твой", "мы", "вы", "они",
                "там", "тут", "здесь", "куда", "откуда"
        );

        List<String> filteredWords = Arrays.stream(message.split("\\s+"))
                .map(word -> word.replaceAll("[^a-zA-Zа-яА-Я0-9]", "")) // Убираем лишние символы
                .map(String::toLowerCase) // Приводим к нижнему регистру
                .filter(word -> word.length() > 2 && !stopWords.contains(word)) // Фильтруем по длине и стоп-словам
                .distinct() // Убираем дубли
                .collect(Collectors.toList()); // Сохраняем в List для удобной работы

        // Если слов меньше 5, возвращаем их все с добавлением тега
        if (filteredWords.size() <= 5) {
            return filteredWords.stream()
                    .map(word -> "#" + word)
                    .collect(Collectors.toSet());
        }

        // Случайно выбираем 5 слов
        Collections.shuffle(filteredWords); // Перемешиваем список
        return filteredWords.stream()
                .limit(3) // Берём первые 3 слов после перемешивания
                .map(word -> "#" + word) // Добавляем символ тега
                .collect(Collectors.toSet());
    }
}
