package hommie.hommie.service.serviceinterface;

import hommie.hommie.dto.requestDTO.ReportRequestDTO;
import hommie.hommie.dto.responseDTO.ReportResponseDTO;

import java.util.List;

public interface ReportService {
    ReportResponseDTO createReport(ReportRequestDTO reportRequestDTO);

    List<ReportResponseDTO> getAllReport();
}
