app.controller('JfLevelCtrl', ['$scope','$http','$log','$modal','$filter', function($scope,$http,$log,$modal,$filter) {
    var selt = this;

    //---页面按钮权限控制--start--
    this.opCodes = [];
    $http.get("/ts-authorize/ts-imis/operList/app-talentPool").success(function (result) {
        if (result.success) {
            selt.opCodes = result.object;
        } else {
            alert(result.message);
        }
    });
    this.isShowOpe = function(value){
        for(var i = 0; i < selt.opCodes.length; i++){
            if(value === selt.opCodes[i]){
                return true;
            }
        }
        return false;
    };
    //-------------------end---


    //--时间控件

    //this.dtStart = $filter("date")(new Date(), "yyyy-MM-dd");
    //this.dtEnd = $filter("date")(new Date(), "yyyy-MM-dd");


    this.openStart = function($event) {
        this.myShaix = false;
        $event.preventDefault();
        $event.stopPropagation();

        selt.openedStart = true;
    };

    this.openEnd = function($event) {
        this.myShaix = false;
        $event.preventDefault();
        $event.stopPropagation();

        selt.openedEnd = true;
    };

    this.dateOptions = {
        formatYear: 'yy',
        startingDay: 1,
        class: 'datepicker'
    };

    this.format = 'yyyy-MM-dd';

    //--时间控件--end

    $http.post("/promotionApp/getCompanyList").success(function (result) {
        if (result.success) {
            selt.companyList = result.object;
        } else {
            alert(result.message);
        }
    });
    this.getDeptList=function(){
        if(selt.companyPkid==undefined) {
            alert("请选择公司")
            return;
        }else{
            var url="/promotionApp/"+selt.companyPkid+"/getDeptList";
            $http.post(url).success(function (result) {
                if (result.success) {
                    selt.deptList = result.object;
                } else {
                    alert(result.message);
                }
            });
        }

    };

    this.searchPersonnel=function(){
        var param={
            "companyId":selt.companyPkid,
            "deptId":selt.deptPkid,
            "name":selt.perName
        };
        $http.post("/jfLevel/queryJfPersonnel",angular.toJson(param)).success(function (result) {
            if(result.success){
                selt.jfPersonList = result.object;
            }else{
                selt.jfPersonList = [];
            }

        });
    };

    this.selectPerson = function (person) {
        selt.person = person;
        selt.dtStart = selt.dtStart==""?"":$filter("date")(selt.dtStart, "yyyy-MM-dd");
        selt.dtEnd = selt.dtStart==""?"":$filter("date")(selt.dtEnd, "yyyy-MM-dd");

        var param={
            "workNum":person.workNum,
            "startDate":selt.dtStart,
            "endDate":selt.dtEnd,
            "pageNo":1,
            "pageSize":5
        };
        $http.post("/jfLevel/seachJfRecord",angular.toJson(param)).success(function (result) {
            if(result.code==1){
                selt.recordList = result.list;
                selt.totalCount = result.totalCount;
                selt.pageSize = result.pageSize;
                selt.pageNo = result.pageNo;
            }else{
                selt.recordList = [];
            }

        });
    };

    this.maxSize = 3;
    this.recordList = [];
    this.totalCount = 0;
    this.pageSize = 0;
    this.pageNo = 1;

    this.pageChanged=function(){
        selt.dtStart = selt.dtStart==""?"":$filter("date")(selt.dtStart, "yyyy-MM-dd");
        selt.dtEnd = selt.dtStart==""?"":$filter("date")(selt.dtEnd, "yyyy-MM-dd");
        var param={
            "workNum":selt.person.workNum,
            "startDate":selt.dtStart,
            "endDate":selt.dtEnd,
            "pageNo":this.pageNo,
            "pageSize":5
        };
        $http.post("/jfLevel/seachJfRecord",angular.toJson(param)).success(function (result) {
            if(result.code==1){
                selt.recordList = result.list;
                selt.totalCount = result.totalCount;
                selt.pageSize = result.pageSize;
                selt.pageNo = result.pageNo;
            }else{
                selt.recordList = [];
            }

        });
    };



}])
