//全局变量
var cartoonPersonPosition={'4':[
    [1,2,3,2,5,2,7,2],
    [8.7,0,10.7,0,12.7,0,14.7,0],
    [15.2,1,17.2,1,19.2,1,21.2,1],
    [22.2,0,24.2,0,26.2,0,28.2,0],
    [31.2,1.6,33.2,1.6,35.2,1.6,37.2,1.6],
    [32.2,10,34.2,10,36.2,10,38.2,10],
    [22.8,8.7,24.8,8.7,26.8,8.7,28.8,8.7],
    [14.8,9.2,16.8,9.2,18.8,9.2,20.8,9.2],
    [7.8,8.7,9.8,8.7,11.8,8.7,13.8,8.7],
    [1,10.2,3,10.2,5,10.2,7,10.2]
],"3":[
    [1.8,2,3.8,2,5.8,2],
    [9.5,0,11.5,0,13.5,0],
    [16,1,18,1,20,1],
    [23,0,25,0,27,0],
    [32,1.6,34,1.6,36,1.6],
    [33,10,35,10,37,10],
    [23.6,8.7,25.6,8.7,27.6,8.7],
    [15.6,9.2,17.6,9.2,19.6,9.2],
    [8.6,8.7,10.6,8.7,12.6,8.7],
    [1.8,10.2,3.8,10.2,5.8,10.2]
],"2":[
    [3,2,5,2],
    [10.7,0,12.7,0],
    [17.2,1,19.2,1],
    [24.2,0,26.2,0],
    [33.2,1.6,35.2,1.6],
    [34.2,10,36.2,10],
    [24.8,8.7,26.8,8.7],
    [16.8,9.2,18.8,9.2],
    [9.8,8.7,11.8,8.7],
    [3,10.2,5,10.2]
],"1":[
    [3.8,2],
    [11.5,0],
    [18,1],
    [25,0],
    [34,1.6],
    [35,10],
    [25.6,8.7],
    [17.6,9.2],
    [10.6,8.7],
    [3.8,10.2]
]};
var websocket = null;
var docData= {
    roomIdError:999999,
    maxMemberPerRoom: 4,
    currentPoint: 2,
    roomUserIds:[],
    roomUserPosition:[0,0,0,0],
    roomUserClose:[false,false,false,false],
    roomUserId:"",
    roomUserPrepare: false,
    gameStart:false,
    turnPosition: 0,
    turnAngel: 0,
    turnStart: false,
    countDownStart: false,
    questionInfo:{},
    answer:false,
    startTimeDown:{}
};


//判断当前浏览器是否支持WebSocket
if('WebSocket' in window){
    websocket = new WebSocket("ws://"+URL+":8080/playGame");
} else{
    alert('Not support websocket');
}


/**
 * 监听窗口关闭事件，当窗口关闭时，主动去关闭websocket连接，防止连接还没断开就关闭窗口，server端会抛异常。
 */
window.onbeforeunload = function(){
    websocket.close();
};

//连接发生错误的回调方法
websocket.onerror = function(){
    exitHandler();
};

//连接成功建立的回调方法
websocket.onopen = function(){
    init();
};

//接收到消息的回调方法
websocket.onmessage = function(event){
    messageController(event.data)
};

//连接关闭的回调方法
websocket.onclose = function(){
    console.log("close");
};

/**
 * websocket处理相应事件控制器
 * @param data
 */
var messageController=function (data) {
    var JSONObject=JSON.parse(data);
    console.log(JSONObject);
    switch (JSONObject["commandBack"]){
        case "exit":{
            exitHandler();
            break;
        }
        case "upgradeMember":{
            upgradeMember(JSONObject);
            break;
        }
        case "backToLobby":{
            backToLobby();
            break;
        }
        case "roomFull":{
            toast("房间已满或错误的操作，请重新进入房间");
            backToLobby();
            break;
        }
        case "setUserId":{
            settingUserId(JSONObject);
            break;
        }
        case "prepareFinish":{
            setMainBoardPrompt(3);
            docData.roomUserPrepare=true;
            break;
        }
        case "startPlay":{
            startPlay(JSONObject);
            break;
        }
        case "turnAngelResult":{
            console.log(JSONObject);
            setTurnAngel(JSONObject.angel);
            docData.questionInfo=JSONObject.question;
            setTimeout(function () {
                setCartoonPosition(JSONObject.angel,JSONObject.userId);
                setQuestion();
            },3000);
            break;
        }
        case "selectResult":{
            selectAnswerHandler(JSONObject);
            break;
        }
        case "turnAngelBlackHouse":{
            //设置旋转角度
            setTurnAngel(JSONObject.angel);
            setTimeout(function () {
                turnAngelBlackHouse(JSONObject);
            },3000);
            break;
        }
        case "success":{
            //有人获胜
            someoneSuccess(JSONObject);
            break;
        }
        default:
            break;
    }
};

