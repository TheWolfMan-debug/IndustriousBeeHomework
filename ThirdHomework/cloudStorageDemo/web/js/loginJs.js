//在页面加载完成后
$(function () {
    //登录请求
    loginRequest();
    //注册请求
    registerPageRequest();
    //当按下enter键时登录
    enterDown();
});

//登录请求
function loginRequest() {
    $("#login").click(function () {
        //获取username文本输入框的值
        let username = $("#username").val();
        let password = $("#password").val();
        //发送ajax请求
        $.get(
            "/wolf/loginServlet",
            {username: username, password: password},
            function (data) {
                //判断userExist键的值是否是true
                let tip = $("#s_username");
                let userExist = data.userExist;
                if (userExist) {
                    //用户名存在
                    tip.css("color", "green");
                    tip.html("登录成功！");

                    tip.fadeIn(2000);
                    tip.fadeOut(2000);


                    setTimeout("window.location.href = \"/wolf/menuPage.html\"", 1000);
                } else {
                    //用户名不存在
                    tip.css("color", "red");
                    tip.html("登录失败，用户名或密码错误！");
                    tip.fadeIn(2000);
                    tip.fadeOut(2000);

                }
            });

    });
}

//注册请求
function registerPageRequest() {
    $("#register").click(function () {
        window.location.href = "/wolf/registerPage.html";
    });
}

//当按下enter键时登录
function enterDown() {
    $(document).keydown(function (event) {
        if (event.keyCode == 13) {
            $("#login").click();
        }
    });
}




