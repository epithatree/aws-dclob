package parser;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import lombok.extern.log4j.Log4j2;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Queue;

@Log4j2
public class CsvParser {

    public void start(Queue messageQueue) {
        CsvMapper mapper = new CsvMapper();
        CsvSchema schema = mapper.schemaFor(Message.class);
        ObjectReader reader = mapper.reader(schema);
        try {
            File csvFile =new File(this.getClass().getResource( "/input.csv" ).toURI() );
            MappingIterator<Message> mappingIterator = reader.forType(Message.class).readValues(csvFile);
            while (mappingIterator.hasNext()) {
                Message next = mappingIterator.next();
                log.info(next);
            }
        } catch (IOException e) {
            log.error("Unable to parse input file", e);
        } catch (URISyntaxException e) {
            log.error("Unable to open input file", e);
        }
    }
}
