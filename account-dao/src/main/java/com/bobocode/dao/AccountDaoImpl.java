package com.bobocode.dao;

import com.bobocode.exception.AccountDaoException;
import com.bobocode.model.Account;
import com.google.inject.internal.cglib.core.$DebuggingClassWriter;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.List;

public class AccountDaoImpl implements AccountDao {
    private EntityManagerFactory emf;

    public AccountDaoImpl(EntityManagerFactory emf) {
        this.emf = emf;
    }

    @Override
    public void save(Account account) {
        EntityManager entityManager = emf.createEntityManager();
        try{
            entityManager.getTransaction().begin();
            entityManager.persist(account);
            entityManager.getTransaction().commit();
        } catch (RuntimeException e) {
            entityManager.getTransaction().rollback();
            throw new AccountDaoException("account is invalid", e);
        }
    }

    @Override
    public Account findById(Long id) {
        EntityManager entityManager = emf.createEntityManager();
        return entityManager
                .createQuery("select a from Account a where a.id = :id", Account.class)
                .setParameter("id", id)
                .getSingleResult();
    }

    @Override
    public Account findByEmail(String email) {
        EntityManager entityManager = emf.createEntityManager();
        return entityManager.createQuery("select a from Account a where a.email = :email", Account.class)
                .setParameter("email", email)
                .getSingleResult();
    }

    @Override
    public List<Account> findAll() {
        EntityManager entityManager = emf.createEntityManager();
        return entityManager.createQuery("select a from Account a", Account.class)
                    .getResultList();
    }

    @Override
    public void update(Account account) {
        EntityManager entityManager = emf.createEntityManager();
        try {
            entityManager.getTransaction().begin();
            entityManager.merge(account);
            entityManager.getTransaction().commit();
        } catch (RuntimeException e) {
            entityManager.getTransaction().rollback();
            throw new AccountDaoException("account is invalid", e);
        }
    }

    @Override
    public void remove(Account account) {
        EntityManager entityManager = emf.createEntityManager();
       entityManager.getTransaction().begin();
        Account managedAccount = entityManager.merge(account);
        entityManager.remove(managedAccount);
       entityManager.getTransaction().commit();
    }
}

