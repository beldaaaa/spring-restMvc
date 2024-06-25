package springframework.spring6restmvc.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import springframework.spring6restmvc.entities.BeerAudit;

import java.util.UUID;

public interface BeerAuditRepository extends JpaRepository<BeerAudit, UUID> {
}