/**
 * 处理页面退出的函数
 */
var exitHandler=function () {
    toast("未登陆，请重新登陆~");
    setTimeout(function () {
        window.location.href="http://"+URL+":8080/login.html";
    },2000);
};

//view控制
/**
 * 弹出信息
 * @param info 需要弹出的信息
 */
var toast=function (info) {
    if(!info){
        info="Welcome";
    }
    document.getElementById("mainContent-toastBox").style.cssText="display:block";
    setTimeout(function () {
        document.getElementById("mainContent-toastBox").style.cssText="display:block;opacity: 1;";
        document.getElementById("mainContent-toastBox").children[0].innerHTML=info;
    },0);
    setTimeout(function () {
        document.getElementById("mainContent-toastBox").style.cssText="display:block;opacity: 0;";
    },1000);
    setTimeout(function () {
        document.getElementById("mainContent-toastBox").style.cssText="";
        document.getElementById("mainContent-toastBox").children[0].innerHTML="Welcome";
    },1300);
};
/**
 * 初始化
 */
var init=function () {
    //初始化已经进入房间的session
    var tableId=window.location.search.substr(1);
    if(!tableId){
        tableId=docData.roomIdError;
    }
    websocket.send("{\"command\":\"init\",\"position\":\"gamePanel\",\"tableId\":"+tableId+"}");

    //退出当前房间
    document.getElementById("back").addEventListener("click",function () {
        websocket.send("{\"command\":\"backToLobby\",\"tableId\":\""+tableId+"\"}");
    });

    //转动色子
    document.getElementById("dice").addEventListener("click",function () {
        if(docData.gameStart){
            if(docData.turnPosition===getPosition()){
                if(docData.turnStart){
                    toast("已经转过转盘啦~");
                }
                else{
                    docData.turnStart=true;
                    startThisTurn();
                }
            }
            else{
                toast("还在其他人的回合，请耐心等待~");
            }
        }
        else{
            toast("游戏尚未开始，请等待所有玩家准备~");
        }
    });

    //开始游戏
    document.getElementById("start").addEventListener("click",function () {
        if(docData.gameStart){
            toast("游戏已经开始，无需准备");
        }
        else{
            if(docData.roomUserPrepare){
                toast("您已准备，无需重新准备")
            }
            else{
                prepare();
            }
        }
    });

    //选中答案
    var answers=document.getElementsByClassName("mainBoard-answerInfoSelect");
    for(var i=0;i<answers.length;i++){
        (function (i) {
            answers[i].addEventListener("click",function () {
                selectAnswer(i);
            });
        })(i);
    }
};
/**
 * 处理成员数据更新
 */
var upgradeMember=function (JSONObject) {
    for(var i=0;i<docData.maxMemberPerRoom;i++){
        document.getElementsByClassName("mainRanking-player")[i].style.cssText="display:none";
    }
    JSONObject.data.forEach(function (t,index) {
        document.getElementsByClassName("mainRanking-player")[index].style.cssText="";
        document.getElementsByClassName("mainRanking-player")[index].children[0].innerHTML="•"+t;
    });
    docData.roomUserIds=JSONObject["ids"];
    console.log(docData);
};
/**
 * 处理成员数据更新
 */
var settingUserId=function (JSONObject) {
    docData.roomUserId=JSONObject["data"];
};
/**
 * 当点击退出按钮以后
 */
var backToLobby=function () {
    if(docData.gameStart){
        toast("游戏已经开始，请不要退出");
    }
    else{
        setTimeout(function () {
            window.location.href="http://"+URL+":8080/gameLobby.html";
        },1000);
    }
};
/**
 * 获取当前的用户位置 position
 * @returns {number} 用户的位置
 */
var getPosition=function (userId) {
    if(!userId){
        userId=docData.roomUserId;
    }
    var result=4;
    docData.roomUserIds.forEach(function (t,i) {
        if(t===userId){
            result=i;
        }
    });
    return result;
};
/**
 * 用户开始这一轮
 */
