package com.websystique.springmvc.service;


import com.google.common.collect.Lists;
import com.websystique.springmvc.message.SimpleMessage;
import com.websystique.springmvc.model.DBEntry;
import com.websystique.springmvc.repo.DBEntryRepository;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



import java.io.IOException;
import java.util.Iterator;
import java.util.List;


@Service("entryService")
public class EntryService {

    @Autowired
    DBEntryBufferService bufferService;

    @Autowired
    DBEntryRepository repository;

    private static final Logger log = LoggerFactory.getLogger(EntryService.class);

    public List<DBEntry> findAllEntries(){
        refreshEntryService();
        return  Lists.newArrayList(repository.findAll());
    }

    public SimpleMessage saveAllEntriesFromWorkbook(Workbook workbook) throws IOException {
        log.info("Workbook reading is now");
        int counter_right = 0;
        int counter_wrong = 0;

        try {
            Sheet firstSheet = workbook.getSheetAt(0);
            Iterator<Row> iterator = firstSheet.iterator();
            iterator.next();//skip the first row

            outer: while (iterator.hasNext()) {
                Row nextRow = iterator.next();
                Iterator<Cell> cellIterator = nextRow.cellIterator();
                DBEntry dbEntry = new DBEntry();
                while (cellIterator.hasNext()) {
                    Cell cell = cellIterator.next();
                    try {//it should be deleted with normal understanding what is an appropriate file
                        switch (cell.getColumnIndex()) {
                            case 0:
                                String num=getValueFromCell(cell);
                                if (check_num(num)) {
                                    dbEntry.setDate1(num);
                                }else {
                                    throw new IllegalStateException("Wrong num format");
                                }
                                dbEntry.setNum(new Long(num));
                                break;
                            case 1:
                                String date=getValueFromCell(cell);
                                if (check_date(date)) {
                                    dbEntry.setDate1(date);
                                }else {
                                    throw new IllegalStateException("Wrong date format");
                                }
                                break;
                            case 2:
                                dbEntry.setString(getValueFromCell(cell));
                                break;
                        }
                    } catch (Exception e){
                        System.out.println("Caught "+e.toString());
                        counter_wrong++;
                        continue outer;
                    }
                }
                counter_right ++;
                bufferService.add(dbEntry);
            }
            return new SimpleMessage(String.format("Uploaded : %d \nWith wrong format : %d",counter_right,counter_wrong));
        }catch (NumberFormatException e){
            e.printStackTrace();
            throw new IllegalStateException("IllegalWorkbooksRowsFormat");
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            workbook.close();
        }
    }

    public void saveEntry(DBEntry entry){
        bufferService.add(entry);
    }

    public void refreshEntryService(){
        bufferService.flush();
    }

    private String getValueFromCell(Cell cell){
        switch (cell.getCellType()) {
            case Cell.CELL_TYPE_STRING:
                return cell.getStringCellValue();
            case Cell.CELL_TYPE_BOOLEAN:
                return ((Object) cell.getBooleanCellValue()).toString();
            case Cell.CELL_TYPE_NUMERIC:
                return ((Object) cell.getNumericCellValue()).toString();
        }
        throw new IllegalStateException("Some wrong types of fields!");
    }

    public static boolean check_date(String str){
        return str.matches("^([0-2]\\d|30|31).{1}(0\\d|10|11|12).{1}\\d{4}\\s([0-1]\\d|20|21|22|23|24).{1}(0\\d|[1-5]\\d).{1}(0\\d|[1-5]\\d)$");
    }

    public static boolean check_num(String str){
        return str.matches("^\\d{1,18}$");
    }
}
