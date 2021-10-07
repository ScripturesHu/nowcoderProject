package com.hjw.dao;

import org.springframework.stereotype.Repository;

/**
 * @author hujw
 * @description
 * @create 2021-09-30 21:37
 */

@Repository("AlphaDaoHibernate")
public class AlphaDaoHibernateImpl implements AlphaDao{
    @Override
    public String select() {
        return "Hibernate";
    }
}
