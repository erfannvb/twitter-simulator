package nvb.dev.base.service.impl;

import lombok.AllArgsConstructor;
import nvb.dev.base.entity.BaseEntity;
import nvb.dev.base.repository.BaseRepository;
import nvb.dev.base.service.BaseService;
import org.hibernate.Session;
import org.hibernate.TransactionException;

import java.io.Serializable;
import java.util.Collection;
import java.util.Optional;

@AllArgsConstructor
public class BaseServiceImpl<ID extends Serializable, Entity extends BaseEntity<ID>,
        Repository extends BaseRepository<ID, Entity>> implements BaseService<ID, Entity> {

    protected final Session session;
    protected final Repository repository;

    @Override
    public void save(Entity entity) {
        try {
            session.getTransaction().begin();
            repository.save(entity);
            session.getTransaction().commit();
        } catch (TransactionException e) {
            System.out.println(e.getMessage());
            session.getTransaction().rollback();
        }
    }

    @Override
    public void update(Entity entity) {
        try {
            session.getTransaction().begin();
            repository.update(entity);
            session.getTransaction().commit();
        } catch (TransactionException e) {
            System.out.println(e.getMessage());
            session.getTransaction().rollback();
        }
    }

    @Override
    public void remove(Entity entity) {
        try {
            session.getTransaction().begin();
            repository.remove(entity);
            session.getTransaction().commit();
        } catch (TransactionException e) {
            System.out.println(e.getMessage());
            session.getTransaction().rollback();
        }
    }

    @Override
    public Optional<Entity> findById(ID id) {
        return repository.findById(id);
    }

    @Override
    public Collection<Entity> findAll() {
        session.getTransaction().begin();
        Collection<Entity> entities = repository.findAll();
        session.getTransaction().commit();
        return entities;
    }
}
