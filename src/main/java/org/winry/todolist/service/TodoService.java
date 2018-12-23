package org.winry.todolist.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.winry.todolist.dao.TodoDAO;
import org.winry.todolist.dto.TodoVO;

import java.util.HashMap;
import java.util.List;

@Service
public class TodoService {

    @Autowired
    TodoDAO dao;

    public void regist(TodoVO vo) {
        dao.regist(vo);
    }

    public TodoVO select_one(int todo_no) {
       return dao.select_one(todo_no);
    }

    public Object select_all(TodoVO vo) {
        return dao.select_all(vo);
    }

    public void insert_refer(HashMap<String, Object> map) {
        dao.insert_refer(map);
    }

    public void delete(int todo_no) {
        dao.delete(todo_no);
    }

    public void update_complete(int todo_no) {
        dao.update_complete(todo_no);
    }

    public void update_rollback(int todo_no) {
        dao.update_rollback(todo_no);
    }

    public void update_todo(TodoVO vo) {
        dao.update_todo(vo);
    }

    public void delete_refer(int todo_no) {
        dao.delete_refer(todo_no);
    }

}
