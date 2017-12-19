var docData={
    isLogin:true
};

/**
 * 登录按钮事件监听
 */
document.getElementById("login").addEventListener("click",function () {
    docData.isLogin=true;//修改记录数据
    if(docData.isLogin){
        document.getElementById("login").className="mainChoice-active";
        document.getElementById("register").className="mainChoice-inactive";
        document.getElementsByClassName("mainName")[0].style.opacity="0";
        document.getElementsByClassName("mainAccount")[0].style.top="-2em";
        document.getElementsByClassName("mainPw")[0].style.top="-2em";
    }
    else{
        document.getElementById("login").className="mainChoice-inactive";
        document.getElementById("register").className="mainChoice-active";
        document.getElementsByClassName("mainName")[0].style.opacity="1";
        document.getElementsByClassName("mainAccount")[0].style.top="0em";
        document.getElementsByClassName("mainPw")[0].style.top="0em";
    }
});
/**
 * 注册按钮事件监听
 */
document.getElementById("register").addEventListener("click",function () {
    docData.isLogin=false;//修改记录数据
    if(docData.isLogin){
        document.getElementById("login").className="mainChoice-active";
        document.getElementById("register").className="mainChoice-inactive";
        document.getElementsByClassName("mainName")[0].style.opacity="0";
        document.getElementsByClassName("mainAccount")[0].style.top="-2em";
        document.getElementsByClassName("mainPw")[0].style.top="-2em";
    }
    else{
        document.getElementById("login").className="mainChoice-inactive";
        document.getElementById("register").className="mainChoice-active";
        document.getElementsByClassName("mainName")[0].style.opacity="1";
        document.getElementsByClassName("mainAccount")[0].style.top="0em";
        document.getElementsByClassName("mainPw")[0].style.top="0em";
    }
});
/**
 * 发送按钮事件监听
 */
document.getElementById("submit").addEventListener("click",function () {
    //获取需要发送的信息
    var account=document.getElementById("account").value;
    var pw=document.getElementById("pw").value;
    var name=document.getElementById("name").value;
    //ajax发送信息到后台
    var xhr=new XMLHttpRequest();
    xhr.open("post","http://localhost:8080/Login");
    xhr.setRequestHeader("Content-Type","application/x-www-form-urlencoded;");
    xhr.onreadystatechange=function () {
        if(xhr.readyState===4){
            if(xhr.status===200){
                console.log("success");
                console.log(xhr.responseText);
                console.log(JSON.parse(xhr.responseText));
            }
            else{
                console.log("error");
            }
        }
    };
    xhr.send("account="+account+"&pw="+pw+"&name="+name);
});


