package pl.xsware.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import pl.xsware.config.properties.CorsProperties;

import java.io.IOException;

@Component
@EnableConfigurationProperties(value = CorsProperties.class)
public class CorsFilterConfig implements Filter {

    private CorsProperties corsProperties;

    public CorsFilterConfig(CorsProperties corsProperties) {
        this.corsProperties = corsProperties;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        res.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        res.setHeader("Access-Control-Expose-Headers", "*");
        res.setHeader("Access-Control-Max-Age", "3600");
        corsProperties.getOrigins().forEach(origin -> {
            res.addHeader("Access-Control-Allow-Origin", origin);
        });
        corsProperties.getHeaders().forEach(header -> {
            res.addHeader("Access-Control-Allow-Headers", header);
        });

        if ("OPTIONS".equalsIgnoreCase(req.getMethod())) {
            res.setStatus(HttpServletResponse.SC_OK);
            return;
        }

        chain.doFilter(request, response);
    }
}