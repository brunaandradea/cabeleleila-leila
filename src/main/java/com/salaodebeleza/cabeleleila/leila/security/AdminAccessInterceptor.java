package com.salaodebeleza.cabeleleila.leila.security;

import com.salaodebeleza.cabeleleila.leila.service.UsuarioAuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.List;

public class AdminAccessInterceptor implements HandlerInterceptor {

    public static final String HEADER_USUARIO_ID = "X-Usuario-Id";

    private static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();

    private record RotaAdmin(String metodo, String padrao) {
    }

    private static final List<RotaAdmin> ROTAS_ADMIN = List.of(
            new RotaAdmin("GET", "/agendamentos"),
            new RotaAdmin("GET", "/agendamentos/relatorio-semanal"),
            new RotaAdmin("PATCH", "/agendamentos/*/status"),
            new RotaAdmin("POST", "/servicos"),
            new RotaAdmin("PUT", "/servicos/*"),
            new RotaAdmin("PATCH", "/servicos/*/ativar"),
            new RotaAdmin("PATCH", "/servicos/*/desativar")
    );

    private final UsuarioAuthService usuarioAuthService;

    public AdminAccessInterceptor(UsuarioAuthService usuarioAuthService) {
        this.usuarioAuthService = usuarioAuthService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        boolean requerAdmin = ROTAS_ADMIN.stream().anyMatch(rota ->
                rota.metodo().equalsIgnoreCase(request.getMethod())
                        && PATH_MATCHER.match(rota.padrao(), request.getRequestURI()));

        if (!requerAdmin) {
            return true;
        }

        String usuarioId = request.getHeader(HEADER_USUARIO_ID);
        if (!usuarioAuthService.isAdmin(usuarioId)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.setContentType("application/json");
            response.getWriter().write("{\"erro\":\"Acesso restrito ao administrador.\"}");
            return false;
        }

        return true;
    }
}
