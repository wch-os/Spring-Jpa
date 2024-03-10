package mvc.servlet.web.frontcontroller.v3;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mvc.servlet.web.frontcontroller.ModelView;
import mvc.servlet.web.frontcontroller.MyView;
import mvc.servlet.web.frontcontroller.v3.controller.MemberFormControllerV3;
import mvc.servlet.web.frontcontroller.v3.controller.MemberListControllerV3;
import mvc.servlet.web.frontcontroller.v3.controller.MemberSaveControllerV3;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(name = "frontControllerServiceV3", urlPatterns = "/front-controller/v3/*")
public class FrontControllerServiceV3 extends HttpServlet {
    private Map<String, ControllerV3> controllerMap = new HashMap<>();

    public FrontControllerServiceV3() {
        controllerMap.put("/front-controller/v3/members/new-form", new MemberFormControllerV3());
        controllerMap.put("/front-controller/v3/members/save", new MemberSaveControllerV3());
        controllerMap.put("/front-controller/v3/members", new MemberListControllerV3());
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("FrontControllerServiceV3.service");

        String requestURI = request.getRequestURI();

        ControllerV3 controller = controllerMap.get(requestURI);
        if(controller == null){
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        //paramMap을 controller에 넘겨줘야 한다.
        Map<String, String> paramMap = createParamMap(request);
        ModelView modelView = controller.process(paramMap);

        //논리이름 : new-form
        String viewName = modelView.getViewName();

        //논리 주소를 물리 주소로 변환
        MyView view = viewResolver(viewName);

        //view가 렌더링되기 위해서, model 정보가 필요함
        view.render(modelView.getModel(), request, response);
    }
    private static MyView viewResolver(String viewName) {
        return new MyView("/WEB-INF/views/" + viewName + ".jsp");
    }

    // request의 모든 파라미터에 대한 정보를 paraMap에 저장한다.
    private static Map<String, String> createParamMap(HttpServletRequest request) {
        Map<String, String> paramMap = new HashMap<>();
        request.getParameterNames().asIterator()
                .forEachRemaining(paramName -> paramMap.put(paramName, request.getParameter(paramName)));
        return paramMap;
    }
}
