package games.crusader.orderbook.socket;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

class DataConverter
{
    public static JsonObject decodeMessage(String encodedData)
      throws DataFormatException, UnsupportedEncodingException
    {
        byte[] compressedData = Base64.getDecoder().decode(encodedData);

        var inflater = new Inflater(true);
        inflater.setInput(compressedData);
        var buffer = new byte[1024];
        var resultBuilder = new StringBuilder();
        while (inflater.inflate(buffer) > 0) {
            resultBuilder.append(new String(buffer, "UTF-8"));
            buffer = new byte[1024];
        }
        inflater.end();

        var text = resultBuilder.toString().trim();
        return new JsonParser().parse(text).getAsJsonObject();
    }
}