docData={
    //source data
    table:[],//表格数据（二维数组）
    tableHeader:[],//表格标题数据（一维数组）

    //magic value
    questionTypeId:["0","1","2","3"],
    tableHeaderDefault:[
        ["玩家ID","昵称","邮箱","密码"],
        [],
        ["问题ID","问题","问题种类","答案1","答案2","答案3","答案4","正确答案"]
    ],

    //operator data
    tableModel:0,//0--玩家管理，1--游戏管理，2--题库管理
    clickNum:[],//保存哪些行被选中
    clickCell:[]//保存修改过哪些行的数据

};
// var URL="192.168.1.100";
var URL="localhost";



//游戏逻辑控制
/**
 * 初始化当前界面
 */
var init=function () {
    getTableData();

    //点击删除按钮
    document.getElementById("deleteButton").addEventListener("click",function () {
        deleteClick();
    });

    //点击玩家管理
    document.getElementById("mainTable-menu0").addEventListener("click",function () {
        docData.tableModel=0;
        getTableData();
    });

    //点击题库管理
    document.getElementById("mainTable-menu2").addEventListener("click",function () {
        docData.tableModel=2;
        getTableData();
    });


};

/**
 * 展示当前页面
 */
var showByTable=function () {
    var table=docData.table;
    var tableDom=document.getElementById("table");
    tableDom.innerHTML="";

    //创建表格标题栏
    var trDom=document.createElement("tr");
    var tableHeaderDefault=docData.tableHeaderDefault[docData.tableModel];
    for(var i=0;i<=tableHeaderDefault.length;i++){
        if(i==0){
            var thDOM=document.createElement("th");
            thDOM.innerHTML=" ";
            trDom.append(thDOM);
        }
        else{
            var thDOM=document.createElement("th");
            thDOM.innerHTML=tableHeaderDefault[i-1];
            trDom.append(thDOM);
        }
    }
    tableDom.append(trDom);


    //创建表格
    for(var i=0;i<table.length;i++){
        var tableRow=table[i];
        trDom=document.createElement("tr");
        for(var j=0;j<=tableRow.length;j++){
            if(j==0){
                var tdDom=document.createElement("td");
                tdDom.setAttribute("id","r"+i+"c"+j);
                (function (arg1,arg2) {
                    tdDom.addEventListener("click",function () {
                        tableCellClick(arg1,arg2);
                    });
                })(i,j);
                if(docData.clickNum[i]){
                    tdDom.className="tdClick";
                }
                else{
                    tdDom.className="tdUnclick";
                }
                tdDom.innerHTML="";
                trDom.append(tdDom);
            }
            else{
                var tdDom=document.createElement("td");
                tdDom.setAttribute("id","r"+i+"c"+j);
                (function (arg1,arg2) {
                    tdDom.addEventListener("click",function () {
                        tableCellClick(arg1,arg2);
                    });
                })(i,j);
                tdDom.innerHTML=tableRow[j-1];
                tdDom.setAttribute("contenteditable","true");
                trDom.append(tdDom);
            }
        }
        tableDom.append(trDom);
    }
};


//ajax回调函数处理
var getTableDataHandler=function (responseText) {
    var jsonObject=JSON.parse(responseText);
    switch (jsonObject.command){
        case "usersInfo":{
            docData.table=[];
            docData.clickNum=[];
            docData.clickCell=[];
            var users=jsonObject.users;
            for(var i=0;i<users.length;i++){
                var user=[];
                user[i]=[];
                user[i][0]=users[i]["userId"];
                user[i][1]=users[i]["nickname"];
                user[i][2]=users[i]["userAccount"];
                user[i][3]=users[i]["pw"];
                docData.table.push(user[i]);
                docData.clickNum.push(false);
            }
            break;
        }
        case "selectQuestion":{
            docData.table=[];
            docData.clickNum=[];
            docData.clickCell=[];
            var questions=jsonObject.questions;
            console.log(questions);
            for(var i=0;i<questions.length;i++){
                var question=[];
                question[i]=[];
                question[i][0]=questions[i]["questionId"];
                question[i][1]=questions[i]["questionInfo"];
                question[i][2]=questions[i]["questionType"];
                question[i][3]=questions[i]["answerA"];
                question[i][4]=questions[i]["answerB"];
                question[i][5]=questions[i]["answerC"];
                question[i][6]=questions[i]["answerD"];
                question[i][7]=questions[i]["answerRight"];
                docData.table.push(question[i]);
                docData.clickNum.push(false);
            }
            break;
        }
        default:
            break;
    }
    showByTable();
};


//绑定的函数
/**
 * 点击表单的表格
 * @param i
 * @param j
 */
var tableCellClick=function (i,j) {
    var tr=document.getElementById("r"+i+"c"+j);
    if(j==0){
        //第一列
        docData.clickNum[i]=!docData.clickNum[i];
        if(docData.clickNum[i]){
            tr.className="tdClick";
        }
        else{
            tr.className="tdUnclick";
        }
    }
    else{
        //其他列
        var handDom={i:i,j:j};
        docData.clickCell.push(handDom);
        console.log(docData.clickCell);
    }
};

/**
 * 获取表单信息
 */
var getTableData=function () {
    var xhr=new XMLHttpRequest();
    xhr.open("post","http://"+URL+":8080/BackManager");
    xhr.setRequestHeader("Content-Type","application/x-www-form-urlencoded;");
    xhr.withCredentials = true;
    xhr.onreadystatechange=function () {
        if(xhr.readyState===4){
            if(xhr.status===200){
                getTableDataHandler(xhr.responseText);
            }
        }
    };
    switch (docData.tableModel){
        case 0:{
            xhr.send("command=selectUser");
            break;
        }
        case 2:{
            xhr.send("command=selectQuestion");
            break;
        }
        default:
            break;
    }
};

/**
 * 点击删除按钮
 */
var deleteClick=function () {
    var deleteItems=[];
    switch (docData.tableModel){
        case "0":{
            console.log(0);
            break;
        }
        default:
            break;
    }
};


init();











