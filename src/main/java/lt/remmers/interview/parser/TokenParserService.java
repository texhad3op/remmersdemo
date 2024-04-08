package lt.remmers.interview.parser;

import lombok.Getter;
import lt.remmers.interview.parser.data.TokenType;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;

@Service
public class TokenParserService {

    private final Function<Iterator<String>, List<String>> digitDigitParser = iterator -> {
        String valueFirst = getDigit(iterator);
        String valueSecond = getDigit(iterator);
        assert valueFirst != null;
        assert valueSecond != null;
        return List.of(valueFirst, valueSecond);
    };

    private final Function<Iterator<String>, List<String>> textDigitParser = iterator -> {
        String valueFirst = getValue(iterator);
        String valueSecond = getDigit(iterator);
        assert valueFirst != null;
        assert valueSecond != null;
        return List.of(valueFirst, valueSecond);
    };

    private final Function<Iterator<String>, List<String>> digitTextDigitParser = iterator -> {
        List<String> result = new ArrayList<>();
        String valueFirst = getDigit(iterator);
        assert valueFirst != null;
        result.add(valueFirst);
        String value = getValue(iterator);
        StringBuilder sb = new StringBuilder();
        while (Boolean.FALSE.equals(isDigit(value))) {
            sb.append(" ");
            sb.append(value);
            value = getValue(iterator);
        }
        result.add(sb.toString());
        result.add(value);
        return result;
    };

    @Getter
    private final Map<TokenType, Function<Iterator<String>, List<String>>> parsers = Map.of(
            TokenType.FT, digitTextDigitParser,
            TokenType.RAL, digitDigitParser,
            TokenType.RC, digitDigitParser,
            TokenType.NCS, textDigitParser
    );

    private String getValue(Iterator<String> iterator) {
        if (iterator.hasNext()) {
            return iterator.next();
        }
        return Strings.EMPTY;
    }

    private String getDigit(Iterator<String> iterator) {
        while (iterator.hasNext()) {
            String candidate = iterator.next();
            if (Boolean.TRUE.equals(isDigit(candidate))) {
                return candidate;
            }
        }
        return null;
    }

    private Boolean isDigit(String candidate) {
        return Objects.nonNull(candidate) ? candidate.chars()
                .allMatch(Character::isDigit) : null;
    }

}
