package com.tkachenko.BasicTelegramBot.service.mainServiceBlocks.psychologicalHealth;

import com.tkachenko.BasicTelegramBot.dto.tg.Intermediate;
import com.tkachenko.BasicTelegramBot.dto.tg.messages.BasicInformationMessage;
import com.tkachenko.BasicTelegramBot.model.psychologicalHealth.Emotion;
import com.tkachenko.BasicTelegramBot.model.psychologicalHealth.ThoughtUser;
import com.tkachenko.BasicTelegramBot.model.psychologicalHealth.UserEmotion;
import com.tkachenko.BasicTelegramBot.repository.psychologicalHealth.EmotionRepository;
import com.tkachenko.BasicTelegramBot.repository.psychologicalHealth.UserEmotionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.List;
import java.util.Map;

@Service
@AllArgsConstructor
public class FillUserEmotion {
    private final EmotionRepository emotionRepository;
    private final UserEmotionRepository userEmotionRepository;

    public void checkAndCreateEmotion(BasicInformationMessage basicInformationMessage,
                               SendMessage sendMessage,
                               Map<String, Intermediate> intermediateData)
    {
        Long chatId = basicInformationMessage.getUserTelegram().getChatId();
        ThoughtUser thoughtUser = intermediateData.get(chatId.toString()).getThoughtUser();
        List<Emotion> emotions = intermediateData.get(chatId.toString()).getEmotions();
        List<UserEmotion> userEmotions = intermediateData.get(chatId.toString()).getUserEmotions();
        if(emotions != null && !emotions.isEmpty())
        {
            emotionRepository.saveAll(emotions);
        }
        if(userEmotions != null && !userEmotions.isEmpty())
        {
            for(UserEmotion userEmotion : userEmotions)
            {
                userEmotion.setThought(thoughtUser);
                userEmotionRepository.save(userEmotion);
            }
        }
    }
}