var startThisTurn=function () {
    var angel=Math.floor(6*Math.random())+1;
    console.log("angel:"+angel);
    websocket.send("{\"command\":\"turnCrane\",\"angel\":\""+angel+"\"}");
};
/**
 * 根据需要抛掷到的点数确定旋转的角度
 * @param num
 */
var setTurnAngel=function (num) {
    var nowNum=(docData.turnAngel/60+2)%6;
    var rotateNum=num>nowNum?(num-nowNum):(num-0+6-nowNum);
    var rotateAngel=rotateNum*60;
    var angel=rotateAngel+docData.turnAngel+360*2;
    docData.turnAngel=angel;
    document.getElementById("dice").style.cssText="transform: rotate(-"+angel+"deg);";
};
/**
 * 设置所有的小人的位置，roomUserPosition为轨道上的位置
 * @param num 当前用户的需要改变的位置
 * @param userId 需要改变的userId
 */
var setCartoonPosition=function (num,userId) {
    var positionId=getPosition(userId);
    docData.roomUserPosition[positionId]=((docData.roomUserPosition[positionId]-0)+(num-0))%10;
    var cartoonIds=["personRed","personYellow","personGreen","personBlue"];
    var presHandResult=preHandCartoonPosition();
    setTimeout(function () {
        cartoonIds.forEach(function (t,i) {
            (function (t,i) {
                var opacity=document.getElementById(t).style.opacity;
                document.getElementById(t).style.cssText="transition: all 1s;opacity: 0;";
                document.getElementById(t).style.cssText="transition: all 1s;left: "+presHandResult[i].left+"em;top: "+presHandResult[i].top+"em;";
                document.getElementById(t).style.opacity=opacity;

            })(t,i);
        })
    },0);
};
/**
 * 对小人的位置进行预处理，获取每个小人的left和top
 * @returns {Array}
 */
var preHandCartoonPosition=function () {
    var countMap={};
    docData.roomUserPosition.forEach(function (t) {
        if(countMap[t]==null){
            countMap[t]=1;
        }
        else{
            countMap[t]=countMap[t]+1;
        }
    });
    var countIndexMap={};
    var result=[];
    docData.roomUserPosition.forEach(function (t) {
        if(countIndexMap[t]==null){
            countIndexMap[t]=0;
        }
        var count=countMap[t];
        var left=cartoonPersonPosition[count][t][2*countIndexMap[t]];
        var top=cartoonPersonPosition[count][t][2*countIndexMap[t]+1];
        countIndexMap[t]=countIndexMap[t]+1;
        result.push({left:left,top:top});
    });
    console.log(result);
    return result;
};
/**
 * 开始倒数计时
 */
var startCountDown=function (num) {
    if(!docData.countDownStart){
        docData.countDownStart=true;
        if(!num)
            document.getElementById("countDown").innerHTML="15s";
        else
            document.getElementById("countDown").innerHTML="3s";
        var countDonwInner=setInterval(function () {
            var nowTime=document.getElementById("countDown").innerHTML.split("s")[0];
            nowTime=nowTime-1;
            if(nowTime===0){
                clearInterval(countDonwInner);
                docData.countDownStart=false;
                nowTime=0;
            }
            document.getElementById("countDown").innerHTML=nowTime+"s";
        },1000);
        docData.startTimeDown=countDonwInner;
    }
};
/**
 * 更改对应位置上的人进入小黑屋的状态
 * @param positionId 位置id
 */
var changeUserClose=function (positionId) {
    docData.roomUserClose[positionId]=!docData.roomUserClose[positionId];
    var cartoonIds=["personRed","personYellow","personGreen","personBlue"];
    if(docData.roomUserClose[positionId]){
        document.getElementById("mainBlackHouse-cartoonPerson"+positionId).style.opacity="1";
        document.getElementById(cartoonIds[positionId]).style.opacity="0";
    }
    else{
        document.getElementById("mainBlackHouse-cartoonPerson"+positionId).style.opacity="0";
        document.getElementById(cartoonIds[positionId]).style.opacity="1";
    }
};
/**
 * 当前玩家准备
 */
var prepare=function () {
    if(!docData.gameStart)
        websocket.send("{\"command\":\"prepare\"}");
};
/**
 * 开始游戏
 */
