package filter;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.*;
import java.io.IOException;

@WebFilter("/*")
public class SecurityFilter extends HttpFilter {
    private final String[] paths={
            "/MyRealEstates","/userinfo","/estates","/statistics","/estate",
            "/NewEstate","/editSpending","/changeUserInfo"
    };
    private boolean doNeedAuth(HttpServletRequest request){
        String context=request.getContextPath();
        String path = request.getRequestURI().substring(context.length());
        for (String item : paths) {
            if (path.startsWith(item) /*|| path.equals("/")*/) {
                return true;
            }
        }
        return false;
    }
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    public SecurityFilter(){

    }
    @Override
    public void destroy(){

    }

    @Override
    public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {

        /*HttpServletRequest req = (HttpServletRequest)request;
        HttpServletResponse resp = (HttpServletResponse)response;
        String servletPath=req.getServletPath();

         User user= (User)req.getSession().getAttribute("user");
         if(servletPath.equals("/enter")){
             chain.doFilter(req,resp);
         }
         HttpServletRequest wrapRequest=req;
         if(user!=null){
             String userName=user.getInfo().getName();
         }*/
        /*if(request.getServletPath().equals("enter")){
            chain.doFilter(request,response);
            return;
        }*/
        HttpSession session=request.getSession();

        if (session.getAttribute("user")==null && doNeedAuth(request))
            response.sendRedirect("enter");
        else
            chain.doFilter(request, response);
    }
}
