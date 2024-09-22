package mk.ukim.finki.wp.june2022.g1.repository;

import mk.ukim.finki.wp.june2022.g1.model.VirtualServer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VirtualServerRepository extends JpaRepository<VirtualServer, Long> {
}