var startPlay=function () {
    setMainBoardPrompt(4);
    docData.gameStart=true;
    setTimeout(function () {
        if(docData.turnPosition===getPosition())
            toast("点击转盘，开始转动它吧~");
    },3000);

    //设置标志小人的大小
    for(var i=0;i<4;i++){
        console.log(docData.turnPosition==i);
        if(docData.turnPosition==i){
            document.getElementsByClassName("mainRanking-cartoonPerson")[docData.turnPosition].style.cssText="transform: scale(1.5);transition: all 1s;";
        }
        else{
            document.getElementsByClassName("mainRanking-cartoonPerson")[i].style.cssText="transform: scale(1.0);transition: all 1s;";
        }
    }
};
/**
 * 设置答题面板的提示
 */
var setMainBoardPrompt=function (num) {
    document.getElementById("countDown").style.cssText="display:none";
    switch (num){
        case 0:{
            //答对了
            document.getElementById("mainBoard-info1").innerHTML="答对啦！收获一枚金币哦";
            document.getElementById("mainBoard-info2").innerHTML="(^з^)-☆";
            break;
        }
        case 1:{
            //打错了
            document.getElementById("mainBoard-info1").innerHTML="答错了，被关进小黑屋...";
            document.getElementById("mainBoard-info2").innerHTML="_(:_」∠)_";
            break;
        }
        case 2:{
            //初始化界面
            document.getElementById("mainBoard-info1").innerHTML="请准备，游戏马上开始~";
            document.getElementById("mainBoard-info2").innerHTML="(^з^)-☆";
            break;
        }
        case 3:{
            //已经准备
            document.getElementById("mainBoard-info1").innerHTML="已准备，等待其他玩家准备";
            document.getElementById("mainBoard-info2").innerHTML="(^з^)-☆";
            break;
        }
        case 4:{
            //所有玩家都准备了，游戏马上开始
            document.getElementById("countDown").style.cssText="display:block";
            document.getElementById("mainBoard-info1").innerHTML="所有玩家均已准备，游戏即将开始~";
            document.getElementById("mainBoard-info2").innerHTML="(^з^)-☆";
            startCountDown(3);
        }
        default:
            break;
    }
    document.getElementById("mainBoard-infoBoard").style.cssText="display:block";
    document.getElementById("mainBoard-question").style.cssText="display:none";
};
/**
 * 根据docData显示问题题目
 */
var setQuestion=function () {
    document.getElementById("questionInfo").innerHTML=docData.questionInfo.questionInfo;
    document.getElementById("answerA").innerHTML=docData.questionInfo.answerA;
    document.getElementById("answerB").innerHTML=docData.questionInfo.answerB;
    document.getElementById("answerC").innerHTML=docData.questionInfo.answerC;
    document.getElementById("answerD").innerHTML=docData.questionInfo.answerD;
    document.getElementById("mainBoard-infoBoard").style.cssText="display:none";
    document.getElementById("mainBoard-question").style.cssText="display:block";
    for(var i=0;i<4;i++){
        document.getElementsByClassName("mainBoard-answerInfoSelect")[i].className="mainBoard-answerInfoSelect";
    }
    docData.answer=true;
    startCountDown();
};
/**
 * 选中答案
 */
var selectAnswer=function (num) {
    if(docData.turnPosition==getPosition()){
        if(docData.answer){
            docData.answer=false;
            for(var i=0;i<4;i++){
                if(i==num){
                    document.getElementsByClassName("mainBoard-answerInfoSelect")[i].className="mainBoard-answerInfoSelect mainBoard-answerInfoSelectActive";
                }
                else{
                    document.getElementsByClassName("mainBoard-answerInfoSelect")[i].className="mainBoard-answerInfoSelect";
                }
            }
            var tableId=window.location.search.substr(1);
            if(!tableId){
                tableId=docData.roomIdError;
            }
            var rightAnswer=docData.questionInfo.answerRight;
            var result=false;
            if(rightAnswer==num){
                result=true;
            }
            websocket.send("{\"command\":\"selectAnswer\",\"answer\":"+num+",\"result\":"+result+",\"tableId\":"+tableId+"}");
        }
    }
    else{
        toast("还没有到您的回合，请稍后作答~");
    }
};
/**
 * 选中完答案后的处理
 * @param jsonObject
 */
