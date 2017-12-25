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
    tableHeaderAttribute:[
        ["userId","nickname","userAccount","pw"],
        [],
        ["questionId","questionInfo","questionType","answerA","answerB","answerC","answerD","answerRight"]
    ],

    //operator data
    tableModel:0,//0--玩家管理，1--游戏管理，2--题库管理
    clickNum:[],//保存哪些行被选中
    clickCell:[],//保存修改过哪些行的数据
    addNum:0//增加了多少条记录

};




//游戏逻辑控制
/**
 * 初始化当前界面
 */
var init=function () {
    verifyId();
    getTableData();


    //点击删除按钮
    document.getElementById("deleteButton").addEventListener("click",function () {
        deleteClick();
    });

    //点击增加按钮
    document.getElementById("addButton").addEventListener("click",function () {
        addClick();
    });

    //点击保存按钮
    document.getElementById("saveButton").addEventListener("click",function () {
        saveClick();
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
            docData.addNum=0;
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
            docData.addNum=0;
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

var deleteDataHandler=function () {
    getTableData();
};

var saveDataHandler=function () {
    getTableData();
};

var verifyHandler=function (data) {
    console.log(data);
    if(data=="false"){
        window.location.href="http://"+URL+":8080/login.html";
    }
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
    var sendInfo={};
    var deleteItems=[];
    for(var i=0;i<docData.clickNum.length;i++){
        if(docData.clickNum[i]){
            deleteItems.push(docData.table[i][0]);
        }
    }
    sendInfo.deleteItems=deleteItems;
    switch (docData.tableModel){
        case 0:{
            sendInfo.deleteTable="users";
            var JSONstr=JSON.stringify(sendInfo);
            var xhr=new XMLHttpRequest();
            xhr.open("post","http://"+URL+":8080/BackManager");
            xhr.setRequestHeader("Content-Type","application/x-www-form-urlencoded;");
            xhr.onreadystatechange=function () {
                if(xhr.readyState===4){
                    if(xhr.status===200){
                        deleteDataHandler();
                    }
                }
            };
            xhr.send("command=deleteUsers&data="+JSONstr);
            break;
        }
        case 2:{
            sendInfo.deleteTable="question";
            var JSONstr=JSON.stringify(sendInfo);
            console.log(JSONstr);
            var xhr=new XMLHttpRequest();
            xhr.open("post","http://"+URL+":8080/BackManager");
            xhr.setRequestHeader("Content-Type","application/x-www-form-urlencoded;");
            xhr.onreadystatechange=function () {
                if(xhr.readyState===4){
                    if(xhr.status===200){
                        deleteDataHandler();
                    }
                }
            };
            xhr.send("command=deleteQuestion&data="+JSONstr);
            break;
        }
        default:
            break;
    }
};

/**
 * 点击增加按钮
 */
var addClick=function () {
    var cellNum=0;
    switch (docData.tableModel){
        case 0:{
            cellNum=4;
            break;
        }
        case 2:{
            cellNum=8;
            break;
        }
        default:
            break;
    }
    var tableDom=document.getElementById("table");
    var trDom=document.createElement("tr");
    for(var j=0;j<=cellNum;j++){
        if(j==0){
            var tdDom=document.createElement("td");
            tdDom.setAttribute("id","add_r"+docData.addNum+"c"+j);
            tdDom.className="tdUnclick";
            tdDom.innerHTML="";
            trDom.append(tdDom);
        }
        else{
            var tdDom=document.createElement("td");
            tdDom.setAttribute("id","add_r"+docData.addNum+"c"+j);
            tdDom.innerHTML="";
            tdDom.setAttribute("contenteditable","true");
            trDom.append(tdDom);
        }
    }
    tableDom.append(trDom);
    docData.addNum=docData.addNum+1;
};

/**
 * 点击保存按钮
 */
var saveClick=function () {

    var updatePart=[];
    for(var i=0;i<docData.clickCell.length;i++){
        var updateItemDoc=docData.clickCell[i];
        if(updateItemDoc.j-0>1){
            var attribute=docData.tableHeaderAttribute[docData.tableModel][updateItemDoc.j-1];
            var cellInfo=document.getElementById("r"+updateItemDoc.i+"c"+updateItemDoc.j).innerHTML;
            var userId=docData.table[updateItemDoc.i][0];
            if(attribute==="pw"){
                cellInfo=hex_sha1(cellInfo);
            }
            var updateItem={attribute:attribute,cellInfo:cellInfo,userId:userId};
            updatePart.push(updateItem);
        }
    }

    var addPart=[];
    for(var i=0;i<docData.addNum;i++){
        var addItem=[];
        for(var j=0;j<=docData.tableHeaderAttribute[docData.tableModel].length;j++){
            if(j>1){
                var cellInfo=document.getElementById("add_r"+i+"c"+j).innerHTML;
                if(j===4&&docData.tableModel===0){
                    cellInfo=hex_sha1(cellInfo);
                }
                addItem.push(cellInfo);
            }
        }
        addPart.push(addItem);
    }

    var sendInfo={
        updatePart:updatePart,
        addPart:addPart
    };

    var JSONstr=JSON.stringify(sendInfo);
    var xhr=new XMLHttpRequest();
    xhr.open("post","http://"+URL+":8080/BackManager");
    xhr.setRequestHeader("Content-Type","application/x-www-form-urlencoded;charset=UTF-8");
    xhr.onreadystatechange=function () {
        if(xhr.readyState===4){
            if(xhr.status===200){
                saveDataHandler();
            }
        }
    };
    switch (docData.tableModel){
        case 0:{
            console.log(JSONstr);
            xhr.send("command=saveUser&data="+JSONstr);
            break;
        }
        case 2:{
            xhr.send("command=saveQuestion&data="+JSONstr);
            break;
        }
        default:
            break;
    }
};

/**
 * 验证当前用户的身份
 */
var verifyId=function () {
    var xhr=new XMLHttpRequest();
    xhr.open("post","http://"+URL+":8080/BackManager");
    xhr.setRequestHeader("Content-Type","application/x-www-form-urlencoded;");
    xhr.onreadystatechange=function () {
        if(xhr.readyState===4){
            if(xhr.status===200){
                verifyHandler(xhr.responseText);
            }
        }
    };
    xhr.send("command=verify");
};


init();











