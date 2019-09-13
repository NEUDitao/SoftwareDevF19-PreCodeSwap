package cs4500.hw2;

/*
  A StrJ is one of the following kinds of well-formed JSON values:
  -- Strings
  -- Arrays that start with a String
  -- Objects that have a key "this" whose value is a String
  No other well-formed JSON values are valid StrJs.
*/

import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonStreamParser;
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

  /**
   * Reads the command line arguments, and gives instructions and System.in to methods that handle
   * the rest.
   * @param argv command line arguments
   */
  public static void main(String[] argv) {
    boolean up = argv.length > 0 && argv[0].equals("--up");

    String parsedStrJ = testableMain(new InputStreamReader(System.in), up);

    System.out.println(parsedStrJ);
  }

  /**
   * Sorts the input on @param{in} based on @param{up}, and gives a JSON list.
   * @param in the JSON to be sorted
   * @param up if the keys should be sorted "upwards" on strings
   * @return a JSON list
   */
  public static String testableMain(Reader in, boolean up) {
    Objects.requireNonNull(in);
    JsonStreamParser p = new JsonStreamParser(in);

    List<JsonElement> elements = new ArrayList<>();
    try {
      while (p.hasNext()) { // will throw a JSONIOException if "in" is empty. Thanks Google.
        JsonElement e = p.next();
        elements.add(e);
      }
    } catch (JsonIOException e) {
      // if given invalid JSON, or an empty input, blow up gracefully
      return "[]";
    }

    Comparator<JsonElement> jsonComp = Comparator.comparing(StrJParser::sortKey);

    elements.sort(up ? jsonComp : jsonComp.reversed());

    return elements.toString();
  }

  /**
   * Gives the key for sorting on @param{input}.
   * @param input element to extract the key from
   * @return the sorting key for the element
   */
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
