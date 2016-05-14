package parser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import controller.ControllerMessage;
import lombok.extern.log4j.Log4j2;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Log4j2
public class CsvParser {

    private final CsvMapper csvMapper;
    private final CsvSchema schema;
    private final ObjectReader reader;

    private final ObjectMapper objectMapper;
    private final ExecutorService executor;

    public CsvParser() {
        csvMapper = new CsvMapper();
        schema = csvMapper.schemaFor(Message.class);
        reader = csvMapper.reader();

        objectMapper = new ObjectMapper();
        executor = Executors.newSingleThreadExecutor();
    }

    public void start(Queue<ControllerMessage> messageQueue) {
        executor.execute(() -> {
            try {
                File csvFile = new File(this.getClass().getResource("/input.csv").toURI());
                MappingIterator<Message> mappingIterator = reader.with(schema).forType(Message.class).readValues(csvFile);
                while (mappingIterator.hasNext()) {
                    try {
                        Message next = mappingIterator.next();
                        ControllerMessage controllerMessage = convertMessage(next);
                        messageQueue.add(controllerMessage);
                        log.info(controllerMessage);
                    } catch (IllegalArgumentException e) {
                        log.error("Unable to convert message to json", e);
                    }
                }
            } catch (IOException e) {
                log.error("Unable to parse input file", e);
            } catch (URISyntaxException e) {
                log.error("Unable to open input file", e);
            }
        });
    }

    private ControllerMessage convertMessage(Message message) {
        try {
            String messageString = objectMapper.writeValueAsString(message);
            return new ControllerMessage(messageString, message.getOrderId(), 0);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("Unable to convert message to json");
        }
    }
}
