package com.kejiahp.musicx;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kejiahp.musicx.util.ApiResponse;

@RestController
public class Controller {

    @GetMapping("/")
    public ResponseEntity<ApiResponse<Void>> healthCheck() {
        return ResponseEntity.ok(new ApiResponse<Void>(true, "Health check successful", null));
    }
}
