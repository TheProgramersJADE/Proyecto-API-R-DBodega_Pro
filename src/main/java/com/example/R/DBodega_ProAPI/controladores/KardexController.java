package com.example.R.DBodega_ProAPI.controladores;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.R.DBodega_ProAPI.dtos.kardex.KardexMovimiento;
import com.example.R.DBodega_ProAPI.servicios.interfaces.IKardexService;

@RestController
@RequestMapping("/api/kardex")
public class KardexController {

    @Autowired
    private IKardexService kardexService;

    // @GetMapping("/{productoId}")
    // public ResponseEntity<List<KardexMovimiento>> obtenerKardex(@PathVariable Integer productoId) {
    //     List<KardexMovimiento> kardex = kardexService.obtenerKardexPorProducto(productoId);
    //     return ResponseEntity.ok(kardex);
    // }

            @PreAuthorize("hasAnyAuthority('ROLE_Administrador', 'ROLE_SupervisorBodega')")
    @GetMapping("/todos")
    public ResponseEntity<List<KardexMovimiento>> obtenerTodosKardex() {
        List<KardexMovimiento> kardex = kardexService.obtenerTodosKardex();
        return ResponseEntity.ok(kardex);
    }

    // @GetMapping("/pdf/{productoId}")
    // public ResponseEntity<ByteArrayResource> generarPdf(@PathVariable Integer productoId) throws Exception {
    //     byte[] pdf = kardexService.generarPdfKardex(productoId);
    //     return ResponseEntity.ok()
    //             .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=kardex_producto_" + productoId + ".pdf")
    //             .contentType(MediaType.APPLICATION_PDF)
    //             .body(new ByteArrayResource(pdf));
    // }

            @PreAuthorize("hasAnyAuthority('ROLE_Administrador', 'ROLE_SupervisorBodega')")
    @GetMapping("/pdf/todos")
    public ResponseEntity<ByteArrayResource> generarPdfTodos() throws Exception {
        byte[] pdf = kardexService.generarPdfTodosKardex();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=kardex_completo.pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(new ByteArrayResource(pdf));
    }
}