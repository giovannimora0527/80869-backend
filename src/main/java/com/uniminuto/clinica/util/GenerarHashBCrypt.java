package com.uniminuto.clinica.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class GenerarHashBCrypt {
    
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String password = "password123";
        String hash = encoder.encode(password);
        
        System.out.println("========================================");
        System.out.println("Password: " + password);
        System.out.println("Hash BCrypt generado:");
        System.out.println(hash);
        System.out.println("Longitud del hash: " + hash.length());
        System.out.println("========================================");
        System.out.println("\nSQL para actualizar:");
        System.out.println("UPDATE usuario SET password_hash = '" + hash + "', intentos_fallidos = 0, bloqueado_hasta = NULL WHERE username = 'admin';");
        System.out.println("========================================");
        
        // Verificar que funciona
        boolean matches = encoder.matches(password, hash);
        System.out.println("\nVerificación: " + (matches ? "✓ CORRECTO" : "✗ ERROR"));
    }
}
