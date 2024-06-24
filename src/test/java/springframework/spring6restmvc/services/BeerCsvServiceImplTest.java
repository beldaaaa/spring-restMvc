package springframework.spring6restmvc.services;

import org.junit.jupiter.api.Test;
import org.springframework.util.ResourceUtils;
import springframework.spring6restmvc.models.BeerCSVRecord;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class BeerCsvServiceImplTest {

    BeerCsvService beerCsvService = new BeerCsvServiceImpl();

    @Test
    void convertCSV() throws FileNotFoundException {
        File file = ResourceUtils.getFile("classpath:csvdata/beers.csv");
        List<BeerCSVRecord> recordList = beerCsvService.convertCSV(file);

        System.out.println(recordList.size());

        assertThat(recordList.size()).isGreaterThan(0);
    }
}