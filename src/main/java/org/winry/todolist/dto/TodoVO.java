package org.winry.todolist.dto;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;
import java.util.Date;

@Data
@Entity
public class TodoVO {

    @Id
    private int todo_no;

    @NotEmpty(message="할일을 입력해 주세요.")
    private String content;

    private Date input_dt;
    private Date update_dt;

    private Date complete_dt;

    private int cnt;

}
