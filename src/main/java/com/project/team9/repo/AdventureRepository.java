package com.project.team9.repo;

import com.project.team9.model.resource.Adventure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.LockModeType;
import javax.persistence.QueryHint;
import java.util.List;

@Repository
public interface AdventureRepository extends JpaRepository<Adventure, Long> {

    @Query("FROM Adventure WHERE owner.id = ?1 AND deleted = false")
    List<Adventure> findAdventuresWithOwner(Long ownerId);
    @Query("FROM Adventure WHERE owner.id = ?1 AND deleted = false")
    List<Adventure> findByOwnerId(Long owner_id);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select p from Adventure p where p.id = :id AND deleted = false")
    //Postgres po defaultu poziva for update bez no wait, tako da treba dodati vrednost 0 za timeout
    //kako bismo dobili PessimisticLockingFailureException ako pri pozivu ove metode torka nije dostupna

    /*
     * To prevent the operation from waiting for other transactions to commit, use the NOWAIT option.
     * With NOWAIT, the statement reports an error, rather than waiting, if a selected row cannot be locked immediately.
     * Note that NOWAIT applies only to the row-level lock(s) — the required ROW SHARE table-level lock is still taken in the ordinary way.
     * You can use LOCK with the NOWAIT option first, if you need to acquire the table-level lock without waiting.
     * https://www.postgresql.org/docs/9.1/sql-select.html
     */
    @QueryHints({@QueryHint(name = "javax.persistence.lock.timeout", value ="0")})
    public Adventure findOneById(@Param("id") Long id);
}
