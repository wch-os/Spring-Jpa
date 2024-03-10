package mvc.servlet.web.frontcontroller.v5.adapter;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import mvc.servlet.web.frontcontroller.ModelView;
import mvc.servlet.web.frontcontroller.v4.ControllerV4;
import mvc.servlet.web.frontcontroller.v5.MyHandlerAdapter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ControllerV4HandlerAdapter implements MyHandlerAdapter {
    @Override
    public boolean supports(Object handler) {
        return handler instanceof ControllerV4;
    }

    @Override
    public ModelView handle(HttpServletRequest request, HttpServletResponse response, Object handler) throws ServletException, IOException {
        ControllerV4 controllerV4 = (ControllerV4) handler;

        Map<String, String> paramMap = createParam(request);
        Map<String, Object> model = new HashMap<>();

        String viewName = controllerV4.process(paramMap, model);

        // Adapter의 역할로서 Modelview로 바꿔준다.
        ModelView mv = new ModelView(viewName);

        //그리고 render 할 때, 필요한 정보인 model을 set() 해준다.
            //ps. v4에서는 같은 라이프사이클 내에 있는 model을 param에 넘겨 줬으면 되었으나, 여기서는 다른 라이프사이클이므로 set() 과정이 필요하다.
        mv.setModel(model);

        return mv;
    }

    private Map<String, String> createParam(HttpServletRequest request) {
        Map<String, String> paramMap = new HashMap<>();
        request.getParameterNames().asIterator()
                .forEachRemaining(parameterName -> paramMap.put(parameterName, request.getParameter(parameterName)));
        return paramMap;
    }
}
