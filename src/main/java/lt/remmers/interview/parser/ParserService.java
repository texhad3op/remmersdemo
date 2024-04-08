package lt.remmers.interview.parser;

import lombok.RequiredArgsConstructor;
import lt.remmers.interview.parser.data.TokenResolver;
import lt.remmers.interview.parser.data.TokenType;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ParserService {

    private static final String NEW_LINE = "\n";
    private static final String MINUS = "-";
    private static final String MINUS_AND_NEW_LINE_SYMBOL = "-\n";
    private static final String NEW_LINE_SYMBOL_AND_MINUS = "-\n";

    private static final List<String> validTokens = Arrays.stream(TokenType.values()).map(TokenType::name).toList();

    private final TokenParserService tokenParserService;
    private final TokenFormatterService tokenFormatterService;

    public String convertString(String source) {
        List<String> split = Arrays.stream(fixWrongNewLineSymbols(source).split("[\n ]")).toList();
        Iterator<String> iterator = split.stream().iterator();
        List<TokenResolver> result = new ArrayList<>();
        while (iterator.hasNext()) {
            TokenType parsedTokenType = getTokenType(iterator);
            if (!parsedTokenType.equals(TokenType.NONE)) {
                List<String> values = tokenParserService
                        .getParsers()
                        .get(parsedTokenType)
                        .apply(iterator);

                result.add(
                        TokenResolver
                                .builder()
                                .tokenType(parsedTokenType)
                                .values(values).build()
                );
            }
        }
        return result
                .stream()
                .map(tokenResolver -> tokenFormatterService
                        .getFormatters()
                        .get(tokenResolver.getTokenType())
                        .apply(tokenResolver))
                .collect(Collectors.joining(NEW_LINE));
    }

    /**
     * I guess the strings
     * NCS S-2010-
     * Y20R 1 20
     * should be fixed, because the minus in the start or in the end of the string make no sense.
     * So, I suppose the report was generated with error (real case)
     */
    private String fixWrongNewLineSymbols(String source) {
        return source
                .replaceAll(MINUS_AND_NEW_LINE_SYMBOL, MINUS)
                .replaceAll(NEW_LINE_SYMBOL_AND_MINUS, MINUS);
    }

    private TokenType getTokenType(Iterator<String> iterator) {
        if (iterator.hasNext()) {
            String token = iterator.next();
            if (validTokens.contains(token)) {
                return TokenType.valueOf(token);
            }
        }
        return TokenType.NONE;
    }

}
