package lt.remmers.interview;

import lt.remmers.interview.parser.ParserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.Stream;

import static org.junit.jupiter.params.provider.Arguments.arguments;

@SpringBootTest
public class ParsersTest {

    @Autowired
    private ParserService parserService;

    @ParameterizedTest
    @MethodSource("badWordsTasks")
    public void testBadWordsTasks(String expected, String argument) {
        Assertions.assertEquals(expected, parserService.convertString(argument));
    }

    private static Stream<Arguments> badWordsTasks() {
        return Stream.of(
                arguments(
                        """
                                #FT 41725#3
                                #FT 41705 weiß lasierend#3
                                #RAL 7006#3"""
                        , """
                                Gebinde
                                (LTR)
                                3489-20 GW 306 Sonder FT 41725 3 20
                                1807-20 LW-715E/30 Sonder
                                FT 41705
                                weiß
                                lasierend 3
                                20
                                1692-20 DW 601/20 Aqua-Stopp Sonder RAL 7006 3 20
                                Liefertermin:   29.08.2023"""),


                arguments("""
                                #FT 41242#3""",
                        """
                                Gebinde 
                                (LTR)
                                1807-20 LW-715E/30 Sonder FT 41242 3 20
                                Liefertermin:   07.12.2023
                                BV: Lager"""),
                arguments("""
                                #RC 660#1
                                #RC 260#1""",
                        """
                                Gebinde 
                                (LTR)
                                1807-05 LW-715E/30 Sonder RC 660 1 5
                                1807-05 LW-715E/30 Sonder RC 260 1 5
                                Liefertermin:   31.08.2023
                                BV: Charlottenbogen"""),
                arguments("""
                                #RAL 7039#1
                                #NCS S-2010-Y20R#1""",
                        """
                                Gebinde 
                                (LTR)
                                1692-20 DW 601/20 Aqua-Stopp Sonder RAL 7039 1 20
                                1692-20 DW 601/20 Aqua-Stopp Sonder
                                NCS S-2010-
                                Y20R 1 20
                                Liefertermin:   15.09.2023
                                BV: Schopenhauer Straße""")
        );
    }
}
