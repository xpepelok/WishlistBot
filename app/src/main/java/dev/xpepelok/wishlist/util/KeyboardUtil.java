package dev.xpepelok.wishlist.util;

import dev.xpepelok.wishlist.data.telegram.button.Button;
import lombok.experimental.UtilityClass;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public final class KeyboardUtil {
    public static List<List<InlineKeyboardButton>> formCallBackButtons(Button... buttons) {
        List<List<InlineKeyboardButton>> inlineButtons = new ArrayList<>();
        List<InlineKeyboardButton> inlineKeyboardButtonList = new ArrayList<>();

        for (Button button : buttons) {
            if (button == null) {
                continue;
            }
            InlineKeyboardButton inlineKeyboardButton = new InlineKeyboardButton();
            inlineKeyboardButton.setText(button.name());
            inlineKeyboardButton.setCallbackData(button.callBack());
            inlineKeyboardButtonList.add(inlineKeyboardButton);
        }

        inlineButtons.add(inlineKeyboardButtonList);
        return inlineButtons;
    }
}