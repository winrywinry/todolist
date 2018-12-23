/*
 * @User suyeon
 * @Date 2018-12-20
 */
var pagination1;
var itemsOnPage = 10;
var listData;
var pagination1 ;

$(function(){
    formReset();
    pagination1 = new Pagination({
        container: $("#pagination-1"),
        maxVisibleElements: 9,
        pageClickCallback: pageClick,
        showSlider: true
    });

    $.ajax({
          url : "/rest/todo/list"
        , dataType: "json"
        , data : {
            'page' : getCurrentPage()
        }
        , type : "get"
        , success : function(data) {
              setListData(data);
              pagination1.make(data.length, itemsOnPage);
              getList(getListData());
        }
        , error : function(){
              console.log("List Error!!!");
        }
    });

    $("form[name='writeForm']").submit(function(){
       console.log("writeForm Submit");
        var formData = $(this).serialize();
        console.log(formData);

        $.ajax({
            url : "/rest/todo/regist",
            dataType: "json",
            data :formData,
            type : "post",
            success : function(data){
                setListData(data);
                setCurrentPage(1);
                pagination1.make(data.length, itemsOnPage, 1);
                getList(getListData());
                formReset();
            }
            , error: function(data){
                console.log("조회 실패");
            }
        });
        return false;
    });

    $("form[name='searchForm']").submit(function(){
        console.log("searchForm Submit");
        var formData = $(this).serialize();
        console.log(formData);

        $.ajax({
            url : "/rest/todo/search",
            dataType: "json",
            data :formData,
            type : "post",
            success : function(data){
                setListData(data);
                setCurrentPage(1);
                pagination1.make(data.length, itemsOnPage, 1);
                getList(getListData());
                formReset();
            }
            , error: function(data){
                console.log("조회 실패");
            }
        });
        return false;
    });

    $(".btnCheck").button({
        icon : "ui-icon-check",
        showLabel : false
    });
    $(".btnPencil").button({
        icon : "ui-icon-pencil",
        showLabel : false
    });
    $(".btnTrash").button({
        icon : "ui-icon-trash",
        showLabel : false
    });
    $(".btnCancel").button();

});
function formReset(){
    var content = $("input[name='content']");
    content.val("");
    content.focus();
}
function pageClick1(pageNumber) {
    $("#page-number-1").text(pageNumber);
}

var currentPage = 1;
function getCurrentPage(){
    return currentPage;
}
function setCurrentPage(page) {
    currentPage = page;
}
function getListData(){
    return listData;
}
function setListData(data){
    listData = data;
}

function getList(data){
    var begin = (getCurrentPage() - 1) * itemsOnPage;
    var end;
    if ((data.length - begin) < itemsOnPage) {
        end = data.length - 1;
    } else {
        end = (itemsOnPage * getCurrentPage()) - 1;
    }

    $("#listPage li").remove();
    for (var index = begin; index <= end; index++) {
        var vo = data[index];
        var html = getListLI(vo);
        $("#listPage").append(html);
    }

    $(".btnCheck").click(function(){
        var todo_no = $(this).parents("li").attr("data");
        var val = $("input[name='content']").val();
        val += "@"+ todo_no +" ";
        $("input[name='content']").val(val);
        $("input[name='content']").focus();

    });

    $(".btnTrash").click(function(){
        var todo_no = $(this).parents("li").attr("data");

        if (confirm("정말 삭제하시겠습니까?")){
            $.ajax({
                url : "/rest/todo/delete",
                dataType: "json",
                data :{
                    "no" : todo_no
                },
                type : "post",
                success : function(data){
                    setListData(data);
                    getList(getListData());
                }
                , error: function(data){
                    console.log("조회 실패");
                }
            })
        } else {
            return false;
        }
    });

    $("input[name='complete_yn']").click(function(){
        var todo_no = $(this).val();

        if ($(this).prop("checked") == true) {
            $.ajax({
                url : "/rest/todo/complete",
                dataType: "json",
                data :{
                    "no" : todo_no
                },
                type : "post",
                success : function(data){
                    setListData(data);
                    getList(getListData());
                }
                , error: function(data){
                    console.log("조회 실패");
                }
            });
        }
        else {
            $.ajax({
                url : "/rest/todo/rollback",
                dataType: "json",
                data :{
                    "no" : todo_no
                },
                type : "post",
                success : function(data){
                    setListData(data);
                    getList(getListData());
                }
                , error: function(data){
                    console.log("조회 실패");
                }
            });
        }
    });

    $(".btnPencil").click(function(){
        var todo_no = $(this).parents("li").attr("data");
        var content = $(this).parents("li").children("label").children("b").text().trim();

        $("input[name='todo_no']").val(todo_no);
        $("input[name='content']").val(content);
    });

    $(".datepicker").datepicker();
}

function pageClick(pageNumber) {
    setCurrentPage(pageNumber);
    getList(getListData());
}

function getListLI(vo) {
    var cnt = parseInt(vo.cnt);
    var chkClass = "";
    var html = "" +
        "<li data='"+ vo.todo_no +"'>" +
        "   <input type='checkbox' id='complete_"+ vo.todo_no +"' name='complete_yn' value='"+ vo.todo_no +"' id='complete'" ;
        if (cnt > 0 && vo.complete_dt == null) {
            html += " disabled";
        }
        if (vo.complete_dt != null) {
            html += "checked";
            chkClass = "checking";
        }
        html += " />&nbsp;" +
        "   <label class='"+ chkClass+"' for='complete_"+ vo.todo_no +"'>" +
        "   "+ vo.todo_no +". <b>"+ tagReplace(vo.content) +"</b> " +
        "   </label>" +
        "   <span>" ;
        if (vo.content.indexOf("@") < 0) {
            html += "<button type='button' class='btnCheck' title='체크추가'><span class='ui-icon ui-icon-check'></span></button>";
        }
        html += "<button type='button' class='btnPencil' title='수정'><span class='ui-icon ui-icon-pencil'></span></button>" +
        "<button type='button' class='btnTrash' title='삭제'><span class='ui-icon ui-icon-trash'></span></button></span>" +
        "   <em>"+ new Date(vo.input_dt).toISOString().split("T")[0];
        if (vo.complete_dt != null) {
            html += " ~ "+ new Date(vo.complete_dt).toISOString().split("T")[0];
        }
        html += "</em>" +
        "</li>";
    return html;
}

function tagReplace(str){
    // var reg = new RegExp("/@/gi");
    var replacement = str.replace(/@[0-9]*/gi, "<strong>$&</strong>");

    return replacement;

}

