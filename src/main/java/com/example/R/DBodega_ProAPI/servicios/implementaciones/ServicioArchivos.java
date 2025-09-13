package com.example.R.DBodega_ProAPI.servicios.implementaciones;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.example.R.DBodega_ProAPI.modelos.Producto;
import com.example.R.DBodega_ProAPI.repositorios.IProductoRepository;
import java.nio.file.Path;
import java.nio.file.Paths;


@Service
public class ServicioArchivos {
    private final String UPLOAD_DIR = "uploads/productos/";
    
    @Autowired
    private IProductoRepository productoRepository;

    public String guardarImagen(MultipartFile archivo) throws IOException {
        // Generar nombre único
        String nombreArchivo = System.currentTimeMillis() + "_" + archivo.getOriginalFilename();

        Path path = Paths.get(UPLOAD_DIR, nombreArchivo);
        Files.createDirectories(path.getParent()); // crea carpeta si no existe
        Files.copy(archivo.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

        return "/" + UPLOAD_DIR + nombreArchivo; // devolver ruta relativa
    }

    public void eliminarImagenExistente(Integer productoId) throws IOException {
        // aquí usas el repository para obtener el producto
        Producto producto = productoRepository.findById(productoId)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        if (producto.getImagen_url() != null) {
            Path path = Paths.get(producto.getImagen_url().replaceFirst("/", ""));
            Files.deleteIfExists(path);
        }
    }
}