package com.db.demo.demo.security.filter;

import com.db.demo.demo.security.dto.UserContextDTO;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author Savitha
 */

public class AuthenticationFilter extends OncePerRequestFilter {

    private static final Logger LOGGER = LogManager.getLogger(AuthenticationFilter.class);
    private static final Map<String, UUID> userNameAndTokenDetails = new HashMap<>();
    public static final String USER_IS_NOT_AUTHORIZED = "User is not authorized";

    static {
        userNameAndTokenDetails.put("validUser", UUID.randomUUID());
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest,
                                    HttpServletResponse httpServletResponse,
                                    FilterChain filterChain) throws ServletException, IOException {
        UUID callTrackerId = UUID.randomUUID();
        LOGGER.info("method:doFilterInternal|callTrackerId:{} | entry ", callTrackerId);
        try {
            String requestURI = httpServletRequest.getRequestURI();
            LOGGER.info("method:doFilterInternal|requestURI:{}", requestURI);

            String user = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);
            UUID password = userNameAndTokenDetails.get(user);
            if(StringUtils.isEmpty(password)) {
                httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED,
                        USER_IS_NOT_AUTHORIZED);
                return;
            }
            UserContextDTO userContext = UserContextDTO.builder().userName(user).password(password).build();
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    userContext,
                    null, new ArrayList<>());
            authentication.setDetails(userContext);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch(Exception e) {
            LOGGER.error("Exception : {}", e);
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
        LOGGER.info("method:doFilterInternal| callTrackerId:{} | exit", callTrackerId);
    }
}