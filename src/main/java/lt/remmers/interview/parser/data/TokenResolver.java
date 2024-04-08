package lt.remmers.interview.parser.data;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class TokenResolver {
    TokenType tokenType;
    List<String> values;
}
