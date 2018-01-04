package com.websystique.springmvc.controller;

import java.io.*;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;


import com.websystique.springmvc.message.SimpleMessage;
import com.websystique.springmvc.model.DBEntry;
import com.websystique.springmvc.repo.DBEntryRepository;
import com.websystique.springmvc.service.EntryService;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/VTB")
public class MainController {

    private static final Logger log = LoggerFactory.getLogger(MainController.class);


    @Autowired
    EntryService entryService;

    @Autowired
    DBEntryRepository repository;


    @RequestMapping(value = "/entry/", method = RequestMethod.GET)
    public ResponseEntity<List<DBEntry>> listAllEntries() {
        List<DBEntry> entries = entryService.findAllEntries();
        if (entries.isEmpty()) {
            return new ResponseEntity<List<DBEntry>>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<List<DBEntry>>(entries, HttpStatus.OK);
    }


    @RequestMapping(value = "/entry/", method = RequestMethod.POST)
    public ResponseEntity<Void> createEntry(@RequestBody DBEntry entry, UriComponentsBuilder ucBuilder) {
        System.out.println("Creating entry " + entry.getNum());
        try {
            entryService.saveEntry(entry);
            entryService.refreshEntryService();
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<Void>(HttpStatus.BAD_REQUEST);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/entry/{id}").buildAndExpand(entry.getId()).toUri());
        return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
    }

    @ResponseBody
    @RequestMapping(value = "/file/", method = RequestMethod.POST)
    public SimpleMessage createUser(HttpServletRequest request) throws IOException {
        log.info("/file/ mapping now is happening!");
        try {
            MultipartHttpServletRequest mRequest =
                    (MultipartHttpServletRequest) request;
            mRequest.getParameterMap();
            Iterator<String> itr = mRequest.getFileNames();
            MultipartFile mFile = mRequest.getFile(itr.next());
            InputStream in = mFile.getInputStream();

            Workbook workbook = new XSSFWorkbook(in);
            return entryService.saveAllEntriesFromWorkbook(workbook);
        } catch (Exception e) {
            return new SimpleMessage("Bad document");
        }
    }


    @RequestMapping(value = "/entry/{id}", method = RequestMethod.PUT)
    public ResponseEntity<DBEntry> updateEntry(@PathVariable("id") long id, @RequestBody DBEntry entry) {
        System.out.println("Updating entry " + id);

        Optional<DBEntry> currentEntry = repository.findById(id);

        if (!currentEntry.isPresent()) {
            System.out.println("Entry with id " + id + " not found");
            return new ResponseEntity<DBEntry>(HttpStatus.NOT_FOUND);
        }
        DBEntry entryForChange = currentEntry.get();
        repository.delete(entryForChange);

        entryForChange.setNum(entry.getNum());
        entryForChange.setDate1(entry.getDate1());
        entryForChange.setString(entry.getString());

        repository.save(entry);
        return new ResponseEntity<DBEntry>(entryForChange, HttpStatus.OK);
    }


    @RequestMapping(value = "/entry/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<DBEntry> deleteEntry(@PathVariable("id") long id) {
        System.out.println("Fetching & Deleting Entry with id " + id);

        Optional<DBEntry> entry = repository.findById(id);
        if (!entry.isPresent()) {
            System.out.println("Unable to delete. Entry with id " + id + " not found");
            return new ResponseEntity<DBEntry>(HttpStatus.NOT_FOUND);
        }

        repository.delete(entry.get());
        return new ResponseEntity<DBEntry>(HttpStatus.NO_CONTENT);
    }


}



