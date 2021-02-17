package com.bwee.webit.service;

import com.bwee.webit.datasource.DbService;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Slf4j
public abstract class SimpleCrudService<T> implements CrudService<T> {

    private final DbService dbService;

    public SimpleCrudService(final DbService dbService) {
        this.dbService = dbService;
    }

    @Override
    public void save(T model) {
        dbService.save(model);
    }

    @Override
    public void saveAll(Collection<T> model) {
        dbService.saveAll(model);
    }

    @Override
    public void saveAll(T model, T... more) {
        dbService.saveAll(Lists.asList(model, more));
    }

    @Override
    public Optional<T> findById(String id) {
        return dbService.findById(id);
    }

    @Override
    public void deleteById(String id) {
        dbService.deleteById(id);
    }

    @Override
    public List<T> findAll(Pageable pageable) {
        return dbService.findAll(pageable);
    }
}
