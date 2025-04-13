package com.fmv.healthkiosk.feature.telemedicine.data.source.local;

import com.fmv.healthkiosk.feature.telemedicine.domain.model.ChatMessage;
import com.fmv.healthkiosk.feature.telemedicine.utils.ChatbotCommands;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DummyChatbotMessageGenerator {
    private final ArrayList<ChatMessage> chatMessages = new ArrayList<>();
    private int messageIdCounter = 0;

    public ArrayList<ChatMessage> handleUserMessage(String userInput) {
        if (userInput == null) {
            chatMessages.clear();
            chatMessages.add(new ChatMessage(messageIdCounter++, "Good morning, I am the doctor's bot assistant", false));
            chatMessages.add(new ChatMessage(messageIdCounter++, "What can I help you with today?", false));
            chatMessages.add(new ChatMessage(messageIdCounter++, ChatbotCommands.STARTING_MENU, false));
        } else if (userInput.equals(ChatbotCommands.RESCHEDULE_DATE)) {
            chatMessages.add(new ChatMessage(messageIdCounter++, "Reschedule appointment date", true));
            chatMessages.add(new ChatMessage(messageIdCounter++, "Here are the recommendations for you", false));
            chatMessages.add(new ChatMessage(messageIdCounter++, ChatbotCommands.SUGGESTION_DATE_MENU, false));
            chatMessages.add(new ChatMessage(messageIdCounter++, "Not suitable for you?", false));
            chatMessages.add(new ChatMessage(messageIdCounter++, ChatbotCommands.ANOTHER_DATE_MENU, false));
        } else {
            if (userInput.equals(ChatbotCommands.DONE_RESCHEDULE_DATE)) {
                chatMessages.add(new ChatMessage(messageIdCounter++, "Select another date", true));
            } else if (isValidDate(userInput)) {
                chatMessages.add(new ChatMessage(messageIdCounter++, userInput, true));
            } else {
                chatMessages.add(new ChatMessage(messageIdCounter++, userInput, true));
                chatMessages.add(new ChatMessage(messageIdCounter++, "Sorry, AI is still in the developing process!", false));
                return new ArrayList<>(chatMessages); // Important: return a copy to trigger LiveData observer
            }

            chatMessages.add(new ChatMessage(messageIdCounter++, "Thank you, your new appointment date has been created", false));
            chatMessages.add(new ChatMessage(messageIdCounter++, "And has been forwarded to the doctor", false));
            chatMessages.add(new ChatMessage(messageIdCounter++, "Have a nice day!", false));
        }

        return new ArrayList<>(chatMessages); // Important: return a copy to trigger LiveData observer
    }

    private boolean isValidDate(String input) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd MMMM", Locale.ENGLISH);
            sdf.setLenient(false); // Makes parsing stricter
            sdf.parse(input);
            return true;
        } catch (ParseException e) {
            return false;
        }
    }
}
