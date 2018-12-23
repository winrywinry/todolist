package org.winry.todolist.dao;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.winry.todolist.dto.TodoVO;

import javax.naming.Name;
import java.util.HashMap;

@Repository
public class TodoDAO {

    @Autowired
    SqlSession session;

    private static final String Namespace = "org.winry.todolist.todoMapper";

    public void regist(TodoVO vo) {
        session.insert(Namespace +".regist", vo);
    }

    public TodoVO select_one(int todo_no) {
        return session.selectOne(Namespace +".select_one", todo_no);
    }

    public Object select_all(TodoVO vo) {
        return session.selectList(Namespace +".select_all", vo);
    }

    public void delete_refer(int todo_no) {
        session.delete(Namespace +".delete_refer", todo_no);
    }

    public void insert_refer(HashMap<String, Object> map) {
        session.insert(Namespace +".insert_refer", map);
    }

    public void delete(int todo_no) {
        session.delete(Namespace +".delete_todo", todo_no);
        session.delete(Namespace +".delete_refer", todo_no);
    }

    public void update_complete(int todo_no) {
        session.update(Namespace +".update_complete", todo_no);
    }

    public void update_rollback(int todo_no) {
        session.update(Namespace +".update_rollback", todo_no);
    }

    public void update_todo(TodoVO vo) {
        session.update(Namespace +".update_todo", vo);
    }

}
