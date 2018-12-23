package org.winry.todolist.web.rest;

import org.springframework.web.bind.annotation.*;
import org.winry.todolist.common.ParamValidatChk;
import org.winry.todolist.dto.TodoVO;
import org.winry.todolist.service.TodoService;

import javax.inject.Inject;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("/rest/todo")
public class TodoRestController extends ParamValidatChk {

    @Inject
    TodoService service;

    @RequestMapping(value = "/regist", method = RequestMethod.POST, produces = "application/json")
    public List<TodoVO> todo_regist(@ModelAttribute @Valid TodoVO vo ){

        // INSERT
        if (vo.getTodo_no() == 0) {
            service.regist(vo);
        }
        // UPDATE
        else {
            service.update_todo(vo);
        }
        service.delete_refer(vo.getTodo_no());
        if (vo.getContent().indexOf("@") > -1) {
            String content = vo.getContent();
            Pattern pattern = Pattern.compile("@[0-9]*");
            Matcher matcher = pattern.matcher(content);
            while (matcher.find()) {
                String matchStr = matcher.group().replace("@", "").trim();

                if (isNumeric(matchStr)) {
                    int refer_no = toInteger(matchStr);
//                    System.out.println(refer_no);
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("todo_no", vo.getTodo_no());
                    map.put("refer_no", refer_no);
                    service.insert_refer(map);
                }
            }
        }
        List<TodoVO> result = (List<TodoVO>) service.select_all(new TodoVO());

        return result;
    }

    @RequestMapping(value = "/search", method = RequestMethod.POST, produces = "application/json")
    public List<TodoVO> search(@ModelAttribute TodoVO vo ){

        List<TodoVO> result = (List<TodoVO>) service.select_all(vo);

        return result;
    }


    @RequestMapping(value = "/list", method = RequestMethod.GET, produces = "application/json")
    public List<TodoVO> list(){
        List<TodoVO> result = (List<TodoVO>) service.select_all(new TodoVO());

        return result;
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST, produces = "application/json")
    public List<TodoVO> delete(
            @RequestParam(value = "no", defaultValue = "0", required = true) String no
    ){
        int todo_no = 0;
        if (!isEmpty(no)) {
            if (isNumeric(no)){
                todo_no = toInteger(no);
                if (todo_no < 1) {
                    todo_no = 0;
                }
            }
        }
        service.delete(todo_no);
        List<TodoVO> result = (List<TodoVO>) service.select_all(new TodoVO());
        return result;
    }

    @RequestMapping(value = "/complete", method = RequestMethod.POST, produces = "application/json")
    public List<TodoVO> complete(
            @RequestParam(value = "no", defaultValue = "0", required = true) String no
    ){
        int todo_no = 0;
        if (!isEmpty(no)) {
            if (isNumeric(no)){
                todo_no = toInteger(no);
                if (todo_no < 1) {
                    todo_no = 0;
                }
            }
        }
        service.update_complete(todo_no);
        List<TodoVO> result = (List<TodoVO>) service.select_all(new TodoVO());
        return result;
    }

    @RequestMapping(value = "/rollback", method = RequestMethod.POST, produces = "application/json")
    public List<TodoVO> rollback(
            @RequestParam(value = "no", defaultValue = "0", required = true) String no
    ){
        int todo_no = 0;
        if (!isEmpty(no)) {
            if (isNumeric(no)){
                todo_no = toInteger(no);
                if (todo_no < 1) {
                    todo_no = 0;
                }
            }
        }
        service.update_rollback(todo_no);
        List<TodoVO> result = (List<TodoVO>) service.select_all(new TodoVO());
        return result;
    }

}
