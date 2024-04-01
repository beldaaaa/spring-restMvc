package springframework.spring6restmvc.services;

import com.opencsv.bean.CsvToBeanBuilder;
import org.springframework.stereotype.Service;
import springframework.spring6restmvc.model.BeerCSVRecord;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;
@Service
public class BeerCsvServiceImpl implements BeerCsvService {
    @Override
    public List<BeerCSVRecord> convertCSV(File csvFile) {
        try {
            List<BeerCSVRecord> beerCSVRecords = new CsvToBeanBuilder<BeerCSVRecord>(new FileReader(csvFile))
                    .withType(BeerCSVRecord.class)//explicitly saying "withType", so that's going to handle the mapping
                    //(it's going to bind it to the POJO(the annotated POJO with a mapping info on it)
                    .build().parse();//parse action will return the list of records
            return beerCSVRecords;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
