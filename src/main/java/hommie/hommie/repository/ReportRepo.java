package hommie.hommie.repository;

import hommie.hommie.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepo extends JpaRepository<Report,Long> {
}
