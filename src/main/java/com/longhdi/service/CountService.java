package com.longhdi.service;

import com.longhdi.entity.VideoCount;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class CountService {

    @PersistenceContext(unitName = "default")
    private EntityManager entityManager;

    public VideoCount incrementTotalVideoCount() {
        VideoCount totalVideoCount = getCurrentCount();
        totalVideoCount.increaseOne();
        return totalVideoCount;
    }

    public VideoCount getCurrentCount() {
        return entityManager
                .createNamedQuery("VideoCount.countAllVideo", VideoCount.class)
                .getSingleResult();
    }

}
