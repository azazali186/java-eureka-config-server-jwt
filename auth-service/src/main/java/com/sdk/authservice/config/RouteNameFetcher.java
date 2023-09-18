package com.sdk.authservice.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import com.sdk.authservice.entity.PermissionEntity;
import com.sdk.authservice.entity.RoleEntity;
import com.sdk.authservice.repository.PermissionRepo;
import com.sdk.authservice.repository.RoleRepo;

import jakarta.annotation.PostConstruct;

import org.springframework.web.method.HandlerMethod;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class RouteNameFetcher {

    @Autowired
    PermissionRepo permRepo;

    @Autowired
    RoleRepo roleRepo;

    @Autowired
    @Qualifier("requestMappingHandlerMapping")
    private RequestMappingHandlerMapping handlerMapping;

    @PostConstruct
    public void fetchRouteNames() {
        Map<RequestMappingInfo, HandlerMethod> handlerMethods = handlerMapping.getHandlerMethods();

        List<PermissionEntity> allPermissions = new ArrayList<>();

        for (RequestMappingInfo requestMappingInfo : handlerMethods.keySet()) {
            String name = requestMappingInfo.getName();
            String route = "";
            if (!requestMappingInfo.getDirectPaths().isEmpty()) {
                route = requestMappingInfo.getDirectPaths().toString().replace("[", "").replace("]", "").replaceAll("/",
                                "-");
            }
            if (name != null) {
                PermissionEntity permissions = permRepo.findByName(name);

                if (permissions == null) {
                    permissions = new PermissionEntity();
                    permissions.setName(name);
                    permissions.setRoute(route);
                    permissions.setGuard("API");
                    permRepo.save(permissions);
                }
                allPermissions.add(permissions);
            }

        }

        Optional<RoleEntity> optionalRole = roleRepo.findByName("admin");
        RoleEntity adminRole;
        if (optionalRole.isPresent()) {
            adminRole = optionalRole.get();
        } else {
            adminRole = new RoleEntity();
            adminRole.setName("ADMIN");
            adminRole.setDesc("Admin role");  // Set a description if needed
        }
       adminRole.setPermissions(allPermissions);
        roleRepo.save(adminRole);
    }
}
