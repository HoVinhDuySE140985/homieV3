package hommie.hommie.controller;

import hommie.hommie.dto.requestDTO.ReportRequestDTO;
import hommie.hommie.dto.responseDTO.ResponseDTO;
import hommie.hommie.service.serviceinterface.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.PermitAll;

@RestController
@RequestMapping("/api/report")
public class ReportController {
    @Autowired
    ReportService reportService;

    @PostMapping("create-report")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<ResponseDTO> createReport(@RequestBody @Validated ReportRequestDTO reportRequestDTO){
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setData(reportService.createReport(reportRequestDTO));
        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("get-all-report")
    @PermitAll
    public ResponseEntity<ResponseDTO> getAllReport(){
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setData(reportService.getAllReport());
        responseDTO.setResult(reportService.getAllReport().size());
        return ResponseEntity.ok().body(responseDTO);
    }
}
