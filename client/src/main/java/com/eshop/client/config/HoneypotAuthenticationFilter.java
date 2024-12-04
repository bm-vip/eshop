package com.eshop.client.config;

import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.model.CountryResponse;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.util.UUID;

public class HoneypotAuthenticationFilter extends OncePerRequestFilter {

    private final String honeypotFieldName;
    private DatabaseReader dbReader;

    @SneakyThrows
    public HoneypotAuthenticationFilter(String honeypotFieldName) {
        this.honeypotFieldName = honeypotFieldName;
        InputStream databaseStream = getClass().getClassLoader().getResourceAsStream("GeoLite2-Country.mmdb");
        dbReader = new DatabaseReader.Builder(databaseStream).build();
    }

    @SneakyThrows
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        MDC.put("traceId", UUID.randomUUID().toString());
        String ipAddress = "";
        if (request != null) {
            ipAddress = request.getHeader("X-FORWARDED-FOR");
            if (StringUtils.isEmpty(ipAddress)) {
                ipAddress = request.getRemoteAddr();
            }
        }
        InetAddress ip = InetAddress.getByName(ipAddress);
        // Get country information
        CountryResponse countryResponse = dbReader.country(ip);
        String countryCode = countryResponse.getCountry().getIsoCode();

        MDC.put("clientIp", ipAddress);

        // Block specific countries
        if ("CN".equals(countryCode) || "RU".equals(countryCode) || "IR".equals(countryCode)|| "KP".equals(countryCode)) {
            response.sendRedirect("/region_denied.html");
            return;
        }
        if (isLoginRequest(request)) {
            String honeypotValue = request.getParameter(honeypotFieldName);
            if (honeypotValue != null && !honeypotValue.isEmpty()) {
                // Bot detected - redirect to login page with error
                response.sendRedirect("/login?errorMsg=botDetected");
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    private boolean isLoginRequest(HttpServletRequest request) {
        return request.getMethod().equals("POST") &&
                request.getServletPath().equals("/login");
    }
}