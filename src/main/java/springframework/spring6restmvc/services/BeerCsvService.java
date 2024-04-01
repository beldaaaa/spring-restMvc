package springframework.spring6restmvc.services;

import springframework.spring6restmvc.model.BeerCSVRecord;

import java.io.File;
import java.util.List;

//to convert CSV data into POJO
public interface BeerCsvService {
    List<BeerCSVRecord> convertCSV(File csvFile);
}
