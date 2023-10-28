package demo.kafka.demokafkatransaction;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DemoRepository extends JpaRepository<DemoEntity, UUID> {
}
