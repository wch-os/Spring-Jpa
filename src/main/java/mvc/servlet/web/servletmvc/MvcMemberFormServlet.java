package mvc.servlet.web.servletmvc;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

// 딱히 기능이 없고, 기존에 만든 jsp인 view로 이동하면 된다.
@WebServlet(name = "mvcMemberFormServlet", urlPatterns = "/servlet-mvc/members/new-form")
public class MvcMemberFormServlet extends HttpServlet {
    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String viewPath = "/WEB-INF/views/new-form.jsp";
        RequestDispatcher dispatcher = request.getRequestDispatcher(viewPath);//컨트롤러에서 뷰로 이동

        // /servlet-mvc/members/new-form 으로 들어온 request 요청을 viewpath(jsp)에서 처리하도록 넘긴다.
        dispatcher.forward(request, response); //서블릿에서 jsp 호출
    }
}