var selectAnswerHandler=function (jsonObject) {
    stopCountDown();
    //设置选中的答案
    for(var i=0;i<4;i++){
        if(i==jsonObject.answer){
            document.getElementsByClassName("mainBoard-answerInfoSelect")[i].className="mainBoard-answerInfoSelect mainBoard-answerInfoSelectActive";
        }
        else{
            document.getElementsByClassName("mainBoard-answerInfoSelect")[i].className="mainBoard-answerInfoSelect";
        }
    }
    //设置分数
    for(var i=0;i<4;i++){
        document.getElementsByClassName("mainRanking-playerScore")[i].innerHTML=jsonObject.scores[i];
    }
    //设置是否关到小黑屋
    if(!jsonObject.result){
        var positionId=getPosition(jsonObject.userId);
        console.log(positionId);
        changeUserClose(positionId);
    }
    //设置轮次
    docData.turnPosition=jsonObject.turn;

    //设置标志小人的大小
    for(var i=0;i<4;i++){
        console.log(docData.turnPosition==i);
        if(docData.turnPosition==i){
            document.getElementsByClassName("mainRanking-cartoonPerson")[docData.turnPosition].style.cssText="transform: scale(1.5);transition: all 1s;";
        }
        else{
            document.getElementsByClassName("mainRanking-cartoonPerson")[i].style.cssText="transform: scale(1.0);transition: all 1s;";
        }
    }
    //开始下一轮
    if(docData.turnPosition==getPosition()){
        toast("请转动转盘~！");
    }
    docData.turnStart=false;
};
/**
 * 关进房子以后的转盘的判断
 * @param jsonObject
 */
var turnAngelBlackHouse=function (jsonObject) {
    //设置是否离开小黑屋
    if(jsonObject.outBlackHouse){
        var positionId=getPosition(jsonObject.userId);
        changeUserClose(positionId);
    }
    //设置轮次
    docData.turnPosition=jsonObject.turn;

    //设置标志小人的大小
    for(var i=0;i<4;i++){
        console.log(docData.turnPosition==i);
        if(docData.turnPosition==i){
            document.getElementsByClassName("mainRanking-cartoonPerson")[docData.turnPosition].style.cssText="transform: scale(1.5);transition: all 1s;";
        }
        else{
            document.getElementsByClassName("mainRanking-cartoonPerson")[i].style.cssText="transform: scale(1.0);transition: all 1s;";
        }
    }
    //开始下一轮
    if(docData.turnPosition==getPosition()){
        toast("请转动转盘~！");
    }
    docData.turnStart=false;
};
/**
 * 停止计时
 */
var stopCountDown=function () {
    clearInterval(docData.startTimeDown);
    docData.countDownStart=false;
    var nowTime=0;
    document.getElementById("countDown").innerHTML=nowTime+"s";
};
/**
 * 有人分数达到6分
 * @param jsonObject
 */
var someoneSuccess=function (jsonObject) {
    var positionId=jsonObject.positionId;
    var positionName=["一","二","三","四"];
    stopCountDown();
    document.getElementById("mainBoard-info1").innerHTML="恭喜玩家"+positionName[positionId]+"获得胜利";
    document.getElementById("mainBoard-info2").innerHTML="(^з^)-☆";
    document.getElementById("mainBoard-infoBoard").style.cssText="display:block";
    document.getElementById("mainBoard-question").style.cssText="display:none";
    document.getElementsByClassName("mainRanking-playerScore")[positionId].innerHTML="6";
    toast("请重新准备~!");
    initWithData();
};
/**
 * 保留数据地初始化
 */
var initWithData=function () {
    document.getElementById("dice").style.cssText="transform: rotate(0deg);transition: none;";
    var roomUserIds=docData.roomUserIds;
    var roomUserId=docData.roomUserId;
    var turnAngel=docData.turnAngel;
    docData= {
        roomIdError:999999,
        maxMemberPerRoom: 4,
        currentPoint: 2,
        roomUserIds:[],
        roomUserPosition:[0,0,0,0],
        roomUserClose:[false,false,false,false],
        roomUserId:"",
        roomUserPrepare: false,
        gameStart:false,
        turnPosition: 0,
        turnAngel: 0,
        turnStart: false,
        countDownStart: false,
        questionInfo:{},
        answer:false,
        startTimeDown:{}
    };
    docData.roomUserId=roomUserId;
    docData.roomUserIds=roomUserIds;
    docData.turnAngel=turnAngel;
    var cartoonIds=["personRed","personYellow","personGreen","personBlue"];
    for(var i=0;i<4;i++){
        document.getElementById("mainBlackHouse-cartoonPerson"+i).style.opacity="0";
        document.getElementById(cartoonIds[i]).style.cssText="opacity: 1;"
    }
};








