package pl.xsware.api.util;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
public class RequestTimingFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
            throws ServletException, IOException
    {
        long start = System.nanoTime();
        String path = req.getRequestURI();
        String method = req.getMethod();

        try {
            chain.doFilter(req, res);
        } finally {
            long ms = java.util.concurrent.TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start);
            int status = res.getStatus();
            if (ms > 1000) log.warn("METHOD: {} {}, STATUS: {}, TIME: {} ms", method, path, status, ms);
            else           log.info("METHOD: {} {}, STATUS: {}, TIME: {} ms", method, path, status, ms);
        }
    }
}
