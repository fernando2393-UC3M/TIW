package main;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet Filter implementation class LogInFilter
 */
@WebFilter(filterName= "LogInFilter",
		   urlPatterns = {"/admin", 
						  "/resultados", "/renting", 
						  "/registrado", "/mensajes", 
						  "/alojamiento", "/casa"})
public class LogInFilter implements Filter {
	
	private FilterConfig fConfig;
	
	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		this.fConfig = fConfig;
	}
	
    /**
     * Default constructor. 
     */
    public LogInFilter() {}

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		ServletContext myContext = fConfig.getServletContext();
		
		HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
		
		String loginURI = req.getContextPath() + "/login";

        boolean loggedIn = myContext != null && myContext.getAttribute("user") != null;
        boolean loginRequest = req.getRequestURI().equals(loginURI);

        if (loggedIn || loginRequest) {
        	// pass the request along the filter chain
            chain.doFilter(request, response);
        } else {
        	// go to the home page
			res.sendRedirect(req.getContextPath());
        }
        return;

	}

	
}
