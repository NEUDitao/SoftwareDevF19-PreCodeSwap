package cs4500.hw2;

import static org.junit.jupiter.api.Assertions.*;

import java.io.StringReader;

class StrJParserTest {

  @org.junit.jupiter.api.Test
  void testTestableMain() {
    String actual = "    [\"b\",1,2,                        3]\n"
        + "\n"
        + "      \"a\"   {\"this\" : \"c\",\n"
        + "\n"
        + "         \"other\" : 0}";
    String expected = "[\"a\", [\"b\",1,2,3], {\"this\":\"c\",\"other\":0}]";
    String expected2 = "[{\"this\":\"c\",\"other\":0}, [\"b\",1,2,3], \"a\"]";


    assertEquals(expected,
        StrJParser.testableMain(new StringReader(actual), true));
    assertEquals(expected2,
        StrJParser.testableMain(new StringReader(actual), false));


    assertEquals("", StrJParser.testableMain(new StringReader(""), true));
    assertEquals("", StrJParser.testableMain(new StringReader(""), false));
  }

}