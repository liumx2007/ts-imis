/**
 * Created by sakfi on 2017/8/30.
 */
(function() {
    angular
        .module('WEBAPP.JFRECORD.CONTROLLER', ['ui.bootstrap'])
        .controller('JfRecordCtrl', ['$http','$scope','utils','$filter',function($http,$scope,utils,$filter) {
            var selt = this;
            selt.showbutton=false;
            this.submitted=false;
            var code =  utils.getUrlVar('code');
            console.log("====="+code);
            if(!code){
                var url = 'https://open.weixin.qq.com/connect/oauth2/authorize?' +
                    'appid=wx5788f1ce93ff3255' +
                    '&redirect_uri=' + encodeURIComponent(window.location.href) +
                    '&response_type=code&scope=snsapi_base&state=TS-IMIS' +
                    '#wechat_redirect';
                window.location.href = url;
            }else {
                //获取用户信息授权
                $http.get("/authorize/oauth2?code=" + code, null).success(function (result) {
                    if (result.status == 1) {
                        selt.openid = result.openid;
                        console.log("====" + selt.openid);
                        var param={
                            openId:selt.openid
                        }
                        $http.post("/promotion/getPersonByopenId",angular.toJson(param)).success(function (result) {
                            if (result.success) {
                                selt.tbJfPerson = result.object;
                                param.score=selt.tbJfPerson.score;
                                $http.post("/promotion/getJfRecordByOpendId",angular.toJson(param)).success(function (result) {
                                    if (result.success) {
                                        selt.jfRecordList = result.object;
                                    } else {
                                        alert(result.message);
                                    }
                                });

                            } else {
                                alert(result.message);
                            }
                        });

                    } else {
                        alert("授权失败!");
                    }

                });
            }

        }]);
})();
