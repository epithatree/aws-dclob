package parser;

import java.util.concurrent.LinkedBlockingQueue;

public class CsvParserTest {

    public static void main(String[] args) {
        CsvParser parser = new CsvParser();
        parser.start(new LinkedBlockingQueue<>());
    }

}