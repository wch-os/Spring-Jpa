package mvc.servlet.web.frontcontroller.v5;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mvc.servlet.web.frontcontroller.ModelView;
import mvc.servlet.web.frontcontroller.MyView;
import mvc.servlet.web.frontcontroller.v3.ControllerV3;
import mvc.servlet.web.frontcontroller.v3.controller.MemberFormControllerV3;
import mvc.servlet.web.frontcontroller.v3.controller.MemberListControllerV3;
import mvc.servlet.web.frontcontroller.v3.controller.MemberSaveControllerV3;
import mvc.servlet.web.frontcontroller.v4.ControllerV4;
import mvc.servlet.web.frontcontroller.v4.controller.MemberFormControllerV4;
import mvc.servlet.web.frontcontroller.v4.controller.MemberListControllerV4;
import mvc.servlet.web.frontcontroller.v4.controller.MemberSaveControllerV4;
import mvc.servlet.web.frontcontroller.v5.adapter.ControllerV3HandlerAdapter;
import mvc.servlet.web.frontcontroller.v5.adapter.ControllerV4HandlerAdapter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet(name = "frontControllerV5", urlPatterns = "/front-controller/v5/*")
public class FrontControllerV5 extends HttpServlet {
    
    // private Map<String, ControllerV4> controllerMap = new HashMap<>();
    private final Map<String, Object> handlerMappingMap = new HashMap<>();
    private final List<MyHandlerAdapter> handlerAdapters = new ArrayList<>();

    public FrontControllerV5() {
        initHandlerMappingMap();

        initHandlerAdapters();
    }

    private void initHandlerMappingMap() {
        handlerMappingMap.put("/front-controller/v5/v3/members/new-form", new MemberFormControllerV3());
        handlerMappingMap.put("/front-controller/v5/v3/members/save", new MemberSaveControllerV3());
        handlerMappingMap.put("/front-controller/v5/v3/members", new MemberListControllerV3());

        handlerMappingMap.put("/front-controller/v5/v4/members/new-form", new MemberFormControllerV4());
        handlerMappingMap.put("/front-controller/v5/v4/members/save", new MemberSaveControllerV4());
        handlerMappingMap.put("/front-controller/v5/v4/members", new MemberListControllerV4());
    }

    private void initHandlerAdapters() {
        handlerAdapters.add(new ControllerV3HandlerAdapter());
        handlerAdapters.add(new ControllerV4HandlerAdapter());
    }


    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 1. handlerMappingMap을 확인해서, 핸들러 조회
        Object handler = getHandler(request);

        if(handler == null){
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        // 2. handlerAdapters에서 핸들러를 처리할 수 있는 handlerAdapter 조회
        MyHandlerAdapter handlerAdapter = getHandlerAdapter(handler);

        // 3. adapter의 handle()을 호출한다.
        ModelView mv = handlerAdapter.handle(request, response, handler);

        //논리이름 : new-form
        String viewName = mv.getViewName();

        //논리 주소를 물리 주소로 변환
        MyView view = viewResolver(viewName);

        //view가 렌더링되기 위해서, model 정보가 필요함
        view.render(mv.getModel(), request, response);

    }

    private Object getHandler(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        Object handler = handlerMappingMap.get(requestURI);
        return handler;
    }

    private MyHandlerAdapter getHandlerAdapter(Object handler) {
        for (MyHandlerAdapter adapter : handlerAdapters) {
            if (adapter.supports(handler)) {
                return adapter;
            }
        }

        throw new IllegalArgumentException("handler adapter 를 찾을 수 없습니다. handler=" + handler);
    }

    private static MyView viewResolver(String viewName) {
        return new MyView("/WEB-INF/views/" + viewName + ".jsp");
    }
}
