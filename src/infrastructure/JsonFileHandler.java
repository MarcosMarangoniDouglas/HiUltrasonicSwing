package infrastructure;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class JsonFileHandler {

    private File file;

    public JsonFileHandler(String fileName) {
        file = new File(fileName);
    }

    public CustomModemMode read() {
        File folder = new File("Inputs");
        if(!folder.exists()) {
            folder.mkdir();
        }

        try (FileReader fileReader = new FileReader(file)){
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(fileReader);

            CustomModemMode customModemMode = new CustomModemMode();
            customModemMode.input = (String) jsonObject.get("input");
            customModemMode.modemBaudRate = Long.valueOf((long)jsonObject.get("fsk_baud_rate")).intValue();
            customModemMode.modemFreqHigh = Long.valueOf((long)jsonObject.get("fsk_high_freq")).intValue();
            customModemMode.modemFreqLow = Long.valueOf((long)jsonObject.get("fsk_low_freq")).intValue();
            customModemMode.modemChirpBaudRate = Long.valueOf((long)jsonObject.get("chirp_baud_rate")).intValue();
            customModemMode.modemChirpFreqHigh = Long.valueOf((long)jsonObject.get("chirp_high_freq")).intValue();
            customModemMode.modemChirpFreqLow = Long.valueOf((long)jsonObject.get("chirp_low_freq")).intValue();
            return customModemMode;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return null;
        }
    }

    public void write(CustomModemMode customModemMode) {
        File folder = new File("Inputs");
        if(!folder.exists()) {
            folder.mkdir();
        }

        try(FileWriter fileWriter = new FileWriter(file)) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("input", customModemMode.input);
            jsonObject.put("fsk_baud_rate", customModemMode.modemBaudRate);
            jsonObject.put("fsk_high_freq", customModemMode.modemFreqHigh);
            jsonObject.put("fsk_low_freq", customModemMode.modemFreqLow);
            jsonObject.put("chirp_baud_rate", customModemMode.modemChirpBaudRate);
            jsonObject.put("chirp_high_freq", customModemMode.modemChirpFreqHigh);
            jsonObject.put("chirp_low_freq", customModemMode.modemChirpFreqLow);
            String stringJson = jsonObject.toJSONString();
            fileWriter.write(stringJson);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}
