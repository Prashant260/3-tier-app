package com.bloghub.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class SessionAuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws Exception {

        // ✅ STEP 0: ALWAYS allow OPTIONS (CORS preflight)
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        // STEP 1: Get existing session (do NOT create new)
        HttpSession session = request.getSession(false);

        // Debug logs (keep for now)
        System.out.println("Path: " + request.getRequestURI());
        System.out.println("Method: " + request.getMethod());
        System.out.println("Session exists?: " + (session != null));

        if (session != null) {
            System.out.println("Session ID: " + session.getId());
            System.out.println("UserId: " + session.getAttribute("userId"));
            System.out.println("UserRole: " + session.getAttribute("userRole"));
        }

        // STEP 2: Check authentication
        if (session == null || session.getAttribute("userId") == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401
            response.setContentType("application/json");
            response.getWriter().write(
                "{\"error\":\"Unauthorized: Please log in to access this resource.\"}"
            );
            return false;
        }

        // STEP 3: User is authenticated
        Long userId = (Long) session.getAttribute("userId");
        String userRole = (String) session.getAttribute("userRole");

        // Pass user info to controller
        request.setAttribute("currentUserId", userId);
        request.setAttribute("currentUserRole", userRole);

        // STEP 4: Role-based authorization
        String path = request.getRequestURI();
        String method = request.getMethod();

        // Only ADMIN can modify categories
        if (path.startsWith("/api/categories")) {
            if (!"GET".equalsIgnoreCase(method) && !"ADMIN".equals(userRole)) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN); // 403
                response.setContentType("application/json");
                response.getWriter().write(
                    "{\"error\":\"You do not have permission to perform this action.\"}"
                );
                return false;
            }
        }

        // STEP 5: Everything OK → continue to controller
        return true;
    }
}
