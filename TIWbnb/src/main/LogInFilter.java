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
import javax.servlet.http.HttpSession;

/**
 * Servlet Filter implementation class LogInFilter
 */
@WebFilter(urlPatterns={""})
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
		
		HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
		
        if(req.getRequestURI().equals(req.getContextPath() + "/") ||
           req.getRequestURI().equals(req.getContextPath() + "/index")) {
             	chain.doFilter(request, response);
             	return;
        }
        
        HttpSession session = req.getSession(false);
		String loginURI = req.getContextPath() + "/login";
        
        
        boolean loggedIn = session != null && session.getAttribute("user") != null;
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
