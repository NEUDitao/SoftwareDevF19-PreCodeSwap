package cs4500.hw2;

/*
  A StrJ is one of the following kinds of well-formed JSON values:
  -- Strings
  -- Arrays that start with a String
  -- Objects that have a key "this" whose value is a String
  No other well-formed JSON values are valid StrJs.
*/

import com.google.gson.*;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

/**
 * A StrJParser parses StrJ as defined in the data definition above.
 */
public class StrJParser {

  public static void main(String[] argv) {
    boolean up = argv.length > 0 && argv[0].equals("-up");

    String parsedStrJ = testableMain(new InputStreamReader(System.in), up);

    System.out.println(parsedStrJ);
  }

  public static String testableMain(Reader in, boolean up) {
    Objects.requireNonNull(in);
    JsonStreamParser p = new JsonStreamParser(in);

    List<JsonElement> elements = new ArrayList<>();
    while (p.hasNext()) {
      JsonElement e = p.next();
        elements.add(e);
    }

    Comparator<JsonElement> jsonComp = Comparator.comparing(StrJParser::sortKey);

    elements.sort(up ? jsonComp : jsonComp.reversed());

    return elements.toString();
  }



  private static String sortKey(JsonElement input) {
    Objects.requireNonNull(input);

    if (input.isJsonPrimitive()) {
      return input.getAsString();
    } else if (input.isJsonArray()) {
      return input.getAsJsonArray().get(0).getAsString();
    } else {
      return input.getAsJsonObject().get("this").getAsString();
    }
  }
}
