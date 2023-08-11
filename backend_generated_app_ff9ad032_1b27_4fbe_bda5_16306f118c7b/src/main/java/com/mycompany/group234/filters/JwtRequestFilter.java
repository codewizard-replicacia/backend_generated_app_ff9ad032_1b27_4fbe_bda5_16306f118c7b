package com.mycompany.group234.filters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.group234.Exception.ErrorResponse;
import com.mycompany.group234.Exception.InvalidTokenException;
import com.mycompany.group234.Exception.RestExceptionHandler;
import com.mycompany.group234.util.JwtTokenUtil;
import io.jsonwebtoken.ExpiredJwtException;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtRequestFilter extends OncePerRequestFilter {
  private final JwtTokenUtil tokenUtil = new JwtTokenUtil();
  private final RestExceptionHandler exceptionHandler = new RestExceptionHandler();

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
      FilterChain filterChain) throws ServletException, IOException {
    final String requestTokenHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
    String jwtToken = null;
    if (requestTokenHeader != null) {
      if (requestTokenHeader.startsWith("Bearer ")) {
        jwtToken = requestTokenHeader.substring(7);
        try {
          tokenUtil.validateToken(jwtToken);
          filterChain.doFilter(request, response);
        } catch (IllegalArgumentException e) {
          logger.error("Unable to get JWT Token");
          handleException(response, "Unable to get JWT Token");
        } catch (ExpiredJwtException e) {
          logger.error("JWT Token has expired");
          handleException(response, "JWT Token has expired");
        }
      }
    } else {
      handleException(response, "Invalid JWT token received");
    }
  }

  private void handleException(HttpServletResponse response, String message) throws IOException {
    ResponseEntity<ErrorResponse> responseEntity = exceptionHandler.unauthorizedException(
        new InvalidTokenException(message));
    response.setStatus(responseEntity.getStatusCodeValue());
    response.getWriter().write(convertObjectToJson(responseEntity.getBody()));
  }

  private String convertObjectToJson(Object object) throws JsonProcessingException {
    if (object == null) {
      return null;
    }
    ObjectMapper mapper = new ObjectMapper();
    return mapper.writeValueAsString(object);
  }
}