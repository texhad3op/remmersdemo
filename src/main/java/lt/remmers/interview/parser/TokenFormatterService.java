package lt.remmers.interview.parser;

import lombok.Getter;
import lt.remmers.interview.parser.data.TokenResolver;
import lt.remmers.interview.parser.data.TokenType;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.function.Function;

@Service
public class TokenFormatterService {

    private final Function<TokenResolver, String> digitDigitFormatter = tokenResolver ->
            "#%s %s#%s".formatted(
                    tokenResolver.getTokenType().name(),
                    tokenResolver.getValues().getFirst(),
                    tokenResolver.getValues().getLast()
            );

    private final Function<TokenResolver, String> digitTextDigitFormatter = tokenResolver ->
            "#%s %s%s#%s".formatted(
                    tokenResolver.getTokenType().name(),
                    tokenResolver.getValues().getFirst(),
                    tokenResolver.getValues().get(1),
                    tokenResolver.getValues().getLast());

    @Getter
    private final Map<TokenType, Function<TokenResolver, String>> formatters = Map.of(
            TokenType.FT, digitTextDigitFormatter,
            TokenType.RAL, digitDigitFormatter,
            TokenType.RC, digitDigitFormatter,
            TokenType.NCS, digitDigitFormatter
    );
}
