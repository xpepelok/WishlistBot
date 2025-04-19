package dev.xpepelok.wishlist.data.command;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Getter
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum CommandResponse {
    START_MESSAGE(
            """
                    Приветствую!
                    
                    Данный бот представляет собой некую «корзину», в которой Вы можете сохранять желаемые товары!
                    
                    Используйте /help для ознакомления с командами
                    """
    ),
    USER_DATA_MSG(
            """
                    Ваш профиль:
                    
                    Имя: %s, ID: %s\
                    
                    Количество записей в виш-листе: %d
                    
                    Используйте /wishes для отображения листа
                    """
    ),
    WISH_FORM(
            """
                    Название: %s\
                    
                    Ссылка: %s
                    """
    );
    String rawMessage;

    public String format(Object... args) {
        return String.format(rawMessage, args);
    }
}