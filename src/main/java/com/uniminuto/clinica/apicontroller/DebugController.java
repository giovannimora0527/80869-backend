package com.uniminuto.clinica.apicontroller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
public class DebugController {

    @GetMapping("/auth/debug-headers")
    public ResponseEntity<Map<String, Object>> debugHeaders(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        boolean hasAuth = authHeader != null;
        boolean startsBearer = hasAuth && authHeader.startsWith("Bearer ");
        int tokenLen = startsBearer ? authHeader.length() - 7 : 0;

        Map<String, Object> out = new HashMap<>();
        out.put("hasAuth", hasAuth);
        out.put("startsWithBearer", startsBearer);
        out.put("tokenLen", tokenLen);

        return ResponseEntity.ok(out);
    }
}
