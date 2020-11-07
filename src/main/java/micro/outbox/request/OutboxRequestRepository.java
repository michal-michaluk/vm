package micro.outbox.request;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.UUID;

@Repository
interface OutboxRequestRepository extends JpaRepository<OutboxRequest, UUID> {

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    default void deleteByIdInSeparateTransaction(UUID id) {
        deleteById(id);
    }

    default void clear() {
        deleteAllInBatch();
    }
}
