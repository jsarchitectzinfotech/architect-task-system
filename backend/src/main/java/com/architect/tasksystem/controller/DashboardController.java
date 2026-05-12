package com.architect.tasksystem.controller;
import com.architect.tasksystem.security.SecurityUtils;
import com.architect.tasksystem.service.DashboardService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
@Tag(name = "Dashboard")
@RequiredArgsConstructor
public class DashboardController {
    private final DashboardService dashboardService;
    private final SecurityUtils securityUtils;

    @GetMapping
    public ResponseEntity<?> getDashboard() {
        var user = securityUtils.getCurrentUser();
        return ResponseEntity.ok(switch(user.getRole()) {
            case MANAGER -> dashboardService.getManagerDashboard();
            case JUNIOR -> dashboardService.getEmployeeDashboard(user.getId());
        });
    }
}
