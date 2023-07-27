package hommie.hommie.service.serviceimplement;

import hommie.hommie.dto.requestDTO.ReportRequestDTO;
import hommie.hommie.dto.responseDTO.ReportResponseDTO;
import hommie.hommie.entity.Order;
import hommie.hommie.entity.Report;
import hommie.hommie.entity.User;
import hommie.hommie.repository.OrderRepo;
import hommie.hommie.repository.ReportRepo;
import hommie.hommie.repository.UserRepo;
import hommie.hommie.service.serviceinterface.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    ReportRepo reportRepo;

    @Autowired
    UserRepo userRepo;

    @Autowired
    OrderRepo orderRepo;

    @Override
    public ReportResponseDTO createReport(ReportRequestDTO reportRequestDTO) {
        User user = userRepo.findById(reportRequestDTO.getUserId()).get();
        Order order = orderRepo.findByOrderCode(reportRequestDTO.getOrderCode());
        Report report = Report.builder()
                .imageReport(reportRequestDTO.getImageReport())
                .reason(reportRequestDTO.getReason())
                .dateCreate(LocalDate.now())
                .user(user)
                .order(order)
                .build();
        reportRepo.save(report);
        ReportResponseDTO reportResponseDTO = ReportResponseDTO.builder()
                .reportId(report.getId())
                .imageReport(report.getImageReport())
                .reason(report.getReason())
                .email(user.getEmail())
                .orderCode(order.getOrderCode())
                .build();
        return reportResponseDTO;
    }

    @Override
    public List<ReportResponseDTO> getAllReport() {
        List<ReportResponseDTO> reportResponseDTOS = new ArrayList<>();
        List<Report> reports = reportRepo.findAll();
        for (Report report: reports) {
            User user = userRepo.findById(report.getUser().getId()).get();
            Order order = orderRepo.findById(report.getOrder().getId()).get();
            ReportResponseDTO reportResponseDTO = ReportResponseDTO.builder()
                    .reportId(report.getId())
                    .imageReport(report.getImageReport())
                    .reason(report.getReason())
                    .email(user.getEmail())
                    .orderCode(order.getOrderCode())
                    .build();
            reportResponseDTOS.add(reportResponseDTO);
        }
        return reportResponseDTOS;
    }
}
