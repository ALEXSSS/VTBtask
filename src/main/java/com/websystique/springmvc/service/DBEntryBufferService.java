package com.websystique.springmvc.service;

import com.google.common.collect.Iterables;
import com.websystique.springmvc.model.DBEntry;
import com.websystique.springmvc.repo.DBEntryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.LinkedList;

@Service
@Scope("singleton")
public class DBEntryBufferService {
    @Autowired
    DBEntryRepository repository;

    private static final Logger log = LoggerFactory.getLogger(DBEntryBufferService.class);

    private final static long buffer_capacity = 200_000;

//    private ConcurrentLinkedQueue<EventRecord> buffer;
//    private LongAdder ...

    private LinkedList<DBEntry> buffer;
    public DBEntryBufferService(){
        buffer=new LinkedList<>();
    }

    public void add(DBEntry record) {
        log.info("Add DBEntry method with current size : {}");
        synchronized (this) {
            buffer.add(record);
        }
        if (buffer.size() > buffer_capacity) {
            synchronized (this) {
                if (buffer.size() > buffer_capacity) {
                    unsafe_flush();
                }
            }
        }
    }

    public void flush(){
        synchronized (this){
            unsafe_flush();
        }
    }

    private void unsafe_flush(){
        repository.saveAll(buffer);
        buffer=new LinkedList<>();
    }

    public int getBufferSize(){
        synchronized (this) {
            return buffer.size();
        }
    }


}

