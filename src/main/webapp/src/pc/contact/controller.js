/**
 * Created by sakfi on 2017/6/28.
 */
app.controller('Contact', ['$scope','$modal','$http','$filter', function($scope,$modal,$http,$filter) {
    var selt=this;
    this.yearc = $filter("date")(new Date(), "yyyy");
    this.yuec = new Date().getMonth()+1+"月";
    this.countDate = this.yearc+$filter("date")(new Date(), "MM");


    //展示框
    this.selected = ["name", "tagName", "shouldSignNum", "factSignNum",
        "attNum", "lateNum", "backNum", "lackNum",
        "maternityLeave", "annualLeave", "peternituLeave", "maritalLeave", "funeralLeave",
        "affairLeave", "sickLeave", "otherLeave"];
    this.selectedCx = [];
    this.showAll = true;
    this.showname = true;
    this.showtagName = true;
    this.showshouldSignNum = true;
    this.showfactSignNum = true;
    this.showattNum = true;
    this.showlateNum = true;
    this.showbackNum = true;
    this.showlackNum = true;
    this.showmaternityLeave = true;
    this.showannualLeave = true;
    this.showpeternituLeave = true;
    this.showmaritalLeave = true;
    this.showfuneralLeave = true;
    this.showaffairLeave = true;
    this.showsickLeave = true;
    this.showotherLeave = true;
    var updateSelected = function (action, att) {
        if (action == 'add' && selt.selected.indexOf(att) == -1) {
            selt.selected.push(att);
        };
        if (action == 'remove' && selt.selected.indexOf(att) != -1) {
            var idx = selt.selected.indexOf(att);
            selt.selected.splice(idx, 1);
        };
        console.log(selt.selected);

    };

    this.updateSelection = function ($event, att) {
        var checkbox = $event.target;
        var action = (checkbox.checked ? 'add' : 'remove');
        updateSelected(action, att);
    };

    var updateCxSelected = function (action, att) {
        if (action == 'add' && selt.selectedCx.indexOf(att) == -1) {
            selt.selectedCx.push(att);
        };
        if (action == 'remove' && selt.selectedCx.indexOf(att) != -1) {
            var idx = selt.selectedCx.indexOf(att);
            selt.selectedCx.splice(idx, 1);
        };
        console.log(selt.selectedCx);

    };

    this.updateCxSelection=function($event, att){
        var checkbox = $event.target;
        var action = (checkbox.checked ? 'add' : 'remove');
        updateCxSelected(action, att);
    }

    this.isSelected = function (att) {
        return this.selected.indexOf(att) != -1;

    };
    this.selectedReset = function () {
        selt.selected=[];
        console.log(selt.selected);
    };
    this.selectedAll=function(){
        selt.selected=['name','tagName','shouldSignNum','factSignNum','attNum','lateNum','backNum','lackNum','maternityLeave','annualLeave','peternituLeave','maritalLeave','funeralLeave','affairLeave','sickLeave','otherLeave'];
    }
    this.selectedSubmit = function () {
        this.myXians= false;
        selt.showAll = selt.selected.length==16?true:false;
        console.log("===="+selt.showAll);
        selt.showname = selt.selected.indexOf('name') != -1;
        selt.showtagName = selt.selected.indexOf('tagName') != -1;
        selt.showshouldSignNum = selt.selected.indexOf('shouldSignNum') != -1;
        selt.showfactSignNum = selt.selected.indexOf('factSignNum') != -1;
        selt.showattNum = selt.selected.indexOf('attNum') != -1;
        selt.showlateNum = selt.selected.indexOf('lateNum') != -1;
        selt.showbackNum = selt.selected.indexOf('backNum') != -1;
        selt.showlackNum = selt.selected.indexOf('lackNum') != -1;
        selt.showmaternityLeave = selt.selected.indexOf('maternityLeave') != -1;
        selt.showannualLeave = selt.selected.indexOf('annualLeave') != -1;
        selt.showpeternituLeave = selt.selected.indexOf('peternituLeave') != -1;
        selt.showmaritalLeave = selt.selected.indexOf('maritalLeave') != -1;
        selt.showfuneralLeave = selt.selected.indexOf('funeralLeave') != -1;
        selt.showaffairLeave = selt.selected.indexOf('affairLeave') != -1;
        selt.showsickLeave = selt.selected.indexOf('sickLeave') != -1;
        selt.showotherLeave = selt.selected.indexOf('otherLeave') != -1;
        selt.setCountPage(1);
    };
    //------展示框---end---




    this.setCountPage = function (pageNo) {
        var attenceMap = {
            name: selt.countName,
            tagName: selt.tagName,
            countDate: selt.countDate,
            selctCx:selt.selectedCx,
            pageNo: pageNo,
            pageSize: 10
        };
        selt.excelCountExprot="/excel/excelContactExport?name="+selt.countName+"&tagName="+selt.tagName+"&countDate="+selt.countDate+"&column="+selt.selected+"&selectCx="+selt.selectedCx;
        $http.post("/count/attCountData", angular.toJson(attenceMap)).success(function (result) {
            if (result.code == "1") {
                selt.countList = result.list;
                selt.cototalCount = result.totalCount;
                selt.copageSize = result.pageSize;
                selt.copageNo = result.pageNo;
            } else {
                selt.countList = [];
            }
        });
    };




    this.countPageChanged = function () {
        var attenceMap = {
            name: selt.countName,
            tagName: selt.tagName,
            countDate: selt.countDate,
            pageNo: this.copageNo,
            pageSize: 10
        };

        $http.post("/count/attCountData", angular.toJson(attenceMap)).success(function (result) {
            if (result.code == "1") {
                selt.countList = result.list;
                selt.cototalCount = result.totalCount;
                selt.copageSize = result.pageSize;
                selt.copageNo = result.pageNo;
            } else {
                selt.countList = [];
            }
        });
    };











    this.setPage = function (pageNo,workNum) {
        selt.workNum = workNum;
        var attenceMap = {
            workNum: workNum,
            pageNo: pageNo,
            pageSize: 5,
            countDate: this.countDate
        };

        $http.post("/attencelog/getAttenceLogList", angular.toJson(attenceMap)).success(function (result) {
            if (result.code == "1") {
                selt.acctenceLogList = result.list;
                selt.totalCount = result.totalCount;
                selt.pageSize = result.pageSize;
                selt.pageNo = result.pageNo;
            } else {
                selt.acctenceLogList = [];
            }
        });
    };

    this.pageChanged = function () {
        var attenceMap = {
            workNum: selt.workNum,
            pageNo: this.pageNo,
            pageSize: 5,
            countDate: this.countDate
        };
        $http.post("/attencelog/getAttenceLogList", angular.toJson(attenceMap)).success(function (result) {
            if (result.code == "1") {
                selt.acctenceLogList = result.list;
                selt.totalCount = result.totalCount;
                selt.pageSize = result.pageSize;
                selt.pageNo = result.pageNo;
            } else {
                selt.acctenceLogList = [];
            }
        });

    };
    this.maxSize=5;


    this.myYear = false;
    this.myYue = false;
    this.myShaix = false;
    this.myXians= false;
    this.toggle= function() {
        selt.myYear = !selt.myYear;
    };
    this.showdivTog=function(){
        selt.myYear = false;
    };
    this.toggYue= function() {
        selt.myYue = !selt.myYue;
    };
    this.showdivYue=function(){
        selt.myYue = false;
    };
    this.toggShaix= function() {
        selt.myShaix = !selt.myShaix;
    };
    this.showdivShaix=function(){
        selt.myShaix=false;
    }
    this.toggXians= function() {
        selt.myXians = !selt.myXians;
    };
    this.showdivXians=function(){
      selt.myXians=false;
    };
    this.yearList = ["2015","2016","2017"];
    this.changeYear = function (year) {
        selt.yearc = year;
        selt.myYear = false;
    }
    this.yueList = [
        { key:'1月', value:'01' },
        { key:'2月', value:'02' },
        { key:'3月', value:'03' },
        { key:'4月', value:'04' },
        { key:'5月', value:'05' },
        { key:'6月', value:'06' },
        { key:'7月', value:'07' },
        { key:'8月', value:'08' },
        { key:'9月', value:'09' },
        { key:'10月', value:'10' },
        { key:'11月', value:'11' },
        { key:'12月', value:'12' },
    ];
    this.changeYue = function (yue) {
        selt.yuec = yue.key;
        selt.myYue = false;
        selt.countDate = selt.yearc+yue.value;
        selt.setCountPage(1);
        console.log("===="+selt.countDate);
    };
    this.nameStrs = [];
    this.tagNameStrs = [];

    this.submitSeach = function () {
        selt.setCountPage(1);
        this.myShaix = false;
        if(selt.countName!=undefined){
            selt.nameStrs.push(selt.countName);
        };
        if(selt.tagName!=undefined){
            selt.tagNameStrs.push(selt.tagName);
        };


    };

    this.cancel = function () {
        selt.countName = "";
        selt.tagName = "";
    };

    this.panelClass = "contact panel panel-default";

    this.openPanel = function () {
        selt.panelClass = "contact panel panel-default active";
    };
    this.closePanel = function () {
        selt.panelClass = "contact panel panel-default";
    };


    //---页面按钮权限控制--start--
    this.opCodes = [];
    this.isShowOpe = function(value){
        for(var i = 0; i < selt.opCodes.length; i++){
            if(value === selt.opCodes[i]){
                return true;
            }
        }
        return false;
    };
    $http.get("/ts-authorize/ts-imis/operList/app-contact").success(function (result) {
        if (result.success) {
            selt.opCodes = result.object;
            if(!selt.isShowOpe("all")){
                selt.tagName = result.message;
            }
            selt.setCountPage(1);
        } else {
            alert(result.message);
        }
    });
    //-------------------end---





}]);
