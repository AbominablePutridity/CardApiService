package com.mycompany.cardapiservice.controller;

import com.mycompany.cardapiservice.dto.OperationDto;
import io.swagger.v3.oas.annotations.Parameter;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 */
@RestController
@RequestMapping("/api/card")
public class OperationController {

//    @GetMapping("/admin/getAllOperations")
//    public List<OperationDto> getAllOperations(
//            @Parameter(
//                description = "Номер страницы (начинается с 0)",
//                example = "0"
//            )
//            @RequestParam(value = "page", defaultValue = "0") int page,
//            
//            @Parameter(
//                description = "Размер страницы",
//                example = "10"
//            )
//            @RequestParam(value = "size", defaultValue = "10") int size
//    )
//    {
//        
//    }
}
